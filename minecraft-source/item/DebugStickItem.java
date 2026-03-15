/*
 * External method calls:
 *   Lnet/minecraft/text/Text;translatable(Ljava/lang/String;[Ljava/lang/Object;)Lnet/minecraft/text/MutableText;
 *   Lnet/minecraft/component/type/DebugStickStateComponent;properties()Ljava/util/Map;
 *   Lnet/minecraft/component/type/DebugStickStateComponent;with(Lnet/minecraft/registry/entry/RegistryEntry;Lnet/minecraft/state/property/Property;)Lnet/minecraft/component/type/DebugStickStateComponent;
 *   Lnet/minecraft/block/BlockState;with(Lnet/minecraft/state/property/Property;Ljava/lang/Comparable;)Ljava/lang/Object;
 *   Lnet/minecraft/util/Util;previous(Ljava/lang/Iterable;Ljava/lang/Object;)Ljava/lang/Object;
 *   Lnet/minecraft/util/Util;next(Ljava/lang/Iterable;Ljava/lang/Object;)Ljava/lang/Object;
 *   Lnet/minecraft/server/network/ServerPlayerEntity;sendMessageToClient(Lnet/minecraft/text/Text;Z)V
 *   Lnet/minecraft/state/property/Property;name(Ljava/lang/Comparable;)Ljava/lang/String;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/item/DebugStickItem;use(Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/block/BlockState;Lnet/minecraft/world/WorldAccess;Lnet/minecraft/util/math/BlockPos;ZLnet/minecraft/item/ItemStack;)Z
 *   Lnet/minecraft/item/DebugStickItem;sendMessage(Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/text/Text;)V
 *   Lnet/minecraft/item/DebugStickItem;cycle(Lnet/minecraft/block/BlockState;Lnet/minecraft/state/property/Property;Z)Lnet/minecraft/block/BlockState;
 *   Lnet/minecraft/item/DebugStickItem;cycle(Ljava/lang/Iterable;Ljava/lang/Object;Z)Ljava/lang/Object;
 */
package net.minecraft.item;

import java.util.Collection;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.DebugStickStateComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Property;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jspecify.annotations.Nullable;

public class DebugStickItem
extends Item {
    public DebugStickItem(Item.Settings arg) {
        super(arg);
    }

    @Override
    public boolean canMine(ItemStack stack, BlockState state, World world, BlockPos pos, LivingEntity user) {
        if (!world.isClient() && user instanceof PlayerEntity) {
            PlayerEntity lv = (PlayerEntity)user;
            this.use(lv, state, world, pos, false, stack);
        }
        return false;
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        BlockPos lv3;
        PlayerEntity lv = context.getPlayer();
        World lv2 = context.getWorld();
        if (!lv2.isClient() && lv != null && !this.use(lv, lv2.getBlockState(lv3 = context.getBlockPos()), lv2, lv3, true, context.getStack())) {
            return ActionResult.FAIL;
        }
        return ActionResult.SUCCESS;
    }

    private boolean use(PlayerEntity player, BlockState state, WorldAccess world, BlockPos pos, boolean update, ItemStack stack) {
        if (!player.isCreativeLevelTwoOp()) {
            return false;
        }
        RegistryEntry<Block> lv = state.getRegistryEntry();
        StateManager<Block, BlockState> lv2 = lv.value().getStateManager();
        Collection<Property<?>> collection = lv2.getProperties();
        if (collection.isEmpty()) {
            DebugStickItem.sendMessage(player, Text.translatable(this.translationKey + ".empty", lv.getIdAsString()));
            return false;
        }
        DebugStickStateComponent lv3 = stack.get(DataComponentTypes.DEBUG_STICK_STATE);
        if (lv3 == null) {
            return false;
        }
        Property<?> lv4 = lv3.properties().get(lv);
        if (update) {
            if (lv4 == null) {
                lv4 = collection.iterator().next();
            }
            BlockState lv5 = DebugStickItem.cycle(state, lv4, player.shouldCancelInteraction());
            world.setBlockState(pos, lv5, Block.NOTIFY_LISTENERS | Block.FORCE_STATE);
            DebugStickItem.sendMessage(player, Text.translatable(this.translationKey + ".update", lv4.getName(), DebugStickItem.getValueString(lv5, lv4)));
        } else {
            lv4 = DebugStickItem.cycle(collection, lv4, player.shouldCancelInteraction());
            stack.set(DataComponentTypes.DEBUG_STICK_STATE, lv3.with(lv, lv4));
            DebugStickItem.sendMessage(player, Text.translatable(this.translationKey + ".select", lv4.getName(), DebugStickItem.getValueString(state, lv4)));
        }
        return true;
    }

    private static <T extends Comparable<T>> BlockState cycle(BlockState state, Property<T> property, boolean inverse) {
        return (BlockState)state.with(property, (Comparable)DebugStickItem.cycle(property.getValues(), state.get(property), inverse));
    }

    private static <T> T cycle(Iterable<T> elements, @Nullable T current, boolean inverse) {
        return inverse ? Util.previous(elements, current) : Util.next(elements, current);
    }

    private static void sendMessage(PlayerEntity player, Text message) {
        ((ServerPlayerEntity)player).sendMessageToClient(message, true);
    }

    private static <T extends Comparable<T>> String getValueString(BlockState state, Property<T> property) {
        return property.name(state.get(property));
    }
}

