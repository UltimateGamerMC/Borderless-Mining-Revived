/*
 * External method calls:
 *   Lnet/minecraft/network/listener/ClientPlayPacketListener;onEnterCombat(Lnet/minecraft/network/packet/s2c/play/EnterCombatS2CPacket;)V
 *   Lnet/minecraft/network/codec/PacketCodec;unit(Ljava/lang/Object;)Lnet/minecraft/network/codec/PacketCodec;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/network/packet/s2c/play/EnterCombatS2CPacket;apply(Lnet/minecraft/network/listener/ClientPlayPacketListener;)V
 */
package net.minecraft.network.packet.s2c.play;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.PacketType;
import net.minecraft.network.packet.PlayPackets;

public class EnterCombatS2CPacket
implements Packet<ClientPlayPacketListener> {
    public static final EnterCombatS2CPacket INSTANCE = new EnterCombatS2CPacket();
    public static final PacketCodec<ByteBuf, EnterCombatS2CPacket> CODEC = PacketCodec.unit(INSTANCE);

    private EnterCombatS2CPacket() {
    }

    @Override
    public PacketType<EnterCombatS2CPacket> getPacketType() {
        return PlayPackets.PLAYER_COMBAT_ENTER;
    }

    @Override
    public void apply(ClientPlayPacketListener arg) {
        arg.onEnterCombat(this);
    }
}

