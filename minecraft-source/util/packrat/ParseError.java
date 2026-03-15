package net.minecraft.util.packrat;

import net.minecraft.util.packrat.Suggestable;

public record ParseError<S>(int cursor, Suggestable<S> suggestions, Object reason) {
}

