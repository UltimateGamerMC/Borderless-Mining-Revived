/*
 * External method calls:
 *   Lnet/minecraft/network/encryption/NetworkEncryptionUtils;encrypt(Ljava/security/Key;[B)[B
 *   Lnet/minecraft/network/PacketByteBuf;readByteArray()[B
 *   Lnet/minecraft/network/PacketByteBuf;writeByteArray([B)Lnet/minecraft/network/PacketByteBuf;
 *   Lnet/minecraft/network/listener/ServerLoginPacketListener;onKey(Lnet/minecraft/network/packet/c2s/login/LoginKeyC2SPacket;)V
 *   Lnet/minecraft/network/encryption/NetworkEncryptionUtils;decryptSecretKey(Ljava/security/PrivateKey;[B)Ljavax/crypto/SecretKey;
 *   Lnet/minecraft/network/encryption/NetworkEncryptionUtils;decrypt(Ljava/security/Key;[B)[B
 *   Lnet/minecraft/network/packet/Packet;createCodec(Lnet/minecraft/network/codec/ValueFirstEncoder;Lnet/minecraft/network/codec/PacketDecoder;)Lnet/minecraft/network/codec/PacketCodec;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/network/packet/c2s/login/LoginKeyC2SPacket;apply(Lnet/minecraft/network/listener/ServerLoginPacketListener;)V
 */
package net.minecraft.network.packet.c2s.login;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Arrays;
import javax.crypto.SecretKey;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.encryption.NetworkEncryptionException;
import net.minecraft.network.encryption.NetworkEncryptionUtils;
import net.minecraft.network.listener.ServerLoginPacketListener;
import net.minecraft.network.packet.LoginPackets;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.PacketType;

public class LoginKeyC2SPacket
implements Packet<ServerLoginPacketListener> {
    public static final PacketCodec<PacketByteBuf, LoginKeyC2SPacket> CODEC = Packet.createCodec(LoginKeyC2SPacket::write, LoginKeyC2SPacket::new);
    private final byte[] encryptedSecretKey;
    private final byte[] nonce;

    public LoginKeyC2SPacket(SecretKey secretKey, PublicKey publicKey, byte[] nonce) throws NetworkEncryptionException {
        this.encryptedSecretKey = NetworkEncryptionUtils.encrypt(publicKey, secretKey.getEncoded());
        this.nonce = NetworkEncryptionUtils.encrypt(publicKey, nonce);
    }

    private LoginKeyC2SPacket(PacketByteBuf buf) {
        this.encryptedSecretKey = buf.readByteArray();
        this.nonce = buf.readByteArray();
    }

    private void write(PacketByteBuf buf) {
        buf.writeByteArray(this.encryptedSecretKey);
        buf.writeByteArray(this.nonce);
    }

    @Override
    public PacketType<LoginKeyC2SPacket> getPacketType() {
        return LoginPackets.KEY;
    }

    @Override
    public void apply(ServerLoginPacketListener arg) {
        arg.onKey(this);
    }

    public SecretKey decryptSecretKey(PrivateKey privateKey) throws NetworkEncryptionException {
        return NetworkEncryptionUtils.decryptSecretKey(privateKey, this.encryptedSecretKey);
    }

    public boolean verifySignedNonce(byte[] nonce, PrivateKey privateKey) {
        try {
            return Arrays.equals(nonce, NetworkEncryptionUtils.decrypt(privateKey, this.nonce));
        } catch (NetworkEncryptionException lv) {
            return false;
        }
    }
}

