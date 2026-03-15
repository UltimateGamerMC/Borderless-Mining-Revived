/*
 * External method calls:
 *   Lnet/minecraft/block/BlockState;with(Lnet/minecraft/state/property/Property;Ljava/lang/Comparable;)Ljava/lang/Object;
 *   Lnet/minecraft/util/BlockRotation;rotate(Lnet/minecraft/util/math/Direction;)Lnet/minecraft/util/math/Direction;
 *   Lnet/minecraft/block/BlockState;rotate(Lnet/minecraft/util/BlockRotation;)Lnet/minecraft/block/BlockState;
 *   Lnet/minecraft/block/pattern/BlockPatternBuilder;start()Lnet/minecraft/block/pattern/BlockPatternBuilder;
 *   Lnet/minecraft/block/pattern/BlockPatternBuilder;aisle([Ljava/lang/String;)Lnet/minecraft/block/pattern/BlockPatternBuilder;
 *   Lnet/minecraft/block/pattern/CachedBlockPosition;matchesBlockState(Ljava/util/function/Predicate;)Ljava/util/function/Predicate;
 *   Lnet/minecraft/block/pattern/BlockPatternBuilder;where(CLjava/util/function/Predicate;)Lnet/minecraft/block/pattern/BlockPatternBuilder;
 *   Lnet/minecraft/predicate/block/BlockStatePredicate;forBlock(Lnet/minecraft/block/Block;)Lnet/minecraft/predicate/block/BlockStatePredicate;
 *   Lnet/minecraft/predicate/block/BlockStatePredicate;with(Lnet/minecraft/state/property/Property;Ljava/util/function/Predicate;)Lnet/minecraft/predicate/block/BlockStatePredicate;
 *   Lnet/minecraft/block/pattern/BlockPatternBuilder;build()Lnet/minecraft/block/pattern/BlockPattern;
 *   Lnet/minecraft/block/Block;createColumnShape(DDD)Lnet/minecraft/util/shape/VoxelShape;
 *   Lnet/minecraft/util/shape/VoxelShapes;union(Lnet/minecraft/util/shape/VoxelShape;Lnet/minecraft/util/shape/VoxelShape;)Lnet/minecraft/util/shape/VoxelShape;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/block/EndPortalFrameBlock;createCodec(Ljava/util/function/Function;)Lcom/mojang/serialization/MapCodec;
 */
package net.minecraft.block;

import com.google.common.base.Predicates;
import com.mojang.serialization.MapCodec;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.pattern.BlockPattern;
import net.minecraft.block.pattern.BlockPatternBuilder;
import net.minecraft.block.pattern.CachedBlockPosition;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.predicate.block.BlockStatePredicate;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jspecify.annotations.Nullable;

public class EndPortalFrameBlock
extends Block {
    public static final MapCodec<EndPortalFrameBlock> CODEC = EndPortalFrameBlock.createCodec(EndPortalFrameBlock::new);
    public static final EnumProperty<Direction> FACING = HorizontalFacingBlock.FACING;
    public static final BooleanProperty EYE = Properties.EYE;
    private static final VoxelShape FRAME_SHAPE = Block.createColumnShape(16.0, 0.0, 13.0);
    private static final VoxelShape FRAME_WITH_EYE_SHAPE = VoxelShapes.union(FRAME_SHAPE, Block.createColumnShape(8.0, 13.0, 16.0));
    private static @Nullable BlockPattern completedFrame;

    public MapCodec<EndPortalFrameBlock> getCodec() {
        return CODEC;
    }

    public EndPortalFrameBlock(AbstractBlock.Settings arg) {
        super(arg);
        this.setDefaultState((BlockState)((BlockState)((BlockState)this.stateManager.getDefaultState()).with(FACING, Direction.NORTH)).with(EYE, false));
    }

    @Override
    protected boolean hasSidedTransparency(BlockState state) {
        return true;
    }

    @Override
    protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return state.get(EYE) != false ? FRAME_WITH_EYE_SHAPE : FRAME_SHAPE;
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return (BlockState)((BlockState)this.getDefaultState().with(FACING, ctx.getHorizontalPlayerFacing().getOpposite())).with(EYE, false);
    }

    @Override
    protected boolean hasComparatorOutput(BlockState state) {
        return true;
    }

    @Override
    protected int getComparatorOutput(BlockState state, World world, BlockPos pos, Direction direction) {
        if (state.get(EYE).booleanValue()) {
            return 15;
        }
        return 0;
    }

    @Override
    protected BlockState rotate(BlockState state, BlockRotation rotation) {
        return (BlockState)state.with(FACING, rotation.rotate(state.get(FACING)));
    }

    @Override
    protected BlockState mirror(BlockState state, BlockMirror mirror) {
        return state.rotate(mirror.getRotation(state.get(FACING)));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING, EYE);
    }

    public static BlockPattern getCompletedFramePattern() {
        if (completedFrame == null) {
            completedFrame = BlockPatternBuilder.start().aisle("?vvv?", ">???<", ">???<", ">???<", "?^^^?").where('?', CachedBlockPosition.matchesBlockState(BlockStatePredicate.ANY)).where('^', CachedBlockPosition.matchesBlockState(BlockStatePredicate.forBlock(Blocks.END_PORTAL_FRAME).with(EYE, Predicates.equalTo(true)).with(FACING, Predicates.equalTo(Direction.SOUTH)))).where('>', CachedBlockPosition.matchesBlockState(BlockStatePredicate.forBlock(Blocks.END_PORTAL_FRAME).with(EYE, Predicates.equalTo(true)).with(FACING, Predicates.equalTo(Direction.WEST)))).where('v', CachedBlockPosition.matchesBlockState(BlockStatePredicate.forBlock(Blocks.END_PORTAL_FRAME).with(EYE, Predicates.equalTo(true)).with(FACING, Predicates.equalTo(Direction.NORTH)))).where('<', CachedBlockPosition.matchesBlockState(BlockStatePredicate.forBlock(Blocks.END_PORTAL_FRAME).with(EYE, Predicates.equalTo(true)).with(FACING, Predicates.equalTo(Direction.EAST)))).build();
        }
        return completedFrame;
    }

    @Override
    protected boolean canPathfindThrough(BlockState state, NavigationType type) {
        return false;
    }
}

