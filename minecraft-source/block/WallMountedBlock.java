/*
 * External method calls:
 *   Lnet/minecraft/block/BlockState;with(Lnet/minecraft/state/property/Property;Ljava/lang/Comparable;)Ljava/lang/Object;
 */
package net.minecraft.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.enums.BlockFace;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.WorldView;
import net.minecraft.world.tick.ScheduledTickView;
import org.jspecify.annotations.Nullable;

public abstract class WallMountedBlock
extends HorizontalFacingBlock {
    public static final EnumProperty<BlockFace> FACE = Properties.BLOCK_FACE;

    protected WallMountedBlock(AbstractBlock.Settings arg) {
        super(arg);
    }

    protected abstract MapCodec<? extends WallMountedBlock> getCodec();

    @Override
    protected boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        return WallMountedBlock.canPlaceAt(world, pos, WallMountedBlock.getDirection(state).getOpposite());
    }

    public static boolean canPlaceAt(WorldView world, BlockPos pos, Direction direction) {
        BlockPos lv = pos.offset(direction);
        return world.getBlockState(lv).isSideSolidFullSquare(world, lv, direction.getOpposite());
    }

    @Override
    public @Nullable BlockState getPlacementState(ItemPlacementContext ctx) {
        for (Direction lv : ctx.getPlacementDirections()) {
            BlockState lv2 = lv.getAxis() == Direction.Axis.Y ? (BlockState)((BlockState)this.getDefaultState().with(FACE, lv == Direction.UP ? BlockFace.CEILING : BlockFace.FLOOR)).with(FACING, ctx.getHorizontalPlayerFacing()) : (BlockState)((BlockState)this.getDefaultState().with(FACE, BlockFace.WALL)).with(FACING, lv.getOpposite());
            if (!lv2.canPlaceAt(ctx.getWorld(), ctx.getBlockPos())) continue;
            return lv2;
        }
        return null;
    }

    @Override
    protected BlockState getStateForNeighborUpdate(BlockState state, WorldView world, ScheduledTickView tickView, BlockPos pos, Direction direction, BlockPos neighborPos, BlockState neighborState, Random random) {
        if (WallMountedBlock.getDirection(state).getOpposite() == direction && !state.canPlaceAt(world, pos)) {
            return Blocks.AIR.getDefaultState();
        }
        return super.getStateForNeighborUpdate(state, world, tickView, pos, direction, neighborPos, neighborState, random);
    }

    protected static Direction getDirection(BlockState state) {
        switch (state.get(FACE)) {
            case CEILING: {
                return Direction.DOWN;
            }
            case FLOOR: {
                return Direction.UP;
            }
        }
        return (Direction)state.get(FACING);
    }
}

