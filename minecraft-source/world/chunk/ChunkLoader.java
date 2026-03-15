/*
 * External method calls:
 *   Lnet/minecraft/util/collection/BoundedRegionArray;create(IIILnet/minecraft/util/collection/BoundedRegionArray$Getter;)Lnet/minecraft/util/collection/BoundedRegionArray;
 *   Lnet/minecraft/world/chunk/ChunkStatus;createOrderedList()Ljava/util/List;
 *   Lnet/minecraft/world/chunk/AbstractChunkHolder;clearLoader(Lnet/minecraft/world/chunk/ChunkLoader;)V
 *   Lnet/minecraft/util/collection/BoundedRegionArray;forEach(Ljava/util/function/Consumer;)V
 *   Lnet/minecraft/world/chunk/ChunkGenerationStep;accumulatedDependencies()Lnet/minecraft/world/chunk/GenerationDependencies;
 *   Lnet/minecraft/util/profiler/Profiler;scoped(Ljava/lang/String;)Lnet/minecraft/util/profiler/ScopedProfiler;
 *   Lnet/minecraft/util/profiler/ScopedProfiler;addLabel(Ljava/util/function/Supplier;)Lnet/minecraft/util/profiler/ScopedProfiler;
 *   Lnet/minecraft/world/chunk/AbstractChunkHolder;generate(Lnet/minecraft/world/chunk/ChunkGenerationStep;Lnet/minecraft/world/ChunkLoadingManager;Lnet/minecraft/util/collection/BoundedRegionArray;)Ljava/util/concurrent/CompletableFuture;
 *   Lnet/minecraft/world/ChunkLoadingManager;acquire(J)Lnet/minecraft/world/chunk/AbstractChunkHolder;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/world/chunk/ChunkLoader;loadAll(Lnet/minecraft/world/chunk/ChunkStatus;Z)V
 *   Lnet/minecraft/world/chunk/ChunkLoader;load(Lnet/minecraft/world/chunk/ChunkStatus;ZLnet/minecraft/world/chunk/AbstractChunkHolder;)Z
 */
package net.minecraft.world.chunk;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import net.minecraft.server.world.OptionalChunk;
import net.minecraft.util.collection.BoundedRegionArray;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.profiler.Profilers;
import net.minecraft.util.profiler.ScopedProfiler;
import net.minecraft.world.ChunkLoadingManager;
import net.minecraft.world.chunk.AbstractChunkHolder;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkGenerationSteps;
import net.minecraft.world.chunk.ChunkStatus;
import net.minecraft.world.chunk.GenerationDependencies;
import org.jspecify.annotations.Nullable;

public class ChunkLoader {
    private final ChunkLoadingManager chunkLoadingManager;
    private final ChunkPos pos;
    private @Nullable ChunkStatus currentlyLoadingStatus = null;
    public final ChunkStatus targetStatus;
    private volatile boolean pendingDisposal;
    private final List<CompletableFuture<OptionalChunk<Chunk>>> futures = new ArrayList<CompletableFuture<OptionalChunk<Chunk>>>();
    private final BoundedRegionArray<AbstractChunkHolder> chunks;
    private boolean allowGeneration;

    private ChunkLoader(ChunkLoadingManager chunkLoadingManager, ChunkStatus targetStatus, ChunkPos pos, BoundedRegionArray<AbstractChunkHolder> chunks) {
        this.chunkLoadingManager = chunkLoadingManager;
        this.targetStatus = targetStatus;
        this.pos = pos;
        this.chunks = chunks;
    }

    public static ChunkLoader create(ChunkLoadingManager chunkLoadingManager, ChunkStatus targetStatus, ChunkPos pos) {
        int i = ChunkGenerationSteps.GENERATION.get(targetStatus).getAdditionalLevel(ChunkStatus.EMPTY);
        BoundedRegionArray<AbstractChunkHolder> lv = BoundedRegionArray.create(pos.x, pos.z, i, (x, z) -> chunkLoadingManager.acquire(ChunkPos.toLong(x, z)));
        return new ChunkLoader(chunkLoadingManager, targetStatus, pos, lv);
    }

    public @Nullable CompletableFuture<?> run() {
        CompletableFuture<?> completableFuture;
        while ((completableFuture = this.getLatestPendingFuture()) == null) {
            if (this.pendingDisposal || this.currentlyLoadingStatus == this.targetStatus) {
                this.dispose();
                return null;
            }
            this.loadNextStatus();
        }
        return completableFuture;
    }

    private void loadNextStatus() {
        ChunkStatus lv;
        if (this.currentlyLoadingStatus == null) {
            lv = ChunkStatus.EMPTY;
        } else if (!this.allowGeneration && this.currentlyLoadingStatus == ChunkStatus.EMPTY && !this.isGenerationUnnecessary()) {
            this.allowGeneration = true;
            lv = ChunkStatus.EMPTY;
        } else {
            lv = ChunkStatus.createOrderedList().get(this.currentlyLoadingStatus.getIndex() + 1);
        }
        this.loadAll(lv, this.allowGeneration);
        this.currentlyLoadingStatus = lv;
    }

    public void markPendingDisposal() {
        this.pendingDisposal = true;
    }

    private void dispose() {
        AbstractChunkHolder lv = this.chunks.get(this.pos.x, this.pos.z);
        lv.clearLoader(this);
        this.chunks.forEach(this.chunkLoadingManager::release);
    }

    private boolean isGenerationUnnecessary() {
        if (this.targetStatus == ChunkStatus.EMPTY) {
            return true;
        }
        ChunkStatus lv = this.chunks.get(this.pos.x, this.pos.z).getActualStatus();
        if (lv == null || lv.isEarlierThan(this.targetStatus)) {
            return false;
        }
        GenerationDependencies lv2 = ChunkGenerationSteps.LOADING.get(this.targetStatus).accumulatedDependencies();
        int i = lv2.getMaxLevel();
        for (int j = this.pos.x - i; j <= this.pos.x + i; ++j) {
            for (int k = this.pos.z - i; k <= this.pos.z + i; ++k) {
                int l = this.pos.getChebyshevDistance(j, k);
                ChunkStatus lv3 = lv2.get(l);
                ChunkStatus lv4 = this.chunks.get(j, k).getActualStatus();
                if (lv4 != null && !lv4.isEarlierThan(lv3)) continue;
                return false;
            }
        }
        return true;
    }

    public AbstractChunkHolder getHolder() {
        return this.chunks.get(this.pos.x, this.pos.z);
    }

    private void loadAll(ChunkStatus targetStatus, boolean allowGeneration) {
        try (ScopedProfiler lv = Profilers.get().scoped("scheduleLayer");){
            lv.addLabel(targetStatus::getId);
            int i = this.getAdditionalLevel(targetStatus, allowGeneration);
            for (int j = this.pos.x - i; j <= this.pos.x + i; ++j) {
                for (int k = this.pos.z - i; k <= this.pos.z + i; ++k) {
                    AbstractChunkHolder lv2 = this.chunks.get(j, k);
                    if (!this.pendingDisposal && this.load(targetStatus, allowGeneration, lv2)) continue;
                    return;
                }
            }
        }
    }

    private int getAdditionalLevel(ChunkStatus status, boolean generate) {
        ChunkGenerationSteps lv = generate ? ChunkGenerationSteps.GENERATION : ChunkGenerationSteps.LOADING;
        return lv.get(this.targetStatus).getAdditionalLevel(status);
    }

    private boolean load(ChunkStatus targetStatus, boolean allowGeneration, AbstractChunkHolder chunkHolder) {
        ChunkGenerationSteps lv2;
        ChunkStatus lv = chunkHolder.getActualStatus();
        boolean bl2 = lv != null && targetStatus.isLaterThan(lv);
        ChunkGenerationSteps chunkGenerationSteps = lv2 = bl2 ? ChunkGenerationSteps.GENERATION : ChunkGenerationSteps.LOADING;
        if (bl2 && !allowGeneration) {
            throw new IllegalStateException("Can't load chunk, but didn't expect to need to generate");
        }
        CompletableFuture<OptionalChunk<Chunk>> completableFuture = chunkHolder.generate(lv2.get(targetStatus), this.chunkLoadingManager, this.chunks);
        OptionalChunk lv3 = completableFuture.getNow(null);
        if (lv3 == null) {
            this.futures.add(completableFuture);
            return true;
        }
        if (lv3.isPresent()) {
            return true;
        }
        this.markPendingDisposal();
        return false;
    }

    private @Nullable CompletableFuture<?> getLatestPendingFuture() {
        while (!this.futures.isEmpty()) {
            CompletableFuture<OptionalChunk<Chunk>> completableFuture = this.futures.getLast();
            OptionalChunk lv = completableFuture.getNow(null);
            if (lv == null) {
                return completableFuture;
            }
            this.futures.removeLast();
            if (lv.isPresent()) continue;
            this.markPendingDisposal();
        }
        return null;
    }
}

