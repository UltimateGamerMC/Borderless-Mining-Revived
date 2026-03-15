package net.fabricmc.fabric.mixin.networking.accessor;

import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import net.minecraft.network.PacketDecoder;

@Mixin(PacketDecoder.class)
public interface PacketDecoderAccessor {
	@Invoker("decode")
	void fabric_decode(ChannelHandlerContext var1, ByteBuf var2, List<Object> var3) throws Exception;
}
