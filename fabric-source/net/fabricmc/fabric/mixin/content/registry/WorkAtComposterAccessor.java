package net.fabricmc.fabric.mixin.content.registry;

import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.world.entity.ai.behavior.WorkAtComposter;
import net.minecraft.world.item.Item;

@Mixin(WorkAtComposter.class)
public interface WorkAtComposterAccessor {
	@Mutable
	@Accessor("COMPOSTABLE_ITEMS")
	static void fabric_setCompostables(List<Item> items) {
		throw new AssertionError("Untransformed @Accessor");
	}

	@Accessor("COMPOSTABLE_ITEMS")
	static List<Item> fabric_getCompostable() {
		throw new AssertionError("Untransformed @Accessor");
	}
}
