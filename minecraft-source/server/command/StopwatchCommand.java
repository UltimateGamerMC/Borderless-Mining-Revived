/*
 * External method calls:
 *   Lnet/minecraft/server/command/CommandManager;literal(Ljava/lang/String;)Lcom/mojang/brigadier/builder/LiteralArgumentBuilder;
 *   Lnet/minecraft/server/command/CommandManager;requirePermissionLevel(Lnet/minecraft/command/permission/PermissionCheck;)Lnet/minecraft/command/permission/PermissionSourcePredicate;
 *   Lnet/minecraft/command/argument/IdentifierArgumentType;identifier()Lnet/minecraft/command/argument/IdentifierArgumentType;
 *   Lnet/minecraft/server/command/CommandManager;argument(Ljava/lang/String;Lcom/mojang/brigadier/arguments/ArgumentType;)Lcom/mojang/brigadier/builder/RequiredArgumentBuilder;
 *   Lnet/minecraft/server/command/ServerCommandSource;sendFeedback(Ljava/util/function/Supplier;Z)V
 *   Lnet/minecraft/world/timer/stopwatch/StopwatchPersistentState;update(Lnet/minecraft/util/Identifier;Ljava/util/function/UnaryOperator;)Z
 *   Lnet/minecraft/text/Text;of(Lnet/minecraft/util/Identifier;)Lnet/minecraft/text/Text;
 *   Lnet/minecraft/text/Text;translatable(Ljava/lang/String;[Ljava/lang/Object;)Lnet/minecraft/text/MutableText;
 *   Lnet/minecraft/world/timer/stopwatch/StopwatchPersistentState;keys()Ljava/util/List;
 *   Lnet/minecraft/command/CommandSource;suggestIdentifiers(Ljava/lang/Iterable;Lcom/mojang/brigadier/suggestion/SuggestionsBuilder;)Ljava/util/concurrent/CompletableFuture;
 *   Lnet/minecraft/text/Text;stringifiedTranslatable(Ljava/lang/String;[Ljava/lang/Object;)Lnet/minecraft/text/MutableText;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/server/command/StopwatchCommand;executeRemove(Lnet/minecraft/server/command/ServerCommandSource;Lnet/minecraft/util/Identifier;)I
 *   Lnet/minecraft/server/command/StopwatchCommand;executeRestart(Lnet/minecraft/server/command/ServerCommandSource;Lnet/minecraft/util/Identifier;)I
 *   Lnet/minecraft/server/command/StopwatchCommand;executeQuery(Lnet/minecraft/server/command/ServerCommandSource;Lnet/minecraft/util/Identifier;D)I
 *   Lnet/minecraft/server/command/StopwatchCommand;executeCreate(Lnet/minecraft/server/command/ServerCommandSource;Lnet/minecraft/util/Identifier;)I
 */
package net.minecraft.server.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import net.minecraft.command.CommandSource;
import net.minecraft.command.argument.IdentifierArgumentType;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.world.timer.stopwatch.Stopwatch;
import net.minecraft.world.timer.stopwatch.StopwatchPersistentState;

public class StopwatchCommand {
    private static final DynamicCommandExceptionType ALREADY_EXISTS_EXCEPTION = new DynamicCommandExceptionType(name -> Text.stringifiedTranslatable("commands.stopwatch.already_exists", name));
    public static final DynamicCommandExceptionType DOES_NOT_EXIST_EXCEPTION = new DynamicCommandExceptionType(name -> Text.stringifiedTranslatable("commands.stopwatch.does_not_exist", name));
    public static final SuggestionProvider<ServerCommandSource> STOPWATCH_SUGGESTION_PROVIDER = (context, builder) -> CommandSource.suggestIdentifiers(((ServerCommandSource)context.getSource()).getServer().getStopwatchPersistentState().keys(), builder);

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)CommandManager.literal("stopwatch").requires(CommandManager.requirePermissionLevel(CommandManager.GAMEMASTERS_CHECK))).then(CommandManager.literal("create").then((ArgumentBuilder<ServerCommandSource, ?>)CommandManager.argument("id", IdentifierArgumentType.identifier()).executes(context -> StopwatchCommand.executeCreate((ServerCommandSource)context.getSource(), IdentifierArgumentType.getIdentifier(context, "id")))))).then(CommandManager.literal("query").then((ArgumentBuilder<ServerCommandSource, ?>)((RequiredArgumentBuilder)CommandManager.argument("id", IdentifierArgumentType.identifier()).suggests(STOPWATCH_SUGGESTION_PROVIDER).then((ArgumentBuilder<ServerCommandSource, ?>)CommandManager.argument("scale", DoubleArgumentType.doubleArg()).executes(context -> StopwatchCommand.executeQuery((ServerCommandSource)context.getSource(), IdentifierArgumentType.getIdentifier(context, "id"), DoubleArgumentType.getDouble(context, "scale"))))).executes(context -> StopwatchCommand.executeQuery((ServerCommandSource)context.getSource(), IdentifierArgumentType.getIdentifier(context, "id"), 1.0))))).then(CommandManager.literal("restart").then((ArgumentBuilder<ServerCommandSource, ?>)CommandManager.argument("id", IdentifierArgumentType.identifier()).suggests(STOPWATCH_SUGGESTION_PROVIDER).executes(context -> StopwatchCommand.executeRestart((ServerCommandSource)context.getSource(), IdentifierArgumentType.getIdentifier(context, "id")))))).then(CommandManager.literal("remove").then((ArgumentBuilder<ServerCommandSource, ?>)CommandManager.argument("id", IdentifierArgumentType.identifier()).suggests(STOPWATCH_SUGGESTION_PROVIDER).executes(context -> StopwatchCommand.executeRemove((ServerCommandSource)context.getSource(), IdentifierArgumentType.getIdentifier(context, "id"))))));
    }

    private static int executeCreate(ServerCommandSource source, Identifier id) throws CommandSyntaxException {
        Stopwatch lv2;
        MinecraftServer minecraftServer = source.getServer();
        StopwatchPersistentState lv = minecraftServer.getStopwatchPersistentState();
        if (!lv.add(id, lv2 = new Stopwatch(StopwatchPersistentState.getTimeMs()))) {
            throw ALREADY_EXISTS_EXCEPTION.create(id);
        }
        source.sendFeedback(() -> Text.translatable("commands.stopwatch.create.success", Text.of(id)), true);
        return 1;
    }

    private static int executeQuery(ServerCommandSource source, Identifier id, double scale) throws CommandSyntaxException {
        MinecraftServer minecraftServer = source.getServer();
        StopwatchPersistentState lv = minecraftServer.getStopwatchPersistentState();
        Stopwatch lv2 = lv.get(id);
        if (lv2 == null) {
            throw DOES_NOT_EXIST_EXCEPTION.create(id);
        }
        long l = StopwatchPersistentState.getTimeMs();
        double e = lv2.getElapsedTimeSeconds(l);
        source.sendFeedback(() -> Text.translatable("commands.stopwatch.query", Text.of(id), e), true);
        return (int)(e * scale);
    }

    private static int executeRestart(ServerCommandSource source, Identifier id) throws CommandSyntaxException {
        MinecraftServer minecraftServer = source.getServer();
        StopwatchPersistentState lv = minecraftServer.getStopwatchPersistentState();
        if (!lv.update(id, stopwatch -> new Stopwatch(StopwatchPersistentState.getTimeMs()))) {
            throw DOES_NOT_EXIST_EXCEPTION.create(id);
        }
        source.sendFeedback(() -> Text.translatable("commands.stopwatch.restart.success", Text.of(id)), true);
        return 1;
    }

    private static int executeRemove(ServerCommandSource source, Identifier id) throws CommandSyntaxException {
        MinecraftServer minecraftServer = source.getServer();
        StopwatchPersistentState lv = minecraftServer.getStopwatchPersistentState();
        if (!lv.remove(id)) {
            throw DOES_NOT_EXIST_EXCEPTION.create(id);
        }
        source.sendFeedback(() -> Text.translatable("commands.stopwatch.remove.success", Text.of(id)), true);
        return 1;
    }
}

