/*
 * External method calls:
 *   Lnet/minecraft/network/codec/PacketCodec;tuple(Lnet/minecraft/network/codec/PacketCodec;Ljava/util/function/Function;Lnet/minecraft/network/codec/PacketCodec;Ljava/util/function/Function;Lnet/minecraft/network/codec/PacketCodec;Ljava/util/function/Function;Lnet/minecraft/network/codec/PacketCodec;Ljava/util/function/Function;Lcom/mojang/datafixers/util/Function4;)Lnet/minecraft/network/codec/PacketCodec;
 *   Lnet/minecraft/registry/entry/RegistryElementCodec;of(Lnet/minecraft/registry/RegistryKey;Lcom/mojang/serialization/Codec;)Lnet/minecraft/registry/entry/RegistryElementCodec;
 *   Lnet/minecraft/network/codec/PacketCodecs;registryEntry(Lnet/minecraft/registry/RegistryKey;Lnet/minecraft/network/codec/PacketCodec;)Lnet/minecraft/network/codec/PacketCodec;
 */
package net.minecraft.item;

import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryElementCodec;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.Text;
import net.minecraft.text.TextCodecs;
import net.minecraft.util.dynamic.Codecs;

public record Instrument(RegistryEntry<SoundEvent> soundEvent, float useDuration, float range, Text description) {
    public static final Codec<Instrument> CODEC = RecordCodecBuilder.create(instance -> instance.group(((MapCodec)SoundEvent.ENTRY_CODEC.fieldOf("sound_event")).forGetter(Instrument::soundEvent), ((MapCodec)Codecs.POSITIVE_FLOAT.fieldOf("use_duration")).forGetter(Instrument::useDuration), ((MapCodec)Codecs.POSITIVE_FLOAT.fieldOf("range")).forGetter(Instrument::range), ((MapCodec)TextCodecs.CODEC.fieldOf("description")).forGetter(Instrument::description)).apply((Applicative<Instrument, ?>)instance, Instrument::new));
    public static final PacketCodec<RegistryByteBuf, Instrument> PACKET_CODEC = PacketCodec.tuple(SoundEvent.ENTRY_PACKET_CODEC, Instrument::soundEvent, PacketCodecs.FLOAT, Instrument::useDuration, PacketCodecs.FLOAT, Instrument::range, TextCodecs.REGISTRY_PACKET_CODEC, Instrument::description, Instrument::new);
    public static final Codec<RegistryEntry<Instrument>> ENTRY_CODEC = RegistryElementCodec.of(RegistryKeys.INSTRUMENT, CODEC);
    public static final PacketCodec<RegistryByteBuf, RegistryEntry<Instrument>> ENTRY_PACKET_CODEC = PacketCodecs.registryEntry(RegistryKeys.INSTRUMENT, PACKET_CODEC);
}

