/*
 * External method calls:
 *   Lnet/minecraft/registry/entry/RegistryFixedCodec;of(Lnet/minecraft/registry/RegistryKey;)Lnet/minecraft/registry/entry/RegistryFixedCodec;
 *   Lnet/minecraft/network/codec/PacketCodecs;registryEntry(Lnet/minecraft/registry/RegistryKey;)Lnet/minecraft/network/codec/PacketCodec;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/entity/passive/WolfSoundVariant;createCodec()Lcom/mojang/serialization/Codec;
 */
package net.minecraft.entity.passive;

import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryFixedCodec;
import net.minecraft.sound.SoundEvent;

public record WolfSoundVariant(RegistryEntry<SoundEvent> ambientSound, RegistryEntry<SoundEvent> deathSound, RegistryEntry<SoundEvent> growlSound, RegistryEntry<SoundEvent> hurtSound, RegistryEntry<SoundEvent> pantSound, RegistryEntry<SoundEvent> whineSound) {
    public static final Codec<WolfSoundVariant> CODEC = WolfSoundVariant.createCodec();
    public static final Codec<WolfSoundVariant> NETWORK_CODEC = WolfSoundVariant.createCodec();
    public static final Codec<RegistryEntry<WolfSoundVariant>> ENTRY_CODEC = RegistryFixedCodec.of(RegistryKeys.WOLF_SOUND_VARIANT);
    public static final PacketCodec<RegistryByteBuf, RegistryEntry<WolfSoundVariant>> PACKET_CODEC = PacketCodecs.registryEntry(RegistryKeys.WOLF_SOUND_VARIANT);

    private static Codec<WolfSoundVariant> createCodec() {
        return RecordCodecBuilder.create(instance -> instance.group(((MapCodec)SoundEvent.ENTRY_CODEC.fieldOf("ambient_sound")).forGetter(WolfSoundVariant::ambientSound), ((MapCodec)SoundEvent.ENTRY_CODEC.fieldOf("death_sound")).forGetter(WolfSoundVariant::deathSound), ((MapCodec)SoundEvent.ENTRY_CODEC.fieldOf("growl_sound")).forGetter(WolfSoundVariant::growlSound), ((MapCodec)SoundEvent.ENTRY_CODEC.fieldOf("hurt_sound")).forGetter(WolfSoundVariant::hurtSound), ((MapCodec)SoundEvent.ENTRY_CODEC.fieldOf("pant_sound")).forGetter(WolfSoundVariant::pantSound), ((MapCodec)SoundEvent.ENTRY_CODEC.fieldOf("whine_sound")).forGetter(WolfSoundVariant::whineSound)).apply((Applicative<WolfSoundVariant, ?>)instance, WolfSoundVariant::new));
    }
}

