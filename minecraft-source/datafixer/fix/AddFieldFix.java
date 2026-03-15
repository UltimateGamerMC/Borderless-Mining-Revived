/*
 * Internal private/static methods:
 *   Lnet/minecraft/datafixer/fix/AddFieldFix;fixTypeEverywhereTyped(Ljava/lang/String;Lcom/mojang/datafixers/types/Type;Lcom/mojang/datafixers/types/Type;Ljava/util/function/Function;)Lcom/mojang/datafixers/TypeRewriteRule;
 *   Lnet/minecraft/datafixer/fix/AddFieldFix;fix(Lcom/mojang/serialization/Dynamic;I)Lcom/mojang/serialization/Dynamic;
 */
package net.minecraft.datafixer.fix;

import com.mojang.datafixers.DSL;
import com.mojang.datafixers.DataFix;
import com.mojang.datafixers.TypeRewriteRule;
import com.mojang.datafixers.Typed;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.serialization.Dynamic;
import java.util.Locale;
import java.util.Optional;
import java.util.function.Function;

public class AddFieldFix
extends DataFix {
    private final String description;
    private final DSL.TypeReference typeReference;
    private final String fieldName;
    private final String[] copiedFields;
    private final Function<Dynamic<?>, Dynamic<?>> defaultValueGetter;

    public AddFieldFix(Schema outputSchema, DSL.TypeReference typeReference, String fieldName, Function<Dynamic<?>, Dynamic<?>> defaultValueGetter, String ... copiedFields) {
        super(outputSchema, false);
        this.description = "Adding field `" + fieldName + "` to type `" + typeReference.typeName().toLowerCase(Locale.ROOT) + "`";
        this.typeReference = typeReference;
        this.fieldName = fieldName;
        this.copiedFields = copiedFields;
        this.defaultValueGetter = defaultValueGetter;
    }

    @Override
    protected TypeRewriteRule makeRule() {
        return this.fixTypeEverywhereTyped(this.description, this.getInputSchema().getType(this.typeReference), this.getOutputSchema().getType(this.typeReference), (Typed<?> typed) -> typed.update(DSL.remainderFinder(), value -> this.fix((Dynamic<?>)value, 0)));
    }

    private Dynamic<?> fix(Dynamic<?> value, int index) {
        if (index >= this.copiedFields.length) {
            return value.set(this.fieldName, this.defaultValueGetter.apply(value));
        }
        Optional<Dynamic<?>> optional = value.get(this.copiedFields[index]).result();
        if (optional.isEmpty()) {
            return value;
        }
        return this.fix(optional.get(), index + 1);
    }
}

