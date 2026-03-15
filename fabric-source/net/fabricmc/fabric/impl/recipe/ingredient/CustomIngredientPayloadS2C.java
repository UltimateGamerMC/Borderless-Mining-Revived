package net.fabricmc.fabric.impl.recipe.ingredient;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record CustomIngredientPayloadS2C(int protocolVersion) implements CustomPacketPayload {
	public static final StreamCodec<FriendlyByteBuf, CustomIngredientPayloadS2C> CODEC = StreamCodec.composite(
			ByteBufCodecs.VAR_INT, CustomIngredientPayloadS2C::protocolVersion,
			CustomIngredientPayloadS2C::new
	);
	public static final CustomPacketPayload.Type<CustomIngredientPayloadS2C> ID = new Type<>(CustomIngredientSync.PACKET_ID);

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return ID;
	}
}
