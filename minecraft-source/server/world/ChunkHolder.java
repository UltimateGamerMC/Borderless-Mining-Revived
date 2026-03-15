/*
 * External method calls:
 *   Lnet/minecraft/server/world/OptionalChunk;orElse(Ljava/lang/Object;)Ljava/lang/Object;
 *   Lnet/minecraft/world/HeightLimitView;sectionIndexToCoord(I)I
 *   Lnet/minecraft/network/packet/s2c/play/ChunkDeltaUpdateS2CPacket;visitUpdates(Ljava/util/function/BiConsumer;)V
 *   Lnet/minecraft/block/entity/BlockEntity;toUpdatePacket()Lnet/minecraft/network/packet/Packet;
 *   Lnet/minecraft/server/world/ServerChunkLoadingManager;onChunkStatusChange(Lnet/minecraft/util/math/ChunkPos;Lnet/minecraft/server/world/ChunkLevelType;)V
 *   Lnet/minecraft/server/world/ServerChunkLoadingManager;makeChunkAccessible(Lnet/minecraft/server/world/ChunkHolder;)Ljava/util/concurrent/CompletableFuture;
 *   Lnet/minecraft/server/world/ServerChunkLoadingManager;makeChunkTickable(Lnet/minecraft/server/world/ChunkHolder;)Ljava/util/concurrent/CompletableFuture;
 *   Lnet/minecraft/server/world/ServerChunkLoadingManager;makeChunkEntitiesTickable(Lnet/minecraft/server/world/ChunkHolder;)Ljava/util/concurrent/CompletableFuture;
 *   Lnet/minecraft/server/world/ChunkHolder$LevelUpdateListener;updateLevel(Lnet/minecraft/util/math/ChunkPos;Ljava/util/function/IntSupplier;ILjava/util/function/IntConsumer;)V
 *   Lnet/minecraft/server/world/OptionalChunk;ifPresent(Ljava/util/function/Consumer;)Lnet/minecraft/server/world/OptionalChunk;
 *   Lnet/minecraft/server/network/ServerPlayNetworkHandler;sendPacket(Lnet/minecraft/network/packet/Packet;)V
 *   Lnet/minecraft/server/world/OptionalChunk;of(Ljava/lang/String;)Lnet/minecraft/server/world/OptionalChunk;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/server/world/ChunkHolder;sendPacketToPlayers(Ljava/util/List;Lnet/minecraft/network/packet/Packet;)V
 *   Lnet/minecraft/server/world/ChunkHolder;tryUpdateBlockEntityAt(Ljava/util/List;Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;)V
 *   Lnet/minecraft/server/world/ChunkHolder;sendBlockEntityUpdatePacket(Ljava/util/List;Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;)V
 *   Lnet/minecraft/server/world/ChunkHolder;increaseLevel(Lnet/minecraft/server/world/ServerChunkLoadingManager;Ljava/util/concurrent/CompletableFuture;Ljava/util/concurrent/Executor;Lnet/minecraft/server/world/ChunkLevelType;)V
 *   Lnet/minecraft/server/world/ChunkHolder;combineSavingFuture(Ljava/util/concurrent/CompletableFuture;)V
 *   Lnet/minecraft/server/world/ChunkHolder;decreaseLevel(Lnet/minecraft/server/world/ServerChunkLoadingManager;Lnet/minecraft/server/world/ChunkLevelType;)V
 */
package net.minecraft.server.world;

import it.unimi.dsi.fastutil.shorts.ShortOpenHashSet;
import it.unimi.dsi.fastutil.shorts.ShortSet;
import java.util.BitSet;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.IntConsumer;
import java.util.function.IntSupplier;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.ChunkDeltaUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.LightUpdateS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ChunkLevelType;
import net.minecraft.server.world.ChunkLevels;
import net.minecraft.server.world.OptionalChunk;
import net.minecraft.server.world.ServerChunkLoadingManager;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.ChunkSectionPos;
import net.minecraft.world.HeightLimitView;
import net.minecraft.world.LightType;
import net.minecraft.world.World;
import net.minecraft.world.chunk.AbstractChunkHolder;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkSection;
import net.minecraft.world.chunk.ChunkStatus;
import net.minecraft.world.chunk.WorldChunk;
import net.minecraft.world.chunk.light.LightingProvider;
import org.jspecify.annotations.Nullable;

public class ChunkHolder
extends AbstractChunkHolder {
    public static final OptionalChunk<WorldChunk> UNLOADED_WORLD_CHUNK = OptionalChunk.of("Unloaded level chunk");
    private static final CompletableFuture<OptionalChunk<WorldChunk>> UNLOADED_WORLD_CHUNK_FUTURE = CompletableFuture.completedFuture(UNLOADED_WORLD_CHUNK);
    private final HeightLimitView world;
    private volatile CompletableFuture<OptionalChunk<WorldChunk>> accessibleFuture = UNLOADED_WORLD_CHUNK_FUTURE;
    private volatile CompletableFuture<OptionalChunk<WorldChunk>> tickingFuture = UNLOADED_WORLD_CHUNK_FUTURE;
    private volatile CompletableFuture<OptionalChunk<WorldChunk>> entityTickingFuture = UNLOADED_WORLD_CHUNK_FUTURE;
    private int lastTickLevel;
    private int level;
    private int completedLevel;
    private boolean pendingBlockUpdates;
    private final @Nullable ShortSet[] blockUpdatesBySection;
    private final BitSet blockLightUpdateBits = new BitSet();
    private final BitSet skyLightUpdateBits = new BitSet();
    private final LightingProvider lightingProvider;
    private final LevelUpdateListener levelUpdateListener;
    private final PlayersWatchingChunkProvider playersWatchingChunkProvider;
    private boolean accessible;
    private CompletableFuture<?> levelIncreaseFuture = CompletableFuture.completedFuture(null);
    private CompletableFuture<?> postProcessingFuture = CompletableFuture.completedFuture(null);
    private CompletableFuture<?> savingFuture = CompletableFuture.completedFuture(null);

    public ChunkHolder(ChunkPos pos, int level, HeightLimitView world, LightingProvider lightingProvider, LevelUpdateListener levelUpdateListener, PlayersWatchingChunkProvider playersWatchingChunkProvider) {
        super(pos);
        this.world = world;
        this.lightingProvider = lightingProvider;
        this.levelUpdateListener = levelUpdateListener;
        this.playersWatchingChunkProvider = playersWatchingChunkProvider;
        this.level = this.lastTickLevel = ChunkLevels.INACCESSIBLE + 1;
        this.completedLevel = this.lastTickLevel;
        this.setLevel(level);
        this.blockUpdatesBySection = new ShortSet[world.countVerticalSections()];
    }

    public CompletableFuture<OptionalChunk<WorldChunk>> getTickingFuture() {
        return this.tickingFuture;
    }

    public CompletableFuture<OptionalChunk<WorldChunk>> getEntityTickingFuture() {
        return this.entityTickingFuture;
    }

    public CompletableFuture<OptionalChunk<WorldChunk>> getAccessibleFuture() {
        return this.accessibleFuture;
    }

    public @Nullable WorldChunk getWorldChunk() {
        return this.getTickingFuture().getNow(UNLOADED_WORLD_CHUNK).orElse(null);
    }

    public @Nullable WorldChunk getPostProcessedChunk() {
        if (!this.postProcessingFuture.isDone()) {
            return null;
        }
        return this.getWorldChunk();
    }

    public CompletableFuture<?> getPostProcessingFuture() {
        return this.postProcessingFuture;
    }

    public void combinePostProcessingFuture(CompletableFuture<?> postProcessingFuture) {
        this.postProcessingFuture = this.postProcessingFuture.isDone() ? postProcessingFuture : this.postProcessingFuture.thenCombine(postProcessingFuture, (object, object2) -> null);
    }

    public CompletableFuture<?> getSavingFuture() {
        return this.savingFuture;
    }

    public boolean isSavable() {
        return this.savingFuture.isDone();
    }

    @Override
    protected void combineSavingFuture(CompletableFuture<?> savingFuture) {
        this.savingFuture = this.savingFuture.isDone() ? savingFuture : this.savingFuture.thenCombine(savingFuture, (object, thenResult) -> null);
    }

    public boolean markForBlockUpdate(BlockPos pos) {
        WorldChunk lv = this.getWorldChunk();
        if (lv == null) {
            return false;
        }
        boolean bl = this.pendingBlockUpdates;
        int i = this.world.getSectionIndex(pos.getY());
        ShortSet shortSet = this.blockUpdatesBySection[i];
        if (shortSet == null) {
            this.pendingBlockUpdates = true;
            this.blockUpdatesBySection[i] = shortSet = new ShortOpenHashSet();
        }
        shortSet.add(ChunkSectionPos.packLocal(pos));
        return !bl;
    }

    public boolean markForLightUpdate(LightType lightType, int y) {
        int l;
        Chunk lv = this.getOrNull(ChunkStatus.INITIALIZE_LIGHT);
        if (lv == null) {
            return false;
        }
        lv.markNeedsSaving();
        WorldChunk lv2 = this.getWorldChunk();
        if (lv2 == null) {
            return false;
        }
        int j = this.lightingProvider.getBottomY();
        int k = this.lightingProvider.getTopY();
        if (y < j || y > k) {
            return false;
        }
        BitSet bitSet = lightType == LightType.SKY ? this.skyLightUpdateBits : this.blockLightUpdateBits;
        if (!bitSet.get(l = y - j)) {
            bitSet.set(l);
            return true;
        }
        return false;
    }

    public boolean hasPendingUpdates() {
        return this.pendingBlockUpdates || !this.skyLightUpdateBits.isEmpty() || !this.blockLightUpdateBits.isEmpty();
    }

    public void flushUpdates(WorldChunk chunk) {
        List<ServerPlayerEntity> list;
        if (!this.hasPendingUpdates()) {
            return;
        }
        World lv = chunk.getWorld();
        if (!this.skyLightUpdateBits.isEmpty() || !this.blockLightUpdateBits.isEmpty()) {
            list = this.playersWatchingChunkProvider.getPlayersWatchingChunk(this.pos, true);
            if (!list.isEmpty()) {
                LightUpdateS2CPacket lv2 = new LightUpdateS2CPacket(chunk.getPos(), this.lightingProvider, this.skyLightUpdateBits, this.blockLightUpdateBits);
                this.sendPacketToPlayers(list, lv2);
            }
            this.skyLightUpdateBits.clear();
            this.blockLightUpdateBits.clear();
        }
        if (!this.pendingBlockUpdates) {
            return;
        }
        list = this.playersWatchingChunkProvider.getPlayersWatchingChunk(this.pos, false);
        for (int i = 0; i < this.blockUpdatesBySection.length; ++i) {
            ShortSet shortSet = this.blockUpdatesBySection[i];
            if (shortSet == null) continue;
            this.blockUpdatesBySection[i] = null;
            if (list.isEmpty()) continue;
            int j = this.world.sectionIndexToCoord(i);
            ChunkSectionPos lv3 = ChunkSectionPos.from(chunk.getPos(), j);
            if (shortSet.size() == 1) {
                BlockPos lv4 = lv3.unpackBlockPos(shortSet.iterator().nextShort());
                BlockState lv5 = lv.getBlockState(lv4);
                this.sendPacketToPlayers(list, new BlockUpdateS2CPacket(lv4, lv5));
                this.tryUpdateBlockEntityAt(list, lv, lv4, lv5);
                continue;
            }
            ChunkSection lv6 = chunk.getSection(i);
            ChunkDeltaUpdateS2CPacket lv7 = new ChunkDeltaUpdateS2CPacket(lv3, shortSet, lv6);
            this.sendPacketToPlayers(list, lv7);
            lv7.visitUpdates((pos, state) -> this.tryUpdateBlockEntityAt(list, lv, (BlockPos)pos, (BlockState)state));
        }
        this.pendingBlockUpdates = false;
    }

    private void tryUpdateBlockEntityAt(List<ServerPlayerEntity> players, World world, BlockPos pos, BlockState state) {
        if (state.hasBlockEntity()) {
            this.sendBlockEntityUpdatePacket(players, world, pos);
        }
    }

    private void sendBlockEntityUpdatePacket(List<ServerPlayerEntity> players, World world, BlockPos pos) {
        Packet<ClientPlayPacketListener> lv2;
        BlockEntity lv = world.getBlockEntity(pos);
        if (lv != null && (lv2 = lv.toUpdatePacket()) != null) {
            this.sendPacketToPlayers(players, lv2);
        }
    }

    private void sendPacketToPlayers(List<ServerPlayerEntity> players, Packet<?> packet) {
        players.forEach(player -> player.networkHandler.sendPacket(packet));
    }

    @Override
    public int getLevel() {
        return this.level;
    }

    @Override
    public int getCompletedLevel() {
        return this.completedLevel;
    }

    private void setCompletedLevel(int level) {
        this.completedLevel = level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    private void increaseLevel(ServerChunkLoadingManager chunkLoadingManager, CompletableFuture<OptionalChunk<WorldChunk>> chunkFuture, Executor executor, ChunkLevelType target) {
        this.levelIncreaseFuture.cancel(false);
        CompletableFuture completableFuture2 = new CompletableFuture();
        completableFuture2.thenRunAsync(() -> chunkLoadingManager.onChunkStatusChange(this.pos, target), executor);
        this.levelIncreaseFuture = completableFuture2;
        chunkFuture.thenAccept(optionalChunk -> optionalChunk.ifPresent(chunk -> completableFuture2.complete(null)));
    }

    private void decreaseLevel(ServerChunkLoadingManager chunkLoadingManager, ChunkLevelType target) {
        this.levelIncreaseFuture.cancel(false);
        chunkLoadingManager.onChunkStatusChange(this.pos, target);
    }

    protected void updateFutures(ServerChunkLoadingManager chunkLoadingManager, Executor executor) {
        ChunkLevelType lv = ChunkLevels.getType(this.lastTickLevel);
        ChunkLevelType lv2 = ChunkLevels.getType(this.level);
        boolean bl = lv.isAfter(ChunkLevelType.FULL);
        boolean bl2 = lv2.isAfter(ChunkLevelType.FULL);
        this.accessible |= bl2;
        if (!bl && bl2) {
            this.accessibleFuture = chunkLoadingManager.makeChunkAccessible(this);
            this.increaseLevel(chunkLoadingManager, this.accessibleFuture, executor, ChunkLevelType.FULL);
            this.combineSavingFuture(this.accessibleFuture);
        }
        if (bl && !bl2) {
            this.accessibleFuture.complete(UNLOADED_WORLD_CHUNK);
            this.accessibleFuture = UNLOADED_WORLD_CHUNK_FUTURE;
        }
        boolean bl3 = lv.isAfter(ChunkLevelType.BLOCK_TICKING);
        boolean bl4 = lv2.isAfter(ChunkLevelType.BLOCK_TICKING);
        if (!bl3 && bl4) {
            this.tickingFuture = chunkLoadingManager.makeChunkTickable(this);
            this.increaseLevel(chunkLoadingManager, this.tickingFuture, executor, ChunkLevelType.BLOCK_TICKING);
            this.combineSavingFuture(this.tickingFuture);
        }
        if (bl3 && !bl4) {
            this.tickingFuture.complete(UNLOADED_WORLD_CHUNK);
            this.tickingFuture = UNLOADED_WORLD_CHUNK_FUTURE;
        }
        boolean bl5 = lv.isAfter(ChunkLevelType.ENTITY_TICKING);
        boolean bl6 = lv2.isAfter(ChunkLevelType.ENTITY_TICKING);
        if (!bl5 && bl6) {
            if (this.entityTickingFuture != UNLOADED_WORLD_CHUNK_FUTURE) {
                throw Util.getFatalOrPause(new IllegalStateException());
            }
            this.entityTickingFuture = chunkLoadingManager.makeChunkEntitiesTickable(this);
            this.increaseLevel(chunkLoadingManager, this.entityTickingFuture, executor, ChunkLevelType.ENTITY_TICKING);
            this.combineSavingFuture(this.entityTickingFuture);
        }
        if (bl5 && !bl6) {
            this.entityTickingFuture.complete(UNLOADED_WORLD_CHUNK);
            this.entityTickingFuture = UNLOADED_WORLD_CHUNK_FUTURE;
        }
        if (!lv2.isAfter(lv)) {
            this.decreaseLevel(chunkLoadingManager, lv2);
        }
        this.levelUpdateListener.updateLevel(this.pos, this::getCompletedLevel, this.level, this::setCompletedLevel);
        this.lastTickLevel = this.level;
    }

    public boolean isAccessible() {
        return this.accessible;
    }

    public void updateAccessibleStatus() {
        this.accessible = ChunkLevels.getType(this.level).isAfter(ChunkLevelType.FULL);
    }

    @FunctionalInterface
    public static interface LevelUpdateListener {
        public void updateLevel(ChunkPos var1, IntSupplier var2, int var3, IntConsumer var4);
    }

    public static interface PlayersWatchingChunkProvider {
        public List<ServerPlayerEntity> getPlayersWatchingChunk(ChunkPos var1, boolean var2);
    }
}

