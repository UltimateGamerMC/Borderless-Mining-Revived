/*
 * External method calls:
 *   Lnet/minecraft/network/PacketByteBuf;readList(Lnet/minecraft/network/codec/PacketDecoder;)Ljava/util/List;
 *   Lnet/minecraft/network/PacketByteBuf;writeCollection(Ljava/util/Collection;Lnet/minecraft/network/codec/PacketEncoder;)V
 *   Lnet/minecraft/network/listener/ClientPlayPacketListener;onPlayerRemove(Lnet/minecraft/network/packet/s2c/play/PlayerRemoveS2CPacket;)V
 *   Lnet/minecraft/network/packet/Packet;createCodec(Lnet/minecraft/network/codec/ValueFirstEncoder;Lnet/minecraft/network/codec/PacketDecoder;)Lnet/minecraft/network/codec/PacketCodec;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/network/packet/s2c/play/PlayerRemoveS2CPacket;apply(Lnet/minecraft/network/listener/ClientPlayPacketListener;)V
 */
package net.minecraft.network.packet.s2c.play;

import java.util.List;
import java.util.UUID;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.PacketType;
import net.minecraft.network.packet.PlayPackets;
import net.minecraft.util.Uuids;

public record PlayerRemoveS2CPacket(List<UUID> profileIds) implements Packet<ClientPlayPacketListener>
{
    public static final PacketCodec<PacketByteBuf, PlayerRemoveS2CPacket> CODEC = Packet.createCodec(PlayerRemoveS2CPacket::write, PlayerRemoveS2CPacket::new);

    private PlayerRemoveS2CPacket(PacketByteBuf buf) {
        this(buf.readList(Uuids.PACKET_CODEC));
    }

    private void write(PacketByteBuf buf) {
        buf.writeCollection(this.profileIds, Uuids.PACKET_CODEC);
    }

    @Override
    public PacketType<PlayerRemoveS2CPacket> getPacketType() {
        return PlayPackets.PLAYER_INFO_REMOVE;
    }

    @Override
    public void apply(ClientPlayPacketListener arg) {
        arg.onPlayerRemove(this);
    }
}

