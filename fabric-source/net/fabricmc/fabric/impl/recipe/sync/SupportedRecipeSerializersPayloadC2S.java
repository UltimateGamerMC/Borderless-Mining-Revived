package net.fabricmc.fabric.impl.recipe.sync;

import java.util.HashSet;
import java.util.Set;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

/**
 * Used to notify server which recipes can be synced to the client.
 */
public record SupportedRecipeSerializersPayloadC2S(Set<Identifier> synchronizedSerializers) implements CustomPacketPayload {
	public static final StreamCodec<FriendlyByteBuf, SupportedRecipeSerializersPayloadC2S> CODEC = StreamCodec.composite(
			ByteBufCodecs.collection(HashSet::new, Identifier.STREAM_CODEC), SupportedRecipeSerializersPayloadC2S::synchronizedSerializers,
			SupportedRecipeSerializersPayloadC2S::new
	);
	public static final Type<SupportedRecipeSerializersPayloadC2S> ID = new Type<>(Identifier.fromNamespaceAndPath("fabric", "recipe_sync/supported_serializers"));

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return ID;
	}
}
