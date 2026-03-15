package net.minecraft.sound;

import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.dynamic.Codecs;

public record MusicSound(RegistryEntry<SoundEvent> sound, int minDelay, int maxDelay, boolean replaceCurrentMusic) {
    public static final Codec<MusicSound> CODEC = RecordCodecBuilder.create(instance -> instance.group(((MapCodec)SoundEvent.ENTRY_CODEC.fieldOf("sound")).forGetter(MusicSound::sound), ((MapCodec)Codecs.NON_NEGATIVE_INT.fieldOf("min_delay")).forGetter(MusicSound::minDelay), ((MapCodec)Codecs.NON_NEGATIVE_INT.fieldOf("max_delay")).forGetter(MusicSound::maxDelay), Codec.BOOL.optionalFieldOf("replace_current_music", false).forGetter(MusicSound::replaceCurrentMusic)).apply((Applicative<MusicSound, ?>)instance, MusicSound::new));
}

