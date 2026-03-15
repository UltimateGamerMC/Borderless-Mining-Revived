package net.fabricmc.fabric.api.util;

import org.jspecify.annotations.NullMarked;

import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

@NullMarked
public interface Block2ObjectMap<V> {
	V get(Block block);

	void add(Block block, V value);

	void add(TagKey<Block> tag, V value);

	void remove(Block block);

	void remove(TagKey<Block> tag);

	void clear(Block block);

	void clear(TagKey<Block> tag);
}
