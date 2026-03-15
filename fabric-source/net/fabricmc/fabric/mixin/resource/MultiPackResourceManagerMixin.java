package net.fabricmc.fabric.mixin.resource;

import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.MultiPackResourceManager;

import net.fabricmc.fabric.impl.resource.FabricLifecycledResourceManager;

@Mixin(MultiPackResourceManager.class)
public class MultiPackResourceManagerMixin implements FabricLifecycledResourceManager {
	@Unique
	private PackType resourceType;

	@Inject(method = "<init>", at = @At("TAIL"))
	private void init(PackType resourceType, List<PackResources> list, CallbackInfo ci) {
		this.resourceType = resourceType;
	}

	@Override
	public PackType fabric$getResourceType() {
		return this.resourceType;
	}
}
