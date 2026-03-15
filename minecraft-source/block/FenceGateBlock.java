/*
 * External method calls:
 *   Lnet/minecraft/block/WoodType;soundType()Lnet/minecraft/sound/BlockSoundGroup;
 *   Lnet/minecraft/block/AbstractBlock$Settings;sounds(Lnet/minecraft/sound/BlockSoundGroup;)Lnet/minecraft/block/AbstractBlock$Settings;
 *   Lnet/minecraft/block/BlockState;with(Lnet/minecraft/state/property/Property;Ljava/lang/Comparable;)Ljava/lang/Object;
 *   Lnet/minecraft/util/shape/VoxelShapes;empty()Lnet/minecraft/util/shape/VoxelShape;
 *   Lnet/minecraft/block/WoodType;fenceGateOpen()Lnet/minecraft/sound/SoundEvent;
 *   Lnet/minecraft/block/WoodType;fenceGateClose()Lnet/minecraft/sound/SoundEvent;
 *   Lnet/minecraft/world/World;playSound(Lnet/minecraft/entity/Entity;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/sound/SoundEvent;Lnet/minecraft/sound/SoundCategory;FF)V
 *   Lnet/minecraft/world/World;emitGameEvent(Lnet/minecraft/entity/Entity;Lnet/minecraft/registry/entry/RegistryEntry;Lnet/minecraft/util/math/BlockPos;)V
 *   Lnet/minecraft/server/world/ServerWorld;playSound(Lnet/minecraft/entity/Entity;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/sound/SoundEvent;Lnet/minecraft/sound/SoundCategory;FF)V
 *   Lnet/minecraft/world/event/GameEvent$Emitter;of(Lnet/minecraft/block/BlockState;)Lnet/minecraft/world/event/GameEvent$Emitter;
 *   Lnet/minecraft/server/world/ServerWorld;emitGameEvent(Lnet/minecraft/registry/entry/RegistryEntry;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/world/event/GameEvent$Emitter;)V
 *   Lnet/minecraft/block/HorizontalFacingBlock;onExploded(Lnet/minecraft/block/BlockState;Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/world/explosion/Explosion;Ljava/util/function/BiConsumer;)V
 *   Lnet/minecraft/util/shape/VoxelShape;offset(DDD)Lnet/minecraft/util/shape/VoxelShape;
 *   Lnet/minecraft/util/shape/VoxelShape;simplify()Lnet/minecraft/util/shape/VoxelShape;
 *   Lnet/minecraft/block/Block;createColumnShape(DDD)Lnet/minecraft/util/shape/VoxelShape;
 *   Lnet/minecraft/util/shape/VoxelShapes;combineAndSimplify(Lnet/minecraft/util/shape/VoxelShape;Lnet/minecraft/util/shape/VoxelShape;Lnet/minecraft/util/function/BooleanBiFunction;)Lnet/minecraft/util/shape/VoxelShape;
 *   Lnet/minecraft/block/Block;createCuboidShape(DDD)Lnet/minecraft/util/shape/VoxelShape;
 *   Lnet/minecraft/util/shape/VoxelShapes;createHorizontalAxisShapeMap(Lnet/minecraft/util/shape/VoxelShape;)Ljava/util/Map;
 *   Lnet/minecraft/util/Util;transformMapValues(Ljava/util/Map;Ljava/util/function/Function;)Ljava/util/Map;
 *   Lnet/minecraft/block/Block;createColumnShape(DDDD)Lnet/minecraft/util/shape/VoxelShape;
 *   Lnet/minecraft/block/Block;createCuboidShape(DDDDDD)Lnet/minecraft/util/shape/VoxelShape;
 *   Lnet/minecraft/util/shape/VoxelShapes;union(Lnet/minecraft/util/shape/VoxelShape;Lnet/minecraft/util/shape/VoxelShape;)Lnet/minecraft/util/shape/VoxelShape;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/block/FenceGateBlock;createSettingsCodec()Lcom/mojang/serialization/codecs/RecordCodecBuilder;
 */
package net.minecraft.block;

import com.google.common.collect.Maps;
import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Map;
import java.util.function.BiConsumer;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.WoodType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Util;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import net.minecraft.world.block.WireOrientation;
import net.minecraft.world.event.GameEvent;
import net.minecraft.world.explosion.Explosion;
import net.minecraft.world.tick.ScheduledTickView;
import org.jspecify.annotations.Nullable;

public class FenceGateBlock
extends HorizontalFacingBlock {
    public static final MapCodec<FenceGateBlock> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(((MapCodec)WoodType.CODEC.fieldOf("wood_type")).forGetter(block -> block.type), FenceGateBlock.createSettingsCodec()).apply((Applicative<FenceGateBlock, ?>)instance, FenceGateBlock::new));
    public static final BooleanProperty OPEN = Properties.OPEN;
    public static final BooleanProperty POWERED = Properties.POWERED;
    public static final BooleanProperty IN_WALL = Properties.IN_WALL;
    private static final Map<Direction.Axis, VoxelShape> REGULAR_OUTLINE_SHAPES = VoxelShapes.createHorizontalAxisShapeMap(Block.createCuboidShape(16.0, 16.0, 4.0));
    private static final Map<Direction.Axis, VoxelShape> IN_WALL_OUTLINE_SHAPES = Maps.newEnumMap(Util.transformMapValues(REGULAR_OUTLINE_SHAPES, shape -> VoxelShapes.combineAndSimplify(shape, Block.createColumnShape(16.0, 13.0, 16.0), BooleanBiFunction.ONLY_FIRST)));
    private static final Map<Direction.Axis, VoxelShape> CLOSED_COLLISION_SHAPES = VoxelShapes.createHorizontalAxisShapeMap(Block.createColumnShape(16.0, 4.0, 0.0, 24.0));
    private static final Map<Direction.Axis, VoxelShape> CLOSED_SIDES_SHAPES = VoxelShapes.createHorizontalAxisShapeMap(Block.createColumnShape(16.0, 4.0, 5.0, 24.0));
    private static final Map<Direction.Axis, VoxelShape> REGULAR_CULLING_SHAPES = VoxelShapes.createHorizontalAxisShapeMap(VoxelShapes.union(Block.createCuboidShape(0.0, 5.0, 7.0, 2.0, 16.0, 9.0), Block.createCuboidShape(14.0, 5.0, 7.0, 16.0, 16.0, 9.0)));
    private static final Map<Direction.Axis, VoxelShape> IN_WALL_CULLING_SHAPES = Maps.newEnumMap(Util.transformMapValues(REGULAR_CULLING_SHAPES, shape -> shape.offset(0.0, -0.1875, 0.0).simplify()));
    private final WoodType type;

    public MapCodec<FenceGateBlock> getCodec() {
        return CODEC;
    }

    public FenceGateBlock(WoodType type, AbstractBlock.Settings settings) {
        super(settings.sounds(type.soundType()));
        this.type = type;
        this.setDefaultState((BlockState)((BlockState)((BlockState)((BlockState)this.stateManager.getDefaultState()).with(OPEN, false)).with(POWERED, false)).with(IN_WALL, false));
    }

    @Override
    protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        Direction.Axis lv = ((Direction)state.get(FACING)).getAxis();
        return (state.get(IN_WALL) != false ? IN_WALL_OUTLINE_SHAPES : REGULAR_OUTLINE_SHAPES).get(lv);
    }

    @Override
    protected BlockState getStateForNeighborUpdate(BlockState state, WorldView world, ScheduledTickView tickView, BlockPos pos, Direction direction, BlockPos neighborPos, BlockState neighborState, Random random) {
        Direction.Axis lv = direction.getAxis();
        if (((Direction)state.get(FACING)).rotateYClockwise().getAxis() == lv) {
            boolean bl = this.isWall(neighborState) || this.isWall(world.getBlockState(pos.offset(direction.getOpposite())));
            return (BlockState)state.with(IN_WALL, bl);
        }
        return super.getStateForNeighborUpdate(state, world, tickView, pos, direction, neighborPos, neighborState, random);
    }

    @Override
    protected VoxelShape getSidesShape(BlockState state, BlockView world, BlockPos pos) {
        Direction.Axis lv = ((Direction)state.get(FACING)).getAxis();
        return state.get(OPEN) != false ? VoxelShapes.empty() : CLOSED_SIDES_SHAPES.get(lv);
    }

    @Override
    protected VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        Direction.Axis lv = ((Direction)state.get(FACING)).getAxis();
        return state.get(OPEN) != false ? VoxelShapes.empty() : CLOSED_COLLISION_SHAPES.get(lv);
    }

    @Override
    protected VoxelShape getCullingShape(BlockState state) {
        Direction.Axis lv = ((Direction)state.get(FACING)).getAxis();
        return (state.get(IN_WALL) != false ? IN_WALL_CULLING_SHAPES : REGULAR_CULLING_SHAPES).get(lv);
    }

    @Override
    protected boolean canPathfindThrough(BlockState state, NavigationType type) {
        switch (type) {
            case LAND: {
                return state.get(OPEN);
            }
            case WATER: {
                return false;
            }
            case AIR: {
                return state.get(OPEN);
            }
        }
        return false;
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        World lv = ctx.getWorld();
        BlockPos lv2 = ctx.getBlockPos();
        boolean bl = lv.isReceivingRedstonePower(lv2);
        Direction lv3 = ctx.getHorizontalPlayerFacing();
        Direction.Axis lv4 = lv3.getAxis();
        boolean bl2 = lv4 == Direction.Axis.Z && (this.isWall(lv.getBlockState(lv2.west())) || this.isWall(lv.getBlockState(lv2.east()))) || lv4 == Direction.Axis.X && (this.isWall(lv.getBlockState(lv2.north())) || this.isWall(lv.getBlockState(lv2.south())));
        return (BlockState)((BlockState)((BlockState)((BlockState)this.getDefaultState().with(FACING, lv3)).with(OPEN, bl)).with(POWERED, bl)).with(IN_WALL, bl2);
    }

    private boolean isWall(BlockState state) {
        return state.isIn(BlockTags.WALLS);
    }

    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        if (state.get(OPEN).booleanValue()) {
            state = (BlockState)state.with(OPEN, false);
            world.setBlockState(pos, state, Block.NOTIFY_LISTENERS | Block.REDRAW_ON_MAIN_THREAD);
        } else {
            Direction lv = player.getHorizontalFacing();
            if (state.get(FACING) == lv.getOpposite()) {
                state = (BlockState)state.with(FACING, lv);
            }
            state = (BlockState)state.with(OPEN, true);
            world.setBlockState(pos, state, Block.NOTIFY_LISTENERS | Block.REDRAW_ON_MAIN_THREAD);
        }
        boolean bl = state.get(OPEN);
        world.playSound((Entity)player, pos, bl ? this.type.fenceGateOpen() : this.type.fenceGateClose(), SoundCategory.BLOCKS, 1.0f, world.getRandom().nextFloat() * 0.1f + 0.9f);
        world.emitGameEvent((Entity)player, bl ? GameEvent.BLOCK_OPEN : GameEvent.BLOCK_CLOSE, pos);
        return ActionResult.SUCCESS;
    }

    @Override
    protected void onExploded(BlockState state, ServerWorld world, BlockPos pos, Explosion explosion, BiConsumer<ItemStack, BlockPos> stackMerger) {
        if (explosion.canTriggerBlocks() && !state.get(POWERED).booleanValue()) {
            boolean bl = state.get(OPEN);
            world.setBlockState(pos, (BlockState)state.with(OPEN, !bl));
            world.playSound(null, pos, bl ? this.type.fenceGateClose() : this.type.fenceGateOpen(), SoundCategory.BLOCKS, 1.0f, world.getRandom().nextFloat() * 0.1f + 0.9f);
            world.emitGameEvent(bl ? GameEvent.BLOCK_CLOSE : GameEvent.BLOCK_OPEN, pos, GameEvent.Emitter.of(state));
        }
        super.onExploded(state, world, pos, explosion, stackMerger);
    }

    @Override
    protected void neighborUpdate(BlockState state, World world, BlockPos pos, Block sourceBlock, @Nullable WireOrientation wireOrientation, boolean notify) {
        if (world.isClient()) {
            return;
        }
        boolean bl2 = world.isReceivingRedstonePower(pos);
        if (state.get(POWERED) != bl2) {
            world.setBlockState(pos, (BlockState)((BlockState)state.with(POWERED, bl2)).with(OPEN, bl2), Block.NOTIFY_LISTENERS);
            if (state.get(OPEN) != bl2) {
                world.playSound(null, pos, bl2 ? this.type.fenceGateOpen() : this.type.fenceGateClose(), SoundCategory.BLOCKS, 1.0f, world.getRandom().nextFloat() * 0.1f + 0.9f);
                world.emitGameEvent(null, bl2 ? GameEvent.BLOCK_OPEN : GameEvent.BLOCK_CLOSE, pos);
            }
        }
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING, OPEN, POWERED, IN_WALL);
    }

    public static boolean canWallConnect(BlockState state, Direction side) {
        return ((Direction)state.get(FACING)).getAxis() == side.rotateYClockwise().getAxis();
    }
}

