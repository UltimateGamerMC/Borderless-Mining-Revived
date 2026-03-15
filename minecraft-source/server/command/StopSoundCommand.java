/*
 * External method calls:
 *   Lnet/minecraft/command/argument/EntityArgumentType;players()Lnet/minecraft/command/argument/EntityArgumentType;
 *   Lnet/minecraft/server/command/CommandManager;argument(Ljava/lang/String;Lcom/mojang/brigadier/arguments/ArgumentType;)Lcom/mojang/brigadier/builder/RequiredArgumentBuilder;
 *   Lnet/minecraft/server/command/CommandManager;literal(Ljava/lang/String;)Lcom/mojang/brigadier/builder/LiteralArgumentBuilder;
 *   Lnet/minecraft/command/argument/IdentifierArgumentType;identifier()Lnet/minecraft/command/argument/IdentifierArgumentType;
 *   Lnet/minecraft/command/suggestion/SuggestionProviders;cast(Lcom/mojang/brigadier/suggestion/SuggestionProvider;)Lcom/mojang/brigadier/suggestion/SuggestionProvider;
 *   Lnet/minecraft/sound/SoundCategory;values()[Lnet/minecraft/sound/SoundCategory;
 *   Lnet/minecraft/server/command/CommandManager;requirePermissionLevel(Lnet/minecraft/command/permission/PermissionCheck;)Lnet/minecraft/command/permission/PermissionSourcePredicate;
 *   Lnet/minecraft/server/network/ServerPlayNetworkHandler;sendPacket(Lnet/minecraft/network/packet/Packet;)V
 *   Lnet/minecraft/server/command/ServerCommandSource;sendFeedback(Ljava/util/function/Supplier;Z)V
 *   Lnet/minecraft/text/Text;translatable(Ljava/lang/String;)Lnet/minecraft/text/MutableText;
 *   Lnet/minecraft/text/Text;of(Lnet/minecraft/util/Identifier;)Lnet/minecraft/text/Text;
 *   Lnet/minecraft/text/Text;translatable(Ljava/lang/String;[Ljava/lang/Object;)Lnet/minecraft/text/MutableText;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/server/command/StopSoundCommand;execute(Lnet/minecraft/server/command/ServerCommandSource;Ljava/util/Collection;Lnet/minecraft/sound/SoundCategory;Lnet/minecraft/util/Identifier;)I
 */
package net.minecraft.server.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import java.util.Collection;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.command.argument.IdentifierArgumentType;
import net.minecraft.command.suggestion.SuggestionProviders;
import net.minecraft.network.packet.s2c.play.StopSoundS2CPacket;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.jspecify.annotations.Nullable;

public class StopSoundCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        RequiredArgumentBuilder requiredArgumentBuilder = (RequiredArgumentBuilder)((RequiredArgumentBuilder)CommandManager.argument("targets", EntityArgumentType.players()).executes(context -> StopSoundCommand.execute((ServerCommandSource)context.getSource(), EntityArgumentType.getPlayers(context, "targets"), null, null))).then(CommandManager.literal("*").then((ArgumentBuilder<ServerCommandSource, ?>)CommandManager.argument("sound", IdentifierArgumentType.identifier()).suggests(SuggestionProviders.cast(SuggestionProviders.AVAILABLE_SOUNDS)).executes(context -> StopSoundCommand.execute((ServerCommandSource)context.getSource(), EntityArgumentType.getPlayers(context, "targets"), null, IdentifierArgumentType.getIdentifier(context, "sound")))));
        for (SoundCategory lv : SoundCategory.values()) {
            requiredArgumentBuilder.then(((LiteralArgumentBuilder)CommandManager.literal(lv.getName()).executes(context -> StopSoundCommand.execute((ServerCommandSource)context.getSource(), EntityArgumentType.getPlayers(context, "targets"), lv, null))).then(CommandManager.argument("sound", IdentifierArgumentType.identifier()).suggests(SuggestionProviders.cast(SuggestionProviders.AVAILABLE_SOUNDS)).executes(context -> StopSoundCommand.execute((ServerCommandSource)context.getSource(), EntityArgumentType.getPlayers(context, "targets"), lv, IdentifierArgumentType.getIdentifier(context, "sound")))));
        }
        dispatcher.register((LiteralArgumentBuilder)((LiteralArgumentBuilder)CommandManager.literal("stopsound").requires(CommandManager.requirePermissionLevel(CommandManager.GAMEMASTERS_CHECK))).then(requiredArgumentBuilder));
    }

    private static int execute(ServerCommandSource source, Collection<ServerPlayerEntity> targets, @Nullable SoundCategory category, @Nullable Identifier sound) {
        StopSoundS2CPacket lv = new StopSoundS2CPacket(sound, category);
        for (ServerPlayerEntity lv2 : targets) {
            lv2.networkHandler.sendPacket(lv);
        }
        if (category != null) {
            if (sound != null) {
                source.sendFeedback(() -> Text.translatable("commands.stopsound.success.source.sound", Text.of(sound), category.getName()), true);
            } else {
                source.sendFeedback(() -> Text.translatable("commands.stopsound.success.source.any", category.getName()), true);
            }
        } else if (sound != null) {
            source.sendFeedback(() -> Text.translatable("commands.stopsound.success.sourceless.sound", Text.of(sound)), true);
        } else {
            source.sendFeedback(() -> Text.translatable("commands.stopsound.success.sourceless.any"), true);
        }
        return targets.size();
    }
}

