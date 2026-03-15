/*
 * External method calls:
 *   Lnet/minecraft/text/Text;translatable(Ljava/lang/String;)Lnet/minecraft/text/MutableText;
 *   Lnet/minecraft/network/ClientConnection;send(Lnet/minecraft/network/packet/Packet;)V
 *   Lnet/minecraft/network/ClientConnection;disconnect(Lnet/minecraft/text/Text;)V
 *   Lnet/minecraft/network/DisconnectionInfo;reason()Lnet/minecraft/text/Text;
 *   Lnet/minecraft/network/packet/c2s/login/LoginHelloC2SPacket;name()Ljava/lang/String;
 *   Lnet/minecraft/server/PlayerManager;checkCanJoin(Ljava/net/SocketAddress;Lnet/minecraft/server/PlayerConfigEntry;)Lnet/minecraft/text/Text;
 *   Lnet/minecraft/network/PacketCallbacks;always(Ljava/lang/Runnable;)Lio/netty/channel/ChannelFutureListener;
 *   Lnet/minecraft/network/ClientConnection;send(Lnet/minecraft/network/packet/Packet;Lio/netty/channel/ChannelFutureListener;)V
 *   Lnet/minecraft/server/PlayerManager;disconnectDuplicateLogins(Ljava/util/UUID;)Z
 *   Lnet/minecraft/network/packet/c2s/login/LoginKeyC2SPacket;verifySignedNonce([BLjava/security/PrivateKey;)Z
 *   Lnet/minecraft/network/packet/c2s/login/LoginKeyC2SPacket;decryptSecretKey(Ljava/security/PrivateKey;)Ljavax/crypto/SecretKey;
 *   Lnet/minecraft/network/encryption/NetworkEncryptionUtils;cipherFromKey(ILjava/security/Key;)Ljavax/crypto/Cipher;
 *   Lnet/minecraft/network/encryption/NetworkEncryptionUtils;computeServerId(Ljava/lang/String;Ljava/security/PublicKey;Ljavax/crypto/SecretKey;)[B
 *   Lnet/minecraft/network/ClientConnection;setupEncryption(Ljavax/crypto/Cipher;Ljavax/crypto/Cipher;)V
 *   Lnet/minecraft/network/ClientConnection;transitionOutbound(Lnet/minecraft/network/state/NetworkState;)V
 *   Lnet/minecraft/server/network/ConnectedClientData;createDefault(Lcom/mojang/authlib/GameProfile;Z)Lnet/minecraft/server/network/ConnectedClientData;
 *   Lnet/minecraft/network/ClientConnection;transitionInbound(Lnet/minecraft/network/state/NetworkState;Lnet/minecraft/network/listener/PacketListener;)V
 *
 * Internal private/static methods:
 *   Lnet/minecraft/server/network/ServerLoginNetworkHandler;tickVerify(Lcom/mojang/authlib/GameProfile;)V
 *   Lnet/minecraft/server/network/ServerLoginNetworkHandler;sendSuccessPacket(Lcom/mojang/authlib/GameProfile;)V
 *   Lnet/minecraft/server/network/ServerLoginNetworkHandler;disconnect(Lnet/minecraft/text/Text;)V
 *   Lnet/minecraft/server/network/ServerLoginNetworkHandler;startVerify(Lcom/mojang/authlib/GameProfile;)V
 */
package net.minecraft.server.network;

import com.google.common.primitives.Ints;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.exceptions.AuthenticationUnavailableException;
import com.mojang.authlib.yggdrasil.ProfileResult;
import com.mojang.logging.LogUtils;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.security.PrivateKey;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.DisconnectionInfo;
import net.minecraft.network.PacketCallbacks;
import net.minecraft.network.encryption.NetworkEncryptionException;
import net.minecraft.network.encryption.NetworkEncryptionUtils;
import net.minecraft.network.listener.ServerLoginPacketListener;
import net.minecraft.network.listener.TickablePacketListener;
import net.minecraft.network.packet.c2s.common.CookieResponseC2SPacket;
import net.minecraft.network.packet.c2s.login.EnterConfigurationC2SPacket;
import net.minecraft.network.packet.c2s.login.LoginHelloC2SPacket;
import net.minecraft.network.packet.c2s.login.LoginKeyC2SPacket;
import net.minecraft.network.packet.c2s.login.LoginQueryResponseC2SPacket;
import net.minecraft.network.packet.s2c.login.LoginCompressionS2CPacket;
import net.minecraft.network.packet.s2c.login.LoginDisconnectS2CPacket;
import net.minecraft.network.packet.s2c.login.LoginHelloS2CPacket;
import net.minecraft.network.packet.s2c.login.LoginSuccessS2CPacket;
import net.minecraft.network.state.ConfigurationStates;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.PlayerConfigEntry;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.dedicated.management.ActivityNotifier;
import net.minecraft.server.network.ConnectedClientData;
import net.minecraft.server.network.ServerCommonNetworkHandler;
import net.minecraft.server.network.ServerConfigurationNetworkHandler;
import net.minecraft.text.Text;
import net.minecraft.util.StringHelper;
import net.minecraft.util.Uuids;
import net.minecraft.util.crash.CrashReport;
import net.minecraft.util.crash.CrashReportSection;
import net.minecraft.util.logging.UncaughtExceptionLogger;
import net.minecraft.util.math.random.Random;
import org.apache.commons.lang3.Validate;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;

public class ServerLoginNetworkHandler
implements ServerLoginPacketListener,
TickablePacketListener {
    private static final AtomicInteger NEXT_AUTHENTICATOR_THREAD_ID = new AtomicInteger(0);
    static final Logger LOGGER = LogUtils.getLogger();
    private static final int TIMEOUT_TICKS = 600;
    private final byte[] nonce;
    final MinecraftServer server;
    final ClientConnection connection;
    final ActivityNotifier activityNotifier;
    private volatile State state = State.HELLO;
    private int loginTicks;
    @Nullable String profileName;
    private @Nullable GameProfile profile;
    private final String serverId = "";
    private final boolean transferred;

    public ServerLoginNetworkHandler(MinecraftServer server, ClientConnection connection, boolean transferred) {
        this.server = server;
        this.connection = connection;
        this.activityNotifier = this.server.getActivityNotifier();
        this.nonce = Ints.toByteArray(Random.create().nextInt());
        this.transferred = transferred;
    }

    @Override
    public void tick() {
        if (this.state == State.VERIFYING) {
            this.tickVerify(Objects.requireNonNull(this.profile));
        }
        if (this.state == State.WAITING_FOR_DUPE_DISCONNECT && !this.hasPlayerWithId(Objects.requireNonNull(this.profile))) {
            this.sendSuccessPacket(this.profile);
        }
        if (this.loginTicks++ == 600) {
            this.disconnect(Text.translatable("multiplayer.disconnect.slow_login"));
        }
    }

    @Override
    public boolean isConnectionOpen() {
        return this.connection.isOpen();
    }

    public void disconnect(Text reason) {
        try {
            LOGGER.info("Disconnecting {}: {}", (Object)this.getConnectionInfo(), (Object)reason.getString());
            this.connection.send(new LoginDisconnectS2CPacket(reason));
            this.connection.disconnect(reason);
        } catch (Exception exception) {
            LOGGER.error("Error whilst disconnecting player", exception);
        }
    }

    private boolean hasPlayerWithId(GameProfile profile) {
        return this.server.getPlayerManager().getPlayer(profile.id()) != null;
    }

    @Override
    public void onDisconnected(DisconnectionInfo info) {
        LOGGER.info("{} lost connection: {}", (Object)this.getConnectionInfo(), (Object)info.reason().getString());
    }

    public String getConnectionInfo() {
        String string = this.connection.getAddressAsString(this.server.shouldLogIps());
        if (this.profileName != null) {
            return this.profileName + " (" + string + ")";
        }
        return string;
    }

    @Override
    public void onHello(LoginHelloC2SPacket packet) {
        Validate.validState(this.state == State.HELLO, "Unexpected hello packet", new Object[0]);
        Validate.validState(StringHelper.isValidPlayerName(packet.name()), "Invalid characters in username", new Object[0]);
        this.profileName = packet.name();
        GameProfile gameProfile = this.server.getHostProfile();
        if (gameProfile != null && this.profileName.equalsIgnoreCase(gameProfile.name())) {
            this.startVerify(gameProfile);
            return;
        }
        if (this.server.isOnlineMode() && !this.connection.isLocal()) {
            this.state = State.KEY;
            this.connection.send(new LoginHelloS2CPacket("", this.server.getKeyPair().getPublic().getEncoded(), this.nonce, true));
        } else {
            this.startVerify(Uuids.getOfflinePlayerProfile(this.profileName));
        }
    }

    void startVerify(GameProfile profile) {
        this.profile = profile;
        this.state = State.VERIFYING;
    }

    private void tickVerify(GameProfile profile) {
        PlayerManager lv = this.server.getPlayerManager();
        Text lv2 = lv.checkCanJoin(this.connection.getAddress(), new PlayerConfigEntry(profile));
        if (lv2 != null) {
            this.disconnect(lv2);
        } else {
            boolean bl;
            if (this.server.getNetworkCompressionThreshold() >= 0 && !this.connection.isLocal()) {
                this.connection.send(new LoginCompressionS2CPacket(this.server.getNetworkCompressionThreshold()), PacketCallbacks.always(() -> this.connection.setCompressionThreshold(this.server.getNetworkCompressionThreshold(), true)));
            }
            if (bl = lv.disconnectDuplicateLogins(profile.id())) {
                this.state = State.WAITING_FOR_DUPE_DISCONNECT;
            } else {
                this.sendSuccessPacket(profile);
            }
        }
    }

    private void sendSuccessPacket(GameProfile profile) {
        this.state = State.PROTOCOL_SWITCHING;
        this.connection.send(new LoginSuccessS2CPacket(profile));
    }

    @Override
    public void onKey(LoginKeyC2SPacket packet) {
        String string;
        Validate.validState(this.state == State.KEY, "Unexpected key packet", new Object[0]);
        try {
            PrivateKey privateKey = this.server.getKeyPair().getPrivate();
            if (!packet.verifySignedNonce(this.nonce, privateKey)) {
                throw new IllegalStateException("Protocol error");
            }
            SecretKey secretKey = packet.decryptSecretKey(privateKey);
            Cipher cipher = NetworkEncryptionUtils.cipherFromKey(2, secretKey);
            Cipher cipher2 = NetworkEncryptionUtils.cipherFromKey(1, secretKey);
            string = new BigInteger(NetworkEncryptionUtils.computeServerId("", this.server.getKeyPair().getPublic(), secretKey)).toString(16);
            this.state = State.AUTHENTICATING;
            this.connection.setupEncryption(cipher, cipher2);
        } catch (NetworkEncryptionException lv) {
            throw new IllegalStateException("Protocol error", lv);
        }
        Thread thread = new Thread("User Authenticator #" + NEXT_AUTHENTICATOR_THREAD_ID.incrementAndGet()){

            @Override
            public void run() {
                String string2 = Objects.requireNonNull(ServerLoginNetworkHandler.this.profileName, "Player name not initialized");
                try {
                    ProfileResult profileResult = ServerLoginNetworkHandler.this.server.getApiServices().sessionService().hasJoinedServer(string2, string, this.getClientAddress());
                    if (profileResult != null) {
                        GameProfile gameProfile = profileResult.profile();
                        LOGGER.info("UUID of player {} is {}", (Object)gameProfile.name(), (Object)gameProfile.id());
                        ServerLoginNetworkHandler.this.activityNotifier.notifyListenersWithRateLimit();
                        ServerLoginNetworkHandler.this.startVerify(gameProfile);
                    } else if (ServerLoginNetworkHandler.this.server.isSingleplayer()) {
                        LOGGER.warn("Failed to verify username but will let them in anyway!");
                        ServerLoginNetworkHandler.this.startVerify(Uuids.getOfflinePlayerProfile(string2));
                    } else {
                        ServerLoginNetworkHandler.this.disconnect(Text.translatable("multiplayer.disconnect.unverified_username"));
                        LOGGER.error("Username '{}' tried to join with an invalid session", (Object)string2);
                    }
                } catch (AuthenticationUnavailableException authenticationUnavailableException) {
                    if (ServerLoginNetworkHandler.this.server.isSingleplayer()) {
                        LOGGER.warn("Authentication servers are down but will let them in anyway!");
                        ServerLoginNetworkHandler.this.startVerify(Uuids.getOfflinePlayerProfile(string2));
                    }
                    ServerLoginNetworkHandler.this.disconnect(Text.translatable("multiplayer.disconnect.authservers_down"));
                    LOGGER.error("Couldn't verify username because servers are unavailable");
                }
            }

            private @Nullable InetAddress getClientAddress() {
                SocketAddress socketAddress = ServerLoginNetworkHandler.this.connection.getAddress();
                return ServerLoginNetworkHandler.this.server.shouldPreventProxyConnections() && socketAddress instanceof InetSocketAddress ? ((InetSocketAddress)socketAddress).getAddress() : null;
            }
        };
        thread.setUncaughtExceptionHandler(new UncaughtExceptionLogger(LOGGER));
        thread.start();
    }

    @Override
    public void onQueryResponse(LoginQueryResponseC2SPacket packet) {
        this.disconnect(ServerCommonNetworkHandler.UNEXPECTED_QUERY_RESPONSE_TEXT);
    }

    @Override
    public void onEnterConfiguration(EnterConfigurationC2SPacket packet) {
        Validate.validState(this.state == State.PROTOCOL_SWITCHING, "Unexpected login acknowledgement packet", new Object[0]);
        this.connection.transitionOutbound(ConfigurationStates.S2C);
        ConnectedClientData lv = ConnectedClientData.createDefault(Objects.requireNonNull(this.profile), this.transferred);
        ServerConfigurationNetworkHandler lv2 = new ServerConfigurationNetworkHandler(this.server, this.connection, lv);
        this.connection.transitionInbound(ConfigurationStates.C2S, lv2);
        lv2.sendConfigurations();
        this.state = State.ACCEPTED;
    }

    @Override
    public void addCustomCrashReportInfo(CrashReport report, CrashReportSection section) {
        section.add("Login phase", () -> this.state.toString());
    }

    @Override
    public void onCookieResponse(CookieResponseC2SPacket packet) {
        this.disconnect(ServerCommonNetworkHandler.UNEXPECTED_QUERY_RESPONSE_TEXT);
    }

    static enum State {
        HELLO,
        KEY,
        AUTHENTICATING,
        NEGOTIATING,
        VERIFYING,
        WAITING_FOR_DUPE_DISCONNECT,
        PROTOCOL_SWITCHING,
        ACCEPTED;

    }
}

