package net.fabricmc.fabric.impl.networking.payload;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.login.custom.CustomQueryAnswerPayload;

public record PacketByteBufLoginQueryResponse(FriendlyByteBuf data) implements CustomQueryAnswerPayload {
	@Override
	public void write(FriendlyByteBuf buf) {
		PayloadHelper.write(buf, data());
	}
}
