/*
 * Internal private/static methods:
 *   Lnet/minecraft/util/packrat/UnquotedStringParsingRule;parse(Lnet/minecraft/util/packrat/ParsingState;)Ljava/lang/String;
 */
package net.minecraft.util.packrat;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.util.packrat.CursorExceptionType;
import net.minecraft.util.packrat.ParsingRule;
import net.minecraft.util.packrat.ParsingState;
import org.jspecify.annotations.Nullable;

public class UnquotedStringParsingRule
implements ParsingRule<StringReader, String> {
    private final int minLength;
    private final CursorExceptionType<CommandSyntaxException> tooShortException;

    public UnquotedStringParsingRule(int minLength, CursorExceptionType<CommandSyntaxException> tooShortException) {
        this.minLength = minLength;
        this.tooShortException = tooShortException;
    }

    @Override
    public @Nullable String parse(ParsingState<StringReader> arg) {
        arg.getReader().skipWhitespace();
        int i = arg.getCursor();
        String string = arg.getReader().readUnquotedString();
        if (string.length() < this.minLength) {
            arg.getErrors().add(i, this.tooShortException);
            return null;
        }
        return string;
    }

    @Override
    public /* synthetic */ @Nullable Object parse(ParsingState state) {
        return this.parse(state);
    }
}

