/*
 * External method calls:
 *   Lnet/minecraft/item/ProjectileItem$Settings;positionFunction()Lnet/minecraft/item/ProjectileItem$PositionFunction;
 *   Lnet/minecraft/item/ProjectileItem;createEntity(Lnet/minecraft/world/World;Lnet/minecraft/util/math/Position;Lnet/minecraft/item/ItemStack;Lnet/minecraft/util/math/Direction;)Lnet/minecraft/entity/projectile/ProjectileEntity;
 *   Lnet/minecraft/entity/projectile/ProjectileEntity;spawnWithVelocity(Lnet/minecraft/entity/projectile/ProjectileEntity;Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/item/ItemStack;DDDFF)Lnet/minecraft/entity/projectile/ProjectileEntity;
 *   Lnet/minecraft/item/ItemStack;decrement(I)V
 *   Lnet/minecraft/item/ProjectileItem$Settings;overrideDispenseEvent()Ljava/util/OptionalInt;
 *   Lnet/minecraft/server/world/ServerWorld;syncWorldEvent(ILnet/minecraft/util/math/BlockPos;I)V
 */
package net.minecraft.block.dispenser;

import net.minecraft.block.DispenserBlock;
import net.minecraft.block.dispenser.ItemDispenserBehavior;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ProjectileItem;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPointer;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Position;

public class ProjectileDispenserBehavior
extends ItemDispenserBehavior {
    private final ProjectileItem projectile;
    private final ProjectileItem.Settings projectileSettings;

    public ProjectileDispenserBehavior(Item item) {
        if (!(item instanceof ProjectileItem)) {
            throw new IllegalArgumentException(String.valueOf(item) + " not instance of " + ProjectileItem.class.getSimpleName());
        }
        ProjectileItem lv = (ProjectileItem)((Object)item);
        this.projectile = lv;
        this.projectileSettings = lv.getProjectileSettings();
    }

    @Override
    public ItemStack dispenseSilently(BlockPointer pointer, ItemStack stack) {
        ServerWorld lv = pointer.world();
        Direction lv2 = pointer.state().get(DispenserBlock.FACING);
        Position lv3 = this.projectileSettings.positionFunction().getDispensePosition(pointer, lv2);
        ProjectileEntity.spawnWithVelocity(this.projectile.createEntity(lv, lv3, stack, lv2), lv, stack, lv2.getOffsetX(), lv2.getOffsetY(), lv2.getOffsetZ(), this.projectileSettings.power(), this.projectileSettings.uncertainty());
        stack.decrement(1);
        return stack;
    }

    @Override
    protected void playSound(BlockPointer pointer) {
        pointer.world().syncWorldEvent(this.projectileSettings.overrideDispenseEvent().orElse(1002), pointer.pos(), 0);
    }
}

