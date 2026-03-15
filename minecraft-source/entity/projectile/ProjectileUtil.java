/*
 * External method calls:
 *   Lnet/minecraft/item/ArrowItem;createArrow(Lnet/minecraft/world/World;Lnet/minecraft/item/ItemStack;Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/item/ItemStack;)Lnet/minecraft/entity/projectile/PersistentProjectileEntity;
 *   Lnet/minecraft/entity/projectile/PersistentProjectileEntity;applyDamageModifier(F)V
 *
 * Internal private/static methods:
 *   Lnet/minecraft/entity/projectile/ProjectileUtil;collectPiercingCollisions(Lnet/minecraft/entity/Entity;Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/util/math/Vec3d;Ljava/util/function/Predicate;Lnet/minecraft/util/math/Vec3d;FLnet/minecraft/world/RaycastContext$ShapeType;)Lcom/mojang/datafixers/util/Either;
 *   Lnet/minecraft/entity/projectile/ProjectileUtil;collectPiercingCollisions(Lnet/minecraft/world/World;Lnet/minecraft/entity/Entity;Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/util/math/Box;Ljava/util/function/Predicate;FLnet/minecraft/world/RaycastContext$ShapeType;Z)Ljava/util/Collection;
 */
package net.minecraft.entity.projectile;

import com.mojang.datafixers.util.Either;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.function.Predicate;
import net.minecraft.component.type.AttackRangeComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.ArrowItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import org.jspecify.annotations.Nullable;

public final class ProjectileUtil {
    public static final float DEFAULT_MARGIN = 0.3f;

    public static HitResult getCollision(Entity entity, Predicate<Entity> predicate) {
        Vec3d lv = entity.getVelocity();
        World lv2 = entity.getEntityWorld();
        Vec3d lv3 = entity.getEntityPos();
        return ProjectileUtil.getCollision(lv3, entity, predicate, lv, lv2, ProjectileUtil.getToleranceMargin(entity), RaycastContext.ShapeType.COLLIDER);
    }

    public static Either<BlockHitResult, Collection<EntityHitResult>> collectPiercingCollisions(Entity entity, AttackRangeComponent attackRange, Predicate<Entity> hitPredicate, RaycastContext.ShapeType shapeType) {
        Vec3d lv = entity.getHeadRotationVector();
        Vec3d lv2 = entity.getEyePos();
        Vec3d lv3 = lv2.add(lv.multiply(attackRange.getEffectiveMinRange(entity)));
        double d = entity.getMovement().dotProduct(lv);
        Vec3d lv4 = lv2.add(lv.multiply((double)attackRange.getEffectiveMaxRange(entity) + Math.max(0.0, d)));
        return ProjectileUtil.collectPiercingCollisions(entity, lv2, lv3, hitPredicate, lv4, attackRange.hitboxMargin(), shapeType);
    }

    public static HitResult getCollision(Entity entity, Predicate<Entity> predicate, RaycastContext.ShapeType raycastShapeType) {
        Vec3d lv = entity.getVelocity();
        World lv2 = entity.getEntityWorld();
        Vec3d lv3 = entity.getEntityPos();
        return ProjectileUtil.getCollision(lv3, entity, predicate, lv, lv2, ProjectileUtil.getToleranceMargin(entity), raycastShapeType);
    }

    public static HitResult getCollision(Entity entity, Predicate<Entity> predicate, double range) {
        Vec3d lv = entity.getRotationVec(0.0f).multiply(range);
        World lv2 = entity.getEntityWorld();
        Vec3d lv3 = entity.getEyePos();
        return ProjectileUtil.getCollision(lv3, entity, predicate, lv, lv2, 0.0f, RaycastContext.ShapeType.COLLIDER);
    }

    private static HitResult getCollision(Vec3d pos, Entity entity, Predicate<Entity> predicate, Vec3d velocity, World world, float margin, RaycastContext.ShapeType raycastShapeType) {
        EntityHitResult lv3;
        Vec3d lv = pos.add(velocity);
        HitResult lv2 = world.getCollisionsIncludingWorldBorder(new RaycastContext(pos, lv, raycastShapeType, RaycastContext.FluidHandling.NONE, entity));
        if (((HitResult)lv2).getType() != HitResult.Type.MISS) {
            lv = lv2.getPos();
        }
        if ((lv3 = ProjectileUtil.getEntityCollision(world, entity, pos, lv, entity.getBoundingBox().stretch(velocity).expand(1.0), predicate, margin)) != null) {
            lv2 = lv3;
        }
        return lv2;
    }

    private static Either<BlockHitResult, Collection<EntityHitResult>> collectPiercingCollisions(Entity entity, Vec3d pos, Vec3d minReach, Predicate<Entity> hitPredicate, Vec3d maxReach, float hitboxMargin, RaycastContext.ShapeType shapeType) {
        World lv = entity.getEntityWorld();
        BlockHitResult lv2 = lv.getCollisionsIncludingWorldBorder(new RaycastContext(pos, maxReach, shapeType, RaycastContext.FluidHandling.NONE, entity));
        if (lv2.getType() != HitResult.Type.MISS && pos.squaredDistanceTo(maxReach = lv2.getPos()) < pos.squaredDistanceTo(minReach)) {
            return Either.left(lv2);
        }
        Box lv3 = Box.of(minReach, hitboxMargin, hitboxMargin, hitboxMargin).stretch(maxReach.subtract(minReach)).expand(1.0);
        Collection<EntityHitResult> collection = ProjectileUtil.collectPiercingCollisions(lv, entity, minReach, maxReach, lv3, hitPredicate, hitboxMargin, shapeType, true);
        if (!collection.isEmpty()) {
            return Either.right(collection);
        }
        return Either.left(lv2);
    }

    public static @Nullable EntityHitResult raycast(Entity entity, Vec3d min, Vec3d max, Box box, Predicate<Entity> predicate, double maxDistance) {
        World lv = entity.getEntityWorld();
        double e = maxDistance;
        Entity lv2 = null;
        Vec3d lv3 = null;
        for (Entity lv4 : lv.getOtherEntities(entity, box, predicate)) {
            Vec3d lv6;
            double f;
            Box lv5 = lv4.getBoundingBox().expand(lv4.getTargetingMargin());
            Optional<Vec3d> optional = lv5.raycast(min, max);
            if (lv5.contains(min)) {
                if (!(e >= 0.0)) continue;
                lv2 = lv4;
                lv3 = optional.orElse(min);
                e = 0.0;
                continue;
            }
            if (!optional.isPresent() || !((f = min.squaredDistanceTo(lv6 = optional.get())) < e) && e != 0.0) continue;
            if (lv4.getRootVehicle() == entity.getRootVehicle()) {
                if (e != 0.0) continue;
                lv2 = lv4;
                lv3 = lv6;
                continue;
            }
            lv2 = lv4;
            lv3 = lv6;
            e = f;
        }
        if (lv2 == null) {
            return null;
        }
        return new EntityHitResult(lv2, lv3);
    }

    public static @Nullable EntityHitResult getEntityCollision(World world, ProjectileEntity projectile, Vec3d min, Vec3d max, Box box, Predicate<Entity> predicate) {
        return ProjectileUtil.getEntityCollision(world, projectile, min, max, box, predicate, ProjectileUtil.getToleranceMargin(projectile));
    }

    public static float getToleranceMargin(Entity entity) {
        return Math.max(0.0f, Math.min(0.3f, (float)(entity.age - 2) / 20.0f));
    }

    public static @Nullable EntityHitResult getEntityCollision(World world, Entity entity, Vec3d min, Vec3d max, Box box, Predicate<Entity> predicate, float margin) {
        double d = Double.MAX_VALUE;
        Optional<Object> optional = Optional.empty();
        Entity lv = null;
        for (Entity lv2 : world.getOtherEntities(entity, box, predicate)) {
            double e;
            Box lv3 = lv2.getBoundingBox().expand(margin);
            Optional<Vec3d> optional2 = lv3.raycast(min, max);
            if (!optional2.isPresent() || !((e = min.squaredDistanceTo(optional2.get())) < d)) continue;
            lv = lv2;
            d = e;
            optional = optional2;
        }
        if (lv == null) {
            return null;
        }
        return new EntityHitResult(lv, (Vec3d)optional.get());
    }

    public static Collection<EntityHitResult> collectPiercingCollisions(World world, Entity entity, Vec3d from, Vec3d to, Box box, Predicate<Entity> hitPredicate, boolean skipRaycast) {
        return ProjectileUtil.collectPiercingCollisions(world, entity, from, to, box, hitPredicate, ProjectileUtil.getToleranceMargin(entity), RaycastContext.ShapeType.COLLIDER, skipRaycast);
    }

    public static Collection<EntityHitResult> collectPiercingCollisions(World world, Entity entity, Vec3d from, Vec3d to, Box box, Predicate<Entity> hitPredicate, float hitboxMargin, RaycastContext.ShapeType arg6, boolean bl) {
        ArrayList<EntityHitResult> list = new ArrayList<EntityHitResult>();
        for (Entity lv : world.getOtherEntities(entity, box, hitPredicate)) {
            Optional<Vec3d> optional3;
            Vec3d lv4;
            Optional<Vec3d> optional2;
            Box lv2 = lv.getBoundingBox();
            if (bl && lv2.contains(from)) {
                list.add(new EntityHitResult(lv, from));
                continue;
            }
            Optional<Vec3d> optional = lv2.raycast(from, to);
            if (optional.isPresent()) {
                list.add(new EntityHitResult(lv, optional.get()));
                continue;
            }
            if ((double)hitboxMargin <= 0.0 || (optional2 = lv2.expand(hitboxMargin).raycast(from, to)).isEmpty()) continue;
            Vec3d lv3 = optional2.get();
            BlockHitResult lv5 = world.getCollisionsIncludingWorldBorder(new RaycastContext(lv3, lv4 = lv2.getCenter(), arg6, RaycastContext.FluidHandling.NONE, entity));
            if (lv5.getType() != HitResult.Type.MISS) {
                lv4 = lv5.getPos();
            }
            if (!(optional3 = lv.getBoundingBox().raycast(lv3, lv4)).isPresent()) continue;
            list.add(new EntityHitResult(lv, optional3.get()));
        }
        return list;
    }

    public static void setRotationFromVelocity(Entity entity, float tickProgress) {
        Vec3d lv = entity.getVelocity();
        if (lv.lengthSquared() == 0.0) {
            return;
        }
        double d = lv.horizontalLength();
        entity.setYaw((float)(MathHelper.atan2(lv.z, lv.x) * 57.2957763671875) + 90.0f);
        entity.setPitch((float)(MathHelper.atan2(d, lv.y) * 57.2957763671875) - 90.0f);
        while (entity.getPitch() - entity.lastPitch < -180.0f) {
            entity.lastPitch -= 360.0f;
        }
        while (entity.getPitch() - entity.lastPitch >= 180.0f) {
            entity.lastPitch += 360.0f;
        }
        while (entity.getYaw() - entity.lastYaw < -180.0f) {
            entity.lastYaw -= 360.0f;
        }
        while (entity.getYaw() - entity.lastYaw >= 180.0f) {
            entity.lastYaw += 360.0f;
        }
        entity.setPitch(MathHelper.lerp(tickProgress, entity.lastPitch, entity.getPitch()));
        entity.setYaw(MathHelper.lerp(tickProgress, entity.lastYaw, entity.getYaw()));
    }

    public static Hand getHandPossiblyHolding(LivingEntity entity, Item item) {
        return entity.getMainHandStack().isOf(item) ? Hand.MAIN_HAND : Hand.OFF_HAND;
    }

    public static PersistentProjectileEntity createArrowProjectile(LivingEntity entity, ItemStack stack, float damageModifier, @Nullable ItemStack bow) {
        ArrowItem lv = (ArrowItem)(stack.getItem() instanceof ArrowItem ? stack.getItem() : Items.ARROW);
        PersistentProjectileEntity lv2 = lv.createArrow(entity.getEntityWorld(), stack, entity, bow);
        lv2.applyDamageModifier(damageModifier);
        return lv2;
    }
}

