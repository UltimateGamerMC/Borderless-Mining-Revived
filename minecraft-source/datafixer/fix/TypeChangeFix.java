/*
 * External method calls:
 *   Lnet/minecraft/datafixer/FixUtil;withType(Lcom/mojang/datafixers/types/Type;Lcom/mojang/datafixers/Typed;)Lcom/mojang/datafixers/Typed;
 */
package net.minecraft.datafixer.fix;

import com.mojang.datafixers.DSL;
import com.mojang.datafixers.Typed;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.types.Type;
import net.minecraft.datafixer.FixUtil;
import net.minecraft.datafixer.fix.ChoiceFix;

public class TypeChangeFix
extends ChoiceFix {
    public TypeChangeFix(Schema outputSchema, String name, DSL.TypeReference type, String choiceName) {
        super(outputSchema, true, name, type, choiceName);
    }

    @Override
    protected Typed<?> transform(Typed<?> inputTyped) {
        Type<?> type = this.getOutputSchema().getChoiceType(this.type, this.choiceName);
        return FixUtil.withType(type, inputTyped);
    }
}

