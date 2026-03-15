/*
 * External method calls:
 *   Lnet/minecraft/network/handler/LegacyQueries;write(Lio/netty/buffer/ByteBuf;Ljava/lang/String;)V
 *   Lnet/minecraft/network/handler/LegacyQueries;read(Lio/netty/buffer/ByteBuf;)Ljava/lang/String;
 *   Lnet/minecraft/client/network/LegacyServerPinger$ResponseHandler;handleResponse(ILjava/lang/String;Ljava/lang/String;II)V
 *
 * Internal private/static methods:
 *   Lnet/minecraft/client/network/LegacyServerPinger;channelRead0(Lio/netty/channel/ChannelHandlerContext;Lio/netty/buffer/ByteBuf;)V
 */
package net.minecraft.client.network;

import com.google.common.base.Splitter;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.concurrent.GenericFutureListener;
import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.network.ServerAddress;
import net.minecraft.network.handler.LegacyQueries;
import net.minecraft.util.math.MathHelper;

@Environment(value=EnvType.CLIENT)
public class LegacyServerPinger
extends SimpleChannelInboundHandler<ByteBuf> {
    private static final Splitter SPLITTER = Splitter.on('\u0000').limit(6);
    private final ServerAddress serverAddress;
    private final ResponseHandler handler;

    public LegacyServerPinger(ServerAddress serverAddress, ResponseHandler handler) {
        this.serverAddress = serverAddress;
        this.handler = handler;
    }

    @Override
    public void channelActive(ChannelHandlerContext context) throws Exception {
        super.channelActive(context);
        ByteBuf byteBuf = context.alloc().buffer();
        try {
            byteBuf.writeByte(254);
            byteBuf.writeByte(1);
            byteBuf.writeByte(250);
            LegacyQueries.write(byteBuf, "MC|PingHost");
            int i = byteBuf.writerIndex();
            byteBuf.writeShort(0);
            int j = byteBuf.writerIndex();
            byteBuf.writeByte(127);
            LegacyQueries.write(byteBuf, this.serverAddress.getAddress());
            byteBuf.writeInt(this.serverAddress.getPort());
            int k = byteBuf.writerIndex() - j;
            byteBuf.setShort(i, k);
            context.channel().writeAndFlush(byteBuf).addListener((GenericFutureListener)ChannelFutureListener.CLOSE_ON_FAILURE);
        } catch (Exception exception) {
            byteBuf.release();
            throw exception;
        }
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf) {
        String string;
        List<String> list;
        short s = byteBuf.readUnsignedByte();
        if (s == 255 && "\u00a71".equals((list = SPLITTER.splitToList(string = LegacyQueries.read(byteBuf))).get(0))) {
            int i = MathHelper.parseInt(list.get(1), 0);
            String string2 = list.get(2);
            String string3 = list.get(3);
            int j = MathHelper.parseInt(list.get(4), -1);
            int k = MathHelper.parseInt(list.get(5), -1);
            this.handler.handleResponse(i, string2, string3, j, k);
        }
        channelHandlerContext.close();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext context, Throwable throwable) {
        context.close();
    }

    @Override
    protected /* synthetic */ void channelRead0(ChannelHandlerContext context, Object buf) throws Exception {
        this.channelRead0(context, (ByteBuf)buf);
    }

    @FunctionalInterface
    @Environment(value=EnvType.CLIENT)
    public static interface ResponseHandler {
        public void handleResponse(int var1, String var2, String var3, int var4, int var5);
    }
}

