/*
 * External method calls:
 *   Lnet/minecraft/network/state/NetworkState;codec()Lnet/minecraft/network/codec/PacketCodec;
 *   Lnet/minecraft/network/codec/PacketCodec;encode(Ljava/lang/Object;Ljava/lang/Object;)V
 *   Lnet/minecraft/network/state/NetworkState;id()Lnet/minecraft/network/NetworkPhase;
 *   Lnet/minecraft/util/profiling/jfr/FlightProfiler;onPacketSent(Lnet/minecraft/network/NetworkPhase;Lnet/minecraft/network/packet/PacketType;Ljava/net/SocketAddress;I)V
 *   Lnet/minecraft/network/handler/NetworkStateTransitionHandler;onEncoded(Lio/netty/channel/ChannelHandlerContext;Lnet/minecraft/network/packet/Packet;)V
 *
 * Internal private/static methods:
 *   Lnet/minecraft/network/handler/EncoderHandler;encode(Lio/netty/channel/ChannelHandlerContext;Lnet/minecraft/network/packet/Packet;Lio/netty/buffer/ByteBuf;)V
 */
package net.minecraft.network.handler;

import com.mojang.logging.LogUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.handler.NetworkStateTransitionHandler;
import net.minecraft.network.handler.PacketEncoderException;
import net.minecraft.network.listener.PacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.PacketType;
import net.minecraft.network.state.NetworkState;
import net.minecraft.util.profiling.jfr.FlightProfiler;
import org.slf4j.Logger;

public class EncoderHandler<T extends PacketListener>
extends MessageToByteEncoder<Packet<T>> {
    private static final Logger LOGGER = LogUtils.getLogger();
    private final NetworkState<T> state;

    public EncoderHandler(NetworkState<T> state) {
        this.state = state;
    }

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Packet<T> arg, ByteBuf byteBuf) throws Exception {
        PacketType<Packet<T>> lv = arg.getPacketType();
        try {
            this.state.codec().encode(byteBuf, arg);
            int i = byteBuf.readableBytes();
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug(ClientConnection.PACKET_SENT_MARKER, "OUT: [{}:{}] {} -> {} bytes", this.state.id().getId(), lv, arg.getClass().getName(), i);
            }
            FlightProfiler.INSTANCE.onPacketSent(this.state.id(), lv, channelHandlerContext.channel().remoteAddress(), i);
        } catch (Throwable throwable) {
            LOGGER.error("Error sending packet {}", (Object)lv, (Object)throwable);
            if (arg.isWritingErrorSkippable()) {
                throw new PacketEncoderException(throwable);
            }
            throw throwable;
        } finally {
            NetworkStateTransitionHandler.onEncoded(channelHandlerContext, arg);
        }
    }

    @Override
    protected /* synthetic */ void encode(ChannelHandlerContext context, Object packet, ByteBuf out) throws Exception {
        this.encode(context, (Packet)packet, out);
    }
}

