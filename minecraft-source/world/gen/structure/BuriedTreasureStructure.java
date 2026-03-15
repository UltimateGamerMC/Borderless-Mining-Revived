/*
 * External method calls:
 *   Lnet/minecraft/world/gen/structure/Structure$Context;chunkPos()Lnet/minecraft/util/math/ChunkPos;
 *   Lnet/minecraft/structure/StructurePiecesCollector;addPiece(Lnet/minecraft/structure/StructurePiece;)V
 *
 * Internal private/static methods:
 *   Lnet/minecraft/world/gen/structure/BuriedTreasureStructure;addPieces(Lnet/minecraft/structure/StructurePiecesCollector;Lnet/minecraft/world/gen/structure/Structure$Context;)V
 *   Lnet/minecraft/world/gen/structure/BuriedTreasureStructure;createCodec(Ljava/util/function/Function;)Lcom/mojang/serialization/MapCodec;
 */
package net.minecraft.world.gen.structure;

import com.mojang.serialization.MapCodec;
import java.util.Optional;
import net.minecraft.structure.BuriedTreasureGenerator;
import net.minecraft.structure.StructurePiecesCollector;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Heightmap;
import net.minecraft.world.gen.structure.Structure;
import net.minecraft.world.gen.structure.StructureType;

public class BuriedTreasureStructure
extends Structure {
    public static final MapCodec<BuriedTreasureStructure> CODEC = BuriedTreasureStructure.createCodec(BuriedTreasureStructure::new);

    public BuriedTreasureStructure(Structure.Config arg) {
        super(arg);
    }

    @Override
    public Optional<Structure.StructurePosition> getStructurePosition(Structure.Context context) {
        return BuriedTreasureStructure.getStructurePosition(context, Heightmap.Type.OCEAN_FLOOR_WG, collector -> BuriedTreasureStructure.addPieces(collector, context));
    }

    private static void addPieces(StructurePiecesCollector collector, Structure.Context context) {
        BlockPos lv = new BlockPos(context.chunkPos().getOffsetX(9), 90, context.chunkPos().getOffsetZ(9));
        collector.addPiece(new BuriedTreasureGenerator.Piece(lv));
    }

    @Override
    public StructureType<?> getType() {
        return StructureType.BURIED_TREASURE;
    }
}

