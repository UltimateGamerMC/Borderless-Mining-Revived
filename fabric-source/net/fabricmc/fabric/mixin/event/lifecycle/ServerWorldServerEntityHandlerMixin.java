package net.fabricmc.fabric.mixin.event.lifecycle;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;

@Mixin(targets = "net.minecraft.server.level.ServerLevel$EntityCallbacks")
abstract class ServerWorldServerEntityHandlerMixin {
	// final synthetic Lnet/minecraft/server/level/ServerLevel; field_26936
	@Shadow
	@Final
	ServerLevel field_26936;

	@Inject(method = "onTrackingStart(Lnet/minecraft/world/entity/Entity;)V", at = @At("TAIL"))
	private void invokeEntityLoadEvent(Entity entity, CallbackInfo ci) {
		ServerEntityEvents.ENTITY_LOAD.invoker().onLoad(entity, this.field_26936);
	}

	@Inject(method = "onTrackingEnd(Lnet/minecraft/world/entity/Entity;)V", at = @At("HEAD"))
	private void invokeEntityUnloadEvent(Entity entity, CallbackInfo info) {
		ServerEntityEvents.ENTITY_UNLOAD.invoker().onUnload(entity, this.field_26936);
	}
}
