/*
 * External method calls:
 *   Lnet/minecraft/block/BlockState;with(Lnet/minecraft/state/property/Property;Ljava/lang/Comparable;)Ljava/lang/Object;
 *   Lnet/minecraft/world/World;playSoundClient(DDDLnet/minecraft/sound/SoundEvent;Lnet/minecraft/sound/SoundCategory;FFZ)V
 *   Lnet/minecraft/world/tick/ScheduledTickView;scheduleBlockTick(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/Block;I)V
 *   Lnet/minecraft/block/PillarBlock;changeRotation(Lnet/minecraft/block/BlockState;Lnet/minecraft/util/BlockRotation;)Lnet/minecraft/block/BlockState;
 *   Lnet/minecraft/util/ItemScatterer;onStateReplaced(Lnet/minecraft/block/BlockState;Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;)V
 *   Lnet/minecraft/block/entity/CreakingHeartBlockEntity;killPuppet(Lnet/minecraft/entity/damage/DamageSource;)V
 *   Lnet/minecraft/block/BlockWithEntity;onExploded(Lnet/minecraft/block/BlockState;Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/world/explosion/Explosion;Ljava/util/function/BiConsumer;)V
 *   Lnet/minecraft/entity/damage/DamageSources;playerAttack(Lnet/minecraft/entity/player/PlayerEntity;)Lnet/minecraft/entity/damage/DamageSource;
 *   Lnet/minecraft/block/BlockWithEntity;onBreak(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;Lnet/minecraft/entity/player/PlayerEntity;)Lnet/minecraft/block/BlockState;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/block/CreakingHeartBlock;validateTicker(Lnet/minecraft/block/entity/BlockEntityType;Lnet/minecraft/block/entity/BlockEntityType;Lnet/minecraft/block/entity/BlockEntityTicker;)Lnet/minecraft/block/entity/BlockEntityTicker;
 *   Lnet/minecraft/block/CreakingHeartBlock;enableIfValid(Lnet/minecraft/block/BlockState;Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/block/BlockState;
 *   Lnet/minecraft/block/CreakingHeartBlock;dropExperienceOnBreak(Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/block/BlockState;Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;)V
 *   Lnet/minecraft/block/CreakingHeartBlock;dropExperience(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/util/math/BlockPos;I)V
 *   Lnet/minecraft/block/CreakingHeartBlock;createCodec(Ljava/util/function/Function;)Lcom/mojang/serialization/MapCodec;
 */
package net.minecraft.block;

import com.mojang.serialization.MapCodec;
import java.util.function.BiConsumer;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.PillarBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.CreakingHeartBlockEntity;
import net.minecraft.block.enums.CreakingHeartState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import net.minecraft.world.attribute.EnvironmentAttributes;
import net.minecraft.world.explosion.Explosion;
import net.minecraft.world.explosion.ExplosionImpl;
import net.minecraft.world.tick.ScheduledTickView;
import org.jspecify.annotations.Nullable;

public class CreakingHeartBlock
extends BlockWithEntity {
    public static final MapCodec<CreakingHeartBlock> CODEC = CreakingHeartBlock.createCodec(CreakingHeartBlock::new);
    public static final EnumProperty<Direction.Axis> AXIS = Properties.AXIS;
    public static final EnumProperty<CreakingHeartState> ACTIVE = Properties.CREAKING_HEART_STATE;
    public static final BooleanProperty NATURAL = Properties.NATURAL;

    public MapCodec<CreakingHeartBlock> getCodec() {
        return CODEC;
    }

    protected CreakingHeartBlock(AbstractBlock.Settings arg) {
        super(arg);
        this.setDefaultState((BlockState)((BlockState)((BlockState)this.getDefaultState().with(AXIS, Direction.Axis.Y)).with(ACTIVE, CreakingHeartState.UPROOTED)).with(NATURAL, false));
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new CreakingHeartBlockEntity(pos, state);
    }

    @Override
    public <T extends BlockEntity> @Nullable BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        if (world.isClient()) {
            return null;
        }
        if (state.get(ACTIVE) != CreakingHeartState.UPROOTED) {
            return CreakingHeartBlock.validateTicker(type, BlockEntityType.CREAKING_HEART, CreakingHeartBlockEntity::tick);
        }
        return null;
    }

    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        if (!world.getEnvironmentAttributes().getAttributeValue(EnvironmentAttributes.CREAKING_ACTIVE_GAMEPLAY, pos).booleanValue()) {
            return;
        }
        if (state.get(ACTIVE) == CreakingHeartState.UPROOTED) {
            return;
        }
        if (random.nextInt(16) == 0 && CreakingHeartBlock.isSurroundedByPaleOakLogs(world, pos)) {
            world.playSoundClient(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.BLOCK_CREAKING_HEART_IDLE, SoundCategory.BLOCKS, 1.0f, 1.0f, false);
        }
    }

    @Override
    protected BlockState getStateForNeighborUpdate(BlockState state, WorldView world, ScheduledTickView tickView, BlockPos pos, Direction direction, BlockPos neighborPos, BlockState neighborState, Random random) {
        tickView.scheduleBlockTick(pos, this, 1);
        return super.getStateForNeighborUpdate(state, world, tickView, pos, direction, neighborPos, neighborState, random);
    }

    @Override
    protected void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        BlockState lv = CreakingHeartBlock.enableIfValid(state, world, pos);
        if (lv != state) {
            world.setBlockState(pos, lv, Block.NOTIFY_ALL);
        }
    }

    private static BlockState enableIfValid(BlockState state, World arg2, BlockPos pos) {
        boolean bl2;
        boolean bl = CreakingHeartBlock.shouldBeEnabled(state, arg2, pos);
        boolean bl3 = bl2 = state.get(ACTIVE) == CreakingHeartState.UPROOTED;
        if (bl && bl2) {
            return (BlockState)state.with(ACTIVE, arg2.getEnvironmentAttributes().getAttributeValue(EnvironmentAttributes.CREAKING_ACTIVE_GAMEPLAY, pos) != false ? CreakingHeartState.AWAKE : CreakingHeartState.DORMANT);
        }
        return state;
    }

    public static boolean shouldBeEnabled(BlockState state, WorldView world, BlockPos pos) {
        Direction.Axis lv = state.get(AXIS);
        for (Direction lv2 : lv.getDirections()) {
            BlockState lv3 = world.getBlockState(pos.offset(lv2));
            if (lv3.isIn(BlockTags.PALE_OAK_LOGS) && lv3.get(AXIS) == lv) continue;
            return false;
        }
        return true;
    }

    private static boolean isSurroundedByPaleOakLogs(WorldAccess world, BlockPos pos) {
        for (Direction lv : Direction.values()) {
            BlockPos lv2 = pos.offset(lv);
            BlockState lv3 = world.getBlockState(lv2);
            if (lv3.isIn(BlockTags.PALE_OAK_LOGS)) continue;
            return false;
        }
        return true;
    }

    @Override
    public @Nullable BlockState getPlacementState(ItemPlacementContext ctx) {
        return CreakingHeartBlock.enableIfValid((BlockState)this.getDefaultState().with(AXIS, ctx.getSide().getAxis()), ctx.getWorld(), ctx.getBlockPos());
    }

    @Override
    protected BlockState rotate(BlockState state, BlockRotation rotation) {
        return PillarBlock.changeRotation(state, rotation);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(AXIS, ACTIVE, NATURAL);
    }

    @Override
    protected void onStateReplaced(BlockState state, ServerWorld world, BlockPos pos, boolean moved) {
        ItemScatterer.onStateReplaced(state, world, pos);
    }

    @Override
    protected void onExploded(BlockState state, ServerWorld world, BlockPos pos, Explosion explosion, BiConsumer<ItemStack, BlockPos> stackMerger) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof CreakingHeartBlockEntity) {
            CreakingHeartBlockEntity lv = (CreakingHeartBlockEntity)blockEntity;
            if (explosion instanceof ExplosionImpl) {
                ExplosionImpl lv2 = (ExplosionImpl)explosion;
                if (explosion.getDestructionType().destroysBlocks()) {
                    lv.killPuppet(lv2.getDamageSource());
                    LivingEntity livingEntity = explosion.getCausingEntity();
                    if (livingEntity instanceof PlayerEntity) {
                        PlayerEntity lv3 = (PlayerEntity)livingEntity;
                        if (explosion.getDestructionType().destroysBlocks()) {
                            this.dropExperienceOnBreak(lv3, state, world, pos);
                        }
                    }
                }
            }
        }
        super.onExploded(state, world, pos, explosion, stackMerger);
    }

    @Override
    public BlockState onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof CreakingHeartBlockEntity) {
            CreakingHeartBlockEntity lv = (CreakingHeartBlockEntity)blockEntity;
            lv.killPuppet(player.getDamageSources().playerAttack(player));
            this.dropExperienceOnBreak(player, state, world, pos);
        }
        return super.onBreak(world, pos, state, player);
    }

    private void dropExperienceOnBreak(PlayerEntity player, BlockState state, World world, BlockPos pos) {
        if (!player.shouldSkipBlockDrops() && !player.isSpectator() && state.get(NATURAL).booleanValue() && world instanceof ServerWorld) {
            ServerWorld lv = (ServerWorld)world;
            this.dropExperience(lv, pos, world.random.nextBetween(20, 24));
        }
    }

    @Override
    protected boolean hasComparatorOutput(BlockState state) {
        return true;
    }

    @Override
    protected int getComparatorOutput(BlockState state, World world, BlockPos pos, Direction direction) {
        if (state.get(ACTIVE) == CreakingHeartState.UPROOTED) {
            return 0;
        }
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (!(blockEntity instanceof CreakingHeartBlockEntity)) {
            return 0;
        }
        CreakingHeartBlockEntity lv = (CreakingHeartBlockEntity)blockEntity;
        return lv.getComparatorOutput();
    }
}

