package net.fabricmc.fabric.impl.itemgroup;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;

public final class FabricItemGroupBuilderImpl extends CreativeModeTab.Builder {
	private boolean hasDisplayName = false;

	public FabricItemGroupBuilderImpl() {
		// Set when building.
		super(null, -1);
	}

	@Override
	public CreativeModeTab.Builder title(Component displayName) {
		hasDisplayName = true;
		return super.title(displayName);
	}

	@Override
	public CreativeModeTab build() {
		if (!hasDisplayName) {
			throw new IllegalStateException("No display name set for ItemGroup");
		}

		return super.build();
	}
}
