/*
 * External method calls:
 *   Lnet/minecraft/network/PacketByteBuf;readString(I)Ljava/lang/String;
 *   Lnet/minecraft/network/PacketByteBuf;readByteArray()[B
 *   Lnet/minecraft/network/PacketByteBuf;writeString(Ljava/lang/String;)Lnet/minecraft/network/PacketByteBuf;
 *   Lnet/minecraft/network/PacketByteBuf;writeByteArray([B)Lnet/minecraft/network/PacketByteBuf;
 *   Lnet/minecraft/network/PacketByteBuf;writeBoolean(Z)Lnet/minecraft/network/PacketByteBuf;
 *   Lnet/minecraft/network/listener/ClientLoginPacketListener;onHello(Lnet/minecraft/network/packet/s2c/login/LoginHelloS2CPacket;)V
 *   Lnet/minecraft/network/encryption/NetworkEncryptionUtils;decodeEncodedRsaPublicKey([B)Ljava/security/PublicKey;
 *   Lnet/minecraft/network/packet/Packet;createCodec(Lnet/minecraft/network/codec/ValueFirstEncoder;Lnet/minecraft/network/codec/PacketDecoder;)Lnet/minecraft/network/codec/PacketCodec;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/network/packet/s2c/login/LoginHelloS2CPacket;apply(Lnet/minecraft/network/listener/ClientLoginPacketListener;)V
 */
package net.minecraft.network.packet.s2c.login;

import java.security.PublicKey;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.encryption.NetworkEncryptionException;
import net.minecraft.network.encryption.NetworkEncryptionUtils;
import net.minecraft.network.listener.ClientLoginPacketListener;
import net.minecraft.network.packet.LoginPackets;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.PacketType;

public class LoginHelloS2CPacket
implements Packet<ClientLoginPacketListener> {
    public static final PacketCodec<PacketByteBuf, LoginHelloS2CPacket> CODEC = Packet.createCodec(LoginHelloS2CPacket::write, LoginHelloS2CPacket::new);
    private final String serverId;
    private final byte[] publicKey;
    private final byte[] nonce;
    private final boolean needsAuthentication;

    public LoginHelloS2CPacket(String serverId, byte[] publicKey, byte[] nonce, boolean needsAuthentication) {
        this.serverId = serverId;
        this.publicKey = publicKey;
        this.nonce = nonce;
        this.needsAuthentication = needsAuthentication;
    }

    private LoginHelloS2CPacket(PacketByteBuf buf) {
        this.serverId = buf.readString(20);
        this.publicKey = buf.readByteArray();
        this.nonce = buf.readByteArray();
        this.needsAuthentication = buf.readBoolean();
    }

    private void write(PacketByteBuf buf) {
        buf.writeString(this.serverId);
        buf.writeByteArray(this.publicKey);
        buf.writeByteArray(this.nonce);
        buf.writeBoolean(this.needsAuthentication);
    }

    @Override
    public PacketType<LoginHelloS2CPacket> getPacketType() {
        return LoginPackets.HELLO_S2C;
    }

    @Override
    public void apply(ClientLoginPacketListener arg) {
        arg.onHello(this);
    }

    public String getServerId() {
        return this.serverId;
    }

    public PublicKey getPublicKey() throws NetworkEncryptionException {
        return NetworkEncryptionUtils.decodeEncodedRsaPublicKey(this.publicKey);
    }

    public byte[] getNonce() {
        return this.nonce;
    }

    public boolean needsAuthentication() {
        return this.needsAuthentication;
    }
}

