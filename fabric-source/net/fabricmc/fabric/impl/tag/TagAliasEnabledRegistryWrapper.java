package net.fabricmc.fabric.impl.tag;

import java.util.Map;
import java.util.Set;

import net.minecraft.tags.TagKey;

/**
 * Implemented on {@code RegistryWrapper.Impl} instances used during data loading
 * to give access to the underlying registry.
 */
public interface TagAliasEnabledRegistryWrapper {
	void fabric_loadTagAliases(Map<TagKey<?>, Set<TagKey<?>>> aliasGroups);
}
