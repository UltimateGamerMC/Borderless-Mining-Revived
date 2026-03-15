/*
 * External method calls:
 *   Lnet/minecraft/network/PacketByteBuf;writeVarInt(I)Lnet/minecraft/network/PacketByteBuf;
 *   Lnet/minecraft/network/listener/ServerPlayPacketListener;onQueryEntityNbt(Lnet/minecraft/network/packet/c2s/play/QueryEntityNbtC2SPacket;)V
 *   Lnet/minecraft/network/packet/Packet;createCodec(Lnet/minecraft/network/codec/ValueFirstEncoder;Lnet/minecraft/network/codec/PacketDecoder;)Lnet/minecraft/network/codec/PacketCodec;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/network/packet/c2s/play/QueryEntityNbtC2SPacket;apply(Lnet/minecraft/network/listener/ServerPlayPacketListener;)V
 */
package net.minecraft.network.packet.c2s.play;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.listener.ServerPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.PacketType;
import net.minecraft.network.packet.PlayPackets;

public class QueryEntityNbtC2SPacket
implements Packet<ServerPlayPacketListener> {
    public static final PacketCodec<PacketByteBuf, QueryEntityNbtC2SPacket> CODEC = Packet.createCodec(QueryEntityNbtC2SPacket::write, QueryEntityNbtC2SPacket::new);
    private final int transactionId;
    private final int entityId;

    public QueryEntityNbtC2SPacket(int transactionId, int entityId) {
        this.transactionId = transactionId;
        this.entityId = entityId;
    }

    private QueryEntityNbtC2SPacket(PacketByteBuf buf) {
        this.transactionId = buf.readVarInt();
        this.entityId = buf.readVarInt();
    }

    private void write(PacketByteBuf buf) {
        buf.writeVarInt(this.transactionId);
        buf.writeVarInt(this.entityId);
    }

    @Override
    public PacketType<QueryEntityNbtC2SPacket> getPacketType() {
        return PlayPackets.ENTITY_TAG_QUERY;
    }

    @Override
    public void apply(ServerPlayPacketListener arg) {
        arg.onQueryEntityNbt(this);
    }

    public int getTransactionId() {
        return this.transactionId;
    }

    public int getEntityId() {
        return this.entityId;
    }
}

