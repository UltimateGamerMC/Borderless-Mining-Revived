package net.fabricmc.fabric.impl.particle;

import org.jspecify.annotations.Nullable;

import net.minecraft.core.BlockPos;

public interface BlockStateParticleEffectExtension {
	void fabric_setBlockPos(@Nullable BlockPos pos);
}
