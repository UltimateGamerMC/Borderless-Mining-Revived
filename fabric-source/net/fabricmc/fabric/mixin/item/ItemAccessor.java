package net.fabricmc.fabric.mixin.item;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.core.component.DataComponentMap;
import net.minecraft.world.item.Item;

@Mixin(Item.class)
public interface ItemAccessor {
	@Accessor
	@Mutable
	void setComponents(DataComponentMap components);
}
