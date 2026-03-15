/*
 * External method calls:
 *   Lnet/minecraft/component/type/ItemEnchantmentsComponent$Builder;build()Lnet/minecraft/component/type/ItemEnchantmentsComponent;
 *   Lnet/minecraft/enchantment/EnchantmentLevelEntry;enchantment()Lnet/minecraft/registry/entry/RegistryEntry;
 *   Lnet/minecraft/item/ItemStack;addEnchantment(Lnet/minecraft/registry/entry/RegistryEntry;I)V
 *   Lnet/minecraft/enchantment/EnchantmentHelper$Consumer;accept(Lnet/minecraft/registry/entry/RegistryEntry;I)V
 *   Lnet/minecraft/enchantment/Enchantment;slotMatches(Lnet/minecraft/entity/EquipmentSlot;)Z
 *   Lnet/minecraft/enchantment/EnchantmentHelper$ContextAwareConsumer;accept(Lnet/minecraft/registry/entry/RegistryEntry;ILnet/minecraft/enchantment/EnchantmentEffectContext;)V
 *   Lnet/minecraft/enchantment/Enchantment;effects()Lnet/minecraft/component/ComponentMap;
 *   Lnet/minecraft/enchantment/provider/EnchantmentProvider;provideEnchantments(Lnet/minecraft/item/ItemStack;Lnet/minecraft/component/type/ItemEnchantmentsComponent$Builder;Lnet/minecraft/util/math/random/Random;Lnet/minecraft/world/LocalDifficulty;)V
 *   Lnet/minecraft/registry/Registry;streamEntries()Ljava/util/stream/Stream;
 *   Lnet/minecraft/enchantment/Enchantment;modifyTridentSpinAttackStrength(Lnet/minecraft/util/math/random/Random;ILorg/apache/commons/lang3/mutable/MutableFloat;)V
 *   Lnet/minecraft/enchantment/Enchantment;modifyCrossbowChargeTime(Lnet/minecraft/util/math/random/Random;ILorg/apache/commons/lang3/mutable/MutableFloat;)V
 *   Lnet/minecraft/enchantment/Enchantment;modifyTridentReturnAcceleration(Lnet/minecraft/server/world/ServerWorld;ILnet/minecraft/item/ItemStack;Lnet/minecraft/entity/Entity;Lorg/apache/commons/lang3/mutable/MutableFloat;)V
 *   Lnet/minecraft/enchantment/Enchantment;modifyFishingTimeReduction(Lnet/minecraft/server/world/ServerWorld;ILnet/minecraft/item/ItemStack;Lnet/minecraft/entity/Entity;Lorg/apache/commons/lang3/mutable/MutableFloat;)V
 *   Lnet/minecraft/enchantment/Enchantment;modifyFishingLuckBonus(Lnet/minecraft/server/world/ServerWorld;ILnet/minecraft/item/ItemStack;Lnet/minecraft/entity/Entity;Lorg/apache/commons/lang3/mutable/MutableFloat;)V
 *   Lnet/minecraft/enchantment/effect/AttributeEnchantmentEffect;attribute()Lnet/minecraft/registry/entry/RegistryEntry;
 *   Lnet/minecraft/enchantment/effect/AttributeEnchantmentEffect;createAttributeModifier(ILnet/minecraft/util/StringIdentifiable;)Lnet/minecraft/entity/attribute/EntityAttributeModifier;
 *   Lnet/minecraft/enchantment/Enchantment;definition()Lnet/minecraft/enchantment/Enchantment$Definition;
 *   Lnet/minecraft/enchantment/Enchantment$Definition;slots()Ljava/util/List;
 *   Lnet/minecraft/enchantment/Enchantment;createEnchantedDamageLootContext(Lnet/minecraft/server/world/ServerWorld;ILnet/minecraft/entity/Entity;Lnet/minecraft/entity/damage/DamageSource;)Lnet/minecraft/loot/context/LootContext;
 *   Lnet/minecraft/enchantment/effect/TargetedEnchantmentEffect;enchanted()Lnet/minecraft/enchantment/effect/EnchantmentEffectTarget;
 *   Lnet/minecraft/enchantment/effect/TargetedEnchantmentEffect;affected()Lnet/minecraft/enchantment/effect/EnchantmentEffectTarget;
 *   Lnet/minecraft/enchantment/effect/TargetedEnchantmentEffect;test(Lnet/minecraft/loot/context/LootContext;)Z
 *   Lnet/minecraft/enchantment/effect/TargetedEnchantmentEffect;effect()Ljava/lang/Object;
 *   Lnet/minecraft/enchantment/effect/EnchantmentValueEffect;apply(ILnet/minecraft/util/math/random/Random;F)F
 *   Lnet/minecraft/enchantment/Enchantment;modifyRepairWithExperience(Lnet/minecraft/server/world/ServerWorld;ILnet/minecraft/item/ItemStack;Lorg/apache/commons/lang3/mutable/MutableFloat;)V
 *   Lnet/minecraft/enchantment/Enchantment;onHitBlock(Lnet/minecraft/server/world/ServerWorld;ILnet/minecraft/enchantment/EnchantmentEffectContext;Lnet/minecraft/entity/Entity;Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/block/BlockState;)V
 *   Lnet/minecraft/enchantment/Enchantment;onProjectileSpawned(Lnet/minecraft/server/world/ServerWorld;ILnet/minecraft/enchantment/EnchantmentEffectContext;Lnet/minecraft/entity/Entity;)V
 *   Lnet/minecraft/enchantment/Enchantment;modifyProjectilePiercing(Lnet/minecraft/server/world/ServerWorld;ILnet/minecraft/item/ItemStack;Lorg/apache/commons/lang3/mutable/MutableFloat;)V
 *   Lnet/minecraft/enchantment/Enchantment;modifyProjectileSpread(Lnet/minecraft/server/world/ServerWorld;ILnet/minecraft/item/ItemStack;Lnet/minecraft/entity/Entity;Lorg/apache/commons/lang3/mutable/MutableFloat;)V
 *   Lnet/minecraft/enchantment/Enchantment;modifyProjectileCount(Lnet/minecraft/server/world/ServerWorld;ILnet/minecraft/item/ItemStack;Lnet/minecraft/entity/Entity;Lorg/apache/commons/lang3/mutable/MutableFloat;)V
 *   Lnet/minecraft/enchantment/Enchantment;onTick(Lnet/minecraft/server/world/ServerWorld;ILnet/minecraft/enchantment/EnchantmentEffectContext;Lnet/minecraft/entity/Entity;)V
 *   Lnet/minecraft/enchantment/Enchantment;removeLocationBasedEffects(ILnet/minecraft/enchantment/EnchantmentEffectContext;Lnet/minecraft/entity/LivingEntity;)V
 *   Lnet/minecraft/enchantment/Enchantment;applyLocationBasedEffects(Lnet/minecraft/server/world/ServerWorld;ILnet/minecraft/enchantment/EnchantmentEffectContext;Lnet/minecraft/entity/LivingEntity;)V
 *   Lnet/minecraft/enchantment/Enchantment;onTargetDamaged(Lnet/minecraft/server/world/ServerWorld;ILnet/minecraft/enchantment/EnchantmentEffectContext;Lnet/minecraft/enchantment/effect/EnchantmentEffectTarget;Lnet/minecraft/entity/Entity;Lnet/minecraft/entity/damage/DamageSource;)V
 *   Lnet/minecraft/enchantment/Enchantment;onPiercingAttack(Lnet/minecraft/server/world/ServerWorld;ILnet/minecraft/enchantment/EnchantmentEffectContext;Lnet/minecraft/entity/Entity;)V
 *   Lnet/minecraft/enchantment/Enchantment;modifyKnockback(Lnet/minecraft/server/world/ServerWorld;ILnet/minecraft/item/ItemStack;Lnet/minecraft/entity/Entity;Lnet/minecraft/entity/damage/DamageSource;Lorg/apache/commons/lang3/mutable/MutableFloat;)V
 *   Lnet/minecraft/enchantment/Enchantment;modifyArmorEffectiveness(Lnet/minecraft/server/world/ServerWorld;ILnet/minecraft/item/ItemStack;Lnet/minecraft/entity/Entity;Lnet/minecraft/entity/damage/DamageSource;Lorg/apache/commons/lang3/mutable/MutableFloat;)V
 *   Lnet/minecraft/enchantment/Enchantment;modifySmashDamagePerFallenBlock(Lnet/minecraft/server/world/ServerWorld;ILnet/minecraft/item/ItemStack;Lnet/minecraft/entity/Entity;Lnet/minecraft/entity/damage/DamageSource;Lorg/apache/commons/lang3/mutable/MutableFloat;)V
 *   Lnet/minecraft/enchantment/Enchantment;modifyDamage(Lnet/minecraft/server/world/ServerWorld;ILnet/minecraft/item/ItemStack;Lnet/minecraft/entity/Entity;Lnet/minecraft/entity/damage/DamageSource;Lorg/apache/commons/lang3/mutable/MutableFloat;)V
 *   Lnet/minecraft/enchantment/EnchantmentEffectContext;stack()Lnet/minecraft/item/ItemStack;
 *   Lnet/minecraft/enchantment/Enchantment;modifyDamageProtection(Lnet/minecraft/server/world/ServerWorld;ILnet/minecraft/item/ItemStack;Lnet/minecraft/entity/Entity;Lnet/minecraft/entity/damage/DamageSource;Lorg/apache/commons/lang3/mutable/MutableFloat;)V
 *   Lnet/minecraft/enchantment/Enchantment;modifyMobExperience(Lnet/minecraft/server/world/ServerWorld;ILnet/minecraft/item/ItemStack;Lnet/minecraft/entity/Entity;Lorg/apache/commons/lang3/mutable/MutableFloat;)V
 *   Lnet/minecraft/enchantment/Enchantment;modifyBlockExperience(Lnet/minecraft/server/world/ServerWorld;ILnet/minecraft/item/ItemStack;Lorg/apache/commons/lang3/mutable/MutableFloat;)V
 *   Lnet/minecraft/enchantment/Enchantment;modifyAmmoUse(Lnet/minecraft/server/world/ServerWorld;ILnet/minecraft/item/ItemStack;Lorg/apache/commons/lang3/mutable/MutableFloat;)V
 *   Lnet/minecraft/enchantment/Enchantment;modifyItemDamage(Lnet/minecraft/server/world/ServerWorld;ILnet/minecraft/item/ItemStack;Lorg/apache/commons/lang3/mutable/MutableFloat;)V
 *
 * Internal private/static methods:
 *   Lnet/minecraft/enchantment/EnchantmentHelper;forEachEnchantment(Lnet/minecraft/item/ItemStack;Lnet/minecraft/enchantment/EnchantmentHelper$Consumer;)V
 *   Lnet/minecraft/enchantment/EnchantmentHelper;forEachEnchantment(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/enchantment/EnchantmentHelper$ContextAwareConsumer;)V
 *   Lnet/minecraft/enchantment/EnchantmentHelper;forEachEnchantment(Lnet/minecraft/item/ItemStack;Lnet/minecraft/entity/EquipmentSlot;Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/enchantment/EnchantmentHelper$ContextAwareConsumer;)V
 *   Lnet/minecraft/enchantment/EnchantmentHelper;onTargetDamaged(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/Entity;Lnet/minecraft/entity/damage/DamageSource;Lnet/minecraft/item/ItemStack;)V
 *   Lnet/minecraft/enchantment/EnchantmentHelper;onTargetDamaged(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/Entity;Lnet/minecraft/entity/damage/DamageSource;Lnet/minecraft/item/ItemStack;Ljava/util/function/Consumer;)V
 *   Lnet/minecraft/enchantment/EnchantmentHelper;enchant(Lnet/minecraft/util/math/random/Random;Lnet/minecraft/item/ItemStack;ILjava/util/stream/Stream;)Lnet/minecraft/item/ItemStack;
 *   Lnet/minecraft/enchantment/EnchantmentHelper;generateEnchantments(Lnet/minecraft/util/math/random/Random;Lnet/minecraft/item/ItemStack;ILjava/util/stream/Stream;)Ljava/util/List;
 *   Lnet/minecraft/enchantment/EnchantmentHelper;removeConflicts(Ljava/util/List;Lnet/minecraft/enchantment/EnchantmentLevelEntry;)V
 *   Lnet/minecraft/enchantment/EnchantmentHelper;apply(Lnet/minecraft/item/ItemStack;Ljava/util/function/Consumer;)Lnet/minecraft/component/type/ItemEnchantmentsComponent;
 */
package net.minecraft.enchantment;

import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Predicate;
import java.util.stream.Stream;
import net.minecraft.block.BlockState;
import net.minecraft.component.ComponentType;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.EnchantmentEffectComponentTypes;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.component.type.EnchantableComponent;
import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentEffectContext;
import net.minecraft.enchantment.EnchantmentLevelEntry;
import net.minecraft.enchantment.effect.EnchantmentEffectTarget;
import net.minecraft.enchantment.effect.EnchantmentValueEffect;
import net.minecraft.enchantment.provider.EnchantmentProvider;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.loot.context.LootContext;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Util;
import net.minecraft.util.collection.Weighting;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.LocalDifficulty;
import org.apache.commons.lang3.mutable.MutableBoolean;
import org.apache.commons.lang3.mutable.MutableFloat;
import org.apache.commons.lang3.mutable.MutableObject;
import org.jspecify.annotations.Nullable;

public class EnchantmentHelper {
    public static int getLevel(RegistryEntry<Enchantment> enchantment, ItemStack stack) {
        ItemEnchantmentsComponent lv = stack.getOrDefault(DataComponentTypes.ENCHANTMENTS, ItemEnchantmentsComponent.DEFAULT);
        return lv.getLevel(enchantment);
    }

    public static ItemEnchantmentsComponent apply(ItemStack stack, java.util.function.Consumer<ItemEnchantmentsComponent.Builder> applier) {
        ComponentType<ItemEnchantmentsComponent> lv = EnchantmentHelper.getEnchantmentsComponentType(stack);
        ItemEnchantmentsComponent lv2 = stack.get(lv);
        if (lv2 == null) {
            return ItemEnchantmentsComponent.DEFAULT;
        }
        ItemEnchantmentsComponent.Builder lv3 = new ItemEnchantmentsComponent.Builder(lv2);
        applier.accept(lv3);
        ItemEnchantmentsComponent lv4 = lv3.build();
        stack.set(lv, lv4);
        return lv4;
    }

    public static boolean canHaveEnchantments(ItemStack stack) {
        return stack.contains(EnchantmentHelper.getEnchantmentsComponentType(stack));
    }

    public static void set(ItemStack stack, ItemEnchantmentsComponent enchantments) {
        stack.set(EnchantmentHelper.getEnchantmentsComponentType(stack), enchantments);
    }

    public static ItemEnchantmentsComponent getEnchantments(ItemStack stack) {
        return stack.getOrDefault(EnchantmentHelper.getEnchantmentsComponentType(stack), ItemEnchantmentsComponent.DEFAULT);
    }

    private static ComponentType<ItemEnchantmentsComponent> getEnchantmentsComponentType(ItemStack stack) {
        return stack.isOf(Items.ENCHANTED_BOOK) ? DataComponentTypes.STORED_ENCHANTMENTS : DataComponentTypes.ENCHANTMENTS;
    }

    public static boolean hasEnchantments(ItemStack stack) {
        return !stack.getOrDefault(DataComponentTypes.ENCHANTMENTS, ItemEnchantmentsComponent.DEFAULT).isEmpty() || !stack.getOrDefault(DataComponentTypes.STORED_ENCHANTMENTS, ItemEnchantmentsComponent.DEFAULT).isEmpty();
    }

    public static int getItemDamage(ServerWorld world, ItemStack stack, int baseItemDamage) {
        MutableFloat mutableFloat = new MutableFloat(baseItemDamage);
        EnchantmentHelper.forEachEnchantment(stack, (RegistryEntry<Enchantment> enchantment, int level) -> ((Enchantment)enchantment.value()).modifyItemDamage(world, level, stack, mutableFloat));
        return mutableFloat.intValue();
    }

    public static int getAmmoUse(ServerWorld world, ItemStack rangedWeaponStack, ItemStack projectileStack, int baseAmmoUse) {
        MutableFloat mutableFloat = new MutableFloat(baseAmmoUse);
        EnchantmentHelper.forEachEnchantment(rangedWeaponStack, (RegistryEntry<Enchantment> enchantment, int level) -> ((Enchantment)enchantment.value()).modifyAmmoUse(world, level, projectileStack, mutableFloat));
        return mutableFloat.intValue();
    }

    public static int getBlockExperience(ServerWorld world, ItemStack stack, int baseBlockExperience) {
        MutableFloat mutableFloat = new MutableFloat(baseBlockExperience);
        EnchantmentHelper.forEachEnchantment(stack, (RegistryEntry<Enchantment> enchantment, int level) -> ((Enchantment)enchantment.value()).modifyBlockExperience(world, level, stack, mutableFloat));
        return mutableFloat.intValue();
    }

    public static int getMobExperience(ServerWorld world, @Nullable Entity attacker, Entity mob, int baseMobExperience) {
        if (attacker instanceof LivingEntity) {
            LivingEntity lv = (LivingEntity)attacker;
            MutableFloat mutableFloat = new MutableFloat(baseMobExperience);
            EnchantmentHelper.forEachEnchantment(lv, (RegistryEntry<Enchantment> enchantment, int level, EnchantmentEffectContext context) -> ((Enchantment)enchantment.value()).modifyMobExperience(world, level, context.stack(), mob, mutableFloat));
            return mutableFloat.intValue();
        }
        return baseMobExperience;
    }

    public static ItemStack getEnchantedBookWith(EnchantmentLevelEntry entry) {
        ItemStack lv = new ItemStack(Items.ENCHANTED_BOOK);
        lv.addEnchantment(entry.enchantment(), entry.level());
        return lv;
    }

    private static void forEachEnchantment(ItemStack stack, Consumer consumer) {
        ItemEnchantmentsComponent lv = stack.getOrDefault(DataComponentTypes.ENCHANTMENTS, ItemEnchantmentsComponent.DEFAULT);
        for (Object2IntMap.Entry<RegistryEntry<Enchantment>> entry : lv.getEnchantmentEntries()) {
            consumer.accept((RegistryEntry)entry.getKey(), entry.getIntValue());
        }
    }

    private static void forEachEnchantment(ItemStack stack, EquipmentSlot slot, LivingEntity entity, ContextAwareConsumer contextAwareConsumer) {
        if (stack.isEmpty()) {
            return;
        }
        ItemEnchantmentsComponent lv = stack.get(DataComponentTypes.ENCHANTMENTS);
        if (lv == null || lv.isEmpty()) {
            return;
        }
        EnchantmentEffectContext lv2 = new EnchantmentEffectContext(stack, slot, entity);
        for (Object2IntMap.Entry<RegistryEntry<Enchantment>> entry : lv.getEnchantmentEntries()) {
            RegistryEntry lv3 = (RegistryEntry)entry.getKey();
            if (!((Enchantment)lv3.value()).slotMatches(slot)) continue;
            contextAwareConsumer.accept(lv3, entry.getIntValue(), lv2);
        }
    }

    private static void forEachEnchantment(LivingEntity entity, ContextAwareConsumer contextAwareConsumer) {
        for (EquipmentSlot lv : EquipmentSlot.VALUES) {
            EnchantmentHelper.forEachEnchantment(entity.getEquippedStack(lv), lv, entity, contextAwareConsumer);
        }
    }

    public static boolean isInvulnerableTo(ServerWorld world, LivingEntity user, DamageSource damageSource) {
        MutableBoolean mutableBoolean = new MutableBoolean();
        EnchantmentHelper.forEachEnchantment(user, (RegistryEntry<Enchantment> enchantment, int level, EnchantmentEffectContext context) -> mutableBoolean.setValue(mutableBoolean.isTrue() || ((Enchantment)enchantment.value()).hasDamageImmunityTo(world, level, user, damageSource)));
        return mutableBoolean.isTrue();
    }

    public static float getProtectionAmount(ServerWorld world, LivingEntity user, DamageSource damageSource) {
        MutableFloat mutableFloat = new MutableFloat(0.0f);
        EnchantmentHelper.forEachEnchantment(user, (RegistryEntry<Enchantment> enchantment, int level, EnchantmentEffectContext context) -> ((Enchantment)enchantment.value()).modifyDamageProtection(world, level, context.stack(), user, damageSource, mutableFloat));
        return mutableFloat.floatValue();
    }

    public static float getDamage(ServerWorld world, ItemStack stack, Entity target, DamageSource damageSource, float baseDamage) {
        MutableFloat mutableFloat = new MutableFloat(baseDamage);
        EnchantmentHelper.forEachEnchantment(stack, (RegistryEntry<Enchantment> enchantment, int level) -> ((Enchantment)enchantment.value()).modifyDamage(world, level, stack, target, damageSource, mutableFloat));
        return mutableFloat.floatValue();
    }

    public static float getSmashDamagePerFallenBlock(ServerWorld world, ItemStack stack, Entity target, DamageSource damageSource, float baseSmashDamagePerFallenBlock) {
        MutableFloat mutableFloat = new MutableFloat(baseSmashDamagePerFallenBlock);
        EnchantmentHelper.forEachEnchantment(stack, (RegistryEntry<Enchantment> enchantment, int level) -> ((Enchantment)enchantment.value()).modifySmashDamagePerFallenBlock(world, level, stack, target, damageSource, mutableFloat));
        return mutableFloat.floatValue();
    }

    public static float getArmorEffectiveness(ServerWorld world, ItemStack stack, Entity user, DamageSource damageSource, float baseArmorEffectiveness) {
        MutableFloat mutableFloat = new MutableFloat(baseArmorEffectiveness);
        EnchantmentHelper.forEachEnchantment(stack, (RegistryEntry<Enchantment> enchantment, int level) -> ((Enchantment)enchantment.value()).modifyArmorEffectiveness(world, level, stack, user, damageSource, mutableFloat));
        return mutableFloat.floatValue();
    }

    public static float modifyKnockback(ServerWorld world, ItemStack stack, Entity target, DamageSource damageSource, float baseKnockback) {
        MutableFloat mutableFloat = new MutableFloat(baseKnockback);
        EnchantmentHelper.forEachEnchantment(stack, (RegistryEntry<Enchantment> enchantment, int level) -> ((Enchantment)enchantment.value()).modifyKnockback(world, level, stack, target, damageSource, mutableFloat));
        return mutableFloat.floatValue();
    }

    public static void onTargetDamaged(ServerWorld world, Entity target, DamageSource damageSource) {
        Entity entity = damageSource.getAttacker();
        if (entity instanceof LivingEntity) {
            LivingEntity lv = (LivingEntity)entity;
            EnchantmentHelper.onTargetDamaged(world, target, damageSource, lv.getWeaponStack());
        } else {
            EnchantmentHelper.onTargetDamaged(world, target, damageSource, null);
        }
    }

    public static void onAttack(ServerWorld world, Entity attacker) {
        if (attacker instanceof LivingEntity) {
            LivingEntity lv = (LivingEntity)attacker;
            EnchantmentHelper.forEachEnchantment(attacker.getWeaponStack(), EquipmentSlot.MAINHAND, lv, (enchantment, level, context) -> ((Enchantment)enchantment.value()).onPiercingAttack(world, level, context, attacker));
        }
    }

    public static void onTargetDamaged(ServerWorld world, Entity target, DamageSource damageSource, @Nullable ItemStack weapon) {
        EnchantmentHelper.onTargetDamaged(world, target, damageSource, weapon, null);
    }

    public static void onTargetDamaged(ServerWorld world, Entity target, DamageSource damageSource, @Nullable ItemStack weapon, @Nullable java.util.function.Consumer<Item> breakCallback) {
        LivingEntity lv;
        if (target instanceof LivingEntity) {
            lv = (LivingEntity)target;
            EnchantmentHelper.forEachEnchantment(lv, (RegistryEntry<Enchantment> enchantment, int level, EnchantmentEffectContext context) -> ((Enchantment)enchantment.value()).onTargetDamaged(world, level, context, EnchantmentEffectTarget.VICTIM, target, damageSource));
        }
        if (weapon != null) {
            Entity entity = damageSource.getAttacker();
            if (entity instanceof LivingEntity) {
                lv = (LivingEntity)entity;
                EnchantmentHelper.forEachEnchantment(weapon, EquipmentSlot.MAINHAND, lv, (enchantment, level, context) -> ((Enchantment)enchantment.value()).onTargetDamaged(world, level, context, EnchantmentEffectTarget.ATTACKER, target, damageSource));
            } else if (breakCallback != null) {
                EnchantmentEffectContext lv2 = new EnchantmentEffectContext(weapon, null, null, breakCallback);
                EnchantmentHelper.forEachEnchantment(weapon, (RegistryEntry<Enchantment> enchantment, int level) -> ((Enchantment)enchantment.value()).onTargetDamaged(world, level, lv2, EnchantmentEffectTarget.ATTACKER, target, damageSource));
            }
        }
    }

    public static void applyLocationBasedEffects(ServerWorld world, LivingEntity user) {
        EnchantmentHelper.forEachEnchantment(user, (RegistryEntry<Enchantment> enchantment, int level, EnchantmentEffectContext context) -> ((Enchantment)enchantment.value()).applyLocationBasedEffects(world, level, context, user));
    }

    public static void applyLocationBasedEffects(ServerWorld world, ItemStack stack, LivingEntity user, EquipmentSlot slot) {
        EnchantmentHelper.forEachEnchantment(stack, slot, user, (enchantment, level, context) -> ((Enchantment)enchantment.value()).applyLocationBasedEffects(world, level, context, user));
    }

    public static void removeLocationBasedEffects(LivingEntity user) {
        EnchantmentHelper.forEachEnchantment(user, (RegistryEntry<Enchantment> enchantment, int level, EnchantmentEffectContext context) -> ((Enchantment)enchantment.value()).removeLocationBasedEffects(level, context, user));
    }

    public static void removeLocationBasedEffects(ItemStack stack, LivingEntity user, EquipmentSlot slot) {
        EnchantmentHelper.forEachEnchantment(stack, slot, user, (enchantment, level, context) -> ((Enchantment)enchantment.value()).removeLocationBasedEffects(level, context, user));
    }

    public static void onTick(ServerWorld world, LivingEntity user) {
        EnchantmentHelper.forEachEnchantment(user, (RegistryEntry<Enchantment> enchantment, int level, EnchantmentEffectContext context) -> ((Enchantment)enchantment.value()).onTick(world, level, context, user));
    }

    public static int getEquipmentLevel(RegistryEntry<Enchantment> enchantment, LivingEntity entity) {
        Collection<ItemStack> iterable = enchantment.value().getEquipment(entity).values();
        int i = 0;
        for (ItemStack lv : iterable) {
            int j = EnchantmentHelper.getLevel(enchantment, lv);
            if (j <= i) continue;
            i = j;
        }
        return i;
    }

    public static int getProjectileCount(ServerWorld world, ItemStack stack, Entity user, int baseProjectileCount) {
        MutableFloat mutableFloat = new MutableFloat(baseProjectileCount);
        EnchantmentHelper.forEachEnchantment(stack, (RegistryEntry<Enchantment> enchantment, int level) -> ((Enchantment)enchantment.value()).modifyProjectileCount(world, level, stack, user, mutableFloat));
        return Math.max(0, mutableFloat.intValue());
    }

    public static float getProjectileSpread(ServerWorld world, ItemStack stack, Entity user, float baseProjectileSpread) {
        MutableFloat mutableFloat = new MutableFloat(baseProjectileSpread);
        EnchantmentHelper.forEachEnchantment(stack, (RegistryEntry<Enchantment> enchantment, int level) -> ((Enchantment)enchantment.value()).modifyProjectileSpread(world, level, stack, user, mutableFloat));
        return Math.max(0.0f, mutableFloat.floatValue());
    }

    public static int getProjectilePiercing(ServerWorld world, ItemStack weaponStack, ItemStack projectileStack) {
        MutableFloat mutableFloat = new MutableFloat(0.0f);
        EnchantmentHelper.forEachEnchantment(weaponStack, (RegistryEntry<Enchantment> enchantment, int level) -> ((Enchantment)enchantment.value()).modifyProjectilePiercing(world, level, projectileStack, mutableFloat));
        return Math.max(0, mutableFloat.intValue());
    }

    public static void onProjectileSpawned(ServerWorld world, ItemStack weaponStack, ProjectileEntity projectile, java.util.function.Consumer<Item> onBreak) {
        LivingEntity lv;
        Entity entity = projectile.getOwner();
        LivingEntity lv2 = entity instanceof LivingEntity ? (lv = (LivingEntity)entity) : null;
        EnchantmentEffectContext lv3 = new EnchantmentEffectContext(weaponStack, null, lv2, onBreak);
        EnchantmentHelper.forEachEnchantment(weaponStack, (RegistryEntry<Enchantment> enchantment, int level) -> ((Enchantment)enchantment.value()).onProjectileSpawned(world, level, lv3, projectile));
    }

    public static void onHitBlock(ServerWorld world, ItemStack stack, @Nullable LivingEntity user, Entity enchantedEntity, @Nullable EquipmentSlot slot, Vec3d pos, BlockState state, java.util.function.Consumer<Item> onBreak) {
        EnchantmentEffectContext lv = new EnchantmentEffectContext(stack, slot, user, onBreak);
        EnchantmentHelper.forEachEnchantment(stack, (RegistryEntry<Enchantment> enchantment, int level) -> ((Enchantment)enchantment.value()).onHitBlock(world, level, lv, enchantedEntity, pos, state));
    }

    public static int getRepairWithExperience(ServerWorld world, ItemStack stack, int baseRepairWithExperience) {
        MutableFloat mutableFloat = new MutableFloat(baseRepairWithExperience);
        EnchantmentHelper.forEachEnchantment(stack, (RegistryEntry<Enchantment> enchantment, int level) -> ((Enchantment)enchantment.value()).modifyRepairWithExperience(world, level, stack, mutableFloat));
        return Math.max(0, mutableFloat.intValue());
    }

    public static float getEquipmentDropChance(ServerWorld world, LivingEntity attacker, DamageSource damageSource, float baseEquipmentDropChance) {
        MutableFloat mutableFloat = new MutableFloat(baseEquipmentDropChance);
        Random lv = attacker.getRandom();
        EnchantmentHelper.forEachEnchantment(attacker, (RegistryEntry<Enchantment> enchantment, int level, EnchantmentEffectContext context) -> {
            LootContext lv = Enchantment.createEnchantedDamageLootContext(world, level, attacker, damageSource);
            ((Enchantment)enchantment.value()).getEffect(EnchantmentEffectComponentTypes.EQUIPMENT_DROPS).forEach(effect -> {
                if (effect.enchanted() == EnchantmentEffectTarget.VICTIM && effect.affected() == EnchantmentEffectTarget.VICTIM && effect.test(lv)) {
                    mutableFloat.setValue(((EnchantmentValueEffect)effect.effect()).apply(level, lv, mutableFloat.floatValue()));
                }
            });
        });
        Entity lv2 = damageSource.getAttacker();
        if (lv2 instanceof LivingEntity) {
            LivingEntity lv3 = (LivingEntity)lv2;
            EnchantmentHelper.forEachEnchantment(lv3, (RegistryEntry<Enchantment> enchantment, int level, EnchantmentEffectContext context) -> {
                LootContext lv = Enchantment.createEnchantedDamageLootContext(world, level, attacker, damageSource);
                ((Enchantment)enchantment.value()).getEffect(EnchantmentEffectComponentTypes.EQUIPMENT_DROPS).forEach(effect -> {
                    if (effect.enchanted() == EnchantmentEffectTarget.ATTACKER && effect.affected() == EnchantmentEffectTarget.VICTIM && effect.test(lv)) {
                        mutableFloat.setValue(((EnchantmentValueEffect)effect.effect()).apply(level, lv, mutableFloat.floatValue()));
                    }
                });
            });
        }
        return mutableFloat.floatValue();
    }

    public static void applyAttributeModifiers(ItemStack stack, AttributeModifierSlot slot, BiConsumer<RegistryEntry<EntityAttribute>, EntityAttributeModifier> attributeModifierConsumer) {
        EnchantmentHelper.forEachEnchantment(stack, (RegistryEntry<Enchantment> enchantment, int level) -> ((Enchantment)enchantment.value()).getEffect(EnchantmentEffectComponentTypes.ATTRIBUTES).forEach(effect -> {
            if (((Enchantment)enchantment.value()).definition().slots().contains(slot)) {
                attributeModifierConsumer.accept(effect.attribute(), effect.createAttributeModifier(level, slot));
            }
        }));
    }

    public static void applyAttributeModifiers(ItemStack stack, EquipmentSlot slot, BiConsumer<RegistryEntry<EntityAttribute>, EntityAttributeModifier> attributeModifierConsumer) {
        EnchantmentHelper.forEachEnchantment(stack, (RegistryEntry<Enchantment> enchantment, int level) -> ((Enchantment)enchantment.value()).getEffect(EnchantmentEffectComponentTypes.ATTRIBUTES).forEach(effect -> {
            if (((Enchantment)enchantment.value()).slotMatches(slot)) {
                attributeModifierConsumer.accept(effect.attribute(), effect.createAttributeModifier(level, slot));
            }
        }));
    }

    public static int getFishingLuckBonus(ServerWorld world, ItemStack stack, Entity user) {
        MutableFloat mutableFloat = new MutableFloat(0.0f);
        EnchantmentHelper.forEachEnchantment(stack, (RegistryEntry<Enchantment> enchantment, int level) -> ((Enchantment)enchantment.value()).modifyFishingLuckBonus(world, level, stack, user, mutableFloat));
        return Math.max(0, mutableFloat.intValue());
    }

    public static float getFishingTimeReduction(ServerWorld world, ItemStack stack, Entity user) {
        MutableFloat mutableFloat = new MutableFloat(0.0f);
        EnchantmentHelper.forEachEnchantment(stack, (RegistryEntry<Enchantment> enchantment, int level) -> ((Enchantment)enchantment.value()).modifyFishingTimeReduction(world, level, stack, user, mutableFloat));
        return Math.max(0.0f, mutableFloat.floatValue());
    }

    public static int getTridentReturnAcceleration(ServerWorld world, ItemStack stack, Entity user) {
        MutableFloat mutableFloat = new MutableFloat(0.0f);
        EnchantmentHelper.forEachEnchantment(stack, (RegistryEntry<Enchantment> enchantment, int level) -> ((Enchantment)enchantment.value()).modifyTridentReturnAcceleration(world, level, stack, user, mutableFloat));
        return Math.max(0, mutableFloat.intValue());
    }

    public static float getCrossbowChargeTime(ItemStack stack, LivingEntity user, float baseCrossbowChargeTime) {
        MutableFloat mutableFloat = new MutableFloat(baseCrossbowChargeTime);
        EnchantmentHelper.forEachEnchantment(stack, (RegistryEntry<Enchantment> enchantment, int level) -> ((Enchantment)enchantment.value()).modifyCrossbowChargeTime(user.getRandom(), level, mutableFloat));
        return Math.max(0.0f, mutableFloat.floatValue());
    }

    public static float getTridentSpinAttackStrength(ItemStack stack, LivingEntity user) {
        MutableFloat mutableFloat = new MutableFloat(0.0f);
        EnchantmentHelper.forEachEnchantment(stack, (RegistryEntry<Enchantment> enchantment, int level) -> ((Enchantment)enchantment.value()).modifyTridentSpinAttackStrength(user.getRandom(), level, mutableFloat));
        return mutableFloat.floatValue();
    }

    public static boolean hasAnyEnchantmentsIn(ItemStack stack, TagKey<Enchantment> tag) {
        ItemEnchantmentsComponent lv = stack.getOrDefault(DataComponentTypes.ENCHANTMENTS, ItemEnchantmentsComponent.DEFAULT);
        for (Object2IntMap.Entry<RegistryEntry<Enchantment>> entry : lv.getEnchantmentEntries()) {
            RegistryEntry lv2 = (RegistryEntry)entry.getKey();
            if (!lv2.isIn(tag)) continue;
            return true;
        }
        return false;
    }

    public static boolean hasAnyEnchantmentsWith(ItemStack stack, ComponentType<?> componentType) {
        MutableBoolean mutableBoolean = new MutableBoolean(false);
        EnchantmentHelper.forEachEnchantment(stack, (RegistryEntry<Enchantment> enchantment, int level) -> {
            if (((Enchantment)enchantment.value()).effects().contains(componentType)) {
                mutableBoolean.setTrue();
            }
        });
        return mutableBoolean.booleanValue();
    }

    public static <T> Optional<T> getEffect(ItemStack stack, ComponentType<List<T>> componentType) {
        Pair<List<T>, Integer> pair = EnchantmentHelper.getHighestLevelEffect(stack, componentType);
        if (pair != null) {
            List<T> list = pair.getFirst();
            int i = pair.getSecond();
            return Optional.of(list.get(Math.min(i, list.size()) - 1));
        }
        return Optional.empty();
    }

    public static <T> @Nullable Pair<T, Integer> getHighestLevelEffect(ItemStack stack, ComponentType<T> componentType) {
        MutableObject<@Nullable T> mutableObject = new MutableObject();
        EnchantmentHelper.forEachEnchantment(stack, (RegistryEntry<Enchantment> enchantment, int level) -> {
            Object object;
            if ((mutableObject.get() == null || (Integer)((Pair)mutableObject.get()).getSecond() < level) && (object = ((Enchantment)enchantment.value()).effects().get(componentType)) != null) {
                mutableObject.setValue(Pair.of(object, level));
            }
        });
        return (Pair)mutableObject.get();
    }

    public static Optional<EnchantmentEffectContext> chooseEquipmentWith(ComponentType<?> componentType, LivingEntity entity, Predicate<ItemStack> stackPredicate) {
        ArrayList<EnchantmentEffectContext> list = new ArrayList<EnchantmentEffectContext>();
        for (EquipmentSlot lv : EquipmentSlot.VALUES) {
            ItemStack lv2 = entity.getEquippedStack(lv);
            if (!stackPredicate.test(lv2)) continue;
            ItemEnchantmentsComponent lv3 = lv2.getOrDefault(DataComponentTypes.ENCHANTMENTS, ItemEnchantmentsComponent.DEFAULT);
            for (Object2IntMap.Entry<RegistryEntry<Enchantment>> entry : lv3.getEnchantmentEntries()) {
                RegistryEntry lv4 = (RegistryEntry)entry.getKey();
                if (!((Enchantment)lv4.value()).effects().contains(componentType) || !((Enchantment)lv4.value()).slotMatches(lv)) continue;
                list.add(new EnchantmentEffectContext(lv2, lv, entity));
            }
        }
        return Util.getRandomOrEmpty(list, entity.getRandom());
    }

    public static int calculateRequiredExperienceLevel(Random random, int slotIndex, int bookshelfCount, ItemStack stack) {
        EnchantableComponent lv = stack.get(DataComponentTypes.ENCHANTABLE);
        if (lv == null) {
            return 0;
        }
        if (bookshelfCount > 15) {
            bookshelfCount = 15;
        }
        int k = random.nextInt(8) + 1 + (bookshelfCount >> 1) + random.nextInt(bookshelfCount + 1);
        if (slotIndex == 0) {
            return Math.max(k / 3, 1);
        }
        if (slotIndex == 1) {
            return k * 2 / 3 + 1;
        }
        return Math.max(k, bookshelfCount * 2);
    }

    public static ItemStack enchant(Random random, ItemStack stack, int level, DynamicRegistryManager dynamicRegistryManager, Optional<? extends RegistryEntryList<Enchantment>> enchantments) {
        return EnchantmentHelper.enchant(random, stack, level, enchantments.map(RegistryEntryList::stream).orElseGet(() -> dynamicRegistryManager.getOrThrow(RegistryKeys.ENCHANTMENT).streamEntries().map(arg -> arg)));
    }

    public static ItemStack enchant(Random random, ItemStack stack, int level, Stream<RegistryEntry<Enchantment>> possibleEnchantments) {
        List<EnchantmentLevelEntry> list = EnchantmentHelper.generateEnchantments(random, stack, level, possibleEnchantments);
        if (stack.isOf(Items.BOOK)) {
            stack = new ItemStack(Items.ENCHANTED_BOOK);
        }
        for (EnchantmentLevelEntry lv : list) {
            stack.addEnchantment(lv.enchantment(), lv.level());
        }
        return stack;
    }

    public static List<EnchantmentLevelEntry> generateEnchantments(Random random, ItemStack stack, int level, Stream<RegistryEntry<Enchantment>> possibleEnchantments) {
        ArrayList<EnchantmentLevelEntry> list = Lists.newArrayList();
        EnchantableComponent lv = stack.get(DataComponentTypes.ENCHANTABLE);
        if (lv == null) {
            return list;
        }
        level += 1 + random.nextInt(lv.value() / 4 + 1) + random.nextInt(lv.value() / 4 + 1);
        float f = (random.nextFloat() + random.nextFloat() - 1.0f) * 0.15f;
        List<EnchantmentLevelEntry> list2 = EnchantmentHelper.getPossibleEntries(level = MathHelper.clamp(Math.round((float)level + (float)level * f), 1, Integer.MAX_VALUE), stack, possibleEnchantments);
        if (!list2.isEmpty()) {
            Weighting.getRandom(random, list2, EnchantmentLevelEntry::getWeight).ifPresent(list::add);
            while (random.nextInt(50) <= level) {
                if (!list.isEmpty()) {
                    EnchantmentHelper.removeConflicts(list2, (EnchantmentLevelEntry)list.getLast());
                }
                if (list2.isEmpty()) break;
                Weighting.getRandom(random, list2, EnchantmentLevelEntry::getWeight).ifPresent(list::add);
                level /= 2;
            }
        }
        return list;
    }

    public static void removeConflicts(List<EnchantmentLevelEntry> possibleEntries, EnchantmentLevelEntry pickedEntry) {
        possibleEntries.removeIf(entry -> !Enchantment.canBeCombined(pickedEntry.enchantment(), entry.enchantment()));
    }

    public static boolean isCompatible(Collection<RegistryEntry<Enchantment>> existing, RegistryEntry<Enchantment> candidate) {
        for (RegistryEntry<Enchantment> lv : existing) {
            if (Enchantment.canBeCombined(lv, candidate)) continue;
            return false;
        }
        return true;
    }

    public static List<EnchantmentLevelEntry> getPossibleEntries(int level, ItemStack stack, Stream<RegistryEntry<Enchantment>> possibleEnchantments) {
        ArrayList<EnchantmentLevelEntry> list = Lists.newArrayList();
        boolean bl = stack.isOf(Items.BOOK);
        possibleEnchantments.filter(enchantment -> ((Enchantment)enchantment.value()).isPrimaryItem(stack) || bl).forEach(enchantmentx -> {
            Enchantment lv = (Enchantment)enchantmentx.value();
            for (int j = lv.getMaxLevel(); j >= lv.getMinLevel(); --j) {
                if (level < lv.getMinPower(j) || level > lv.getMaxPower(j)) continue;
                list.add(new EnchantmentLevelEntry((RegistryEntry<Enchantment>)enchantmentx, j));
                break;
            }
        });
        return list;
    }

    public static void applyEnchantmentProvider(ItemStack stack, DynamicRegistryManager registryManager, RegistryKey<EnchantmentProvider> providerKey, LocalDifficulty localDifficulty, Random random) {
        EnchantmentProvider lv = registryManager.getOrThrow(RegistryKeys.ENCHANTMENT_PROVIDER).get(providerKey);
        if (lv != null) {
            EnchantmentHelper.apply(stack, componentBuilder -> lv.provideEnchantments(stack, (ItemEnchantmentsComponent.Builder)componentBuilder, random, localDifficulty));
        }
    }

    @FunctionalInterface
    static interface Consumer {
        public void accept(RegistryEntry<Enchantment> var1, int var2);
    }

    @FunctionalInterface
    static interface ContextAwareConsumer {
        public void accept(RegistryEntry<Enchantment> var1, int var2, EnchantmentEffectContext var3);
    }
}

