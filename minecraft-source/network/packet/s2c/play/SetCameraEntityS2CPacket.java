/*
 * External method calls:
 *   Lnet/minecraft/network/PacketByteBuf;writeVarInt(I)Lnet/minecraft/network/PacketByteBuf;
 *   Lnet/minecraft/network/listener/ClientPlayPacketListener;onSetCameraEntity(Lnet/minecraft/network/packet/s2c/play/SetCameraEntityS2CPacket;)V
 *   Lnet/minecraft/network/packet/Packet;createCodec(Lnet/minecraft/network/codec/ValueFirstEncoder;Lnet/minecraft/network/codec/PacketDecoder;)Lnet/minecraft/network/codec/PacketCodec;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/network/packet/s2c/play/SetCameraEntityS2CPacket;apply(Lnet/minecraft/network/listener/ClientPlayPacketListener;)V
 */
package net.minecraft.network.packet.s2c.play;

import net.minecraft.entity.Entity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.PacketType;
import net.minecraft.network.packet.PlayPackets;
import net.minecraft.world.World;
import org.jspecify.annotations.Nullable;

public class SetCameraEntityS2CPacket
implements Packet<ClientPlayPacketListener> {
    public static final PacketCodec<PacketByteBuf, SetCameraEntityS2CPacket> CODEC = Packet.createCodec(SetCameraEntityS2CPacket::write, SetCameraEntityS2CPacket::new);
    private final int entityId;

    public SetCameraEntityS2CPacket(Entity entity) {
        this.entityId = entity.getId();
    }

    private SetCameraEntityS2CPacket(PacketByteBuf buf) {
        this.entityId = buf.readVarInt();
    }

    private void write(PacketByteBuf buf) {
        buf.writeVarInt(this.entityId);
    }

    @Override
    public PacketType<SetCameraEntityS2CPacket> getPacketType() {
        return PlayPackets.SET_CAMERA;
    }

    @Override
    public void apply(ClientPlayPacketListener arg) {
        arg.onSetCameraEntity(this);
    }

    public @Nullable Entity getEntity(World world) {
        return world.getEntityById(this.entityId);
    }
}

