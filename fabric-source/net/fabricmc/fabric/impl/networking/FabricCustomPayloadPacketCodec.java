package net.fabricmc.fabric.impl.networking;

import net.minecraft.network.FriendlyByteBuf;

public interface FabricCustomPayloadPacketCodec<B extends FriendlyByteBuf> {
	void fabric_setPacketCodecProvider(CustomPayloadTypeProvider<B> customPayloadTypeProvider);
}
