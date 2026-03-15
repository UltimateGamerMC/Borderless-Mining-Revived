/*
 * Internal private/static methods:
 *   Lnet/minecraft/world/attribute/BooleanModifier;apply(Ljava/lang/Boolean;Ljava/lang/Boolean;)Ljava/lang/Boolean;
 *   Lnet/minecraft/world/attribute/BooleanModifier;method_75716()[Lnet/minecraft/world/attribute/BooleanModifier;
 */
package net.minecraft.world.attribute;

import com.mojang.serialization.Codec;
import net.minecraft.util.math.Interpolator;
import net.minecraft.world.attribute.EnvironmentAttribute;
import net.minecraft.world.attribute.EnvironmentAttributeModifier;

public enum BooleanModifier implements EnvironmentAttributeModifier<Boolean, Boolean>
{
    AND,
    NAND,
    OR,
    NOR,
    XOR,
    XNOR;


    @Override
    public Boolean apply(Boolean boolean_, Boolean boolean2) {
        return switch (this.ordinal()) {
            default -> throw new MatchException(null, null);
            case 0 -> boolean2 != false && boolean_ != false;
            case 1 -> boolean2 == false || boolean_ == false;
            case 2 -> boolean2 != false || boolean_ != false;
            case 3 -> boolean2 == false && boolean_ == false;
            case 4 -> boolean2 ^ boolean_;
            case 5 -> boolean2 == boolean_;
        };
    }

    @Override
    public Codec<Boolean> argumentCodec(EnvironmentAttribute<Boolean> arg) {
        return Codec.BOOL;
    }

    @Override
    public Interpolator<Boolean> argumentKeyframeLerp(EnvironmentAttribute<Boolean> arg) {
        return Interpolator.first();
    }

    @Override
    public /* synthetic */ Object apply(Object object, Object object2) {
        return this.apply((Boolean)object, (Boolean)object2);
    }
}

