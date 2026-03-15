/*
 * External method calls:
 *   Lnet/minecraft/block/BlockState;with(Lnet/minecraft/state/property/Property;Ljava/lang/Comparable;)Ljava/lang/Object;
 *   Lnet/minecraft/block/spawner/TrialSpawnerLogic;tickClient(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Z)V
 *   Lnet/minecraft/block/spawner/TrialSpawnerLogic;tickServer(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/util/math/BlockPos;Z)V
 *
 * Internal private/static methods:
 *   Lnet/minecraft/block/TrialSpawnerBlock;validateTicker(Lnet/minecraft/block/entity/BlockEntityType;Lnet/minecraft/block/entity/BlockEntityType;Lnet/minecraft/block/entity/BlockEntityTicker;)Lnet/minecraft/block/entity/BlockEntityTicker;
 *   Lnet/minecraft/block/TrialSpawnerBlock;createCodec(Ljava/util/function/Function;)Lcom/mojang/serialization/MapCodec;
 */
package net.minecraft.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.TrialSpawnerBlockEntity;
import net.minecraft.block.enums.TrialSpawnerState;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jspecify.annotations.Nullable;

public class TrialSpawnerBlock
extends BlockWithEntity {
    public static final MapCodec<TrialSpawnerBlock> CODEC = TrialSpawnerBlock.createCodec(TrialSpawnerBlock::new);
    public static final EnumProperty<TrialSpawnerState> TRIAL_SPAWNER_STATE = Properties.TRIAL_SPAWNER_STATE;
    public static final BooleanProperty OMINOUS = Properties.OMINOUS;

    public MapCodec<TrialSpawnerBlock> getCodec() {
        return CODEC;
    }

    public TrialSpawnerBlock(AbstractBlock.Settings arg) {
        super(arg);
        this.setDefaultState((BlockState)((BlockState)((BlockState)this.stateManager.getDefaultState()).with(TRIAL_SPAWNER_STATE, TrialSpawnerState.INACTIVE)).with(OMINOUS, false));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(TRIAL_SPAWNER_STATE, OMINOUS);
    }

    @Override
    public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new TrialSpawnerBlockEntity(pos, state);
    }

    @Override
    public <T extends BlockEntity> @Nullable BlockEntityTicker<T> getTicker(World world2, BlockState state2, BlockEntityType<T> type) {
        BlockEntityTicker<T> blockEntityTicker;
        if (world2 instanceof ServerWorld) {
            ServerWorld lv = (ServerWorld)world2;
            blockEntityTicker = TrialSpawnerBlock.validateTicker(type, BlockEntityType.TRIAL_SPAWNER, (world, pos, state, blockEntity) -> blockEntity.getSpawner().tickServer(lv, pos, state.getOrEmpty(Properties.OMINOUS).orElse(false)));
        } else {
            blockEntityTicker = TrialSpawnerBlock.validateTicker(type, BlockEntityType.TRIAL_SPAWNER, (world, pos, state, blockEntity) -> blockEntity.getSpawner().tickClient(world, pos, state.getOrEmpty(Properties.OMINOUS).orElse(false)));
        }
        return blockEntityTicker;
    }
}

