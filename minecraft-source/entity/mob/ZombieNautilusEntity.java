/*
 * External method calls:
 *   Lnet/minecraft/entity/passive/AbstractNautilusEntity;createNautilusAttributes()Lnet/minecraft/entity/attribute/DefaultAttributeContainer$Builder;
 *   Lnet/minecraft/entity/mob/ZombieNautilusBrain;createProfile()Lnet/minecraft/entity/ai/brain/Brain$Profile;
 *   Lnet/minecraft/entity/ai/brain/Brain$Profile;deserialize(Lcom/mojang/serialization/Dynamic;)Lnet/minecraft/entity/ai/brain/Brain;
 *   Lnet/minecraft/entity/mob/ZombieNautilusBrain;create(Lnet/minecraft/entity/ai/brain/Brain;)Lnet/minecraft/entity/ai/brain/Brain;
 *   Lnet/minecraft/util/profiler/Profiler;push(Ljava/lang/String;)V
 *   Lnet/minecraft/entity/ai/brain/Brain;tick(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/LivingEntity;)V
 *   Lnet/minecraft/entity/mob/ZombieNautilusBrain;updateActivities(Lnet/minecraft/entity/mob/ZombieNautilusEntity;)V
 *   Lnet/minecraft/entity/passive/AbstractNautilusEntity;mobTick(Lnet/minecraft/server/world/ServerWorld;)V
 *   Lnet/minecraft/entity/passive/AbstractNautilusEntity;initDataTracker(Lnet/minecraft/entity/data/DataTracker$Builder;)V
 *   Lnet/minecraft/entity/passive/AbstractNautilusEntity;readCustomData(Lnet/minecraft/storage/ReadView;)V
 *   Lnet/minecraft/entity/Variants;fromData(Lnet/minecraft/storage/ReadView;Lnet/minecraft/registry/RegistryKey;)Ljava/util/Optional;
 *   Lnet/minecraft/entity/passive/AbstractNautilusEntity;writeCustomData(Lnet/minecraft/storage/WriteView;)V
 *   Lnet/minecraft/entity/Variants;writeData(Lnet/minecraft/storage/WriteView;Lnet/minecraft/registry/entry/RegistryEntry;)V
 *   Lnet/minecraft/entity/passive/AbstractNautilusEntity;copyComponentsFrom(Lnet/minecraft/component/ComponentsAccess;)V
 *   Lnet/minecraft/registry/entry/LazyRegistryEntryReference;resolveEntry(Lnet/minecraft/registry/RegistryWrapper$WrapperLookup;)Ljava/util/Optional;
 *   Lnet/minecraft/entity/spawn/SpawnContext;of(Lnet/minecraft/world/ServerWorldAccess;Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/entity/spawn/SpawnContext;
 *   Lnet/minecraft/entity/Variants;select(Lnet/minecraft/entity/spawn/SpawnContext;Lnet/minecraft/registry/RegistryKey;)Ljava/util/Optional;
 *   Lnet/minecraft/entity/passive/AbstractNautilusEntity;initialize(Lnet/minecraft/world/ServerWorldAccess;Lnet/minecraft/world/LocalDifficulty;Lnet/minecraft/entity/SpawnReason;Lnet/minecraft/entity/EntityData;)Lnet/minecraft/entity/EntityData;
 *   Lnet/minecraft/entity/data/DataTracker;registerData(Ljava/lang/Class;Lnet/minecraft/entity/data/TrackedDataHandler;)Lnet/minecraft/entity/data/TrackedData;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/entity/mob/ZombieNautilusEntity;createBrainProfile()Lnet/minecraft/entity/ai/brain/Brain$Profile;
 *   Lnet/minecraft/entity/mob/ZombieNautilusEntity;playSound(Lnet/minecraft/sound/SoundEvent;)V
 *   Lnet/minecraft/entity/mob/ZombieNautilusEntity;castComponentValue(Lnet/minecraft/component/ComponentType;Ljava/lang/Object;)Ljava/lang/Object;
 *   Lnet/minecraft/entity/mob/ZombieNautilusEntity;copyComponentFrom(Lnet/minecraft/component/ComponentsAccess;Lnet/minecraft/component/ComponentType;)Z
 *   Lnet/minecraft/entity/mob/ZombieNautilusEntity;createChild(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/passive/PassiveEntity;)Lnet/minecraft/entity/mob/ZombieNautilusEntity;
 */
package net.minecraft.entity.mob;

import com.mojang.serialization.Dynamic;
import java.util.Optional;
import net.minecraft.component.ComponentType;
import net.minecraft.component.ComponentsAccess;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.EntityData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.Variants;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.ZombieNautilusBrain;
import net.minecraft.entity.mob.ZombieNautilusVariant;
import net.minecraft.entity.mob.ZombieNautilusVariants;
import net.minecraft.entity.passive.AbstractNautilusEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.spawn.SpawnContext;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.LazyRegistryEntryReference;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.util.profiler.Profilers;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import org.jspecify.annotations.Nullable;

public class ZombieNautilusEntity
extends AbstractNautilusEntity {
    private static final TrackedData<RegistryEntry<ZombieNautilusVariant>> VARIANT = DataTracker.registerData(ZombieNautilusEntity.class, TrackedDataHandlerRegistry.ZOMBIE_NAUTILUS_VARIANT);

    public ZombieNautilusEntity(EntityType<? extends ZombieNautilusEntity> arg, World arg2) {
        super((EntityType<? extends AbstractNautilusEntity>)arg, arg2);
    }

    public static DefaultAttributeContainer.Builder createZombieNautilusAttributes() {
        return AbstractNautilusEntity.createNautilusAttributes().add(EntityAttributes.MOVEMENT_SPEED, 1.1f);
    }

    @Override
    public @Nullable ZombieNautilusEntity createChild(ServerWorld arg, PassiveEntity arg2) {
        return null;
    }

    @Override
    protected EquipmentSlot getDaylightProtectionSlot() {
        return EquipmentSlot.BODY;
    }

    protected Brain.Profile<ZombieNautilusEntity> createBrainProfile() {
        return ZombieNautilusBrain.createProfile();
    }

    @Override
    protected Brain<?> deserializeBrain(Dynamic<?> dynamic) {
        return ZombieNautilusBrain.create(this.createBrainProfile().deserialize(dynamic));
    }

    public Brain<ZombieNautilusEntity> getBrain() {
        return super.getBrain();
    }

    @Override
    protected void mobTick(ServerWorld world) {
        Profiler lv = Profilers.get();
        lv.push("zombieNautilusBrain");
        this.getBrain().tick(world, this);
        lv.pop();
        lv.push("zombieNautilusActivityUpdate");
        ZombieNautilusBrain.updateActivities(this);
        lv.pop();
        super.mobTick(world);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return this.isSubmergedInWater() ? SoundEvents.ENTITY_ZOMBIE_NAUTILUS_AMBIENT : SoundEvents.ENTITY_ZOMBIE_NAUTILUS_AMBIENT_LAND;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return this.isSubmergedInWater() ? SoundEvents.ENTITY_ZOMBIE_NAUTILUS_HURT : SoundEvents.ENTITY_ZOMBIE_NAUTILUS_HURT_LAND;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return this.isSubmergedInWater() ? SoundEvents.ENTITY_ZOMBIE_NAUTILUS_DEATH : SoundEvents.ENTITY_ZOMBIE_NAUTILUS_DEATH_LAND;
    }

    @Override
    protected SoundEvent getDashSound() {
        return this.isSubmergedInWater() ? SoundEvents.ENTITY_ZOMBIE_NAUTILUS_DASH : SoundEvents.ENTITY_ZOMBIE_NAUTILUS_DASH_LAND;
    }

    @Override
    protected SoundEvent getDashReadySound() {
        return this.isSubmergedInWater() ? SoundEvents.ENTITY_ZOMBIE_NAUTILUS_DASH_READY : SoundEvents.ENTITY_ZOMBIE_NAUTILUS_DASH_READY_LAND;
    }

    @Override
    protected void playEatSound() {
        this.playSound(SoundEvents.ENTITY_ZOMBIE_NAUTILUS_EAT);
    }

    @Override
    protected SoundEvent getSwimSound() {
        return SoundEvents.ENTITY_ZOMBIE_NAUTILUS_SWIM;
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        super.initDataTracker(builder);
        builder.add(VARIANT, Variants.getOrDefaultOrThrow(this.getRegistryManager(), ZombieNautilusVariants.TEMPERATE));
    }

    @Override
    protected void readCustomData(ReadView view) {
        super.readCustomData(view);
        Variants.fromData(view, RegistryKeys.ZOMBIE_NAUTILUS_VARIANT).ifPresent(this::setVariant);
    }

    @Override
    protected void writeCustomData(WriteView view) {
        super.writeCustomData(view);
        Variants.writeData(view, this.getVariant());
    }

    public void setVariant(RegistryEntry<ZombieNautilusVariant> variant) {
        this.dataTracker.set(VARIANT, variant);
    }

    public RegistryEntry<ZombieNautilusVariant> getVariant() {
        return this.dataTracker.get(VARIANT);
    }

    @Override
    public <T> @Nullable T get(ComponentType<? extends T> type) {
        if (type == DataComponentTypes.ZOMBIE_NAUTILUS_VARIANT) {
            return ZombieNautilusEntity.castComponentValue(type, new LazyRegistryEntryReference<ZombieNautilusVariant>(this.getVariant()));
        }
        return super.get(type);
    }

    @Override
    protected void copyComponentsFrom(ComponentsAccess from) {
        this.copyComponentFrom(from, DataComponentTypes.ZOMBIE_NAUTILUS_VARIANT);
        super.copyComponentsFrom(from);
    }

    @Override
    protected <T> boolean setApplicableComponent(ComponentType<T> type, T value) {
        if (type == DataComponentTypes.ZOMBIE_NAUTILUS_VARIANT) {
            Optional<RegistryEntry<ZombieNautilusVariant>> optional = ZombieNautilusEntity.castComponentValue(DataComponentTypes.ZOMBIE_NAUTILUS_VARIANT, value).resolveEntry(this.getRegistryManager());
            if (optional.isPresent()) {
                this.setVariant(optional.get());
                return true;
            }
            return false;
        }
        return super.setApplicableComponent(type, value);
    }

    @Override
    public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData) {
        Variants.select(SpawnContext.of(world, this.getBlockPos()), RegistryKeys.ZOMBIE_NAUTILUS_VARIANT).ifPresent(this::setVariant);
        return super.initialize(world, difficulty, spawnReason, entityData);
    }

    @Override
    public boolean canBeLeashed() {
        return !this.hasAttackTarget() && !this.isControlledByMob();
    }

    @Override
    public boolean isBaby() {
        return false;
    }

    @Override
    public /* synthetic */ @Nullable PassiveEntity createChild(ServerWorld world, PassiveEntity entity) {
        return this.createChild(world, entity);
    }
}

