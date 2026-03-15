package net.fabricmc.fabric.mixin.registry.sync;

import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.Main;
import net.minecraft.world.item.CreativeModeTabs;

import net.fabricmc.api.EnvType;
import net.fabricmc.fabric.impl.registry.sync.trackers.vanilla.BlockInitTracker;
import net.fabricmc.loader.api.FabricLoader;

@Mixin(Main.class)
public class MainMixin {
	@Shadow
	@Final
	private static Logger LOGGER;

	@Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/util/Util;startTimerHackThread()V"), method = "main")
	private static void afterModInit(CallbackInfo info) {
		if (FabricLoader.getInstance().getEnvironmentType() == EnvType.SERVER) {
			// Freeze the registries on the server
			LOGGER.debug("Freezing registries");

			BuiltInRegistries.bootStrap();
			BlockInitTracker.postFreeze();
			CreativeModeTabs.validate();
		}
	}
}
