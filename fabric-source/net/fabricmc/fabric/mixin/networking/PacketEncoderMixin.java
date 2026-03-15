package net.fabricmc.fabric.mixin.networking;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.network.PacketEncoder;
import net.minecraft.network.protocol.Packet;

import net.fabricmc.fabric.impl.networking.splitter.PassthroughPacket;

// Lowered the default priority, as this should happen before other mods.
@Mixin(value = PacketEncoder.class, priority = 500)
public class PacketEncoderMixin {
	@Inject(method = "encode(Lio/netty/channel/ChannelHandlerContext;Lnet/minecraft/network/protocol/Packet;Lio/netty/buffer/ByteBuf;)V", at = @At("HEAD"), cancellable = true)
	private void handlePassthroughPacket(ChannelHandlerContext channelHandlerContext, Packet<?> packet, ByteBuf byteBuf, CallbackInfo ci) {
		if (packet instanceof PassthroughPacket passthroughPacket) {
			byteBuf.writeBytes(passthroughPacket.buf());
			ci.cancel();
		}
	}
}
