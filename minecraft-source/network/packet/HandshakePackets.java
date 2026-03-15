/*
 * External method calls:
 *   Lnet/minecraft/util/Identifier;ofVanilla(Ljava/lang/String;)Lnet/minecraft/util/Identifier;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/network/packet/HandshakePackets;c2s(Ljava/lang/String;)Lnet/minecraft/network/packet/PacketType;
 */
package net.minecraft.network.packet;

import net.minecraft.network.NetworkSide;
import net.minecraft.network.listener.ServerHandshakePacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.PacketType;
import net.minecraft.network.packet.c2s.handshake.HandshakeC2SPacket;
import net.minecraft.util.Identifier;

public class HandshakePackets {
    public static final PacketType<HandshakeC2SPacket> INTENTION = HandshakePackets.c2s("intention");

    private static <T extends Packet<ServerHandshakePacketListener>> PacketType<T> c2s(String id) {
        return new PacketType(NetworkSide.SERVERBOUND, Identifier.ofVanilla(id));
    }
}

