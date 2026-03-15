/*
 * External method calls:
 *   Lnet/minecraft/network/packet/c2s/common/SyncedClientOptions;write(Lnet/minecraft/network/PacketByteBuf;)V
 *   Lnet/minecraft/network/listener/ServerCommonPacketListener;onClientOptions(Lnet/minecraft/network/packet/c2s/common/ClientOptionsC2SPacket;)V
 *   Lnet/minecraft/network/packet/Packet;createCodec(Lnet/minecraft/network/codec/ValueFirstEncoder;Lnet/minecraft/network/codec/PacketDecoder;)Lnet/minecraft/network/codec/PacketCodec;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/network/packet/c2s/common/ClientOptionsC2SPacket;apply(Lnet/minecraft/network/listener/ServerCommonPacketListener;)V
 */
package net.minecraft.network.packet.c2s.common;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.listener.ServerCommonPacketListener;
import net.minecraft.network.packet.CommonPackets;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.PacketType;
import net.minecraft.network.packet.c2s.common.SyncedClientOptions;

public record ClientOptionsC2SPacket(SyncedClientOptions options) implements Packet<ServerCommonPacketListener>
{
    public static final PacketCodec<PacketByteBuf, ClientOptionsC2SPacket> CODEC = Packet.createCodec(ClientOptionsC2SPacket::write, ClientOptionsC2SPacket::new);

    private ClientOptionsC2SPacket(PacketByteBuf buf) {
        this(new SyncedClientOptions(buf));
    }

    private void write(PacketByteBuf buf) {
        this.options.write(buf);
    }

    @Override
    public PacketType<ClientOptionsC2SPacket> getPacketType() {
        return CommonPackets.CLIENT_INFORMATION;
    }

    @Override
    public void apply(ServerCommonPacketListener arg) {
        arg.onClientOptions(this);
    }
}

