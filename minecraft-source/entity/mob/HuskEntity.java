/*
 * External method calls:
 *   Lnet/minecraft/entity/mob/ZombieEntity;tryAttack(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/Entity;)Z
 *   Lnet/minecraft/entity/LivingEntity;addStatusEffect(Lnet/minecraft/entity/effect/StatusEffectInstance;Lnet/minecraft/entity/Entity;)Z
 *   Lnet/minecraft/server/world/ServerWorld;syncWorldEvent(Lnet/minecraft/entity/Entity;ILnet/minecraft/util/math/BlockPos;I)V
 *   Lnet/minecraft/entity/mob/ZombieEntity;initialize(Lnet/minecraft/world/ServerWorldAccess;Lnet/minecraft/world/LocalDifficulty;Lnet/minecraft/entity/SpawnReason;Lnet/minecraft/entity/EntityData;)Lnet/minecraft/entity/EntityData;
 *   Lnet/minecraft/entity/EntityType;create(Lnet/minecraft/world/World;Lnet/minecraft/entity/SpawnReason;)Lnet/minecraft/entity/Entity;
 *   Lnet/minecraft/entity/mob/CamelHuskEntity;initialize(Lnet/minecraft/world/ServerWorldAccess;Lnet/minecraft/world/LocalDifficulty;Lnet/minecraft/entity/SpawnReason;Lnet/minecraft/entity/EntityData;)Lnet/minecraft/entity/EntityData;
 *   Lnet/minecraft/world/ServerWorldAccess;spawnEntity(Lnet/minecraft/entity/Entity;)Z
 *   Lnet/minecraft/entity/mob/ParchedEntity;refreshPositionAndAngles(DDDFF)V
 *   Lnet/minecraft/entity/mob/ParchedEntity;initialize(Lnet/minecraft/world/ServerWorldAccess;Lnet/minecraft/world/LocalDifficulty;Lnet/minecraft/entity/SpawnReason;Lnet/minecraft/entity/EntityData;)Lnet/minecraft/entity/EntityData;
 *   Lnet/minecraft/entity/mob/ParchedEntity;startRiding(Lnet/minecraft/entity/Entity;ZZ)Z
 *   Lnet/minecraft/world/ServerWorldAccess;spawnEntityAndPassengers(Lnet/minecraft/entity/Entity;)V
 *
 * Internal private/static methods:
 *   Lnet/minecraft/entity/mob/HuskEntity;convertTo(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/EntityType;)V
 *   Lnet/minecraft/entity/mob/HuskEntity;equipStack(Lnet/minecraft/entity/EquipmentSlot;Lnet/minecraft/item/ItemStack;)V
 *   Lnet/minecraft/entity/mob/HuskEntity;startRiding(Lnet/minecraft/entity/Entity;ZZ)Z
 */
package net.minecraft.entity.mob;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.CamelHuskEntity;
import net.minecraft.entity.mob.ParchedEntity;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldEvents;
import org.jspecify.annotations.Nullable;

public class HuskEntity
extends ZombieEntity {
    public HuskEntity(EntityType<? extends HuskEntity> arg, World arg2) {
        super((EntityType<? extends ZombieEntity>)arg, arg2);
    }

    @Override
    protected boolean burnsInDaylight() {
        return false;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_HUSK_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.ENTITY_HUSK_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_HUSK_DEATH;
    }

    @Override
    protected SoundEvent getStepSound() {
        return SoundEvents.ENTITY_HUSK_STEP;
    }

    @Override
    public boolean tryAttack(ServerWorld world, Entity target) {
        boolean bl = super.tryAttack(world, target);
        if (bl && this.getMainHandStack().isEmpty() && target instanceof LivingEntity) {
            float f = world.getLocalDifficulty(this.getBlockPos()).getLocalDifficulty();
            ((LivingEntity)target).addStatusEffect(new StatusEffectInstance(StatusEffects.HUNGER, 140 * (int)f), this);
        }
        return bl;
    }

    @Override
    protected boolean canConvertInWater() {
        return true;
    }

    @Override
    protected void convertInWater(ServerWorld world) {
        this.convertTo(world, EntityType.ZOMBIE);
        if (!this.isSilent()) {
            world.syncWorldEvent(null, WorldEvents.HUSK_CONVERTS_TO_ZOMBIE, this.getBlockPos(), 0);
        }
    }

    @Override
    public @Nullable EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData) {
        Random lv = world.getRandom();
        entityData = super.initialize(world, difficulty, spawnReason, entityData);
        float f = difficulty.getClampedLocalDifficulty();
        if (spawnReason != SpawnReason.CONVERSION) {
            this.setCanPickUpLoot(lv.nextFloat() < 0.55f * f);
        }
        if (entityData != null) {
            entityData = new HuskData((ZombieEntity.ZombieData)entityData);
            boolean bl = ((HuskData)entityData).unnatural = spawnReason != SpawnReason.NATURAL;
        }
        if (entityData instanceof HuskData) {
            BlockPos lv3;
            HuskData lv2 = (HuskData)entityData;
            if (!lv2.unnatural && world.isSpaceEmpty(EntityType.CAMEL_HUSK.getSpawnBox((double)(lv3 = this.getBlockPos()).getX() + 0.5, lv3.getY(), (double)lv3.getZ() + 0.5))) {
                lv2.unnatural = true;
                if (lv.nextFloat() < 0.1f) {
                    this.equipStack(EquipmentSlot.MAINHAND, new ItemStack(Items.IRON_SPEAR));
                    CamelHuskEntity lv4 = EntityType.CAMEL_HUSK.create(this.getEntityWorld(), SpawnReason.NATURAL);
                    if (lv4 != null) {
                        lv4.setPosition(this.getX(), this.getY(), this.getZ());
                        lv4.initialize(world, difficulty, spawnReason, null);
                        this.startRiding(lv4, true, true);
                        world.spawnEntity(lv4);
                        ParchedEntity lv5 = EntityType.PARCHED.create(this.getEntityWorld(), SpawnReason.NATURAL);
                        if (lv5 != null) {
                            lv5.refreshPositionAndAngles(this.getX(), this.getY(), this.getZ(), this.getYaw(), 0.0f);
                            lv5.initialize(world, difficulty, spawnReason, null);
                            lv5.startRiding(lv4, false, false);
                            world.spawnEntityAndPassengers(lv5);
                        }
                    }
                }
            }
        }
        return entityData;
    }

    public static class HuskData
    extends ZombieEntity.ZombieData {
        public boolean unnatural = false;

        public HuskData(ZombieEntity.ZombieData data) {
            super(data.baby, data.tryChickenJockey);
        }
    }
}

