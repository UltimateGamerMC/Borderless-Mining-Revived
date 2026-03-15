package net.fabricmc.fabric.mixin.biome;

import com.google.common.base.Preconditions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import net.minecraft.world.level.biome.Climate;
import net.minecraft.world.level.levelgen.LegacyRandomSource;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.synth.ImprovedNoise;

import net.fabricmc.fabric.impl.biome.MultiNoiseSamplerHooks;

@Mixin(Climate.Sampler.class)
public class ClimateSamplerMixin implements MultiNoiseSamplerHooks {
	@Unique
	private Long seed = null;

	@Unique
	private ImprovedNoise endBiomesSampler = null;

	@Override
	public void fabric_setSeed(long seed) {
		this.seed = seed;
	}

	@Override
	public long fabric_getSeed() {
		return this.seed;
	}

	@Override
	public ImprovedNoise fabric_getEndBiomesSampler() {
		if (endBiomesSampler == null) {
			Preconditions.checkState(seed != null, "MultiNoiseSampler doesn't have a seed set, created using different method?");
			endBiomesSampler = new ImprovedNoise(new WorldgenRandom(new LegacyRandomSource(seed)));
		}

		return endBiomesSampler;
	}
}
