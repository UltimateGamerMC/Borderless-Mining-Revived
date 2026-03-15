package net.fabricmc.fabric.mixin.blockview;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.biome.Biome;

@Mixin(LevelReader.class)
public interface LevelReaderMixin extends BlockAndTintGetter {
	@Shadow
	Holder<Biome> getBiome(BlockPos pos);

	@Override
	default boolean hasBiomes() {
		return true;
	}

	@Override
	default Holder<Biome> getBiomeFabric(BlockPos pos) {
		return getBiome(pos);
	}
}
