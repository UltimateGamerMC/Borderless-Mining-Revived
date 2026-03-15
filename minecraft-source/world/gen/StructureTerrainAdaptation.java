/*
 * External method calls:
 *   Lnet/minecraft/util/StringIdentifiable;createCodec(Ljava/util/function/Supplier;)Lnet/minecraft/util/StringIdentifiable$EnumCodec;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/world/gen/StructureTerrainAdaptation;method_36756()[Lnet/minecraft/world/gen/StructureTerrainAdaptation;
 */
package net.minecraft.world.gen;

import com.mojang.serialization.Codec;
import net.minecraft.util.StringIdentifiable;

public enum StructureTerrainAdaptation implements StringIdentifiable
{
    NONE("none"),
    BURY("bury"),
    BEARD_THIN("beard_thin"),
    BEARD_BOX("beard_box"),
    ENCAPSULATE("encapsulate");

    public static final Codec<StructureTerrainAdaptation> CODEC;
    private final String name;

    private StructureTerrainAdaptation(String name) {
        this.name = name;
    }

    @Override
    public String asString() {
        return this.name;
    }

    static {
        CODEC = StringIdentifiable.createCodec(StructureTerrainAdaptation::values);
    }
}

