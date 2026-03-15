/*
 * External method calls:
 *   Lnet/minecraft/server/command/CommandManager;literal(Ljava/lang/String;)Lcom/mojang/brigadier/builder/LiteralArgumentBuilder;
 *   Lnet/minecraft/server/command/CommandManager;requirePermissionLevel(Lnet/minecraft/command/permission/PermissionCheck;)Lnet/minecraft/command/permission/PermissionSourcePredicate;
 *   Lnet/minecraft/GameVersion;id()Ljava/lang/String;
 *   Lnet/minecraft/text/Text;translatable(Ljava/lang/String;[Ljava/lang/Object;)Lnet/minecraft/text/MutableText;
 *   Lnet/minecraft/GameVersion;name()Ljava/lang/String;
 *   Lnet/minecraft/GameVersion;dataVersion()Lnet/minecraft/SaveVersion;
 *   Lnet/minecraft/SaveVersion;series()Ljava/lang/String;
 *   Lnet/minecraft/GameVersion;buildTime()Ljava/util/Date;
 *   Lnet/minecraft/text/Text;of(Ljava/util/Date;)Lnet/minecraft/text/Text;
 *   Lnet/minecraft/GameVersion;packVersion(Lnet/minecraft/resource/ResourceType;)Lnet/minecraft/resource/PackVersion;
 *   Lnet/minecraft/server/command/ServerCommandSource;sendMessage(Lnet/minecraft/text/Text;)V
 *   Lnet/minecraft/text/Text;translatable(Ljava/lang/String;)Lnet/minecraft/text/MutableText;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/server/command/VersionCommand;acceptInfo(Ljava/util/function/Consumer;)V
 */
package net.minecraft.server.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import java.util.function.Consumer;
import net.minecraft.GameVersion;
import net.minecraft.SharedConstants;
import net.minecraft.resource.ResourceType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

public class VersionCommand {
    private static final Text HEADER_TEXT = Text.translatable("commands.version.header");
    private static final Text STABLE_YES_TEXT = Text.translatable("commands.version.stable.yes");
    private static final Text STABLE_NO_TEXT = Text.translatable("commands.version.stable.no");

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, boolean dedicated) {
        dispatcher.register((LiteralArgumentBuilder)((LiteralArgumentBuilder)CommandManager.literal("version").requires(CommandManager.requirePermissionLevel(dedicated ? CommandManager.GAMEMASTERS_CHECK : CommandManager.ALWAYS_PASS_CHECK))).executes(context -> {
            ServerCommandSource lv = (ServerCommandSource)context.getSource();
            lv.sendMessage(HEADER_TEXT);
            VersionCommand.acceptInfo(lv::sendMessage);
            return 1;
        }));
    }

    public static void acceptInfo(Consumer<Text> sender) {
        GameVersion lv = SharedConstants.getGameVersion();
        sender.accept(Text.translatable("commands.version.id", lv.id()));
        sender.accept(Text.translatable("commands.version.name", lv.name()));
        sender.accept(Text.translatable("commands.version.data", lv.dataVersion().id()));
        sender.accept(Text.translatable("commands.version.series", lv.dataVersion().series()));
        sender.accept(Text.translatable("commands.version.protocol", lv.protocolVersion(), "0x" + Integer.toHexString(lv.protocolVersion())));
        sender.accept(Text.translatable("commands.version.build_time", Text.of(lv.buildTime())));
        sender.accept(Text.translatable("commands.version.pack.resource", lv.packVersion(ResourceType.CLIENT_RESOURCES).toString()));
        sender.accept(Text.translatable("commands.version.pack.data", lv.packVersion(ResourceType.SERVER_DATA).toString()));
        sender.accept(lv.stable() ? STABLE_YES_TEXT : STABLE_NO_TEXT);
    }
}

