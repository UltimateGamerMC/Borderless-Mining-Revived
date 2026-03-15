/*
 * Internal private/static methods:
 *   Lnet/minecraft/loot/condition/LootConditionConsumingBuilder;conditionally(Lnet/minecraft/loot/condition/LootCondition$Builder;)Lnet/minecraft/loot/condition/LootConditionConsumingBuilder;
 */
package net.minecraft.loot.condition;

import java.util.function.Function;
import net.minecraft.loot.condition.LootCondition;

public interface LootConditionConsumingBuilder<T extends LootConditionConsumingBuilder<T>> {
    public T conditionally(LootCondition.Builder var1);

    default public <E> T conditionally(Iterable<E> conditions, Function<E, LootCondition.Builder> toBuilderFunction) {
        T lv = this.getThisConditionConsumingBuilder();
        for (E object : conditions) {
            lv = lv.conditionally(toBuilderFunction.apply(object));
        }
        return lv;
    }

    public T getThisConditionConsumingBuilder();
}

