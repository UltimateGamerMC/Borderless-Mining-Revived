/*
 * External method calls:
 *   Lnet/minecraft/server/command/CommandManager;literal(Ljava/lang/String;)Lcom/mojang/brigadier/builder/LiteralArgumentBuilder;
 *   Lnet/minecraft/command/argument/MessageArgumentType;message()Lnet/minecraft/command/argument/MessageArgumentType;
 *   Lnet/minecraft/server/command/CommandManager;argument(Ljava/lang/String;Lcom/mojang/brigadier/arguments/ArgumentType;)Lcom/mojang/brigadier/builder/RequiredArgumentBuilder;
 *   Lnet/minecraft/text/MutableText;fillStyle(Lnet/minecraft/text/Style;)Lnet/minecraft/text/MutableText;
 *   Lnet/minecraft/network/message/MessageType;params(Lnet/minecraft/registry/RegistryKey;Lnet/minecraft/server/command/ServerCommandSource;)Lnet/minecraft/network/message/MessageType$Parameters;
 *   Lnet/minecraft/network/message/MessageType$Parameters;withTargetName(Lnet/minecraft/text/Text;)Lnet/minecraft/network/message/MessageType$Parameters;
 *   Lnet/minecraft/network/message/SentMessage;of(Lnet/minecraft/network/message/SignedMessage;)Lnet/minecraft/network/message/SentMessage;
 *   Lnet/minecraft/server/network/ServerPlayerEntity;sendChatMessage(Lnet/minecraft/network/message/SentMessage;ZLnet/minecraft/network/message/MessageType$Parameters;)V
 *   Lnet/minecraft/server/command/ServerCommandSource;sendMessage(Lnet/minecraft/text/Text;)V
 *   Lnet/minecraft/text/Text;translatable(Ljava/lang/String;)Lnet/minecraft/text/MutableText;
 *   Lnet/minecraft/text/Style;withHoverEvent(Lnet/minecraft/text/HoverEvent;)Lnet/minecraft/text/Style;
 *   Lnet/minecraft/text/Style;withClickEvent(Lnet/minecraft/text/ClickEvent;)Lnet/minecraft/text/Style;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/server/command/TeamMsgCommand;execute(Lnet/minecraft/server/command/ServerCommandSource;Lnet/minecraft/entity/Entity;Lnet/minecraft/scoreboard/Team;Ljava/util/List;Lnet/minecraft/network/message/SignedMessage;)V
 */
package net.minecraft.server.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.tree.LiteralCommandNode;
import java.util.List;
import net.minecraft.command.argument.MessageArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.network.message.MessageType;
import net.minecraft.network.message.SentMessage;
import net.minecraft.network.message.SignedMessage;
import net.minecraft.scoreboard.Team;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;

public class TeamMsgCommand {
    private static final Style STYLE = Style.EMPTY.withHoverEvent(new HoverEvent.ShowText(Text.translatable("chat.type.team.hover"))).withClickEvent(new ClickEvent.SuggestCommand("/teammsg "));
    private static final SimpleCommandExceptionType NO_TEAM_EXCEPTION = new SimpleCommandExceptionType(Text.translatable("commands.teammsg.failed.noteam"));

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        LiteralCommandNode<ServerCommandSource> literalCommandNode = dispatcher.register((LiteralArgumentBuilder)CommandManager.literal("teammsg").then((ArgumentBuilder<ServerCommandSource, ?>)CommandManager.argument("message", MessageArgumentType.message()).executes(context -> {
            ServerCommandSource lv = (ServerCommandSource)context.getSource();
            Entity lv2 = lv.getEntityOrThrow();
            Team lv3 = lv2.getScoreboardTeam();
            if (lv3 == null) {
                throw NO_TEAM_EXCEPTION.create();
            }
            List<ServerPlayerEntity> list = lv.getServer().getPlayerManager().getPlayerList().stream().filter(player -> player == lv2 || player.getScoreboardTeam() == lv3).toList();
            if (!list.isEmpty()) {
                MessageArgumentType.getSignedMessage(context, "message", message -> TeamMsgCommand.execute(lv, lv2, lv3, list, message));
            }
            return list.size();
        })));
        dispatcher.register((LiteralArgumentBuilder)CommandManager.literal("tm").redirect(literalCommandNode));
    }

    private static void execute(ServerCommandSource source, Entity entity, Team team, List<ServerPlayerEntity> recipients, SignedMessage message) {
        MutableText lv = team.getFormattedName().fillStyle(STYLE);
        MessageType.Parameters lv2 = MessageType.params(MessageType.TEAM_MSG_COMMAND_INCOMING, source).withTargetName(lv);
        MessageType.Parameters lv3 = MessageType.params(MessageType.TEAM_MSG_COMMAND_OUTGOING, source).withTargetName(lv);
        SentMessage lv4 = SentMessage.of(message);
        boolean bl = false;
        for (ServerPlayerEntity lv5 : recipients) {
            MessageType.Parameters lv6 = lv5 == entity ? lv3 : lv2;
            boolean bl2 = source.shouldFilterText(lv5);
            lv5.sendChatMessage(lv4, bl2, lv6);
            bl |= bl2 && message.isFullyFiltered();
        }
        if (bl) {
            source.sendMessage(PlayerManager.FILTERED_FULL_TEXT);
        }
    }
}

