/*
 * External method calls:
 *   Lnet/minecraft/util/packrat/Literals;createReader(Ljava/lang/String;I)Lcom/mojang/brigadier/StringReader;
 */
package net.minecraft.util.packrat;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.minecraft.util.packrat.Literals;

public interface CursorExceptionType<T extends Exception> {
    public T create(String var1, int var2);

    public static CursorExceptionType<CommandSyntaxException> create(SimpleCommandExceptionType type) {
        return (input, cursor) -> type.createWithContext(Literals.createReader(input, cursor));
    }

    public static CursorExceptionType<CommandSyntaxException> create(DynamicCommandExceptionType type, String arg) {
        return (input, cursor) -> type.createWithContext(Literals.createReader(input, cursor), arg);
    }
}

