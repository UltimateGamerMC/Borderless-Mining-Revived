package net.minecraft.client.input;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.input.AbstractInput;

@Environment(value=EnvType.CLIENT)
public record MouseInput(@ButtonCode int button, @AbstractInput.Modifier int modifiers) implements AbstractInput
{
    @Override
    @ButtonCode
    public int getKeycode() {
        return this.button;
    }

    @ButtonCode
    public int button() {
        return this.button;
    }

    @Override
    @AbstractInput.Modifier
    public int modifiers() {
        return this.modifiers;
    }

    @Retention(value=RetentionPolicy.CLASS)
    @Target(value={ElementType.FIELD, ElementType.PARAMETER, ElementType.LOCAL_VARIABLE, ElementType.METHOD, ElementType.TYPE_USE})
    @Environment(value=EnvType.CLIENT)
    public static @interface ButtonCode {
    }

    @Retention(value=RetentionPolicy.CLASS)
    @Target(value={ElementType.FIELD, ElementType.PARAMETER, ElementType.LOCAL_VARIABLE, ElementType.METHOD, ElementType.TYPE_USE})
    @Environment(value=EnvType.CLIENT)
    public static @interface MouseAction {
    }
}

