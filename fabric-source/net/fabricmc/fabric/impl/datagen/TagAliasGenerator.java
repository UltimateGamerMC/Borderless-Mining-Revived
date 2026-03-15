package net.fabricmc.fabric.impl.datagen;

import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import net.minecraft.core.Registry;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;

import net.fabricmc.fabric.impl.tag.TagAliasGroup;

public final class TagAliasGenerator {
	public static String getDirectory(ResourceKey<? extends Registry<?>> registryKey) {
		String directory = "fabric/tag_aliases/";
		Identifier registryId = registryKey.identifier();

		if (!Identifier.DEFAULT_NAMESPACE.equals(registryId.getNamespace())) {
			directory += registryId.getNamespace() + '/';
		}

		return directory + registryId.getPath();
	}

	public static <T> CompletableFuture<?> writeTagAlias(CachedOutput writer, PackOutput.PathProvider pathResolver, ResourceKey<? extends Registry<T>> registryRef, Identifier groupId, List<TagKey<T>> tags) {
		Path path = pathResolver.json(groupId);
		return DataProvider.saveStable(writer, TagAliasGroup.codec(registryRef), new TagAliasGroup<>(tags), path);
	}
}
