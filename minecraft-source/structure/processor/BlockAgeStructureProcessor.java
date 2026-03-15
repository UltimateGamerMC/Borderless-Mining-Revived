/*
 * External method calls:
 *   Lnet/minecraft/structure/StructureTemplate$StructureBlockInfo;pos()Lnet/minecraft/util/math/BlockPos;
 *   Lnet/minecraft/structure/StructureTemplate$StructureBlockInfo;state()Lnet/minecraft/block/BlockState;
 *   Lnet/minecraft/structure/StructureTemplate$StructureBlockInfo;nbt()Lnet/minecraft/nbt/NbtCompound;
 *   Lnet/minecraft/block/BlockState;with(Lnet/minecraft/state/property/Property;Ljava/lang/Comparable;)Ljava/lang/Object;
 *   Lnet/minecraft/block/enums/BlockHalf;values()[Lnet/minecraft/block/enums/BlockHalf;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/structure/processor/BlockAgeStructureProcessor;processBlocks(Lnet/minecraft/util/math/random/Random;)Lnet/minecraft/block/BlockState;
 *   Lnet/minecraft/structure/processor/BlockAgeStructureProcessor;processStairs(Lnet/minecraft/block/BlockState;Lnet/minecraft/util/math/random/Random;)Lnet/minecraft/block/BlockState;
 *   Lnet/minecraft/structure/processor/BlockAgeStructureProcessor;processSlabs(Lnet/minecraft/block/BlockState;Lnet/minecraft/util/math/random/Random;)Lnet/minecraft/block/BlockState;
 *   Lnet/minecraft/structure/processor/BlockAgeStructureProcessor;processWalls(Lnet/minecraft/block/BlockState;Lnet/minecraft/util/math/random/Random;)Lnet/minecraft/block/BlockState;
 *   Lnet/minecraft/structure/processor/BlockAgeStructureProcessor;processObsidian(Lnet/minecraft/util/math/random/Random;)Lnet/minecraft/block/BlockState;
 *   Lnet/minecraft/structure/processor/BlockAgeStructureProcessor;randomStairProperties(Lnet/minecraft/util/math/random/Random;Lnet/minecraft/block/Block;)Lnet/minecraft/block/BlockState;
 *   Lnet/minecraft/structure/processor/BlockAgeStructureProcessor;process(Lnet/minecraft/util/math/random/Random;[Lnet/minecraft/block/BlockState;[Lnet/minecraft/block/BlockState;)Lnet/minecraft/block/BlockState;
 *   Lnet/minecraft/structure/processor/BlockAgeStructureProcessor;randomState(Lnet/minecraft/util/math/random/Random;[Lnet/minecraft/block/BlockState;)Lnet/minecraft/block/BlockState;
 */
package net.minecraft.structure.processor;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.StairsBlock;
import net.minecraft.block.enums.BlockHalf;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.structure.StructurePlacementData;
import net.minecraft.structure.StructureTemplate;
import net.minecraft.structure.processor.StructureProcessor;
import net.minecraft.structure.processor.StructureProcessorType;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.WorldView;
import org.jspecify.annotations.Nullable;

public class BlockAgeStructureProcessor
extends StructureProcessor {
    public static final MapCodec<BlockAgeStructureProcessor> CODEC = ((MapCodec)Codec.FLOAT.fieldOf("mossiness")).xmap(BlockAgeStructureProcessor::new, processor -> Float.valueOf(processor.mossiness));
    private static final float field_31681 = 0.5f;
    private static final float field_31682 = 0.5f;
    private static final float field_31683 = 0.15f;
    private static final BlockState[] AGEABLE_SLABS = new BlockState[]{Blocks.STONE_SLAB.getDefaultState(), Blocks.STONE_BRICK_SLAB.getDefaultState()};
    private final float mossiness;

    public BlockAgeStructureProcessor(float mossiness) {
        this.mossiness = mossiness;
    }

    @Override
    public @Nullable StructureTemplate.StructureBlockInfo process(WorldView world, BlockPos pos, BlockPos pivot, StructureTemplate.StructureBlockInfo originalBlockInfo, StructureTemplate.StructureBlockInfo currentBlockInfo, StructurePlacementData data) {
        Random lv = data.getRandom(currentBlockInfo.pos());
        BlockState lv2 = currentBlockInfo.state();
        BlockPos lv3 = currentBlockInfo.pos();
        BlockState lv4 = null;
        if (lv2.isOf(Blocks.STONE_BRICKS) || lv2.isOf(Blocks.STONE) || lv2.isOf(Blocks.CHISELED_STONE_BRICKS)) {
            lv4 = this.processBlocks(lv);
        } else if (lv2.isIn(BlockTags.STAIRS)) {
            lv4 = this.processStairs(lv2, lv);
        } else if (lv2.isIn(BlockTags.SLABS)) {
            lv4 = this.processSlabs(lv2, lv);
        } else if (lv2.isIn(BlockTags.WALLS)) {
            lv4 = this.processWalls(lv2, lv);
        } else if (lv2.isOf(Blocks.OBSIDIAN)) {
            lv4 = this.processObsidian(lv);
        }
        if (lv4 != null) {
            return new StructureTemplate.StructureBlockInfo(lv3, lv4, currentBlockInfo.nbt());
        }
        return currentBlockInfo;
    }

    private @Nullable BlockState processBlocks(Random random) {
        if (random.nextFloat() >= 0.5f) {
            return null;
        }
        BlockState[] lvs = new BlockState[]{Blocks.CRACKED_STONE_BRICKS.getDefaultState(), BlockAgeStructureProcessor.randomStairProperties(random, Blocks.STONE_BRICK_STAIRS)};
        BlockState[] lvs2 = new BlockState[]{Blocks.MOSSY_STONE_BRICKS.getDefaultState(), BlockAgeStructureProcessor.randomStairProperties(random, Blocks.MOSSY_STONE_BRICK_STAIRS)};
        return this.process(random, lvs, lvs2);
    }

    private @Nullable BlockState processStairs(BlockState arg, Random arg2) {
        if (arg2.nextFloat() >= 0.5f) {
            return null;
        }
        BlockState[] lvs = new BlockState[]{Blocks.MOSSY_STONE_BRICK_STAIRS.getStateWithProperties(arg), Blocks.MOSSY_STONE_BRICK_SLAB.getDefaultState()};
        return this.process(arg2, AGEABLE_SLABS, lvs);
    }

    private @Nullable BlockState processSlabs(BlockState arg, Random arg2) {
        if (arg2.nextFloat() < this.mossiness) {
            return Blocks.MOSSY_STONE_BRICK_SLAB.getStateWithProperties(arg);
        }
        return null;
    }

    private @Nullable BlockState processWalls(BlockState arg, Random arg2) {
        if (arg2.nextFloat() < this.mossiness) {
            return Blocks.MOSSY_STONE_BRICK_WALL.getStateWithProperties(arg);
        }
        return null;
    }

    private @Nullable BlockState processObsidian(Random random) {
        if (random.nextFloat() < 0.15f) {
            return Blocks.CRYING_OBSIDIAN.getDefaultState();
        }
        return null;
    }

    private static BlockState randomStairProperties(Random random, Block stairs) {
        return (BlockState)((BlockState)stairs.getDefaultState().with(StairsBlock.FACING, Direction.Type.HORIZONTAL.random(random))).with(StairsBlock.HALF, Util.getRandom(BlockHalf.values(), random));
    }

    private BlockState process(Random random, BlockState[] regularStates, BlockState[] mossyStates) {
        if (random.nextFloat() < this.mossiness) {
            return BlockAgeStructureProcessor.randomState(random, mossyStates);
        }
        return BlockAgeStructureProcessor.randomState(random, regularStates);
    }

    private static BlockState randomState(Random random, BlockState[] states) {
        return states[random.nextInt(states.length)];
    }

    @Override
    protected StructureProcessorType<?> getType() {
        return StructureProcessorType.BLOCK_AGE;
    }
}

