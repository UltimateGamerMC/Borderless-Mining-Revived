/*
 * External method calls:
 *   Lnet/minecraft/network/codec/PacketCodec;decode(Ljava/lang/Object;)Ljava/lang/Object;
 *   Lnet/minecraft/network/RegistryByteBuf;readCollection(Ljava/util/function/IntFunction;Lnet/minecraft/network/codec/PacketDecoder;)Ljava/util/Collection;
 *   Lnet/minecraft/network/RegistryByteBuf;readMap(Lnet/minecraft/network/codec/PacketDecoder;Lnet/minecraft/network/codec/PacketDecoder;)Ljava/util/Map;
 *   Lnet/minecraft/network/RegistryByteBuf;writeBoolean(Z)Lnet/minecraft/network/PacketByteBuf;
 *   Lnet/minecraft/network/codec/PacketCodec;encode(Ljava/lang/Object;Ljava/lang/Object;)V
 *   Lnet/minecraft/network/RegistryByteBuf;writeCollection(Ljava/util/Collection;Lnet/minecraft/network/codec/PacketEncoder;)V
 *   Lnet/minecraft/network/RegistryByteBuf;writeMap(Ljava/util/Map;Lnet/minecraft/network/codec/PacketEncoder;Lnet/minecraft/network/codec/PacketEncoder;)V
 *   Lnet/minecraft/network/listener/ClientPlayPacketListener;onAdvancements(Lnet/minecraft/network/packet/s2c/play/AdvancementUpdateS2CPacket;)V
 *   Lnet/minecraft/advancement/AdvancementProgress;toPacket(Lnet/minecraft/network/PacketByteBuf;)V
 *   Lnet/minecraft/network/packet/Packet;createCodec(Lnet/minecraft/network/codec/ValueFirstEncoder;Lnet/minecraft/network/codec/PacketDecoder;)Lnet/minecraft/network/codec/PacketCodec;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/network/packet/s2c/play/AdvancementUpdateS2CPacket;apply(Lnet/minecraft/network/listener/ClientPlayPacketListener;)V
 */
package net.minecraft.network.packet.s2c.play;

import com.google.common.collect.Sets;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.minecraft.advancement.AdvancementEntry;
import net.minecraft.advancement.AdvancementProgress;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.PacketType;
import net.minecraft.network.packet.PlayPackets;
import net.minecraft.util.Identifier;

public class AdvancementUpdateS2CPacket
implements Packet<ClientPlayPacketListener> {
    public static final PacketCodec<RegistryByteBuf, AdvancementUpdateS2CPacket> CODEC = Packet.createCodec(AdvancementUpdateS2CPacket::write, AdvancementUpdateS2CPacket::new);
    private final boolean clearCurrent;
    private final List<AdvancementEntry> toEarn;
    private final Set<Identifier> toRemove;
    private final Map<Identifier, AdvancementProgress> toSetProgress;
    private final boolean showToast;

    public AdvancementUpdateS2CPacket(boolean clearCurrent, Collection<AdvancementEntry> toEarn, Set<Identifier> toRemove, Map<Identifier, AdvancementProgress> toSetProgress, boolean showToast) {
        this.clearCurrent = clearCurrent;
        this.toEarn = List.copyOf(toEarn);
        this.toRemove = Set.copyOf(toRemove);
        this.toSetProgress = Map.copyOf(toSetProgress);
        this.showToast = showToast;
    }

    private AdvancementUpdateS2CPacket(RegistryByteBuf buf) {
        this.clearCurrent = buf.readBoolean();
        this.toEarn = (List)AdvancementEntry.LIST_PACKET_CODEC.decode(buf);
        this.toRemove = buf.readCollection(Sets::newLinkedHashSetWithExpectedSize, PacketByteBuf::readIdentifier);
        this.toSetProgress = buf.readMap(PacketByteBuf::readIdentifier, AdvancementProgress::fromPacket);
        this.showToast = buf.readBoolean();
    }

    private void write(RegistryByteBuf buf) {
        buf.writeBoolean(this.clearCurrent);
        AdvancementEntry.LIST_PACKET_CODEC.encode(buf, this.toEarn);
        buf.writeCollection(this.toRemove, PacketByteBuf::writeIdentifier);
        buf.writeMap(this.toSetProgress, PacketByteBuf::writeIdentifier, (buf2, progress) -> progress.toPacket((PacketByteBuf)buf2));
        buf.writeBoolean(this.showToast);
    }

    @Override
    public PacketType<AdvancementUpdateS2CPacket> getPacketType() {
        return PlayPackets.UPDATE_ADVANCEMENTS;
    }

    @Override
    public void apply(ClientPlayPacketListener arg) {
        arg.onAdvancements(this);
    }

    public List<AdvancementEntry> getAdvancementsToEarn() {
        return this.toEarn;
    }

    public Set<Identifier> getAdvancementIdsToRemove() {
        return this.toRemove;
    }

    public Map<Identifier, AdvancementProgress> getAdvancementsToProgress() {
        return this.toSetProgress;
    }

    public boolean shouldClearCurrent() {
        return this.clearCurrent;
    }

    public boolean shouldShowToast() {
        return this.showToast;
    }
}

