package net.fabricmc.fabric.impl.particle;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

public class ExtendedBlockStateParticleEffectPacketCodec implements StreamCodec<RegistryFriendlyByteBuf, BlockParticleOption> {
	private static final int PACKET_MARKER = -1;
	private final StreamCodec<? super RegistryFriendlyByteBuf, BlockParticleOption> fallback;

	public ExtendedBlockStateParticleEffectPacketCodec(StreamCodec<? super RegistryFriendlyByteBuf, BlockParticleOption> fallback) {
		this.fallback = fallback;
	}

	@Override
	public BlockParticleOption decode(RegistryFriendlyByteBuf buf) {
		int index = buf.readerIndex();

		if (buf.readVarInt() != PACKET_MARKER) {
			// Reset index for vanilla's normal deserialization logic.
			buf.readerIndex(index);
			return fallback.decode(buf);
		}

		BlockParticleOption value = fallback.decode(buf);
		BlockPos pos = BlockPos.STREAM_CODEC.decode(buf);
		((BlockStateParticleEffectExtension) value).fabric_setBlockPos(pos);
		return value;
	}

	@Override
	public void encode(RegistryFriendlyByteBuf buf, BlockParticleOption value) {
		BlockPos pos = value.getBlockPos();

		if (pos == null || ExtendedBlockStateParticleEffectSync.shouldEncodeFallback(buf)) {
			fallback.encode(buf, value);
			return;
		}

		buf.writeVarInt(PACKET_MARKER);
		fallback.encode(buf, value);
		BlockPos.STREAM_CODEC.encode(buf, pos);
	}
}
