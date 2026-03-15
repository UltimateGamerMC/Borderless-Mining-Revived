package net.fabricmc.fabric.mixin.loot;

import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;

import net.fabricmc.fabric.api.loot.v3.FabricLootPoolBuilder;

/**
 * Accesses loot pool fields for {@link FabricLootPoolBuilder#copyOf(LootPool)}.
 * These are normally available in the transitive access widener module.
 */
@Mixin(LootPool.class)
public interface LootPoolAccessor {
	@Accessor("rolls")
	NumberProvider fabric_getRolls();

	@Accessor("bonusRolls")
	NumberProvider fabric_getBonusRolls();

	@Accessor("entries")
	List<LootPoolEntryContainer> fabric_getEntries();

	@Accessor("conditions")
	List<LootItemCondition> fabric_getConditions();

	@Accessor("functions")
	List<LootItemFunction> fabric_getFunctions();
}
