package net.fabricmc.fabric.mixin.event.interaction;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.server.PlayerAdvancements;
import net.minecraft.server.level.ServerPlayer;

import net.fabricmc.fabric.api.entity.FakePlayer;

@Mixin(PlayerAdvancements.class)
public class PlayerAdvancementsMixin {
	@Shadow
	private ServerPlayer player;

	@Inject(method = "setPlayer", at = @At("HEAD"), cancellable = true)
	void preventOwnerOverride(ServerPlayer newOwner, CallbackInfo ci) {
		if (newOwner instanceof FakePlayer) {
			// Prevent fake players with the same UUID as a real player from stealing the real player's advancement tracker.
			ci.cancel();
		}
	}

	@Inject(method = "award", at = @At("HEAD"), cancellable = true)
	void preventGrantCriterion(AdvancementHolder advancement, String criterionName, CallbackInfoReturnable<Boolean> ci) {
		if (player instanceof FakePlayer) {
			// Prevent granting advancements to fake players.
			ci.setReturnValue(false);
		}
	}
}
