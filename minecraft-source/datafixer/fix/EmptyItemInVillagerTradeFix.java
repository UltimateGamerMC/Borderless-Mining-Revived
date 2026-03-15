/*
 * External method calls:
 *   Lnet/minecraft/datafixer/schema/IdentifierNormalizingSchema;normalize(Ljava/lang/String;)Ljava/lang/String;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/datafixer/fix/EmptyItemInVillagerTradeFix;writeFixAndRead(Ljava/lang/String;Lcom/mojang/datafixers/types/Type;Lcom/mojang/datafixers/types/Type;Ljava/util/function/Function;)Lcom/mojang/datafixers/TypeRewriteRule;
 */
package net.minecraft.datafixer.fix;

import com.mojang.datafixers.DataFix;
import com.mojang.datafixers.TypeRewriteRule;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.types.Type;
import com.mojang.serialization.Dynamic;
import net.minecraft.datafixer.TypeReferences;
import net.minecraft.datafixer.schema.IdentifierNormalizingSchema;

public class EmptyItemInVillagerTradeFix
extends DataFix {
    public EmptyItemInVillagerTradeFix(Schema outputSchema) {
        super(outputSchema, false);
    }

    @Override
    public TypeRewriteRule makeRule() {
        Type<?> type = this.getInputSchema().getType(TypeReferences.VILLAGER_TRADE);
        return this.writeFixAndRead("EmptyItemInVillagerTradeFix", type, type, villagerTradeDynamic -> {
            Dynamic dynamic2 = villagerTradeDynamic.get("buyB").orElseEmptyMap();
            String string = IdentifierNormalizingSchema.normalize(dynamic2.get("id").asString("minecraft:air"));
            int i = dynamic2.get("count").asInt(0);
            if (string.equals("minecraft:air") || i == 0) {
                return villagerTradeDynamic.remove("buyB");
            }
            return villagerTradeDynamic;
        });
    }
}

