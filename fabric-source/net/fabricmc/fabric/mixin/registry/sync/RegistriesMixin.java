package net.fabricmc.fabric.mixin.registry.sync;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;

// Vanilla doesn't mark namespaces in the directories of tags and dynamic registry elements at all,
// so we prepend the directories with the namespace if it's a modded registry id.
@Mixin(Registries.class)
public class RegistriesMixin {
	@ModifyReturnValue(method = "elementsDirPath", at = @At("RETURN"))
	private static String prependDirectoryWithNamespace(String original, @Local(argsOnly = true) ResourceKey<? extends Registry<?>> registryRef) {
		Identifier id = registryRef.identifier();

		if (!id.getNamespace().equals(Identifier.DEFAULT_NAMESPACE)) {
			return id.getNamespace() + "/" + id.getPath();
		}

		return original;
	}

	@ModifyReturnValue(method = "tagsDirPath", at = @At("RETURN"))
	private static String prependTagDirectoryWithNamespace(String original, @Local(argsOnly = true) ResourceKey<? extends Registry<?>> registryRef) {
		Identifier id = registryRef.identifier();

		if (!id.getNamespace().equals(Identifier.DEFAULT_NAMESPACE)) {
			return "tags/" + id.getNamespace() + "/" + id.getPath();
		}

		return original;
	}
}
