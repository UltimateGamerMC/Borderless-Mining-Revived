/*
 * External method calls:
 *   Lnet/minecraft/entity/data/DataTracker;registerData(Ljava/lang/Class;Lnet/minecraft/entity/data/TrackedDataHandler;)Lnet/minecraft/entity/data/TrackedData;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/entity/vehicle/VehicleEntity;emitGameEvent(Lnet/minecraft/registry/entry/RegistryEntry;Lnet/minecraft/entity/Entity;)V
 *   Lnet/minecraft/entity/vehicle/VehicleEntity;killAndDropSelf(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/damage/DamageSource;)V
 *   Lnet/minecraft/entity/vehicle/VehicleEntity;kill(Lnet/minecraft/server/world/ServerWorld;)V
 *   Lnet/minecraft/entity/vehicle/VehicleEntity;dropStack(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/item/ItemStack;)Lnet/minecraft/entity/ItemEntity;
 *   Lnet/minecraft/entity/vehicle/VehicleEntity;asItem()Lnet/minecraft/item/Item;
 *   Lnet/minecraft/entity/vehicle/VehicleEntity;killAndDropItem(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/item/Item;)V
 */
package net.minecraft.entity.vehicle;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import net.minecraft.world.explosion.Explosion;
import net.minecraft.world.rule.GameRules;

public abstract class VehicleEntity
extends Entity {
    protected static final TrackedData<Integer> DAMAGE_WOBBLE_TICKS = DataTracker.registerData(VehicleEntity.class, TrackedDataHandlerRegistry.INTEGER);
    protected static final TrackedData<Integer> DAMAGE_WOBBLE_SIDE = DataTracker.registerData(VehicleEntity.class, TrackedDataHandlerRegistry.INTEGER);
    protected static final TrackedData<Float> DAMAGE_WOBBLE_STRENGTH = DataTracker.registerData(VehicleEntity.class, TrackedDataHandlerRegistry.FLOAT);

    public VehicleEntity(EntityType<?> arg, World arg2) {
        super(arg, arg2);
    }

    @Override
    public boolean clientDamage(DamageSource source) {
        return true;
    }

    /*
     * Unable to fully structure code
     */
    @Override
    public boolean damage(ServerWorld world, DamageSource source, float amount) {
        if (this.isRemoved()) {
            return true;
        }
        if (this.isAlwaysInvulnerableTo(source)) {
            return false;
        }
        this.setDamageWobbleSide(-this.getDamageWobbleSide());
        this.setDamageWobbleTicks(10);
        this.scheduleVelocityUpdate();
        this.setDamageWobbleStrength(this.getDamageWobbleStrength() + amount * 10.0f);
        this.emitGameEvent(GameEvent.ENTITY_DAMAGE, source.getAttacker());
        var6_4 = source.getAttacker();
        if (!(var6_4 instanceof PlayerEntity)) ** GOTO lbl-1000
        lv = (PlayerEntity)var6_4;
        if (lv.getAbilities().creativeMode) {
            v0 = true;
        } else lbl-1000:
        // 2 sources

        {
            v0 = bl = false;
        }
        if (bl == false && this.getDamageWobbleStrength() > 40.0f || this.shouldAlwaysKill(source)) {
            this.killAndDropSelf(world, source);
        } else if (bl) {
            this.discard();
        }
        return true;
    }

    protected boolean shouldAlwaysKill(DamageSource source) {
        return false;
    }

    @Override
    public boolean isImmuneToExplosion(Explosion explosion) {
        return explosion.getCausingEntity() instanceof MobEntity && explosion.getWorld().getGameRules().getValue(GameRules.DO_MOB_GRIEFING) == false;
    }

    public void killAndDropItem(ServerWorld world, Item item) {
        this.kill(world);
        if (!world.getGameRules().getValue(GameRules.ENTITY_DROPS).booleanValue()) {
            return;
        }
        ItemStack lv = new ItemStack(item);
        lv.set(DataComponentTypes.CUSTOM_NAME, this.getCustomName());
        this.dropStack(world, lv);
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        builder.add(DAMAGE_WOBBLE_TICKS, 0);
        builder.add(DAMAGE_WOBBLE_SIDE, 1);
        builder.add(DAMAGE_WOBBLE_STRENGTH, Float.valueOf(0.0f));
    }

    public void setDamageWobbleTicks(int damageWobbleTicks) {
        this.dataTracker.set(DAMAGE_WOBBLE_TICKS, damageWobbleTicks);
    }

    public void setDamageWobbleSide(int damageWobbleSide) {
        this.dataTracker.set(DAMAGE_WOBBLE_SIDE, damageWobbleSide);
    }

    public void setDamageWobbleStrength(float damageWobbleStrength) {
        this.dataTracker.set(DAMAGE_WOBBLE_STRENGTH, Float.valueOf(damageWobbleStrength));
    }

    public float getDamageWobbleStrength() {
        return this.dataTracker.get(DAMAGE_WOBBLE_STRENGTH).floatValue();
    }

    public int getDamageWobbleTicks() {
        return this.dataTracker.get(DAMAGE_WOBBLE_TICKS);
    }

    public int getDamageWobbleSide() {
        return this.dataTracker.get(DAMAGE_WOBBLE_SIDE);
    }

    protected void killAndDropSelf(ServerWorld world, DamageSource damageSource) {
        this.killAndDropItem(world, this.asItem());
    }

    @Override
    public int getDefaultPortalCooldown() {
        return 10;
    }

    protected abstract Item asItem();
}

