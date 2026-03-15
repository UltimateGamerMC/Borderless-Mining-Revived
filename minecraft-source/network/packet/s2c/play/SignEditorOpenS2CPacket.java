/*
 * External method calls:
 *   Lnet/minecraft/network/PacketByteBuf;readBlockPos()Lnet/minecraft/util/math/BlockPos;
 *   Lnet/minecraft/network/PacketByteBuf;writeBlockPos(Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/network/PacketByteBuf;
 *   Lnet/minecraft/network/PacketByteBuf;writeBoolean(Z)Lnet/minecraft/network/PacketByteBuf;
 *   Lnet/minecraft/network/listener/ClientPlayPacketListener;onSignEditorOpen(Lnet/minecraft/network/packet/s2c/play/SignEditorOpenS2CPacket;)V
 *   Lnet/minecraft/network/packet/Packet;createCodec(Lnet/minecraft/network/codec/ValueFirstEncoder;Lnet/minecraft/network/codec/PacketDecoder;)Lnet/minecraft/network/codec/PacketCodec;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/network/packet/s2c/play/SignEditorOpenS2CPacket;apply(Lnet/minecraft/network/listener/ClientPlayPacketListener;)V
 */
package net.minecraft.network.packet.s2c.play;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.PacketType;
import net.minecraft.network.packet.PlayPackets;
import net.minecraft.util.math.BlockPos;

public class SignEditorOpenS2CPacket
implements Packet<ClientPlayPacketListener> {
    public static final PacketCodec<PacketByteBuf, SignEditorOpenS2CPacket> CODEC = Packet.createCodec(SignEditorOpenS2CPacket::write, SignEditorOpenS2CPacket::new);
    private final BlockPos pos;
    private final boolean front;

    public SignEditorOpenS2CPacket(BlockPos pos, boolean front) {
        this.pos = pos;
        this.front = front;
    }

    private SignEditorOpenS2CPacket(PacketByteBuf buf) {
        this.pos = buf.readBlockPos();
        this.front = buf.readBoolean();
    }

    private void write(PacketByteBuf buf) {
        buf.writeBlockPos(this.pos);
        buf.writeBoolean(this.front);
    }

    @Override
    public PacketType<SignEditorOpenS2CPacket> getPacketType() {
        return PlayPackets.OPEN_SIGN_EDITOR;
    }

    @Override
    public void apply(ClientPlayPacketListener arg) {
        arg.onSignEditorOpen(this);
    }

    public BlockPos getPos() {
        return this.pos;
    }

    public boolean isFront() {
        return this.front;
    }
}

