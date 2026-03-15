/*
 * External method calls:
 *   Lnet/minecraft/network/codec/PacketCodecs;registryValue(Lnet/minecraft/registry/RegistryKey;)Lnet/minecraft/network/codec/PacketCodec;
 *   Lnet/minecraft/network/codec/PacketCodec;dispatch(Ljava/util/function/Function;Ljava/util/function/Function;)Lnet/minecraft/network/codec/PacketCodec;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/recipe/display/RecipeDisplay;result()Lnet/minecraft/recipe/display/SlotDisplay;
 *   Lnet/minecraft/recipe/display/RecipeDisplay;craftingStation()Lnet/minecraft/recipe/display/SlotDisplay;
 */
package net.minecraft.recipe.display;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.recipe.display.SlotDisplay;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.resource.featuretoggle.FeatureSet;

public interface RecipeDisplay {
    public static final Codec<RecipeDisplay> CODEC = Registries.RECIPE_DISPLAY.getCodec().dispatch(RecipeDisplay::serializer, Serializer::codec);
    public static final PacketCodec<RegistryByteBuf, RecipeDisplay> PACKET_CODEC = PacketCodecs.registryValue(RegistryKeys.RECIPE_DISPLAY).dispatch(RecipeDisplay::serializer, Serializer::streamCodec);

    public SlotDisplay result();

    public SlotDisplay craftingStation();

    public Serializer<? extends RecipeDisplay> serializer();

    default public boolean isEnabled(FeatureSet features) {
        return this.result().isEnabled(features) && this.craftingStation().isEnabled(features);
    }

    public record Serializer<T extends RecipeDisplay>(MapCodec<T> codec, PacketCodec<RegistryByteBuf, T> streamCodec) {
    }
}

