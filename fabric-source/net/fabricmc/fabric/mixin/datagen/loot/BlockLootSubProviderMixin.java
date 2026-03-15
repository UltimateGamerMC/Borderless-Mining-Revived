package net.fabricmc.fabric.mixin.datagen.loot;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.data.loot.BlockLootSubProvider;

import net.fabricmc.fabric.api.datagen.v1.loot.FabricBlockLootTableGenerator;

@Mixin(BlockLootSubProvider.class)
public class BlockLootSubProviderMixin implements FabricBlockLootTableGenerator {
}
