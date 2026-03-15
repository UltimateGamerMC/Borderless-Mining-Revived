package net.minecraft.sound;

import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;

public record BiomeMoodSound(RegistryEntry<SoundEvent> sound, int tickDelay, int blockSearchExtent, double offset) {
    public static final Codec<BiomeMoodSound> CODEC = RecordCodecBuilder.create(instance -> instance.group(((MapCodec)SoundEvent.ENTRY_CODEC.fieldOf("sound")).forGetter(sound -> sound.sound), ((MapCodec)Codec.INT.fieldOf("tick_delay")).forGetter(sound -> sound.tickDelay), ((MapCodec)Codec.INT.fieldOf("block_search_extent")).forGetter(sound -> sound.blockSearchExtent), ((MapCodec)Codec.DOUBLE.fieldOf("offset")).forGetter(sound -> sound.offset)).apply((Applicative<BiomeMoodSound, ?>)instance, BiomeMoodSound::new));
    public static final BiomeMoodSound CAVE = new BiomeMoodSound(SoundEvents.AMBIENT_CAVE, 6000, 8, 2.0);
}

