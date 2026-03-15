/*
 * Internal private/static methods:
 *   Lnet/minecraft/datafixer/fix/EntityFallDistanceFloatToDoubleFix;fixTypeEverywhereTyped(Ljava/lang/String;Lcom/mojang/datafixers/types/Type;Ljava/util/function/Function;)Lcom/mojang/datafixers/TypeRewriteRule;
 */
package net.minecraft.datafixer.fix;

import com.mojang.datafixers.DSL;
import com.mojang.datafixers.DataFix;
import com.mojang.datafixers.TypeRewriteRule;
import com.mojang.datafixers.Typed;
import com.mojang.datafixers.schemas.Schema;

public class EntityFallDistanceFloatToDoubleFix
extends DataFix {
    private final DSL.TypeReference typeReference;

    public EntityFallDistanceFloatToDoubleFix(Schema outputSchema, DSL.TypeReference typeReference) {
        super(outputSchema, false);
        this.typeReference = typeReference;
    }

    @Override
    protected TypeRewriteRule makeRule() {
        return this.fixTypeEverywhereTyped("EntityFallDistanceFloatToDoubleFixFor" + this.typeReference.typeName(), this.getOutputSchema().getType(this.typeReference), EntityFallDistanceFloatToDoubleFix::fixFallDistance);
    }

    private static Typed<?> fixFallDistance(Typed<?> typed) {
        return typed.update(DSL.remainderFinder(), dynamic2 -> dynamic2.renameAndFixField("FallDistance", "fall_distance", dynamic -> dynamic.createDouble(dynamic.asFloat(0.0f))));
    }
}

