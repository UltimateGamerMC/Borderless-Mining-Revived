/*
 * External method calls:
 *   Lnet/minecraft/loot/condition/LootCondition;test(Ljava/lang/Object;)Z
 *   Lnet/minecraft/loot/condition/LootCondition;validate(Lnet/minecraft/loot/LootTableReporter;)V
 *   Lnet/minecraft/loot/condition/LootCondition$Builder;build()Lnet/minecraft/loot/condition/LootCondition;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/loot/condition/InvertedLootCondition;test(Lnet/minecraft/loot/context/LootContext;)Z
 */
package net.minecraft.loot.condition;

import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Set;
import net.minecraft.loot.LootTableReporter;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.loot.condition.LootConditionType;
import net.minecraft.loot.condition.LootConditionTypes;
import net.minecraft.loot.context.LootContext;
import net.minecraft.util.context.ContextParameter;

public record InvertedLootCondition(LootCondition term) implements LootCondition
{
    public static final MapCodec<InvertedLootCondition> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(((MapCodec)LootCondition.CODEC.fieldOf("term")).forGetter(InvertedLootCondition::term)).apply((Applicative<InvertedLootCondition, ?>)instance, InvertedLootCondition::new));

    @Override
    public LootConditionType getType() {
        return LootConditionTypes.INVERTED;
    }

    @Override
    public boolean test(LootContext arg) {
        return !this.term.test(arg);
    }

    @Override
    public Set<ContextParameter<?>> getAllowedParameters() {
        return this.term.getAllowedParameters();
    }

    @Override
    public void validate(LootTableReporter reporter) {
        LootCondition.super.validate(reporter);
        this.term.validate(reporter);
    }

    public static LootCondition.Builder builder(LootCondition.Builder term) {
        InvertedLootCondition lv = new InvertedLootCondition(term.build());
        return () -> lv;
    }

    @Override
    public /* synthetic */ boolean test(Object context) {
        return this.test((LootContext)context);
    }
}

