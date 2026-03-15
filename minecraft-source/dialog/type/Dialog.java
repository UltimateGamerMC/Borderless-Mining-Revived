/*
 * External method calls:
 *   Lnet/minecraft/util/dynamic/Codecs;rangedInt(II)Lcom/mojang/serialization/Codec;
 *   Lnet/minecraft/registry/entry/RegistryElementCodec;of(Lnet/minecraft/registry/RegistryKey;Lcom/mojang/serialization/Codec;)Lnet/minecraft/registry/entry/RegistryElementCodec;
 *   Lnet/minecraft/registry/RegistryCodecs;entryList(Lnet/minecraft/registry/RegistryKey;Lcom/mojang/serialization/Codec;)Lcom/mojang/serialization/Codec;
 *   Lnet/minecraft/network/codec/PacketCodecs;unlimitedRegistryCodec(Lcom/mojang/serialization/Codec;)Lnet/minecraft/network/codec/PacketCodec;
 *   Lnet/minecraft/network/codec/PacketCodecs;registryEntry(Lnet/minecraft/registry/RegistryKey;Lnet/minecraft/network/codec/PacketCodec;)Lnet/minecraft/network/codec/PacketCodec;
 *   Lnet/minecraft/network/codec/PacketCodecs;unlimitedCodec(Lcom/mojang/serialization/Codec;)Lnet/minecraft/network/codec/PacketCodec;
 */
package net.minecraft.dialog.type;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import io.netty.buffer.ByteBuf;
import java.util.Optional;
import net.minecraft.dialog.DialogCommonData;
import net.minecraft.dialog.action.DialogAction;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryCodecs;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryElementCodec;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.util.dynamic.Codecs;

public interface Dialog {
    public static final Codec<Integer> WIDTH_CODEC = Codecs.rangedInt(1, 1024);
    public static final Codec<Dialog> CODEC = Registries.DIALOG_TYPE.getCodec().dispatch(Dialog::getCodec, mapCodec -> mapCodec);
    public static final Codec<RegistryEntry<Dialog>> ENTRY_CODEC = RegistryElementCodec.of(RegistryKeys.DIALOG, CODEC);
    public static final Codec<RegistryEntryList<Dialog>> ENTRY_LIST_CODEC = RegistryCodecs.entryList(RegistryKeys.DIALOG, CODEC);
    public static final PacketCodec<RegistryByteBuf, RegistryEntry<Dialog>> ENTRY_PACKET_CODEC = PacketCodecs.registryEntry(RegistryKeys.DIALOG, PacketCodecs.unlimitedRegistryCodec(CODEC));
    public static final PacketCodec<ByteBuf, Dialog> PACKET_CODEC = PacketCodecs.unlimitedCodec(CODEC);

    public DialogCommonData common();

    public MapCodec<? extends Dialog> getCodec();

    public Optional<DialogAction> getCancelAction();
}

