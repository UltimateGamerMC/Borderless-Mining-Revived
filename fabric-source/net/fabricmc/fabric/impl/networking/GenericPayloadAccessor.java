package net.fabricmc.fabric.impl.networking;

import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public interface GenericPayloadAccessor {
	CustomPacketPayload fabric_payload();
}
