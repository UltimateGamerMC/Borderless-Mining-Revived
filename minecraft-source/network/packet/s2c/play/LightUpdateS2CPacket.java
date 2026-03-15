/*
 * External method calls:
 *   Lnet/minecraft/network/PacketByteBuf;writeVarInt(I)Lnet/minecraft/network/PacketByteBuf;
 *   Lnet/minecraft/network/packet/s2c/play/LightData;write(Lnet/minecraft/network/PacketByteBuf;)V
 *   Lnet/minecraft/network/listener/ClientPlayPacketListener;onLightUpdate(Lnet/minecraft/network/packet/s2c/play/LightUpdateS2CPacket;)V
 *   Lnet/minecraft/network/packet/Packet;createCodec(Lnet/minecraft/network/codec/ValueFirstEncoder;Lnet/minecraft/network/codec/PacketDecoder;)Lnet/minecraft/network/codec/PacketCodec;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/network/packet/s2c/play/LightUpdateS2CPacket;apply(Lnet/minecraft/network/listener/ClientPlayPacketListener;)V
 */
package net.minecraft.network.packet.s2c.play;

import java.util.BitSet;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.PacketType;
import net.minecraft.network.packet.PlayPackets;
import net.minecraft.network.packet.s2c.play.LightData;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.chunk.light.LightingProvider;
import org.jspecify.annotations.Nullable;

public class LightUpdateS2CPacket
implements Packet<ClientPlayPacketListener> {
    public static final PacketCodec<PacketByteBuf, LightUpdateS2CPacket> CODEC = Packet.createCodec(LightUpdateS2CPacket::write, LightUpdateS2CPacket::new);
    private final int chunkX;
    private final int chunkZ;
    private final LightData data;

    public LightUpdateS2CPacket(ChunkPos chunkPos, LightingProvider lightProvider, @Nullable BitSet skyBits, @Nullable BitSet blockBits) {
        this.chunkX = chunkPos.x;
        this.chunkZ = chunkPos.z;
        this.data = new LightData(chunkPos, lightProvider, skyBits, blockBits);
    }

    private LightUpdateS2CPacket(PacketByteBuf buf) {
        this.chunkX = buf.readVarInt();
        this.chunkZ = buf.readVarInt();
        this.data = new LightData(buf, this.chunkX, this.chunkZ);
    }

    private void write(PacketByteBuf buf) {
        buf.writeVarInt(this.chunkX);
        buf.writeVarInt(this.chunkZ);
        this.data.write(buf);
    }

    @Override
    public PacketType<LightUpdateS2CPacket> getPacketType() {
        return PlayPackets.LIGHT_UPDATE;
    }

    @Override
    public void apply(ClientPlayPacketListener arg) {
        arg.onLightUpdate(this);
    }

    public int getChunkX() {
        return this.chunkX;
    }

    public int getChunkZ() {
        return this.chunkZ;
    }

    public LightData getData() {
        return this.data;
    }
}

