/*
 * External method calls:
 *   Lnet/minecraft/network/listener/ClientPlayPacketListener;onRecipeBookSettings(Lnet/minecraft/network/packet/s2c/play/RecipeBookSettingsS2CPacket;)V
 *   Lnet/minecraft/network/codec/PacketCodec;tuple(Lnet/minecraft/network/codec/PacketCodec;Ljava/util/function/Function;Ljava/util/function/Function;)Lnet/minecraft/network/codec/PacketCodec;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/network/packet/s2c/play/RecipeBookSettingsS2CPacket;apply(Lnet/minecraft/network/listener/ClientPlayPacketListener;)V
 */
package net.minecraft.network.packet.s2c.play;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.PacketType;
import net.minecraft.network.packet.PlayPackets;
import net.minecraft.recipe.book.RecipeBookOptions;

public record RecipeBookSettingsS2CPacket(RecipeBookOptions bookSettings) implements Packet<ClientPlayPacketListener>
{
    public static final PacketCodec<PacketByteBuf, RecipeBookSettingsS2CPacket> CODEC = PacketCodec.tuple(RecipeBookOptions.PACKET_CODEC, RecipeBookSettingsS2CPacket::bookSettings, RecipeBookSettingsS2CPacket::new);

    @Override
    public PacketType<RecipeBookSettingsS2CPacket> getPacketType() {
        return PlayPackets.RECIPE_BOOK_SETTINGS;
    }

    @Override
    public void apply(ClientPlayPacketListener arg) {
        arg.onRecipeBookSettings(this);
    }
}

