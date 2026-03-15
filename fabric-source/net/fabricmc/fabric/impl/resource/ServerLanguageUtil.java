package net.fabricmc.fabric.impl.resource;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import net.minecraft.locale.Language;
import net.minecraft.server.packs.PackType;

import net.fabricmc.fabric.impl.resource.pack.ModNioPackResources;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;

public final class ServerLanguageUtil {
	private static final String ASSETS_PREFIX = PackType.CLIENT_RESOURCES.getDirectory() + '/';

	private ServerLanguageUtil() {
	}

	public static Collection<Path> getModLanguageFiles() {
		Set<Path> paths = new LinkedHashSet<>();

		for (ModContainer mod : FabricLoader.getInstance().getAllMods()) {
			if (mod.getMetadata().getType().equals("builtin")) continue;

			final Map<PackType, Set<String>> map = ModNioPackResources.readNamespaces(mod.getRootPaths(), mod.getMetadata().getId());

			for (String ns : map.get(PackType.CLIENT_RESOURCES)) {
				mod.findPath(ASSETS_PREFIX + ns + "/lang/" + Language.DEFAULT + ".json")
						.filter(Files::isRegularFile)
						.ifPresent(paths::add);
			}
		}

		return Collections.unmodifiableCollection(paths);
	}
}
