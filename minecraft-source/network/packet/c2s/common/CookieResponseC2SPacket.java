/*
 * External method calls:
 *   Lnet/minecraft/network/PacketByteBuf;readIdentifier()Lnet/minecraft/util/Identifier;
 *   Lnet/minecraft/network/PacketByteBuf;readNullable(Lnet/minecraft/network/codec/PacketDecoder;)Ljava/lang/Object;
 *   Lnet/minecraft/network/PacketByteBuf;writeIdentifier(Lnet/minecraft/util/Identifier;)Lnet/minecraft/network/PacketByteBuf;
 *   Lnet/minecraft/network/PacketByteBuf;writeNullable(Ljava/lang/Object;Lnet/minecraft/network/codec/PacketEncoder;)V
 *   Lnet/minecraft/network/listener/ServerCookieResponsePacketListener;onCookieResponse(Lnet/minecraft/network/packet/c2s/common/CookieResponseC2SPacket;)V
 *   Lnet/minecraft/network/packet/Packet;createCodec(Lnet/minecraft/network/codec/ValueFirstEncoder;Lnet/minecraft/network/codec/PacketDecoder;)Lnet/minecraft/network/codec/PacketCodec;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/network/packet/c2s/common/CookieResponseC2SPacket;apply(Lnet/minecraft/network/listener/ServerCookieResponsePacketListener;)V
 */
package net.minecraft.network.packet.c2s.common;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.listener.ServerCookieResponsePacketListener;
import net.minecraft.network.packet.CookiePackets;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.PacketType;
import net.minecraft.network.packet.s2c.common.StoreCookieS2CPacket;
import net.minecraft.util.Identifier;
import org.jspecify.annotations.Nullable;

public record CookieResponseC2SPacket(Identifier key, byte @Nullable [] payload) implements Packet<ServerCookieResponsePacketListener>
{
    public static final PacketCodec<PacketByteBuf, CookieResponseC2SPacket> CODEC = Packet.createCodec(CookieResponseC2SPacket::write, CookieResponseC2SPacket::new);

    private CookieResponseC2SPacket(PacketByteBuf buf) {
        this(buf.readIdentifier(), buf.readNullable(StoreCookieS2CPacket.COOKIE_PACKET_CODEC));
    }

    private void write(PacketByteBuf buf) {
        buf.writeIdentifier(this.key);
        buf.writeNullable(this.payload, StoreCookieS2CPacket.COOKIE_PACKET_CODEC);
    }

    @Override
    public PacketType<CookieResponseC2SPacket> getPacketType() {
        return CookiePackets.COOKIE_RESPONSE;
    }

    @Override
    public void apply(ServerCookieResponsePacketListener arg) {
        arg.onCookieResponse(this);
    }
}

