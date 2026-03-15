package net.fabricmc.fabric.impl.datagen.loot;

import java.util.Collections;

import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootTable;

import net.fabricmc.fabric.api.resource.conditions.v1.ResourceCondition;
import net.fabricmc.fabric.impl.datagen.FabricDataGenHelper;
import net.fabricmc.fabric.mixin.datagen.loot.BlockLootSubProviderAccessor;

public class ConditionBlockLootTableGenerator extends BlockLootSubProvider {
	private final BlockLootSubProvider parent;
	private final ResourceCondition[] conditions;

	public ConditionBlockLootTableGenerator(BlockLootSubProvider parent, ResourceCondition[] conditions) {
		super(Collections.emptySet(), FeatureFlags.REGISTRY.allFlags(), ((BlockLootSubProviderAccessor) parent).getRegistries());

		this.parent = parent;
		this.conditions = conditions;
	}

	@Override
	public void generate() {
		throw new UnsupportedOperationException("generate() should not be called.");
	}

	@Override
	public void add(Block block, LootTable.Builder lootTable) {
		FabricDataGenHelper.addConditions(lootTable, conditions);
		this.parent.add(block, lootTable);
	}
}
