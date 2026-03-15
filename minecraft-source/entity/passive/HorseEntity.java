/*
 * External method calls:
 *   Lnet/minecraft/entity/passive/AbstractHorseEntity;initDataTracker(Lnet/minecraft/entity/data/DataTracker$Builder;)V
 *   Lnet/minecraft/entity/passive/AbstractHorseEntity;writeCustomData(Lnet/minecraft/storage/WriteView;)V
 *   Lnet/minecraft/storage/WriteView;putInt(Ljava/lang/String;I)V
 *   Lnet/minecraft/entity/passive/AbstractHorseEntity;readCustomData(Lnet/minecraft/storage/ReadView;)V
 *   Lnet/minecraft/entity/passive/HorseColor;byIndex(I)Lnet/minecraft/entity/passive/HorseColor;
 *   Lnet/minecraft/entity/passive/AbstractHorseEntity;copyComponentsFrom(Lnet/minecraft/component/ComponentsAccess;)V
 *   Lnet/minecraft/entity/passive/HorseMarking;byIndex(I)Lnet/minecraft/entity/passive/HorseMarking;
 *   Lnet/minecraft/entity/passive/AbstractHorseEntity;playWalkSound(Lnet/minecraft/sound/BlockSoundGroup;)V
 *   Lnet/minecraft/entity/passive/AbstractHorseEntity;interactMob(Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/util/Hand;)Lnet/minecraft/util/ActionResult;
 *   Lnet/minecraft/entity/EntityType;create(Lnet/minecraft/world/World;Lnet/minecraft/entity/SpawnReason;)Lnet/minecraft/entity/Entity;
 *   Lnet/minecraft/entity/passive/HorseColor;values()[Lnet/minecraft/entity/passive/HorseColor;
 *   Lnet/minecraft/entity/passive/HorseMarking;values()[Lnet/minecraft/entity/passive/HorseMarking;
 *   Lnet/minecraft/entity/passive/AbstractHorseEntity;initialize(Lnet/minecraft/world/ServerWorldAccess;Lnet/minecraft/world/LocalDifficulty;Lnet/minecraft/entity/SpawnReason;Lnet/minecraft/entity/EntityData;)Lnet/minecraft/entity/EntityData;
 *   Lnet/minecraft/entity/data/DataTracker;registerData(Ljava/lang/Class;Lnet/minecraft/entity/data/TrackedDataHandler;)Lnet/minecraft/entity/data/TrackedData;
 *   Lnet/minecraft/entity/EntityAttachments;builder()Lnet/minecraft/entity/EntityAttachments$Builder;
 *   Lnet/minecraft/entity/EntityDimensions;withAttachments(Lnet/minecraft/entity/EntityAttachments$Builder;)Lnet/minecraft/entity/EntityDimensions;
 *   Lnet/minecraft/entity/EntityDimensions;scaled(F)Lnet/minecraft/entity/EntityDimensions;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/entity/passive/HorseEntity;castComponentValue(Lnet/minecraft/component/ComponentType;Ljava/lang/Object;)Ljava/lang/Object;
 *   Lnet/minecraft/entity/passive/HorseEntity;copyComponentFrom(Lnet/minecraft/component/ComponentsAccess;Lnet/minecraft/component/ComponentType;)Z
 *   Lnet/minecraft/entity/passive/HorseEntity;playSound(Lnet/minecraft/sound/SoundEvent;FF)V
 *   Lnet/minecraft/entity/passive/HorseEntity;interactHorse(Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/item/ItemStack;)Lnet/minecraft/util/ActionResult;
 *   Lnet/minecraft/entity/passive/HorseEntity;damageEquipment(Lnet/minecraft/entity/damage/DamageSource;F[Lnet/minecraft/entity/EquipmentSlot;)V
 */
package net.minecraft.entity.passive;

import net.minecraft.component.ComponentType;
import net.minecraft.component.ComponentsAccess;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.EntityAttachmentType;
import net.minecraft.entity.EntityAttachments;
import net.minecraft.entity.EntityData;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.pathing.PathNodeType;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.passive.AbstractHorseEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.DonkeyEntity;
import net.minecraft.entity.passive.HorseColor;
import net.minecraft.entity.passive.HorseMarking;
import net.minecraft.entity.passive.MuleEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Util;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import org.jspecify.annotations.Nullable;

public class HorseEntity
extends AbstractHorseEntity {
    private static final TrackedData<Integer> VARIANT = DataTracker.registerData(HorseEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private static final EntityDimensions BABY_BASE_DIMENSIONS = EntityType.HORSE.getDimensions().withAttachments(EntityAttachments.builder().add(EntityAttachmentType.PASSENGER, 0.0f, EntityType.HORSE.getHeight() + 0.125f, 0.0f)).scaled(0.5f);
    private static final int DEFAULT_VARIANT = 0;

    public HorseEntity(EntityType<? extends HorseEntity> arg, World arg2) {
        super((EntityType<? extends AbstractHorseEntity>)arg, arg2);
        this.setPathfindingPenalty(PathNodeType.DANGER_OTHER, -1.0f);
        this.setPathfindingPenalty(PathNodeType.DAMAGE_OTHER, -1.0f);
    }

    @Override
    protected void initAttributes(Random random) {
        this.getAttributeInstance(EntityAttributes.MAX_HEALTH).setBaseValue(HorseEntity.getChildHealthBonus(random::nextInt));
        this.getAttributeInstance(EntityAttributes.MOVEMENT_SPEED).setBaseValue(HorseEntity.getChildMovementSpeedBonus(random::nextDouble));
        this.getAttributeInstance(EntityAttributes.JUMP_STRENGTH).setBaseValue(HorseEntity.getChildJumpStrengthBonus(random::nextDouble));
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        super.initDataTracker(builder);
        builder.add(VARIANT, 0);
    }

    @Override
    protected void writeCustomData(WriteView view) {
        super.writeCustomData(view);
        view.putInt("Variant", this.getHorseVariant());
    }

    @Override
    protected void readCustomData(ReadView view) {
        super.readCustomData(view);
        this.setHorseVariant(view.getInt("Variant", 0));
    }

    private void setHorseVariant(int variant) {
        this.dataTracker.set(VARIANT, variant);
    }

    private int getHorseVariant() {
        return this.dataTracker.get(VARIANT);
    }

    private void setHorseVariant(HorseColor color, HorseMarking marking) {
        this.setHorseVariant(color.getIndex() & 0xFF | marking.getIndex() << 8 & 0xFF00);
    }

    public HorseColor getHorseColor() {
        return HorseColor.byIndex(this.getHorseVariant() & 0xFF);
    }

    private void setHorseColor(HorseColor color) {
        this.setHorseVariant(color.getIndex() & 0xFF | this.getHorseVariant() & 0xFFFFFF00);
    }

    @Override
    public <T> @Nullable T get(ComponentType<? extends T> type) {
        if (type == DataComponentTypes.HORSE_VARIANT) {
            return HorseEntity.castComponentValue(type, this.getHorseColor());
        }
        return super.get(type);
    }

    @Override
    protected void copyComponentsFrom(ComponentsAccess from) {
        this.copyComponentFrom(from, DataComponentTypes.HORSE_VARIANT);
        super.copyComponentsFrom(from);
    }

    @Override
    protected <T> boolean setApplicableComponent(ComponentType<T> type, T value) {
        if (type == DataComponentTypes.HORSE_VARIANT) {
            this.setHorseColor(HorseEntity.castComponentValue(DataComponentTypes.HORSE_VARIANT, value));
            return true;
        }
        return super.setApplicableComponent(type, value);
    }

    public HorseMarking getMarking() {
        return HorseMarking.byIndex((this.getHorseVariant() & 0xFF00) >> 8);
    }

    @Override
    protected void playWalkSound(BlockSoundGroup group) {
        super.playWalkSound(group);
        if (this.random.nextInt(10) == 0) {
            this.playSound(SoundEvents.ENTITY_HORSE_BREATHE, group.getVolume() * 0.6f, group.getPitch());
        }
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_HORSE_AMBIENT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_HORSE_DEATH;
    }

    @Override
    protected SoundEvent getEatSound() {
        return SoundEvents.ENTITY_HORSE_EAT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.ENTITY_HORSE_HURT;
    }

    @Override
    protected SoundEvent getAngrySound() {
        return SoundEvents.ENTITY_HORSE_ANGRY;
    }

    @Override
    public ActionResult interactMob(PlayerEntity player, Hand hand) {
        boolean bl;
        boolean bl2 = bl = !this.isBaby() && this.isTame() && player.shouldCancelInteraction();
        if (this.hasPassengers() || bl) {
            return super.interactMob(player, hand);
        }
        ItemStack lv = player.getStackInHand(hand);
        if (!lv.isEmpty()) {
            if (this.isBreedingItem(lv)) {
                return this.interactHorse(player, lv);
            }
            if (!this.isTame()) {
                this.playAngrySound();
                return ActionResult.SUCCESS;
            }
        }
        return super.interactMob(player, hand);
    }

    @Override
    public boolean canBreedWith(AnimalEntity other) {
        if (other == this) {
            return false;
        }
        if (other instanceof DonkeyEntity || other instanceof HorseEntity) {
            return this.canBreed() && ((AbstractHorseEntity)other).canBreed();
        }
        return false;
    }

    @Override
    public @Nullable PassiveEntity createChild(ServerWorld world, PassiveEntity entity) {
        if (entity instanceof DonkeyEntity) {
            MuleEntity lv = EntityType.MULE.create(world, SpawnReason.BREEDING);
            if (lv != null) {
                this.setChildAttributes(entity, lv);
            }
            return lv;
        }
        HorseEntity lv2 = (HorseEntity)entity;
        HorseEntity lv3 = EntityType.HORSE.create(world, SpawnReason.BREEDING);
        if (lv3 != null) {
            int i = this.random.nextInt(9);
            HorseColor lv4 = i < 4 ? this.getHorseColor() : (i < 8 ? lv2.getHorseColor() : Util.getRandom(HorseColor.values(), this.random));
            int j = this.random.nextInt(5);
            HorseMarking lv5 = j < 2 ? this.getMarking() : (j < 4 ? lv2.getMarking() : Util.getRandom(HorseMarking.values(), this.random));
            lv3.setHorseVariant(lv4, lv5);
            this.setChildAttributes(entity, lv3);
        }
        return lv3;
    }

    @Override
    public boolean canUseSlot(EquipmentSlot slot) {
        return true;
    }

    @Override
    protected void damageArmor(DamageSource source, float amount) {
        this.damageEquipment(source, amount, EquipmentSlot.BODY);
    }

    @Override
    public @Nullable EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData) {
        HorseColor lv2;
        Random lv = world.getRandom();
        if (entityData instanceof HorseData) {
            lv2 = ((HorseData)entityData).color;
        } else {
            lv2 = Util.getRandom(HorseColor.values(), lv);
            entityData = new HorseData(lv2);
        }
        this.setHorseVariant(lv2, Util.getRandom(HorseMarking.values(), lv));
        return super.initialize(world, difficulty, spawnReason, entityData);
    }

    @Override
    public EntityDimensions getBaseDimensions(EntityPose pose) {
        return this.isBaby() ? BABY_BASE_DIMENSIONS : super.getBaseDimensions(pose);
    }

    public static class HorseData
    extends PassiveEntity.PassiveData {
        public final HorseColor color;

        public HorseData(HorseColor color) {
            super(true);
            this.color = color;
        }
    }
}

