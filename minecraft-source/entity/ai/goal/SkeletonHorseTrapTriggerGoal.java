/*
 * External method calls:
 *   Lnet/minecraft/entity/EntityType;create(Lnet/minecraft/world/World;Lnet/minecraft/entity/SpawnReason;)Lnet/minecraft/entity/Entity;
 *   Lnet/minecraft/entity/LightningEntity;refreshPositionAfterTeleport(DDD)V
 *   Lnet/minecraft/server/world/ServerWorld;spawnEntity(Lnet/minecraft/entity/Entity;)Z
 *   Lnet/minecraft/entity/mob/SkeletonEntity;startRiding(Lnet/minecraft/entity/Entity;)Z
 *   Lnet/minecraft/server/world/ServerWorld;spawnEntityAndPassengers(Lnet/minecraft/entity/Entity;)V
 *   Lnet/minecraft/entity/passive/AbstractHorseEntity;addVelocity(DDD)V
 *   Lnet/minecraft/entity/mob/SkeletonHorseEntity;initialize(Lnet/minecraft/world/ServerWorldAccess;Lnet/minecraft/world/LocalDifficulty;Lnet/minecraft/entity/SpawnReason;Lnet/minecraft/entity/EntityData;)Lnet/minecraft/entity/EntityData;
 *   Lnet/minecraft/entity/mob/SkeletonEntity;initialize(Lnet/minecraft/world/ServerWorldAccess;Lnet/minecraft/world/LocalDifficulty;Lnet/minecraft/entity/SpawnReason;Lnet/minecraft/entity/EntityData;)Lnet/minecraft/entity/EntityData;
 *   Lnet/minecraft/entity/mob/SkeletonEntity;equipStack(Lnet/minecraft/entity/EquipmentSlot;Lnet/minecraft/item/ItemStack;)V
 *   Lnet/minecraft/enchantment/EnchantmentHelper;applyEnchantmentProvider(Lnet/minecraft/item/ItemStack;Lnet/minecraft/registry/DynamicRegistryManager;Lnet/minecraft/registry/RegistryKey;Lnet/minecraft/world/LocalDifficulty;Lnet/minecraft/util/math/random/Random;)V
 *
 * Internal private/static methods:
 *   Lnet/minecraft/entity/ai/goal/SkeletonHorseTrapTriggerGoal;enchantEquipment(Lnet/minecraft/entity/mob/SkeletonEntity;Lnet/minecraft/entity/EquipmentSlot;Lnet/minecraft/world/LocalDifficulty;)V
 */
package net.minecraft.entity.ai.goal;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.provider.EnchantmentProviders;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LightningEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.mob.SkeletonEntity;
import net.minecraft.entity.mob.SkeletonHorseEntity;
import net.minecraft.entity.passive.AbstractHorseEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.LocalDifficulty;
import org.jspecify.annotations.Nullable;

public class SkeletonHorseTrapTriggerGoal
extends Goal {
    private final SkeletonHorseEntity skeletonHorse;

    public SkeletonHorseTrapTriggerGoal(SkeletonHorseEntity skeletonHorse) {
        this.skeletonHorse = skeletonHorse;
    }

    @Override
    public boolean canStart() {
        return this.skeletonHorse.getEntityWorld().isPlayerInRange(this.skeletonHorse.getX(), this.skeletonHorse.getY(), this.skeletonHorse.getZ(), 10.0);
    }

    @Override
    public void tick() {
        ServerWorld lv = (ServerWorld)this.skeletonHorse.getEntityWorld();
        LocalDifficulty lv2 = lv.getLocalDifficulty(this.skeletonHorse.getBlockPos());
        this.skeletonHorse.setTrapped(false);
        this.skeletonHorse.setTame(true);
        this.skeletonHorse.setBreedingAge(0);
        LightningEntity lv3 = EntityType.LIGHTNING_BOLT.create(lv, SpawnReason.TRIGGERED);
        if (lv3 == null) {
            return;
        }
        lv3.refreshPositionAfterTeleport(this.skeletonHorse.getX(), this.skeletonHorse.getY(), this.skeletonHorse.getZ());
        lv3.setCosmetic(true);
        lv.spawnEntity(lv3);
        SkeletonEntity lv4 = this.getSkeleton(lv2, this.skeletonHorse);
        if (lv4 == null) {
            return;
        }
        lv4.startRiding(this.skeletonHorse);
        lv.spawnEntityAndPassengers(lv4);
        for (int i = 0; i < 3; ++i) {
            SkeletonEntity lv6;
            AbstractHorseEntity lv5 = this.getHorse(lv2);
            if (lv5 == null || (lv6 = this.getSkeleton(lv2, lv5)) == null) continue;
            lv6.startRiding(lv5);
            lv5.addVelocity(this.skeletonHorse.getRandom().nextTriangular(0.0, 1.1485), 0.0, this.skeletonHorse.getRandom().nextTriangular(0.0, 1.1485));
            lv.spawnEntityAndPassengers(lv5);
        }
    }

    private @Nullable AbstractHorseEntity getHorse(LocalDifficulty localDifficulty) {
        SkeletonHorseEntity lv = EntityType.SKELETON_HORSE.create(this.skeletonHorse.getEntityWorld(), SpawnReason.TRIGGERED);
        if (lv != null) {
            lv.initialize((ServerWorld)this.skeletonHorse.getEntityWorld(), localDifficulty, SpawnReason.TRIGGERED, null);
            lv.setPosition(this.skeletonHorse.getX(), this.skeletonHorse.getY(), this.skeletonHorse.getZ());
            lv.timeUntilRegen = 60;
            lv.setPersistent();
            lv.setTame(true);
            lv.setBreedingAge(0);
        }
        return lv;
    }

    private @Nullable SkeletonEntity getSkeleton(LocalDifficulty localDifficulty, AbstractHorseEntity vehicle) {
        SkeletonEntity lv = EntityType.SKELETON.create(vehicle.getEntityWorld(), SpawnReason.TRIGGERED);
        if (lv != null) {
            lv.initialize((ServerWorld)vehicle.getEntityWorld(), localDifficulty, SpawnReason.TRIGGERED, null);
            lv.setPosition(vehicle.getX(), vehicle.getY(), vehicle.getZ());
            lv.timeUntilRegen = 60;
            lv.setPersistent();
            if (lv.getEquippedStack(EquipmentSlot.HEAD).isEmpty()) {
                lv.equipStack(EquipmentSlot.HEAD, new ItemStack(Items.IRON_HELMET));
            }
            this.enchantEquipment(lv, EquipmentSlot.MAINHAND, localDifficulty);
            this.enchantEquipment(lv, EquipmentSlot.HEAD, localDifficulty);
        }
        return lv;
    }

    private void enchantEquipment(SkeletonEntity rider, EquipmentSlot slot, LocalDifficulty localDifficulty) {
        ItemStack lv = rider.getEquippedStack(slot);
        lv.set(DataComponentTypes.ENCHANTMENTS, ItemEnchantmentsComponent.DEFAULT);
        EnchantmentHelper.applyEnchantmentProvider(lv, rider.getEntityWorld().getRegistryManager(), EnchantmentProviders.MOB_SPAWN_EQUIPMENT, localDifficulty, rider.getRandom());
        rider.equipStack(slot, lv);
    }
}

