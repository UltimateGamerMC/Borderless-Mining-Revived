/*
 * External method calls:
 *   Lnet/minecraft/entity/projectile/ProjectileUtil;collectPiercingCollisions(Lnet/minecraft/entity/Entity;Lnet/minecraft/component/type/AttackRangeComponent;Ljava/util/function/Predicate;Lnet/minecraft/world/RaycastContext$ShapeType;)Lcom/mojang/datafixers/util/Either;
 *   Lnet/minecraft/entity/LivingEntity;startPiercingCooldown(Lnet/minecraft/entity/Entity;)V
 *   Lnet/minecraft/entity/LivingEntity;pierce(Lnet/minecraft/entity/EquipmentSlot;Lnet/minecraft/entity/Entity;FZZZ)Z
 *   Lnet/minecraft/world/World;sendEntityStatus(Lnet/minecraft/entity/Entity;B)V
 *   Lnet/minecraft/advancement/criterion/SpearMobsCriterion;trigger(Lnet/minecraft/server/network/ServerPlayerEntity;I)V
 *   Lnet/minecraft/world/World;playSoundFromEntityClient(Lnet/minecraft/entity/Entity;Lnet/minecraft/sound/SoundEvent;Lnet/minecraft/sound/SoundCategory;FF)V
 *   Lnet/minecraft/world/World;playSound(Lnet/minecraft/entity/Entity;DDDLnet/minecraft/registry/entry/RegistryEntry;Lnet/minecraft/sound/SoundCategory;FF)V
 *   Lnet/minecraft/network/codec/PacketCodec;collect(Lnet/minecraft/network/codec/PacketCodec$ResultFunction;)Lnet/minecraft/network/codec/PacketCodec;
 *   Lnet/minecraft/network/codec/PacketCodec;tuple(Lnet/minecraft/network/codec/PacketCodec;Ljava/util/function/Function;Lnet/minecraft/network/codec/PacketCodec;Ljava/util/function/Function;Lnet/minecraft/network/codec/PacketCodec;Ljava/util/function/Function;Lnet/minecraft/network/codec/PacketCodec;Ljava/util/function/Function;Lnet/minecraft/network/codec/PacketCodec;Ljava/util/function/Function;Lnet/minecraft/network/codec/PacketCodec;Ljava/util/function/Function;Lnet/minecraft/network/codec/PacketCodec;Ljava/util/function/Function;Lnet/minecraft/network/codec/PacketCodec;Ljava/util/function/Function;Lnet/minecraft/network/codec/PacketCodec;Ljava/util/function/Function;Lcom/mojang/datafixers/util/Function9;)Lnet/minecraft/network/codec/PacketCodec;
 */
package net.minecraft.component.type;

import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import java.util.List;
import java.util.Optional;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.component.type.AttackRangeComponent;
import net.minecraft.component.type.PiercingWeaponComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityStatuses;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.boss.dragon.EnderDragonPart;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.dynamic.Codecs;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;

public record KineticWeaponComponent(int contactCooldownTicks, int delayTicks, Optional<Condition> dismountConditions, Optional<Condition> knockbackConditions, Optional<Condition> damageConditions, float forwardMovement, float damageMultiplier, Optional<RegistryEntry<SoundEvent>> sound, Optional<RegistryEntry<SoundEvent>> hitSound) {
    public static final int field_64687 = 10;
    public static final Codec<KineticWeaponComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(Codecs.NON_NEGATIVE_INT.optionalFieldOf("contact_cooldown_ticks", 10).forGetter(KineticWeaponComponent::contactCooldownTicks), Codecs.NON_NEGATIVE_INT.optionalFieldOf("delay_ticks", 0).forGetter(KineticWeaponComponent::delayTicks), Condition.CODEC.optionalFieldOf("dismount_conditions").forGetter(KineticWeaponComponent::dismountConditions), Condition.CODEC.optionalFieldOf("knockback_conditions").forGetter(KineticWeaponComponent::knockbackConditions), Condition.CODEC.optionalFieldOf("damage_conditions").forGetter(KineticWeaponComponent::damageConditions), Codec.FLOAT.optionalFieldOf("forward_movement", Float.valueOf(0.0f)).forGetter(KineticWeaponComponent::forwardMovement), Codec.FLOAT.optionalFieldOf("damage_multiplier", Float.valueOf(1.0f)).forGetter(KineticWeaponComponent::damageMultiplier), SoundEvent.ENTRY_CODEC.optionalFieldOf("sound").forGetter(KineticWeaponComponent::sound), SoundEvent.ENTRY_CODEC.optionalFieldOf("hit_sound").forGetter(KineticWeaponComponent::hitSound)).apply((Applicative<KineticWeaponComponent, ?>)instance, KineticWeaponComponent::new));
    public static final PacketCodec<RegistryByteBuf, KineticWeaponComponent> PACKET_CODEC = PacketCodec.tuple(PacketCodecs.VAR_INT, KineticWeaponComponent::contactCooldownTicks, PacketCodecs.VAR_INT, KineticWeaponComponent::delayTicks, Condition.PACKET_CODEC.collect(PacketCodecs::optional), KineticWeaponComponent::dismountConditions, Condition.PACKET_CODEC.collect(PacketCodecs::optional), KineticWeaponComponent::knockbackConditions, Condition.PACKET_CODEC.collect(PacketCodecs::optional), KineticWeaponComponent::damageConditions, PacketCodecs.FLOAT, KineticWeaponComponent::forwardMovement, PacketCodecs.FLOAT, KineticWeaponComponent::damageMultiplier, SoundEvent.ENTRY_PACKET_CODEC.collect(PacketCodecs::optional), KineticWeaponComponent::sound, SoundEvent.ENTRY_PACKET_CODEC.collect(PacketCodecs::optional), KineticWeaponComponent::hitSound, KineticWeaponComponent::new);

    public static Vec3d getAmplifiedMovement(Entity entity) {
        if (!(entity instanceof PlayerEntity) && entity.hasVehicle()) {
            entity = entity.getRootVehicle();
        }
        return entity.getKineticAttackMovement().multiply(20.0);
    }

    public void playSound(Entity entity) {
        this.sound.ifPresent(sound -> entity.getEntityWorld().playSound(entity, entity.getX(), entity.getY(), entity.getZ(), (RegistryEntry<SoundEvent>)sound, entity.getSoundCategory(), 1.0f, 1.0f));
    }

    public void playHitSound(Entity entity) {
        this.hitSound.ifPresent(hitSound -> entity.getEntityWorld().playSoundFromEntityClient(entity, (SoundEvent)hitSound.value(), entity.getSoundCategory(), 1.0f, 1.0f));
    }

    public int getUseTicks() {
        return this.delayTicks + this.damageConditions.map(Condition::maxDurationTicks).orElse(0);
    }

    public void usageTick(ItemStack stack, int remainingUseTicks, LivingEntity user, EquipmentSlot slot) {
        int j = stack.getMaxUseTime(user) - remainingUseTicks;
        if (j < this.delayTicks) {
            return;
        }
        j -= this.delayTicks;
        Vec3d lv = user.getRotationVector();
        double d = lv.dotProduct(KineticWeaponComponent.getAmplifiedMovement(user));
        float f = user instanceof PlayerEntity ? 1.0f : 0.2f;
        AttackRangeComponent lv2 = user.getAttackRange();
        double e = user.getAttributeBaseValue(EntityAttributes.ATTACK_DAMAGE);
        boolean bl = false;
        for (EntityHitResult lv3 : ProjectileUtil.collectPiercingCollisions(user, lv2, target -> PiercingWeaponComponent.canHit(user, target), RaycastContext.ShapeType.COLLIDER).map(blockHit -> List.of(), entityHits -> entityHits)) {
            boolean bl5;
            boolean bl2;
            Entity lv4 = lv3.getEntity();
            if (lv4 instanceof EnderDragonPart) {
                EnderDragonPart lv5 = (EnderDragonPart)lv4;
                lv4 = lv5.owner;
            }
            if (bl2 = user.isInPiercingCooldown(lv4, this.contactCooldownTicks)) continue;
            user.startPiercingCooldown(lv4);
            double g = lv.dotProduct(KineticWeaponComponent.getAmplifiedMovement(lv4));
            double h = Math.max(0.0, d - g);
            boolean bl3 = this.dismountConditions.isPresent() && this.dismountConditions.get().isSatisfied(j, d, h, f);
            boolean bl4 = this.knockbackConditions.isPresent() && this.knockbackConditions.get().isSatisfied(j, d, h, f);
            boolean bl6 = bl5 = this.damageConditions.isPresent() && this.damageConditions.get().isSatisfied(j, d, h, f);
            if (!bl3 && !bl4 && !bl5) continue;
            float k = (float)e + (float)MathHelper.floor(h * (double)this.damageMultiplier);
            bl |= user.pierce(slot, lv4, k, bl5, bl4, bl3);
        }
        if (bl) {
            user.getEntityWorld().sendEntityStatus(user, EntityStatuses.KINETIC_ATTACK);
            if (user instanceof ServerPlayerEntity) {
                ServerPlayerEntity lv6 = (ServerPlayerEntity)user;
                Criteria.SPEAR_MOBS.trigger(lv6, user.getPiercedEntityCount(entity -> entity instanceof LivingEntity));
            }
        }
    }

    public record Condition(int maxDurationTicks, float minSpeed, float minRelativeSpeed) {
        public static final Codec<Condition> CODEC = RecordCodecBuilder.create(instance -> instance.group(((MapCodec)Codecs.NON_NEGATIVE_INT.fieldOf("max_duration_ticks")).forGetter(Condition::maxDurationTicks), Codec.FLOAT.optionalFieldOf("min_speed", Float.valueOf(0.0f)).forGetter(Condition::minSpeed), Codec.FLOAT.optionalFieldOf("min_relative_speed", Float.valueOf(0.0f)).forGetter(Condition::minRelativeSpeed)).apply((Applicative<Condition, ?>)instance, Condition::new));
        public static final PacketCodec<ByteBuf, Condition> PACKET_CODEC = PacketCodec.tuple(PacketCodecs.VAR_INT, Condition::maxDurationTicks, PacketCodecs.FLOAT, Condition::minSpeed, PacketCodecs.FLOAT, Condition::minRelativeSpeed, Condition::new);

        public boolean isSatisfied(int durationTicks, double speed, double relativeSpeed, double minSpeedMultiplier) {
            return durationTicks <= this.maxDurationTicks && speed >= (double)this.minSpeed * minSpeedMultiplier && relativeSpeed >= (double)this.minRelativeSpeed * minSpeedMultiplier;
        }

        public static Optional<Condition> ofMinSpeed(int maxDurationTicks, float minSpeed) {
            return Optional.of(new Condition(maxDurationTicks, minSpeed, 0.0f));
        }

        public static Optional<Condition> ofMinRelativeSpeed(int maxDurationTicks, float minRelativeSpeed) {
            return Optional.of(new Condition(maxDurationTicks, 0.0f, minRelativeSpeed));
        }
    }
}

