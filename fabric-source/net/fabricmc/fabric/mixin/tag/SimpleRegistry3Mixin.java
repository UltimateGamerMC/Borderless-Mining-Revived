package net.fabricmc.fabric.mixin.tag;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.core.MappedRegistry;

import net.fabricmc.fabric.impl.tag.SimpleRegistryExtension;

/**
 * This is a mixin to the Registry.PendingTagLoad implementation in SimpleRegistry.
 * It applies pending tag aliases to static registries when data packs are loaded
 * and to dynamic registries when data packs are reloaded using the {@code /reload} command.
 * (Tags run on their own data loading system separate from resource reloaders, so we need to inject them
 * once the tag and resource reloads are done, which is here.)
 */
@Mixin(targets = "net.minecraft.core.MappedRegistry$3")
abstract class SimpleRegistry3Mixin {
	@Shadow
	@Final
	MappedRegistry<?> field_53689;

	@Inject(method = "apply", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/MappedRegistry;refreshTagsInHolders()V"))
	private void applyTagAliases(CallbackInfo info) {
		((SimpleRegistryExtension) this.field_53689).fabric_applyPendingTagAliases();
	}
}
