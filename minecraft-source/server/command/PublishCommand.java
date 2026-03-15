/*
 * External method calls:
 *   Lnet/minecraft/server/command/CommandManager;literal(Ljava/lang/String;)Lcom/mojang/brigadier/builder/LiteralArgumentBuilder;
 *   Lnet/minecraft/server/command/CommandManager;requirePermissionLevel(Lnet/minecraft/command/permission/PermissionCheck;)Lnet/minecraft/command/permission/PermissionSourcePredicate;
 *   Lnet/minecraft/server/command/CommandManager;argument(Ljava/lang/String;Lcom/mojang/brigadier/arguments/ArgumentType;)Lcom/mojang/brigadier/builder/RequiredArgumentBuilder;
 *   Lnet/minecraft/command/argument/GameModeArgumentType;gameMode()Lnet/minecraft/command/argument/GameModeArgumentType;
 *   Lnet/minecraft/server/MinecraftServer;openToLan(Lnet/minecraft/world/GameMode;ZI)Z
 *   Lnet/minecraft/server/command/ServerCommandSource;sendFeedback(Ljava/util/function/Supplier;Z)V
 *   Lnet/minecraft/text/Texts;bracketedCopyable(Ljava/lang/String;)Lnet/minecraft/text/MutableText;
 *   Lnet/minecraft/text/Text;translatable(Ljava/lang/String;[Ljava/lang/Object;)Lnet/minecraft/text/MutableText;
 *   Lnet/minecraft/text/Text;stringifiedTranslatable(Ljava/lang/String;[Ljava/lang/Object;)Lnet/minecraft/text/MutableText;
 *   Lnet/minecraft/text/Text;translatable(Ljava/lang/String;)Lnet/minecraft/text/MutableText;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/server/command/PublishCommand;execute(Lnet/minecraft/server/command/ServerCommandSource;IZLnet/minecraft/world/GameMode;)I
 */
package net.minecraft.server.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.minecraft.command.argument.GameModeArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.text.Texts;
import net.minecraft.util.NetworkUtils;
import net.minecraft.world.GameMode;
import org.jspecify.annotations.Nullable;

public class PublishCommand {
    private static final SimpleCommandExceptionType FAILED_EXCEPTION = new SimpleCommandExceptionType(Text.translatable("commands.publish.failed"));
    private static final DynamicCommandExceptionType ALREADY_PUBLISHED_EXCEPTION = new DynamicCommandExceptionType(port -> Text.stringifiedTranslatable("commands.publish.alreadyPublished", port));

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)CommandManager.literal("publish").requires(CommandManager.requirePermissionLevel(CommandManager.OWNERS_CHECK))).executes(context -> PublishCommand.execute((ServerCommandSource)context.getSource(), NetworkUtils.findLocalPort(), false, null))).then(((RequiredArgumentBuilder)CommandManager.argument("allowCommands", BoolArgumentType.bool()).executes(context -> PublishCommand.execute((ServerCommandSource)context.getSource(), NetworkUtils.findLocalPort(), BoolArgumentType.getBool(context, "allowCommands"), null))).then(((RequiredArgumentBuilder)CommandManager.argument("gamemode", GameModeArgumentType.gameMode()).executes(context -> PublishCommand.execute((ServerCommandSource)context.getSource(), NetworkUtils.findLocalPort(), BoolArgumentType.getBool(context, "allowCommands"), GameModeArgumentType.getGameMode(context, "gamemode")))).then(CommandManager.argument("port", IntegerArgumentType.integer(0, 65535)).executes(context -> PublishCommand.execute((ServerCommandSource)context.getSource(), IntegerArgumentType.getInteger(context, "port"), BoolArgumentType.getBool(context, "allowCommands"), GameModeArgumentType.getGameMode(context, "gamemode")))))));
    }

    private static int execute(ServerCommandSource source, int port, boolean allowCommands, @Nullable GameMode gameMode) throws CommandSyntaxException {
        if (source.getServer().isRemote()) {
            throw ALREADY_PUBLISHED_EXCEPTION.create(source.getServer().getServerPort());
        }
        if (!source.getServer().openToLan(gameMode, allowCommands, port)) {
            throw FAILED_EXCEPTION.create();
        }
        source.sendFeedback(() -> PublishCommand.getStartedText(port), true);
        return port;
    }

    public static MutableText getStartedText(int port) {
        MutableText lv = Texts.bracketedCopyable(String.valueOf(port));
        return Text.translatable("commands.publish.started", lv);
    }
}

