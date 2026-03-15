/*
 * External method calls:
 *   Lnet/minecraft/network/PacketByteBuf;writeVarInt(I)Lnet/minecraft/network/PacketByteBuf;
 *   Lnet/minecraft/network/listener/ServerPlayPacketListener;onTeleportConfirm(Lnet/minecraft/network/packet/c2s/play/TeleportConfirmC2SPacket;)V
 *   Lnet/minecraft/network/packet/Packet;createCodec(Lnet/minecraft/network/codec/ValueFirstEncoder;Lnet/minecraft/network/codec/PacketDecoder;)Lnet/minecraft/network/codec/PacketCodec;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/network/packet/c2s/play/TeleportConfirmC2SPacket;apply(Lnet/minecraft/network/listener/ServerPlayPacketListener;)V
 */
package net.minecraft.network.packet.c2s.play;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.listener.ServerPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.PacketType;
import net.minecraft.network.packet.PlayPackets;

public class TeleportConfirmC2SPacket
implements Packet<ServerPlayPacketListener> {
    public static final PacketCodec<PacketByteBuf, TeleportConfirmC2SPacket> CODEC = Packet.createCodec(TeleportConfirmC2SPacket::write, TeleportConfirmC2SPacket::new);
    private final int teleportId;

    public TeleportConfirmC2SPacket(int teleportId) {
        this.teleportId = teleportId;
    }

    private TeleportConfirmC2SPacket(PacketByteBuf buf) {
        this.teleportId = buf.readVarInt();
    }

    private void write(PacketByteBuf buf) {
        buf.writeVarInt(this.teleportId);
    }

    @Override
    public PacketType<TeleportConfirmC2SPacket> getPacketType() {
        return PlayPackets.ACCEPT_TELEPORTATION;
    }

    @Override
    public void apply(ServerPlayPacketListener arg) {
        arg.onTeleportConfirm(this);
    }

    public int getTeleportId() {
        return this.teleportId;
    }
}

