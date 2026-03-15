package net.fabricmc.fabric.impl.attachment.sync.s2c;

import java.util.List;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

import net.fabricmc.fabric.impl.attachment.sync.AttachmentChange;

public record AttachmentSyncPayloadS2C(List<AttachmentChange> attachments) implements CustomPacketPayload {
	public static final StreamCodec<FriendlyByteBuf, AttachmentSyncPayloadS2C> CODEC = StreamCodec.composite(
			AttachmentChange.PACKET_CODEC.apply(ByteBufCodecs.list()), AttachmentSyncPayloadS2C::attachments,
			AttachmentSyncPayloadS2C::new
	);
	public static final Identifier PACKET_ID = Identifier.fromNamespaceAndPath("fabric", "attachment_sync_v1");
	public static final Type<AttachmentSyncPayloadS2C> ID = new Type<>(PACKET_ID);

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return ID;
	}
}
