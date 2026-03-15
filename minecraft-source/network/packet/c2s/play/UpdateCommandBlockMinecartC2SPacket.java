/*
 * External method calls:
 *   Lnet/minecraft/network/PacketByteBuf;readString()Ljava/lang/String;
 *   Lnet/minecraft/network/PacketByteBuf;writeVarInt(I)Lnet/minecraft/network/PacketByteBuf;
 *   Lnet/minecraft/network/PacketByteBuf;writeString(Ljava/lang/String;)Lnet/minecraft/network/PacketByteBuf;
 *   Lnet/minecraft/network/PacketByteBuf;writeBoolean(Z)Lnet/minecraft/network/PacketByteBuf;
 *   Lnet/minecraft/network/listener/ServerPlayPacketListener;onUpdateCommandBlockMinecart(Lnet/minecraft/network/packet/c2s/play/UpdateCommandBlockMinecartC2SPacket;)V
 *   Lnet/minecraft/network/packet/Packet;createCodec(Lnet/minecraft/network/codec/ValueFirstEncoder;Lnet/minecraft/network/codec/PacketDecoder;)Lnet/minecraft/network/codec/PacketCodec;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/network/packet/c2s/play/UpdateCommandBlockMinecartC2SPacket;apply(Lnet/minecraft/network/listener/ServerPlayPacketListener;)V
 */
package net.minecraft.network.packet.c2s.play;

import net.minecraft.entity.Entity;
import net.minecraft.entity.vehicle.CommandBlockMinecartEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.listener.ServerPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.PacketType;
import net.minecraft.network.packet.PlayPackets;
import net.minecraft.world.CommandBlockExecutor;
import net.minecraft.world.World;
import org.jspecify.annotations.Nullable;

public class UpdateCommandBlockMinecartC2SPacket
implements Packet<ServerPlayPacketListener> {
    public static final PacketCodec<PacketByteBuf, UpdateCommandBlockMinecartC2SPacket> CODEC = Packet.createCodec(UpdateCommandBlockMinecartC2SPacket::write, UpdateCommandBlockMinecartC2SPacket::new);
    private final int entityId;
    private final String command;
    private final boolean trackOutput;

    public UpdateCommandBlockMinecartC2SPacket(int entityId, String command, boolean trackOutput) {
        this.entityId = entityId;
        this.command = command;
        this.trackOutput = trackOutput;
    }

    private UpdateCommandBlockMinecartC2SPacket(PacketByteBuf buf) {
        this.entityId = buf.readVarInt();
        this.command = buf.readString();
        this.trackOutput = buf.readBoolean();
    }

    private void write(PacketByteBuf buf) {
        buf.writeVarInt(this.entityId);
        buf.writeString(this.command);
        buf.writeBoolean(this.trackOutput);
    }

    @Override
    public PacketType<UpdateCommandBlockMinecartC2SPacket> getPacketType() {
        return PlayPackets.SET_COMMAND_MINECART;
    }

    @Override
    public void apply(ServerPlayPacketListener arg) {
        arg.onUpdateCommandBlockMinecart(this);
    }

    public @Nullable CommandBlockExecutor getMinecartCommandExecutor(World world) {
        Entity lv = world.getEntityById(this.entityId);
        if (lv instanceof CommandBlockMinecartEntity) {
            return ((CommandBlockMinecartEntity)lv).getCommandExecutor();
        }
        return null;
    }

    public String getCommand() {
        return this.command;
    }

    public boolean shouldTrackOutput() {
        return this.trackOutput;
    }
}

