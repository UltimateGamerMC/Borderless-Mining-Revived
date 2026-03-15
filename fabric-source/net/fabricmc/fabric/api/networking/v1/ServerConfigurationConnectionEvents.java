package net.fabricmc.fabric.api.networking.v1;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerConfigurationPacketListenerImpl;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

/**
 * Offers access to events related to the connection to a client on a logical server while a client is configuring.
 */
public final class ServerConfigurationConnectionEvents {
	/**
	 * Event fired before any vanilla configuration has taken place.
	 *
	 * <p>This event is executed on {@linkplain io.netty.channel.EventLoop netty's event loops}.
	 *
	 * <p>Task queued during this event will complete before vanilla configuration starts.
	 */
	public static final Event<Configure> BEFORE_CONFIGURE = EventFactory.createArrayBacked(Configure.class, callbacks -> (handler, server) -> {
		for (Configure callback : callbacks) {
			callback.onSendConfiguration(handler, server);
		}
	});

	/**
	 * Event fired during vanilla configuration.
	 *
	 * <p>This event is executed on {@linkplain io.netty.channel.EventLoop netty's event loops}.
	 *
	 * <p>An example usage of this:
	 * <pre>{@code
	 * ServerConfigurationConnectionEvents.CONFIGURE.register((handler, server) -> {
	 * 	if (ServerConfigurationNetworking.canSend(handler, ConfigurationPacket.PACKET_TYPE)) {
	 *  handler.addTask(new TestConfigurationTask("Example data"));
	 * 	} else {
	 * 	  // You can opt to disconnect the client if it cannot handle the configuration task
	 * 	  handler.disconnect(Text.literal("Network test configuration not supported by client"));
	 * 	  }
	 * });
	 * }</pre>
	 */
	public static final Event<Configure> CONFIGURE = EventFactory.createArrayBacked(Configure.class, callbacks -> (handler, server) -> {
		for (Configure callback : callbacks) {
			callback.onSendConfiguration(handler, server);
		}
	});

	/**
	 * An event for the disconnection of the server configuration network handler.
	 *
	 * <p>No packets should be sent when this event is invoked.
	 */
	public static final Event<ServerConfigurationConnectionEvents.Disconnect> DISCONNECT = EventFactory.createArrayBacked(ServerConfigurationConnectionEvents.Disconnect.class, callbacks -> (handler, server) -> {
		for (ServerConfigurationConnectionEvents.Disconnect callback : callbacks) {
			callback.onConfigureDisconnect(handler, server);
		}
	});

	private ServerConfigurationConnectionEvents() {
	}

	@FunctionalInterface
	public interface Configure {
		void onSendConfiguration(ServerConfigurationPacketListenerImpl handler, MinecraftServer server);
	}

	@FunctionalInterface
	public interface Disconnect {
		void onConfigureDisconnect(ServerConfigurationPacketListenerImpl handler, MinecraftServer server);
	}
}
