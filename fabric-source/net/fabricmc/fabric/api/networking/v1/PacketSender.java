package net.fabricmc.fabric.api.networking.v1;

import io.netty.channel.ChannelFutureListener;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.Nullable;

import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

/**
 * Represents something that supports sending packets to channels.
 * Any packets sent must be {@linkplain PayloadTypeRegistry registered} in the appropriate registry.
 */
@ApiStatus.NonExtendable
public interface PacketSender {
	/**
	 * Creates a packet from a packet payload.
	 *
	 * @param payload the packet payload
	 */
	Packet<?> createPacket(CustomPacketPayload payload);

	/**
	 * Sends a packet.
	 *
	 * @param packet the packet
	 */
	default void sendPacket(Packet<?> packet) {
		sendPacket(packet, null);
	}

	/**
	 * Sends a packet.
	 * @param payload the payload
	 */
	default void sendPacket(CustomPacketPayload payload) {
		sendPacket(createPacket(payload));
	}

	/**
	 * Sends a packet.
	 *
	 * @param packet the packet
	 * @param callback an optional callback to execute after the packet is sent, may be {@code null}.
	 */
	void sendPacket(Packet<?> packet, @Nullable ChannelFutureListener callback);

	/**
	 * Sends a packet.
	 *
	 * @param payload the payload
	 * @param callback an optional callback to execute after the packet is sent, may be {@code null}.
	 */
	default void sendPacket(CustomPacketPayload payload, @Nullable ChannelFutureListener callback) {
		sendPacket(createPacket(payload), callback);
	}

	/**
	 * Disconnects the player.
	 * @param disconnectReason the reason for disconnection
	 */
	void disconnect(Component disconnectReason);
}
