package net.minecraft.command;

import com.mojang.brigadier.exceptions.CommandSyntaxException;

@FunctionalInterface
public interface ArgumentGetter<T, R> {
    public R apply(T var1) throws CommandSyntaxException;
}

