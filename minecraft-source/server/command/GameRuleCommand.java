/*
 * External method calls:
 *   Lnet/minecraft/server/command/CommandManager;literal(Ljava/lang/String;)Lcom/mojang/brigadier/builder/LiteralArgumentBuilder;
 *   Lnet/minecraft/server/command/CommandManager;requirePermissionLevel(Lnet/minecraft/command/permission/PermissionCheck;)Lnet/minecraft/command/permission/PermissionSourcePredicate;
 *   Lnet/minecraft/world/rule/GameRules;accept(Lnet/minecraft/world/rule/GameRuleVisitor;)V
 *   Lnet/minecraft/server/command/CommandManager;argument(Ljava/lang/String;Lcom/mojang/brigadier/arguments/ArgumentType;)Lcom/mojang/brigadier/builder/RequiredArgumentBuilder;
 *   Lnet/minecraft/server/command/ServerCommandSource;sendFeedback(Ljava/util/function/Supplier;Z)V
 *   Lnet/minecraft/world/rule/GameRule;toShortString()Ljava/lang/String;
 *   Lnet/minecraft/text/Text;translatable(Ljava/lang/String;[Ljava/lang/Object;)Lnet/minecraft/text/MutableText;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/server/command/GameRuleCommand;executeSet(Lcom/mojang/brigadier/context/CommandContext;Lnet/minecraft/world/rule/GameRule;)I
 *   Lnet/minecraft/server/command/GameRuleCommand;executeQuery(Lnet/minecraft/server/command/ServerCommandSource;Lnet/minecraft/world/rule/GameRule;)I
 */
package net.minecraft.server.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.world.rule.GameRule;
import net.minecraft.world.rule.GameRuleVisitor;
import net.minecraft.world.rule.GameRules;

public class GameRuleCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess commandRegistryAccess) {
        final LiteralArgumentBuilder literalArgumentBuilder = (LiteralArgumentBuilder)CommandManager.literal("gamerule").requires(CommandManager.requirePermissionLevel(CommandManager.GAMEMASTERS_CHECK));
        new GameRules(commandRegistryAccess.getEnabledFeatures()).accept(new GameRuleVisitor(){

            @Override
            public <T> void visit(GameRule<T> rule) {
                LiteralArgumentBuilder<ServerCommandSource> literalArgumentBuilder3 = CommandManager.literal(rule.toShortString());
                LiteralArgumentBuilder<ServerCommandSource> literalArgumentBuilder2 = CommandManager.literal(rule.getId().toString());
                ((LiteralArgumentBuilder)literalArgumentBuilder.then(GameRuleCommand.appendRule(rule, literalArgumentBuilder3))).then(GameRuleCommand.appendRule(rule, literalArgumentBuilder2));
            }
        });
        dispatcher.register(literalArgumentBuilder);
    }

    static <T> LiteralArgumentBuilder<ServerCommandSource> appendRule(GameRule<T> rule, LiteralArgumentBuilder<ServerCommandSource> builder) {
        return (LiteralArgumentBuilder)((LiteralArgumentBuilder)builder.executes(context -> GameRuleCommand.executeQuery((ServerCommandSource)context.getSource(), rule))).then(CommandManager.argument("value", rule.getArgumentType()).executes(context -> GameRuleCommand.executeSet(context, rule)));
    }

    private static <T> int executeSet(CommandContext<ServerCommandSource> context, GameRule<T> key) {
        ServerCommandSource lv = context.getSource();
        Object object = context.getArgument("value", key.getValueClass());
        lv.getWorld().getGameRules().setValue(key, object, context.getSource().getServer());
        lv.sendFeedback(() -> Text.translatable("commands.gamerule.set", key.toShortString(), key.getValueName(object)), true);
        return key.getCommandResult(object);
    }

    private static <T> int executeQuery(ServerCommandSource source, GameRule<T> key) {
        Object object = source.getWorld().getGameRules().getValue(key);
        source.sendFeedback(() -> Text.translatable("commands.gamerule.query", key.toShortString(), key.getValueName(object)), false);
        return key.getCommandResult(object);
    }
}

