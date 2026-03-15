/*
 * External method calls:
 *   Lnet/minecraft/network/PacketByteBuf;readIdentifier()Lnet/minecraft/util/Identifier;
 *   Lnet/minecraft/network/PacketByteBuf;writeIdentifier(Lnet/minecraft/util/Identifier;)Lnet/minecraft/network/PacketByteBuf;
 *   Lnet/minecraft/network/listener/ClientCookieRequestPacketListener;onCookieRequest(Lnet/minecraft/network/packet/s2c/common/CookieRequestS2CPacket;)V
 *   Lnet/minecraft/network/packet/Packet;createCodec(Lnet/minecraft/network/codec/ValueFirstEncoder;Lnet/minecraft/network/codec/PacketDecoder;)Lnet/minecraft/network/codec/PacketCodec;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/network/packet/s2c/common/CookieRequestS2CPacket;apply(Lnet/minecraft/network/listener/ClientCookieRequestPacketListener;)V
 */
package net.minecraft.network.packet.s2c.common;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.listener.ClientCookieRequestPacketListener;
import net.minecraft.network.packet.CookiePackets;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.PacketType;
import net.minecraft.util.Identifier;

public record CookieRequestS2CPacket(Identifier key) implements Packet<ClientCookieRequestPacketListener>
{
    public static final PacketCodec<PacketByteBuf, CookieRequestS2CPacket> CODEC = Packet.createCodec(CookieRequestS2CPacket::write, CookieRequestS2CPacket::new);

    private CookieRequestS2CPacket(PacketByteBuf buf) {
        this(buf.readIdentifier());
    }

    private void write(PacketByteBuf buf) {
        buf.writeIdentifier(this.key);
    }

    @Override
    public PacketType<CookieRequestS2CPacket> getPacketType() {
        return CookiePackets.COOKIE_REQUEST;
    }

    @Override
    public void apply(ClientCookieRequestPacketListener arg) {
        arg.onCookieRequest(this);
    }
}

