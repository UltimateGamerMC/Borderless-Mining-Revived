/*
 * External method calls:
 *   Lnet/minecraft/loot/LootTableReporter;report(Lnet/minecraft/util/ErrorReporter$Error;)V
 *   Lnet/minecraft/loot/condition/LootCondition;validate(Lnet/minecraft/loot/LootTableReporter;)V
 *   Lnet/minecraft/loot/context/LootContext;predicate(Lnet/minecraft/loot/condition/LootCondition;)Lnet/minecraft/loot/context/LootContext$Entry;
 *   Lnet/minecraft/loot/context/LootContext;markActive(Lnet/minecraft/loot/context/LootContext$Entry;)Z
 *   Lnet/minecraft/loot/condition/LootCondition;test(Ljava/lang/Object;)Z
 *   Lnet/minecraft/loot/context/LootContext;markInactive(Lnet/minecraft/loot/context/LootContext$Entry;)V
 *   Lnet/minecraft/loot/LootTableReporter;makeChild(Lnet/minecraft/util/ErrorReporter$Context;Lnet/minecraft/registry/RegistryKey;)Lnet/minecraft/loot/LootTableReporter;
 *   Lnet/minecraft/registry/RegistryKey;createCodec(Lnet/minecraft/registry/RegistryKey;)Lcom/mojang/serialization/Codec;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/loot/condition/ReferenceLootCondition;test(Lnet/minecraft/loot/context/LootContext;)Z
 */
package net.minecraft.loot.condition;

import com.mojang.datafixers.kinds.Applicative;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.loot.LootTableReporter;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.loot.condition.LootConditionType;
import net.minecraft.loot.condition.LootConditionTypes;
import net.minecraft.loot.context.LootContext;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.ErrorReporter;
import org.slf4j.Logger;

public record ReferenceLootCondition(RegistryKey<LootCondition> id) implements LootCondition
{
    private static final Logger LOGGER = LogUtils.getLogger();
    public static final MapCodec<ReferenceLootCondition> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(((MapCodec)RegistryKey.createCodec(RegistryKeys.PREDICATE).fieldOf("name")).forGetter(ReferenceLootCondition::id)).apply((Applicative<ReferenceLootCondition, ?>)instance, ReferenceLootCondition::new));

    @Override
    public LootConditionType getType() {
        return LootConditionTypes.REFERENCE;
    }

    @Override
    public void validate(LootTableReporter reporter) {
        if (!reporter.canUseReferences()) {
            reporter.report(new LootTableReporter.ReferenceNotAllowedError(this.id));
            return;
        }
        if (reporter.isInStack(this.id)) {
            reporter.report(new LootTableReporter.RecursionError(this.id));
            return;
        }
        LootCondition.super.validate(reporter);
        reporter.getDataLookup().getOptionalEntry(this.id).ifPresentOrElse(entry -> ((LootCondition)entry.value()).validate(reporter.makeChild(new ErrorReporter.ReferenceLootTableContext(this.id), this.id)), () -> reporter.report(new LootTableReporter.MissingElementError(this.id)));
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public boolean test(LootContext arg) {
        LootCondition lv = arg.getLookup().getOptionalEntry(this.id).map(RegistryEntry.Reference::value).orElse(null);
        if (lv == null) {
            LOGGER.warn("Tried using unknown condition table called {}", (Object)this.id.getValue());
            return false;
        }
        LootContext.Entry<LootCondition> lv2 = LootContext.predicate(lv);
        if (arg.markActive(lv2)) {
            try {
                boolean bl = lv.test(arg);
                return bl;
            } finally {
                arg.markInactive(lv2);
            }
        }
        LOGGER.warn("Detected infinite loop in loot tables");
        return false;
    }

    public static LootCondition.Builder builder(RegistryKey<LootCondition> key) {
        return () -> new ReferenceLootCondition(key);
    }

    @Override
    public /* synthetic */ boolean test(Object context) {
        return this.test((LootContext)context);
    }
}

