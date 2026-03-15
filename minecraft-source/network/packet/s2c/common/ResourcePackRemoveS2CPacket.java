/*
 * External method calls:
 *   Lnet/minecraft/network/PacketByteBuf;readOptional(Lnet/minecraft/network/codec/PacketDecoder;)Ljava/util/Optional;
 *   Lnet/minecraft/network/PacketByteBuf;writeOptional(Ljava/util/Optional;Lnet/minecraft/network/codec/PacketEncoder;)V
 *   Lnet/minecraft/network/listener/ClientCommonPacketListener;onResourcePackRemove(Lnet/minecraft/network/packet/s2c/common/ResourcePackRemoveS2CPacket;)V
 *   Lnet/minecraft/network/packet/Packet;createCodec(Lnet/minecraft/network/codec/ValueFirstEncoder;Lnet/minecraft/network/codec/PacketDecoder;)Lnet/minecraft/network/codec/PacketCodec;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/network/packet/s2c/common/ResourcePackRemoveS2CPacket;apply(Lnet/minecraft/network/listener/ClientCommonPacketListener;)V
 */
package net.minecraft.network.packet.s2c.common;

import java.util.Optional;
import java.util.UUID;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.listener.ClientCommonPacketListener;
import net.minecraft.network.packet.CommonPackets;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.PacketType;
import net.minecraft.util.Uuids;

public record ResourcePackRemoveS2CPacket(Optional<UUID> id) implements Packet<ClientCommonPacketListener>
{
    public static final PacketCodec<PacketByteBuf, ResourcePackRemoveS2CPacket> CODEC = Packet.createCodec(ResourcePackRemoveS2CPacket::write, ResourcePackRemoveS2CPacket::new);

    private ResourcePackRemoveS2CPacket(PacketByteBuf buf) {
        this(buf.readOptional(Uuids.PACKET_CODEC));
    }

    private void write(PacketByteBuf buf) {
        buf.writeOptional(this.id, Uuids.PACKET_CODEC);
    }

    @Override
    public PacketType<ResourcePackRemoveS2CPacket> getPacketType() {
        return CommonPackets.RESOURCE_PACK_POP;
    }

    @Override
    public void apply(ClientCommonPacketListener arg) {
        arg.onResourcePackRemove(this);
    }
}

