package net.fabricmc.fabric.mixin.command;

import com.mojang.brigadier.CommandDispatcher;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;

@Mixin(Commands.class)
public abstract class CommandsMixin {
	@Shadow
	@Final
	private CommandDispatcher<CommandSourceStack> dispatcher;

	/**
	 * Wait an inject in a constructor?
	 * This is a new addition to Fabric's fork of mixin.
	 * If you are not using fabric's fork of mixin this will fail.
	 *
	 * @reason Add commands before ambiguities are calculated.
	 */
	@Inject(at = @At(value = "INVOKE", target = "Lcom/mojang/brigadier/CommandDispatcher;setConsumer(Lcom/mojang/brigadier/ResultConsumer;)V"), method = "<init>")
	private void fabric_addCommands(Commands.CommandSelection environment, CommandBuildContext registryAccess, CallbackInfo ci) {
		CommandRegistrationCallback.EVENT.invoker().register(this.dispatcher, registryAccess, environment);
	}
}
