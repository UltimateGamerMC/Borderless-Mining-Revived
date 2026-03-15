/*
 * External method calls:
 *   Lnet/minecraft/network/PacketByteBuf;readBlockPos()Lnet/minecraft/util/math/BlockPos;
 *   Lnet/minecraft/network/PacketByteBuf;writeVarInt(I)Lnet/minecraft/network/PacketByteBuf;
 *   Lnet/minecraft/network/PacketByteBuf;writeBlockPos(Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/network/PacketByteBuf;
 *   Lnet/minecraft/network/PacketByteBuf;writeByte(I)Lnet/minecraft/network/PacketByteBuf;
 *   Lnet/minecraft/network/listener/ClientPlayPacketListener;onBlockBreakingProgress(Lnet/minecraft/network/packet/s2c/play/BlockBreakingProgressS2CPacket;)V
 *   Lnet/minecraft/network/packet/Packet;createCodec(Lnet/minecraft/network/codec/ValueFirstEncoder;Lnet/minecraft/network/codec/PacketDecoder;)Lnet/minecraft/network/codec/PacketCodec;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/network/packet/s2c/play/BlockBreakingProgressS2CPacket;apply(Lnet/minecraft/network/listener/ClientPlayPacketListener;)V
 */
package net.minecraft.network.packet.s2c.play;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.PacketType;
import net.minecraft.network.packet.PlayPackets;
import net.minecraft.util.math.BlockPos;

public class BlockBreakingProgressS2CPacket
implements Packet<ClientPlayPacketListener> {
    public static final PacketCodec<PacketByteBuf, BlockBreakingProgressS2CPacket> CODEC = Packet.createCodec(BlockBreakingProgressS2CPacket::write, BlockBreakingProgressS2CPacket::new);
    private final int entityId;
    private final BlockPos pos;
    private final int progress;

    public BlockBreakingProgressS2CPacket(int entityId, BlockPos pos, int progress) {
        this.entityId = entityId;
        this.pos = pos;
        this.progress = progress;
    }

    private BlockBreakingProgressS2CPacket(PacketByteBuf buf) {
        this.entityId = buf.readVarInt();
        this.pos = buf.readBlockPos();
        this.progress = buf.readUnsignedByte();
    }

    private void write(PacketByteBuf buf) {
        buf.writeVarInt(this.entityId);
        buf.writeBlockPos(this.pos);
        buf.writeByte(this.progress);
    }

    @Override
    public PacketType<BlockBreakingProgressS2CPacket> getPacketType() {
        return PlayPackets.BLOCK_DESTRUCTION;
    }

    @Override
    public void apply(ClientPlayPacketListener arg) {
        arg.onBlockBreakingProgress(this);
    }

    public int getEntityId() {
        return this.entityId;
    }

    public BlockPos getPos() {
        return this.pos;
    }

    public int getProgress() {
        return this.progress;
    }
}

