/*
 * External method calls:
 *   Lnet/minecraft/structure/StructureTemplate$StructureBlockInfo;pos()Lnet/minecraft/util/math/BlockPos;
 *   Lnet/minecraft/structure/StructureTemplate$StructureBlockInfo;state()Lnet/minecraft/block/BlockState;
 *   Lnet/minecraft/structure/StructureTemplate$StructureBlockInfo;nbt()Lnet/minecraft/nbt/NbtCompound;
 */
package net.minecraft.structure.processor;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.structure.StructurePlacementData;
import net.minecraft.structure.StructureTemplate;
import net.minecraft.structure.processor.StructureProcessor;
import net.minecraft.structure.processor.StructureProcessorType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldView;
import org.jspecify.annotations.Nullable;

public class LavaSubmergedBlockStructureProcessor
extends StructureProcessor {
    public static final MapCodec<LavaSubmergedBlockStructureProcessor> CODEC = MapCodec.unit(() -> INSTANCE);
    public static final LavaSubmergedBlockStructureProcessor INSTANCE = new LavaSubmergedBlockStructureProcessor();

    @Override
    public @Nullable StructureTemplate.StructureBlockInfo process(WorldView world, BlockPos pos, BlockPos pivot, StructureTemplate.StructureBlockInfo originalBlockInfo, StructureTemplate.StructureBlockInfo currentBlockInfo, StructurePlacementData data) {
        BlockPos lv = currentBlockInfo.pos();
        boolean bl = world.getBlockState(lv).isOf(Blocks.LAVA);
        if (bl && !Block.isShapeFullCube(currentBlockInfo.state().getOutlineShape(world, lv))) {
            return new StructureTemplate.StructureBlockInfo(lv, Blocks.LAVA.getDefaultState(), currentBlockInfo.nbt());
        }
        return currentBlockInfo;
    }

    @Override
    protected StructureProcessorType<?> getType() {
        return StructureProcessorType.LAVA_SUBMERGED_BLOCK;
    }
}

