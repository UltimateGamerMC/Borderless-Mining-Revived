package net.fabricmc.fabric.mixin.networking;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.server.commands.DebugConfigCommand;
import net.minecraft.server.network.ServerConfigurationPacketListenerImpl;

@Mixin(DebugConfigCommand.class)
public class DebugConfigCommandMixin {
	// endConfiguration() does not re-run the configuration tasks. This means we loose the state such as what channels we can send on when in the play phase.
	@Redirect(method = "unconfig", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerConfigurationPacketListenerImpl;returnToWorld()V"))
	private static void sendConfigurations(ServerConfigurationPacketListenerImpl networkHandler) {
		networkHandler.startConfiguration();
	}
}
