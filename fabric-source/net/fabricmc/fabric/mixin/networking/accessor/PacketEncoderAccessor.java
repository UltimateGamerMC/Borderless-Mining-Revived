package net.fabricmc.fabric.mixin.networking.accessor;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import net.minecraft.network.PacketEncoder;
import net.minecraft.network.protocol.Packet;

@Mixin(PacketEncoder.class)
public interface PacketEncoderAccessor {
	@Invoker("encode")
	void fabric_encode(ChannelHandlerContext channelHandlerContext, Packet<?> packet, ByteBuf byteBuf) throws Exception;
}
