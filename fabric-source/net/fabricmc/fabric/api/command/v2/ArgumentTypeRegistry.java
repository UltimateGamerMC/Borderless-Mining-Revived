package net.fabricmc.fabric.api.command.v2;

import com.mojang.brigadier.arguments.ArgumentType;

import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;

import net.fabricmc.fabric.mixin.command.ArgumentTypeInfosAccessor;

public final class ArgumentTypeRegistry {
	/**
	 * Register a new argument type.
	 *
	 * @param id the identifier of the argument type
	 * @param clazz the class of the argument type
	 * @param serializer the serializer for the argument type
	 * @param <A> the argument type
	 * @param <T> the argument type properties
	 */
	public static <A extends ArgumentType<?>, T extends ArgumentTypeInfo.Template<A>> void registerArgumentType(
			Identifier id, Class<? extends A> clazz, ArgumentTypeInfo<A, T> serializer) {
		ArgumentTypeInfosAccessor.fabric_getClassMap().put(clazz, serializer);
		Registry.register(BuiltInRegistries.COMMAND_ARGUMENT_TYPE, id, serializer);
	}

	private ArgumentTypeRegistry() {
	}
}
