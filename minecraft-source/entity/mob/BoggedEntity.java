/*
 * External method calls:
 *   Lnet/minecraft/entity/mob/AbstractSkeletonEntity;createAbstractSkeletonAttributes()Lnet/minecraft/entity/attribute/DefaultAttributeContainer$Builder;
 *   Lnet/minecraft/entity/mob/AbstractSkeletonEntity;initDataTracker(Lnet/minecraft/entity/data/DataTracker$Builder;)V
 *   Lnet/minecraft/entity/mob/AbstractSkeletonEntity;writeCustomData(Lnet/minecraft/storage/WriteView;)V
 *   Lnet/minecraft/storage/WriteView;putBoolean(Ljava/lang/String;Z)V
 *   Lnet/minecraft/entity/mob/AbstractSkeletonEntity;readCustomData(Lnet/minecraft/storage/ReadView;)V
 *   Lnet/minecraft/item/ItemStack;damage(ILnet/minecraft/entity/LivingEntity;Lnet/minecraft/entity/EquipmentSlot;)V
 *   Lnet/minecraft/entity/mob/AbstractSkeletonEntity;interactMob(Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/util/Hand;)Lnet/minecraft/util/ActionResult;
 *   Lnet/minecraft/entity/mob/AbstractSkeletonEntity;createArrowProjectile(Lnet/minecraft/item/ItemStack;FLnet/minecraft/item/ItemStack;)Lnet/minecraft/entity/projectile/PersistentProjectileEntity;
 *   Lnet/minecraft/entity/projectile/ArrowEntity;addEffect(Lnet/minecraft/entity/effect/StatusEffectInstance;)V
 *   Lnet/minecraft/server/world/ServerWorld;playSoundFromEntity(Lnet/minecraft/entity/Entity;Lnet/minecraft/entity/Entity;Lnet/minecraft/sound/SoundEvent;Lnet/minecraft/sound/SoundCategory;FF)V
 *   Lnet/minecraft/entity/data/DataTracker;registerData(Ljava/lang/Class;Lnet/minecraft/entity/data/TrackedDataHandler;)Lnet/minecraft/entity/data/TrackedData;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/entity/mob/BoggedEntity;sheared(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/sound/SoundCategory;Lnet/minecraft/item/ItemStack;)V
 *   Lnet/minecraft/entity/mob/BoggedEntity;emitGameEvent(Lnet/minecraft/registry/entry/RegistryEntry;Lnet/minecraft/entity/Entity;)V
 *   Lnet/minecraft/entity/mob/BoggedEntity;dropShearedItems(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/item/ItemStack;)V
 *   Lnet/minecraft/entity/mob/BoggedEntity;forEachShearedItem(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/registry/RegistryKey;Lnet/minecraft/item/ItemStack;Ljava/util/function/BiConsumer;)V
 *   Lnet/minecraft/entity/mob/BoggedEntity;dropStack(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/item/ItemStack;F)Lnet/minecraft/entity/ItemEntity;
 */
package net.minecraft.entity.mob;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Shearable;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.AbstractSkeletonEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.loot.LootTables;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.jspecify.annotations.Nullable;

public class BoggedEntity
extends AbstractSkeletonEntity
implements Shearable {
    private static final TrackedData<Boolean> SHEARED = DataTracker.registerData(BoggedEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final String SHEARED_KEY = "sheared";
    private static final boolean DEFAULT_SHEARED = false;

    public static DefaultAttributeContainer.Builder createBoggedAttributes() {
        return AbstractSkeletonEntity.createAbstractSkeletonAttributes().add(EntityAttributes.MAX_HEALTH, 16.0);
    }

    public BoggedEntity(EntityType<? extends BoggedEntity> arg, World arg2) {
        super((EntityType<? extends AbstractSkeletonEntity>)arg, arg2);
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        super.initDataTracker(builder);
        builder.add(SHEARED, false);
    }

    @Override
    protected void writeCustomData(WriteView view) {
        super.writeCustomData(view);
        view.putBoolean(SHEARED_KEY, this.isSheared());
    }

    @Override
    protected void readCustomData(ReadView view) {
        super.readCustomData(view);
        this.setSheared(view.getBoolean(SHEARED_KEY, false));
    }

    public boolean isSheared() {
        return this.dataTracker.get(SHEARED);
    }

    public void setSheared(boolean sheared) {
        this.dataTracker.set(SHEARED, sheared);
    }

    @Override
    protected ActionResult interactMob(PlayerEntity player, Hand hand) {
        ItemStack lv = player.getStackInHand(hand);
        if (lv.isOf(Items.SHEARS) && this.isShearable()) {
            World world = this.getEntityWorld();
            if (world instanceof ServerWorld) {
                ServerWorld lv2 = (ServerWorld)world;
                this.sheared(lv2, SoundCategory.PLAYERS, lv);
                this.emitGameEvent(GameEvent.SHEAR, player);
                lv.damage(1, (LivingEntity)player, hand.getEquipmentSlot());
            }
            return ActionResult.SUCCESS;
        }
        return super.interactMob(player, hand);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_BOGGED_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.ENTITY_BOGGED_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_BOGGED_DEATH;
    }

    @Override
    protected SoundEvent getStepSound() {
        return SoundEvents.ENTITY_BOGGED_STEP;
    }

    @Override
    protected PersistentProjectileEntity createArrowProjectile(ItemStack arrow, float damageModifier, @Nullable ItemStack shotFrom) {
        PersistentProjectileEntity lv = super.createArrowProjectile(arrow, damageModifier, shotFrom);
        if (lv instanceof ArrowEntity) {
            ArrowEntity lv2 = (ArrowEntity)lv;
            lv2.addEffect(new StatusEffectInstance(StatusEffects.POISON, 100));
        }
        return lv;
    }

    @Override
    protected int getHardAttackInterval() {
        return 50;
    }

    @Override
    protected int getRegularAttackInterval() {
        return 70;
    }

    @Override
    public void sheared(ServerWorld world, SoundCategory shearedSoundCategory, ItemStack shears) {
        world.playSoundFromEntity(null, this, SoundEvents.ENTITY_BOGGED_SHEAR, shearedSoundCategory, 1.0f, 1.0f);
        this.dropShearedItems(world, shears);
        this.setSheared(true);
    }

    private void dropShearedItems(ServerWorld world, ItemStack shears) {
        this.forEachShearedItem(world, LootTables.BOGGED_SHEARING, shears, (worldx, stack) -> this.dropStack((ServerWorld)worldx, (ItemStack)stack, this.getHeight()));
    }

    @Override
    public boolean isShearable() {
        return !this.isSheared() && this.isAlive();
    }
}

