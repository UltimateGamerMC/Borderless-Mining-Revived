/*
 * External method calls:
 *   Lnet/minecraft/datafixer/FixUtil;withType(Lcom/mojang/datafixers/types/Type;Lcom/mojang/datafixers/Typed;)Lcom/mojang/datafixers/Typed;
 *   Lnet/minecraft/util/Util;apply(Lcom/mojang/datafixers/Typed;Lcom/mojang/datafixers/types/Type;Ljava/util/function/UnaryOperator;)Lcom/mojang/datafixers/Typed;
 *   Lnet/minecraft/util/Util;readTyped(Lcom/mojang/datafixers/types/Type;Lcom/mojang/serialization/Dynamic;)Lcom/mojang/datafixers/Typed;
 *   Lnet/minecraft/datafixer/fix/TextFixes;text(Lcom/mojang/serialization/DynamicOps;Ljava/lang/String;)Lcom/mojang/serialization/Dynamic;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/datafixer/fix/EntityCustomNameToTextFix;fixTypeEverywhereTyped(Ljava/lang/String;Lcom/mojang/datafixers/types/Type;Lcom/mojang/datafixers/types/Type;Ljava/util/function/Function;)Lcom/mojang/datafixers/TypeRewriteRule;
 *   Lnet/minecraft/datafixer/fix/EntityCustomNameToTextFix;method_66066(Lcom/mojang/serialization/DynamicOps;Ljava/lang/String;Ljava/lang/String;)Lcom/mojang/serialization/Dynamic;
 *   Lnet/minecraft/datafixer/fix/EntityCustomNameToTextFix;method_66064(Lcom/mojang/datafixers/Typed;Lcom/mojang/datafixers/types/Type;Lcom/mojang/datafixers/OpticFinder;Lcom/mojang/datafixers/OpticFinder;Lcom/mojang/datafixers/types/Type;)Lcom/mojang/datafixers/Typed;
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
import com.mojang.serialization.DynamicOps;
import java.util.Optional;
import net.minecraft.datafixer.FixUtil;
import net.minecraft.datafixer.TypeReferences;
import net.minecraft.datafixer.fix.TextFixes;
import net.minecraft.datafixer.schema.IdentifierNormalizingSchema;
import net.minecraft.util.Util;

public class EntityCustomNameToTextFix
extends DataFix {
    public EntityCustomNameToTextFix(Schema schema) {
        super(schema, true);
    }

    @Override
    public TypeRewriteRule makeRule() {
        Type<?> type = this.getInputSchema().getType(TypeReferences.ENTITY);
        Type<?> type2 = this.getOutputSchema().getType(TypeReferences.ENTITY);
        OpticFinder<String> opticFinder = DSL.fieldFinder("id", IdentifierNormalizingSchema.getIdentifierType());
        OpticFinder<?> opticFinder2 = type.findField("CustomName");
        Type<?> type3 = type2.findFieldType("CustomName");
        return this.fixTypeEverywhereTyped("EntityCustomNameToComponentFix", type, type2, (Typed<?> typed) -> EntityCustomNameToTextFix.method_66064(typed, type2, opticFinder, opticFinder2, type3));
    }

    private static <T> Typed<?> method_66064(Typed<?> typed, Type<?> type, OpticFinder<String> opticFinder, OpticFinder<String> opticFinder2, Type<T> type2) {
        Optional<String> optional = typed.getOptional(opticFinder2);
        if (optional.isEmpty()) {
            return FixUtil.withType(type, typed);
        }
        if (optional.get().isEmpty()) {
            return Util.apply(typed, type, dynamic -> dynamic.remove("CustomName"));
        }
        String string = typed.getOptional(opticFinder).orElse("");
        Dynamic<?> dynamic2 = EntityCustomNameToTextFix.method_66066(typed.getOps(), optional.get(), string);
        return typed.set(opticFinder2, Util.readTyped(type2, dynamic2));
    }

    private static <T> Dynamic<T> method_66066(DynamicOps<T> dynamicOps, String string, String string2) {
        if ("minecraft:commandblock_minecart".equals(string2)) {
            return new Dynamic<T>(dynamicOps, dynamicOps.createString(string));
        }
        return TextFixes.text(dynamicOps, string);
    }
}

