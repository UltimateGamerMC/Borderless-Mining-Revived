/*
 * External method calls:
 *   Lnet/minecraft/util/StringIdentifiable;createCodec(Ljava/util/function/Supplier;)Lnet/minecraft/util/StringIdentifiable$EnumCodec;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/world/MoonPhase;method_75262()[Lnet/minecraft/world/MoonPhase;
 *   Lnet/minecraft/world/MoonPhase;values()[Lnet/minecraft/world/MoonPhase;
 */
package net.minecraft.world;

import com.mojang.serialization.Codec;
import net.minecraft.util.StringIdentifiable;

public enum MoonPhase implements StringIdentifiable
{
    FULL_MOON(0, "full_moon"),
    WANING_GIBBOUS(1, "waning_gibbous"),
    THIRD_QUARTER(2, "third_quarter"),
    WANING_CRESCENT(3, "waning_crescent"),
    NEW_MOON(4, "new_moon"),
    WAXING_CRESCENT(5, "waxing_crescent"),
    FIRST_QUARTER(6, "first_quarter"),
    WAXING_GIBBOUS(7, "waxing_gibbous");

    public static final Codec<MoonPhase> CODEC;
    public static final int COUNT;
    public static final int DAY_LENGTH = 24000;
    private final int index;
    private final String name;

    private MoonPhase(int index, String name) {
        this.index = index;
        this.name = name;
    }

    public int getIndex() {
        return this.index;
    }

    public int phaseTicks() {
        return this.index * 24000;
    }

    @Override
    public String asString() {
        return this.name;
    }

    static {
        CODEC = StringIdentifiable.createCodec(MoonPhase::values);
        COUNT = MoonPhase.values().length;
    }
}

