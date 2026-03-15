package net.fabricmc.fabric.impl.tag;

import java.util.List;

import com.mojang.serialization.Codec;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;

/**
 * A wrapper record for tag alias groups.
 *
 * @param tags the tags in the group, must be from the same registry
 * @param <T> the type of registry entries in the tags
 */
public record TagAliasGroup<T>(List<TagKey<T>> tags) {
	/**
	 * Creates a codec for tag alias groups in the specified registry.
	 *
	 * @param registryKey the key of the registry where the tags are from
	 * @param <T>         the entry type
	 * @return the codec
	 */
	public static <T> Codec<TagAliasGroup<T>> codec(ResourceKey<? extends Registry<T>> registryKey) {
		return TagKey.codec(registryKey)
				.listOf()
				.fieldOf("tags")
				.xmap(TagAliasGroup::new, TagAliasGroup::tags)
				.codec();
	}
}
