package net.fabricmc.fabric.mixin.object.builder;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.EntityDataSerializers;

import net.fabricmc.fabric.impl.object.builder.FabricTrackedDataRegistryImpl;
import net.fabricmc.loader.api.FabricLoader;

@Mixin(EntityDataSerializers.class)
abstract class EntityDataSerializersMixin {
	@Inject(method = "<clinit>", at = @At("TAIL"))
	private static void storeVanillaHandlers(CallbackInfo ci) {
		FabricTrackedDataRegistryImpl.storeVanillaHandlers();
	}

	@Inject(method = "registerSerializer(Lnet/minecraft/network/syncher/EntityDataSerializer;)V", at = @At("HEAD"))
	private static void onHeadRegister(EntityDataSerializer<?> handler, CallbackInfo ci) {
		if (FabricTrackedDataRegistryImpl.hasStoredVanillaHandlers() && FabricLoader.getInstance().isDevelopmentEnvironment()) {
			throw new IllegalStateException("Tried to register tracked data handler " + handler + " using TrackedDataHandlerRegistry.register. This is not allowed as it can lead to desynchronization issues; use FabricTrackedDataRegistry.register instead.");
		}
	}
}
