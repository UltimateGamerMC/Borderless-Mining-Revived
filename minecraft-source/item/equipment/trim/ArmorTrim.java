/*
 * External method calls:
 *   Lnet/minecraft/screen/ScreenTexts;space()Lnet/minecraft/text/MutableText;
 *   Lnet/minecraft/text/MutableText;append(Lnet/minecraft/text/Text;)Lnet/minecraft/text/MutableText;
 *   Lnet/minecraft/item/equipment/trim/ArmorTrimMaterial;description()Lnet/minecraft/text/Text;
 *   Lnet/minecraft/item/equipment/trim/ArmorTrimMaterial;assets()Lnet/minecraft/item/equipment/trim/ArmorTrimAssets;
 *   Lnet/minecraft/item/equipment/trim/ArmorTrimPattern;assetId()Lnet/minecraft/util/Identifier;
 *   Lnet/minecraft/util/Identifier;withPath(Ljava/util/function/UnaryOperator;)Lnet/minecraft/util/Identifier;
 *   Lnet/minecraft/item/equipment/trim/ArmorTrimAssets$AssetId;suffix()Ljava/lang/String;
 *   Lnet/minecraft/network/codec/PacketCodec;tuple(Lnet/minecraft/network/codec/PacketCodec;Ljava/util/function/Function;Lnet/minecraft/network/codec/PacketCodec;Ljava/util/function/Function;Ljava/util/function/BiFunction;)Lnet/minecraft/network/codec/PacketCodec;
 *   Lnet/minecraft/util/Identifier;ofVanilla(Ljava/lang/String;)Lnet/minecraft/util/Identifier;
 *   Lnet/minecraft/util/Util;createTranslationKey(Ljava/lang/String;Lnet/minecraft/util/Identifier;)Ljava/lang/String;
 *   Lnet/minecraft/text/Text;translatable(Ljava/lang/String;)Lnet/minecraft/text/MutableText;
 *   Lnet/minecraft/text/MutableText;formatted(Lnet/minecraft/util/Formatting;)Lnet/minecraft/text/MutableText;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/item/equipment/trim/ArmorTrim;material()Lnet/minecraft/registry/entry/RegistryEntry;
 *   Lnet/minecraft/item/equipment/trim/ArmorTrim;pattern()Lnet/minecraft/registry/entry/RegistryEntry;
 */
package net.minecraft.item.equipment.trim;

import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.function.Consumer;
import net.minecraft.component.ComponentsAccess;
import net.minecraft.item.Item;
import net.minecraft.item.equipment.EquipmentAsset;
import net.minecraft.item.equipment.trim.ArmorTrimAssets;
import net.minecraft.item.equipment.trim.ArmorTrimMaterial;
import net.minecraft.item.equipment.trim.ArmorTrimPattern;
import net.minecraft.item.tooltip.TooltipAppender;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;

public record ArmorTrim(RegistryEntry<ArmorTrimMaterial> material, RegistryEntry<ArmorTrimPattern> pattern) implements TooltipAppender
{
    public static final Codec<ArmorTrim> CODEC = RecordCodecBuilder.create(instance -> instance.group(((MapCodec)ArmorTrimMaterial.ENTRY_CODEC.fieldOf("material")).forGetter(ArmorTrim::material), ((MapCodec)ArmorTrimPattern.ENTRY_CODEC.fieldOf("pattern")).forGetter(ArmorTrim::pattern)).apply((Applicative<ArmorTrim, ?>)instance, ArmorTrim::new));
    public static final PacketCodec<RegistryByteBuf, ArmorTrim> PACKET_CODEC = PacketCodec.tuple(ArmorTrimMaterial.ENTRY_PACKET_CODEC, ArmorTrim::material, ArmorTrimPattern.ENTRY_PACKET_CODEC, ArmorTrim::pattern, ArmorTrim::new);
    private static final Text UPGRADE_TEXT = Text.translatable(Util.createTranslationKey("item", Identifier.ofVanilla("smithing_template.upgrade"))).formatted(Formatting.GRAY);

    @Override
    public void appendTooltip(Item.TooltipContext context, Consumer<Text> textConsumer, TooltipType type, ComponentsAccess components) {
        textConsumer.accept(UPGRADE_TEXT);
        textConsumer.accept(ScreenTexts.space().append(this.pattern.value().getDescription(this.material)));
        textConsumer.accept(ScreenTexts.space().append(this.material.value().description()));
    }

    public Identifier getTextureId(String trimsDirectory, RegistryKey<EquipmentAsset> equipmentAsset) {
        ArmorTrimAssets.AssetId lv = this.material().value().assets().getAssetId(equipmentAsset);
        return this.pattern().value().assetId().withPath(patternId -> trimsDirectory + "/" + patternId + "_" + lv.suffix());
    }
}

