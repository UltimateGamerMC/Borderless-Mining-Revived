/*
 * External method calls:
 *   Lnet/minecraft/util/profiler/Profiler;push(Ljava/util/function/Supplier;)V
 *   Lnet/minecraft/server/command/AbstractServerCommandSource;asResultConsumer()Lcom/mojang/brigadier/ResultConsumer;
 *   Lnet/minecraft/server/function/Tracer;traceCommandEnd(ILjava/lang/String;I)V
 *   Lnet/minecraft/server/command/AbstractServerCommandSource;handleException(Lcom/mojang/brigadier/exceptions/CommandSyntaxException;ZLnet/minecraft/server/function/Tracer;)V
 *
 * Internal private/static methods:
 *   Lnet/minecraft/command/FixedCommandAction;execute(Lnet/minecraft/server/command/AbstractServerCommandSource;Lnet/minecraft/command/CommandExecutionContext;Lnet/minecraft/command/Frame;)V
 */
package net.minecraft.command;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.context.ContextChain;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandExecutionContext;
import net.minecraft.command.ExecutionFlags;
import net.minecraft.command.Frame;
import net.minecraft.command.SourcedCommandAction;
import net.minecraft.server.command.AbstractServerCommandSource;
import net.minecraft.server.function.Tracer;

public class FixedCommandAction<T extends AbstractServerCommandSource<T>>
implements SourcedCommandAction<T> {
    private final String command;
    private final ExecutionFlags flags;
    private final CommandContext<T> context;

    public FixedCommandAction(String command, ExecutionFlags flags, CommandContext<T> context) {
        this.command = command;
        this.flags = flags;
        this.context = context;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void execute(T arg, CommandExecutionContext<T> arg2, Frame arg3) {
        arg2.getProfiler().push(() -> "execute " + this.command);
        try {
            arg2.decrementCommandQuota();
            int i = ContextChain.runExecutable(this.context, arg, AbstractServerCommandSource.asResultConsumer(), this.flags.isSilent());
            Tracer lv = arg2.getTracer();
            if (lv != null) {
                lv.traceCommandEnd(arg3.depth(), this.command, i);
            }
        } catch (CommandSyntaxException commandSyntaxException) {
            arg.handleException(commandSyntaxException, this.flags.isSilent(), arg2.getTracer());
        } finally {
            arg2.getProfiler().pop();
        }
    }

    @Override
    public /* synthetic */ void execute(Object object, CommandExecutionContext arg, Frame arg2) {
        this.execute((AbstractServerCommandSource)object, arg, arg2);
    }
}

