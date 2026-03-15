/*
 * External method calls:
 *   Lnet/minecraft/network/PacketByteBuf;readIdentifier()Lnet/minecraft/util/Identifier;
 *   Lnet/minecraft/network/codec/PacketCodec;decode(Ljava/lang/Object;)Ljava/lang/Object;
 *   Lnet/minecraft/network/PacketByteBuf;writeIdentifier(Lnet/minecraft/util/Identifier;)Lnet/minecraft/network/PacketByteBuf;
 *   Lnet/minecraft/network/codec/PacketCodec;encode(Ljava/lang/Object;Ljava/lang/Object;)V
 *   Lnet/minecraft/network/listener/ClientCommonPacketListener;onStoreCookie(Lnet/minecraft/network/packet/s2c/common/StoreCookieS2CPacket;)V
 *   Lnet/minecraft/network/packet/Packet;createCodec(Lnet/minecraft/network/codec/ValueFirstEncoder;Lnet/minecraft/network/codec/PacketDecoder;)Lnet/minecraft/network/codec/PacketCodec;
 *   Lnet/minecraft/network/codec/PacketCodecs;byteArray(I)Lnet/minecraft/network/codec/PacketCodec;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/network/packet/s2c/common/StoreCookieS2CPacket;apply(Lnet/minecraft/network/listener/ClientCommonPacketListener;)V
 */
package net.minecraft.network.packet.s2c.common;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.listener.ClientCommonPacketListener;
import net.minecraft.network.packet.CommonPackets;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.PacketType;
import net.minecraft.util.Identifier;

public record StoreCookieS2CPacket(Identifier key, byte[] payload) implements Packet<ClientCommonPacketListener>
{
    public static final PacketCodec<PacketByteBuf, StoreCookieS2CPacket> CODEC = Packet.createCodec(StoreCookieS2CPacket::write, StoreCookieS2CPacket::new);
    private static final int MAX_COOKIE_LENGTH = 5120;
    public static final PacketCodec<ByteBuf, byte[]> COOKIE_PACKET_CODEC = PacketCodecs.byteArray(5120);

    private StoreCookieS2CPacket(PacketByteBuf buf) {
        this(buf.readIdentifier(), (byte[])COOKIE_PACKET_CODEC.decode(buf));
    }

    private void write(PacketByteBuf buf) {
        buf.writeIdentifier(this.key);
        COOKIE_PACKET_CODEC.encode(buf, this.payload);
    }

    @Override
    public PacketType<StoreCookieS2CPacket> getPacketType() {
        return CommonPackets.STORE_COOKIE;
    }

    @Override
    public void apply(ClientCommonPacketListener arg) {
        arg.onStoreCookie(this);
    }
}

