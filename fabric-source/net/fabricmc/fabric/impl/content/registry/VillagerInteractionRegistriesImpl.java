package net.fabricmc.fabric.impl.content.registry;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.minecraft.world.entity.npc.villager.Villager;
import net.minecraft.world.item.Item;

import net.fabricmc.fabric.impl.content.registry.util.ImmutableCollectionUtils;
import net.fabricmc.fabric.mixin.content.registry.VillagerAccessor;
import net.fabricmc.fabric.mixin.content.registry.WorkAtComposterAccessor;

public final class VillagerInteractionRegistriesImpl {
	private static final Set<Item> GATHERABLE_ITEMS = new HashSet<>();

	private VillagerInteractionRegistriesImpl() {
	}

	public static Set<Item> getCollectableRegistry() {
		return GATHERABLE_ITEMS;
	}

	public static List<Item> getCompostableRegistry() {
		return ImmutableCollectionUtils.getAsMutableList(WorkAtComposterAccessor::fabric_getCompostable, WorkAtComposterAccessor::fabric_setCompostables);
	}

	public static Map<Item, Integer> getFoodRegistry() {
		return ImmutableCollectionUtils.getAsMutableMap(() -> Villager.FOOD_POINTS, VillagerAccessor::fabric_setItemFoodValues);
	}
}
