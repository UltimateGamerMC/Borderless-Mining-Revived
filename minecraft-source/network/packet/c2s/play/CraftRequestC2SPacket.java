/*
 * External method calls:
 *   Lnet/minecraft/network/listener/ServerPlayPacketListener;onCraftRequest(Lnet/minecraft/network/packet/c2s/play/CraftRequestC2SPacket;)V
 *   Lnet/minecraft/network/codec/PacketCodec;tuple(Lnet/minecraft/network/codec/PacketCodec;Ljava/util/function/Function;Lnet/minecraft/network/codec/PacketCodec;Ljava/util/function/Function;Lnet/minecraft/network/codec/PacketCodec;Ljava/util/function/Function;Lcom/mojang/datafixers/util/Function3;)Lnet/minecraft/network/codec/PacketCodec;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/network/packet/c2s/play/CraftRequestC2SPacket;apply(Lnet/minecraft/network/listener/ServerPlayPacketListener;)V
 */
package net.minecraft.network.packet.c2s.play;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.listener.ServerPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.PacketType;
import net.minecraft.network.packet.PlayPackets;
import net.minecraft.recipe.NetworkRecipeId;

public record CraftRequestC2SPacket(int syncId, NetworkRecipeId recipeId, boolean craftAll) implements Packet<ServerPlayPacketListener>
{
    public static final PacketCodec<PacketByteBuf, CraftRequestC2SPacket> CODEC = PacketCodec.tuple(PacketCodecs.SYNC_ID, CraftRequestC2SPacket::syncId, NetworkRecipeId.PACKET_CODEC, CraftRequestC2SPacket::recipeId, PacketCodecs.BOOLEAN, CraftRequestC2SPacket::craftAll, CraftRequestC2SPacket::new);

    @Override
    public PacketType<CraftRequestC2SPacket> getPacketType() {
        return PlayPackets.PLACE_RECIPE;
    }

    @Override
    public void apply(ServerPlayPacketListener arg) {
        arg.onCraftRequest(this);
    }
}

