/*
 * External method calls:
 *   Lnet/minecraft/network/codec/PacketCodecs;toList()Lnet/minecraft/network/codec/PacketCodec$ResultFunction;
 *   Lnet/minecraft/network/codec/PacketCodec;collect(Lnet/minecraft/network/codec/PacketCodec$ResultFunction;)Lnet/minecraft/network/codec/PacketCodec;
 *   Lnet/minecraft/network/codec/PacketCodec;tuple(Lnet/minecraft/network/codec/PacketCodec;Ljava/util/function/Function;Lnet/minecraft/network/codec/PacketCodec;Ljava/util/function/Function;Ljava/util/function/BiFunction;)Lnet/minecraft/network/codec/PacketCodec;
 */
package net.minecraft.world.debug.data;

import io.netty.buffer.ByteBuf;
import java.util.List;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.util.math.BlockBox;

public record StructureDebugData(BlockBox boundingBox, List<Piece> pieces) {
    public static final PacketCodec<ByteBuf, StructureDebugData> PACKET_CODEC = PacketCodec.tuple(BlockBox.PACKET_CODEC, StructureDebugData::boundingBox, Piece.PACKET_CODEC.collect(PacketCodecs.toList()), StructureDebugData::pieces, StructureDebugData::new);

    public record Piece(BlockBox boundingBox, boolean isStart) {
        public static final PacketCodec<ByteBuf, Piece> PACKET_CODEC = PacketCodec.tuple(BlockBox.PACKET_CODEC, Piece::boundingBox, PacketCodecs.BOOLEAN, Piece::isStart, Piece::new);
    }
}

