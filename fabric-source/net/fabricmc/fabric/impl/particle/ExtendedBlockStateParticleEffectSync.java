package net.fabricmc.fabric.impl.particle;

import java.util.Set;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.impl.networking.FabricRegistryByteBuf;

public class ExtendedBlockStateParticleEffectSync implements ModInitializer {
	private static final Identifier PACKET_ID = Identifier.fromNamespaceAndPath("fabric", "extended_block_state_particle_effect_sync");

	@Override
	public void onInitialize() {
		PayloadTypeRegistry.configurationS2C().register(DummyPayload.ID, DummyPayload.CODEC);
	}

	public static boolean shouldEncodeFallback(RegistryFriendlyByteBuf buf) {
		Set<Identifier> channels = ((FabricRegistryByteBuf) buf).fabric_getSendableConfigurationChannels();

		if (channels == null) {
			return true;
		}

		return !channels.contains(ExtendedBlockStateParticleEffectSync.PACKET_ID);
	}

	public record DummyPayload() implements CustomPacketPayload {
		public static final DummyPayload INSTANCE = new DummyPayload();
		public static final StreamCodec<FriendlyByteBuf, DummyPayload> CODEC = StreamCodec.unit(INSTANCE);
		public static final CustomPacketPayload.Type<DummyPayload> ID = new Type<>(PACKET_ID);

		@Override
		public Type<? extends CustomPacketPayload> type() {
			return ID;
		}
	}
}
