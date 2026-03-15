package net.fabricmc.fabric.mixin.networking;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.network.Connection;
import net.minecraft.network.PacketListener;
import net.minecraft.network.ProtocolInfo;
import net.minecraft.network.protocol.common.ServerboundCustomPayloadPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.RunningOnDifferentThreadException;
import net.minecraft.server.network.CommonListenerCookie;
import net.minecraft.server.network.ServerCommonPacketListenerImpl;
import net.minecraft.server.network.ServerConfigurationPacketListenerImpl;
import net.minecraft.server.network.ServerGamePacketListenerImpl;

import net.fabricmc.fabric.impl.networking.NetworkHandlerExtensions;
import net.fabricmc.fabric.impl.networking.UntrackedNetworkHandler;
import net.fabricmc.fabric.impl.networking.server.ServerNetworkingImpl;
import net.fabricmc.fabric.impl.networking.server.ServerPlayNetworkAddon;

// We want to apply a bit earlier than other mods which may not use us in order to prevent refCount issues
@Mixin(value = ServerGamePacketListenerImpl.class, priority = 999)
abstract class ServerGamePacketListenerImplMixin extends ServerCommonPacketListenerImpl implements NetworkHandlerExtensions {
	@Unique
	private ServerPlayNetworkAddon addon;

	ServerGamePacketListenerImplMixin(MinecraftServer server, Connection connection, CommonListenerCookie arg) {
		super(server, connection, arg);
	}

	@Inject(method = "<init>", at = @At("RETURN"))
	private void initAddon(CallbackInfo ci) {
		this.addon = new ServerPlayNetworkAddon((ServerGamePacketListenerImpl) (Object) this, connection, server);

		if (!(this instanceof UntrackedNetworkHandler)) {
			// A bit of a hack but it allows the field above to be set in case someone registers handlers during INIT event which refers to said field
			this.addon.lateInit();
		}
	}

	@Inject(method = "handleCustomPayload", at = @At("HEAD"), cancellable = true)
	private void handleCustomPayloadReceivedAsync(ServerboundCustomPayloadPacket packet, CallbackInfo ci) {
		try {
			if (getAddon().handle(packet.payload())) {
				ci.cancel();
			}
		} catch (RunningOnDifferentThreadException e) {
			this.server.packetProcessor().scheduleIfPossible(this, packet);
			ci.cancel();
		}
	}

	@WrapOperation(method = "handleConfigurationAcknowledged", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/Connection;setupInboundProtocol(Lnet/minecraft/network/ProtocolInfo;Lnet/minecraft/network/PacketListener;)V"))
	private <T extends PacketListener> void onAcknowledgeReconfiguration(Connection instance, ProtocolInfo<T> state, T packetListener, Operation<Void> original) {
		original.call(instance, state, packetListener);

		ServerConfigurationPacketListenerImpl networkHandler = (ServerConfigurationPacketListenerImpl) packetListener;
		ServerNetworkingImpl.getAddon(networkHandler).setReconfiguring();

		if (addon.requestedReconfigure()) {
			networkHandler.startConfiguration();
		}
	}

	@Override
	public ServerPlayNetworkAddon getAddon() {
		return this.addon;
	}
}
