package net.fabricmc.fabric.impl.content.registry;

import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.ComposterBlock;

import net.fabricmc.fabric.api.registry.CompostingChanceRegistry;

public class CompostingChanceRegistryImpl implements CompostingChanceRegistry {
	@Override
	public Float get(ItemLike item) {
		return ComposterBlock.COMPOSTABLES.getOrDefault(item.asItem(), 0.0F);
	}

	@Override
	public void add(ItemLike item, Float value) {
		ComposterBlock.COMPOSTABLES.put(item.asItem(), value);
	}

	@Override
	public void add(TagKey<Item> tag, Float value) {
		throw new UnsupportedOperationException("Tags currently not supported!");
	}

	@Override
	public void remove(ItemLike item) {
		ComposterBlock.COMPOSTABLES.removeFloat(item.asItem());
	}

	@Override
	public void remove(TagKey<Item> tag) {
		throw new UnsupportedOperationException("Tags currently not supported!");
	}

	@Override
	public void clear(ItemLike item) {
		throw new UnsupportedOperationException("CompostingChanceRegistry operates directly on the vanilla map - clearing not supported!");
	}

	@Override
	public void clear(TagKey<Item> tag) {
		throw new UnsupportedOperationException("CompostingChanceRegistry operates directly on the vanilla map - clearing not supported!");
	}
}
