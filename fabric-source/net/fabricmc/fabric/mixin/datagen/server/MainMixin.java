package net.fabricmc.fabric.mixin.datagen.server;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.server.Main;

import net.fabricmc.fabric.impl.datagen.FabricDataGenHelper;

@Mixin(Main.class)
public class MainMixin {
	@Inject(method = "main", at = @At(value = "NEW", target = "net/minecraft/server/dedicated/DedicatedServerSettings"), cancellable = true)
	private static void main(String[] args, CallbackInfo info) {
		if (FabricDataGenHelper.ENABLED) {
			FabricDataGenHelper.run();
			info.cancel();
		}
	}
}
