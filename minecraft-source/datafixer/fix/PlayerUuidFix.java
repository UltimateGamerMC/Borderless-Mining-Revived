/*
 * External method calls:
 *   Lnet/minecraft/datafixer/fix/EntityUuidFix;updateLiving(Lcom/mojang/serialization/Dynamic;)Lcom/mojang/serialization/Dynamic;
 *   Lnet/minecraft/datafixer/fix/EntityUuidFix;updateSelfUuid(Lcom/mojang/serialization/Dynamic;)Lcom/mojang/serialization/Dynamic;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/datafixer/fix/PlayerUuidFix;fixTypeEverywhereTyped(Ljava/lang/String;Lcom/mojang/datafixers/types/Type;Ljava/util/function/Function;)Lcom/mojang/datafixers/TypeRewriteRule;
 *   Lnet/minecraft/datafixer/fix/PlayerUuidFix;updateRegularMostLeast(Lcom/mojang/serialization/Dynamic;Ljava/lang/String;Ljava/lang/String;)Ljava/util/Optional;
 */
package net.minecraft.datafixer.fix;

import com.mojang.datafixers.DSL;
import com.mojang.datafixers.OpticFinder;
import com.mojang.datafixers.TypeRewriteRule;
import com.mojang.datafixers.Typed;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.serialization.Dynamic;
import net.minecraft.datafixer.TypeReferences;
import net.minecraft.datafixer.fix.AbstractUuidFix;
import net.minecraft.datafixer.fix.EntityUuidFix;

public class PlayerUuidFix
extends AbstractUuidFix {
    public PlayerUuidFix(Schema outputSchema) {
        super(outputSchema, TypeReferences.PLAYER);
    }

    @Override
    protected TypeRewriteRule makeRule() {
        return this.fixTypeEverywhereTyped("PlayerUUIDFix", this.getInputSchema().getType(this.typeReference), playerTyped -> {
            OpticFinder<?> opticFinder = playerTyped.getType().findField("RootVehicle");
            return playerTyped.updateTyped(opticFinder, opticFinder.type(), (Typed<?> rootVehicleTyped) -> rootVehicleTyped.update(DSL.remainderFinder(), rootVehicleDynamic -> PlayerUuidFix.updateRegularMostLeast(rootVehicleDynamic, "Attach", "Attach").orElse((Dynamic<?>)rootVehicleDynamic))).update(DSL.remainderFinder(), playerDynamic -> EntityUuidFix.updateSelfUuid(EntityUuidFix.updateLiving(playerDynamic)));
        });
    }
}

