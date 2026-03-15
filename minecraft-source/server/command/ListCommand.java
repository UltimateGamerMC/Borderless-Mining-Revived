/*
 * External method calls:
 *   Lnet/minecraft/server/command/CommandManager;literal(Ljava/lang/String;)Lcom/mojang/brigadier/builder/LiteralArgumentBuilder;
 *   Lnet/minecraft/text/Texts;join(Ljava/util/Collection;Ljava/util/function/Function;)Lnet/minecraft/text/Text;
 *   Lnet/minecraft/server/command/ServerCommandSource;sendFeedback(Ljava/util/function/Supplier;Z)V
 *   Lnet/minecraft/text/Text;translatable(Ljava/lang/String;[Ljava/lang/Object;)Lnet/minecraft/text/MutableText;
 *   Lnet/minecraft/text/Text;of(Ljava/util/UUID;)Lnet/minecraft/text/Text;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/server/command/ListCommand;execute(Lnet/minecraft/server/command/ServerCommandSource;Ljava/util/function/Function;)I
 *   Lnet/minecraft/server/command/ListCommand;executeUuids(Lnet/minecraft/server/command/ServerCommandSource;)I
 *   Lnet/minecraft/server/command/ListCommand;executeNames(Lnet/minecraft/server/command/ServerCommandSource;)I
 */
package net.minecraft.server.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import java.util.List;
import java.util.function.Function;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.text.Texts;

public class ListCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register((LiteralArgumentBuilder)((LiteralArgumentBuilder)CommandManager.literal("list").executes(context -> ListCommand.executeNames((ServerCommandSource)context.getSource()))).then(CommandManager.literal("uuids").executes(context -> ListCommand.executeUuids((ServerCommandSource)context.getSource()))));
    }

    private static int executeNames(ServerCommandSource source) {
        return ListCommand.execute(source, PlayerEntity::getDisplayName);
    }

    private static int executeUuids(ServerCommandSource source) {
        return ListCommand.execute(source, player -> Text.translatable("commands.list.nameAndId", player.getName(), Text.of(player.getGameProfile().id())));
    }

    private static int execute(ServerCommandSource source, Function<ServerPlayerEntity, Text> nameProvider) {
        PlayerManager lv = source.getServer().getPlayerManager();
        List<ServerPlayerEntity> list = lv.getPlayerList();
        Text lv2 = Texts.join(list, nameProvider);
        source.sendFeedback(() -> Text.translatable("commands.list.players", list.size(), lv.getMaxPlayerCount(), lv2), false);
        return list.size();
    }
}

