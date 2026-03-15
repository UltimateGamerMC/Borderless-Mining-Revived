/*
 * External method calls:
 *   Lnet/minecraft/server/function/CommandFunctionManager;execute(Lnet/minecraft/server/function/CommandFunction;Lnet/minecraft/server/command/ServerCommandSource;)V
 *
 * Internal private/static methods:
 *   Lnet/minecraft/world/timer/FunctionTimerCallback;call(Lnet/minecraft/server/MinecraftServer;Lnet/minecraft/world/timer/Timer;J)V
 */
package net.minecraft.world.timer;

import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.function.CommandFunction;
import net.minecraft.server.function.CommandFunctionManager;
import net.minecraft.util.Identifier;
import net.minecraft.world.timer.Timer;
import net.minecraft.world.timer.TimerCallback;

public record FunctionTimerCallback(Identifier name) implements TimerCallback<MinecraftServer>
{
    public static final MapCodec<FunctionTimerCallback> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(((MapCodec)Identifier.CODEC.fieldOf("Name")).forGetter(FunctionTimerCallback::name)).apply((Applicative<FunctionTimerCallback, ?>)instance, FunctionTimerCallback::new));

    @Override
    public void call(MinecraftServer minecraftServer, Timer<MinecraftServer> arg, long l) {
        CommandFunctionManager lv = minecraftServer.getCommandFunctionManager();
        lv.getFunction(this.name).ifPresent(function -> lv.execute((CommandFunction<ServerCommandSource>)function, lv.getScheduledCommandSource()));
    }

    @Override
    public MapCodec<FunctionTimerCallback> getCodec() {
        return CODEC;
    }
}

