/*
 * Internal private/static methods:
 *   Lnet/minecraft/loot/function/LootFunctionConsumingBuilder;apply(Lnet/minecraft/loot/function/LootFunction$Builder;)Lnet/minecraft/loot/function/LootFunctionConsumingBuilder;
 *   Lnet/minecraft/loot/function/LootFunctionConsumingBuilder;apply(Ljava/lang/Iterable;Ljava/util/function/Function;)Lnet/minecraft/loot/function/LootFunctionConsumingBuilder;
 */
package net.minecraft.loot.function;

import java.util.Arrays;
import java.util.function.Function;
import net.minecraft.loot.function.LootFunction;

public interface LootFunctionConsumingBuilder<T extends LootFunctionConsumingBuilder<T>> {
    public T apply(LootFunction.Builder var1);

    default public <E> T apply(Iterable<E> functions, Function<E, LootFunction.Builder> toBuilderFunction) {
        T lv = this.getThisFunctionConsumingBuilder();
        for (E object : functions) {
            lv = lv.apply(toBuilderFunction.apply(object));
        }
        return lv;
    }

    default public <E> T apply(E[] functions, Function<E, LootFunction.Builder> toBuilderFunction) {
        return this.apply(Arrays.asList(functions), toBuilderFunction);
    }

    public T getThisFunctionConsumingBuilder();
}

