/*
 * External method calls:
 *   Lnet/minecraft/network/handler/PacketBundleHandler;createBundler(Lnet/minecraft/network/packet/Packet;)Lnet/minecraft/network/handler/PacketBundleHandler$Bundler;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/network/handler/PacketBundler;ensureNotTransitioning(Lnet/minecraft/network/packet/Packet;)V
 *   Lnet/minecraft/network/handler/PacketBundler;decode(Lio/netty/channel/ChannelHandlerContext;Lnet/minecraft/network/packet/Packet;Ljava/util/List;)V
 */
package net.minecraft.network.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.DecoderException;
import io.netty.handler.codec.MessageToMessageDecoder;
import java.util.List;
import net.minecraft.network.handler.PacketBundleHandler;
import net.minecraft.network.packet.Packet;
import org.jspecify.annotations.Nullable;

public class PacketBundler
extends MessageToMessageDecoder<Packet<?>> {
    private final PacketBundleHandler handler;
    private @Nullable PacketBundleHandler.Bundler currentBundler;

    public PacketBundler(PacketBundleHandler handler) {
        this.handler = handler;
    }

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, Packet<?> arg, List<Object> list) throws Exception {
        if (this.currentBundler != null) {
            PacketBundler.ensureNotTransitioning(arg);
            Packet<?> lv = this.currentBundler.add(arg);
            if (lv != null) {
                this.currentBundler = null;
                list.add(lv);
            }
        } else {
            PacketBundleHandler.Bundler lv2 = this.handler.createBundler(arg);
            if (lv2 != null) {
                PacketBundler.ensureNotTransitioning(arg);
                this.currentBundler = lv2;
            } else {
                list.add(arg);
                if (arg.transitionsNetworkState()) {
                    channelHandlerContext.pipeline().remove(channelHandlerContext.name());
                }
            }
        }
    }

    private static void ensureNotTransitioning(Packet<?> packet) {
        if (packet.transitionsNetworkState()) {
            throw new DecoderException("Terminal message received in bundle");
        }
    }

    @Override
    protected /* synthetic */ void decode(ChannelHandlerContext context, Object packet, List packets) throws Exception {
        this.decode(context, (Packet)packet, (List<Object>)packets);
    }
}

