/*
 * External method calls:
 *   Lnet/minecraft/entity/player/PlayerInventory;insertStack(Lnet/minecraft/item/ItemStack;)Z
 *   Lnet/minecraft/item/ItemStack;decrementUnlessCreative(ILnet/minecraft/entity/LivingEntity;)V
 *   Lnet/minecraft/entity/player/PlayerEntity;dropItem(Lnet/minecraft/item/ItemStack;Z)Lnet/minecraft/entity/ItemEntity;
 *   Lnet/minecraft/world/World;spawnEntity(Lnet/minecraft/entity/Entity;)Z
 *
 * Internal private/static methods:
 *   Lnet/minecraft/item/ItemUsage;exchangeStack(Lnet/minecraft/item/ItemStack;Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/item/ItemStack;Z)Lnet/minecraft/item/ItemStack;
 */
package net.minecraft.item;

import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public class ItemUsage {
    public static ActionResult consumeHeldItem(World world, PlayerEntity player, Hand hand) {
        player.setCurrentHand(hand);
        return ActionResult.CONSUME;
    }

    public static ItemStack exchangeStack(ItemStack inputStack, PlayerEntity player, ItemStack outputStack, boolean creativeOverride) {
        boolean bl2 = player.isInCreativeMode();
        if (creativeOverride && bl2) {
            if (!player.getInventory().contains(outputStack)) {
                player.getInventory().insertStack(outputStack);
            }
            return inputStack;
        }
        inputStack.decrementUnlessCreative(1, player);
        if (inputStack.isEmpty()) {
            return outputStack;
        }
        if (!player.getInventory().insertStack(outputStack)) {
            player.dropItem(outputStack, false);
        }
        return inputStack;
    }

    public static ItemStack exchangeStack(ItemStack inputStack, PlayerEntity player, ItemStack outputStack) {
        return ItemUsage.exchangeStack(inputStack, player, outputStack, true);
    }

    public static void spawnItemContents(ItemEntity itemEntity, Iterable<ItemStack> contents) {
        World lv = itemEntity.getEntityWorld();
        if (lv.isClient()) {
            return;
        }
        contents.forEach(stack -> lv.spawnEntity(new ItemEntity(lv, itemEntity.getX(), itemEntity.getY(), itemEntity.getZ(), (ItemStack)stack)));
    }
}

