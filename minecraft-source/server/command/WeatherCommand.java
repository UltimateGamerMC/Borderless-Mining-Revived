/*
 * External method calls:
 *   Lnet/minecraft/server/command/CommandManager;literal(Ljava/lang/String;)Lcom/mojang/brigadier/builder/LiteralArgumentBuilder;
 *   Lnet/minecraft/server/command/CommandManager;requirePermissionLevel(Lnet/minecraft/command/permission/PermissionCheck;)Lnet/minecraft/command/permission/PermissionSourcePredicate;
 *   Lnet/minecraft/command/argument/TimeArgumentType;time(I)Lnet/minecraft/command/argument/TimeArgumentType;
 *   Lnet/minecraft/server/command/CommandManager;argument(Ljava/lang/String;Lcom/mojang/brigadier/arguments/ArgumentType;)Lcom/mojang/brigadier/builder/RequiredArgumentBuilder;
 *   Lnet/minecraft/server/command/ServerCommandSource;sendFeedback(Ljava/util/function/Supplier;Z)V
 *   Lnet/minecraft/text/Text;translatable(Ljava/lang/String;)Lnet/minecraft/text/MutableText;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/server/command/WeatherCommand;processDuration(Lnet/minecraft/server/command/ServerCommandSource;ILnet/minecraft/util/math/intprovider/IntProvider;)I
 *   Lnet/minecraft/server/command/WeatherCommand;executeThunder(Lnet/minecraft/server/command/ServerCommandSource;I)I
 *   Lnet/minecraft/server/command/WeatherCommand;executeRain(Lnet/minecraft/server/command/ServerCommandSource;I)I
 *   Lnet/minecraft/server/command/WeatherCommand;executeClear(Lnet/minecraft/server/command/ServerCommandSource;I)I
 */
package net.minecraft.server.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.command.argument.TimeArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.intprovider.IntProvider;

public class WeatherCommand {
    private static final int DEFAULT_DURATION = -1;

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)CommandManager.literal("weather").requires(CommandManager.requirePermissionLevel(CommandManager.GAMEMASTERS_CHECK))).then(((LiteralArgumentBuilder)CommandManager.literal("clear").executes(context -> WeatherCommand.executeClear((ServerCommandSource)context.getSource(), -1))).then(CommandManager.argument("duration", TimeArgumentType.time(1)).executes(context -> WeatherCommand.executeClear((ServerCommandSource)context.getSource(), IntegerArgumentType.getInteger(context, "duration")))))).then(((LiteralArgumentBuilder)CommandManager.literal("rain").executes(context -> WeatherCommand.executeRain((ServerCommandSource)context.getSource(), -1))).then(CommandManager.argument("duration", TimeArgumentType.time(1)).executes(context -> WeatherCommand.executeRain((ServerCommandSource)context.getSource(), IntegerArgumentType.getInteger(context, "duration")))))).then(((LiteralArgumentBuilder)CommandManager.literal("thunder").executes(context -> WeatherCommand.executeThunder((ServerCommandSource)context.getSource(), -1))).then(CommandManager.argument("duration", TimeArgumentType.time(1)).executes(context -> WeatherCommand.executeThunder((ServerCommandSource)context.getSource(), IntegerArgumentType.getInteger(context, "duration"))))));
    }

    private static int processDuration(ServerCommandSource source, int duration, IntProvider provider) {
        if (duration == -1) {
            return provider.get(source.getServer().getOverworld().getRandom());
        }
        return duration;
    }

    private static int executeClear(ServerCommandSource source, int duration) {
        source.getServer().getOverworld().setWeather(WeatherCommand.processDuration(source, duration, ServerWorld.CLEAR_WEATHER_DURATION_PROVIDER), 0, false, false);
        source.sendFeedback(() -> Text.translatable("commands.weather.set.clear"), true);
        return duration;
    }

    private static int executeRain(ServerCommandSource source, int duration) {
        source.getServer().getOverworld().setWeather(0, WeatherCommand.processDuration(source, duration, ServerWorld.RAIN_WEATHER_DURATION_PROVIDER), true, false);
        source.sendFeedback(() -> Text.translatable("commands.weather.set.rain"), true);
        return duration;
    }

    private static int executeThunder(ServerCommandSource source, int duration) {
        source.getServer().getOverworld().setWeather(0, WeatherCommand.processDuration(source, duration, ServerWorld.THUNDER_WEATHER_DURATION_PROVIDER), true, true);
        source.sendFeedback(() -> Text.translatable("commands.weather.set.thunder"), true);
        return duration;
    }
}

