/*
 * Internal private/static methods:
 *   Lnet/minecraft/datafixer/fix/BlockEntityUuidFix;fixTypeEverywhereTyped(Ljava/lang/String;Lcom/mojang/datafixers/types/Type;Ljava/util/function/Function;)Lcom/mojang/datafixers/TypeRewriteRule;
 *   Lnet/minecraft/datafixer/fix/BlockEntityUuidFix;updateCompoundUuid(Lcom/mojang/serialization/Dynamic;Ljava/lang/String;Ljava/lang/String;)Ljava/util/Optional;
 *   Lnet/minecraft/datafixer/fix/BlockEntityUuidFix;updateStringUuid(Lcom/mojang/serialization/Dynamic;Ljava/lang/String;Ljava/lang/String;)Ljava/util/Optional;
 *   Lnet/minecraft/datafixer/fix/BlockEntityUuidFix;updateTyped(Lcom/mojang/datafixers/Typed;Ljava/lang/String;Ljava/util/function/Function;)Lcom/mojang/datafixers/Typed;
 */
package net.minecraft.datafixer.fix;

import com.mojang.datafixers.TypeRewriteRule;
import com.mojang.datafixers.Typed;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.serialization.Dynamic;
import net.minecraft.datafixer.TypeReferences;
import net.minecraft.datafixer.fix.AbstractUuidFix;

public class BlockEntityUuidFix
extends AbstractUuidFix {
    public BlockEntityUuidFix(Schema outputSchema) {
        super(outputSchema, TypeReferences.BLOCK_ENTITY);
    }

    @Override
    protected TypeRewriteRule makeRule() {
        return this.fixTypeEverywhereTyped("BlockEntityUUIDFix", this.getInputSchema().getType(this.typeReference), typed -> {
            typed = this.updateTyped((Typed<?>)typed, "minecraft:conduit", this::updateConduit);
            typed = this.updateTyped((Typed<?>)typed, "minecraft:skull", this::updateSkull);
            return typed;
        });
    }

    private Dynamic<?> updateSkull(Dynamic<?> skullDynamic) {
        return skullDynamic.get("Owner").get().map(ownerDynamic -> BlockEntityUuidFix.updateStringUuid(ownerDynamic, "Id", "Id").orElse((Dynamic<?>)ownerDynamic)).map(ownerDynamic -> skullDynamic.remove("Owner").set("SkullOwner", (Dynamic<?>)ownerDynamic)).result().orElse(skullDynamic);
    }

    private Dynamic<?> updateConduit(Dynamic<?> conduitDynamic) {
        return BlockEntityUuidFix.updateCompoundUuid(conduitDynamic, "target_uuid", "Target").orElse(conduitDynamic);
    }
}

