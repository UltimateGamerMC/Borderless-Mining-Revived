/*
 * Internal private/static methods:
 *   Lnet/minecraft/datafixer/fix/LockComponentPredicateFix;fixLock(Lcom/mojang/serialization/Dynamic;)Lcom/mojang/serialization/Dynamic;
 */
package net.minecraft.datafixer.fix;

import com.google.common.escape.Escaper;
import com.google.common.escape.Escapers;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.serialization.Dynamic;
import java.util.Optional;
import net.minecraft.datafixer.fix.ComponentFix;
import org.jspecify.annotations.Nullable;

public class LockComponentPredicateFix
extends ComponentFix {
    public static final Escaper ESCAPER = Escapers.builder().addEscape('\"', "\\\"").addEscape('\\', "\\\\").build();

    public LockComponentPredicateFix(Schema outputSchema) {
        super(outputSchema, "LockComponentPredicateFix", "minecraft:lock");
    }

    @Override
    protected <T> @Nullable Dynamic<T> fixComponent(Dynamic<T> dynamic) {
        return LockComponentPredicateFix.fixLock(dynamic);
    }

    public static <T> @Nullable Dynamic<T> fixLock(Dynamic<T> dynamic) {
        Optional<String> optional = dynamic.asString().result();
        if (optional.isEmpty()) {
            return null;
        }
        if (optional.get().isEmpty()) {
            return null;
        }
        Dynamic dynamic2 = dynamic.createString("\"" + ESCAPER.escape(optional.get()) + "\"");
        Dynamic dynamic3 = dynamic.emptyMap().set("minecraft:custom_name", dynamic2);
        return dynamic.emptyMap().set("components", dynamic3);
    }
}

