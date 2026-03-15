/*
 * External method calls:
 *   Lnet/minecraft/util/Identifier;ofVanilla(Ljava/lang/String;)Lnet/minecraft/util/Identifier;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/network/packet/CookiePackets;s2c(Ljava/lang/String;)Lnet/minecraft/network/packet/PacketType;
 *   Lnet/minecraft/network/packet/CookiePackets;c2s(Ljava/lang/String;)Lnet/minecraft/network/packet/PacketType;
 */
package net.minecraft.network.packet;

import net.minecraft.network.NetworkSide;
import net.minecraft.network.listener.ClientCookieRequestPacketListener;
import net.minecraft.network.listener.ServerCookieResponsePacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.PacketType;
import net.minecraft.network.packet.c2s.common.CookieResponseC2SPacket;
import net.minecraft.network.packet.s2c.common.CookieRequestS2CPacket;
import net.minecraft.util.Identifier;

public class CookiePackets {
    public static final PacketType<CookieRequestS2CPacket> COOKIE_REQUEST = CookiePackets.s2c("cookie_request");
    public static final PacketType<CookieResponseC2SPacket> COOKIE_RESPONSE = CookiePackets.c2s("cookie_response");

    private static <T extends Packet<ClientCookieRequestPacketListener>> PacketType<T> s2c(String id) {
        return new PacketType(NetworkSide.CLIENTBOUND, Identifier.ofVanilla(id));
    }

    private static <T extends Packet<ServerCookieResponsePacketListener>> PacketType<T> c2s(String id) {
        return new PacketType(NetworkSide.SERVERBOUND, Identifier.ofVanilla(id));
    }
}

