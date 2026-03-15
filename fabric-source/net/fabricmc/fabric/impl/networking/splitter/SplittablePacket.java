package net.fabricmc.fabric.impl.networking.splitter;

import java.util.function.Consumer;

import io.netty.channel.ChannelHandlerContext;

import net.minecraft.network.PacketEncoder;
import net.minecraft.network.protocol.Packet;

import net.fabricmc.fabric.impl.networking.PayloadTypeRegistryImpl;

public interface SplittablePacket {
	void fabric_split(PayloadTypeRegistryImpl<?> payloadTypeRegistry, ChannelHandlerContext channelHandlerContext, PacketEncoder<?> encoder, Packet<?> packet, Consumer<Packet<?>> consumer) throws Exception;
}
