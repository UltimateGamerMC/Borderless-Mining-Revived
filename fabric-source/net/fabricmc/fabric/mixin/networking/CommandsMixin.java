package net.fabricmc.fabric.mixin.networking;

import com.mojang.brigadier.CommandDispatcher;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.SharedConstants;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.commands.DebugConfigCommand;

import net.fabricmc.loader.api.FabricLoader;

@Mixin(Commands.class)
public class CommandsMixin {
	@Shadow
	@Final
	private CommandDispatcher<CommandSourceStack> dispatcher;

	@Inject(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/commands/BanIpCommands;register(Lcom/mojang/brigadier/CommandDispatcher;)V"))
	private void init(Commands.CommandSelection environment, CommandBuildContext commandRegistryAccess, CallbackInfo ci) {
		if (SharedConstants.IS_RUNNING_IN_IDE) {
			// Command is registered when isDevelopment is set.
			return;
		}

		if (!FabricLoader.getInstance().isDevelopmentEnvironment()) {
			// Only register this command in a dev env
			return;
		}

		DebugConfigCommand.register(this.dispatcher, commandRegistryAccess);
	}
}
