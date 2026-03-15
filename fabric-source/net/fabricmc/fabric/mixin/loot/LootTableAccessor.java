package net.fabricmc.fabric.mixin.loot;

import java.util.List;
import java.util.Optional;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.resources.Identifier;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;

import net.fabricmc.fabric.api.loot.v3.FabricLootTableBuilder;

/**
 * Accesses loot table fields for {@link FabricLootTableBuilder#copyOf(LootTable)}.
 * These are normally available in the transitive access widener module.
 */
@Mixin(LootTable.class)
public interface LootTableAccessor {
	@Accessor("pools")
	List<LootPool> fabric_getPools();

	@Accessor("functions")
	List<LootItemFunction> fabric_getFunctions();

	@Accessor("randomSequence")
	Optional<Identifier> fabric_getRandomSequenceId();
}
