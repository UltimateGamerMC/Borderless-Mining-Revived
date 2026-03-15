package net.fabricmc.fabric.mixin.content.registry;

import java.util.Map;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.world.entity.npc.villager.Villager;
import net.minecraft.world.item.Item;

@Mixin(Villager.class)
public interface VillagerAccessor {
	@Mutable
	@Accessor("FOOD_POINTS")
	static void fabric_setItemFoodValues(Map<Item, Integer> items) {
		throw new AssertionError("Untransformed @Accessor");
	}
}
