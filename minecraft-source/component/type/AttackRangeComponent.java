/*
 * External method calls:
 *   Lnet/minecraft/entity/projectile/ProjectileUtil;collectPiercingCollisions(Lnet/minecraft/entity/Entity;Lnet/minecraft/component/type/AttackRangeComponent;Ljava/util/function/Predicate;Lnet/minecraft/world/RaycastContext$ShapeType;)Lcom/mojang/datafixers/util/Either;
 *   Lnet/minecraft/util/hit/BlockHitResult;createMissed(Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/util/math/Direction;Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/util/hit/BlockHitResult;
 *   Lnet/minecraft/util/dynamic/Codecs;rangedInclusiveFloat(FF)Lcom/mojang/serialization/Codec;
 *   Lnet/minecraft/network/codec/PacketCodec;tuple(Lnet/minecraft/network/codec/PacketCodec;Ljava/util/function/Function;Lnet/minecraft/network/codec/PacketCodec;Ljava/util/function/Function;Lnet/minecraft/network/codec/PacketCodec;Ljava/util/function/Function;Lnet/minecraft/network/codec/PacketCodec;Ljava/util/function/Function;Lnet/minecraft/network/codec/PacketCodec;Ljava/util/function/Function;Lnet/minecraft/network/codec/PacketCodec;Ljava/util/function/Function;Lcom/mojang/datafixers/util/Function6;)Lnet/minecraft/network/codec/PacketCodec;
 */
package net.minecraft.component.type;

import com.mojang.datafixers.kinds.Applicative;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import java.util.Collection;
import java.util.function.Predicate;
import java.util.function.ToDoubleFunction;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.util.dynamic.Codecs;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;

public record AttackRangeComponent(float minRange, float maxRange, float minCreativeRange, float maxCreativeRange, float hitboxMargin, float mobFactor) {
    public static final Codec<AttackRangeComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(Codecs.rangedInclusiveFloat(0.0f, 64.0f).optionalFieldOf("min_reach", Float.valueOf(0.0f)).forGetter(AttackRangeComponent::minRange), Codecs.rangedInclusiveFloat(0.0f, 64.0f).optionalFieldOf("max_reach", Float.valueOf(3.0f)).forGetter(AttackRangeComponent::maxRange), Codecs.rangedInclusiveFloat(0.0f, 64.0f).optionalFieldOf("min_creative_reach", Float.valueOf(0.0f)).forGetter(AttackRangeComponent::minCreativeRange), Codecs.rangedInclusiveFloat(0.0f, 64.0f).optionalFieldOf("max_creative_reach", Float.valueOf(5.0f)).forGetter(AttackRangeComponent::maxCreativeRange), Codecs.rangedInclusiveFloat(0.0f, 1.0f).optionalFieldOf("hitbox_margin", Float.valueOf(0.3f)).forGetter(AttackRangeComponent::hitboxMargin), Codec.floatRange(0.0f, 2.0f).optionalFieldOf("mob_factor", Float.valueOf(1.0f)).forGetter(AttackRangeComponent::mobFactor)).apply((Applicative<AttackRangeComponent, ?>)instance, AttackRangeComponent::new));
    public static final PacketCodec<ByteBuf, AttackRangeComponent> PACKET_CODEC = PacketCodec.tuple(PacketCodecs.FLOAT, AttackRangeComponent::minRange, PacketCodecs.FLOAT, AttackRangeComponent::maxRange, PacketCodecs.FLOAT, AttackRangeComponent::minCreativeRange, PacketCodecs.FLOAT, AttackRangeComponent::maxCreativeRange, PacketCodecs.FLOAT, AttackRangeComponent::hitboxMargin, PacketCodecs.FLOAT, AttackRangeComponent::mobFactor, AttackRangeComponent::new);

    public static AttackRangeComponent defaultForEntity(LivingEntity entity) {
        return new AttackRangeComponent(0.0f, (float)entity.getAttributeValue(EntityAttributes.ENTITY_INTERACTION_RANGE), 0.0f, (float)entity.getAttributeValue(EntityAttributes.ENTITY_INTERACTION_RANGE), 0.0f, 1.0f);
    }

    public HitResult getHitResult(Entity entity, float tickProgress, Predicate<Entity> hitPredicate) {
        Either<BlockHitResult, Collection<EntityHitResult>> either = ProjectileUtil.collectPiercingCollisions(entity, this, hitPredicate, RaycastContext.ShapeType.OUTLINE);
        if (either.left().isPresent()) {
            return either.left().get();
        }
        Collection<EntityHitResult> collection = either.right().get();
        EntityHitResult lv = null;
        Vec3d lv2 = entity.getCameraPosVec(tickProgress);
        double d = Double.MAX_VALUE;
        for (EntityHitResult lv3 : collection) {
            double e = lv2.squaredDistanceTo(lv3.getPos());
            if (!(e < d)) continue;
            d = e;
            lv = lv3;
        }
        if (lv != null) {
            return lv;
        }
        Vec3d lv4 = entity.getHeadRotationVector();
        Vec3d lv5 = entity.getCameraPosVec(tickProgress).add(lv4);
        return BlockHitResult.createMissed(lv5, Direction.getFacing(lv4), BlockPos.ofFloored(lv5));
    }

    public float getEffectiveMinRange(Entity entity) {
        if (entity instanceof PlayerEntity) {
            PlayerEntity lv = (PlayerEntity)entity;
            if (lv.isSpectator()) {
                return 0.0f;
            }
            return lv.isCreative() ? this.minCreativeRange : this.minRange;
        }
        return this.minRange * this.mobFactor;
    }

    public float getEffectiveMaxRange(Entity entity) {
        if (entity instanceof PlayerEntity) {
            PlayerEntity lv = (PlayerEntity)entity;
            return lv.isCreative() ? this.maxCreativeRange : this.maxRange;
        }
        return this.maxRange * this.mobFactor;
    }

    public boolean isWithinRange(LivingEntity entity, Vec3d pos) {
        return this.isWithinRange(entity, pos::squaredDistanceTo, 0.0);
    }

    public boolean isWithinRange(LivingEntity entity, Box box, double extraHitboxMargin) {
        return this.isWithinRange(entity, box::squaredMagnitude, extraHitboxMargin);
    }

    private boolean isWithinRange(LivingEntity entity, ToDoubleFunction<Vec3d> squaredDistanceFunction, double extraHitboxMargin) {
        double e = Math.sqrt(squaredDistanceFunction.applyAsDouble(entity.getEyePos()));
        double f = (double)(this.getEffectiveMinRange(entity) - this.hitboxMargin) - extraHitboxMargin;
        double g = (double)(this.getEffectiveMaxRange(entity) + this.hitboxMargin) + extraHitboxMargin;
        return e >= f && e <= g;
    }
}

