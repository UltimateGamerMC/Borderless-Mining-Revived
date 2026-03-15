/*
 * External method calls:
 *   Lnet/minecraft/network/PacketByteBuf;readNullable(Lnet/minecraft/network/codec/PacketDecoder;)Ljava/lang/Object;
 *   Lnet/minecraft/network/PacketByteBuf;writeNullable(Ljava/lang/Object;Lnet/minecraft/network/codec/PacketEncoder;)V
 *   Lnet/minecraft/network/listener/ClientPlayPacketListener;onSelectAdvancementTab(Lnet/minecraft/network/packet/s2c/play/SelectAdvancementTabS2CPacket;)V
 *   Lnet/minecraft/network/packet/Packet;createCodec(Lnet/minecraft/network/codec/ValueFirstEncoder;Lnet/minecraft/network/codec/PacketDecoder;)Lnet/minecraft/network/codec/PacketCodec;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/network/packet/s2c/play/SelectAdvancementTabS2CPacket;apply(Lnet/minecraft/network/listener/ClientPlayPacketListener;)V
 */
package net.minecraft.network.packet.s2c.play;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.PacketType;
import net.minecraft.network.packet.PlayPackets;
import net.minecraft.util.Identifier;
import org.jspecify.annotations.Nullable;

public class SelectAdvancementTabS2CPacket
implements Packet<ClientPlayPacketListener> {
    public static final PacketCodec<PacketByteBuf, SelectAdvancementTabS2CPacket> CODEC = Packet.createCodec(SelectAdvancementTabS2CPacket::write, SelectAdvancementTabS2CPacket::new);
    private final @Nullable Identifier tabId;

    public SelectAdvancementTabS2CPacket(@Nullable Identifier tabId) {
        this.tabId = tabId;
    }

    private SelectAdvancementTabS2CPacket(PacketByteBuf buf) {
        this.tabId = buf.readNullable(PacketByteBuf::readIdentifier);
    }

    private void write(PacketByteBuf buf) {
        buf.writeNullable(this.tabId, PacketByteBuf::writeIdentifier);
    }

    @Override
    public PacketType<SelectAdvancementTabS2CPacket> getPacketType() {
        return PlayPackets.SELECT_ADVANCEMENTS_TAB;
    }

    @Override
    public void apply(ClientPlayPacketListener arg) {
        arg.onSelectAdvancementTab(this);
    }

    public @Nullable Identifier getTabId() {
        return this.tabId;
    }
}

