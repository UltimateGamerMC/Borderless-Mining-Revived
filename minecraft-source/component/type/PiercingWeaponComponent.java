/*
 * External method calls:
 *   Lnet/minecraft/entity/projectile/ProjectileUtil;collectPiercingCollisions(Lnet/minecraft/entity/Entity;Lnet/minecraft/component/type/AttackRangeComponent;Ljava/util/function/Predicate;Lnet/minecraft/world/RaycastContext$ShapeType;)Lcom/mojang/datafixers/util/Either;
 *   Lnet/minecraft/entity/LivingEntity;pierce(Lnet/minecraft/entity/EquipmentSlot;Lnet/minecraft/entity/Entity;FZZZ)Z
 *   Lnet/minecraft/entity/LivingEntity;swingHand(Lnet/minecraft/util/Hand;Z)V
 *   Lnet/minecraft/world/World;playSound(Lnet/minecraft/entity/Entity;DDDLnet/minecraft/registry/entry/RegistryEntry;Lnet/minecraft/sound/SoundCategory;FF)V
 *   Lnet/minecraft/network/codec/PacketCodec;collect(Lnet/minecraft/network/codec/PacketCodec$ResultFunction;)Lnet/minecraft/network/codec/PacketCodec;
 *   Lnet/minecraft/network/codec/PacketCodec;tuple(Lnet/minecraft/network/codec/PacketCodec;Ljava/util/function/Function;Lnet/minecraft/network/codec/PacketCodec;Ljava/util/function/Function;Lnet/minecraft/network/codec/PacketCodec;Ljava/util/function/Function;Lnet/minecraft/network/codec/PacketCodec;Ljava/util/function/Function;Lcom/mojang/datafixers/util/Function4;)Lnet/minecraft/network/codec/PacketCodec;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/component/type/PiercingWeaponComponent;playHitSound(Lnet/minecraft/entity/Entity;)V
 *   Lnet/minecraft/component/type/PiercingWeaponComponent;playSound(Lnet/minecraft/entity/Entity;)V
 */
package net.minecraft.component.type;

import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.List;
import java.util.Optional;
import net.minecraft.component.type.AttackRangeComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.decoration.InteractionEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.world.RaycastContext;

public record PiercingWeaponComponent(boolean dealsKnockback, boolean dismounts, Optional<RegistryEntry<SoundEvent>> sound, Optional<RegistryEntry<SoundEvent>> hitSound) {
    public static final Codec<PiercingWeaponComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(Codec.BOOL.optionalFieldOf("deals_knockback", true).forGetter(PiercingWeaponComponent::dealsKnockback), Codec.BOOL.optionalFieldOf("dismounts", false).forGetter(PiercingWeaponComponent::dismounts), SoundEvent.ENTRY_CODEC.optionalFieldOf("sound").forGetter(PiercingWeaponComponent::sound), SoundEvent.ENTRY_CODEC.optionalFieldOf("hit_sound").forGetter(PiercingWeaponComponent::hitSound)).apply((Applicative<PiercingWeaponComponent, ?>)instance, PiercingWeaponComponent::new));
    public static final PacketCodec<RegistryByteBuf, PiercingWeaponComponent> PACKET_CODEC = PacketCodec.tuple(PacketCodecs.BOOLEAN, PiercingWeaponComponent::dealsKnockback, PacketCodecs.BOOLEAN, PiercingWeaponComponent::dismounts, SoundEvent.ENTRY_PACKET_CODEC.collect(PacketCodecs::optional), PiercingWeaponComponent::sound, SoundEvent.ENTRY_PACKET_CODEC.collect(PacketCodecs::optional), PiercingWeaponComponent::hitSound, PiercingWeaponComponent::new);

    public void playSound(Entity entity) {
        this.sound.ifPresent(sound -> entity.getEntityWorld().playSound(entity, entity.getX(), entity.getY(), entity.getZ(), (RegistryEntry<SoundEvent>)sound, entity.getSoundCategory(), 1.0f, 1.0f));
    }

    public void playHitSound(Entity entity) {
        this.hitSound.ifPresent(sound -> entity.getEntityWorld().playSound(null, entity.getX(), entity.getY(), entity.getZ(), (RegistryEntry<SoundEvent>)sound, entity.getSoundCategory(), 1.0f, 1.0f));
    }

    public static boolean canHit(Entity attacker, Entity target) {
        if (target.isInvulnerable() || !target.isAlive()) {
            return false;
        }
        if (target instanceof InteractionEntity) {
            return true;
        }
        if (!target.canBeHitByProjectile()) {
            return false;
        }
        if (target instanceof PlayerEntity) {
            PlayerEntity lv2;
            PlayerEntity lv = (PlayerEntity)target;
            if (attacker instanceof PlayerEntity && !(lv2 = (PlayerEntity)attacker).shouldDamagePlayer(lv)) {
                return false;
            }
        }
        return !attacker.isConnectedThroughVehicle(target);
    }

    public void stab(LivingEntity attacker, EquipmentSlot slot) {
        float f = (float)attacker.getAttributeValue(EntityAttributes.ATTACK_DAMAGE);
        AttackRangeComponent lv = attacker.getAttackRange();
        boolean bl = false;
        for (EntityHitResult lv2 : ProjectileUtil.collectPiercingCollisions(attacker, lv, target -> PiercingWeaponComponent.canHit(attacker, target), RaycastContext.ShapeType.COLLIDER).map(blockHit -> List.of(), entityHits -> entityHits)) {
            bl |= attacker.pierce(slot, lv2.getEntity(), f, true, this.dealsKnockback, this.dismounts);
        }
        attacker.beforePlayerAttack();
        attacker.useAttackEnchantmentEffects();
        if (bl) {
            this.playHitSound(attacker);
        }
        this.playSound(attacker);
        attacker.swingHand(Hand.MAIN_HAND, false);
    }
}

