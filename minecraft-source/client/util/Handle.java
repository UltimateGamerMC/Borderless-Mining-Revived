package net.minecraft.client.util;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(value=EnvType.CLIENT)
public interface Handle<T> {
    public static final Handle<?> EMPTY = () -> {
        throw new IllegalStateException("Cannot dereference handle with no underlying resource");
    };

    public static <T> Handle<T> empty() {
        return EMPTY;
    }

    public T get();
}

