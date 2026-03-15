/*
 * External method calls:
 *   Lnet/minecraft/network/PacketByteBuf;readUuid()Ljava/util/UUID;
 *   Lnet/minecraft/network/PacketByteBuf;readEnumConstant(Ljava/lang/Class;)Ljava/lang/Enum;
 *   Lnet/minecraft/network/PacketByteBuf;writeUuid(Ljava/util/UUID;)Lnet/minecraft/network/PacketByteBuf;
 *   Lnet/minecraft/network/PacketByteBuf;writeEnumConstant(Ljava/lang/Enum;)Lnet/minecraft/network/PacketByteBuf;
 *   Lnet/minecraft/network/listener/ServerCommonPacketListener;onResourcePackStatus(Lnet/minecraft/network/packet/c2s/common/ResourcePackStatusC2SPacket;)V
 *   Lnet/minecraft/network/packet/Packet;createCodec(Lnet/minecraft/network/codec/ValueFirstEncoder;Lnet/minecraft/network/codec/PacketDecoder;)Lnet/minecraft/network/codec/PacketCodec;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/network/packet/c2s/common/ResourcePackStatusC2SPacket;apply(Lnet/minecraft/network/listener/ServerCommonPacketListener;)V
 */
package net.minecraft.network.packet.c2s.common;

import java.util.UUID;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.listener.ServerCommonPacketListener;
import net.minecraft.network.packet.CommonPackets;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.PacketType;

public record ResourcePackStatusC2SPacket(UUID id, Status status) implements Packet<ServerCommonPacketListener>
{
    public static final PacketCodec<PacketByteBuf, ResourcePackStatusC2SPacket> CODEC = Packet.createCodec(ResourcePackStatusC2SPacket::write, ResourcePackStatusC2SPacket::new);

    private ResourcePackStatusC2SPacket(PacketByteBuf buf) {
        this(buf.readUuid(), buf.readEnumConstant(Status.class));
    }

    private void write(PacketByteBuf buf) {
        buf.writeUuid(this.id);
        buf.writeEnumConstant(this.status);
    }

    @Override
    public PacketType<ResourcePackStatusC2SPacket> getPacketType() {
        return CommonPackets.RESOURCE_PACK;
    }

    @Override
    public void apply(ServerCommonPacketListener arg) {
        arg.onResourcePackStatus(this);
    }

    public static enum Status {
        SUCCESSFULLY_LOADED,
        DECLINED,
        FAILED_DOWNLOAD,
        ACCEPTED,
        DOWNLOADED,
        INVALID_URL,
        FAILED_RELOAD,
        DISCARDED;


        public boolean hasFinished() {
            return this != ACCEPTED && this != DOWNLOADED;
        }
    }
}

