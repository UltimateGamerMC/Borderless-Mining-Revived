/*
 * External method calls:
 *   Lnet/minecraft/network/PacketByteBuf;readString(I)Ljava/lang/String;
 *   Lnet/minecraft/network/PacketByteBuf;readUuid()Ljava/util/UUID;
 *   Lnet/minecraft/network/PacketByteBuf;writeString(Ljava/lang/String;I)Lnet/minecraft/network/PacketByteBuf;
 *   Lnet/minecraft/network/PacketByteBuf;writeUuid(Ljava/util/UUID;)Lnet/minecraft/network/PacketByteBuf;
 *   Lnet/minecraft/network/listener/ServerLoginPacketListener;onHello(Lnet/minecraft/network/packet/c2s/login/LoginHelloC2SPacket;)V
 *   Lnet/minecraft/network/packet/Packet;createCodec(Lnet/minecraft/network/codec/ValueFirstEncoder;Lnet/minecraft/network/codec/PacketDecoder;)Lnet/minecraft/network/codec/PacketCodec;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/network/packet/c2s/login/LoginHelloC2SPacket;apply(Lnet/minecraft/network/listener/ServerLoginPacketListener;)V
 */
package net.minecraft.network.packet.c2s.login;

import java.util.UUID;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.listener.ServerLoginPacketListener;
import net.minecraft.network.packet.LoginPackets;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.PacketType;

public record LoginHelloC2SPacket(String name, UUID profileId) implements Packet<ServerLoginPacketListener>
{
    public static final PacketCodec<PacketByteBuf, LoginHelloC2SPacket> CODEC = Packet.createCodec(LoginHelloC2SPacket::write, LoginHelloC2SPacket::new);

    private LoginHelloC2SPacket(PacketByteBuf buf) {
        this(buf.readString(16), buf.readUuid());
    }

    private void write(PacketByteBuf buf) {
        buf.writeString(this.name, 16);
        buf.writeUuid(this.profileId);
    }

    @Override
    public PacketType<LoginHelloC2SPacket> getPacketType() {
        return LoginPackets.HELLO_C2S;
    }

    @Override
    public void apply(ServerLoginPacketListener arg) {
        arg.onHello(this);
    }
}

