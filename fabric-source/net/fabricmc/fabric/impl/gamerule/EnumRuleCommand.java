package net.fabricmc.fabric.impl.gamerule;

import static net.minecraft.commands.Commands.literal;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.tree.LiteralCommandNode;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.gamerules.GameRule;

import net.fabricmc.fabric.mixin.gamerule.GameRuleCommandAccessor;

public final class EnumRuleCommand {
	public static <E extends Enum<E>> void register(LiteralArgumentBuilder<CommandSourceStack> literalArgumentBuilder, GameRule<E> enumRule) {
		String name = enumRule.toString();
		literalArgumentBuilder.then(literal(name).executes(context -> {
			// We can use the vanilla query method
			return GameRuleCommandAccessor.invokeExecuteQuery(context.getSource(), enumRule);
		}));

		// The LiteralRuleType handles the executeSet
		LiteralCommandNode<CommandSourceStack> ruleNode = literal(name).build();

		for (Enum<?> supportedValue : ((RuleTypeExtensions) (Object) enumRule).fabric_getSupportedEnumValues()) {
			ruleNode.addChild(literal(supportedValue.toString()).executes(context -> EnumRuleCommand.executeAndSetEnum(context, (E) supportedValue, enumRule)).build());
		}

		literalArgumentBuilder.then(ruleNode);
	}

	public static <E extends Enum<E>> int executeAndSetEnum(CommandContext<CommandSourceStack> context, E value, GameRule<E> enumRule) throws CommandSyntaxException {
		// Mostly copied from vanilla, but tweaked so we can use literals
		CommandSourceStack serverCommandSource = context.getSource();

		try {
			serverCommandSource.getLevel().getGameRules().set(enumRule, value, serverCommandSource.getServer());
		} catch (IllegalArgumentException e) {
			throw new SimpleCommandExceptionType(Component.literal(e.getMessage())).create();
		}

		serverCommandSource.sendSuccess(() -> Component.translatable("commands.gamerule.set", enumRule.id(), enumRule.serialize(value)), true);
		return enumRule.getCommandResult(value);
	}
}
