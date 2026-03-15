/*
 * External method calls:
 *   Lnet/minecraft/registry/Registry;register(Lnet/minecraft/registry/Registry;Ljava/lang/String;Ljava/lang/Object;)Ljava/lang/Object;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/structure/rule/blockentity/RuleBlockEntityModifierType;register(Ljava/lang/String;Lcom/mojang/serialization/MapCodec;)Lnet/minecraft/structure/rule/blockentity/RuleBlockEntityModifierType;
 */
package net.minecraft.structure.rule.blockentity;

import com.mojang.serialization.MapCodec;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.structure.rule.blockentity.AppendLootRuleBlockEntityModifier;
import net.minecraft.structure.rule.blockentity.AppendStaticRuleBlockEntityModifier;
import net.minecraft.structure.rule.blockentity.ClearRuleBlockEntityModifier;
import net.minecraft.structure.rule.blockentity.PassthroughRuleBlockEntityModifier;
import net.minecraft.structure.rule.blockentity.RuleBlockEntityModifier;

public interface RuleBlockEntityModifierType<P extends RuleBlockEntityModifier> {
    public static final RuleBlockEntityModifierType<ClearRuleBlockEntityModifier> CLEAR = RuleBlockEntityModifierType.register("clear", ClearRuleBlockEntityModifier.CODEC);
    public static final RuleBlockEntityModifierType<PassthroughRuleBlockEntityModifier> PASSTHROUGH = RuleBlockEntityModifierType.register("passthrough", PassthroughRuleBlockEntityModifier.CODEC);
    public static final RuleBlockEntityModifierType<AppendStaticRuleBlockEntityModifier> APPEND_STATIC = RuleBlockEntityModifierType.register("append_static", AppendStaticRuleBlockEntityModifier.CODEC);
    public static final RuleBlockEntityModifierType<AppendLootRuleBlockEntityModifier> APPEND_LOOT = RuleBlockEntityModifierType.register("append_loot", AppendLootRuleBlockEntityModifier.CODEC);

    public MapCodec<P> codec();

    private static <P extends RuleBlockEntityModifier> RuleBlockEntityModifierType<P> register(String id, MapCodec<P> codec) {
        return Registry.register(Registries.RULE_BLOCK_ENTITY_MODIFIER, id, () -> codec);
    }
}

