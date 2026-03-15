/*
 * External method calls:
 *   Lnet/minecraft/util/StringIdentifiable;createBasicCodec(Ljava/util/function/Supplier;)Lcom/mojang/serialization/Codec;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/client/texture/MipmapStrategy;method_76036()[Lnet/minecraft/client/texture/MipmapStrategy;
 */
package net.minecraft.client.texture;

import com.mojang.serialization.Codec;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.StringIdentifiable;

@Environment(value=EnvType.CLIENT)
public enum MipmapStrategy implements StringIdentifiable
{
    AUTO("auto"),
    MEAN("mean"),
    CUTOUT("cutout"),
    STRICT_CUTOUT("strict_cutout"),
    DARK_CUTOUT("dark_cutout");

    public static final Codec<MipmapStrategy> CODEC;
    private final String name;

    private MipmapStrategy(String name) {
        this.name = name;
    }

    @Override
    public String asString() {
        return this.name;
    }

    static {
        CODEC = StringIdentifiable.createBasicCodec(MipmapStrategy::values);
    }
}

