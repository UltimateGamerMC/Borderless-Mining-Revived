package net.fabricmc.fabric.impl.networking.splitter;

import io.netty.buffer.ByteBuf;

import net.minecraft.network.PacketEncoder;
import net.minecraft.network.PacketListener;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.protocol.PacketType;
import net.minecraft.resources.Identifier;

import net.fabricmc.fabric.impl.networking.NetworkingImpl;

/**
 * A fake packet implementation used to pass already encoded data from {@link FabricPacketSplitter} to {@link PacketEncoder}.
 * Allows to avoid requiring to serialize the packet twice.
 */
public record PassthroughPacket(ByteBuf buf) implements Packet<PacketListener> {
	private static final PacketType<? extends Packet<PacketListener>> FAKE_TYPE = new PacketType<>(PacketFlow.SERVERBOUND, Identifier.fromNamespaceAndPath(NetworkingImpl.MOD_ID, "passthrough"));

	@Override
	public PacketType<? extends Packet<PacketListener>> type() {
		return FAKE_TYPE;
	}

	@Override
	public void handle(PacketListener listener) {
		throw new UnsupportedOperationException("This is not a real packet!");
	}
}
