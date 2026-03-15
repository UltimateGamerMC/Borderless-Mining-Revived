package net.fabricmc.fabric.mixin.command;

import java.util.function.Predicate;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import net.minecraft.commands.arguments.selector.EntitySelectorParser;
import net.minecraft.commands.arguments.selector.options.EntitySelectorOptions;
import net.minecraft.network.chat.Component;

@Mixin(EntitySelectorOptions.class)
public interface EntitySelectorOptionsAccessor {
	@Invoker("register")
	static void callPutOption(String id, EntitySelectorOptions.Modifier handler, Predicate<EntitySelectorParser> condition, Component description) {
	}
}
