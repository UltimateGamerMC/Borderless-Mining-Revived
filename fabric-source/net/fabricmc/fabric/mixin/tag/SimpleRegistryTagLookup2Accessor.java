package net.fabricmc.fabric.mixin.tag;

import java.util.Map;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.core.HolderSet;
import net.minecraft.tags.TagKey;

@Mixin(targets = "net.minecraft.core.MappedRegistry$TagSet$2")
public interface SimpleRegistryTagLookup2Accessor<T> {
	@Accessor("val$tags")
	Map<TagKey<T>, HolderSet.Named<T>> fabric_getTagMap();

	@Accessor("val$tags")
	@Mutable
	void fabric_setTagMap(Map<TagKey<T>, HolderSet.Named<T>> tagMap);
}
