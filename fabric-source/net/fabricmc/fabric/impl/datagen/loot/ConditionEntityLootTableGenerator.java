package net.fabricmc.fabric.impl.datagen.loot;

import net.minecraft.data.loot.EntityLootSubProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.storage.loot.LootTable;

import net.fabricmc.fabric.api.resource.conditions.v1.ResourceCondition;
import net.fabricmc.fabric.impl.datagen.FabricDataGenHelper;
import net.fabricmc.fabric.mixin.datagen.loot.EntityLootSubProviderAccessor;

public class ConditionEntityLootTableGenerator extends EntityLootSubProvider {
	private final EntityLootSubProvider parent;
	private final ResourceCondition[] conditions;

	public ConditionEntityLootTableGenerator(EntityLootSubProvider parent, ResourceCondition[] conditions) {
		super(FeatureFlags.REGISTRY.allFlags(), ((EntityLootSubProviderAccessor) parent).getRegistries());

		this.parent = parent;
		this.conditions = conditions;
	}

	@Override
	public void generate() {
		throw new UnsupportedOperationException("generate() should not be called.");
	}

	@Override
	public void add(EntityType<?> entityType, ResourceKey<LootTable> tableKey, LootTable.Builder lootTable) {
		FabricDataGenHelper.addConditions(lootTable, this.conditions);
		this.parent.add(entityType, tableKey, lootTable);
	}
}
