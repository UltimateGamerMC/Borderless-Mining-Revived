/*
 * External method calls:
 *   Lnet/minecraft/network/PacketByteBuf;readBlockPos()Lnet/minecraft/util/math/BlockPos;
 *   Lnet/minecraft/network/PacketByteBuf;writeVarInt(I)Lnet/minecraft/network/PacketByteBuf;
 *   Lnet/minecraft/network/PacketByteBuf;writeBlockPos(Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/network/PacketByteBuf;
 *   Lnet/minecraft/network/listener/ServerPlayPacketListener;onQueryBlockNbt(Lnet/minecraft/network/packet/c2s/play/QueryBlockNbtC2SPacket;)V
 *   Lnet/minecraft/network/packet/Packet;createCodec(Lnet/minecraft/network/codec/ValueFirstEncoder;Lnet/minecraft/network/codec/PacketDecoder;)Lnet/minecraft/network/codec/PacketCodec;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/network/packet/c2s/play/QueryBlockNbtC2SPacket;apply(Lnet/minecraft/network/listener/ServerPlayPacketListener;)V
 */
package net.minecraft.network.packet.c2s.play;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.listener.ServerPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.PacketType;
import net.minecraft.network.packet.PlayPackets;
import net.minecraft.util.math.BlockPos;

public class QueryBlockNbtC2SPacket
implements Packet<ServerPlayPacketListener> {
    public static final PacketCodec<PacketByteBuf, QueryBlockNbtC2SPacket> CODEC = Packet.createCodec(QueryBlockNbtC2SPacket::write, QueryBlockNbtC2SPacket::new);
    private final int transactionId;
    private final BlockPos pos;

    public QueryBlockNbtC2SPacket(int transactionId, BlockPos pos) {
        this.transactionId = transactionId;
        this.pos = pos;
    }

    private QueryBlockNbtC2SPacket(PacketByteBuf buf) {
        this.transactionId = buf.readVarInt();
        this.pos = buf.readBlockPos();
    }

    private void write(PacketByteBuf buf) {
        buf.writeVarInt(this.transactionId);
        buf.writeBlockPos(this.pos);
    }

    @Override
    public PacketType<QueryBlockNbtC2SPacket> getPacketType() {
        return PlayPackets.BLOCK_ENTITY_TAG_QUERY;
    }

    @Override
    public void apply(ServerPlayPacketListener arg) {
        arg.onQueryBlockNbt(this);
    }

    public int getTransactionId() {
        return this.transactionId;
    }

    public BlockPos getPos() {
        return this.pos;
    }
}

