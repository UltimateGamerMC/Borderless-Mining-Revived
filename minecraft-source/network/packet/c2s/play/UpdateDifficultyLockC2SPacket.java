/*
 * External method calls:
 *   Lnet/minecraft/network/PacketByteBuf;writeBoolean(Z)Lnet/minecraft/network/PacketByteBuf;
 *   Lnet/minecraft/network/listener/ServerPlayPacketListener;onUpdateDifficultyLock(Lnet/minecraft/network/packet/c2s/play/UpdateDifficultyLockC2SPacket;)V
 *   Lnet/minecraft/network/packet/Packet;createCodec(Lnet/minecraft/network/codec/ValueFirstEncoder;Lnet/minecraft/network/codec/PacketDecoder;)Lnet/minecraft/network/codec/PacketCodec;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/network/packet/c2s/play/UpdateDifficultyLockC2SPacket;apply(Lnet/minecraft/network/listener/ServerPlayPacketListener;)V
 */
package net.minecraft.network.packet.c2s.play;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.listener.ServerPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.PacketType;
import net.minecraft.network.packet.PlayPackets;

public class UpdateDifficultyLockC2SPacket
implements Packet<ServerPlayPacketListener> {
    public static final PacketCodec<PacketByteBuf, UpdateDifficultyLockC2SPacket> CODEC = Packet.createCodec(UpdateDifficultyLockC2SPacket::write, UpdateDifficultyLockC2SPacket::new);
    private final boolean difficultyLocked;

    public UpdateDifficultyLockC2SPacket(boolean difficultyLocked) {
        this.difficultyLocked = difficultyLocked;
    }

    private UpdateDifficultyLockC2SPacket(PacketByteBuf buf) {
        this.difficultyLocked = buf.readBoolean();
    }

    private void write(PacketByteBuf buf) {
        buf.writeBoolean(this.difficultyLocked);
    }

    @Override
    public PacketType<UpdateDifficultyLockC2SPacket> getPacketType() {
        return PlayPackets.LOCK_DIFFICULTY;
    }

    @Override
    public void apply(ServerPlayPacketListener arg) {
        arg.onUpdateDifficultyLock(this);
    }

    public boolean isDifficultyLocked() {
        return this.difficultyLocked;
    }
}

