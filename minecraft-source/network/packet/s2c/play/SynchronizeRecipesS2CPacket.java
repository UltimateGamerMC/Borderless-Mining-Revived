/*
 * External method calls:
 *   Lnet/minecraft/network/listener/ClientPlayPacketListener;onSynchronizeRecipes(Lnet/minecraft/network/packet/s2c/play/SynchronizeRecipesS2CPacket;)V
 *   Lnet/minecraft/registry/RegistryKey;createPacketCodec(Lnet/minecraft/registry/RegistryKey;)Lnet/minecraft/network/codec/PacketCodec;
 *   Lnet/minecraft/network/codec/PacketCodecs;map(Ljava/util/function/IntFunction;Lnet/minecraft/network/codec/PacketCodec;Lnet/minecraft/network/codec/PacketCodec;)Lnet/minecraft/network/codec/PacketCodec;
 *   Lnet/minecraft/recipe/display/CuttingRecipeDisplay$Grouping;codec()Lnet/minecraft/network/codec/PacketCodec;
 *   Lnet/minecraft/network/codec/PacketCodec;tuple(Lnet/minecraft/network/codec/PacketCodec;Ljava/util/function/Function;Lnet/minecraft/network/codec/PacketCodec;Ljava/util/function/Function;Ljava/util/function/BiFunction;)Lnet/minecraft/network/codec/PacketCodec;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/network/packet/s2c/play/SynchronizeRecipesS2CPacket;apply(Lnet/minecraft/network/listener/ClientPlayPacketListener;)V
 */
package net.minecraft.network.packet.s2c.play;

import java.util.HashMap;
import java.util.Map;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.PacketType;
import net.minecraft.network.packet.PlayPackets;
import net.minecraft.recipe.RecipePropertySet;
import net.minecraft.recipe.StonecuttingRecipe;
import net.minecraft.recipe.display.CuttingRecipeDisplay;
import net.minecraft.registry.RegistryKey;

public record SynchronizeRecipesS2CPacket(Map<RegistryKey<RecipePropertySet>, RecipePropertySet> itemSets, CuttingRecipeDisplay.Grouping<StonecuttingRecipe> stonecutterRecipes) implements Packet<ClientPlayPacketListener>
{
    public static final PacketCodec<RegistryByteBuf, SynchronizeRecipesS2CPacket> CODEC = PacketCodec.tuple(PacketCodecs.map(HashMap::new, RegistryKey.createPacketCodec(RecipePropertySet.REGISTRY), RecipePropertySet.PACKET_CODEC), SynchronizeRecipesS2CPacket::itemSets, CuttingRecipeDisplay.Grouping.codec(), SynchronizeRecipesS2CPacket::stonecutterRecipes, SynchronizeRecipesS2CPacket::new);

    @Override
    public PacketType<SynchronizeRecipesS2CPacket> getPacketType() {
        return PlayPackets.UPDATE_RECIPES;
    }

    @Override
    public void apply(ClientPlayPacketListener arg) {
        arg.onSynchronizeRecipes(this);
    }
}

