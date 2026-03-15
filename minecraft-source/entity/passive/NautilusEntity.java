/*
 * External method calls:
 *   Lnet/minecraft/entity/passive/NautilusBrain;createProfile()Lnet/minecraft/entity/ai/brain/Brain$Profile;
 *   Lnet/minecraft/entity/ai/brain/Brain$Profile;deserialize(Lcom/mojang/serialization/Dynamic;)Lnet/minecraft/entity/ai/brain/Brain;
 *   Lnet/minecraft/entity/passive/NautilusBrain;create(Lnet/minecraft/entity/ai/brain/Brain;)Lnet/minecraft/entity/ai/brain/Brain;
 *   Lnet/minecraft/entity/EntityType;create(Lnet/minecraft/world/World;Lnet/minecraft/entity/SpawnReason;)Lnet/minecraft/entity/Entity;
 *   Lnet/minecraft/util/profiler/Profiler;push(Ljava/lang/String;)V
 *   Lnet/minecraft/entity/ai/brain/Brain;tick(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/LivingEntity;)V
 *   Lnet/minecraft/entity/passive/NautilusBrain;updateActivities(Lnet/minecraft/entity/passive/NautilusEntity;)V
 *   Lnet/minecraft/entity/passive/AbstractNautilusEntity;mobTick(Lnet/minecraft/server/world/ServerWorld;)V
 *   Lnet/minecraft/entity/damage/DamageSources;dryOut()Lnet/minecraft/entity/damage/DamageSource;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/entity/passive/NautilusEntity;createBrainProfile()Lnet/minecraft/entity/ai/brain/Brain$Profile;
 *   Lnet/minecraft/entity/passive/NautilusEntity;playSound(Lnet/minecraft/sound/SoundEvent;)V
 *   Lnet/minecraft/entity/passive/NautilusEntity;damage(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/damage/DamageSource;F)Z
 *   Lnet/minecraft/entity/passive/NautilusEntity;tickAir(Lnet/minecraft/server/world/ServerWorld;I)V
 *   Lnet/minecraft/entity/passive/NautilusEntity;createChild(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/passive/PassiveEntity;)Lnet/minecraft/entity/passive/NautilusEntity;
 */
package net.minecraft.entity.passive;

import com.mojang.serialization.Dynamic;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.passive.AbstractNautilusEntity;
import net.minecraft.entity.passive.NautilusBrain;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.util.profiler.Profilers;
import net.minecraft.world.World;
import org.jspecify.annotations.Nullable;

public class NautilusEntity
extends AbstractNautilusEntity {
    private static final int MAX_AIR = 300;

    public NautilusEntity(EntityType<? extends NautilusEntity> arg, World arg2) {
        super((EntityType<? extends AbstractNautilusEntity>)arg, arg2);
    }

    protected Brain.Profile<NautilusEntity> createBrainProfile() {
        return NautilusBrain.createProfile();
    }

    @Override
    protected Brain<?> deserializeBrain(Dynamic<?> dynamic) {
        return NautilusBrain.create(this.createBrainProfile().deserialize(dynamic));
    }

    public Brain<NautilusEntity> getBrain() {
        return super.getBrain();
    }

    @Override
    public @Nullable NautilusEntity createChild(ServerWorld arg, PassiveEntity arg2) {
        NautilusEntity lv = EntityType.NAUTILUS.create(arg, SpawnReason.BREEDING);
        if (lv != null && this.isTamed()) {
            lv.setOwner(this.getOwnerReference());
            lv.setTamed(true, true);
        }
        return lv;
    }

    @Override
    protected void mobTick(ServerWorld world) {
        Profiler lv = Profilers.get();
        lv.push("nautilusBrain");
        this.getBrain().tick(world, this);
        lv.pop();
        lv.push("nautilusActivityUpdate");
        NautilusBrain.updateActivities(this);
        lv.pop();
        super.mobTick(world);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        if (this.isBaby()) {
            return this.isSubmergedInWater() ? SoundEvents.ENTITY_BABY_NAUTILUS_AMBIENT : SoundEvents.ENTITY_BABY_NAUTILUS_AMBIENT_LAND;
        }
        return this.isSubmergedInWater() ? SoundEvents.ENTITY_NAUTILUS_AMBIENT : SoundEvents.ENTITY_NAUTILUS_AMBIENT_LAND;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        if (this.isBaby()) {
            return this.isSubmergedInWater() ? SoundEvents.ENTITY_BABY_NAUTILUS_HURT : SoundEvents.ENTITY_BABY_NAUTILUS_HURT_LAND;
        }
        return this.isSubmergedInWater() ? SoundEvents.ENTITY_NAUTILUS_HURT : SoundEvents.ENTITY_NAUTILUS_HURT_LAND;
    }

    @Override
    protected SoundEvent getDeathSound() {
        if (this.isBaby()) {
            return this.isSubmergedInWater() ? SoundEvents.ENTITY_BABY_NAUTILUS_DEATH : SoundEvents.ENTITY_BABY_NAUTILUS_DEATH_LAND;
        }
        return this.isSubmergedInWater() ? SoundEvents.ENTITY_NAUTILUS_DEATH : SoundEvents.ENTITY_NAUTILUS_DEATH_LAND;
    }

    @Override
    protected SoundEvent getDashSound() {
        return this.isSubmergedInWater() ? SoundEvents.ENTITY_NAUTILUS_DASH : SoundEvents.ENTITY_NAUTILUS_DASH_LAND;
    }

    @Override
    protected SoundEvent getDashReadySound() {
        return this.isSubmergedInWater() ? SoundEvents.ENTITY_NAUTILUS_DASH_READY : SoundEvents.ENTITY_NAUTILUS_DASH_READY_LAND;
    }

    @Override
    protected void playEatSound() {
        SoundEvent lv = this.isBaby() ? SoundEvents.ENTITY_BABY_NAUTILUS_EAT : SoundEvents.ENTITY_NAUTILUS_EAT;
        this.playSound(lv);
    }

    @Override
    protected SoundEvent getSwimSound() {
        return this.isBaby() ? SoundEvents.ENTITY_BABY_NAUTILUS_SWIM : SoundEvents.ENTITY_NAUTILUS_SWIM;
    }

    @Override
    public int getMaxAir() {
        return 300;
    }

    protected void tickAir(ServerWorld world, int lastAir) {
        if (this.isAlive() && !this.isTouchingWater()) {
            this.setAir(lastAir - 1);
            if (this.getAir() <= -20) {
                this.setAir(0);
                this.damage(world, this.getDamageSources().dryOut(), 2.0f);
            }
        } else {
            this.setAir(300);
        }
    }

    @Override
    public void baseTick() {
        World world;
        int i = this.getAir();
        super.baseTick();
        if (!this.isAiDisabled() && (world = this.getEntityWorld()) instanceof ServerWorld) {
            ServerWorld lv = (ServerWorld)world;
            this.tickAir(lv, i);
        }
    }

    @Override
    public boolean canBeLeashed() {
        return !this.hasAttackTarget();
    }

    @Override
    public /* synthetic */ @Nullable PassiveEntity createChild(ServerWorld world, PassiveEntity entity) {
        return this.createChild(world, entity);
    }
}

