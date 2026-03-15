package net.fabricmc.fabric.mixin.registry.sync;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.core.registries.BuiltInRegistries;

@Mixin(BuiltInRegistries.class)
public class BuiltInRegistriesMixin {
	@Unique
	private static boolean hasInitialised = false;

	@Inject(method = "createContents", at = @At("HEAD"), cancellable = true)
	private static void init(CallbackInfo ci) {
		if (hasInitialised) {
			ci.cancel();
		}

		hasInitialised = true;
	}
}
