/*
 * External method calls:
 *   Lnet/minecraft/loot/function/LootFunctionTypes;join(Ljava/util/List;)Ljava/util/function/BiFunction;
 *   Lnet/minecraft/loot/function/LootFunction;validate(Lnet/minecraft/loot/LootTableReporter;)V
 *   Lnet/minecraft/loot/LootTableReporter;makeChild(Lnet/minecraft/util/ErrorReporter$Context;)Lnet/minecraft/loot/LootTableReporter;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/loot/function/AndLootFunction;apply(Lnet/minecraft/item/ItemStack;Lnet/minecraft/loot/context/LootContext;)Lnet/minecraft/item/ItemStack;
 */
package net.minecraft.loot.function;

import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.List;
import java.util.function.BiFunction;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootTableReporter;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.function.LootFunction;
import net.minecraft.loot.function.LootFunctionType;
import net.minecraft.loot.function.LootFunctionTypes;
import net.minecraft.util.ErrorReporter;

public class AndLootFunction
implements LootFunction {
    public static final MapCodec<AndLootFunction> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(((MapCodec)LootFunctionTypes.BASE_CODEC.listOf().fieldOf("functions")).forGetter(function -> function.terms)).apply((Applicative<AndLootFunction, ?>)instance, AndLootFunction::new));
    public static final Codec<AndLootFunction> INLINE_CODEC = LootFunctionTypes.BASE_CODEC.listOf().xmap(AndLootFunction::new, function -> function.terms);
    private final List<LootFunction> terms;
    private final BiFunction<ItemStack, LootContext, ItemStack> applier;

    private AndLootFunction(List<LootFunction> terms) {
        this.terms = terms;
        this.applier = LootFunctionTypes.join(terms);
    }

    public static AndLootFunction create(List<LootFunction> terms) {
        return new AndLootFunction(List.copyOf(terms));
    }

    @Override
    public ItemStack apply(ItemStack arg, LootContext arg2) {
        return this.applier.apply(arg, arg2);
    }

    @Override
    public void validate(LootTableReporter reporter) {
        LootFunction.super.validate(reporter);
        for (int i = 0; i < this.terms.size(); ++i) {
            this.terms.get(i).validate(reporter.makeChild(new ErrorReporter.NamedListElementContext("functions", i)));
        }
    }

    public LootFunctionType<AndLootFunction> getType() {
        return LootFunctionTypes.SEQUENCE;
    }

    @Override
    public /* synthetic */ Object apply(Object stack, Object context) {
        return this.apply((ItemStack)stack, (LootContext)context);
    }
}

