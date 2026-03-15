package net.fabricmc.fabric.mixin.command;

import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.server.commands.HelpCommand;

@Mixin(HelpCommand.class)
public interface HelpCommandAccessor {
	@Accessor("ERROR_FAILED")
	static SimpleCommandExceptionType getFailedException() {
		throw new AssertionError("mixin");
	}
}
