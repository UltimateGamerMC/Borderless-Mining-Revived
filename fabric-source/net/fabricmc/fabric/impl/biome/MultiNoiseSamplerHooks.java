package net.fabricmc.fabric.impl.biome;

import net.minecraft.world.level.levelgen.synth.ImprovedNoise;

public interface MultiNoiseSamplerHooks {
	ImprovedNoise fabric_getEndBiomesSampler();

	void fabric_setSeed(long seed);

	long fabric_getSeed();
}
