/*
 * External method calls:
 *   Lnet/minecraft/server/command/CommandManager;literal(Ljava/lang/String;)Lcom/mojang/brigadier/builder/LiteralArgumentBuilder;
 *   Lnet/minecraft/server/command/CommandManager;requirePermissionLevel(Lnet/minecraft/command/permission/PermissionCheck;)Lnet/minecraft/command/permission/PermissionSourcePredicate;
 *   Lnet/minecraft/command/argument/EntityArgumentType;players()Lnet/minecraft/command/argument/EntityArgumentType;
 *   Lnet/minecraft/server/command/CommandManager;argument(Ljava/lang/String;Lcom/mojang/brigadier/arguments/ArgumentType;)Lcom/mojang/brigadier/builder/RequiredArgumentBuilder;
 *   Lnet/minecraft/command/argument/RegistryKeyArgumentType;registryKey(Lnet/minecraft/registry/RegistryKey;)Lnet/minecraft/command/argument/RegistryKeyArgumentType;
 *   Lnet/minecraft/server/network/ServerPlayerEntity;unlockRecipes(Ljava/util/Collection;)I
 *   Lnet/minecraft/server/command/ServerCommandSource;sendFeedback(Ljava/util/function/Supplier;Z)V
 *   Lnet/minecraft/server/network/ServerPlayerEntity;lockRecipes(Ljava/util/Collection;)I
 *   Lnet/minecraft/text/Text;translatable(Ljava/lang/String;[Ljava/lang/Object;)Lnet/minecraft/text/MutableText;
 *   Lnet/minecraft/recipe/ServerRecipeManager;values()Ljava/util/Collection;
 *   Lnet/minecraft/text/Text;translatable(Ljava/lang/String;)Lnet/minecraft/text/MutableText;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/server/command/RecipeCommand;executeTake(Lnet/minecraft/server/command/ServerCommandSource;Ljava/util/Collection;Ljava/util/Collection;)I
 *   Lnet/minecraft/server/command/RecipeCommand;executeGive(Lnet/minecraft/server/command/ServerCommandSource;Ljava/util/Collection;Ljava/util/Collection;)I
 */
package net.minecraft.server.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import java.util.Collection;
import java.util.Collections;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.command.argument.RegistryKeyArgumentType;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class RecipeCommand {
    private static final SimpleCommandExceptionType GIVE_FAILED_EXCEPTION = new SimpleCommandExceptionType(Text.translatable("commands.recipe.give.failed"));
    private static final SimpleCommandExceptionType TAKE_FAILED_EXCEPTION = new SimpleCommandExceptionType(Text.translatable("commands.recipe.take.failed"));

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)CommandManager.literal("recipe").requires(CommandManager.requirePermissionLevel(CommandManager.GAMEMASTERS_CHECK))).then(CommandManager.literal("give").then((ArgumentBuilder<ServerCommandSource, ?>)((RequiredArgumentBuilder)CommandManager.argument("targets", EntityArgumentType.players()).then((ArgumentBuilder<ServerCommandSource, ?>)CommandManager.argument("recipe", RegistryKeyArgumentType.registryKey(RegistryKeys.RECIPE)).executes(context -> RecipeCommand.executeGive((ServerCommandSource)context.getSource(), EntityArgumentType.getPlayers(context, "targets"), Collections.singleton(RegistryKeyArgumentType.getRecipeEntry(context, "recipe")))))).then(CommandManager.literal("*").executes(context -> RecipeCommand.executeGive((ServerCommandSource)context.getSource(), EntityArgumentType.getPlayers(context, "targets"), ((ServerCommandSource)context.getSource()).getServer().getRecipeManager().values())))))).then(CommandManager.literal("take").then((ArgumentBuilder<ServerCommandSource, ?>)((RequiredArgumentBuilder)CommandManager.argument("targets", EntityArgumentType.players()).then((ArgumentBuilder<ServerCommandSource, ?>)CommandManager.argument("recipe", RegistryKeyArgumentType.registryKey(RegistryKeys.RECIPE)).executes(context -> RecipeCommand.executeTake((ServerCommandSource)context.getSource(), EntityArgumentType.getPlayers(context, "targets"), Collections.singleton(RegistryKeyArgumentType.getRecipeEntry(context, "recipe")))))).then(CommandManager.literal("*").executes(context -> RecipeCommand.executeTake((ServerCommandSource)context.getSource(), EntityArgumentType.getPlayers(context, "targets"), ((ServerCommandSource)context.getSource()).getServer().getRecipeManager().values()))))));
    }

    private static int executeGive(ServerCommandSource source, Collection<ServerPlayerEntity> targets, Collection<RecipeEntry<?>> recipes) throws CommandSyntaxException {
        int i = 0;
        for (ServerPlayerEntity lv : targets) {
            i += lv.unlockRecipes(recipes);
        }
        if (i == 0) {
            throw GIVE_FAILED_EXCEPTION.create();
        }
        if (targets.size() == 1) {
            source.sendFeedback(() -> Text.translatable("commands.recipe.give.success.single", recipes.size(), ((ServerPlayerEntity)targets.iterator().next()).getDisplayName()), true);
        } else {
            source.sendFeedback(() -> Text.translatable("commands.recipe.give.success.multiple", recipes.size(), targets.size()), true);
        }
        return i;
    }

    private static int executeTake(ServerCommandSource source, Collection<ServerPlayerEntity> targets, Collection<RecipeEntry<?>> recipes) throws CommandSyntaxException {
        int i = 0;
        for (ServerPlayerEntity lv : targets) {
            i += lv.lockRecipes(recipes);
        }
        if (i == 0) {
            throw TAKE_FAILED_EXCEPTION.create();
        }
        if (targets.size() == 1) {
            source.sendFeedback(() -> Text.translatable("commands.recipe.take.success.single", recipes.size(), ((ServerPlayerEntity)targets.iterator().next()).getDisplayName()), true);
        } else {
            source.sendFeedback(() -> Text.translatable("commands.recipe.take.success.multiple", recipes.size(), targets.size()), true);
        }
        return i;
    }
}

