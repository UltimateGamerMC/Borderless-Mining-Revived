/*
 * External method calls:
 *   Lnet/minecraft/server/command/CommandManager;literal(Ljava/lang/String;)Lcom/mojang/brigadier/builder/LiteralArgumentBuilder;
 *   Lnet/minecraft/command/argument/EntityArgumentType;players()Lnet/minecraft/command/argument/EntityArgumentType;
 *   Lnet/minecraft/server/command/CommandManager;argument(Ljava/lang/String;Lcom/mojang/brigadier/arguments/ArgumentType;)Lcom/mojang/brigadier/builder/RequiredArgumentBuilder;
 *   Lnet/minecraft/command/argument/MessageArgumentType;message()Lnet/minecraft/command/argument/MessageArgumentType;
 *   Lnet/minecraft/network/message/MessageType;params(Lnet/minecraft/registry/RegistryKey;Lnet/minecraft/server/command/ServerCommandSource;)Lnet/minecraft/network/message/MessageType$Parameters;
 *   Lnet/minecraft/network/message/SentMessage;of(Lnet/minecraft/network/message/SignedMessage;)Lnet/minecraft/network/message/SentMessage;
 *   Lnet/minecraft/network/message/MessageType$Parameters;withTargetName(Lnet/minecraft/text/Text;)Lnet/minecraft/network/message/MessageType$Parameters;
 *   Lnet/minecraft/server/command/ServerCommandSource;sendChatMessage(Lnet/minecraft/network/message/SentMessage;ZLnet/minecraft/network/message/MessageType$Parameters;)V
 *   Lnet/minecraft/server/network/ServerPlayerEntity;sendChatMessage(Lnet/minecraft/network/message/SentMessage;ZLnet/minecraft/network/message/MessageType$Parameters;)V
 *   Lnet/minecraft/server/command/ServerCommandSource;sendMessage(Lnet/minecraft/text/Text;)V
 *
 * Internal private/static methods:
 *   Lnet/minecraft/server/command/MessageCommand;execute(Lnet/minecraft/server/command/ServerCommandSource;Ljava/util/Collection;Lnet/minecraft/network/message/SignedMessage;)V
 */
package net.minecraft.server.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import java.util.Collection;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.command.argument.MessageArgumentType;
import net.minecraft.network.message.MessageType;
import net.minecraft.network.message.SentMessage;
import net.minecraft.network.message.SignedMessage;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;

public class MessageCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        LiteralCommandNode<ServerCommandSource> literalCommandNode = dispatcher.register((LiteralArgumentBuilder)CommandManager.literal("msg").then((ArgumentBuilder<ServerCommandSource, ?>)CommandManager.argument("targets", EntityArgumentType.players()).then((ArgumentBuilder<ServerCommandSource, ?>)CommandManager.argument("message", MessageArgumentType.message()).executes(context -> {
            Collection<ServerPlayerEntity> collection = EntityArgumentType.getPlayers(context, "targets");
            if (!collection.isEmpty()) {
                MessageArgumentType.getSignedMessage(context, "message", message -> MessageCommand.execute((ServerCommandSource)context.getSource(), collection, message));
            }
            return collection.size();
        }))));
        dispatcher.register((LiteralArgumentBuilder)CommandManager.literal("tell").redirect(literalCommandNode));
        dispatcher.register((LiteralArgumentBuilder)CommandManager.literal("w").redirect(literalCommandNode));
    }

    private static void execute(ServerCommandSource source, Collection<ServerPlayerEntity> targets, SignedMessage message) {
        MessageType.Parameters lv = MessageType.params(MessageType.MSG_COMMAND_INCOMING, source);
        SentMessage lv2 = SentMessage.of(message);
        boolean bl = false;
        for (ServerPlayerEntity lv3 : targets) {
            MessageType.Parameters lv4 = MessageType.params(MessageType.MSG_COMMAND_OUTGOING, source).withTargetName(lv3.getDisplayName());
            source.sendChatMessage(lv2, false, lv4);
            boolean bl2 = source.shouldFilterText(lv3);
            lv3.sendChatMessage(lv2, bl2, lv);
            bl |= bl2 && message.isFullyFiltered();
        }
        if (bl) {
            source.sendMessage(PlayerManager.FILTERED_FULL_TEXT);
        }
    }
}

