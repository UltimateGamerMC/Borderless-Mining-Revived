/*
 * External method calls:
 *   Lnet/minecraft/loot/condition/LootCondition;test(Ljava/lang/Object;)Z
 *   Lnet/minecraft/loot/condition/LootCondition;validate(Lnet/minecraft/loot/LootTableReporter;)V
 *
 * Internal private/static methods:
 *   Lnet/minecraft/enchantment/effect/EnchantmentEffectEntry;createRequirementsCodec(Lnet/minecraft/util/context/ContextType;)Lcom/mojang/serialization/Codec;
 */
package net.minecraft.enchantment.effect;

import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Optional;
import net.minecraft.loot.LootTableReporter;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.loot.context.LootContext;
import net.minecraft.util.ErrorReporter;
import net.minecraft.util.context.ContextType;

public record EnchantmentEffectEntry<T>(T effect, Optional<LootCondition> requirements) {
    public static Codec<LootCondition> createRequirementsCodec(ContextType lootContextType) {
        return LootCondition.CODEC.validate(condition -> {
            ErrorReporter.Impl lv = new ErrorReporter.Impl();
            LootTableReporter lv2 = new LootTableReporter(lv, lootContextType);
            condition.validate(lv2);
            if (!lv.isEmpty()) {
                return DataResult.error(() -> "Validation error in enchantment effect condition: " + lv.getErrorsAsString());
            }
            return DataResult.success(condition);
        });
    }

    public static <T> Codec<EnchantmentEffectEntry<T>> createCodec(Codec<T> effectCodec, ContextType lootContextType) {
        return RecordCodecBuilder.create(instance -> instance.group(((MapCodec)effectCodec.fieldOf("effect")).forGetter(EnchantmentEffectEntry::effect), EnchantmentEffectEntry.createRequirementsCodec(lootContextType).optionalFieldOf("requirements").forGetter(EnchantmentEffectEntry::requirements)).apply((Applicative<EnchantmentEffectEntry, ?>)instance, EnchantmentEffectEntry::new));
    }

    public boolean test(LootContext context) {
        if (this.requirements.isEmpty()) {
            return true;
        }
        return this.requirements.get().test(context);
    }
}

