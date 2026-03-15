/*
 * External method calls:
 *   Lnet/minecraft/world/gen/structure/Structure$Context;chunkPos()Lnet/minecraft/util/math/ChunkPos;
 *   Lnet/minecraft/world/gen/structure/Structure$Context;random()Lnet/minecraft/util/math/random/ChunkRandom;
 *   Lnet/minecraft/structure/StructurePiecesCollector;addPiece(Lnet/minecraft/structure/StructurePiece;)V
 *   Lnet/minecraft/structure/NetherFortressGenerator$Start;fillOpenings(Lnet/minecraft/structure/StructurePiece;Lnet/minecraft/structure/StructurePiecesHolder;Lnet/minecraft/util/math/random/Random;)V
 *   Lnet/minecraft/structure/StructurePiece;fillOpenings(Lnet/minecraft/structure/StructurePiece;Lnet/minecraft/structure/StructurePiecesHolder;Lnet/minecraft/util/math/random/Random;)V
 *   Lnet/minecraft/structure/StructurePiecesCollector;shiftInto(Lnet/minecraft/util/math/random/Random;II)V
 *   Lnet/minecraft/util/collection/Pool;builder()Lnet/minecraft/util/collection/Pool$Builder;
 *   Lnet/minecraft/util/collection/Pool$Builder;build()Lnet/minecraft/util/collection/Pool;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/world/gen/structure/NetherFortressStructure;addPieces(Lnet/minecraft/structure/StructurePiecesCollector;Lnet/minecraft/world/gen/structure/Structure$Context;)V
 *   Lnet/minecraft/world/gen/structure/NetherFortressStructure;createCodec(Ljava/util/function/Function;)Lcom/mojang/serialization/MapCodec;
 */
package net.minecraft.world.gen.structure;

import com.mojang.serialization.MapCodec;
import java.util.List;
import java.util.Optional;
import net.minecraft.entity.EntityType;
import net.minecraft.structure.NetherFortressGenerator;
import net.minecraft.structure.StructurePiece;
import net.minecraft.structure.StructurePiecesCollector;
import net.minecraft.util.collection.Pool;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.biome.SpawnSettings;
import net.minecraft.world.gen.structure.Structure;
import net.minecraft.world.gen.structure.StructureType;

public class NetherFortressStructure
extends Structure {
    public static final Pool<SpawnSettings.SpawnEntry> MONSTER_SPAWNS = Pool.builder().add(new SpawnSettings.SpawnEntry(EntityType.BLAZE, 2, 3), 10).add(new SpawnSettings.SpawnEntry(EntityType.ZOMBIFIED_PIGLIN, 4, 4), 5).add(new SpawnSettings.SpawnEntry(EntityType.WITHER_SKELETON, 5, 5), 8).add(new SpawnSettings.SpawnEntry(EntityType.SKELETON, 5, 5), 2).add(new SpawnSettings.SpawnEntry(EntityType.MAGMA_CUBE, 4, 4), 3).build();
    public static final MapCodec<NetherFortressStructure> CODEC = NetherFortressStructure.createCodec(NetherFortressStructure::new);

    public NetherFortressStructure(Structure.Config arg) {
        super(arg);
    }

    @Override
    public Optional<Structure.StructurePosition> getStructurePosition(Structure.Context context) {
        ChunkPos lv = context.chunkPos();
        BlockPos lv2 = new BlockPos(lv.getStartX(), 64, lv.getStartZ());
        return Optional.of(new Structure.StructurePosition(lv2, collector -> NetherFortressStructure.addPieces(collector, context)));
    }

    private static void addPieces(StructurePiecesCollector collector, Structure.Context context) {
        NetherFortressGenerator.Start lv = new NetherFortressGenerator.Start(context.random(), context.chunkPos().getOffsetX(2), context.chunkPos().getOffsetZ(2));
        collector.addPiece(lv);
        lv.fillOpenings(lv, collector, context.random());
        List<StructurePiece> list = lv.pieces;
        while (!list.isEmpty()) {
            int i = context.random().nextInt(list.size());
            StructurePiece lv2 = list.remove(i);
            lv2.fillOpenings(lv, collector, context.random());
        }
        collector.shiftInto(context.random(), 48, 70);
    }

    @Override
    public StructureType<?> getType() {
        return StructureType.FORTRESS;
    }
}

