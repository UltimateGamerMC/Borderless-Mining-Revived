/*
 * External method calls:
 *   Lnet/minecraft/text/Text;translatable(Ljava/lang/String;)Lnet/minecraft/text/MutableText;
 *   Lnet/minecraft/client/util/NarratorManager;narrateSystemImmediately(Lnet/minecraft/text/Text;)V
 *   Lnet/minecraft/network/ClientConnection;disconnect(Lnet/minecraft/text/Text;)V
 */
package net.minecraft.client.realms;

import com.mojang.logging.LogUtils;
import java.net.InetSocketAddress;
import java.util.Objects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.QuickPlayLogger;
import net.minecraft.client.gui.screen.DisconnectedScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.network.ClientLoginNetworkHandler;
import net.minecraft.client.network.ServerAddress;
import net.minecraft.client.realms.dto.RealmsServer;
import net.minecraft.client.resource.server.ServerResourcePackManager;
import net.minecraft.client.session.report.ReporterEnvironment;
import net.minecraft.client.world.ClientChunkLoadProgress;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.NetworkingBackend;
import net.minecraft.network.packet.c2s.login.LoginHelloC2SPacket;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;

@Environment(value=EnvType.CLIENT)
public class RealmsConnection {
    static final Logger LOGGER = LogUtils.getLogger();
    final Screen onlineScreen;
    volatile boolean aborted;
    @Nullable ClientConnection connection;

    public RealmsConnection(Screen onlineScreen) {
        this.onlineScreen = onlineScreen;
    }

    public void connect(final RealmsServer server, ServerAddress address) {
        final MinecraftClient lv = MinecraftClient.getInstance();
        lv.loadBlockList();
        lv.getNarratorManager().narrateSystemImmediately(Text.translatable("mco.connect.success"));
        final String string = address.getAddress();
        final int i = address.getPort();
        new Thread("Realms-connect-task"){

            @Override
            public void run() {
                InetSocketAddress inetSocketAddress = null;
                try {
                    inetSocketAddress = new InetSocketAddress(string, i);
                    if (RealmsConnection.this.aborted) {
                        return;
                    }
                    RealmsConnection.this.connection = ClientConnection.connect(inetSocketAddress, NetworkingBackend.remote(lv.options.shouldUseNativeTransport()), lv.getDebugHud().getPacketSizeLog());
                    if (RealmsConnection.this.aborted) {
                        return;
                    }
                    ClientLoginNetworkHandler lv3 = new ClientLoginNetworkHandler(RealmsConnection.this.connection, lv, server.createServerInfo(string), RealmsConnection.this.onlineScreen, false, null, status -> {}, new ClientChunkLoadProgress(), null);
                    if (server.isMinigame()) {
                        lv3.setMinigameName(server.minigameName);
                    }
                    if (RealmsConnection.this.aborted) {
                        return;
                    }
                    RealmsConnection.this.connection.connect(string, i, lv3);
                    if (RealmsConnection.this.aborted) {
                        return;
                    }
                    RealmsConnection.this.connection.send(new LoginHelloC2SPacket(lv.getSession().getUsername(), lv.getSession().getUuidOrNull()));
                    lv.ensureAbuseReportContext(ReporterEnvironment.ofRealm(server));
                    lv.getQuickPlayLogger().setWorld(QuickPlayLogger.WorldType.REALMS, String.valueOf(server.id), Objects.requireNonNullElse(server.name, "unknown"));
                    lv.getServerResourcePackProvider().init(RealmsConnection.this.connection, ServerResourcePackManager.AcceptanceStatus.ALLOWED);
                } catch (Exception exception) {
                    lv.getServerResourcePackProvider().clear();
                    if (RealmsConnection.this.aborted) {
                        return;
                    }
                    LOGGER.error("Couldn't connect to world", exception);
                    String string3 = exception.toString();
                    if (inetSocketAddress != null) {
                        String string2 = String.valueOf(inetSocketAddress) + ":" + i;
                        string3 = string3.replaceAll(string2, "");
                    }
                    DisconnectedScreen lv2 = new DisconnectedScreen(RealmsConnection.this.onlineScreen, (Text)Text.translatable("mco.connect.failed"), Text.translatable("disconnect.genericReason", string3), ScreenTexts.BACK);
                    lv.execute(() -> lv.setScreen(lv2));
                }
            }
        }.start();
    }

    public void abort() {
        this.aborted = true;
        if (this.connection != null && this.connection.isOpen()) {
            this.connection.disconnect(Text.translatable("disconnect.genericReason"));
            this.connection.handleDisconnection();
        }
    }

    public void tick() {
        if (this.connection != null) {
            if (this.connection.isOpen()) {
                this.connection.tick();
            } else {
                this.connection.handleDisconnection();
            }
        }
    }
}

