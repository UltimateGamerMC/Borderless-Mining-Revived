/*
 * External method calls:
 *   Lnet/minecraft/network/PacketByteBuf;readBitSet()Ljava/util/BitSet;
 *   Lnet/minecraft/network/PacketByteBuf;readList(Lnet/minecraft/network/codec/PacketDecoder;)Ljava/util/List;
 *   Lnet/minecraft/network/PacketByteBuf;writeBitSet(Ljava/util/BitSet;)V
 *   Lnet/minecraft/network/PacketByteBuf;writeCollection(Ljava/util/Collection;Lnet/minecraft/network/codec/PacketEncoder;)V
 *   Lnet/minecraft/world/chunk/ChunkNibbleArray;asByteArray()[B
 *   Lnet/minecraft/network/codec/PacketCodecs;byteArray(I)Lnet/minecraft/network/codec/PacketCodec;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/network/packet/s2c/play/LightData;putChunk(Lnet/minecraft/util/math/ChunkPos;Lnet/minecraft/world/chunk/light/LightingProvider;Lnet/minecraft/world/LightType;ILjava/util/BitSet;Ljava/util/BitSet;Ljava/util/List;)V
 */
package net.minecraft.network.packet.s2c.play;

import com.google.common.collect.Lists;
import io.netty.buffer.ByteBuf;
import java.util.BitSet;
import java.util.List;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.ChunkSectionPos;
import net.minecraft.world.LightType;
import net.minecraft.world.chunk.ChunkNibbleArray;
import net.minecraft.world.chunk.light.LightingProvider;
import org.jspecify.annotations.Nullable;

public class LightData {
    private static final PacketCodec<ByteBuf, byte[]> CODEC = PacketCodecs.byteArray(2048);
    private final BitSet initedSky;
    private final BitSet initedBlock;
    private final BitSet uninitedSky;
    private final BitSet uninitedBlock;
    private final List<byte[]> skyNibbles;
    private final List<byte[]> blockNibbles;

    public LightData(ChunkPos pos, LightingProvider lightProvider, @Nullable BitSet skyBits, @Nullable BitSet blockBits) {
        this.initedSky = new BitSet();
        this.initedBlock = new BitSet();
        this.uninitedSky = new BitSet();
        this.uninitedBlock = new BitSet();
        this.skyNibbles = Lists.newArrayList();
        this.blockNibbles = Lists.newArrayList();
        for (int i = 0; i < lightProvider.getHeight(); ++i) {
            if (skyBits == null || skyBits.get(i)) {
                this.putChunk(pos, lightProvider, LightType.SKY, i, this.initedSky, this.uninitedSky, this.skyNibbles);
            }
            if (blockBits != null && !blockBits.get(i)) continue;
            this.putChunk(pos, lightProvider, LightType.BLOCK, i, this.initedBlock, this.uninitedBlock, this.blockNibbles);
        }
    }

    public LightData(PacketByteBuf buf, int x, int y) {
        this.initedSky = buf.readBitSet();
        this.initedBlock = buf.readBitSet();
        this.uninitedSky = buf.readBitSet();
        this.uninitedBlock = buf.readBitSet();
        this.skyNibbles = buf.readList(CODEC);
        this.blockNibbles = buf.readList(CODEC);
    }

    public void write(PacketByteBuf buf) {
        buf.writeBitSet(this.initedSky);
        buf.writeBitSet(this.initedBlock);
        buf.writeBitSet(this.uninitedSky);
        buf.writeBitSet(this.uninitedBlock);
        buf.writeCollection(this.skyNibbles, CODEC);
        buf.writeCollection(this.blockNibbles, CODEC);
    }

    private void putChunk(ChunkPos pos, LightingProvider lightProvider, LightType type, int y, BitSet initialized, BitSet uninitialized, List<byte[]> nibbles) {
        ChunkNibbleArray lv = lightProvider.get(type).getLightSection(ChunkSectionPos.from(pos, lightProvider.getBottomY() + y));
        if (lv != null) {
            if (lv.isUninitialized()) {
                uninitialized.set(y);
            } else {
                initialized.set(y);
                nibbles.add(lv.copy().asByteArray());
            }
        }
    }

    public BitSet getInitedSky() {
        return this.initedSky;
    }

    public BitSet getUninitedSky() {
        return this.uninitedSky;
    }

    public List<byte[]> getSkyNibbles() {
        return this.skyNibbles;
    }

    public BitSet getInitedBlock() {
        return this.initedBlock;
    }

    public BitSet getUninitedBlock() {
        return this.uninitedBlock;
    }

    public List<byte[]> getBlockNibbles() {
        return this.blockNibbles;
    }
}

