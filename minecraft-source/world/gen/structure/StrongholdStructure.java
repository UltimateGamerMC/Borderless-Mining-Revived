/*
 * External method calls:
 *   Lnet/minecraft/world/gen/structure/Structure$Context;chunkPos()Lnet/minecraft/util/math/ChunkPos;
 *   Lnet/minecraft/world/gen/structure/Structure$Context;random()Lnet/minecraft/util/math/random/ChunkRandom;
 *   Lnet/minecraft/structure/StructurePiecesCollector;addPiece(Lnet/minecraft/structure/StructurePiece;)V
 *   Lnet/minecraft/structure/StrongholdGenerator$Start;fillOpenings(Lnet/minecraft/structure/StructurePiece;Lnet/minecraft/structure/StructurePiecesHolder;Lnet/minecraft/util/math/random/Random;)V
 *   Lnet/minecraft/structure/StructurePiece;fillOpenings(Lnet/minecraft/structure/StructurePiece;Lnet/minecraft/structure/StructurePiecesHolder;Lnet/minecraft/util/math/random/Random;)V
 *   Lnet/minecraft/world/gen/structure/Structure$Context;chunkGenerator()Lnet/minecraft/world/gen/chunk/ChunkGenerator;
 *   Lnet/minecraft/structure/StructurePiecesCollector;shiftInto(IILnet/minecraft/util/math/random/Random;I)I
 *
 * Internal private/static methods:
 *   Lnet/minecraft/world/gen/structure/StrongholdStructure;addPieces(Lnet/minecraft/structure/StructurePiecesCollector;Lnet/minecraft/world/gen/structure/Structure$Context;)V
 *   Lnet/minecraft/world/gen/structure/StrongholdStructure;createCodec(Ljava/util/function/Function;)Lcom/mojang/serialization/MapCodec;
 */
package net.minecraft.world.gen.structure;

import com.mojang.serialization.MapCodec;
import java.util.List;
import java.util.Optional;
import net.minecraft.structure.StrongholdGenerator;
import net.minecraft.structure.StructurePiece;
import net.minecraft.structure.StructurePiecesCollector;
import net.minecraft.world.gen.structure.Structure;
import net.minecraft.world.gen.structure.StructureType;

public class StrongholdStructure
extends Structure {
    public static final MapCodec<StrongholdStructure> CODEC = StrongholdStructure.createCodec(StrongholdStructure::new);

    public StrongholdStructure(Structure.Config arg) {
        super(arg);
    }

    @Override
    public Optional<Structure.StructurePosition> getStructurePosition(Structure.Context context) {
        return Optional.of(new Structure.StructurePosition(context.chunkPos().getStartPos(), collector -> StrongholdStructure.addPieces(collector, context)));
    }

    private static void addPieces(StructurePiecesCollector collector, Structure.Context context) {
        StrongholdGenerator.Start lv;
        int i = 0;
        do {
            collector.clear();
            context.random().setCarverSeed(context.seed() + (long)i++, context.chunkPos().x, context.chunkPos().z);
            StrongholdGenerator.init();
            lv = new StrongholdGenerator.Start(context.random(), context.chunkPos().getOffsetX(2), context.chunkPos().getOffsetZ(2));
            collector.addPiece(lv);
            lv.fillOpenings(lv, collector, context.random());
            List<StructurePiece> list = lv.pieces;
            while (!list.isEmpty()) {
                int j = context.random().nextInt(list.size());
                StructurePiece lv2 = list.remove(j);
                lv2.fillOpenings(lv, collector, context.random());
            }
            collector.shiftInto(context.chunkGenerator().getSeaLevel(), context.chunkGenerator().getMinimumY(), context.random(), 10);
        } while (collector.isEmpty() || lv.portalRoom == null);
    }

    @Override
    public StructureType<?> getType() {
        return StructureType.STRONGHOLD;
    }
}

