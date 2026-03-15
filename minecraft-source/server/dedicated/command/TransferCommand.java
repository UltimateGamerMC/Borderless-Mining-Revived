/*
 * External method calls:
 *   Lnet/minecraft/server/command/CommandManager;literal(Ljava/lang/String;)Lcom/mojang/brigadier/builder/LiteralArgumentBuilder;
 *   Lnet/minecraft/server/command/CommandManager;requirePermissionLevel(Lnet/minecraft/command/permission/PermissionCheck;)Lnet/minecraft/command/permission/PermissionSourcePredicate;
 *   Lnet/minecraft/server/command/CommandManager;argument(Ljava/lang/String;Lcom/mojang/brigadier/arguments/ArgumentType;)Lcom/mojang/brigadier/builder/RequiredArgumentBuilder;
 *   Lnet/minecraft/command/argument/EntityArgumentType;players()Lnet/minecraft/command/argument/EntityArgumentType;
 *   Lnet/minecraft/server/network/ServerPlayNetworkHandler;sendPacket(Lnet/minecraft/network/packet/Packet;)V
 *   Lnet/minecraft/server/command/ServerCommandSource;sendFeedback(Ljava/util/function/Supplier;Z)V
 *   Lnet/minecraft/text/Text;translatable(Ljava/lang/String;[Ljava/lang/Object;)Lnet/minecraft/text/MutableText;
 *   Lnet/minecraft/text/Text;translatable(Ljava/lang/String;)Lnet/minecraft/text/MutableText;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/server/dedicated/command/TransferCommand;executeTransfer(Lnet/minecraft/server/command/ServerCommandSource;Ljava/lang/String;ILjava/util/Collection;)I
 */
package net.minecraft.server.dedicated.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import java.util.Collection;
import java.util.List;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.network.packet.s2c.common.ServerTransferS2CPacket;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class TransferCommand {
    private static final SimpleCommandExceptionType NO_PLAYERS_EXCEPTION = new SimpleCommandExceptionType(Text.translatable("commands.transfer.error.no_players"));

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register((LiteralArgumentBuilder)((LiteralArgumentBuilder)CommandManager.literal("transfer").requires(CommandManager.requirePermissionLevel(CommandManager.ADMINS_CHECK))).then(((RequiredArgumentBuilder)CommandManager.argument("hostname", StringArgumentType.string()).executes(context -> TransferCommand.executeTransfer((ServerCommandSource)context.getSource(), StringArgumentType.getString(context, "hostname"), 25565, List.of(((ServerCommandSource)context.getSource()).getPlayerOrThrow())))).then(((RequiredArgumentBuilder)CommandManager.argument("port", IntegerArgumentType.integer(1, 65535)).executes(context -> TransferCommand.executeTransfer((ServerCommandSource)context.getSource(), StringArgumentType.getString(context, "hostname"), IntegerArgumentType.getInteger(context, "port"), List.of(((ServerCommandSource)context.getSource()).getPlayerOrThrow())))).then(CommandManager.argument("players", EntityArgumentType.players()).executes(context -> TransferCommand.executeTransfer((ServerCommandSource)context.getSource(), StringArgumentType.getString(context, "hostname"), IntegerArgumentType.getInteger(context, "port"), EntityArgumentType.getPlayers(context, "players")))))));
    }

    private static int executeTransfer(ServerCommandSource source, String host, int port, Collection<ServerPlayerEntity> players) throws CommandSyntaxException {
        if (players.isEmpty()) {
            throw NO_PLAYERS_EXCEPTION.create();
        }
        for (ServerPlayerEntity lv : players) {
            lv.networkHandler.sendPacket(new ServerTransferS2CPacket(host, port));
        }
        if (players.size() == 1) {
            source.sendFeedback(() -> Text.translatable("commands.transfer.success.single", ((ServerPlayerEntity)players.iterator().next()).getDisplayName(), host, port), true);
        } else {
            source.sendFeedback(() -> Text.translatable("commands.transfer.success.multiple", players.size(), host, port), true);
        }
        return players.size();
    }
}

