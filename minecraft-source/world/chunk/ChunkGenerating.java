/*
 * External method calls:
 *   Lnet/minecraft/world/chunk/ChunkGenerationContext;world()Lnet/minecraft/server/world/ServerWorld;
 *   Lnet/minecraft/world/chunk/ChunkGenerationContext;generator()Lnet/minecraft/world/gen/chunk/ChunkGenerator;
 *   Lnet/minecraft/world/chunk/ChunkGenerationContext;structureManager()Lnet/minecraft/structure/StructureTemplateManager;
 *   Lnet/minecraft/server/world/ServerWorld;cacheStructures(Lnet/minecraft/world/chunk/Chunk;)V
 *   Lnet/minecraft/world/gen/StructureAccessor;forRegion(Lnet/minecraft/world/ChunkRegion;)Lnet/minecraft/world/gen/StructureAccessor;
 *   Lnet/minecraft/world/gen/chunk/ChunkGenerator;addStructureReferences(Lnet/minecraft/world/StructureWorldAccess;Lnet/minecraft/world/gen/StructureAccessor;Lnet/minecraft/world/chunk/Chunk;)V
 *   Lnet/minecraft/world/gen/chunk/ChunkGenerator;populateBiomes(Lnet/minecraft/world/gen/noise/NoiseConfig;Lnet/minecraft/world/gen/chunk/Blender;Lnet/minecraft/world/gen/StructureAccessor;Lnet/minecraft/world/chunk/Chunk;)Ljava/util/concurrent/CompletableFuture;
 *   Lnet/minecraft/world/gen/chunk/ChunkGenerator;populateNoise(Lnet/minecraft/world/gen/chunk/Blender;Lnet/minecraft/world/gen/noise/NoiseConfig;Lnet/minecraft/world/gen/StructureAccessor;Lnet/minecraft/world/chunk/Chunk;)Ljava/util/concurrent/CompletableFuture;
 *   Lnet/minecraft/world/gen/chunk/ChunkGenerator;buildSurface(Lnet/minecraft/world/ChunkRegion;Lnet/minecraft/world/gen/StructureAccessor;Lnet/minecraft/world/gen/noise/NoiseConfig;Lnet/minecraft/world/chunk/Chunk;)V
 *   Lnet/minecraft/world/gen/chunk/Blender;createCarvingMasks(Lnet/minecraft/world/StructureWorldAccess;Lnet/minecraft/world/chunk/ProtoChunk;)V
 *   Lnet/minecraft/world/gen/chunk/ChunkGenerator;carve(Lnet/minecraft/world/ChunkRegion;JLnet/minecraft/world/gen/noise/NoiseConfig;Lnet/minecraft/world/biome/source/BiomeAccess;Lnet/minecraft/world/gen/StructureAccessor;Lnet/minecraft/world/chunk/Chunk;)V
 *   Lnet/minecraft/world/Heightmap;populateHeightmaps(Lnet/minecraft/world/chunk/Chunk;Ljava/util/Set;)V
 *   Lnet/minecraft/world/gen/chunk/ChunkGenerator;generateFeatures(Lnet/minecraft/world/StructureWorldAccess;Lnet/minecraft/world/chunk/Chunk;Lnet/minecraft/world/gen/StructureAccessor;)V
 *   Lnet/minecraft/world/gen/chunk/Blender;tickLeavesAndFluids(Lnet/minecraft/world/ChunkRegion;Lnet/minecraft/world/chunk/Chunk;)V
 *   Lnet/minecraft/world/chunk/ChunkGenerationContext;lightingProvider()Lnet/minecraft/server/world/ServerLightingProvider;
 *   Lnet/minecraft/server/world/ServerLightingProvider;initializeLight(Lnet/minecraft/world/chunk/Chunk;Z)Ljava/util/concurrent/CompletableFuture;
 *   Lnet/minecraft/server/world/ServerLightingProvider;light(Lnet/minecraft/world/chunk/Chunk;Z)Ljava/util/concurrent/CompletableFuture;
 *   Lnet/minecraft/world/gen/chunk/ChunkGenerator;populateEntities(Lnet/minecraft/world/ChunkRegion;)V
 *   Lnet/minecraft/world/chunk/ChunkGenerationContext;mainThreadExecutor()Ljava/util/concurrent/Executor;
 *   Lnet/minecraft/entity/EntityType;streamFromData(Lnet/minecraft/storage/ReadView$ListReadView;Lnet/minecraft/world/World;Lnet/minecraft/entity/SpawnReason;)Ljava/util/stream/Stream;
 *   Lnet/minecraft/server/world/ServerWorld;addEntities(Ljava/util/stream/Stream;)V
 *   Lnet/minecraft/world/chunk/AbstractChunkHolder;replaceWith(Lnet/minecraft/world/chunk/WrapperProtoChunk;)V
 *   Lnet/minecraft/world/chunk/WorldChunk;addChunkTickSchedulers(Lnet/minecraft/server/world/ServerWorld;)V
 *   Lnet/minecraft/world/chunk/ChunkGenerationContext;unsavedListener()Lnet/minecraft/world/chunk/WorldChunk$UnsavedListener;
 *   Lnet/minecraft/storage/NbtReadView;createList(Lnet/minecraft/util/ErrorReporter;Lnet/minecraft/registry/RegistryWrapper$WrapperLookup;Ljava/util/List;)Lnet/minecraft/storage/ReadView$ListReadView;
 *   Lnet/minecraft/world/chunk/BelowZeroRetrogen;replaceOldBedrock(Lnet/minecraft/world/chunk/ProtoChunk;)V
 *   Lnet/minecraft/world/chunk/BelowZeroRetrogen;fillColumnsWithAirIfMissingBedrock(Lnet/minecraft/world/chunk/ProtoChunk;)V
 *
 * Internal private/static methods:
 *   Lnet/minecraft/world/chunk/ChunkGenerating;addEntities(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/storage/ReadView$ListReadView;)V
 */
package net.minecraft.world.chunk;

import com.mojang.logging.LogUtils;
import java.util.EnumSet;
import java.util.concurrent.CompletableFuture;
import net.minecraft.SharedConstants;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.server.world.ServerLightingProvider;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.storage.NbtReadView;
import net.minecraft.storage.ReadView;
import net.minecraft.util.ErrorReporter;
import net.minecraft.util.collection.BoundedRegionArray;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.Heightmap;
import net.minecraft.world.chunk.AbstractChunkHolder;
import net.minecraft.world.chunk.BelowZeroRetrogen;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkGenerationContext;
import net.minecraft.world.chunk.ChunkGenerationStep;
import net.minecraft.world.chunk.ChunkStatus;
import net.minecraft.world.chunk.ProtoChunk;
import net.minecraft.world.chunk.WorldChunk;
import net.minecraft.world.chunk.WrapperProtoChunk;
import net.minecraft.world.gen.chunk.Blender;
import org.slf4j.Logger;

public class ChunkGenerating {
    private static final Logger LOGGER = LogUtils.getLogger();

    private static boolean isLightOn(Chunk chunk) {
        return chunk.getStatus().isAtLeast(ChunkStatus.LIGHT) && chunk.isLightOn();
    }

    static CompletableFuture<Chunk> noop(ChunkGenerationContext context, ChunkGenerationStep step, BoundedRegionArray<AbstractChunkHolder> chunks, Chunk chunk) {
        return CompletableFuture.completedFuture(chunk);
    }

    static CompletableFuture<Chunk> generateStructures(ChunkGenerationContext context, ChunkGenerationStep step, BoundedRegionArray<AbstractChunkHolder> chunks, Chunk chunk) {
        ServerWorld lv = context.world();
        if (lv.getServer().getSaveProperties().getGeneratorOptions().shouldGenerateStructures()) {
            context.generator().setStructureStarts(lv.getRegistryManager(), lv.getChunkManager().getStructurePlacementCalculator(), lv.getStructureAccessor(), chunk, context.structureManager(), lv.getRegistryKey());
        }
        lv.cacheStructures(chunk);
        return CompletableFuture.completedFuture(chunk);
    }

    static CompletableFuture<Chunk> loadStructures(ChunkGenerationContext context, ChunkGenerationStep step, BoundedRegionArray<AbstractChunkHolder> chunks, Chunk chunk) {
        context.world().cacheStructures(chunk);
        return CompletableFuture.completedFuture(chunk);
    }

    static CompletableFuture<Chunk> generateStructureReferences(ChunkGenerationContext context, ChunkGenerationStep step, BoundedRegionArray<AbstractChunkHolder> chunks, Chunk chunk) {
        ServerWorld lv = context.world();
        ChunkRegion lv2 = new ChunkRegion(lv, chunks, step, chunk);
        context.generator().addStructureReferences(lv2, lv.getStructureAccessor().forRegion(lv2), chunk);
        return CompletableFuture.completedFuture(chunk);
    }

    static CompletableFuture<Chunk> populateBiomes(ChunkGenerationContext context, ChunkGenerationStep step, BoundedRegionArray<AbstractChunkHolder> chunks, Chunk chunk) {
        ServerWorld lv = context.world();
        ChunkRegion lv2 = new ChunkRegion(lv, chunks, step, chunk);
        return context.generator().populateBiomes(lv.getChunkManager().getNoiseConfig(), Blender.getBlender(lv2), lv.getStructureAccessor().forRegion(lv2), chunk);
    }

    static CompletableFuture<Chunk> populateNoise(ChunkGenerationContext context, ChunkGenerationStep step, BoundedRegionArray<AbstractChunkHolder> chunks, Chunk chunk) {
        ServerWorld lv = context.world();
        ChunkRegion lv2 = new ChunkRegion(lv, chunks, step, chunk);
        return context.generator().populateNoise(Blender.getBlender(lv2), lv.getChunkManager().getNoiseConfig(), lv.getStructureAccessor().forRegion(lv2), chunk).thenApply(populated -> {
            ProtoChunk lv;
            BelowZeroRetrogen lv2;
            if (populated instanceof ProtoChunk && (lv2 = (lv = (ProtoChunk)populated).getBelowZeroRetrogen()) != null) {
                BelowZeroRetrogen.replaceOldBedrock(lv);
                if (lv2.hasMissingBedrock()) {
                    lv2.fillColumnsWithAirIfMissingBedrock(lv);
                }
            }
            return populated;
        });
    }

    static CompletableFuture<Chunk> buildSurface(ChunkGenerationContext context, ChunkGenerationStep step, BoundedRegionArray<AbstractChunkHolder> chunks, Chunk chunk) {
        ServerWorld lv = context.world();
        ChunkRegion lv2 = new ChunkRegion(lv, chunks, step, chunk);
        context.generator().buildSurface(lv2, lv.getStructureAccessor().forRegion(lv2), lv.getChunkManager().getNoiseConfig(), chunk);
        return CompletableFuture.completedFuture(chunk);
    }

    static CompletableFuture<Chunk> carve(ChunkGenerationContext context, ChunkGenerationStep step, BoundedRegionArray<AbstractChunkHolder> chunks, Chunk chunk) {
        ServerWorld lv = context.world();
        ChunkRegion lv2 = new ChunkRegion(lv, chunks, step, chunk);
        if (chunk instanceof ProtoChunk) {
            ProtoChunk lv3 = (ProtoChunk)chunk;
            Blender.createCarvingMasks(lv2, lv3);
        }
        context.generator().carve(lv2, lv.getSeed(), lv.getChunkManager().getNoiseConfig(), lv.getBiomeAccess(), lv.getStructureAccessor().forRegion(lv2), chunk);
        return CompletableFuture.completedFuture(chunk);
    }

    static CompletableFuture<Chunk> generateFeatures(ChunkGenerationContext context, ChunkGenerationStep step, BoundedRegionArray<AbstractChunkHolder> chunks, Chunk chunk) {
        ServerWorld lv = context.world();
        Heightmap.populateHeightmaps(chunk, EnumSet.of(Heightmap.Type.MOTION_BLOCKING, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, Heightmap.Type.OCEAN_FLOOR, Heightmap.Type.WORLD_SURFACE));
        ChunkRegion lv2 = new ChunkRegion(lv, chunks, step, chunk);
        if (!SharedConstants.DISABLE_FEATURES) {
            context.generator().generateFeatures(lv2, chunk, lv.getStructureAccessor().forRegion(lv2));
        }
        Blender.tickLeavesAndFluids(lv2, chunk);
        return CompletableFuture.completedFuture(chunk);
    }

    static CompletableFuture<Chunk> initializeLight(ChunkGenerationContext context, ChunkGenerationStep step, BoundedRegionArray<AbstractChunkHolder> chunks, Chunk chunk) {
        ServerLightingProvider lv = context.lightingProvider();
        chunk.refreshSurfaceY();
        ((ProtoChunk)chunk).setLightingProvider(lv);
        boolean bl = ChunkGenerating.isLightOn(chunk);
        return lv.initializeLight(chunk, bl);
    }

    static CompletableFuture<Chunk> light(ChunkGenerationContext context, ChunkGenerationStep step, BoundedRegionArray<AbstractChunkHolder> chunks, Chunk chunk) {
        boolean bl = ChunkGenerating.isLightOn(chunk);
        return context.lightingProvider().light(chunk, bl);
    }

    static CompletableFuture<Chunk> generateEntities(ChunkGenerationContext context, ChunkGenerationStep step, BoundedRegionArray<AbstractChunkHolder> chunks, Chunk chunk) {
        if (!chunk.hasBelowZeroRetrogen()) {
            context.generator().populateEntities(new ChunkRegion(context.world(), chunks, step, chunk));
        }
        return CompletableFuture.completedFuture(chunk);
    }

    static CompletableFuture<Chunk> convertToFullChunk(ChunkGenerationContext context, ChunkGenerationStep step, BoundedRegionArray<AbstractChunkHolder> chunks, Chunk chunk) {
        ChunkPos lv = chunk.getPos();
        AbstractChunkHolder lv2 = chunks.get(lv.x, lv.z);
        return CompletableFuture.supplyAsync(() -> {
            WorldChunk lv4;
            ProtoChunk lv = (ProtoChunk)chunk;
            ServerWorld lv2 = context.world();
            if (lv instanceof WrapperProtoChunk) {
                WrapperProtoChunk lv3 = (WrapperProtoChunk)lv;
                lv4 = lv3.getWrappedChunk();
            } else {
                lv4 = new WorldChunk(lv2, lv, worldChunk -> {
                    try (ErrorReporter.Logging lv = new ErrorReporter.Logging(chunk.getErrorReporterContext(), LOGGER);){
                        ChunkGenerating.addEntities(lv2, NbtReadView.createList(lv, lv2.getRegistryManager(), lv.getEntities()));
                    }
                });
                lv2.replaceWith(new WrapperProtoChunk(lv4, false));
            }
            lv4.setLevelTypeProvider(lv2::getLevelType);
            lv4.loadEntities();
            lv4.setLoadedToWorld(true);
            lv4.updateAllBlockEntities();
            lv4.addChunkTickSchedulers(lv2);
            lv4.setUnsavedListener(context.unsavedListener());
            return lv4;
        }, context.mainThreadExecutor());
    }

    private static void addEntities(ServerWorld world, ReadView.ListReadView entities) {
        if (!entities.isEmpty()) {
            world.addEntities(EntityType.streamFromData(entities, world, SpawnReason.LOAD));
        }
    }
}

