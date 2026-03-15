package net.fabricmc.fabric.impl.recipe.ingredient;

import java.util.HashSet;
import java.util.Set;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

public record CustomIngredientPayloadC2S(int protocolVersion, Set<Identifier> registeredSerializers) implements CustomPacketPayload {
	public static final StreamCodec<FriendlyByteBuf, CustomIngredientPayloadC2S> CODEC = StreamCodec.composite(
			ByteBufCodecs.VAR_INT, CustomIngredientPayloadC2S::protocolVersion,
			ByteBufCodecs.collection(HashSet::new, Identifier.STREAM_CODEC), CustomIngredientPayloadC2S::registeredSerializers,
			CustomIngredientPayloadC2S::new
	);
	public static final CustomPacketPayload.Type<CustomIngredientPayloadC2S> ID = new Type<>(CustomIngredientSync.PACKET_ID);

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return ID;
	}
}
