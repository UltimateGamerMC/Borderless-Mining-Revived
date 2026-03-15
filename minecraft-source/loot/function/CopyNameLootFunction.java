/*
 * External method calls:
 *   Lnet/minecraft/loot/context/LootEntityValueSource;cast(Lnet/minecraft/loot/context/LootEntityValueSource;)Lnet/minecraft/loot/context/LootEntityValueSource;
 *   Lnet/minecraft/loot/context/LootEntityValueSource;contextParam()Lnet/minecraft/util/context/ContextParameter;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/loot/function/CopyNameLootFunction;builder(Ljava/util/function/Function;)Lnet/minecraft/loot/function/ConditionalLootFunction$Builder;
 *   Lnet/minecraft/loot/function/CopyNameLootFunction;addConditionsField(Lcom/mojang/serialization/codecs/RecordCodecBuilder$Instance;)Lcom/mojang/datafixers/Products$P1;
 */
package net.minecraft.loot.function;

import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.List;
import java.util.Set;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootEntityValueSource;
import net.minecraft.loot.function.ConditionalLootFunction;
import net.minecraft.loot.function.LootFunctionType;
import net.minecraft.loot.function.LootFunctionTypes;
import net.minecraft.util.Nameable;
import net.minecraft.util.context.ContextParameter;

public class CopyNameLootFunction
extends ConditionalLootFunction {
    public static final MapCodec<CopyNameLootFunction> CODEC = RecordCodecBuilder.mapCodec(instance -> CopyNameLootFunction.addConditionsField(instance).and(((MapCodec)LootEntityValueSource.ENTITY_OR_BLOCK_ENTITY_CODEC.fieldOf("source")).forGetter(function -> function.source)).apply((Applicative<CopyNameLootFunction, ?>)instance, CopyNameLootFunction::new));
    private final LootEntityValueSource<Object> source;

    private CopyNameLootFunction(List<LootCondition> conditions, LootEntityValueSource<?> source) {
        super(conditions);
        this.source = LootEntityValueSource.cast(source);
    }

    public LootFunctionType<CopyNameLootFunction> getType() {
        return LootFunctionTypes.COPY_NAME;
    }

    @Override
    public Set<ContextParameter<?>> getAllowedParameters() {
        return Set.of(this.source.contextParam());
    }

    @Override
    public ItemStack process(ItemStack stack, LootContext context) {
        Object object = this.source.get(context);
        if (object instanceof Nameable) {
            Nameable lv = (Nameable)object;
            stack.set(DataComponentTypes.CUSTOM_NAME, lv.getCustomName());
        }
        return stack;
    }

    public static ConditionalLootFunction.Builder<?> builder(LootEntityValueSource<?> source) {
        return CopyNameLootFunction.builder((List<LootCondition> conditions) -> new CopyNameLootFunction((List<LootCondition>)conditions, source));
    }
}

