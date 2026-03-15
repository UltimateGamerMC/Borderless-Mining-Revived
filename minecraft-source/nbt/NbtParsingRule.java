/*
 * External method calls:
 *   Lnet/minecraft/nbt/StringNbtReader;fromOps(Lcom/mojang/serialization/DynamicOps;)Lnet/minecraft/nbt/StringNbtReader;
 *   Lnet/minecraft/nbt/StringNbtReader;readAsArgument(Lcom/mojang/brigadier/StringReader;)Ljava/lang/Object;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/nbt/NbtParsingRule;parse(Lnet/minecraft/util/packrat/ParsingState;)Lcom/mojang/serialization/Dynamic;
 */
package net.minecraft.nbt;

import com.mojang.brigadier.StringReader;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.DynamicOps;
import net.minecraft.nbt.StringNbtReader;
import net.minecraft.util.packrat.ParsingRule;
import net.minecraft.util.packrat.ParsingState;
import org.jspecify.annotations.Nullable;

public class NbtParsingRule<T>
implements ParsingRule<StringReader, Dynamic<?>> {
    private final StringNbtReader<T> nbtReader;

    public NbtParsingRule(DynamicOps<T> ops) {
        this.nbtReader = StringNbtReader.fromOps(ops);
    }

    @Override
    public @Nullable Dynamic<T> parse(ParsingState<StringReader> arg) {
        arg.getReader().skipWhitespace();
        int i = arg.getCursor();
        try {
            return new Dynamic<T>(this.nbtReader.getOps(), this.nbtReader.readAsArgument(arg.getReader()));
        } catch (Exception exception) {
            arg.getErrors().add(i, exception);
            return null;
        }
    }

    @Override
    public /* synthetic */ @Nullable Object parse(ParsingState state) {
        return this.parse(state);
    }
}

