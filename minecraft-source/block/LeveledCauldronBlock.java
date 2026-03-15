/*
 * External method calls:
 *   Lnet/minecraft/block/BlockState;with(Lnet/minecraft/state/property/Property;Ljava/lang/Comparable;)Ljava/lang/Object;
 *   Lnet/minecraft/entity/EntityCollisionHandler;addPreCallback(Lnet/minecraft/entity/CollisionEvent;Ljava/util/function/Consumer;)V
 *   Lnet/minecraft/entity/EntityCollisionHandler;addEvent(Lnet/minecraft/entity/CollisionEvent;)V
 *   Lnet/minecraft/world/event/GameEvent$Emitter;of(Lnet/minecraft/block/BlockState;)Lnet/minecraft/world/event/GameEvent$Emitter;
 *   Lnet/minecraft/world/World;emitGameEvent(Lnet/minecraft/registry/entry/RegistryEntry;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/world/event/GameEvent$Emitter;)V
 *   Lnet/minecraft/block/BlockState;cycle(Lnet/minecraft/state/property/Property;)Ljava/lang/Object;
 *   Lnet/minecraft/world/World;syncWorldEvent(ILnet/minecraft/util/math/BlockPos;I)V
 *   Lnet/minecraft/block/Block;createShapeArray(ILjava/util/function/IntFunction;)[Lnet/minecraft/util/shape/VoxelShape;
 *   Lnet/minecraft/block/Block;createColumnShape(DDD)Lnet/minecraft/util/shape/VoxelShape;
 *   Lnet/minecraft/util/shape/VoxelShapes;union(Lnet/minecraft/util/shape/VoxelShape;Lnet/minecraft/util/shape/VoxelShape;)Lnet/minecraft/util/shape/VoxelShape;
 *   Lnet/minecraft/util/Util;make(Ljava/util/function/Supplier;)Ljava/lang/Object;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/block/LeveledCauldronBlock;decrementFluidLevel(Lnet/minecraft/block/BlockState;Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;)V
 *   Lnet/minecraft/block/LeveledCauldronBlock;onFireCollision(Lnet/minecraft/block/BlockState;Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;)V
 *   Lnet/minecraft/block/LeveledCauldronBlock;createSettingsCodec()Lcom/mojang/serialization/codecs/RecordCodecBuilder;
 */
package net.minecraft.block;

import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.AbstractCauldronBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CauldronBlock;
import net.minecraft.block.cauldron.CauldronBehavior;
import net.minecraft.entity.CollisionEvent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCollisionHandler;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldEvents;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.event.GameEvent;

public class LeveledCauldronBlock
extends AbstractCauldronBlock {
    public static final MapCodec<LeveledCauldronBlock> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(((MapCodec)Biome.Precipitation.CODEC.fieldOf("precipitation")).forGetter(block -> block.precipitation), ((MapCodec)CauldronBehavior.CODEC.fieldOf("interactions")).forGetter(block -> block.behaviorMap), LeveledCauldronBlock.createSettingsCodec()).apply((Applicative<LeveledCauldronBlock, ?>)instance, LeveledCauldronBlock::new));
    public static final int MIN_LEVEL = 1;
    public static final int MAX_LEVEL = 3;
    public static final IntProperty LEVEL = Properties.LEVEL_3;
    private static final int BASE_FLUID_HEIGHT = 6;
    private static final double FLUID_HEIGHT_PER_LEVEL = 3.0;
    private static final VoxelShape[] INSIDE_COLLISION_SHAPE_BY_LEVEL = Util.make(() -> Block.createShapeArray(2, level -> VoxelShapes.union(AbstractCauldronBlock.OUTLINE_SHAPE, Block.createColumnShape(12.0, 4.0, LeveledCauldronBlock.getFluidHeight(level + 1)))));
    private final Biome.Precipitation precipitation;

    public MapCodec<LeveledCauldronBlock> getCodec() {
        return CODEC;
    }

    public LeveledCauldronBlock(Biome.Precipitation precipitation, CauldronBehavior.CauldronBehaviorMap behaviorMap, AbstractBlock.Settings settings) {
        super(settings, behaviorMap);
        this.precipitation = precipitation;
        this.setDefaultState((BlockState)((BlockState)this.stateManager.getDefaultState()).with(LEVEL, 1));
    }

    @Override
    public boolean isFull(BlockState state) {
        return state.get(LEVEL) == 3;
    }

    @Override
    protected boolean canBeFilledByDripstone(Fluid fluid) {
        return fluid == Fluids.WATER && this.precipitation == Biome.Precipitation.RAIN;
    }

    @Override
    protected double getFluidHeight(BlockState state) {
        return LeveledCauldronBlock.getFluidHeight(state.get(LEVEL)) / 16.0;
    }

    private static double getFluidHeight(int level) {
        return 6.0 + (double)level * 3.0;
    }

    @Override
    protected VoxelShape getInsideCollisionShape(BlockState state, BlockView world, BlockPos pos, Entity entity) {
        return INSIDE_COLLISION_SHAPE_BY_LEVEL[state.get(LEVEL) - 1];
    }

    @Override
    protected void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity, EntityCollisionHandler handler, boolean bl) {
        if (world instanceof ServerWorld) {
            ServerWorld lv = (ServerWorld)world;
            BlockPos lv2 = pos.toImmutable();
            handler.addPreCallback(CollisionEvent.EXTINGUISH, collidedEntity -> {
                if (collidedEntity.isOnFire() && collidedEntity.canModifyAt(lv, lv2)) {
                    this.onFireCollision(state, world, lv2);
                }
            });
        }
        handler.addEvent(CollisionEvent.EXTINGUISH);
    }

    private void onFireCollision(BlockState state, World world, BlockPos pos) {
        if (this.precipitation == Biome.Precipitation.SNOW) {
            LeveledCauldronBlock.decrementFluidLevel((BlockState)Blocks.WATER_CAULDRON.getDefaultState().with(LEVEL, state.get(LEVEL)), world, pos);
        } else {
            LeveledCauldronBlock.decrementFluidLevel(state, world, pos);
        }
    }

    public static void decrementFluidLevel(BlockState state, World world, BlockPos pos) {
        int i = state.get(LEVEL) - 1;
        BlockState lv = i == 0 ? Blocks.CAULDRON.getDefaultState() : (BlockState)state.with(LEVEL, i);
        world.setBlockState(pos, lv);
        world.emitGameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Emitter.of(lv));
    }

    @Override
    public void precipitationTick(BlockState state, World world, BlockPos pos, Biome.Precipitation precipitation) {
        if (!CauldronBlock.canFillWithPrecipitation(world, precipitation) || state.get(LEVEL) == 3 || precipitation != this.precipitation) {
            return;
        }
        BlockState lv = (BlockState)state.cycle(LEVEL);
        world.setBlockState(pos, lv);
        world.emitGameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Emitter.of(lv));
    }

    @Override
    protected int getComparatorOutput(BlockState state, World world, BlockPos pos, Direction direction) {
        return state.get(LEVEL);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(LEVEL);
    }

    @Override
    protected void fillFromDripstone(BlockState state, World world, BlockPos pos, Fluid fluid) {
        if (this.isFull(state)) {
            return;
        }
        BlockState lv = (BlockState)state.with(LEVEL, state.get(LEVEL) + 1);
        world.setBlockState(pos, lv);
        world.emitGameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Emitter.of(lv));
        world.syncWorldEvent(WorldEvents.POINTED_DRIPSTONE_DRIPS_WATER_INTO_CAULDRON, pos, 0);
    }
}

