/*
 * External method calls:
 *   Lnet/minecraft/loot/LootTableReporter;report(Lnet/minecraft/util/ErrorReporter$Error;)V
 *   Lnet/minecraft/loot/function/ConditionalLootFunction;validate(Lnet/minecraft/loot/LootTableReporter;)V
 *   Lnet/minecraft/loot/context/LootContext;itemModifier(Lnet/minecraft/loot/function/LootFunction;)Lnet/minecraft/loot/context/LootContext$Entry;
 *   Lnet/minecraft/loot/context/LootContext;markActive(Lnet/minecraft/loot/context/LootContext$Entry;)Z
 *   Lnet/minecraft/loot/function/LootFunction;apply(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
 *   Lnet/minecraft/loot/context/LootContext;markInactive(Lnet/minecraft/loot/context/LootContext$Entry;)V
 *   Lnet/minecraft/loot/LootTableReporter;makeChild(Lnet/minecraft/util/ErrorReporter$Context;Lnet/minecraft/registry/RegistryKey;)Lnet/minecraft/loot/LootTableReporter;
 *   Lnet/minecraft/loot/function/LootFunction;validate(Lnet/minecraft/loot/LootTableReporter;)V
 *   Lnet/minecraft/registry/RegistryKey;createCodec(Lnet/minecraft/registry/RegistryKey;)Lcom/mojang/serialization/Codec;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/loot/function/ReferenceLootFunction;builder(Ljava/util/function/Function;)Lnet/minecraft/loot/function/ConditionalLootFunction$Builder;
 *   Lnet/minecraft/loot/function/ReferenceLootFunction;addConditionsField(Lcom/mojang/serialization/codecs/RecordCodecBuilder$Instance;)Lcom/mojang/datafixers/Products$P1;
 */
package net.minecraft.loot.function;

import com.mojang.datafixers.kinds.Applicative;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.List;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootTableReporter;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.function.ConditionalLootFunction;
import net.minecraft.loot.function.LootFunction;
import net.minecraft.loot.function.LootFunctionType;
import net.minecraft.loot.function.LootFunctionTypes;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.ErrorReporter;
import org.slf4j.Logger;

public class ReferenceLootFunction
extends ConditionalLootFunction {
    private static final Logger LOGGER = LogUtils.getLogger();
    public static final MapCodec<ReferenceLootFunction> CODEC = RecordCodecBuilder.mapCodec(instance -> ReferenceLootFunction.addConditionsField(instance).and(((MapCodec)RegistryKey.createCodec(RegistryKeys.ITEM_MODIFIER).fieldOf("name")).forGetter(function -> function.name)).apply((Applicative<ReferenceLootFunction, ?>)instance, ReferenceLootFunction::new));
    private final RegistryKey<LootFunction> name;

    private ReferenceLootFunction(List<LootCondition> conditions, RegistryKey<LootFunction> name) {
        super(conditions);
        this.name = name;
    }

    public LootFunctionType<ReferenceLootFunction> getType() {
        return LootFunctionTypes.REFERENCE;
    }

    @Override
    public void validate(LootTableReporter reporter) {
        if (!reporter.canUseReferences()) {
            reporter.report(new LootTableReporter.ReferenceNotAllowedError(this.name));
            return;
        }
        if (reporter.isInStack(this.name)) {
            reporter.report(new LootTableReporter.RecursionError(this.name));
            return;
        }
        super.validate(reporter);
        reporter.getDataLookup().getOptionalEntry(this.name).ifPresentOrElse(arg2 -> ((LootFunction)arg2.value()).validate(reporter.makeChild(new ErrorReporter.ReferenceLootTableContext(this.name), this.name)), () -> reporter.report(new LootTableReporter.MissingElementError(this.name)));
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    protected ItemStack process(ItemStack stack, LootContext context) {
        LootFunction lv = context.getLookup().getOptionalEntry(this.name).map(RegistryEntry::value).orElse(null);
        if (lv == null) {
            LOGGER.warn("Unknown function: {}", (Object)this.name.getValue());
            return stack;
        }
        LootContext.Entry<LootFunction> lv2 = LootContext.itemModifier(lv);
        if (context.markActive(lv2)) {
            try {
                ItemStack itemStack = (ItemStack)lv.apply(stack, context);
                return itemStack;
            } finally {
                context.markInactive(lv2);
            }
        }
        LOGGER.warn("Detected infinite loop in loot tables");
        return stack;
    }

    public static ConditionalLootFunction.Builder<?> builder(RegistryKey<LootFunction> name) {
        return ReferenceLootFunction.builder((List<LootCondition> conditions) -> new ReferenceLootFunction((List<LootCondition>)conditions, name));
    }
}

