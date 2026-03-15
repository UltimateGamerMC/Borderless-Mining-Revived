/*
 * External method calls:
 *   Lnet/minecraft/network/PacketByteBuf;writeBoolean(Z)Lnet/minecraft/network/PacketByteBuf;
 *   Lnet/minecraft/network/listener/ServerPlayPacketListener;onBoatPaddleState(Lnet/minecraft/network/packet/c2s/play/BoatPaddleStateC2SPacket;)V
 *   Lnet/minecraft/network/packet/Packet;createCodec(Lnet/minecraft/network/codec/ValueFirstEncoder;Lnet/minecraft/network/codec/PacketDecoder;)Lnet/minecraft/network/codec/PacketCodec;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/network/packet/c2s/play/BoatPaddleStateC2SPacket;apply(Lnet/minecraft/network/listener/ServerPlayPacketListener;)V
 */
package net.minecraft.network.packet.c2s.play;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.listener.ServerPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.PacketType;
import net.minecraft.network.packet.PlayPackets;

public class BoatPaddleStateC2SPacket
implements Packet<ServerPlayPacketListener> {
    public static final PacketCodec<PacketByteBuf, BoatPaddleStateC2SPacket> CODEC = Packet.createCodec(BoatPaddleStateC2SPacket::write, BoatPaddleStateC2SPacket::new);
    private final boolean leftPaddling;
    private final boolean rightPaddling;

    public BoatPaddleStateC2SPacket(boolean leftPaddling, boolean rightPaddling) {
        this.leftPaddling = leftPaddling;
        this.rightPaddling = rightPaddling;
    }

    private BoatPaddleStateC2SPacket(PacketByteBuf buf) {
        this.leftPaddling = buf.readBoolean();
        this.rightPaddling = buf.readBoolean();
    }

    private void write(PacketByteBuf buf) {
        buf.writeBoolean(this.leftPaddling);
        buf.writeBoolean(this.rightPaddling);
    }

    @Override
    public void apply(ServerPlayPacketListener arg) {
        arg.onBoatPaddleState(this);
    }

    @Override
    public PacketType<BoatPaddleStateC2SPacket> getPacketType() {
        return PlayPackets.PADDLE_BOAT;
    }

    public boolean isLeftPaddling() {
        return this.leftPaddling;
    }

    public boolean isRightPaddling() {
        return this.rightPaddling;
    }
}

