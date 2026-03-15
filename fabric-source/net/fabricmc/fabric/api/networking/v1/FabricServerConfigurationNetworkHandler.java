package net.fabricmc.fabric.api.networking.v1;

import net.minecraft.resources.Identifier;
import net.minecraft.server.network.ConfigurationTask;
import net.minecraft.server.network.ServerConfigurationPacketListenerImpl;

/**
 * Fabric-provided extensions for {@link ServerConfigurationPacketListenerImpl}.
 * This interface is automatically implemented via Mixin and interface injection.
 */
public interface FabricServerConfigurationNetworkHandler {
	/**
	 * Enqueues a {@link ConfigurationTask} task to be processed.
	 *
	 * <p>Before adding a task use {@link ServerConfigurationNetworking#canSend(ServerConfigurationPacketListenerImpl, Identifier)}
	 * to ensure that the client can process this task.
	 *
	 * <p>Once the client has handled the task a packet should be sent to the server.
	 * Upon receiving this packet the server should call {@link FabricServerConfigurationNetworkHandler#completeTask(ConfigurationTask.Type)},
	 * otherwise the client cannot join the world.
	 *
	 * @param task the task
	 */
	default void addTask(ConfigurationTask task) {
		throw new UnsupportedOperationException("Implemented via mixin");
	}

	/**
	 * Completes the task identified by {@code key}.
	 *
	 * @param key the task key
	 * @throws IllegalStateException if the current task is not {@code key}
	 */
	default void completeTask(ConfigurationTask.Type key) {
		throw new UnsupportedOperationException("Implemented via mixin");
	}
}
