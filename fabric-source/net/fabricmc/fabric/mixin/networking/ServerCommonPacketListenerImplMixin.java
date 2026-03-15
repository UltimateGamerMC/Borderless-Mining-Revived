package net.fabricmc.fabric.mixin.networking;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.network.protocol.common.ServerboundCustomPayloadPacket;
import net.minecraft.network.protocol.common.ServerboundPongPacket;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.RunningOnDifferentThreadException;
import net.minecraft.server.network.ServerCommonPacketListenerImpl;

import net.fabricmc.fabric.impl.networking.NetworkHandlerExtensions;
import net.fabricmc.fabric.impl.networking.server.ServerConfigurationNetworkAddon;

@Mixin(ServerCommonPacketListenerImpl.class)
public abstract class ServerCommonPacketListenerImplMixin implements NetworkHandlerExtensions {
	@Shadow
	@Final
	protected MinecraftServer server;

	@Inject(method = "handleCustomPayload", at = @At("HEAD"), cancellable = true)
	private void handleCustomPayloadReceivedAsync(ServerboundCustomPayloadPacket packet, CallbackInfo ci) {
		final CustomPacketPayload payload = packet.payload();

		try {
			boolean handled;

			if (getAddon() instanceof ServerConfigurationNetworkAddon addon) {
				handled = addon.handle(payload);
			} else {
				// Play should be handled in ServerGamePacketListenerImplMixin
				throw new IllegalStateException("Unknown addon");
			}

			if (handled) {
				ci.cancel();
			}
		} catch (RunningOnDifferentThreadException e) {
			this.server.packetProcessor().scheduleIfPossible((ServerCommonPacketListenerImpl) (Object) this, packet);
			ci.cancel();
		}
	}

	@Inject(method = "handlePong", at = @At("HEAD"))
	private void onPlayPong(ServerboundPongPacket packet, CallbackInfo ci) {
		if (getAddon() instanceof ServerConfigurationNetworkAddon addon) {
			addon.onPong(packet.getId());
		}
	}
}
