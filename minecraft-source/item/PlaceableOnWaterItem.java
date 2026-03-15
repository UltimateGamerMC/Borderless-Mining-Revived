/*
 * External method calls:
 *   Lnet/minecraft/util/hit/BlockHitResult;withBlockPos(Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/util/hit/BlockHitResult;
 *   Lnet/minecraft/item/BlockItem;useOnBlock(Lnet/minecraft/item/ItemUsageContext;)Lnet/minecraft/util/ActionResult;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/item/PlaceableOnWaterItem;raycast(Lnet/minecraft/world/World;Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/world/RaycastContext$FluidHandling;)Lnet/minecraft/util/hit/BlockHitResult;
 */
package net.minecraft.item;

import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;

public class PlaceableOnWaterItem
extends BlockItem {
    public PlaceableOnWaterItem(Block arg, Item.Settings arg2) {
        super(arg, arg2);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        return ActionResult.PASS;
    }

    @Override
    public ActionResult use(World world, PlayerEntity user, Hand hand) {
        BlockHitResult lv = PlaceableOnWaterItem.raycast(world, user, RaycastContext.FluidHandling.SOURCE_ONLY);
        BlockHitResult lv2 = lv.withBlockPos(lv.getBlockPos().up());
        return super.useOnBlock(new ItemUsageContext(user, hand, lv2));
    }
}

