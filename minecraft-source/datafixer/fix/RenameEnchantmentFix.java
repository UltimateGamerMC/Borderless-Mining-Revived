/*
 * External method calls:
 *   Lnet/minecraft/datafixer/schema/IdentifierNormalizingSchema;normalize(Ljava/lang/String;)Ljava/lang/String;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/datafixer/fix/RenameEnchantmentFix;fixTypeEverywhereTyped(Ljava/lang/String;Lcom/mojang/datafixers/types/Type;Ljava/util/function/Function;)Lcom/mojang/datafixers/TypeRewriteRule;
 *   Lnet/minecraft/datafixer/fix/RenameEnchantmentFix;fixIds(Lcom/mojang/serialization/Dynamic;Ljava/lang/String;)Lcom/mojang/serialization/Dynamic;
 */
package net.minecraft.datafixer.fix;

import com.mojang.datafixers.DSL;
import com.mojang.datafixers.DataFix;
import com.mojang.datafixers.OpticFinder;
import com.mojang.datafixers.TypeRewriteRule;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.types.Type;
import com.mojang.serialization.Dynamic;
import java.util.Map;
import java.util.function.Function;
import net.minecraft.datafixer.TypeReferences;
import net.minecraft.datafixer.schema.IdentifierNormalizingSchema;

public class RenameEnchantmentFix
extends DataFix {
    final String name;
    final Map<String, String> oldToNewIds;

    public RenameEnchantmentFix(Schema outputSchema, String name, Map<String, String> oldToNewIds) {
        super(outputSchema, false);
        this.name = name;
        this.oldToNewIds = oldToNewIds;
    }

    @Override
    protected TypeRewriteRule makeRule() {
        Type<?> type = this.getInputSchema().getType(TypeReferences.ITEM_STACK);
        OpticFinder<?> opticFinder = type.findField("tag");
        return this.fixTypeEverywhereTyped(this.name, type, itemStackTyped -> itemStackTyped.updateTyped(opticFinder, itemTagTyped -> itemTagTyped.update(DSL.remainderFinder(), this::fixIds)));
    }

    private Dynamic<?> fixIds(Dynamic<?> itemTagDynamic) {
        itemTagDynamic = this.fixIds(itemTagDynamic, "Enchantments");
        itemTagDynamic = this.fixIds(itemTagDynamic, "StoredEnchantments");
        return itemTagDynamic;
    }

    private Dynamic<?> fixIds(Dynamic<?> itemTagDynamic, String enchantmentsKey) {
        return itemTagDynamic.update(enchantmentsKey, enchantmentsDynamic -> enchantmentsDynamic.asStreamOpt().map(enchantments -> enchantments.map(enchantmentDynamic -> enchantmentDynamic.update("id", idDynamic -> idDynamic.asString().map(oldId -> enchantmentDynamic.createString(this.oldToNewIds.getOrDefault(IdentifierNormalizingSchema.normalize(oldId), (String)oldId))).mapOrElse(Function.identity(), error -> idDynamic)))).map(enchantmentsDynamic::createList).mapOrElse(Function.identity(), error -> enchantmentsDynamic));
    }
}

