/*
 * External method calls:
 *   Lnet/minecraft/datafixer/FixUtil;compose([Ljava/util/function/Function;)Ljava/util/function/Function;
 *   Lnet/minecraft/util/Util;apply(Lcom/mojang/datafixers/Typed;Lcom/mojang/datafixers/types/Type;Ljava/util/function/UnaryOperator;)Lcom/mojang/datafixers/Typed;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/datafixer/fix/ArrowStoredWeaponFix;fixFor(Ljava/lang/String;)Ljava/util/function/Function;
 *   Lnet/minecraft/datafixer/fix/ArrowStoredWeaponFix;fixTypeEverywhereTyped(Ljava/lang/String;Lcom/mojang/datafixers/types/Type;Lcom/mojang/datafixers/types/Type;Ljava/util/function/Function;)Lcom/mojang/datafixers/TypeRewriteRule;
 *   Lnet/minecraft/datafixer/fix/ArrowStoredWeaponFix;method_59912(Ljava/lang/String;Lcom/mojang/datafixers/types/Type;Lcom/mojang/datafixers/types/Type;)Ljava/util/function/Function;
 */
package net.minecraft.datafixer.fix;

import com.mojang.datafixers.DSL;
import com.mojang.datafixers.DataFix;
import com.mojang.datafixers.OpticFinder;
import com.mojang.datafixers.TypeRewriteRule;
import com.mojang.datafixers.Typed;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.types.Type;
import java.util.function.Function;
import java.util.function.UnaryOperator;
import net.minecraft.datafixer.FixUtil;
import net.minecraft.datafixer.TypeReferences;
import net.minecraft.util.Util;

public class ArrowStoredWeaponFix
extends DataFix {
    public ArrowStoredWeaponFix(Schema outputSchema) {
        super(outputSchema, true);
    }

    @Override
    protected TypeRewriteRule makeRule() {
        Type<?> type = this.getInputSchema().getType(TypeReferences.ENTITY);
        Type<?> type2 = this.getOutputSchema().getType(TypeReferences.ENTITY);
        return this.fixTypeEverywhereTyped("Fix Arrow stored weapon", type, type2, FixUtil.compose(this.fixFor("minecraft:arrow"), this.fixFor("minecraft:spectral_arrow")));
    }

    private Function<Typed<?>, Typed<?>> fixFor(String entityId) {
        Type<?> type = this.getInputSchema().getChoiceType(TypeReferences.ENTITY, entityId);
        Type<?> type2 = this.getOutputSchema().getChoiceType(TypeReferences.ENTITY, entityId);
        return ArrowStoredWeaponFix.method_59912(entityId, type, type2);
    }

    private static <T> Function<Typed<?>, Typed<?>> method_59912(String name, Type<?> type, Type<T> type2) {
        OpticFinder<?> opticFinder = DSL.namedChoice(name, type);
        return typed2 -> typed2.updateTyped(opticFinder, type2, typed -> Util.apply(typed, type2, UnaryOperator.identity()));
    }
}

