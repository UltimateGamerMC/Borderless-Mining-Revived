/*
 * External method calls:
 *   Lnet/minecraft/command/ReturnValueConsumer;onResult(ZI)V
 *
 * Internal private/static methods:
 *   Lnet/minecraft/server/command/AbstractServerCommandSource;withReturnValueConsumer(Lnet/minecraft/command/ReturnValueConsumer;)Lnet/minecraft/server/command/AbstractServerCommandSource;
 *   Lnet/minecraft/server/command/AbstractServerCommandSource;handleException(Lcom/mojang/brigadier/exceptions/CommandExceptionType;Lcom/mojang/brigadier/Message;ZLnet/minecraft/server/function/Tracer;)V
 */
package net.minecraft.server.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.Message;
import com.mojang.brigadier.ResultConsumer;
import com.mojang.brigadier.exceptions.CommandExceptionType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.ReturnValueConsumer;
import net.minecraft.command.permission.PermissionSource;
import net.minecraft.server.function.Tracer;
import org.jspecify.annotations.Nullable;

public interface AbstractServerCommandSource<T extends AbstractServerCommandSource<T>>
extends PermissionSource {
    public T withReturnValueConsumer(ReturnValueConsumer var1);

    public ReturnValueConsumer getReturnValueConsumer();

    default public T withDummyReturnValueConsumer() {
        return this.withReturnValueConsumer(ReturnValueConsumer.EMPTY);
    }

    public CommandDispatcher<T> getDispatcher();

    public void handleException(CommandExceptionType var1, Message var2, boolean var3, @Nullable Tracer var4);

    public boolean isSilent();

    default public void handleException(CommandSyntaxException exception, boolean silent, @Nullable Tracer tracer) {
        this.handleException(exception.getType(), exception.getRawMessage(), silent, tracer);
    }

    public static <T extends AbstractServerCommandSource<T>> ResultConsumer<T> asResultConsumer() {
        return (context, success, result) -> ((AbstractServerCommandSource)context.getSource()).getReturnValueConsumer().onResult(success, result);
    }
}

