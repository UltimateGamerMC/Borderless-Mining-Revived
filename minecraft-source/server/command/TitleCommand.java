/*
 * External method calls:
 *   Lnet/minecraft/server/command/CommandManager;literal(Ljava/lang/String;)Lcom/mojang/brigadier/builder/LiteralArgumentBuilder;
 *   Lnet/minecraft/server/command/CommandManager;requirePermissionLevel(Lnet/minecraft/command/permission/PermissionCheck;)Lnet/minecraft/command/permission/PermissionSourcePredicate;
 *   Lnet/minecraft/command/argument/EntityArgumentType;players()Lnet/minecraft/command/argument/EntityArgumentType;
 *   Lnet/minecraft/server/command/CommandManager;argument(Ljava/lang/String;Lcom/mojang/brigadier/arguments/ArgumentType;)Lcom/mojang/brigadier/builder/RequiredArgumentBuilder;
 *   Lnet/minecraft/command/argument/TextArgumentType;text(Lnet/minecraft/command/CommandRegistryAccess;)Lnet/minecraft/command/argument/TextArgumentType;
 *   Lnet/minecraft/command/argument/TimeArgumentType;time()Lnet/minecraft/command/argument/TimeArgumentType;
 *   Lnet/minecraft/server/network/ServerPlayNetworkHandler;sendPacket(Lnet/minecraft/network/packet/Packet;)V
 *   Lnet/minecraft/server/command/ServerCommandSource;sendFeedback(Ljava/util/function/Supplier;Z)V
 *   Lnet/minecraft/text/Texts;parse(Lnet/minecraft/server/command/ServerCommandSource;Lnet/minecraft/text/Text;Lnet/minecraft/entity/Entity;I)Lnet/minecraft/text/MutableText;
 *   Lnet/minecraft/text/Text;translatable(Ljava/lang/String;[Ljava/lang/Object;)Lnet/minecraft/text/MutableText;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/server/command/TitleCommand;executeTimes(Lnet/minecraft/server/command/ServerCommandSource;Ljava/util/Collection;III)I
 *   Lnet/minecraft/server/command/TitleCommand;executeTitle(Lnet/minecraft/server/command/ServerCommandSource;Ljava/util/Collection;Lnet/minecraft/text/Text;Ljava/lang/String;Ljava/util/function/Function;)I
 *   Lnet/minecraft/server/command/TitleCommand;executeReset(Lnet/minecraft/server/command/ServerCommandSource;Ljava/util/Collection;)I
 *   Lnet/minecraft/server/command/TitleCommand;executeClear(Lnet/minecraft/server/command/ServerCommandSource;Ljava/util/Collection;)I
 */
package net.minecraft.server.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import java.util.Collection;
import java.util.function.Function;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.command.argument.TextArgumentType;
import net.minecraft.command.argument.TimeArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.ClearTitleS2CPacket;
import net.minecraft.network.packet.s2c.play.OverlayMessageS2CPacket;
import net.minecraft.network.packet.s2c.play.SubtitleS2CPacket;
import net.minecraft.network.packet.s2c.play.TitleFadeS2CPacket;
import net.minecraft.network.packet.s2c.play.TitleS2CPacket;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.text.Texts;

public class TitleCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess) {
        dispatcher.register((LiteralArgumentBuilder)((LiteralArgumentBuilder)CommandManager.literal("title").requires(CommandManager.requirePermissionLevel(CommandManager.GAMEMASTERS_CHECK))).then(((RequiredArgumentBuilder)((RequiredArgumentBuilder)((RequiredArgumentBuilder)((RequiredArgumentBuilder)((RequiredArgumentBuilder)CommandManager.argument("targets", EntityArgumentType.players()).then((ArgumentBuilder<ServerCommandSource, ?>)CommandManager.literal("clear").executes(context -> TitleCommand.executeClear((ServerCommandSource)context.getSource(), EntityArgumentType.getPlayers(context, "targets"))))).then(CommandManager.literal("reset").executes(context -> TitleCommand.executeReset((ServerCommandSource)context.getSource(), EntityArgumentType.getPlayers(context, "targets"))))).then(CommandManager.literal("title").then((ArgumentBuilder<ServerCommandSource, ?>)CommandManager.argument("title", TextArgumentType.text(registryAccess)).executes(context -> TitleCommand.executeTitle((ServerCommandSource)context.getSource(), EntityArgumentType.getPlayers(context, "targets"), TextArgumentType.getTextArgument(context, "title"), "title", TitleS2CPacket::new))))).then(CommandManager.literal("subtitle").then((ArgumentBuilder<ServerCommandSource, ?>)CommandManager.argument("title", TextArgumentType.text(registryAccess)).executes(context -> TitleCommand.executeTitle((ServerCommandSource)context.getSource(), EntityArgumentType.getPlayers(context, "targets"), TextArgumentType.getTextArgument(context, "title"), "subtitle", SubtitleS2CPacket::new))))).then(CommandManager.literal("actionbar").then((ArgumentBuilder<ServerCommandSource, ?>)CommandManager.argument("title", TextArgumentType.text(registryAccess)).executes(context -> TitleCommand.executeTitle((ServerCommandSource)context.getSource(), EntityArgumentType.getPlayers(context, "targets"), TextArgumentType.getTextArgument(context, "title"), "actionbar", OverlayMessageS2CPacket::new))))).then(CommandManager.literal("times").then((ArgumentBuilder<ServerCommandSource, ?>)CommandManager.argument("fadeIn", TimeArgumentType.time()).then((ArgumentBuilder<ServerCommandSource, ?>)CommandManager.argument("stay", TimeArgumentType.time()).then((ArgumentBuilder<ServerCommandSource, ?>)CommandManager.argument("fadeOut", TimeArgumentType.time()).executes(context -> TitleCommand.executeTimes((ServerCommandSource)context.getSource(), EntityArgumentType.getPlayers(context, "targets"), IntegerArgumentType.getInteger(context, "fadeIn"), IntegerArgumentType.getInteger(context, "stay"), IntegerArgumentType.getInteger(context, "fadeOut")))))))));
    }

    private static int executeClear(ServerCommandSource source, Collection<ServerPlayerEntity> targets) {
        ClearTitleS2CPacket lv = new ClearTitleS2CPacket(false);
        for (ServerPlayerEntity lv2 : targets) {
            lv2.networkHandler.sendPacket(lv);
        }
        if (targets.size() == 1) {
            source.sendFeedback(() -> Text.translatable("commands.title.cleared.single", ((ServerPlayerEntity)targets.iterator().next()).getDisplayName()), true);
        } else {
            source.sendFeedback(() -> Text.translatable("commands.title.cleared.multiple", targets.size()), true);
        }
        return targets.size();
    }

    private static int executeReset(ServerCommandSource source, Collection<ServerPlayerEntity> targets) {
        ClearTitleS2CPacket lv = new ClearTitleS2CPacket(true);
        for (ServerPlayerEntity lv2 : targets) {
            lv2.networkHandler.sendPacket(lv);
        }
        if (targets.size() == 1) {
            source.sendFeedback(() -> Text.translatable("commands.title.reset.single", ((ServerPlayerEntity)targets.iterator().next()).getDisplayName()), true);
        } else {
            source.sendFeedback(() -> Text.translatable("commands.title.reset.multiple", targets.size()), true);
        }
        return targets.size();
    }

    private static int executeTitle(ServerCommandSource source, Collection<ServerPlayerEntity> targets, Text title, String titleType, Function<Text, Packet<?>> constructor) throws CommandSyntaxException {
        for (ServerPlayerEntity lv : targets) {
            lv.networkHandler.sendPacket(constructor.apply(Texts.parse(source, title, (Entity)lv, 0)));
        }
        if (targets.size() == 1) {
            source.sendFeedback(() -> Text.translatable("commands.title.show." + titleType + ".single", ((ServerPlayerEntity)targets.iterator().next()).getDisplayName()), true);
        } else {
            source.sendFeedback(() -> Text.translatable("commands.title.show." + titleType + ".multiple", targets.size()), true);
        }
        return targets.size();
    }

    private static int executeTimes(ServerCommandSource source, Collection<ServerPlayerEntity> targets, int fadeIn, int stay, int fadeOut) {
        TitleFadeS2CPacket lv = new TitleFadeS2CPacket(fadeIn, stay, fadeOut);
        for (ServerPlayerEntity lv2 : targets) {
            lv2.networkHandler.sendPacket(lv);
        }
        if (targets.size() == 1) {
            source.sendFeedback(() -> Text.translatable("commands.title.times.single", ((ServerPlayerEntity)targets.iterator().next()).getDisplayName()), true);
        } else {
            source.sendFeedback(() -> Text.translatable("commands.title.times.multiple", targets.size()), true);
        }
        return targets.size();
    }
}

