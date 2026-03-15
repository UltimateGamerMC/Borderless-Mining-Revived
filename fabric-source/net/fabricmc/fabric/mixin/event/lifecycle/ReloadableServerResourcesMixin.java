package net.fabricmc.fabric.mixin.event.lifecycle;

import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.commands.Commands;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.LayeredRegistryAccess;
import net.minecraft.core.RegistryAccess;
import net.minecraft.server.RegistryLayer;
import net.minecraft.server.ReloadableServerResources;
import net.minecraft.server.permissions.PermissionSet;
import net.minecraft.world.flag.FeatureFlagSet;

import net.fabricmc.fabric.api.event.lifecycle.v1.CommonLifecycleEvents;

@Mixin(ReloadableServerResources.class)
public class ReloadableServerResourcesMixin {
	@Unique
	private RegistryAccess dynamicRegistryManager;

	@Inject(method = "<init>", at = @At("TAIL"))
	private void init(LayeredRegistryAccess<RegistryLayer> combinedDynamicRegistries, HolderLookup.Provider registries, FeatureFlagSet enabledFeatures, Commands.CommandSelection environment, List pendingTagLoads, PermissionSet permissionPredicate, CallbackInfo ci) {
		dynamicRegistryManager = combinedDynamicRegistries.compositeAccess();
	}

	@Inject(method = "updateStaticRegistryTags", at = @At("TAIL"))
	private void hookRefresh(CallbackInfo ci) {
		CommonLifecycleEvents.TAGS_LOADED.invoker().onTagsLoaded(dynamicRegistryManager, false);
	}
}
