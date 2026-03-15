/*
 * External method calls:
 *   Lnet/minecraft/network/listener/ClientPlayPacketListener;onOpenScreen(Lnet/minecraft/network/packet/s2c/play/OpenScreenS2CPacket;)V
 *   Lnet/minecraft/network/codec/PacketCodecs;registryValue(Lnet/minecraft/registry/RegistryKey;)Lnet/minecraft/network/codec/PacketCodec;
 *   Lnet/minecraft/network/codec/PacketCodec;tuple(Lnet/minecraft/network/codec/PacketCodec;Ljava/util/function/Function;Lnet/minecraft/network/codec/PacketCodec;Ljava/util/function/Function;Lnet/minecraft/network/codec/PacketCodec;Ljava/util/function/Function;Lcom/mojang/datafixers/util/Function3;)Lnet/minecraft/network/codec/PacketCodec;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/network/packet/s2c/play/OpenScreenS2CPacket;apply(Lnet/minecraft/network/listener/ClientPlayPacketListener;)V
 */
package net.minecraft.network.packet.s2c.play;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.PacketType;
import net.minecraft.network.packet.PlayPackets;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.text.Text;
import net.minecraft.text.TextCodecs;

public class OpenScreenS2CPacket
implements Packet<ClientPlayPacketListener> {
    public static final PacketCodec<RegistryByteBuf, OpenScreenS2CPacket> CODEC = PacketCodec.tuple(PacketCodecs.SYNC_ID, OpenScreenS2CPacket::getSyncId, PacketCodecs.registryValue(RegistryKeys.SCREEN_HANDLER), OpenScreenS2CPacket::getScreenHandlerType, TextCodecs.UNLIMITED_REGISTRY_PACKET_CODEC, OpenScreenS2CPacket::getName, OpenScreenS2CPacket::new);
    private final int syncId;
    private final ScreenHandlerType<?> screenHandlerId;
    private final Text name;

    public OpenScreenS2CPacket(int syncId, ScreenHandlerType<?> screenHandlerId, Text name) {
        this.syncId = syncId;
        this.screenHandlerId = screenHandlerId;
        this.name = name;
    }

    @Override
    public PacketType<OpenScreenS2CPacket> getPacketType() {
        return PlayPackets.OPEN_SCREEN;
    }

    @Override
    public void apply(ClientPlayPacketListener arg) {
        arg.onOpenScreen(this);
    }

    public int getSyncId() {
        return this.syncId;
    }

    public ScreenHandlerType<?> getScreenHandlerType() {
        return this.screenHandlerId;
    }

    public Text getName() {
        return this.name;
    }
}

