/*
 * External method calls:
 *   Lnet/minecraft/world/tick/ScheduledTickView;scheduleFluidTick(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/fluid/Fluid;I)V
 *   Lnet/minecraft/item/SignChangingItem;useOnSign(Lnet/minecraft/world/World;Lnet/minecraft/block/entity/SignBlockEntity;ZLnet/minecraft/entity/player/PlayerEntity;)Z
 *   Lnet/minecraft/block/entity/SignBlockEntity;runCommandClickEvent(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/util/math/BlockPos;Z)Z
 *   Lnet/minecraft/entity/player/PlayerEntity;incrementStat(Lnet/minecraft/stat/Stat;)V
 *   Lnet/minecraft/world/event/GameEvent$Emitter;of(Lnet/minecraft/entity/Entity;Lnet/minecraft/block/BlockState;)Lnet/minecraft/world/event/GameEvent$Emitter;
 *   Lnet/minecraft/server/world/ServerWorld;emitGameEvent(Lnet/minecraft/registry/entry/RegistryEntry;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/world/event/GameEvent$Emitter;)V
 *   Lnet/minecraft/item/ItemStack;decrementUnlessCreative(ILnet/minecraft/entity/LivingEntity;)V
 *   Lnet/minecraft/server/world/ServerWorld;playSound(Lnet/minecraft/entity/Entity;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/sound/SoundEvent;Lnet/minecraft/sound/SoundCategory;)V
 *   Lnet/minecraft/entity/player/PlayerEntity;openEditSignScreen(Lnet/minecraft/block/entity/SignBlockEntity;Z)V
 *   Lnet/minecraft/block/Block;createColumnShape(DDD)Lnet/minecraft/util/shape/VoxelShape;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/block/AbstractSignBlock;openEditScreen(Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/block/entity/SignBlockEntity;Z)V
 *   Lnet/minecraft/block/AbstractSignBlock;validateTicker(Lnet/minecraft/block/entity/BlockEntityType;Lnet/minecraft/block/entity/BlockEntityType;Lnet/minecraft/block/entity/BlockEntityTicker;)Lnet/minecraft/block/entity/BlockEntityTicker;
 */
package net.minecraft.block;

import com.mojang.serialization.MapCodec;
import java.util.Arrays;
import java.util.UUID;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.Waterloggable;
import net.minecraft.block.WoodType;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.SignBlockEntity;
import net.minecraft.block.entity.SignText;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SignChangingItem;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.stat.Stats;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.text.PlainTextContent;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Util;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import net.minecraft.world.event.GameEvent;
import net.minecraft.world.tick.ScheduledTickView;
import org.jspecify.annotations.Nullable;

public abstract class AbstractSignBlock
extends BlockWithEntity
implements Waterloggable {
    public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;
    private static final VoxelShape SHAPE = Block.createColumnShape(8.0, 0.0, 16.0);
    private final WoodType type;

    protected AbstractSignBlock(WoodType type, AbstractBlock.Settings settings) {
        super(settings);
        this.type = type;
    }

    protected abstract MapCodec<? extends AbstractSignBlock> getCodec();

    @Override
    protected BlockState getStateForNeighborUpdate(BlockState state, WorldView world, ScheduledTickView tickView, BlockPos pos, Direction direction, BlockPos neighborPos, BlockState neighborState, Random random) {
        if (state.get(WATERLOGGED).booleanValue()) {
            tickView.scheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
        }
        return super.getStateForNeighborUpdate(state, world, tickView, pos, direction, neighborPos, neighborState, random);
    }

    @Override
    protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    @Override
    public boolean canMobSpawnInside(BlockState state) {
        return true;
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new SignBlockEntity(pos, state);
    }

    @Override
    protected ActionResult onUseWithItem(ItemStack stack, BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        boolean bl;
        SignChangingItem lv2;
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (!(blockEntity instanceof SignBlockEntity)) {
            return ActionResult.PASS;
        }
        SignBlockEntity lv = (SignBlockEntity)blockEntity;
        Item item = stack.getItem();
        SignChangingItem lv3 = item instanceof SignChangingItem ? (lv2 = (SignChangingItem)((Object)item)) : null;
        boolean bl2 = bl = lv3 != null && player.canModifyBlocks();
        if (!(world instanceof ServerWorld)) {
            return bl || lv.isWaxed() ? ActionResult.SUCCESS : ActionResult.CONSUME;
        }
        ServerWorld lv4 = (ServerWorld)world;
        if (!bl || lv.isWaxed() || this.isOtherPlayerEditing(player, lv)) {
            return ActionResult.PASS_TO_DEFAULT_BLOCK_ACTION;
        }
        boolean bl22 = lv.isPlayerFacingFront(player);
        if (lv3.canUseOnSignText(lv.getText(bl22), player) && lv3.useOnSign(lv4, lv, bl22, player)) {
            lv.runCommandClickEvent(lv4, player, pos, bl22);
            player.incrementStat(Stats.USED.getOrCreateStat(stack.getItem()));
            lv4.emitGameEvent(GameEvent.BLOCK_CHANGE, lv.getPos(), GameEvent.Emitter.of(player, lv.getCachedState()));
            stack.decrementUnlessCreative(1, player);
            return ActionResult.SUCCESS;
        }
        return ActionResult.PASS_TO_DEFAULT_BLOCK_ACTION;
    }

    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (!(blockEntity instanceof SignBlockEntity)) {
            return ActionResult.PASS;
        }
        SignBlockEntity lv = (SignBlockEntity)blockEntity;
        if (!(world instanceof ServerWorld)) {
            Util.getFatalOrPause(new IllegalStateException("Expected to only call this on server"));
            return ActionResult.CONSUME;
        }
        ServerWorld lv2 = (ServerWorld)world;
        boolean bl = lv.isPlayerFacingFront(player);
        boolean bl2 = lv.runCommandClickEvent(lv2, player, pos, bl);
        if (lv.isWaxed()) {
            lv2.playSound(null, lv.getPos(), lv.getInteractionFailSound(), SoundCategory.BLOCKS);
            return ActionResult.SUCCESS_SERVER;
        }
        if (bl2) {
            return ActionResult.SUCCESS_SERVER;
        }
        if (!this.isOtherPlayerEditing(player, lv) && player.canModifyBlocks() && this.isTextLiteralOrEmpty(player, lv, bl)) {
            this.openEditScreen(player, lv, bl);
            return ActionResult.SUCCESS_SERVER;
        }
        return ActionResult.PASS;
    }

    private boolean isTextLiteralOrEmpty(PlayerEntity player, SignBlockEntity blockEntity, boolean front) {
        SignText lv = blockEntity.getText(front);
        return Arrays.stream(lv.getMessages(player.shouldFilterText())).allMatch(message -> message.equals(ScreenTexts.EMPTY) || message.getContent() instanceof PlainTextContent);
    }

    public abstract float getRotationDegrees(BlockState var1);

    public Vec3d getCenter(BlockState state) {
        return new Vec3d(0.5, 0.5, 0.5);
    }

    @Override
    protected FluidState getFluidState(BlockState state) {
        if (state.get(WATERLOGGED).booleanValue()) {
            return Fluids.WATER.getStill(false);
        }
        return super.getFluidState(state);
    }

    public WoodType getWoodType() {
        return this.type;
    }

    public static WoodType getWoodType(Block block) {
        WoodType lv = block instanceof AbstractSignBlock ? ((AbstractSignBlock)block).getWoodType() : WoodType.OAK;
        return lv;
    }

    public void openEditScreen(PlayerEntity player, SignBlockEntity blockEntity, boolean front) {
        blockEntity.setEditor(player.getUuid());
        player.openEditSignScreen(blockEntity, front);
    }

    private boolean isOtherPlayerEditing(PlayerEntity player, SignBlockEntity blockEntity) {
        UUID uUID = blockEntity.getEditor();
        return uUID != null && !uUID.equals(player.getUuid());
    }

    @Override
    public <T extends BlockEntity> @Nullable BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return AbstractSignBlock.validateTicker(type, BlockEntityType.SIGN, SignBlockEntity::tick);
    }
}

