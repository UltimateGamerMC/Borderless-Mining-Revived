/*
 * External method calls:
 *   Lnet/minecraft/entity/projectile/ProjectileEntity;spawnWithVelocity(Lnet/minecraft/entity/projectile/ProjectileEntity$ProjectileCreator;Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/item/ItemStack;Lnet/minecraft/entity/LivingEntity;FFF)Lnet/minecraft/entity/projectile/ProjectileEntity;
 *   Lnet/minecraft/entity/player/PlayerEntity;incrementStat(Lnet/minecraft/stat/Stat;)V
 *   Lnet/minecraft/item/ItemStack;decrementUnlessCreative(ILnet/minecraft/entity/LivingEntity;)V
 *   Lnet/minecraft/item/ProjectileItem$Settings;builder()Lnet/minecraft/item/ProjectileItem$Settings$Builder;
 *   Lnet/minecraft/item/ProjectileItem$Settings$Builder;uncertainty(F)Lnet/minecraft/item/ProjectileItem$Settings$Builder;
 *   Lnet/minecraft/item/ProjectileItem$Settings$Builder;power(F)Lnet/minecraft/item/ProjectileItem$Settings$Builder;
 *   Lnet/minecraft/item/ProjectileItem$Settings$Builder;build()Lnet/minecraft/item/ProjectileItem$Settings;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/item/ThrowablePotionItem;createEntity(Lnet/minecraft/world/World;Lnet/minecraft/util/math/Position;Lnet/minecraft/item/ItemStack;)Lnet/minecraft/entity/projectile/thrown/PotionEntity;
 */
package net.minecraft.item;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.entity.projectile.thrown.PotionEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.PotionItem;
import net.minecraft.item.ProjectileItem;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.stat.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Position;
import net.minecraft.world.World;

public abstract class ThrowablePotionItem
extends PotionItem
implements ProjectileItem {
    public static float POWER = 0.5f;

    public ThrowablePotionItem(Item.Settings arg) {
        super(arg);
    }

    @Override
    public ActionResult use(World world, PlayerEntity user, Hand hand) {
        ItemStack lv = user.getStackInHand(hand);
        if (world instanceof ServerWorld) {
            ServerWorld lv2 = (ServerWorld)world;
            ProjectileEntity.spawnWithVelocity(this::createEntity, lv2, lv, user, -20.0f, POWER, 1.0f);
        }
        user.incrementStat(Stats.USED.getOrCreateStat(this));
        lv.decrementUnlessCreative(1, user);
        return ActionResult.SUCCESS;
    }

    protected abstract PotionEntity createEntity(ServerWorld var1, LivingEntity var2, ItemStack var3);

    protected abstract PotionEntity createEntity(World var1, Position var2, ItemStack var3);

    @Override
    public ProjectileEntity createEntity(World world, Position pos, ItemStack stack, Direction direction) {
        return this.createEntity(world, pos, stack);
    }

    @Override
    public ProjectileItem.Settings getProjectileSettings() {
        return ProjectileItem.Settings.builder().uncertainty(ProjectileItem.Settings.DEFAULT.uncertainty() * 0.5f).power(ProjectileItem.Settings.DEFAULT.power() * 1.25f).build();
    }
}

