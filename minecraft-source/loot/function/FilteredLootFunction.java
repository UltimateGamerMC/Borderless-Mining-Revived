/*
 * External method calls:
 *   Lnet/minecraft/predicate/item/ItemPredicate;test(Lnet/minecraft/item/ItemStack;)Z
 *   Lnet/minecraft/loot/function/LootFunction;apply(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
 *   Lnet/minecraft/loot/function/ConditionalLootFunction;validate(Lnet/minecraft/loot/LootTableReporter;)V
 *   Lnet/minecraft/loot/LootTableReporter;makeChild(Lnet/minecraft/util/ErrorReporter$Context;)Lnet/minecraft/loot/LootTableReporter;
 *   Lnet/minecraft/loot/function/LootFunction;validate(Lnet/minecraft/loot/LootTableReporter;)V
 *
 * Internal private/static methods:
 *   Lnet/minecraft/loot/function/FilteredLootFunction;addConditionsField(Lcom/mojang/serialization/codecs/RecordCodecBuilder$Instance;)Lcom/mojang/datafixers/Products$P1;
 */
package net.minecraft.loot.function;

import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.List;
import java.util.Optional;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootTableReporter;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.function.ConditionalLootFunction;
import net.minecraft.loot.function.LootFunction;
import net.minecraft.loot.function.LootFunctionType;
import net.minecraft.loot.function.LootFunctionTypes;
import net.minecraft.predicate.item.ItemPredicate;
import net.minecraft.util.ErrorReporter;

public class FilteredLootFunction
extends ConditionalLootFunction {
    public static final MapCodec<FilteredLootFunction> CODEC = RecordCodecBuilder.mapCodec(instance -> FilteredLootFunction.addConditionsField(instance).and(instance.group(((MapCodec)ItemPredicate.CODEC.fieldOf("item_filter")).forGetter(lootFunction -> lootFunction.itemFilter), LootFunctionTypes.CODEC.optionalFieldOf("on_pass").forGetter(lootFunction -> lootFunction.onPass), LootFunctionTypes.CODEC.optionalFieldOf("on_fail").forGetter(lootFunction -> lootFunction.onFail))).apply((Applicative<FilteredLootFunction, ?>)instance, FilteredLootFunction::new));
    private final ItemPredicate itemFilter;
    private final Optional<LootFunction> onPass;
    private final Optional<LootFunction> onFail;

    FilteredLootFunction(List<LootCondition> conditions, ItemPredicate itemFilter, Optional<LootFunction> onPass, Optional<LootFunction> onFail) {
        super(conditions);
        this.itemFilter = itemFilter;
        this.onPass = onPass;
        this.onFail = onFail;
    }

    public LootFunctionType<FilteredLootFunction> getType() {
        return LootFunctionTypes.FILTERED;
    }

    @Override
    public ItemStack process(ItemStack stack, LootContext context) {
        Optional<LootFunction> optional;
        Optional<LootFunction> optional2 = optional = this.itemFilter.test(stack) ? this.onPass : this.onFail;
        if (optional.isPresent()) {
            return (ItemStack)optional.get().apply(stack, context);
        }
        return stack;
    }

    @Override
    public void validate(LootTableReporter reporter) {
        super.validate(reporter);
        this.onPass.ifPresent(lootFunction -> lootFunction.validate(reporter.makeChild(new ErrorReporter.MapElementContext("on_pass"))));
        this.onFail.ifPresent(lootFunction -> lootFunction.validate(reporter.makeChild(new ErrorReporter.MapElementContext("on_fail"))));
    }

    public static Builder builder(ItemPredicate itemFilter) {
        return new Builder(itemFilter);
    }

    public static class Builder
    extends ConditionalLootFunction.Builder<Builder> {
        private final ItemPredicate itemFilter;
        private Optional<LootFunction> onPass = Optional.empty();
        private Optional<LootFunction> onFail = Optional.empty();

        Builder(ItemPredicate itemFilter) {
            this.itemFilter = itemFilter;
        }

        @Override
        protected Builder getThisBuilder() {
            return this;
        }

        public Builder onPass(Optional<LootFunction> onPass) {
            this.onPass = onPass;
            return this;
        }

        public Builder onFail(Optional<LootFunction> onFail) {
            this.onFail = onFail;
            return this;
        }

        @Override
        public LootFunction build() {
            return new FilteredLootFunction(this.getConditions(), this.itemFilter, this.onPass, this.onFail);
        }

        @Override
        protected /* synthetic */ ConditionalLootFunction.Builder getThisBuilder() {
            return this.getThisBuilder();
        }
    }
}

