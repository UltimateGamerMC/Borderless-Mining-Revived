/*
 * External method calls:
 *   Lnet/minecraft/datafixer/FixUtil;withTypeChanged(Lcom/mojang/datafixers/types/Type;Lcom/mojang/datafixers/types/Type;Lcom/mojang/datafixers/types/Type;)Lcom/mojang/datafixers/types/Type;
 *   Lnet/minecraft/datafixer/fix/TextFixes;text(Lcom/mojang/serialization/DynamicOps;Ljava/lang/String;)Lcom/mojang/serialization/Dynamic;
 *   Lnet/minecraft/datafixer/FixUtil;withType(Lcom/mojang/datafixers/types/Type;Lcom/mojang/datafixers/Typed;)Lcom/mojang/datafixers/Typed;
 *   Lnet/minecraft/util/Util;apply(Lcom/mojang/datafixers/Typed;Lcom/mojang/datafixers/types/Type;Ljava/util/function/UnaryOperator;)Lcom/mojang/datafixers/Typed;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/datafixer/fix/BlockEntityCustomNameToTextFix;fixTypeEverywhereTyped(Ljava/lang/String;Lcom/mojang/datafixers/types/Type;Lcom/mojang/datafixers/types/Type;Ljava/util/function/Function;)Lcom/mojang/datafixers/TypeRewriteRule;
 */
package net.minecraft.datafixer.fix;

import com.mojang.datafixers.DSL;
import com.mojang.datafixers.DataFix;
import com.mojang.datafixers.OpticFinder;
import com.mojang.datafixers.TypeRewriteRule;
import com.mojang.datafixers.Typed;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.types.Type;
import com.mojang.serialization.Dynamic;
import java.util.Optional;
import java.util.Set;
import net.minecraft.datafixer.FixUtil;
import net.minecraft.datafixer.TypeReferences;
import net.minecraft.datafixer.fix.TextFixes;
import net.minecraft.datafixer.schema.IdentifierNormalizingSchema;
import net.minecraft.util.Util;

public class BlockEntityCustomNameToTextFix
extends DataFix {
    private static final Set<String> NAMEABLE_BLOCK_ENTITY_IDS = Set.of("minecraft:beacon", "minecraft:banner", "minecraft:brewing_stand", "minecraft:chest", "minecraft:trapped_chest", "minecraft:dispenser", "minecraft:dropper", "minecraft:enchanting_table", "minecraft:furnace", "minecraft:hopper", "minecraft:shulker_box");

    public BlockEntityCustomNameToTextFix(Schema outputSchema) {
        super(outputSchema, true);
    }

    @Override
    public TypeRewriteRule makeRule() {
        OpticFinder<String> opticFinder = DSL.fieldFinder("id", IdentifierNormalizingSchema.getIdentifierType());
        Type<?> type = this.getInputSchema().getType(TypeReferences.BLOCK_ENTITY);
        Type<?> type2 = this.getOutputSchema().getType(TypeReferences.BLOCK_ENTITY);
        Type<?> type3 = FixUtil.withTypeChanged(type, type, type2);
        return this.fixTypeEverywhereTyped("BlockEntityCustomNameToComponentFix", type, type2, (Typed<?> typed) -> {
            Optional optional = typed.getOptional(opticFinder);
            if (optional.isPresent() && !NAMEABLE_BLOCK_ENTITY_IDS.contains(optional.get())) {
                return FixUtil.withType(type2, typed);
            }
            return Util.apply(FixUtil.withType(type3, typed), type2, BlockEntityCustomNameToTextFix::fixCustomName);
        });
    }

    public static <T> Dynamic<T> fixCustomName(Dynamic<T> dynamic) {
        String string = dynamic.get("CustomName").asString("");
        if (string.isEmpty()) {
            return dynamic.remove("CustomName");
        }
        return dynamic.set("CustomName", TextFixes.text(dynamic.getOps(), string));
    }
}

