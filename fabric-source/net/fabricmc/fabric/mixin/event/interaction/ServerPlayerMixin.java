package net.fabricmc.fabric.mixin.event.interaction;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.GameType;

import net.fabricmc.fabric.api.entity.FakePlayer;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;

@Mixin(ServerPlayer.class)
public class ServerPlayerMixin {
	@Inject(method = "attack", at = @At("HEAD"), cancellable = true)
	public void onPlayerInteractEntity(Entity target, CallbackInfo info) {
		ServerPlayer player = (ServerPlayer) (Object) this;
		InteractionResult result = AttackEntityCallback.EVENT.invoker().interact(player, player.level(), InteractionHand.MAIN_HAND, target, null);

		if (result != InteractionResult.PASS) {
			info.cancel();
		}
	}

	@Inject(method = "calculateGameModeForNewPlayer", at = @At("HEAD"), cancellable = true)
	public void fakePlayerGameMode(GameType backupGameMode, CallbackInfoReturnable<GameType> cir) {
		// Set the default game mode of the fake player to survival, regardless of the servers forced game mode.
		if ((Object) this instanceof FakePlayer) {
			cir.setReturnValue(GameType.SURVIVAL);
		}
	}
}
