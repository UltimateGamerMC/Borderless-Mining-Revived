package net.fabricmc.fabric.impl.resource.pack;

import java.util.ArrayList;

import net.minecraft.server.packs.CompositePackResources;
import net.minecraft.server.packs.PackLocationInfo;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.repository.Pack;

import net.fabricmc.fabric.api.resource.v1.pack.ModPackResources;

public record ModPackResourcesFactory(ModPackResources pack) implements Pack.ResourcesSupplier {
	@Override
	public PackResources openPrimary(PackLocationInfo location) {
		return this.pack;
	}

	@Override
	public PackResources openFull(PackLocationInfo location, Pack.Metadata metadata) {
		if (metadata.overlays().isEmpty()) {
			return this.pack;
		} else {
			var overlays = new ArrayList<PackResources>(metadata.overlays().size());

			for (String overlay : metadata.overlays()) {
				overlays.add(this.pack.createOverlay(overlay));
			}

			return new CompositePackResources(this.pack, overlays);
		}
	}
}
