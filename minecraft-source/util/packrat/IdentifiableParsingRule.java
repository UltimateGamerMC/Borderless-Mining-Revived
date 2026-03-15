/*
 * External method calls:
 *   Lnet/minecraft/util/packrat/CursorExceptionType;create(Lcom/mojang/brigadier/exceptions/SimpleCommandExceptionType;)Lnet/minecraft/util/packrat/CursorExceptionType;
 *   Lnet/minecraft/util/packrat/ParsingState;parse(Lnet/minecraft/util/packrat/ParsingRuleEntry;)Ljava/lang/Object;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/util/packrat/IdentifiableParsingRule;parse(Lcom/mojang/brigadier/ImmutableStringReader;Lnet/minecraft/util/Identifier;)Ljava/lang/Object;
 */
package net.minecraft.util.packrat;

import com.mojang.brigadier.ImmutableStringReader;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.util.Identifier;
import net.minecraft.util.packrat.CursorExceptionType;
import net.minecraft.util.packrat.IdentifierSuggestable;
import net.minecraft.util.packrat.ParsingRule;
import net.minecraft.util.packrat.ParsingRuleEntry;
import net.minecraft.util.packrat.ParsingState;
import org.jspecify.annotations.Nullable;

public abstract class IdentifiableParsingRule<C, V>
implements ParsingRule<StringReader, V>,
IdentifierSuggestable {
    private final ParsingRuleEntry<StringReader, Identifier> idParsingRule;
    protected final C callbacks;
    private final CursorExceptionType<CommandSyntaxException> exception;

    protected IdentifiableParsingRule(ParsingRuleEntry<StringReader, Identifier> idParsingRule, C callbacks) {
        this.idParsingRule = idParsingRule;
        this.callbacks = callbacks;
        this.exception = CursorExceptionType.create(Identifier.COMMAND_EXCEPTION);
    }

    @Override
    public @Nullable V parse(ParsingState<StringReader> state) {
        state.getReader().skipWhitespace();
        int i = state.getCursor();
        Identifier lv = state.parse(this.idParsingRule);
        if (lv != null) {
            try {
                return this.parse(state.getReader(), lv);
            } catch (Exception exception) {
                state.getErrors().add(i, this, exception);
                return null;
            }
        }
        state.getErrors().add(i, this, this.exception);
        return null;
    }

    protected abstract V parse(ImmutableStringReader var1, Identifier var2) throws Exception;
}

