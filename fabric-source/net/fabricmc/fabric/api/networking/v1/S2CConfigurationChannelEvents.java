package net.fabricmc.fabric.api.networking.v1;

import java.util.List;

import net.minecraft.resources.Identifier;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerConfigurationPacketListenerImpl;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

/**
 * Offers access to events related to the indication of a connected client's ability to receive packets in certain channels.
 */
public final class S2CConfigurationChannelEvents {
	/**
	 * An event for the server configuration network handler receiving an update indicating the connected client's ability to receive packets in certain channels.
	 * This event may be invoked at any time after login and up to disconnection.
	 */
	public static final Event<Register> REGISTER = EventFactory.createArrayBacked(Register.class, callbacks -> (handler, sender, server, channels) -> {
		for (Register callback : callbacks) {
			callback.onChannelRegister(handler, sender, server, channels);
		}
	});

	/**
	 * An event for the server configuration network handler receiving an update indicating the connected client's lack of ability to receive packets in certain channels.
	 * This event may be invoked at any time after login and up to disconnection.
	 */
	public static final Event<Unregister> UNREGISTER = EventFactory.createArrayBacked(Unregister.class, callbacks -> (handler, sender, server, channels) -> {
		for (Unregister callback : callbacks) {
			callback.onChannelUnregister(handler, sender, server, channels);
		}
	});

	private S2CConfigurationChannelEvents() {
	}

	/**
	 * @see S2CConfigurationChannelEvents#REGISTER
	 */
	@FunctionalInterface
	public interface Register {
		void onChannelRegister(ServerConfigurationPacketListenerImpl handler, PacketSender sender, MinecraftServer server, List<Identifier> channels);
	}

	/**
	 * @see S2CConfigurationChannelEvents#UNREGISTER
	 */
	@FunctionalInterface
	public interface Unregister {
		void onChannelUnregister(ServerConfigurationPacketListenerImpl handler, PacketSender sender, MinecraftServer server, List<Identifier> channels);
	}
}
