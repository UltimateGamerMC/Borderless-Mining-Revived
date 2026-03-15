package net.fabricmc.fabric.api.networking.v1;

import java.util.Objects;

import io.netty.channel.ChannelFutureListener;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.Nullable;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.resources.Identifier;

/**
 * Represents something that supports sending packets to login channels.
 * @see PacketSender
 */
@ApiStatus.NonExtendable
public interface LoginPacketSender extends PacketSender {
	/**
	 * Creates a packet for sending to a login channel.
	 *
	 * @param channelName the id of the channel
	 * @param buf the content of the packet
	 * @return the created packet
	 */
	Packet<?> createPacket(Identifier channelName, FriendlyByteBuf buf);

	/**
	 * Sends a packet to a channel.
	 *
	 * @param channel the id of the channel
	 * @param buf the content of the packet
	 */
	default void sendPacket(Identifier channel, FriendlyByteBuf buf) {
		Objects.requireNonNull(channel, "Channel cannot be null");
		Objects.requireNonNull(buf, "Payload cannot be null");

		this.sendPacket(this.createPacket(channel, buf));
	}

	/**
	 * Sends a packet to a channel.
	 *
	 * @param channel  the id of the channel
	 * @param buf the content of the packet
	 * @param callback an optional callback to execute after the packet is sent, may be {@code null}
	 */
	default void sendPacket(Identifier channel, FriendlyByteBuf buf, @Nullable ChannelFutureListener callback) {
		Objects.requireNonNull(channel, "Channel cannot be null");
		Objects.requireNonNull(buf, "Payload cannot be null");

		this.sendPacket(this.createPacket(channel, buf), callback);
	}
}
