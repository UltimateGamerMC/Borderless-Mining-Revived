/*
 * External method calls:
 *   Lnet/minecraft/network/codec/PacketCodec;tuple(Lnet/minecraft/network/codec/PacketCodec;Ljava/util/function/Function;Lnet/minecraft/network/codec/PacketCodec;Ljava/util/function/Function;Ljava/util/function/BiFunction;)Lnet/minecraft/network/codec/PacketCodec;
 *   Lnet/minecraft/network/codec/PacketCodecs;toList()Lnet/minecraft/network/codec/PacketCodec$ResultFunction;
 *   Lnet/minecraft/network/codec/PacketCodec;collect(Lnet/minecraft/network/codec/PacketCodec$ResultFunction;)Lnet/minecraft/network/codec/PacketCodec;
 */
package net.minecraft.advancement;

import java.util.List;
import net.minecraft.advancement.Advancement;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.util.Identifier;

public record AdvancementEntry(Identifier id, Advancement value) {
    public static final PacketCodec<RegistryByteBuf, AdvancementEntry> PACKET_CODEC = PacketCodec.tuple(Identifier.PACKET_CODEC, AdvancementEntry::id, Advancement.PACKET_CODEC, AdvancementEntry::value, AdvancementEntry::new);
    public static final PacketCodec<RegistryByteBuf, List<AdvancementEntry>> LIST_PACKET_CODEC = PACKET_CODEC.collect(PacketCodecs.toList());

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AdvancementEntry)) return false;
        AdvancementEntry lv = (AdvancementEntry)o;
        if (!this.id.equals(lv.id)) return false;
        return true;
    }

    @Override
    public int hashCode() {
        return this.id.hashCode();
    }

    @Override
    public String toString() {
        return this.id.toString();
    }
}

