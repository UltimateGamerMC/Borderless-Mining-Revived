/*
 * External method calls:
 *   Lnet/minecraft/server/command/CommandManager;literal(Ljava/lang/String;)Lcom/mojang/brigadier/builder/LiteralArgumentBuilder;
 *   Lnet/minecraft/server/command/CommandManager;requirePermissionLevel(Lnet/minecraft/command/permission/PermissionCheck;)Lnet/minecraft/command/permission/PermissionSourcePredicate;
 *   Lnet/minecraft/command/argument/EntityArgumentType;entities()Lnet/minecraft/command/argument/EntityArgumentType;
 *   Lnet/minecraft/server/command/CommandManager;argument(Ljava/lang/String;Lcom/mojang/brigadier/arguments/ArgumentType;)Lcom/mojang/brigadier/builder/RequiredArgumentBuilder;
 *   Lnet/minecraft/entity/Entity;addCommandTag(Ljava/lang/String;)Z
 *   Lnet/minecraft/server/command/ServerCommandSource;sendFeedback(Ljava/util/function/Supplier;Z)V
 *   Lnet/minecraft/entity/Entity;removeCommandTag(Ljava/lang/String;)Z
 *   Lnet/minecraft/text/Texts;joinOrdered(Ljava/util/Collection;)Lnet/minecraft/text/Text;
 *   Lnet/minecraft/text/Text;translatable(Ljava/lang/String;[Ljava/lang/Object;)Lnet/minecraft/text/MutableText;
 *   Lnet/minecraft/command/CommandSource;suggestMatching(Ljava/lang/Iterable;Lcom/mojang/brigadier/suggestion/SuggestionsBuilder;)Ljava/util/concurrent/CompletableFuture;
 *   Lnet/minecraft/text/Text;translatable(Ljava/lang/String;)Lnet/minecraft/text/MutableText;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/server/command/TagCommand;executeList(Lnet/minecraft/server/command/ServerCommandSource;Ljava/util/Collection;)I
 *   Lnet/minecraft/server/command/TagCommand;executeRemove(Lnet/minecraft/server/command/ServerCommandSource;Ljava/util/Collection;Ljava/lang/String;)I
 *   Lnet/minecraft/server/command/TagCommand;executeAdd(Lnet/minecraft/server/command/ServerCommandSource;Ljava/util/Collection;Ljava/lang/String;)I
 */
package net.minecraft.server.command;

import com.google.common.collect.Sets;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import java.util.Collection;
import java.util.HashSet;
import net.minecraft.command.CommandSource;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.text.Texts;

public class TagCommand {
    private static final SimpleCommandExceptionType ADD_FAILED_EXCEPTION = new SimpleCommandExceptionType(Text.translatable("commands.tag.add.failed"));
    private static final SimpleCommandExceptionType REMOVE_FAILED_EXCEPTION = new SimpleCommandExceptionType(Text.translatable("commands.tag.remove.failed"));

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register((LiteralArgumentBuilder)((LiteralArgumentBuilder)CommandManager.literal("tag").requires(CommandManager.requirePermissionLevel(CommandManager.GAMEMASTERS_CHECK))).then(((RequiredArgumentBuilder)((RequiredArgumentBuilder)CommandManager.argument("targets", EntityArgumentType.entities()).then((ArgumentBuilder<ServerCommandSource, ?>)CommandManager.literal("add").then((ArgumentBuilder<ServerCommandSource, ?>)CommandManager.argument("name", StringArgumentType.word()).executes(context -> TagCommand.executeAdd((ServerCommandSource)context.getSource(), EntityArgumentType.getEntities(context, "targets"), StringArgumentType.getString(context, "name")))))).then(CommandManager.literal("remove").then((ArgumentBuilder<ServerCommandSource, ?>)CommandManager.argument("name", StringArgumentType.word()).suggests((context, builder) -> CommandSource.suggestMatching(TagCommand.getTags(EntityArgumentType.getEntities(context, "targets")), builder)).executes(context -> TagCommand.executeRemove((ServerCommandSource)context.getSource(), EntityArgumentType.getEntities(context, "targets"), StringArgumentType.getString(context, "name")))))).then(CommandManager.literal("list").executes(context -> TagCommand.executeList((ServerCommandSource)context.getSource(), EntityArgumentType.getEntities(context, "targets"))))));
    }

    private static Collection<String> getTags(Collection<? extends Entity> entities) {
        HashSet<String> set = Sets.newHashSet();
        for (Entity entity : entities) {
            set.addAll(entity.getCommandTags());
        }
        return set;
    }

    private static int executeAdd(ServerCommandSource source, Collection<? extends Entity> targets, String tag) throws CommandSyntaxException {
        int i = 0;
        for (Entity entity : targets) {
            if (!entity.addCommandTag(tag)) continue;
            ++i;
        }
        if (i == 0) {
            throw ADD_FAILED_EXCEPTION.create();
        }
        if (targets.size() == 1) {
            source.sendFeedback(() -> Text.translatable("commands.tag.add.success.single", tag, ((Entity)targets.iterator().next()).getDisplayName()), true);
        } else {
            source.sendFeedback(() -> Text.translatable("commands.tag.add.success.multiple", tag, targets.size()), true);
        }
        return i;
    }

    private static int executeRemove(ServerCommandSource source, Collection<? extends Entity> targets, String tag) throws CommandSyntaxException {
        int i = 0;
        for (Entity entity : targets) {
            if (!entity.removeCommandTag(tag)) continue;
            ++i;
        }
        if (i == 0) {
            throw REMOVE_FAILED_EXCEPTION.create();
        }
        if (targets.size() == 1) {
            source.sendFeedback(() -> Text.translatable("commands.tag.remove.success.single", tag, ((Entity)targets.iterator().next()).getDisplayName()), true);
        } else {
            source.sendFeedback(() -> Text.translatable("commands.tag.remove.success.multiple", tag, targets.size()), true);
        }
        return i;
    }

    private static int executeList(ServerCommandSource source, Collection<? extends Entity> targets) {
        HashSet<String> set = Sets.newHashSet();
        for (Entity entity : targets) {
            set.addAll(entity.getCommandTags());
        }
        if (targets.size() == 1) {
            Entity lv2 = targets.iterator().next();
            if (set.isEmpty()) {
                source.sendFeedback(() -> Text.translatable("commands.tag.list.single.empty", lv2.getDisplayName()), false);
            } else {
                source.sendFeedback(() -> Text.translatable("commands.tag.list.single.success", lv2.getDisplayName(), set.size(), Texts.joinOrdered(set)), false);
            }
        } else if (set.isEmpty()) {
            source.sendFeedback(() -> Text.translatable("commands.tag.list.multiple.empty", targets.size()), false);
        } else {
            source.sendFeedback(() -> Text.translatable("commands.tag.list.multiple.success", targets.size(), set.size(), Texts.joinOrdered(set)), false);
        }
        return set.size();
    }
}

