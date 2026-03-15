/*
 * External method calls:
 *   Lnet/minecraft/entity/damage/DamageSources;explosion(Lnet/minecraft/world/explosion/Explosion;)Lnet/minecraft/entity/damage/DamageSource;
 *   Lnet/minecraft/world/World;raycast(Lnet/minecraft/world/RaycastContext;)Lnet/minecraft/util/hit/BlockHitResult;
 *   Lnet/minecraft/entity/Entity;squaredDistanceTo(Lnet/minecraft/util/math/Vec3d;)D
 *   Lnet/minecraft/world/explosion/ExplosionBehavior;calculateDamage(Lnet/minecraft/world/explosion/Explosion;Lnet/minecraft/entity/Entity;F)F
 *   Lnet/minecraft/entity/Entity;damage(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/damage/DamageSource;F)Z
 *   Lnet/minecraft/entity/Entity;addVelocity(Lnet/minecraft/util/math/Vec3d;)V
 *   Lnet/minecraft/entity/Entity;onExplodedBy(Lnet/minecraft/entity/Entity;)V
 *   Lnet/minecraft/util/Util;shuffle(Ljava/util/List;Lnet/minecraft/util/math/random/Random;)V
 *   Lnet/minecraft/block/BlockState;onExploded(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/world/explosion/Explosion;Ljava/util/function/BiConsumer;)V
 *   Lnet/minecraft/block/Block;dropStack(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/item/ItemStack;)V
 *   Lnet/minecraft/server/world/ServerWorld;emitGameEvent(Lnet/minecraft/entity/Entity;Lnet/minecraft/registry/entry/RegistryEntry;Lnet/minecraft/util/math/Vec3d;)V
 *   Lnet/minecraft/util/profiler/Profiler;push(Ljava/lang/String;)V
 *   Lnet/minecraft/world/explosion/ExplosionImpl$DroppedItem;merge(Lnet/minecraft/item/ItemStack;)V
 *
 * Internal private/static methods:
 *   Lnet/minecraft/world/explosion/ExplosionImpl;makeBehavior(Lnet/minecraft/entity/Entity;)Lnet/minecraft/world/explosion/ExplosionBehavior;
 *   Lnet/minecraft/world/explosion/ExplosionImpl;calculateReceivedDamage(Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/entity/Entity;)F
 *   Lnet/minecraft/world/explosion/ExplosionImpl;destroyBlocks(Ljava/util/List;)V
 *   Lnet/minecraft/world/explosion/ExplosionImpl;createFire(Ljava/util/List;)V
 *   Lnet/minecraft/world/explosion/ExplosionImpl;addDroppedItem(Ljava/util/List;Lnet/minecraft/item/ItemStack;Lnet/minecraft/util/math/BlockPos;)V
 */
package net.minecraft.world.explosion;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import net.minecraft.block.AbstractFireBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.TntEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.EntityTypeTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Util;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.util.profiler.Profilers;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import net.minecraft.world.explosion.EntityExplosionBehavior;
import net.minecraft.world.explosion.Explosion;
import net.minecraft.world.explosion.ExplosionBehavior;
import net.minecraft.world.rule.GameRules;
import org.jspecify.annotations.Nullable;

public class ExplosionImpl
implements Explosion {
    private static final ExplosionBehavior DEFAULT_BEHAVIOR = new ExplosionBehavior();
    private static final int field_52618 = 16;
    private static final float field_52619 = 2.0f;
    private final boolean createFire;
    private final Explosion.DestructionType destructionType;
    private final ServerWorld world;
    private final Vec3d pos;
    private final @Nullable Entity entity;
    private final float power;
    private final DamageSource damageSource;
    private final ExplosionBehavior behavior;
    private final Map<PlayerEntity, Vec3d> knockbackByPlayer = new HashMap<PlayerEntity, Vec3d>();

    public ExplosionImpl(ServerWorld world, @Nullable Entity entity, @Nullable DamageSource damageSource, @Nullable ExplosionBehavior behavior, Vec3d pos, float power, boolean createFire, Explosion.DestructionType destructionType) {
        this.world = world;
        this.entity = entity;
        this.power = power;
        this.pos = pos;
        this.createFire = createFire;
        this.destructionType = destructionType;
        this.damageSource = damageSource == null ? world.getDamageSources().explosion(this) : damageSource;
        this.behavior = behavior == null ? this.makeBehavior(entity) : behavior;
    }

    private ExplosionBehavior makeBehavior(@Nullable Entity entity) {
        return entity == null ? DEFAULT_BEHAVIOR : new EntityExplosionBehavior(entity);
    }

    public static float calculateReceivedDamage(Vec3d pos, Entity entity) {
        Box lv = entity.getBoundingBox();
        double d = 1.0 / ((lv.maxX - lv.minX) * 2.0 + 1.0);
        double e = 1.0 / ((lv.maxY - lv.minY) * 2.0 + 1.0);
        double f = 1.0 / ((lv.maxZ - lv.minZ) * 2.0 + 1.0);
        double g = (1.0 - Math.floor(1.0 / d) * d) / 2.0;
        double h = (1.0 - Math.floor(1.0 / f) * f) / 2.0;
        if (d < 0.0 || e < 0.0 || f < 0.0) {
            return 0.0f;
        }
        int i = 0;
        int j = 0;
        for (double k = 0.0; k <= 1.0; k += d) {
            for (double l = 0.0; l <= 1.0; l += e) {
                for (double m = 0.0; m <= 1.0; m += f) {
                    double n = MathHelper.lerp(k, lv.minX, lv.maxX);
                    double o = MathHelper.lerp(l, lv.minY, lv.maxY);
                    double p = MathHelper.lerp(m, lv.minZ, lv.maxZ);
                    Vec3d lv2 = new Vec3d(n + g, o, p + h);
                    if (entity.getEntityWorld().raycast(new RaycastContext(lv2, pos, RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, entity)).getType() == HitResult.Type.MISS) {
                        ++i;
                    }
                    ++j;
                }
            }
        }
        return (float)i / (float)j;
    }

    @Override
    public float getPower() {
        return this.power;
    }

    @Override
    public Vec3d getPosition() {
        return this.pos;
    }

    private List<BlockPos> getBlocksToDestroy() {
        HashSet<BlockPos> set = new HashSet<BlockPos>();
        int i = 16;
        for (int j = 0; j < 16; ++j) {
            for (int k = 0; k < 16; ++k) {
                block2: for (int l = 0; l < 16; ++l) {
                    if (j != 0 && j != 15 && k != 0 && k != 15 && l != 0 && l != 15) continue;
                    double d = (float)j / 15.0f * 2.0f - 1.0f;
                    double e = (float)k / 15.0f * 2.0f - 1.0f;
                    double f = (float)l / 15.0f * 2.0f - 1.0f;
                    double g = Math.sqrt(d * d + e * e + f * f);
                    d /= g;
                    e /= g;
                    f /= g;
                    double m = this.pos.x;
                    double n = this.pos.y;
                    double o = this.pos.z;
                    float p = 0.3f;
                    for (float h = this.power * (0.7f + this.world.random.nextFloat() * 0.6f); h > 0.0f; h -= 0.22500001f) {
                        BlockPos lv = BlockPos.ofFloored(m, n, o);
                        BlockState lv2 = this.world.getBlockState(lv);
                        FluidState lv3 = this.world.getFluidState(lv);
                        if (!this.world.isInBuildLimit(lv)) continue block2;
                        Optional<Float> optional = this.behavior.getBlastResistance(this, this.world, lv, lv2, lv3);
                        if (optional.isPresent()) {
                            h -= (optional.get().floatValue() + 0.3f) * 0.3f;
                        }
                        if (h > 0.0f && this.behavior.canDestroyBlock(this, this.world, lv, lv2, h)) {
                            set.add(lv);
                        }
                        m += d * (double)0.3f;
                        n += e * (double)0.3f;
                        o += f * (double)0.3f;
                    }
                }
            }
        }
        return new ObjectArrayList<BlockPos>(set);
    }

    private void damageEntities() {
        if (this.power < 1.0E-5f) {
            return;
        }
        float f = this.power * 2.0f;
        int i = MathHelper.floor(this.pos.x - (double)f - 1.0);
        int j = MathHelper.floor(this.pos.x + (double)f + 1.0);
        int k = MathHelper.floor(this.pos.y - (double)f - 1.0);
        int l = MathHelper.floor(this.pos.y + (double)f + 1.0);
        int m = MathHelper.floor(this.pos.z - (double)f - 1.0);
        int n = MathHelper.floor(this.pos.z + (double)f + 1.0);
        List<Entity> list = this.world.getOtherEntities(this.entity, new Box(i, k, m, j, l, n));
        for (Entity lv : list) {
            PlayerEntity lv7;
            double d;
            float h;
            double d2;
            if (lv.isImmuneToExplosion(this) || (d2 = Math.sqrt(lv.squaredDistanceTo(this.pos)) / (double)f) > 1.0) continue;
            Vec3d lv2 = lv instanceof TntEntity ? lv.getEntityPos() : lv.getEyePos();
            Vec3d lv3 = lv2.subtract(this.pos).normalize();
            boolean bl = this.behavior.shouldDamage(this, lv);
            float g = this.behavior.getKnockbackModifier(lv);
            float f2 = h = bl || g != 0.0f ? ExplosionImpl.calculateReceivedDamage(this.pos, lv) : 0.0f;
            if (bl) {
                lv.damage(this.world, this.damageSource, this.behavior.calculateDamage(this, lv, h));
            }
            if (lv instanceof LivingEntity) {
                LivingEntity lv4 = (LivingEntity)lv;
                d = lv4.getAttributeValue(EntityAttributes.EXPLOSION_KNOCKBACK_RESISTANCE);
            } else {
                d = 0.0;
            }
            double e = d;
            double o = (1.0 - d2) * (double)h * (double)g * (1.0 - e);
            Vec3d lv5 = lv3.multiply(o);
            lv.addVelocity(lv5);
            if (lv.getType().isIn(EntityTypeTags.REDIRECTABLE_PROJECTILE) && lv instanceof ProjectileEntity) {
                ProjectileEntity lv6 = (ProjectileEntity)lv;
                lv6.setOwner(this.damageSource.getAttacker());
            } else if (!(!(lv instanceof PlayerEntity) || (lv7 = (PlayerEntity)lv).isSpectator() || lv7.isCreative() && lv7.getAbilities().flying)) {
                this.knockbackByPlayer.put(lv7, lv5);
            }
            lv.onExplodedBy(this.entity);
        }
    }

    private void destroyBlocks(List<BlockPos> positions) {
        ArrayList list2 = new ArrayList();
        Util.shuffle(positions, this.world.random);
        for (BlockPos lv : positions) {
            this.world.getBlockState(lv).onExploded(this.world, lv, this, (item, pos) -> ExplosionImpl.addDroppedItem(list2, item, pos));
        }
        for (DroppedItem lv2 : list2) {
            Block.dropStack((World)this.world, lv2.pos, lv2.item);
        }
    }

    private void createFire(List<BlockPos> positions) {
        for (BlockPos lv : positions) {
            if (this.world.random.nextInt(3) != 0 || !this.world.getBlockState(lv).isAir() || !this.world.getBlockState(lv.down()).isOpaqueFullCube()) continue;
            this.world.setBlockState(lv, AbstractFireBlock.getState(this.world, lv));
        }
    }

    public int explode() {
        this.world.emitGameEvent(this.entity, GameEvent.EXPLODE, this.pos);
        List<BlockPos> list = this.getBlocksToDestroy();
        this.damageEntities();
        if (this.shouldDestroyBlocks()) {
            Profiler lv = Profilers.get();
            lv.push("explosion_blocks");
            this.destroyBlocks(list);
            lv.pop();
        }
        if (this.createFire) {
            this.createFire(list);
        }
        return list.size();
    }

    private static void addDroppedItem(List<DroppedItem> droppedItemsOut, ItemStack item, BlockPos pos) {
        for (DroppedItem lv : droppedItemsOut) {
            lv.merge(item);
            if (!item.isEmpty()) continue;
            return;
        }
        droppedItemsOut.add(new DroppedItem(pos, item));
    }

    private boolean shouldDestroyBlocks() {
        return this.destructionType != Explosion.DestructionType.KEEP;
    }

    public Map<PlayerEntity, Vec3d> getKnockbackByPlayer() {
        return this.knockbackByPlayer;
    }

    @Override
    public ServerWorld getWorld() {
        return this.world;
    }

    @Override
    public @Nullable LivingEntity getCausingEntity() {
        return Explosion.getCausingEntity(this.entity);
    }

    @Override
    public @Nullable Entity getEntity() {
        return this.entity;
    }

    public DamageSource getDamageSource() {
        return this.damageSource;
    }

    @Override
    public Explosion.DestructionType getDestructionType() {
        return this.destructionType;
    }

    @Override
    public boolean canTriggerBlocks() {
        if (this.destructionType != Explosion.DestructionType.TRIGGER_BLOCK) {
            return false;
        }
        if (this.entity != null && this.entity.getType() == EntityType.BREEZE_WIND_CHARGE) {
            return this.world.getGameRules().getValue(GameRules.DO_MOB_GRIEFING);
        }
        return true;
    }

    @Override
    public boolean preservesDecorativeEntities() {
        boolean bl2;
        boolean bl = this.world.getGameRules().getValue(GameRules.DO_MOB_GRIEFING);
        boolean bl3 = bl2 = this.entity == null || this.entity.getType() != EntityType.BREEZE_WIND_CHARGE && this.entity.getType() != EntityType.WIND_CHARGE;
        if (bl) {
            return bl2;
        }
        return this.destructionType.destroysBlocks() && bl2;
    }

    public boolean isSmall() {
        return this.power < 2.0f || !this.shouldDestroyBlocks();
    }

    static class DroppedItem {
        final BlockPos pos;
        ItemStack item;

        DroppedItem(BlockPos pos, ItemStack item) {
            this.pos = pos;
            this.item = item;
        }

        public void merge(ItemStack other) {
            if (ItemEntity.canMerge(this.item, other)) {
                this.item = ItemEntity.merge(this.item, other, 16);
            }
        }
    }
}

