/*
 * External method calls:
 *   Lnet/minecraft/datafixer/schema/IdentifierNormalizingSchema;registerBlockEntities(Lcom/mojang/datafixers/schemas/Schema;)Ljava/util/Map;
 *   Lnet/minecraft/datafixer/schema/Schema3439;method_66179(Lcom/mojang/datafixers/schemas/Schema;)Lcom/mojang/datafixers/types/templates/TypeTemplate;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/datafixer/schema/Schema3439_1;register(Ljava/util/Map;Ljava/lang/String;Ljava/util/function/Supplier;)V
 */
package net.minecraft.datafixer.schema;

import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.types.templates.TypeTemplate;
import java.util.Map;
import java.util.function.Supplier;
import net.minecraft.datafixer.schema.IdentifierNormalizingSchema;
import net.minecraft.datafixer.schema.Schema3439;

public class Schema3439_1
extends IdentifierNormalizingSchema {
    public Schema3439_1(int i, Schema schema) {
        super(i, schema);
    }

    @Override
    public Map<String, Supplier<TypeTemplate>> registerBlockEntities(Schema schema) {
        Map<String, Supplier<TypeTemplate>> map = super.registerBlockEntities(schema);
        this.register(map, "minecraft:hanging_sign", () -> Schema3439.method_66179(schema));
        return map;
    }
}

