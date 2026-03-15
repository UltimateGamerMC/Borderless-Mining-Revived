package net.fabricmc.fabric.impl.content.registry;

import net.minecraft.world.level.block.state.BlockState;

import net.fabricmc.fabric.api.registry.FlammableBlockRegistry;

public interface FireBlockHooks {
	FlammableBlockRegistry.Entry fabric_getVanillaEntry(BlockState block);
}
