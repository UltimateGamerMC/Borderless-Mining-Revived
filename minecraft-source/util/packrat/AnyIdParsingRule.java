/*
 * External method calls:
 *   Lnet/minecraft/util/Identifier;fromCommandInputNonEmpty(Lcom/mojang/brigadier/StringReader;)Lnet/minecraft/util/Identifier;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/util/packrat/AnyIdParsingRule;parse(Lnet/minecraft/util/packrat/ParsingState;)Lnet/minecraft/util/Identifier;
 */
package net.minecraft.util.packrat;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.util.Identifier;
import net.minecraft.util.packrat.ParsingRule;
import net.minecraft.util.packrat.ParsingState;
import org.jspecify.annotations.Nullable;

public class AnyIdParsingRule
implements ParsingRule<StringReader, Identifier> {
    public static final ParsingRule<StringReader, Identifier> INSTANCE = new AnyIdParsingRule();

    private AnyIdParsingRule() {
    }

    @Override
    public @Nullable Identifier parse(ParsingState<StringReader> arg) {
        arg.getReader().skipWhitespace();
        try {
            return Identifier.fromCommandInputNonEmpty(arg.getReader());
        } catch (CommandSyntaxException commandSyntaxException) {
            return null;
        }
    }

    @Override
    public /* synthetic */ @Nullable Object parse(ParsingState state) {
        return this.parse(state);
    }
}

