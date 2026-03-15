package net.fabricmc.fabric.mixin.resource;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.server.packs.resources.Resource;

import net.fabricmc.fabric.api.resource.v1.FabricResource;
import net.fabricmc.fabric.impl.resource.PackSourceTracker;

/**
 * Implements {@link FabricResource} (resource source getter/setter)
 * for vanilla's basic {@link Resource} used for most game resources.
 */
@Mixin(Resource.class)
class ResourceMixin implements FabricResource {
	@SuppressWarnings("ConstantConditions")
	@Override
	public PackSource getFabricPackSource() {
		Resource self = (Resource) (Object) this;
		return PackSourceTracker.getSource(self.source());
	}
}
