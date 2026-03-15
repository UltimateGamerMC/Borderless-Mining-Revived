/*
 * External method calls:
 *   Lnet/minecraft/util/StringIdentifiable;createCodec(Ljava/util/function/Supplier;)Lnet/minecraft/util/StringIdentifiable$EnumCodec;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/util/TriState;method_61347()[Lnet/minecraft/util/TriState;
 */
package net.minecraft.util;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import java.util.function.Function;
import net.minecraft.util.StringIdentifiable;

public enum TriState implements StringIdentifiable
{
    TRUE("true"),
    FALSE("false"),
    DEFAULT("default");

    public static final Codec<TriState> CODEC;
    private final String name;

    private TriState(String name) {
        this.name = name;
    }

    public static TriState ofBoolean(boolean value) {
        return value ? TRUE : FALSE;
    }

    public boolean asBoolean(boolean fallback) {
        return switch (this.ordinal()) {
            case 0 -> true;
            case 1 -> false;
            default -> fallback;
        };
    }

    @Override
    public String asString() {
        return this.name;
    }

    static {
        CODEC = Codec.either(Codec.BOOL, StringIdentifiable.createCodec(TriState::values)).xmap(either -> either.map(TriState::ofBoolean, Function.identity()), triState -> switch (triState.ordinal()) {
            default -> throw new MatchException(null, null);
            case 2 -> Either.right(triState);
            case 0 -> Either.left(true);
            case 1 -> Either.left(false);
        });
    }
}

