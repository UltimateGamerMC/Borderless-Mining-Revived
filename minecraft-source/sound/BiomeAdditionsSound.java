package net.minecraft.sound;

import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.sound.SoundEvent;

public record BiomeAdditionsSound(RegistryEntry<SoundEvent> sound, double tickChance) {
    public static final Codec<BiomeAdditionsSound> CODEC = RecordCodecBuilder.create(instance -> instance.group(((MapCodec)SoundEvent.ENTRY_CODEC.fieldOf("sound")).forGetter(sound -> sound.sound), ((MapCodec)Codec.DOUBLE.fieldOf("tick_chance")).forGetter(sound -> sound.tickChance)).apply((Applicative<BiomeAdditionsSound, ?>)instance, BiomeAdditionsSound::new));
}

