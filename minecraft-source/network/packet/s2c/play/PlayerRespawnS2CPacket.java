/*
 * External method calls:
 *   Lnet/minecraft/network/packet/s2c/play/CommonPlayerSpawnInfo;write(Lnet/minecraft/network/RegistryByteBuf;)V
 *   Lnet/minecraft/network/RegistryByteBuf;writeByte(I)Lnet/minecraft/network/PacketByteBuf;
 *   Lnet/minecraft/network/listener/ClientPlayPacketListener;onPlayerRespawn(Lnet/minecraft/network/packet/s2c/play/PlayerRespawnS2CPacket;)V
 *   Lnet/minecraft/network/packet/Packet;createCodec(Lnet/minecraft/network/codec/ValueFirstEncoder;Lnet/minecraft/network/codec/PacketDecoder;)Lnet/minecraft/network/codec/PacketCodec;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/network/packet/s2c/play/PlayerRespawnS2CPacket;apply(Lnet/minecraft/network/listener/ClientPlayPacketListener;)V
 */
package net.minecraft.network.packet.s2c.play;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.PacketType;
import net.minecraft.network.packet.PlayPackets;
import net.minecraft.network.packet.s2c.play.CommonPlayerSpawnInfo;

public record PlayerRespawnS2CPacket(CommonPlayerSpawnInfo commonPlayerSpawnInfo, byte flag) implements Packet<ClientPlayPacketListener>
{
    public static final PacketCodec<RegistryByteBuf, PlayerRespawnS2CPacket> CODEC = Packet.createCodec(PlayerRespawnS2CPacket::write, PlayerRespawnS2CPacket::new);
    public static final byte KEEP_ATTRIBUTES = 1;
    public static final byte KEEP_TRACKED_DATA = 2;
    public static final byte KEEP_ALL = 3;

    private PlayerRespawnS2CPacket(RegistryByteBuf arg) {
        this(new CommonPlayerSpawnInfo(arg), arg.readByte());
    }

    private void write(RegistryByteBuf arg) {
        this.commonPlayerSpawnInfo.write(arg);
        arg.writeByte(this.flag);
    }

    @Override
    public PacketType<PlayerRespawnS2CPacket> getPacketType() {
        return PlayPackets.RESPAWN;
    }

    @Override
    public void apply(ClientPlayPacketListener arg) {
        arg.onPlayerRespawn(this);
    }

    public boolean hasFlag(byte flag) {
        return (this.flag & flag) != 0;
    }
}

