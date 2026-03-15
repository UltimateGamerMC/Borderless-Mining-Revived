package net.fabricmc.fabric.impl.networking;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

public interface CustomPayloadTypeProvider<B extends FriendlyByteBuf> {
	CustomPacketPayload.TypeAndCodec<B, ? extends CustomPacketPayload> get(B packetByteBuf, Identifier identifier);
}
