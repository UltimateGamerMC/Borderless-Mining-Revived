/*
 * External method calls:
 *   Lnet/minecraft/network/NetworkingBackend;remote(Z)Lnet/minecraft/network/NetworkingBackend;
 *   Lnet/minecraft/network/NetworkingBackend;local()Lnet/minecraft/network/NetworkingBackend;
 *   Lnet/minecraft/util/crash/CrashReport;create(Ljava/lang/Throwable;Ljava/lang/String;)Lnet/minecraft/util/crash/CrashReport;
 *   Lnet/minecraft/text/Text;literal(Ljava/lang/String;)Lnet/minecraft/text/MutableText;
 *   Lnet/minecraft/network/PacketCallbacks;always(Ljava/lang/Runnable;)Lio/netty/channel/ChannelFutureListener;
 *   Lnet/minecraft/network/ClientConnection;send(Lnet/minecraft/network/packet/Packet;Lio/netty/channel/ChannelFutureListener;)V
 *   Lnet/minecraft/network/ClientConnection;disconnect(Lnet/minecraft/text/Text;)V
 */
package net.minecraft.server;

import com.google.common.collect.Lists;
import com.mojang.logging.LogUtils;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelException;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.local.LocalAddress;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.util.HashedWheelTimer;
import io.netty.util.Timeout;
import io.netty.util.Timer;
import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketAddress;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import net.minecraft.SharedConstants;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.NetworkSide;
import net.minecraft.network.NetworkingBackend;
import net.minecraft.network.PacketCallbacks;
import net.minecraft.network.RateLimitedConnection;
import net.minecraft.network.handler.LegacyQueryHandler;
import net.minecraft.network.packet.s2c.common.DisconnectS2CPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.LocalServerHandshakeNetworkHandler;
import net.minecraft.server.network.ServerHandshakeNetworkHandler;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.crash.CrashException;
import net.minecraft.util.crash.CrashReport;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;

public class ServerNetworkIo {
    private static final Logger LOGGER = LogUtils.getLogger();
    final MinecraftServer server;
    public volatile boolean active;
    private final List<ChannelFuture> channels = Collections.synchronizedList(Lists.newArrayList());
    final List<ClientConnection> connections = Collections.synchronizedList(Lists.newArrayList());

    public ServerNetworkIo(MinecraftServer server) {
        this.server = server;
        this.active = true;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void bind(@Nullable InetAddress address, int port) throws IOException {
        List<ChannelFuture> list = this.channels;
        synchronized (list) {
            NetworkingBackend lv = NetworkingBackend.remote(this.server.isUsingNativeTransport());
            this.channels.add(((ServerBootstrap)((ServerBootstrap)new ServerBootstrap().channel(lv.getServerChannelClass())).childHandler(new ChannelInitializer<Channel>(){

                @Override
                protected void initChannel(Channel channel) {
                    try {
                        channel.config().setOption(ChannelOption.TCP_NODELAY, true);
                    } catch (ChannelException channelException) {
                        // empty catch block
                    }
                    ChannelPipeline channelPipeline = channel.pipeline().addLast("timeout", (ChannelHandler)new ReadTimeoutHandler(30));
                    if (ServerNetworkIo.this.server.acceptsStatusQuery()) {
                        channelPipeline.addLast("legacy_query", (ChannelHandler)new LegacyQueryHandler(ServerNetworkIo.this.getServer()));
                    }
                    ClientConnection.addHandlers(channelPipeline, NetworkSide.SERVERBOUND, false, null);
                    int i = ServerNetworkIo.this.server.getRateLimit();
                    ClientConnection lv = i > 0 ? new RateLimitedConnection(i) : new ClientConnection(NetworkSide.SERVERBOUND);
                    ServerNetworkIo.this.connections.add(lv);
                    lv.addFlowControlHandler(channelPipeline);
                    lv.setInitialPacketListener(new ServerHandshakeNetworkHandler(ServerNetworkIo.this.server, lv));
                }
            }).group(lv.getEventLoopGroup()).localAddress(address, port)).bind().syncUninterruptibly());
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public SocketAddress bindLocal() {
        ChannelFuture channelFuture;
        List<ChannelFuture> list = this.channels;
        synchronized (list) {
            channelFuture = ((ServerBootstrap)((ServerBootstrap)new ServerBootstrap().channel(NetworkingBackend.local().getServerChannelClass())).childHandler(new ChannelInitializer<Channel>(){

                @Override
                protected void initChannel(Channel channel) {
                    ClientConnection lv = new ClientConnection(NetworkSide.SERVERBOUND);
                    lv.setInitialPacketListener(new LocalServerHandshakeNetworkHandler(ServerNetworkIo.this.server, lv));
                    ServerNetworkIo.this.connections.add(lv);
                    ChannelPipeline channelPipeline = channel.pipeline();
                    ClientConnection.addLocalValidator(channelPipeline, NetworkSide.SERVERBOUND);
                    if (SharedConstants.FAKE_LATENCY_MS > 0) {
                        channelPipeline.addLast("latency", (ChannelHandler)new DelayingChannelInboundHandler(SharedConstants.FAKE_LATENCY_MS, SharedConstants.FAKE_JITTER_MS));
                    }
                    lv.addFlowControlHandler(channelPipeline);
                }
            }).group(NetworkingBackend.local().getEventLoopGroup()).localAddress(LocalAddress.ANY)).bind().syncUninterruptibly();
            this.channels.add(channelFuture);
        }
        return channelFuture.channel().localAddress();
    }

    public void stop() {
        this.active = false;
        for (ChannelFuture channelFuture : this.channels) {
            try {
                channelFuture.channel().close().sync();
            } catch (InterruptedException interruptedException) {
                LOGGER.error("Interrupted whilst closing channel");
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void tick() {
        List<ClientConnection> list = this.connections;
        synchronized (list) {
            Iterator<ClientConnection> iterator = this.connections.iterator();
            while (iterator.hasNext()) {
                ClientConnection lv = iterator.next();
                if (lv.isChannelAbsent()) continue;
                if (lv.isOpen()) {
                    try {
                        lv.tick();
                    } catch (Exception exception) {
                        if (lv.isLocal()) {
                            throw new CrashException(CrashReport.create(exception, "Ticking memory connection"));
                        }
                        LOGGER.warn("Failed to handle packet for {}", (Object)lv.getAddressAsString(this.server.shouldLogIps()), (Object)exception);
                        MutableText lv2 = Text.literal("Internal server error");
                        lv.send(new DisconnectS2CPacket(lv2), PacketCallbacks.always(() -> lv.disconnect(lv2)));
                        lv.tryDisableAutoRead();
                    }
                    continue;
                }
                iterator.remove();
                lv.handleDisconnection();
            }
        }
    }

    public MinecraftServer getServer() {
        return this.server;
    }

    public List<ClientConnection> getConnections() {
        return this.connections;
    }

    static class DelayingChannelInboundHandler
    extends ChannelInboundHandlerAdapter {
        private static final Timer TIMER = new HashedWheelTimer();
        private final int baseDelay;
        private final int extraDelay;
        private final List<Packet> packets = Lists.newArrayList();

        public DelayingChannelInboundHandler(int baseDelay, int extraDelay) {
            this.baseDelay = baseDelay;
            this.extraDelay = extraDelay;
        }

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) {
            this.delay(ctx, msg);
        }

        private void delay(ChannelHandlerContext ctx, Object msg) {
            int i = this.baseDelay + (int)(Math.random() * (double)this.extraDelay);
            this.packets.add(new Packet(ctx, msg));
            TIMER.newTimeout(this::forward, i, TimeUnit.MILLISECONDS);
        }

        private void forward(Timeout timeout) {
            Packet lv = this.packets.remove(0);
            lv.context.fireChannelRead(lv.message);
        }

        static class Packet {
            public final ChannelHandlerContext context;
            public final Object message;

            public Packet(ChannelHandlerContext context, Object message) {
                this.context = context;
                this.message = message;
            }
        }
    }
}

