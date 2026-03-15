package net.minecraft.world.attribute;

import com.mojang.serialization.Codec;
import java.util.Map;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.math.Interpolator;
import net.minecraft.world.attribute.BooleanModifier;
import net.minecraft.world.attribute.ColorModifier;
import net.minecraft.world.attribute.EnvironmentAttribute;
import net.minecraft.world.attribute.FloatModifier;

public interface EnvironmentAttributeModifier<Subject, Argument> {
    public static final Map<Type, EnvironmentAttributeModifier<Boolean, ?>> BOOLEAN_MODIFIERS = Map.of(Type.AND, BooleanModifier.AND, Type.NAND, BooleanModifier.NAND, Type.OR, BooleanModifier.OR, Type.NOR, BooleanModifier.NOR, Type.XOR, BooleanModifier.XOR, Type.XNOR, BooleanModifier.XNOR);
    public static final Map<Type, EnvironmentAttributeModifier<Float, ?>> FLOAT_MODIFIERS = Map.of(Type.ALPHA_BLEND, FloatModifier.ALPHA_BLEND, Type.ADD, FloatModifier.ADD, Type.SUBTRACT, FloatModifier.SUBTRACT, Type.MULTIPLY, FloatModifier.MULTIPLY, Type.MINIMUM, FloatModifier.MINIMUM, Type.MAXIMUM, FloatModifier.MAXIMUM);
    public static final Map<Type, EnvironmentAttributeModifier<Integer, ?>> RGB = Map.of(Type.ALPHA_BLEND, ColorModifier.ALPHA_BLEND, Type.ADD, ColorModifier.ADD, Type.SUBTRACT, ColorModifier.SUBTRACT, Type.MULTIPLY, ColorModifier.MULTIPLY_RGB, Type.BLEND_TO_GRAY, ColorModifier.BLEND_TO_GRAY);
    public static final Map<Type, EnvironmentAttributeModifier<Integer, ?>> ARGB = Map.of(Type.ALPHA_BLEND, ColorModifier.ALPHA_BLEND, Type.ADD, ColorModifier.ADD, Type.SUBTRACT, ColorModifier.SUBTRACT, Type.MULTIPLY, ColorModifier.MULTIPLY_ARGB, Type.BLEND_TO_GRAY, ColorModifier.BLEND_TO_GRAY);

    public static <Value> EnvironmentAttributeModifier<Value, Value> override() {
        return OverrideModifier.INSTANCE;
    }

    public Subject apply(Subject var1, Argument var2);

    public Codec<Argument> argumentCodec(EnvironmentAttribute<Subject> var1);

    public Interpolator<Argument> argumentKeyframeLerp(EnvironmentAttribute<Subject> var1);

    public record OverrideModifier<Value>() implements EnvironmentAttributeModifier<Value, Value>
    {
        static final OverrideModifier<?> INSTANCE = new OverrideModifier();

        @Override
        public Value apply(Value object, Value object2) {
            return object2;
        }

        @Override
        public Codec<Value> argumentCodec(EnvironmentAttribute<Value> arg) {
            return arg.getCodec();
        }

        @Override
        public Interpolator<Value> argumentKeyframeLerp(EnvironmentAttribute<Value> arg) {
            return arg.getType().keyframeLerp();
        }
    }

    public static enum Type implements StringIdentifiable
    {
        OVERRIDE("override"),
        ALPHA_BLEND("alpha_blend"),
        ADD("add"),
        SUBTRACT("subtract"),
        MULTIPLY("multiply"),
        BLEND_TO_GRAY("blend_to_gray"),
        MINIMUM("minimum"),
        MAXIMUM("maximum"),
        AND("and"),
        NAND("nand"),
        OR("or"),
        NOR("nor"),
        XOR("xor"),
        XNOR("xnor");

        public static final Codec<Type> CODEC;
        private final String name;

        private Type(String name) {
            this.name = name;
        }

        @Override
        public String asString() {
            return this.name;
        }

        static {
            CODEC = StringIdentifiable.createCodec(Type::values);
        }
    }
}

