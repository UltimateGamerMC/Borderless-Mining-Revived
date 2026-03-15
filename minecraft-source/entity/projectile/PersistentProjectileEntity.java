/*
 * External method calls:
 *   Lnet/minecraft/entity/projectile/ProjectileEntity;onTrackedDataSet(Lnet/minecraft/entity/data/TrackedData;)V
 *   Lnet/minecraft/world/World;addParticleClient(Lnet/minecraft/particle/ParticleEffect;DDDDDD)V
 *   Lnet/minecraft/entity/projectile/ProjectileEntity;move(Lnet/minecraft/entity/MovementType;Lnet/minecraft/util/math/Vec3d;)V
 *   Lnet/minecraft/entity/projectile/ProjectileEntity;onBubbleColumnSurfaceCollision(ZLnet/minecraft/util/math/BlockPos;)V
 *   Lnet/minecraft/entity/projectile/ProjectileEntity;onBubbleColumnCollision(Z)V
 *   Lnet/minecraft/entity/projectile/ProjectileEntity;addVelocity(DDD)V
 *   Lnet/minecraft/entity/projectile/ProjectileEntity;onEntityHit(Lnet/minecraft/util/hit/EntityHitResult;)V
 *   Lnet/minecraft/entity/damage/DamageSources;arrow(Lnet/minecraft/entity/projectile/PersistentProjectileEntity;Lnet/minecraft/entity/Entity;)Lnet/minecraft/entity/damage/DamageSource;
 *   Lnet/minecraft/entity/LivingEntity;onAttacking(Lnet/minecraft/entity/Entity;)V
 *   Lnet/minecraft/entity/Entity;sidedDamage(Lnet/minecraft/entity/damage/DamageSource;F)Z
 *   Lnet/minecraft/enchantment/EnchantmentHelper;onTargetDamaged(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/Entity;Lnet/minecraft/entity/damage/DamageSource;Lnet/minecraft/item/ItemStack;)V
 *   Lnet/minecraft/server/network/ServerPlayNetworkHandler;sendPacket(Lnet/minecraft/network/packet/Packet;)V
 *   Lnet/minecraft/advancement/criterion/KilledByArrowCriterion;trigger(Lnet/minecraft/server/network/ServerPlayerEntity;Ljava/util/Collection;Lnet/minecraft/item/ItemStack;)V
 *   Lnet/minecraft/enchantment/EnchantmentHelper;modifyKnockback(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/item/ItemStack;Lnet/minecraft/entity/Entity;Lnet/minecraft/entity/damage/DamageSource;F)F
 *   Lnet/minecraft/entity/LivingEntity;addVelocity(DDD)V
 *   Lnet/minecraft/entity/projectile/ProjectileEntity;onBlockHit(Lnet/minecraft/util/hit/BlockHitResult;)V
 *   Lnet/minecraft/enchantment/EnchantmentHelper;onHitBlock(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/item/ItemStack;Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/entity/Entity;Lnet/minecraft/entity/EquipmentSlot;Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/block/BlockState;Ljava/util/function/Consumer;)V
 *   Lnet/minecraft/entity/projectile/ProjectileUtil;collectPiercingCollisions(Lnet/minecraft/world/World;Lnet/minecraft/entity/Entity;Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/util/math/Box;Ljava/util/function/Predicate;Z)Ljava/util/Collection;
 *   Lnet/minecraft/entity/projectile/ProjectileEntity;writeCustomData(Lnet/minecraft/storage/WriteView;)V
 *   Lnet/minecraft/storage/WriteView;putShort(Ljava/lang/String;S)V
 *   Lnet/minecraft/storage/WriteView;putNullable(Ljava/lang/String;Lcom/mojang/serialization/Codec;Ljava/lang/Object;)V
 *   Lnet/minecraft/storage/WriteView;putByte(Ljava/lang/String;B)V
 *   Lnet/minecraft/storage/WriteView;putBoolean(Ljava/lang/String;Z)V
 *   Lnet/minecraft/storage/WriteView;putDouble(Ljava/lang/String;D)V
 *   Lnet/minecraft/entity/projectile/ProjectileEntity;readCustomData(Lnet/minecraft/storage/ReadView;)V
 *   Lnet/minecraft/storage/ReadView;read(Ljava/lang/String;Lcom/mojang/serialization/Codec;)Ljava/util/Optional;
 *   Lnet/minecraft/entity/player/PlayerEntity;sendPickup(Lnet/minecraft/entity/Entity;I)V
 *   Lnet/minecraft/entity/player/PlayerInventory;insertStack(Lnet/minecraft/item/ItemStack;)Z
 *   Lnet/minecraft/inventory/StackReference;of(Ljava/util/function/Supplier;Ljava/util/function/Consumer;)Lnet/minecraft/inventory/StackReference;
 *   Lnet/minecraft/entity/data/DataTracker;registerData(Ljava/lang/Class;Lnet/minecraft/entity/data/TrackedDataHandler;)Lnet/minecraft/entity/data/TrackedData;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/entity/projectile/PersistentProjectileEntity;copyComponentsFrom(Lnet/minecraft/item/ItemStack;)V
 *   Lnet/minecraft/entity/projectile/PersistentProjectileEntity;applyDrag(F)V
 *   Lnet/minecraft/entity/projectile/PersistentProjectileEntity;spawnBubbleParticles(Lnet/minecraft/util/math/Vec3d;)V
 *   Lnet/minecraft/entity/projectile/PersistentProjectileEntity;updateRotation(FF)F
 *   Lnet/minecraft/entity/projectile/PersistentProjectileEntity;applyCollision(Lnet/minecraft/util/hit/BlockHitResult;)V
 *   Lnet/minecraft/entity/projectile/PersistentProjectileEntity;collectPiercingCollisions(Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/util/math/Vec3d;)Ljava/util/Collection;
 *   Lnet/minecraft/entity/projectile/PersistentProjectileEntity;tickBlockCollision(Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/util/math/Vec3d;)V
 *   Lnet/minecraft/entity/projectile/PersistentProjectileEntity;hitOrDeflect(Lnet/minecraft/util/hit/HitResult;)Lnet/minecraft/entity/ProjectileDeflection;
 *   Lnet/minecraft/entity/projectile/PersistentProjectileEntity;hitOrDeflect(Ljava/util/Collection;)Lnet/minecraft/entity/ProjectileDeflection;
 *   Lnet/minecraft/entity/projectile/PersistentProjectileEntity;knockback(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/entity/damage/DamageSource;)V
 *   Lnet/minecraft/entity/projectile/PersistentProjectileEntity;onHit(Lnet/minecraft/entity/LivingEntity;)V
 *   Lnet/minecraft/entity/projectile/PersistentProjectileEntity;playSound(Lnet/minecraft/sound/SoundEvent;FF)V
 *   Lnet/minecraft/entity/projectile/PersistentProjectileEntity;deflect(Lnet/minecraft/entity/ProjectileDeflection;Lnet/minecraft/entity/Entity;Lnet/minecraft/entity/LazyEntityReference;Z)Z
 *   Lnet/minecraft/entity/projectile/PersistentProjectileEntity;asItemStack()Lnet/minecraft/item/ItemStack;
 *   Lnet/minecraft/entity/projectile/PersistentProjectileEntity;dropStack(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/item/ItemStack;F)Lnet/minecraft/entity/ItemEntity;
 *   Lnet/minecraft/entity/projectile/PersistentProjectileEntity;onBlockHitEnchantmentEffects(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/util/hit/BlockHitResult;Lnet/minecraft/item/ItemStack;)V
 *   Lnet/minecraft/entity/projectile/PersistentProjectileEntity;tryPickup(Lnet/minecraft/entity/player/PlayerEntity;)Z
 */
package net.minecraft.entity.projectile;

import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import java.lang.runtime.SwitchBootstraps;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.block.BlockState;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.OminousItemSpawnerEntity;
import net.minecraft.entity.ProjectileDeflection;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.s2c.play.GameStateChangeS2CPacket;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.Registries;
import net.minecraft.registry.tag.EntityTypeTags;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.util.Unit;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import org.jspecify.annotations.Nullable;

public abstract class PersistentProjectileEntity
extends ProjectileEntity {
    private static final double field_30657 = 2.0;
    private static final int field_54968 = 7;
    private static final float field_55017 = 0.6f;
    private static final float DEFAULT_DRAG = 0.99f;
    private static final short DEFAULT_LIFE = 0;
    private static final byte DEFAULT_SHAKE = 0;
    private static final boolean DEFAULT_IN_GROUND = false;
    private static final boolean DEFAULT_CRITICAL = false;
    private static final byte DEFAULT_PIERCE_LEVEL = 0;
    private static final TrackedData<Byte> PROJECTILE_FLAGS = DataTracker.registerData(PersistentProjectileEntity.class, TrackedDataHandlerRegistry.BYTE);
    private static final TrackedData<Byte> PIERCE_LEVEL = DataTracker.registerData(PersistentProjectileEntity.class, TrackedDataHandlerRegistry.BYTE);
    private static final TrackedData<Boolean> IN_GROUND = DataTracker.registerData(PersistentProjectileEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final int CRITICAL_FLAG = 1;
    private static final int NO_CLIP_FLAG = 2;
    private @Nullable BlockState inBlockState;
    protected int inGroundTime;
    public PickupPermission pickupType = PickupPermission.DISALLOWED;
    public int shake = 0;
    private int life = 0;
    private double damage = 2.0;
    private SoundEvent sound = this.getHitSound();
    private @Nullable IntOpenHashSet piercedEntities;
    private @Nullable List<Entity> piercingKilledEntities;
    private ItemStack stack = this.getDefaultItemStack();
    private @Nullable ItemStack weapon = null;

    protected PersistentProjectileEntity(EntityType<? extends PersistentProjectileEntity> arg, World arg2) {
        super((EntityType<? extends ProjectileEntity>)arg, arg2);
    }

    protected PersistentProjectileEntity(EntityType<? extends PersistentProjectileEntity> type, double x, double y, double z, World world, ItemStack stack, @Nullable ItemStack weapon) {
        this(type, world);
        this.stack = stack.copy();
        this.copyComponentsFrom(stack);
        Unit lv = stack.remove(DataComponentTypes.INTANGIBLE_PROJECTILE);
        if (lv != null) {
            this.pickupType = PickupPermission.CREATIVE_ONLY;
        }
        this.setPosition(x, y, z);
        if (weapon != null && world instanceof ServerWorld) {
            ServerWorld lv2 = (ServerWorld)world;
            if (weapon.isEmpty()) {
                throw new IllegalArgumentException("Invalid weapon firing an arrow");
            }
            this.weapon = weapon.copy();
            int i = EnchantmentHelper.getProjectilePiercing(lv2, weapon, this.stack);
            if (i > 0) {
                this.setPierceLevel((byte)i);
            }
        }
    }

    protected PersistentProjectileEntity(EntityType<? extends PersistentProjectileEntity> type, LivingEntity owner, World world, ItemStack stack, @Nullable ItemStack shotFrom) {
        this(type, owner.getX(), owner.getEyeY() - (double)0.1f, owner.getZ(), world, stack, shotFrom);
        this.setOwner(owner);
    }

    public void setSound(SoundEvent sound) {
        this.sound = sound;
    }

    @Override
    public boolean shouldRender(double distance) {
        double e = this.getBoundingBox().getAverageSideLength() * 10.0;
        if (Double.isNaN(e)) {
            e = 1.0;
        }
        return distance < (e *= 64.0 * PersistentProjectileEntity.getRenderDistanceMultiplier()) * e;
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        builder.add(PROJECTILE_FLAGS, (byte)0);
        builder.add(PIERCE_LEVEL, (byte)0);
        builder.add(IN_GROUND, false);
    }

    @Override
    public void setVelocity(double x, double y, double z, float power, float uncertainty) {
        super.setVelocity(x, y, z, power, uncertainty);
        this.life = 0;
    }

    @Override
    public void setVelocityClient(Vec3d clientVelocity) {
        super.setVelocityClient(clientVelocity);
        this.life = 0;
        if (this.isInGround() && clientVelocity.lengthSquared() > 0.0) {
            this.setInGround(false);
        }
    }

    @Override
    public void onTrackedDataSet(TrackedData<?> data) {
        super.onTrackedDataSet(data);
        if (!this.firstUpdate && this.shake <= 0 && data.equals(IN_GROUND) && this.isInGround()) {
            this.shake = 7;
        }
    }

    @Override
    public void tick() {
        VoxelShape lv4;
        boolean bl = !this.isNoClip();
        Vec3d lv = this.getVelocity();
        BlockPos lv2 = this.getBlockPos();
        BlockState lv3 = this.getEntityWorld().getBlockState(lv2);
        if (!lv3.isAir() && bl && !(lv4 = lv3.getCollisionShape(this.getEntityWorld(), lv2)).isEmpty()) {
            Vec3d lv5 = this.getEntityPos();
            for (Box lv6 : lv4.getBoundingBoxes()) {
                if (!lv6.offset(lv2).contains(lv5)) continue;
                this.setVelocity(Vec3d.ZERO);
                this.setInGround(true);
                break;
            }
        }
        if (this.shake > 0) {
            --this.shake;
        }
        if (this.isTouchingWaterOrRain()) {
            this.extinguish();
        }
        if (this.isInGround() && bl) {
            if (!this.getEntityWorld().isClient()) {
                if (this.inBlockState != lv3 && this.shouldFall()) {
                    this.fall();
                } else {
                    this.age();
                }
            }
            ++this.inGroundTime;
            if (this.isAlive()) {
                this.tickBlockCollision();
            }
            if (!this.getEntityWorld().isClient()) {
                this.setOnFire(this.getFireTicks() > 0);
            }
            return;
        }
        this.inGroundTime = 0;
        Vec3d lv7 = this.getEntityPos();
        if (this.isTouchingWater()) {
            this.applyDrag(this.getDragInWater());
            this.spawnBubbleParticles(lv7);
        }
        if (this.isCritical()) {
            for (int i = 0; i < 4; ++i) {
                this.getEntityWorld().addParticleClient(ParticleTypes.CRIT, lv7.x + lv.x * (double)i / 4.0, lv7.y + lv.y * (double)i / 4.0, lv7.z + lv.z * (double)i / 4.0, -lv.x, -lv.y + 0.2, -lv.z);
            }
        }
        float f = !bl ? (float)(MathHelper.atan2(-lv.x, -lv.z) * 57.2957763671875) : (float)(MathHelper.atan2(lv.x, lv.z) * 57.2957763671875);
        float g = (float)(MathHelper.atan2(lv.y, lv.horizontalLength()) * 57.2957763671875);
        this.setPitch(PersistentProjectileEntity.updateRotation(this.getPitch(), g));
        this.setYaw(PersistentProjectileEntity.updateRotation(this.getYaw(), f));
        this.tickLeftOwner();
        if (bl) {
            BlockHitResult lv8 = this.getEntityWorld().getCollisionsIncludingWorldBorder(new RaycastContext(lv7, lv7.add(lv), RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, this));
            this.applyCollision(lv8);
        } else {
            this.setPosition(lv7.add(lv));
            this.tickBlockCollision();
        }
        if (!this.isTouchingWater()) {
            this.applyDrag(0.99f);
        }
        if (bl && !this.isInGround()) {
            this.applyGravity();
        }
        super.tick();
    }

    private void applyCollision(BlockHitResult blockHitResult) {
        while (this.isAlive()) {
            Vec3d lv = this.getEntityPos();
            ArrayList<EntityHitResult> arrayList = new ArrayList<EntityHitResult>(this.collectPiercingCollisions(lv, blockHitResult.getPos()));
            arrayList.sort(Comparator.comparingDouble(arg2 -> lv.squaredDistanceTo(arg2.getEntity().getEntityPos())));
            EntityHitResult lv2 = arrayList.isEmpty() ? null : arrayList.getFirst();
            Vec3d lv3 = ((HitResult)Objects.requireNonNullElse(lv2, blockHitResult)).getPos();
            this.setPosition(lv3);
            this.tickBlockCollision(lv, lv3);
            if (this.portalManager != null && this.portalManager.isInPortal()) {
                this.tickPortalTeleportation();
            }
            if (arrayList.isEmpty()) {
                if (!this.isAlive() || blockHitResult.getType() == HitResult.Type.MISS) break;
                this.hitOrDeflect(blockHitResult);
                this.velocityDirty = true;
                break;
            }
            if (!this.isAlive() || this.noClip) continue;
            ProjectileDeflection lv4 = this.hitOrDeflect(arrayList);
            this.velocityDirty = true;
            if (this.getPierceLevel() > 0 && lv4 == ProjectileDeflection.NONE) continue;
            break;
        }
    }

    private ProjectileDeflection hitOrDeflect(Collection<EntityHitResult> hitResults) {
        for (EntityHitResult lv : hitResults) {
            ProjectileDeflection lv2 = this.hitOrDeflect(lv);
            if (this.isAlive() && lv2 == ProjectileDeflection.NONE) continue;
            return lv2;
        }
        return ProjectileDeflection.NONE;
    }

    private void applyDrag(float drag) {
        Vec3d lv = this.getVelocity();
        this.setVelocity(lv.multiply(drag));
    }

    private void spawnBubbleParticles(Vec3d pos) {
        Vec3d lv = this.getVelocity();
        for (int i = 0; i < 4; ++i) {
            float f = 0.25f;
            this.getEntityWorld().addParticleClient(ParticleTypes.BUBBLE, pos.x - lv.x * 0.25, pos.y - lv.y * 0.25, pos.z - lv.z * 0.25, lv.x, lv.y, lv.z);
        }
    }

    @Override
    protected double getGravity() {
        return 0.05;
    }

    private boolean shouldFall() {
        return this.isInGround() && this.getEntityWorld().isSpaceEmpty(new Box(this.getEntityPos(), this.getEntityPos()).expand(0.06));
    }

    private void fall() {
        this.setInGround(false);
        Vec3d lv = this.getVelocity();
        this.setVelocity(lv.multiply(this.random.nextFloat() * 0.2f, this.random.nextFloat() * 0.2f, this.random.nextFloat() * 0.2f));
        this.life = 0;
    }

    protected boolean isInGround() {
        return this.dataTracker.get(IN_GROUND);
    }

    protected void setInGround(boolean inGround) {
        this.dataTracker.set(IN_GROUND, inGround);
    }

    @Override
    public boolean isPushedByFluids() {
        return !this.isInGround();
    }

    @Override
    public void move(MovementType type, Vec3d movement) {
        super.move(type, movement);
        if (type != MovementType.SELF && this.shouldFall()) {
            this.fall();
        }
    }

    protected void age() {
        ++this.life;
        if (this.life >= 1200) {
            this.discard();
        }
    }

    private void clearPiercingStatus() {
        if (this.piercingKilledEntities != null) {
            this.piercingKilledEntities.clear();
        }
        if (this.piercedEntities != null) {
            this.piercedEntities.clear();
        }
    }

    @Override
    public void onBroken(Item item) {
        this.weapon = null;
    }

    @Override
    public void onBubbleColumnSurfaceCollision(boolean drag, BlockPos pos) {
        if (this.isInGround()) {
            return;
        }
        super.onBubbleColumnSurfaceCollision(drag, pos);
    }

    @Override
    public void onBubbleColumnCollision(boolean drag) {
        if (this.isInGround()) {
            return;
        }
        super.onBubbleColumnCollision(drag);
    }

    @Override
    public void addVelocity(double deltaX, double deltaY, double deltaZ) {
        if (this.isInGround()) {
            return;
        }
        super.addVelocity(deltaX, deltaY, deltaZ);
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        World world;
        super.onEntityHit(entityHitResult);
        Entity lv = entityHitResult.getEntity();
        float f = (float)this.getVelocity().length();
        double d = this.damage;
        Entity lv2 = this.getOwner();
        DamageSource lv3 = this.getDamageSources().arrow(this, lv2 != null ? lv2 : this);
        if (this.getWeaponStack() != null && (world = this.getEntityWorld()) instanceof ServerWorld) {
            ServerWorld lv4 = (ServerWorld)world;
            d = EnchantmentHelper.getDamage(lv4, this.getWeaponStack(), lv, lv3, (float)d);
        }
        int i = MathHelper.ceil(MathHelper.clamp((double)f * d, 0.0, 2.147483647E9));
        if (this.getPierceLevel() > 0) {
            if (this.piercedEntities == null) {
                this.piercedEntities = new IntOpenHashSet(5);
            }
            if (this.piercingKilledEntities == null) {
                this.piercingKilledEntities = Lists.newArrayListWithCapacity(5);
            }
            if (this.piercedEntities.size() < this.getPierceLevel() + 1) {
                this.piercedEntities.add(lv.getId());
            } else {
                this.discard();
                return;
            }
        }
        if (this.isCritical()) {
            long l = this.random.nextInt(i / 2 + 2);
            i = (int)Math.min(l + (long)i, Integer.MAX_VALUE);
        }
        if (lv2 instanceof LivingEntity) {
            LivingEntity lv5 = (LivingEntity)lv2;
            lv5.onAttacking(lv);
        }
        boolean bl = lv.getType() == EntityType.ENDERMAN;
        int j = lv.getFireTicks();
        if (this.isOnFire() && !bl) {
            lv.setOnFireFor(5.0f);
        }
        if (lv.sidedDamage(lv3, i)) {
            if (bl) {
                return;
            }
            if (lv instanceof LivingEntity) {
                ServerPlayerEntity lv8;
                LivingEntity lv6 = (LivingEntity)lv;
                if (!this.getEntityWorld().isClient() && this.getPierceLevel() <= 0) {
                    lv6.setStuckArrowCount(lv6.getStuckArrowCount() + 1);
                }
                this.knockback(lv6, lv3);
                World world2 = this.getEntityWorld();
                if (world2 instanceof ServerWorld) {
                    ServerWorld lv7 = (ServerWorld)world2;
                    EnchantmentHelper.onTargetDamaged(lv7, lv6, lv3, this.getWeaponStack());
                }
                this.onHit(lv6);
                if (lv6 instanceof PlayerEntity && lv2 instanceof ServerPlayerEntity) {
                    lv8 = (ServerPlayerEntity)lv2;
                    if (!this.isSilent() && lv6 != lv8) {
                        lv8.networkHandler.sendPacket(new GameStateChangeS2CPacket(GameStateChangeS2CPacket.PROJECTILE_HIT_PLAYER, 0.0f));
                    }
                }
                if (!lv.isAlive() && this.piercingKilledEntities != null) {
                    this.piercingKilledEntities.add(lv6);
                }
                if (!this.getEntityWorld().isClient() && lv2 instanceof ServerPlayerEntity) {
                    lv8 = (ServerPlayerEntity)lv2;
                    if (this.piercingKilledEntities != null) {
                        Criteria.KILLED_BY_ARROW.trigger(lv8, this.piercingKilledEntities, this.weapon);
                    } else if (!lv.isAlive()) {
                        Criteria.KILLED_BY_ARROW.trigger(lv8, List.of(lv), this.weapon);
                    }
                }
            }
            this.playSound(this.sound, 1.0f, 1.2f / (this.random.nextFloat() * 0.2f + 0.9f));
            if (this.getPierceLevel() <= 0) {
                this.discard();
            }
        } else {
            lv.setFireTicks(j);
            this.deflect(ProjectileDeflection.SIMPLE, lv, this.owner, false);
            this.setVelocity(this.getVelocity().multiply(0.2));
            World world3 = this.getEntityWorld();
            if (world3 instanceof ServerWorld) {
                ServerWorld lv9 = (ServerWorld)world3;
                if (this.getVelocity().lengthSquared() < 1.0E-7) {
                    if (this.pickupType == PickupPermission.ALLOWED) {
                        this.dropStack(lv9, this.asItemStack(), 0.1f);
                    }
                    this.discard();
                }
            }
        }
    }

    protected void knockback(LivingEntity target, DamageSource source) {
        float f;
        World world;
        if (this.weapon != null && (world = this.getEntityWorld()) instanceof ServerWorld) {
            ServerWorld lv = (ServerWorld)world;
            f = EnchantmentHelper.modifyKnockback(lv, this.weapon, target, source, 0.0f);
        } else {
            f = 0.0f;
        }
        double d = f;
        if (d > 0.0) {
            double e = Math.max(0.0, 1.0 - target.getAttributeValue(EntityAttributes.KNOCKBACK_RESISTANCE));
            Vec3d lv2 = this.getVelocity().multiply(1.0, 0.0, 1.0).normalize().multiply(d * 0.6 * e);
            if (lv2.lengthSquared() > 0.0) {
                target.addVelocity(lv2.x, 0.1, lv2.z);
            }
        }
    }

    @Override
    protected void onBlockHit(BlockHitResult blockHitResult) {
        this.inBlockState = this.getEntityWorld().getBlockState(blockHitResult.getBlockPos());
        super.onBlockHit(blockHitResult);
        ItemStack lv = this.getWeaponStack();
        World world = this.getEntityWorld();
        if (world instanceof ServerWorld) {
            ServerWorld lv2 = (ServerWorld)world;
            if (lv != null) {
                this.onBlockHitEnchantmentEffects(lv2, blockHitResult, lv);
            }
        }
        Vec3d lv3 = this.getVelocity();
        Vec3d lv4 = new Vec3d(Math.signum(lv3.x), Math.signum(lv3.y), Math.signum(lv3.z));
        Vec3d lv5 = lv4.multiply(0.05f);
        this.setPosition(this.getEntityPos().subtract(lv5));
        this.setVelocity(Vec3d.ZERO);
        this.playSound(this.getSound(), 1.0f, 1.2f / (this.random.nextFloat() * 0.2f + 0.9f));
        this.setInGround(true);
        this.shake = 7;
        this.setCritical(false);
        this.setPierceLevel((byte)0);
        this.setSound(SoundEvents.ENTITY_ARROW_HIT);
        this.clearPiercingStatus();
    }

    protected void onBlockHitEnchantmentEffects(ServerWorld world, BlockHitResult blockHitResult, ItemStack weaponStack) {
        LivingEntity lv2;
        Vec3d lv = blockHitResult.getBlockPos().clampToWithin(blockHitResult.getPos());
        Entity entity = this.getOwner();
        EnchantmentHelper.onHitBlock(world, weaponStack, entity instanceof LivingEntity ? (lv2 = (LivingEntity)entity) : null, this, null, lv, world.getBlockState(blockHitResult.getBlockPos()), item -> {
            this.weapon = null;
        });
    }

    @Override
    public @Nullable ItemStack getWeaponStack() {
        return this.weapon;
    }

    protected SoundEvent getHitSound() {
        return SoundEvents.ENTITY_ARROW_HIT;
    }

    protected final SoundEvent getSound() {
        return this.sound;
    }

    protected void onHit(LivingEntity target) {
    }

    protected @Nullable EntityHitResult getEntityCollision(Vec3d currentPosition, Vec3d nextPosition) {
        return ProjectileUtil.getEntityCollision(this.getEntityWorld(), this, currentPosition, nextPosition, this.getBoundingBox().stretch(this.getVelocity()).expand(1.0), this::canHit);
    }

    protected Collection<EntityHitResult> collectPiercingCollisions(Vec3d from, Vec3d to) {
        return ProjectileUtil.collectPiercingCollisions(this.getEntityWorld(), this, from, to, this.getBoundingBox().stretch(this.getVelocity()).expand(1.0), this::canHit, false);
    }

    @Override
    protected boolean canHit(Entity entity) {
        PlayerEntity lv;
        Entity entity2;
        if (entity instanceof PlayerEntity && (entity2 = this.getOwner()) instanceof PlayerEntity && !(lv = (PlayerEntity)entity2).shouldDamagePlayer((PlayerEntity)entity)) {
            return false;
        }
        return super.canHit(entity) && (this.piercedEntities == null || !this.piercedEntities.contains(entity.getId()));
    }

    @Override
    protected void writeCustomData(WriteView view) {
        super.writeCustomData(view);
        view.putShort("life", (short)this.life);
        view.putNullable("inBlockState", BlockState.CODEC, this.inBlockState);
        view.putByte("shake", (byte)this.shake);
        view.putBoolean("inGround", this.isInGround());
        view.put("pickup", PickupPermission.CODEC, this.pickupType);
        view.putDouble("damage", this.damage);
        view.putBoolean("crit", this.isCritical());
        view.putByte("PierceLevel", this.getPierceLevel());
        view.put("SoundEvent", Registries.SOUND_EVENT.getCodec(), this.sound);
        view.put("item", ItemStack.CODEC, this.stack);
        view.putNullable("weapon", ItemStack.CODEC, this.weapon);
    }

    @Override
    protected void readCustomData(ReadView view) {
        super.readCustomData(view);
        this.life = view.getShort("life", (short)0);
        this.inBlockState = view.read("inBlockState", BlockState.CODEC).orElse(null);
        this.shake = view.getByte("shake", (byte)0) & 0xFF;
        this.setInGround(view.getBoolean("inGround", false));
        this.damage = view.getDouble("damage", 2.0);
        this.pickupType = view.read("pickup", PickupPermission.CODEC).orElse(PickupPermission.DISALLOWED);
        this.setCritical(view.getBoolean("crit", false));
        this.setPierceLevel(view.getByte("PierceLevel", (byte)0));
        this.sound = view.read("SoundEvent", Registries.SOUND_EVENT.getCodec()).orElse(this.getHitSound());
        this.setStack(view.read("item", ItemStack.CODEC).orElse(this.getDefaultItemStack()));
        this.weapon = view.read("weapon", ItemStack.CODEC).orElse(null);
    }

    @Override
    public void setOwner(@Nullable Entity owner) {
        PickupPermission pickupPermission;
        super.setOwner(owner);
        Entity entity = owner;
        int n = 0;
        block4: while (true) {
            switch (SwitchBootstraps.typeSwitch("typeSwitch", new Object[]{PlayerEntity.class, OminousItemSpawnerEntity.class}, (Object)entity, n)) {
                case 0: {
                    PlayerEntity lv = (PlayerEntity)entity;
                    if (this.pickupType != PickupPermission.DISALLOWED) {
                        n = 1;
                        continue block4;
                    }
                    pickupPermission = PickupPermission.ALLOWED;
                    break block4;
                }
                case 1: {
                    OminousItemSpawnerEntity lv2 = (OminousItemSpawnerEntity)entity;
                    pickupPermission = PickupPermission.DISALLOWED;
                    break block4;
                }
                default: {
                    pickupPermission = this.pickupType;
                    break block4;
                }
            }
            break;
        }
        this.pickupType = pickupPermission;
    }

    @Override
    public void onPlayerCollision(PlayerEntity player) {
        if (this.getEntityWorld().isClient() || !this.isInGround() && !this.isNoClip() || this.shake > 0) {
            return;
        }
        if (this.tryPickup(player)) {
            player.sendPickup(this, 1);
            this.discard();
        }
    }

    protected boolean tryPickup(PlayerEntity player) {
        return switch (this.pickupType.ordinal()) {
            default -> throw new MatchException(null, null);
            case 0 -> false;
            case 1 -> player.getInventory().insertStack(this.asItemStack());
            case 2 -> player.isInCreativeMode();
        };
    }

    protected ItemStack asItemStack() {
        return this.stack.copy();
    }

    protected abstract ItemStack getDefaultItemStack();

    @Override
    protected Entity.MoveEffect getMoveEffect() {
        return Entity.MoveEffect.NONE;
    }

    public ItemStack getItemStack() {
        return this.stack;
    }

    public void setDamage(double damage) {
        this.damage = damage;
    }

    @Override
    public boolean isAttackable() {
        return this.getType().isIn(EntityTypeTags.REDIRECTABLE_PROJECTILE);
    }

    public void setCritical(boolean critical) {
        this.setProjectileFlag(CRITICAL_FLAG, critical);
    }

    private void setPierceLevel(byte level) {
        this.dataTracker.set(PIERCE_LEVEL, level);
    }

    private void setProjectileFlag(int index, boolean flag) {
        byte b = this.dataTracker.get(PROJECTILE_FLAGS);
        if (flag) {
            this.dataTracker.set(PROJECTILE_FLAGS, (byte)(b | index));
        } else {
            this.dataTracker.set(PROJECTILE_FLAGS, (byte)(b & ~index));
        }
    }

    protected void setStack(ItemStack stack) {
        this.stack = !stack.isEmpty() ? stack : this.getDefaultItemStack();
    }

    public boolean isCritical() {
        byte b = this.dataTracker.get(PROJECTILE_FLAGS);
        return (b & 1) != 0;
    }

    public byte getPierceLevel() {
        return this.dataTracker.get(PIERCE_LEVEL);
    }

    public void applyDamageModifier(float damageModifier) {
        this.setDamage((double)(damageModifier * 2.0f) + this.random.nextTriangular((double)this.getEntityWorld().getDifficulty().getId() * 0.11, 0.57425));
    }

    protected float getDragInWater() {
        return 0.6f;
    }

    public void setNoClip(boolean noClip) {
        this.noClip = noClip;
        this.setProjectileFlag(NO_CLIP_FLAG, noClip);
    }

    public boolean isNoClip() {
        if (!this.getEntityWorld().isClient()) {
            return this.noClip;
        }
        return (this.dataTracker.get(PROJECTILE_FLAGS) & 2) != 0;
    }

    @Override
    public boolean canHit() {
        return super.canHit() && !this.isInGround();
    }

    @Override
    public @Nullable StackReference getStackReference(int slot) {
        if (slot == 0) {
            return StackReference.of(this::getItemStack, this::setStack);
        }
        return super.getStackReference(slot);
    }

    @Override
    protected boolean deflectsAgainstWorldBorder() {
        return true;
    }

    public static enum PickupPermission {
        DISALLOWED,
        ALLOWED,
        CREATIVE_ONLY;

        public static final Codec<PickupPermission> CODEC;

        public static PickupPermission fromOrdinal(int ordinal) {
            if (ordinal < 0 || ordinal > PickupPermission.values().length) {
                ordinal = 0;
            }
            return PickupPermission.values()[ordinal];
        }

        static {
            CODEC = Codec.BYTE.xmap(PickupPermission::fromOrdinal, arg -> (byte)arg.ordinal());
        }
    }
}

