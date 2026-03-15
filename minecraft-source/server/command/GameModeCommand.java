/*
 * External method calls:
 *   Lnet/minecraft/server/command/CommandManager;literal(Ljava/lang/String;)Lcom/mojang/brigadier/builder/LiteralArgumentBuilder;
 *   Lnet/minecraft/server/command/CommandManager;requirePermissionLevel(Lnet/minecraft/command/permission/PermissionCheck;)Lnet/minecraft/command/permission/PermissionSourcePredicate;
 *   Lnet/minecraft/command/argument/GameModeArgumentType;gameMode()Lnet/minecraft/command/argument/GameModeArgumentType;
 *   Lnet/minecraft/server/command/CommandManager;argument(Ljava/lang/String;Lcom/mojang/brigadier/arguments/ArgumentType;)Lcom/mojang/brigadier/builder/RequiredArgumentBuilder;
 *   Lnet/minecraft/command/argument/EntityArgumentType;players()Lnet/minecraft/command/argument/EntityArgumentType;
 *   Lnet/minecraft/text/Text;translatable(Ljava/lang/String;)Lnet/minecraft/text/MutableText;
 *   Lnet/minecraft/server/command/ServerCommandSource;sendFeedback(Ljava/util/function/Supplier;Z)V
 *   Lnet/minecraft/text/Text;translatable(Ljava/lang/String;[Ljava/lang/Object;)Lnet/minecraft/text/MutableText;
 *   Lnet/minecraft/server/network/ServerPlayerEntity;sendMessage(Lnet/minecraft/text/Text;)V
 *   Lnet/minecraft/server/network/ServerPlayerEntity;changeGameMode(Lnet/minecraft/world/GameMode;)Z
 *
 * Internal private/static methods:
 *   Lnet/minecraft/server/command/GameModeCommand;execute(Lnet/minecraft/server/command/ServerCommandSource;Lnet/minecraft/server/network/ServerPlayerEntity;Lnet/minecraft/world/GameMode;)Z
 *   Lnet/minecraft/server/command/GameModeCommand;sendFeedback(Lnet/minecraft/server/command/ServerCommandSource;Lnet/minecraft/server/network/ServerPlayerEntity;Lnet/minecraft/world/GameMode;)V
 *   Lnet/minecraft/server/command/GameModeCommand;execute(Lcom/mojang/brigadier/context/CommandContext;Ljava/util/Collection;Lnet/minecraft/world/GameMode;)I
 */
package net.minecraft.server.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import java.util.Collection;
import java.util.Collections;
import net.minecraft.command.DefaultPermissions;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.command.argument.GameModeArgumentType;
import net.minecraft.command.permission.PermissionCheck;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.world.GameMode;
import net.minecraft.world.rule.GameRules;

public class GameModeCommand {
    public static final PermissionCheck PERMISSION_CHECK = new PermissionCheck.Require(DefaultPermissions.GAMEMASTERS);

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register((LiteralArgumentBuilder)((LiteralArgumentBuilder)CommandManager.literal("gamemode").requires(CommandManager.requirePermissionLevel(PERMISSION_CHECK))).then(((RequiredArgumentBuilder)CommandManager.argument("gamemode", GameModeArgumentType.gameMode()).executes(context -> GameModeCommand.execute(context, Collections.singleton(((ServerCommandSource)context.getSource()).getPlayerOrThrow()), GameModeArgumentType.getGameMode(context, "gamemode")))).then(CommandManager.argument("target", EntityArgumentType.players()).executes(context -> GameModeCommand.execute(context, EntityArgumentType.getPlayers(context, "target"), GameModeArgumentType.getGameMode(context, "gamemode"))))));
    }

    private static void sendFeedback(ServerCommandSource source, ServerPlayerEntity player, GameMode gameMode) {
        MutableText lv = Text.translatable("gameMode." + gameMode.getId());
        if (source.getEntity() == player) {
            source.sendFeedback(() -> Text.translatable("commands.gamemode.success.self", lv), true);
        } else {
            if (source.getWorld().getGameRules().getValue(GameRules.SEND_COMMAND_FEEDBACK).booleanValue()) {
                player.sendMessage(Text.translatable("gameMode.changed", lv));
            }
            source.sendFeedback(() -> Text.translatable("commands.gamemode.success.other", player.getDisplayName(), lv), true);
        }
    }

    private static int execute(CommandContext<ServerCommandSource> context, Collection<ServerPlayerEntity> targets, GameMode gameMode) {
        int i = 0;
        for (ServerPlayerEntity lv : targets) {
            if (!GameModeCommand.execute(context.getSource(), lv, gameMode)) continue;
            ++i;
        }
        return i;
    }

    public static void execute(ServerPlayerEntity target, GameMode gameMode) {
        GameModeCommand.execute(target.getCommandSource(), target, gameMode);
    }

    private static boolean execute(ServerCommandSource source, ServerPlayerEntity target, GameMode gameMode) {
        if (target.changeGameMode(gameMode)) {
            GameModeCommand.sendFeedback(source, target, gameMode);
            return true;
        }
        return false;
    }
}

