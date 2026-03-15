/*
 * Internal private/static methods:
 *   Lnet/minecraft/util/packrat/IdentifierSuggestable;possibleIds()Ljava/util/stream/Stream;
 */
package net.minecraft.util.packrat;

import com.mojang.brigadier.StringReader;
import java.util.stream.Stream;
import net.minecraft.util.Identifier;
import net.minecraft.util.packrat.ParsingState;
import net.minecraft.util.packrat.Suggestable;

public interface IdentifierSuggestable
extends Suggestable<StringReader> {
    public Stream<Identifier> possibleIds();

    @Override
    default public Stream<String> possibleValues(ParsingState<StringReader> arg) {
        return this.possibleIds().map(Identifier::toString);
    }
}

