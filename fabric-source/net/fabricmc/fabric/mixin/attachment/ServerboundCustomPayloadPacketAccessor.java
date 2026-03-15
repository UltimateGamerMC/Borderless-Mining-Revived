package net.fabricmc.fabric.mixin.attachment;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.network.protocol.common.ServerboundCustomPayloadPacket;

@Mixin(ServerboundCustomPayloadPacket.class)
public interface ServerboundCustomPayloadPacketAccessor {
	@Accessor("MAX_PAYLOAD_SIZE")
	static int getMaxPayloadSize() {
		throw new UnsupportedOperationException("Implemented via mixin");
	}
}
