package net.fabricmc.fabric.impl.tag;

import net.minecraft.server.packs.PackType;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.resource.v1.ResourceLoader;

public final class TagInit implements ModInitializer {
	@Override
	public void onInitialize() {
		ResourceLoader.get(PackType.SERVER_DATA).registerReloader(TagAliasLoader.ID, new TagAliasLoader());
	}
}
