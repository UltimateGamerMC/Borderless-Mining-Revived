/*
 * External method calls:
 *   Lnet/minecraft/server/command/CommandManager;literal(Ljava/lang/String;)Lcom/mojang/brigadier/builder/LiteralArgumentBuilder;
 *   Lnet/minecraft/server/command/CommandManager;requirePermissionLevel(Lnet/minecraft/command/permission/PermissionCheck;)Lnet/minecraft/command/permission/PermissionSourcePredicate;
 *   Lnet/minecraft/command/argument/EntityArgumentType;players()Lnet/minecraft/command/argument/EntityArgumentType;
 *   Lnet/minecraft/server/command/CommandManager;argument(Ljava/lang/String;Lcom/mojang/brigadier/arguments/ArgumentType;)Lcom/mojang/brigadier/builder/RequiredArgumentBuilder;
 *   Lnet/minecraft/command/argument/RegistryEntryArgumentType;dialog(Lnet/minecraft/command/CommandRegistryAccess;)Lnet/minecraft/command/argument/RegistryEntryArgumentType$DialogArgumentType;
 *   Lnet/minecraft/server/network/ServerPlayerEntity;openDialog(Lnet/minecraft/registry/entry/RegistryEntry;)V
 *   Lnet/minecraft/server/command/ServerCommandSource;sendFeedback(Ljava/util/function/Supplier;Z)V
 *   Lnet/minecraft/server/network/ServerPlayNetworkHandler;sendPacket(Lnet/minecraft/network/packet/Packet;)V
 *   Lnet/minecraft/text/Text;translatable(Ljava/lang/String;[Ljava/lang/Object;)Lnet/minecraft/text/MutableText;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/server/command/DialogCommand;executeClear(Lnet/minecraft/server/command/ServerCommandSource;Ljava/util/Collection;)I
 *   Lnet/minecraft/server/command/DialogCommand;executeShow(Lnet/minecraft/server/command/ServerCommandSource;Ljava/util/Collection;Lnet/minecraft/registry/entry/RegistryEntry;)I
 */
package net.minecraft.server.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import java.util.Collection;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.command.argument.RegistryEntryArgumentType;
import net.minecraft.dialog.type.Dialog;
import net.minecraft.network.packet.s2c.common.ClearDialogS2CPacket;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class DialogCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess) {
        dispatcher.register((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)CommandManager.literal("dialog").requires(CommandManager.requirePermissionLevel(CommandManager.GAMEMASTERS_CHECK))).then(CommandManager.literal("show").then((ArgumentBuilder<ServerCommandSource, ?>)CommandManager.argument("targets", EntityArgumentType.players()).then((ArgumentBuilder<ServerCommandSource, ?>)CommandManager.argument("dialog", RegistryEntryArgumentType.dialog(registryAccess)).executes(context -> DialogCommand.executeShow((ServerCommandSource)context.getSource(), EntityArgumentType.getPlayers(context, "targets"), RegistryEntryArgumentType.getDialog(context, "dialog"))))))).then(CommandManager.literal("clear").then((ArgumentBuilder<ServerCommandSource, ?>)CommandManager.argument("targets", EntityArgumentType.players()).executes(context -> DialogCommand.executeClear((ServerCommandSource)context.getSource(), EntityArgumentType.getPlayers(context, "targets"))))));
    }

    private static int executeShow(ServerCommandSource source, Collection<ServerPlayerEntity> players, RegistryEntry<Dialog> dialog) {
        for (ServerPlayerEntity lv : players) {
            lv.openDialog(dialog);
        }
        if (players.size() == 1) {
            source.sendFeedback(() -> Text.translatable("commands.dialog.show.single", ((ServerPlayerEntity)players.iterator().next()).getDisplayName()), true);
        } else {
            source.sendFeedback(() -> Text.translatable("commands.dialog.show.multiple", players.size()), true);
        }
        return players.size();
    }

    private static int executeClear(ServerCommandSource source, Collection<ServerPlayerEntity> players) {
        for (ServerPlayerEntity lv : players) {
            lv.networkHandler.sendPacket(ClearDialogS2CPacket.INSTANCE);
        }
        if (players.size() == 1) {
            source.sendFeedback(() -> Text.translatable("commands.dialog.clear.single", ((ServerPlayerEntity)players.iterator().next()).getDisplayName()), true);
        } else {
            source.sendFeedback(() -> Text.translatable("commands.dialog.clear.multiple", players.size()), true);
        }
        return players.size();
    }
}

