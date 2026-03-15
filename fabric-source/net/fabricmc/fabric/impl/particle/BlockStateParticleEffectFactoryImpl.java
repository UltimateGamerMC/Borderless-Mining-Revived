package net.fabricmc.fabric.impl.particle;

import org.jspecify.annotations.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.world.level.block.state.BlockState;

public final class BlockStateParticleEffectFactoryImpl {
	private BlockStateParticleEffectFactoryImpl() {
	}

	public static BlockParticleOption create(ParticleType<BlockParticleOption> type, BlockState blockState, @Nullable BlockPos blockPos) {
		BlockParticleOption effect = new BlockParticleOption(type, blockState);
		((BlockStateParticleEffectExtension) effect).fabric_setBlockPos(blockPos);
		return effect;
	}
}
