package net.fabricmc.fabric.mixin.gamerule;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.commands.GameRuleCommand;
import net.minecraft.world.level.gamerules.GameRule;

@Mixin(GameRuleCommand.class)
public interface GameRuleCommandAccessor {
	@Invoker("queryRule")
	static <T> int invokeExecuteQuery(CommandSourceStack serverCommandSource, GameRule<T> ruleKey) {
		throw new AssertionError("This shouldn't happen!");
	}
}
