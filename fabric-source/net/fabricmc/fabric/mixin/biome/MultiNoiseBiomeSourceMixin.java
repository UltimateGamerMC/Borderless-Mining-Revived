package net.fabricmc.fabric.mixin.biome;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import net.minecraft.world.level.biome.MultiNoiseBiomeSource;

import net.fabricmc.fabric.impl.biome.BiomeSourceAccess;

@Mixin(MultiNoiseBiomeSource.class)
public class MultiNoiseBiomeSourceMixin implements BiomeSourceAccess {
	@Unique
	private boolean modifyBiomeEntries = true;

	@Override
	public void fabric_setModifyBiomeEntries(boolean modifyBiomeEntries) {
		this.modifyBiomeEntries = modifyBiomeEntries;
	}

	@Override
	public boolean fabric_shouldModifyBiomeEntries() {
		return this.modifyBiomeEntries;
	}
}
