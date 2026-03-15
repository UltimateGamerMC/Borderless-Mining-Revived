/*
 * External method calls:
 *   Lnet/minecraft/server/command/CommandManager;literal(Ljava/lang/String;)Lcom/mojang/brigadier/builder/LiteralArgumentBuilder;
 *   Lnet/minecraft/server/command/CommandManager;requirePermissionLevel(Lnet/minecraft/command/permission/PermissionCheck;)Lnet/minecraft/command/permission/PermissionSourcePredicate;
 *   Lnet/minecraft/command/argument/EntityArgumentType;player()Lnet/minecraft/command/argument/EntityArgumentType;
 *   Lnet/minecraft/server/command/CommandManager;argument(Ljava/lang/String;Lcom/mojang/brigadier/arguments/ArgumentType;)Lcom/mojang/brigadier/builder/RequiredArgumentBuilder;
 *   Lnet/minecraft/command/argument/UuidArgumentType;uuid()Lnet/minecraft/command/argument/UuidArgumentType;
 *   Lnet/minecraft/command/argument/RegistryEntryArgumentType;dialog(Lnet/minecraft/command/CommandRegistryAccess;)Lnet/minecraft/command/argument/RegistryEntryArgumentType$DialogArgumentType;
 *   Lnet/minecraft/server/command/ServerCommandSource;sendFeedback(Ljava/util/function/Supplier;Z)V
 *   Lnet/minecraft/text/Text;literal(Ljava/lang/String;)Lnet/minecraft/text/MutableText;
 *   Lnet/minecraft/server/command/ServerCommandSource;sendError(Lnet/minecraft/text/Text;)V
 *   Lnet/minecraft/server/network/ServerConfigurationNetworkHandler;sendPacket(Lnet/minecraft/network/packet/Packet;)V
 *   Lnet/minecraft/command/CommandSource;suggestMatching(Ljava/lang/Iterable;Lcom/mojang/brigadier/suggestion/SuggestionsBuilder;)Ljava/util/concurrent/CompletableFuture;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/server/command/DebugConfigCommand;findConfigurationNetworkHandler(Lnet/minecraft/server/MinecraftServer;Ljava/util/UUID;)Lnet/minecraft/server/network/ServerConfigurationNetworkHandler;
 *   Lnet/minecraft/server/command/DebugConfigCommand;executeDialog(Lnet/minecraft/server/command/ServerCommandSource;Ljava/util/UUID;Lnet/minecraft/registry/entry/RegistryEntry;)I
 *   Lnet/minecraft/server/command/DebugConfigCommand;collectConfiguringPlayers(Lnet/minecraft/server/MinecraftServer;)Ljava/lang/Iterable;
 *   Lnet/minecraft/server/command/DebugConfigCommand;executeUnconfig(Lnet/minecraft/server/command/ServerCommandSource;Ljava/util/UUID;)I
 *   Lnet/minecraft/server/command/DebugConfigCommand;executeConfig(Lnet/minecraft/server/command/ServerCommandSource;Lnet/minecraft/server/network/ServerPlayerEntity;)I
 */
package net.minecraft.server.command;

import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import java.util.HashSet;
import java.util.UUID;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.CommandSource;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.command.argument.RegistryEntryArgumentType;
import net.minecraft.command.argument.UuidArgumentType;
import net.minecraft.dialog.type.Dialog;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.listener.PacketListener;
import net.minecraft.network.packet.s2c.common.ShowDialogS2CPacket;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerConfigurationNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import org.jspecify.annotations.Nullable;

public class DebugConfigCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess) {
        dispatcher.register((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)CommandManager.literal("debugconfig").requires(CommandManager.requirePermissionLevel(CommandManager.ADMINS_CHECK))).then(CommandManager.literal("config").then((ArgumentBuilder<ServerCommandSource, ?>)CommandManager.argument("target", EntityArgumentType.player()).executes(context -> DebugConfigCommand.executeConfig((ServerCommandSource)context.getSource(), EntityArgumentType.getPlayer(context, "target")))))).then(CommandManager.literal("unconfig").then((ArgumentBuilder<ServerCommandSource, ?>)CommandManager.argument("target", UuidArgumentType.uuid()).suggests((context, suggestionsBuilder) -> CommandSource.suggestMatching(DebugConfigCommand.collectConfiguringPlayers(((ServerCommandSource)context.getSource()).getServer()), suggestionsBuilder)).executes(context -> DebugConfigCommand.executeUnconfig((ServerCommandSource)context.getSource(), UuidArgumentType.getUuid(context, "target")))))).then(CommandManager.literal("dialog").then((ArgumentBuilder<ServerCommandSource, ?>)CommandManager.argument("target", UuidArgumentType.uuid()).suggests((context, suggestionsBuilder) -> CommandSource.suggestMatching(DebugConfigCommand.collectConfiguringPlayers(((ServerCommandSource)context.getSource()).getServer()), suggestionsBuilder)).then((ArgumentBuilder<ServerCommandSource, ?>)CommandManager.argument("dialog", RegistryEntryArgumentType.dialog(registryAccess)).executes(context -> DebugConfigCommand.executeDialog((ServerCommandSource)context.getSource(), UuidArgumentType.getUuid(context, "target"), RegistryEntryArgumentType.getDialog(context, "dialog")))))));
    }

    private static Iterable<String> collectConfiguringPlayers(MinecraftServer server) {
        HashSet<String> set = new HashSet<String>();
        for (ClientConnection lv : server.getNetworkIo().getConnections()) {
            PacketListener packetListener = lv.getPacketListener();
            if (!(packetListener instanceof ServerConfigurationNetworkHandler)) continue;
            ServerConfigurationNetworkHandler lv2 = (ServerConfigurationNetworkHandler)packetListener;
            set.add(lv2.getDebugProfile().id().toString());
        }
        return set;
    }

    private static int executeConfig(ServerCommandSource source, ServerPlayerEntity player) {
        GameProfile gameProfile = player.getGameProfile();
        player.networkHandler.reconfigure();
        source.sendFeedback(() -> Text.literal("Switched player " + gameProfile.name() + "(" + String.valueOf(gameProfile.id()) + ") to config mode"), false);
        return 1;
    }

    private static @Nullable ServerConfigurationNetworkHandler findConfigurationNetworkHandler(MinecraftServer server, UUID uuid) {
        for (ClientConnection lv : server.getNetworkIo().getConnections()) {
            ServerConfigurationNetworkHandler lv2;
            PacketListener packetListener = lv.getPacketListener();
            if (!(packetListener instanceof ServerConfigurationNetworkHandler) || !(lv2 = (ServerConfigurationNetworkHandler)packetListener).getDebugProfile().id().equals(uuid)) continue;
            return lv2;
        }
        return null;
    }

    private static int executeUnconfig(ServerCommandSource source, UUID uuid) {
        ServerConfigurationNetworkHandler lv = DebugConfigCommand.findConfigurationNetworkHandler(source.getServer(), uuid);
        if (lv != null) {
            lv.endConfiguration();
            return 1;
        }
        source.sendError(Text.literal("Can't find player to unconfig"));
        return 0;
    }

    private static int executeDialog(ServerCommandSource source, UUID uuid, RegistryEntry<Dialog> dialog) {
        ServerConfigurationNetworkHandler lv = DebugConfigCommand.findConfigurationNetworkHandler(source.getServer(), uuid);
        if (lv != null) {
            lv.sendPacket(new ShowDialogS2CPacket(dialog));
            return 1;
        }
        source.sendError(Text.literal("Can't find player to talk to"));
        return 0;
    }
}

