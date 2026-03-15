/*
 * Internal private/static methods:
 *   Lnet/minecraft/client/gl/UniformType;method_67774()[Lnet/minecraft/client/gl/UniformType;
 */
package net.minecraft.client.gl;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(value=EnvType.CLIENT)
public enum UniformType {
    UNIFORM_BUFFER("ubo"),
    TEXEL_BUFFER("utb");

    final String name;

    private UniformType(String name) {
        this.name = name;
    }
}

