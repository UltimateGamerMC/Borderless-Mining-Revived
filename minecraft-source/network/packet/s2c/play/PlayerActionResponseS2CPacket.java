/*
 * External method calls:
 *   Lnet/minecraft/network/PacketByteBuf;writeVarInt(I)Lnet/minecraft/network/PacketByteBuf;
 *   Lnet/minecraft/network/listener/ClientPlayPacketListener;onPlayerActionResponse(Lnet/minecraft/network/packet/s2c/play/PlayerActionResponseS2CPacket;)V
 *   Lnet/minecraft/network/packet/Packet;createCodec(Lnet/minecraft/network/codec/ValueFirstEncoder;Lnet/minecraft/network/codec/PacketDecoder;)Lnet/minecraft/network/codec/PacketCodec;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/network/packet/s2c/play/PlayerActionResponseS2CPacket;apply(Lnet/minecraft/network/listener/ClientPlayPacketListener;)V
 */
package net.minecraft.network.packet.s2c.play;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.PacketType;
import net.minecraft.network.packet.PlayPackets;

public record PlayerActionResponseS2CPacket(int sequence) implements Packet<ClientPlayPacketListener>
{
    public static final PacketCodec<PacketByteBuf, PlayerActionResponseS2CPacket> CODEC = Packet.createCodec(PlayerActionResponseS2CPacket::write, PlayerActionResponseS2CPacket::new);

    private PlayerActionResponseS2CPacket(PacketByteBuf buf) {
        this(buf.readVarInt());
    }

    private void write(PacketByteBuf buf) {
        buf.writeVarInt(this.sequence);
    }

    @Override
    public PacketType<PlayerActionResponseS2CPacket> getPacketType() {
        return PlayPackets.BLOCK_CHANGED_ACK;
    }

    @Override
    public void apply(ClientPlayPacketListener arg) {
        arg.onPlayerActionResponse(this);
    }
}

