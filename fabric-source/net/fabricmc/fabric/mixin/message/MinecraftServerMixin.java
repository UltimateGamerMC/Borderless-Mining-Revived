package net.fabricmc.fabric.mixin.message;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.network.chat.ChatDecorator;
import net.minecraft.server.MinecraftServer;

import net.fabricmc.fabric.api.message.v1.ServerMessageDecoratorEvent;

@Mixin(MinecraftServer.class)
public class MinecraftServerMixin {
	@Inject(method = "getChatDecorator", at = @At("RETURN"), cancellable = true)
	private void onGetChatDecorator(CallbackInfoReturnable<ChatDecorator> cir) {
		cir.setReturnValue((sender, message) -> ServerMessageDecoratorEvent.EVENT.invoker().decorate(sender, message));
	}
}
