/*
 * External method calls:
 *   Lnet/minecraft/item/Item;use(Lnet/minecraft/world/World;Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/util/Hand;)Lnet/minecraft/util/ActionResult;
 *   Lnet/minecraft/block/Block;onPlaced(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/item/ItemStack;)V
 *   Lnet/minecraft/advancement/criterion/ItemCriterion;trigger(Lnet/minecraft/server/network/ServerPlayerEntity;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/item/ItemStack;)V
 *   Lnet/minecraft/world/World;playSound(Lnet/minecraft/entity/Entity;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/sound/SoundEvent;Lnet/minecraft/sound/SoundCategory;FF)V
 *   Lnet/minecraft/world/event/GameEvent$Emitter;of(Lnet/minecraft/entity/Entity;Lnet/minecraft/block/BlockState;)Lnet/minecraft/world/event/GameEvent$Emitter;
 *   Lnet/minecraft/world/World;emitGameEvent(Lnet/minecraft/registry/entry/RegistryEntry;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/world/event/GameEvent$Emitter;)V
 *   Lnet/minecraft/item/ItemStack;decrementUnlessCreative(ILnet/minecraft/entity/LivingEntity;)V
 *   Lnet/minecraft/block/entity/BlockEntity;readComponents(Lnet/minecraft/item/ItemStack;)V
 *   Lnet/minecraft/component/type/BlockStateComponent;applyToState(Lnet/minecraft/block/BlockState;)Lnet/minecraft/block/BlockState;
 *   Lnet/minecraft/block/ShapeContext;ofPlacement(Lnet/minecraft/entity/player/PlayerEntity;)Lnet/minecraft/block/ShapeContext;
 *   Lnet/minecraft/entity/TypedEntityData;applyToBlockEntity(Lnet/minecraft/block/entity/BlockEntity;Lnet/minecraft/registry/RegistryWrapper$WrapperLookup;)Z
 *   Lnet/minecraft/component/type/ContainerComponent;iterateNonEmptyCopy()Ljava/lang/Iterable;
 *   Lnet/minecraft/item/ItemUsage;spawnItemContents(Lnet/minecraft/entity/ItemEntity;Ljava/lang/Iterable;)V
 *   Lnet/minecraft/block/entity/BlockEntity;writeId(Lnet/minecraft/storage/WriteView;Lnet/minecraft/block/entity/BlockEntityType;)V
 *   Lnet/minecraft/entity/TypedEntityData;create(Ljava/lang/Object;Lnet/minecraft/nbt/NbtCompound;)Lnet/minecraft/entity/TypedEntityData;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/item/BlockItem;place(Lnet/minecraft/item/ItemPlacementContext;)Lnet/minecraft/util/ActionResult;
 *   Lnet/minecraft/item/BlockItem;place(Lnet/minecraft/item/ItemPlacementContext;Lnet/minecraft/block/BlockState;)Z
 *   Lnet/minecraft/item/BlockItem;placeFromNbt(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/world/World;Lnet/minecraft/item/ItemStack;Lnet/minecraft/block/BlockState;)Lnet/minecraft/block/BlockState;
 *   Lnet/minecraft/item/BlockItem;postPlacement(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/world/World;Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/item/ItemStack;Lnet/minecraft/block/BlockState;)Z
 *   Lnet/minecraft/item/BlockItem;copyComponentsToBlockEntity(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/item/ItemStack;)V
 *   Lnet/minecraft/item/BlockItem;writeNbtToBlockEntity(Lnet/minecraft/world/World;Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/item/ItemStack;)Z
 */
package net.minecraft.item;

import java.util.Map;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.command.DefaultPermissions;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.BlockStateComponent;
import net.minecraft.component.type.ContainerComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.TypedEntityData;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.resource.featuretoggle.FeatureSet;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.storage.NbtWriteView;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.jspecify.annotations.Nullable;

public class BlockItem
extends Item {
    @Deprecated
    private final Block block;

    public BlockItem(Block block, Item.Settings settings) {
        super(settings);
        this.block = block;
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        ActionResult lv = this.place(new ItemPlacementContext(context));
        if (!lv.isAccepted() && context.getStack().contains(DataComponentTypes.CONSUMABLE)) {
            return super.use(context.getWorld(), context.getPlayer(), context.getHand());
        }
        return lv;
    }

    public ActionResult place(ItemPlacementContext context) {
        if (!this.getBlock().isEnabled(context.getWorld().getEnabledFeatures())) {
            return ActionResult.FAIL;
        }
        if (!context.canPlace()) {
            return ActionResult.FAIL;
        }
        ItemPlacementContext lv = this.getPlacementContext(context);
        if (lv == null) {
            return ActionResult.FAIL;
        }
        BlockState lv2 = this.getPlacementState(lv);
        if (lv2 == null) {
            return ActionResult.FAIL;
        }
        if (!this.place(lv, lv2)) {
            return ActionResult.FAIL;
        }
        BlockPos lv3 = lv.getBlockPos();
        World lv4 = lv.getWorld();
        PlayerEntity lv5 = lv.getPlayer();
        ItemStack lv6 = lv.getStack();
        BlockState lv7 = lv4.getBlockState(lv3);
        if (lv7.isOf(lv2.getBlock())) {
            lv7 = this.placeFromNbt(lv3, lv4, lv6, lv7);
            this.postPlacement(lv3, lv4, lv5, lv6, lv7);
            BlockItem.copyComponentsToBlockEntity(lv4, lv3, lv6);
            lv7.getBlock().onPlaced(lv4, lv3, lv7, lv5, lv6);
            if (lv5 instanceof ServerPlayerEntity) {
                Criteria.PLACED_BLOCK.trigger((ServerPlayerEntity)lv5, lv3, lv6);
            }
        }
        BlockSoundGroup lv8 = lv7.getSoundGroup();
        lv4.playSound((Entity)lv5, lv3, this.getPlaceSound(lv7), SoundCategory.BLOCKS, (lv8.getVolume() + 1.0f) / 2.0f, lv8.getPitch() * 0.8f);
        lv4.emitGameEvent(GameEvent.BLOCK_PLACE, lv3, GameEvent.Emitter.of(lv5, lv7));
        lv6.decrementUnlessCreative(1, lv5);
        return ActionResult.SUCCESS;
    }

    protected SoundEvent getPlaceSound(BlockState state) {
        return state.getSoundGroup().getPlaceSound();
    }

    public @Nullable ItemPlacementContext getPlacementContext(ItemPlacementContext context) {
        return context;
    }

    private static void copyComponentsToBlockEntity(World world, BlockPos pos, ItemStack stack) {
        BlockEntity lv = world.getBlockEntity(pos);
        if (lv != null) {
            lv.readComponents(stack);
            lv.markDirty();
        }
    }

    protected boolean postPlacement(BlockPos pos, World world, @Nullable PlayerEntity player, ItemStack stack, BlockState state) {
        return BlockItem.writeNbtToBlockEntity(world, player, pos, stack);
    }

    protected @Nullable BlockState getPlacementState(ItemPlacementContext context) {
        BlockState lv = this.getBlock().getPlacementState(context);
        return lv != null && this.canPlace(context, lv) ? lv : null;
    }

    private BlockState placeFromNbt(BlockPos pos, World world, ItemStack stack, BlockState state) {
        BlockStateComponent lv = stack.getOrDefault(DataComponentTypes.BLOCK_STATE, BlockStateComponent.DEFAULT);
        if (lv.isEmpty()) {
            return state;
        }
        BlockState lv2 = lv.applyToState(state);
        if (lv2 != state) {
            world.setBlockState(pos, lv2, Block.NOTIFY_LISTENERS);
        }
        return lv2;
    }

    protected boolean canPlace(ItemPlacementContext context, BlockState state) {
        PlayerEntity lv = context.getPlayer();
        return (!this.checkStatePlacement() || state.canPlaceAt(context.getWorld(), context.getBlockPos())) && context.getWorld().canPlace(state, context.getBlockPos(), ShapeContext.ofPlacement(lv));
    }

    protected boolean checkStatePlacement() {
        return true;
    }

    protected boolean place(ItemPlacementContext context, BlockState state) {
        return context.getWorld().setBlockState(context.getBlockPos(), state, Block.NOTIFY_ALL_AND_REDRAW);
    }

    public static boolean writeNbtToBlockEntity(World world, @Nullable PlayerEntity player, BlockPos pos, ItemStack stack) {
        BlockEntity lv2;
        if (world.isClient()) {
            return false;
        }
        TypedEntityData<BlockEntityType<?>> lv = stack.get(DataComponentTypes.BLOCK_ENTITY_DATA);
        if (lv != null && (lv2 = world.getBlockEntity(pos)) != null) {
            BlockEntityType<?> lv3 = lv2.getType();
            if (lv3 != lv.getType()) {
                return false;
            }
            if (lv3.canPotentiallyExecuteCommands() && (player == null || !player.isCreativeLevelTwoOp())) {
                return false;
            }
            return lv.applyToBlockEntity(lv2, world.getRegistryManager());
        }
        return false;
    }

    @Override
    public boolean shouldShowOperatorBlockWarnings(ItemStack stack, @Nullable PlayerEntity player) {
        TypedEntityData<BlockEntityType<?>> lv;
        if (player != null && player.getPermissions().hasPermission(DefaultPermissions.GAMEMASTERS) && (lv = stack.get(DataComponentTypes.BLOCK_ENTITY_DATA)) != null) {
            return lv.getType().canPotentiallyExecuteCommands();
        }
        return false;
    }

    public Block getBlock() {
        return this.block;
    }

    public void appendBlocks(Map<Block, Item> map, Item item) {
        map.put(this.getBlock(), item);
    }

    @Override
    public boolean canBeNested() {
        return !(this.getBlock() instanceof ShulkerBoxBlock);
    }

    @Override
    public void onItemEntityDestroyed(ItemEntity entity) {
        ContainerComponent lv = entity.getStack().set(DataComponentTypes.CONTAINER, ContainerComponent.DEFAULT);
        if (lv != null) {
            ItemUsage.spawnItemContents(entity, lv.iterateNonEmptyCopy());
        }
    }

    public static void setBlockEntityData(ItemStack stack, BlockEntityType<?> type, NbtWriteView view) {
        view.remove("id");
        if (view.isEmpty()) {
            stack.remove(DataComponentTypes.BLOCK_ENTITY_DATA);
        } else {
            BlockEntity.writeId(view, type);
            stack.set(DataComponentTypes.BLOCK_ENTITY_DATA, TypedEntityData.create(type, view.getNbt()));
        }
    }

    @Override
    public FeatureSet getRequiredFeatures() {
        return this.getBlock().getRequiredFeatures();
    }
}

