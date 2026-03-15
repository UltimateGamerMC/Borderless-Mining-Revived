package net.fabricmc.fabric.mixin.gametest;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.gametest.framework.GameTestServer;

@Mixin(GameTestServer.class)
public abstract class GameTestServerMixin {
	@Inject(method = "isDedicatedServer", at = @At("HEAD"), cancellable = true)
	public void isDedicated(CallbackInfoReturnable<Boolean> cir) {
		// Allow dedicated server commands to be registered.
		// Should aid with mods that use this to detect if they are running on a dedicated server as well.
		cir.setReturnValue(true);
	}
}
