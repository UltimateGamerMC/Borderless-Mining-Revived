package net.fabricmc.fabric.mixin.datagen.loot;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.data.loot.EntityLootSubProvider;

import net.fabricmc.fabric.api.datagen.v1.loot.FabricEntityLootTableGenerator;

@Mixin(EntityLootSubProvider.class)
public class EntityLootSubProviderMixin implements FabricEntityLootTableGenerator {
}
