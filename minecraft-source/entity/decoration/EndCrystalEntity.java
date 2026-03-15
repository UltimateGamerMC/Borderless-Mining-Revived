/*
 * External method calls:
 *   Lnet/minecraft/storage/WriteView;putNullable(Ljava/lang/String;Lcom/mojang/serialization/Codec;Ljava/lang/Object;)V
 *   Lnet/minecraft/storage/WriteView;putBoolean(Ljava/lang/String;Z)V
 *   Lnet/minecraft/storage/ReadView;read(Ljava/lang/String;Lcom/mojang/serialization/Codec;)Ljava/util/Optional;
 *   Lnet/minecraft/entity/damage/DamageSources;explosion(Lnet/minecraft/entity/Entity;Lnet/minecraft/entity/Entity;)Lnet/minecraft/entity/damage/DamageSource;
 *   Lnet/minecraft/server/world/ServerWorld;createExplosion(Lnet/minecraft/entity/Entity;Lnet/minecraft/entity/damage/DamageSource;Lnet/minecraft/world/explosion/ExplosionBehavior;DDDFZLnet/minecraft/world/World$ExplosionSourceType;)V
 *   Lnet/minecraft/entity/damage/DamageSources;generic()Lnet/minecraft/entity/damage/DamageSource;
 *   Lnet/minecraft/entity/Entity;kill(Lnet/minecraft/server/world/ServerWorld;)V
 *   Lnet/minecraft/entity/boss/dragon/EnderDragonFight;crystalDestroyed(Lnet/minecraft/entity/decoration/EndCrystalEntity;Lnet/minecraft/entity/damage/DamageSource;)V
 *   Lnet/minecraft/entity/data/DataTracker;registerData(Ljava/lang/Class;Lnet/minecraft/entity/data/TrackedDataHandler;)Lnet/minecraft/entity/data/TrackedData;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/entity/decoration/EndCrystalEntity;crystalDestroyed(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/damage/DamageSource;)V
 */
package net.minecraft.entity.decoration;

import java.util.Optional;
import net.minecraft.block.AbstractFireBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.boss.dragon.EnderDragonFight;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jspecify.annotations.Nullable;

public class EndCrystalEntity
extends Entity {
    private static final TrackedData<Optional<BlockPos>> BEAM_TARGET = DataTracker.registerData(EndCrystalEntity.class, TrackedDataHandlerRegistry.OPTIONAL_BLOCK_POS);
    private static final TrackedData<Boolean> SHOW_BOTTOM = DataTracker.registerData(EndCrystalEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final boolean DEFAULT_SHOW_BOTTOM = true;
    public int endCrystalAge;

    public EndCrystalEntity(EntityType<? extends EndCrystalEntity> arg, World arg2) {
        super(arg, arg2);
        this.intersectionChecked = true;
        this.endCrystalAge = this.random.nextInt(100000);
    }

    public EndCrystalEntity(World world, double x, double y, double z) {
        this((EntityType<? extends EndCrystalEntity>)EntityType.END_CRYSTAL, world);
        this.setPosition(x, y, z);
    }

    @Override
    protected Entity.MoveEffect getMoveEffect() {
        return Entity.MoveEffect.NONE;
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        builder.add(BEAM_TARGET, Optional.empty());
        builder.add(SHOW_BOTTOM, true);
    }

    @Override
    public void tick() {
        ++this.endCrystalAge;
        this.tickBlockCollision();
        this.tickPortalTeleportation();
        if (this.getEntityWorld() instanceof ServerWorld) {
            BlockPos lv = this.getBlockPos();
            if (((ServerWorld)this.getEntityWorld()).getEnderDragonFight() != null && this.getEntityWorld().getBlockState(lv).isAir()) {
                this.getEntityWorld().setBlockState(lv, AbstractFireBlock.getState(this.getEntityWorld(), lv));
            }
        }
    }

    @Override
    protected void writeCustomData(WriteView view) {
        view.putNullable("beam_target", BlockPos.CODEC, this.getBeamTarget());
        view.putBoolean("ShowBottom", this.shouldShowBottom());
    }

    @Override
    protected void readCustomData(ReadView view) {
        this.setBeamTarget(view.read("beam_target", BlockPos.CODEC).orElse(null));
        this.setShowBottom(view.getBoolean("ShowBottom", true));
    }

    @Override
    public boolean canHit() {
        return true;
    }

    @Override
    public final boolean clientDamage(DamageSource source) {
        if (this.isAlwaysInvulnerableTo(source)) {
            return false;
        }
        return !(source.getAttacker() instanceof EnderDragonEntity);
    }

    @Override
    public final boolean damage(ServerWorld world, DamageSource source, float amount) {
        if (this.isAlwaysInvulnerableTo(source)) {
            return false;
        }
        if (source.getAttacker() instanceof EnderDragonEntity) {
            return false;
        }
        if (!this.isRemoved()) {
            this.remove(Entity.RemovalReason.KILLED);
            if (!source.isIn(DamageTypeTags.IS_EXPLOSION)) {
                DamageSource lv = source.getAttacker() != null ? this.getDamageSources().explosion(this, source.getAttacker()) : null;
                world.createExplosion(this, lv, null, this.getX(), this.getY(), this.getZ(), 6.0f, false, World.ExplosionSourceType.BLOCK);
            }
            this.crystalDestroyed(world, source);
        }
        return true;
    }

    @Override
    public void kill(ServerWorld world) {
        this.crystalDestroyed(world, this.getDamageSources().generic());
        super.kill(world);
    }

    private void crystalDestroyed(ServerWorld world, DamageSource source) {
        EnderDragonFight lv = world.getEnderDragonFight();
        if (lv != null) {
            lv.crystalDestroyed(this, source);
        }
    }

    public void setBeamTarget(@Nullable BlockPos beamTarget) {
        this.getDataTracker().set(BEAM_TARGET, Optional.ofNullable(beamTarget));
    }

    public @Nullable BlockPos getBeamTarget() {
        return this.getDataTracker().get(BEAM_TARGET).orElse(null);
    }

    public void setShowBottom(boolean showBottom) {
        this.getDataTracker().set(SHOW_BOTTOM, showBottom);
    }

    public boolean shouldShowBottom() {
        return this.getDataTracker().get(SHOW_BOTTOM);
    }

    @Override
    public boolean shouldRender(double distance) {
        return super.shouldRender(distance) || this.getBeamTarget() != null;
    }

    @Override
    public ItemStack getPickBlockStack() {
        return new ItemStack(Items.END_CRYSTAL);
    }
}

