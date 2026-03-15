/*
 * External method calls:
 *   Lnet/minecraft/loot/context/LootContext$EntityReference;contextParam()Lnet/minecraft/util/context/ContextParameter;
 *   Lnet/minecraft/predicate/entity/EntityPredicate;test(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/entity/Entity;)Z
 *   Lnet/minecraft/predicate/entity/EntityPredicate$Builder;create()Lnet/minecraft/predicate/entity/EntityPredicate$Builder;
 *   Lnet/minecraft/predicate/entity/EntityPredicate$Builder;build()Lnet/minecraft/predicate/entity/EntityPredicate;
 *   Lnet/minecraft/util/StringIdentifiable$EnumCodec;fieldOf(Ljava/lang/String;)Lcom/mojang/serialization/MapCodec;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/loot/condition/EntityPropertiesLootCondition;builder(Lnet/minecraft/loot/context/LootContext$EntityReference;Lnet/minecraft/predicate/entity/EntityPredicate$Builder;)Lnet/minecraft/loot/condition/LootCondition$Builder;
 *   Lnet/minecraft/loot/condition/EntityPropertiesLootCondition;test(Lnet/minecraft/loot/context/LootContext;)Z
 */
package net.minecraft.loot.condition;

import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Optional;
import java.util.Set;
import net.minecraft.entity.Entity;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.loot.condition.LootConditionType;
import net.minecraft.loot.condition.LootConditionTypes;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.util.context.ContextParameter;
import net.minecraft.util.math.Vec3d;

public record EntityPropertiesLootCondition(Optional<EntityPredicate> predicate, LootContext.EntityReference entity) implements LootCondition
{
    public static final MapCodec<EntityPropertiesLootCondition> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(EntityPredicate.CODEC.optionalFieldOf("predicate").forGetter(EntityPropertiesLootCondition::predicate), ((MapCodec)LootContext.EntityReference.CODEC.fieldOf("entity")).forGetter(EntityPropertiesLootCondition::entity)).apply((Applicative<EntityPropertiesLootCondition, ?>)instance, EntityPropertiesLootCondition::new));

    @Override
    public LootConditionType getType() {
        return LootConditionTypes.ENTITY_PROPERTIES;
    }

    @Override
    public Set<ContextParameter<?>> getAllowedParameters() {
        return Set.of(LootContextParameters.ORIGIN, this.entity.contextParam());
    }

    @Override
    public boolean test(LootContext arg) {
        Entity lv = arg.get(this.entity.contextParam());
        Vec3d lv2 = arg.get(LootContextParameters.ORIGIN);
        return this.predicate.isEmpty() || this.predicate.get().test(arg.getWorld(), lv2, lv);
    }

    public static LootCondition.Builder create(LootContext.EntityReference entity) {
        return EntityPropertiesLootCondition.builder(entity, EntityPredicate.Builder.create());
    }

    public static LootCondition.Builder builder(LootContext.EntityReference entity, EntityPredicate.Builder predicateBuilder) {
        return () -> new EntityPropertiesLootCondition(Optional.of(predicateBuilder.build()), entity);
    }

    public static LootCondition.Builder builder(LootContext.EntityReference entity, EntityPredicate predicate) {
        return () -> new EntityPropertiesLootCondition(Optional.of(predicate), entity);
    }

    @Override
    public /* synthetic */ boolean test(Object context) {
        return this.test((LootContext)context);
    }
}

