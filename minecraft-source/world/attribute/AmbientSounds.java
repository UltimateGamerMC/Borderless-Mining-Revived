/*
 * External method calls:
 *   Lnet/minecraft/util/dynamic/Codecs;listOrSingle(Lcom/mojang/serialization/Codec;)Lcom/mojang/serialization/Codec;
 */
package net.minecraft.world.attribute;

import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.List;
import java.util.Optional;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.sound.BiomeAdditionsSound;
import net.minecraft.sound.BiomeMoodSound;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.dynamic.Codecs;

public record AmbientSounds(Optional<RegistryEntry<SoundEvent>> loop, Optional<BiomeMoodSound> mood, List<BiomeAdditionsSound> additions) {
    public static final AmbientSounds DEFAULT = new AmbientSounds(Optional.empty(), Optional.empty(), List.of());
    public static final AmbientSounds CAVE = new AmbientSounds(Optional.empty(), Optional.of(BiomeMoodSound.CAVE), List.of());
    public static final Codec<AmbientSounds> CODEC = RecordCodecBuilder.create(instance -> instance.group(SoundEvent.ENTRY_CODEC.optionalFieldOf("loop").forGetter(AmbientSounds::loop), BiomeMoodSound.CODEC.optionalFieldOf("mood").forGetter(AmbientSounds::mood), Codecs.listOrSingle(BiomeAdditionsSound.CODEC).optionalFieldOf("additions", List.of()).forGetter(AmbientSounds::additions)).apply((Applicative<AmbientSounds, ?>)instance, AmbientSounds::new));
}

