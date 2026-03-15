/*
 * External method calls:
 *   Lnet/minecraft/server/command/CommandManager;literal(Ljava/lang/String;)Lcom/mojang/brigadier/builder/LiteralArgumentBuilder;
 *   Lnet/minecraft/server/command/CommandManager;requirePermissionLevel(Lnet/minecraft/command/permission/PermissionCheck;)Lnet/minecraft/command/permission/PermissionSourcePredicate;
 *   Lnet/minecraft/server/command/ServerCommandSource;sendFeedback(Ljava/util/function/Supplier;Z)V
 *   Lnet/minecraft/server/BanEntry;toText()Lnet/minecraft/text/Text;
 *   Lnet/minecraft/text/Text;translatable(Ljava/lang/String;[Ljava/lang/Object;)Lnet/minecraft/text/MutableText;
 *   Lnet/minecraft/text/Text;translatable(Ljava/lang/String;)Lnet/minecraft/text/MutableText;
 *   Lnet/minecraft/server/BannedPlayerList;values()Ljava/util/Collection;
 *   Lnet/minecraft/server/BannedIpList;values()Ljava/util/Collection;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/server/dedicated/command/BanListCommand;execute(Lnet/minecraft/server/command/ServerCommandSource;Ljava/util/Collection;)I
 */
package net.minecraft.server.dedicated.command;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import java.util.Collection;
import net.minecraft.server.BanEntry;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

public class BanListCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)CommandManager.literal("banlist").requires(CommandManager.requirePermissionLevel(CommandManager.ADMINS_CHECK))).executes(context -> {
            PlayerManager lv = ((ServerCommandSource)context.getSource()).getServer().getPlayerManager();
            return BanListCommand.execute((ServerCommandSource)context.getSource(), Lists.newArrayList(Iterables.concat(lv.getUserBanList().values(), lv.getIpBanList().values())));
        })).then(CommandManager.literal("ips").executes(context -> BanListCommand.execute((ServerCommandSource)context.getSource(), ((ServerCommandSource)context.getSource()).getServer().getPlayerManager().getIpBanList().values())))).then(CommandManager.literal("players").executes(context -> BanListCommand.execute((ServerCommandSource)context.getSource(), ((ServerCommandSource)context.getSource()).getServer().getPlayerManager().getUserBanList().values()))));
    }

    private static int execute(ServerCommandSource source, Collection<? extends BanEntry<?>> targets) {
        if (targets.isEmpty()) {
            source.sendFeedback(() -> Text.translatable("commands.banlist.none"), false);
        } else {
            source.sendFeedback(() -> Text.translatable("commands.banlist.list", targets.size()), false);
            for (BanEntry<?> lv : targets) {
                source.sendFeedback(() -> Text.translatable("commands.banlist.entry", lv.toText(), lv.getSource(), lv.getReasonText()), false);
            }
        }
        return targets.size();
    }
}

