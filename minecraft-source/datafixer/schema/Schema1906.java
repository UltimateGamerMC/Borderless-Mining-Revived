/*
 * External method calls:
 *   Lnet/minecraft/datafixer/schema/IdentifierNormalizingSchema;registerBlockEntities(Lcom/mojang/datafixers/schemas/Schema;)Ljava/util/Map;
 *   Lnet/minecraft/datafixer/schema/Schema1458;itemsAndCustomName(Lcom/mojang/datafixers/schemas/Schema;)Lcom/mojang/datafixers/types/templates/TypeTemplate;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/datafixer/schema/Schema1906;method_16052(Lcom/mojang/datafixers/schemas/Schema;Ljava/util/Map;Ljava/lang/String;)V
 */
package net.minecraft.datafixer.schema;

import com.mojang.datafixers.DSL;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.types.templates.TypeTemplate;
import java.util.Map;
import java.util.function.Supplier;
import net.minecraft.datafixer.TypeReferences;
import net.minecraft.datafixer.schema.IdentifierNormalizingSchema;
import net.minecraft.datafixer.schema.Schema1458;

public class Schema1906
extends IdentifierNormalizingSchema {
    public Schema1906(int i, Schema schema) {
        super(i, schema);
    }

    @Override
    public Map<String, Supplier<TypeTemplate>> registerBlockEntities(Schema schema) {
        Map<String, Supplier<TypeTemplate>> map = super.registerBlockEntities(schema);
        Schema1906.method_16052(schema, map, "minecraft:barrel");
        Schema1906.method_16052(schema, map, "minecraft:smoker");
        Schema1906.method_16052(schema, map, "minecraft:blast_furnace");
        schema.register(map, "minecraft:lectern", (String name) -> DSL.optionalFields("Book", TypeReferences.ITEM_STACK.in(schema)));
        schema.registerSimple(map, "minecraft:bell");
        return map;
    }

    protected static void method_16052(Schema schema, Map<String, Supplier<TypeTemplate>> map, String name) {
        schema.register(map, name, () -> Schema1458.itemsAndCustomName(schema));
    }
}

