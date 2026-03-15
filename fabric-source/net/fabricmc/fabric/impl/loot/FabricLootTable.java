package net.fabricmc.fabric.impl.loot;

import net.minecraft.core.Holder;
import net.minecraft.world.level.storage.loot.LootTable;

public interface FabricLootTable {
	void fabric$setRegistryEntry(Holder<LootTable> key);
}
