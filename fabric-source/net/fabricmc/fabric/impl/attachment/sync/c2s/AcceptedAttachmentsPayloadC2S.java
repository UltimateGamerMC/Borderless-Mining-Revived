package net.fabricmc.fabric.impl.attachment.sync.c2s;

import java.util.HashSet;
import java.util.Set;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

public record AcceptedAttachmentsPayloadC2S(Set<Identifier> acceptedAttachments) implements CustomPacketPayload {
	public static final StreamCodec<FriendlyByteBuf, AcceptedAttachmentsPayloadC2S> CODEC = StreamCodec.composite(
			ByteBufCodecs.collection(HashSet::new, Identifier.STREAM_CODEC), AcceptedAttachmentsPayloadC2S::acceptedAttachments,
			AcceptedAttachmentsPayloadC2S::new
	);
	public static final Identifier PACKET_ID = Identifier.fromNamespaceAndPath("fabric", "accepted_attachments_v1");
	public static final Type<AcceptedAttachmentsPayloadC2S> ID = new Type<>(PACKET_ID);

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return ID;
	}
}
