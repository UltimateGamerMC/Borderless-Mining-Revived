/*
 * Internal private/static methods:
 *   Lnet/minecraft/util/packrat/TokenParsingRule;parse(Lnet/minecraft/util/packrat/ParsingState;)Ljava/lang/String;
 */
package net.minecraft.util.packrat;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.util.packrat.CursorExceptionType;
import net.minecraft.util.packrat.ParsingRule;
import net.minecraft.util.packrat.ParsingState;
import org.jspecify.annotations.Nullable;

public abstract class TokenParsingRule
implements ParsingRule<StringReader, String> {
    private final int minLength;
    private final int maxLength;
    private final CursorExceptionType<CommandSyntaxException> tooShortException;

    public TokenParsingRule(int minLength, CursorExceptionType<CommandSyntaxException> tooShortException) {
        this(minLength, Integer.MAX_VALUE, tooShortException);
    }

    public TokenParsingRule(int minLength, int maxLength, CursorExceptionType<CommandSyntaxException> tooShortException) {
        this.minLength = minLength;
        this.maxLength = maxLength;
        this.tooShortException = tooShortException;
    }

    @Override
    public @Nullable String parse(ParsingState<StringReader> arg) {
        int i;
        int j;
        StringReader stringReader = arg.getReader();
        String string = stringReader.getString();
        for (j = i = stringReader.getCursor(); j < string.length() && this.isValidChar(string.charAt(j)) && j - i < this.maxLength; ++j) {
        }
        int k = j - i;
        if (k < this.minLength) {
            arg.getErrors().add(arg.getCursor(), this.tooShortException);
            return null;
        }
        stringReader.setCursor(j);
        return string.substring(i, j);
    }

    protected abstract boolean isValidChar(char var1);

    @Override
    public /* synthetic */ @Nullable Object parse(ParsingState state) {
        return this.parse(state);
    }
}

