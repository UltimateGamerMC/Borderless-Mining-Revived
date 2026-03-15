package net.fabricmc.fabric.mixin.item;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.resources.DependantName;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;

import net.fabricmc.fabric.api.item.v1.FabricItem;

@Mixin(Item.Properties.class)
public class ItemPropertiesMixin implements FabricItem.Settings {
	@Final
	@Shadow
	@Mutable
	private DependantName<Item, Identifier> model;

	@Override
	public Item.Properties modelId(Identifier modelId) {
		this.model = DependantName.fixed(modelId);
		return FabricItem.Settings.super.modelId(modelId);
	}
}
