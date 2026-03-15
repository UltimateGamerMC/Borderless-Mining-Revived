package net.fabricmc.fabric.mixin.datagen.loot;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.EntityLootSubProvider;

@Mixin(EntityLootSubProvider.class)
public interface EntityLootSubProviderAccessor {
	@Accessor()
	HolderLookup.Provider getRegistries();
}
