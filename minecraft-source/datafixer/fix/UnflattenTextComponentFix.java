/*
 * External method calls:
 *   Lnet/minecraft/util/LenientJsonParser;parse(Ljava/lang/String;)Lcom/google/gson/JsonElement;
 *   Lnet/minecraft/util/Util;readTyped(Lcom/mojang/datafixers/types/Type;Lcom/mojang/serialization/Dynamic;Z)Lcom/mojang/datafixers/Typed;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/datafixer/fix/UnflattenTextComponentFix;method_66142(Lcom/mojang/datafixers/types/Type;Lcom/mojang/datafixers/types/Type;)Lcom/mojang/datafixers/TypeRewriteRule;
 *   Lnet/minecraft/datafixer/fix/UnflattenTextComponentFix;fixTypeEverywhere(Ljava/lang/String;Lcom/mojang/datafixers/types/Type;Lcom/mojang/datafixers/types/Type;Ljava/util/function/Function;)Lcom/mojang/datafixers/TypeRewriteRule;
 *   Lnet/minecraft/datafixer/fix/UnflattenTextComponentFix;method_66145(Lcom/mojang/serialization/DynamicOps;Ljava/lang/String;)Lcom/mojang/serialization/Dynamic;
 */
package net.minecraft.datafixer.fix;

import com.google.gson.JsonElement;
import com.mojang.datafixers.DataFix;
import com.mojang.datafixers.TypeRewriteRule;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.types.Type;
import com.mojang.datafixers.util.Pair;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.JsonOps;
import net.minecraft.datafixer.TypeReferences;
import net.minecraft.util.LenientJsonParser;
import net.minecraft.util.Util;
import org.slf4j.Logger;

public class UnflattenTextComponentFix
extends DataFix {
    private static final Logger LOGGER = LogUtils.getLogger();

    public UnflattenTextComponentFix(Schema outputSchema) {
        super(outputSchema, true);
    }

    @Override
    protected TypeRewriteRule makeRule() {
        Type<Pair<String, String>> type = this.getInputSchema().getType(TypeReferences.TEXT_COMPONENT);
        Type<?> type2 = this.getOutputSchema().getType(TypeReferences.TEXT_COMPONENT);
        return this.method_66142(type, type2);
    }

    private <T> TypeRewriteRule method_66142(Type<Pair<String, String>> type, Type<T> type2) {
        return this.fixTypeEverywhere("UnflattenTextComponentFix", type, type2, dynamicOps -> pair -> Util.readTyped(type2, UnflattenTextComponentFix.method_66145(dynamicOps, (String)pair.getSecond()), true).getValue());
    }

    private static <T> Dynamic<T> method_66145(DynamicOps<T> dynamicOps, String string) {
        try {
            JsonElement jsonElement = LenientJsonParser.parse(string);
            if (!jsonElement.isJsonNull()) {
                return new Dynamic<T>(dynamicOps, JsonOps.INSTANCE.convertTo(dynamicOps, jsonElement));
            }
        } catch (Exception exception) {
            LOGGER.error("Failed to unflatten text component json: {}", (Object)string, (Object)exception);
        }
        return new Dynamic<T>(dynamicOps, dynamicOps.createString(string));
    }
}

