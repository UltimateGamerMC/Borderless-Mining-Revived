package net.fabricmc.fabric.impl.attachment.sync.s2c;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

public class RequestAcceptedAttachmentsPayloadS2C implements CustomPacketPayload {
	public static final RequestAcceptedAttachmentsPayloadS2C INSTANCE = new RequestAcceptedAttachmentsPayloadS2C();
	public static final Identifier PACKET_ID = Identifier.fromNamespaceAndPath("fabric", "accepted_attachments_v1");
	public static final Type<RequestAcceptedAttachmentsPayloadS2C> ID = new Type<>(PACKET_ID);
	public static final StreamCodec<FriendlyByteBuf, RequestAcceptedAttachmentsPayloadS2C> CODEC = StreamCodec.unit(INSTANCE);

	private RequestAcceptedAttachmentsPayloadS2C() {
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return ID;
	}
}
