/*
 * External method calls:
 *   Lnet/minecraft/network/PacketByteBuf;readUuid()Ljava/util/UUID;
 *   Lnet/minecraft/network/PacketByteBuf;writeUuid(Ljava/util/UUID;)Lnet/minecraft/network/PacketByteBuf;
 *   Lnet/minecraft/network/listener/ServerPlayPacketListener;onSpectatorTeleport(Lnet/minecraft/network/packet/c2s/play/SpectatorTeleportC2SPacket;)V
 *   Lnet/minecraft/network/packet/Packet;createCodec(Lnet/minecraft/network/codec/ValueFirstEncoder;Lnet/minecraft/network/codec/PacketDecoder;)Lnet/minecraft/network/codec/PacketCodec;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/network/packet/c2s/play/SpectatorTeleportC2SPacket;apply(Lnet/minecraft/network/listener/ServerPlayPacketListener;)V
 */
package net.minecraft.network.packet.c2s.play;

import java.util.UUID;
import net.minecraft.entity.Entity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.listener.ServerPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.PacketType;
import net.minecraft.network.packet.PlayPackets;
import net.minecraft.server.world.ServerWorld;
import org.jspecify.annotations.Nullable;

public class SpectatorTeleportC2SPacket
implements Packet<ServerPlayPacketListener> {
    public static final PacketCodec<PacketByteBuf, SpectatorTeleportC2SPacket> CODEC = Packet.createCodec(SpectatorTeleportC2SPacket::write, SpectatorTeleportC2SPacket::new);
    private final UUID targetUuid;

    public SpectatorTeleportC2SPacket(UUID targetUuid) {
        this.targetUuid = targetUuid;
    }

    private SpectatorTeleportC2SPacket(PacketByteBuf buf) {
        this.targetUuid = buf.readUuid();
    }

    private void write(PacketByteBuf buf) {
        buf.writeUuid(this.targetUuid);
    }

    @Override
    public PacketType<SpectatorTeleportC2SPacket> getPacketType() {
        return PlayPackets.TELEPORT_TO_ENTITY;
    }

    @Override
    public void apply(ServerPlayPacketListener arg) {
        arg.onSpectatorTeleport(this);
    }

    public @Nullable Entity getTarget(ServerWorld world) {
        return world.getEntity(this.targetUuid);
    }
}

