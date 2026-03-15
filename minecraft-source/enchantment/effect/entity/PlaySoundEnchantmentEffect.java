/*
 * External method calls:
 *   Lnet/minecraft/server/world/ServerWorld;playSound(Lnet/minecraft/entity/Entity;DDDLnet/minecraft/registry/entry/RegistryEntry;Lnet/minecraft/sound/SoundCategory;FF)V
 *   Lnet/minecraft/util/dynamic/Codecs;listOrSingle(Lcom/mojang/serialization/Codec;Lcom/mojang/serialization/Codec;)Lcom/mojang/serialization/Codec;
 */
package net.minecraft.enchantment.effect.entity;

import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.List;
import net.minecraft.enchantment.EnchantmentEffectContext;
import net.minecraft.enchantment.effect.EnchantmentEntityEffect;
import net.minecraft.entity.Entity;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.dynamic.Codecs;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.floatprovider.FloatProvider;
import net.minecraft.util.math.random.Random;

public record PlaySoundEnchantmentEffect(List<RegistryEntry<SoundEvent>> soundEvents, FloatProvider volume, FloatProvider pitch) implements EnchantmentEntityEffect
{
    public static final MapCodec<PlaySoundEnchantmentEffect> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(((MapCodec)Codecs.listOrSingle(SoundEvent.ENTRY_CODEC, SoundEvent.ENTRY_CODEC.sizeLimitedListOf(255)).fieldOf("sound")).forGetter(PlaySoundEnchantmentEffect::soundEvents), ((MapCodec)FloatProvider.createValidatedCodec(1.0E-5f, 10.0f).fieldOf("volume")).forGetter(PlaySoundEnchantmentEffect::volume), ((MapCodec)FloatProvider.createValidatedCodec(1.0E-5f, 2.0f).fieldOf("pitch")).forGetter(PlaySoundEnchantmentEffect::pitch)).apply((Applicative<PlaySoundEnchantmentEffect, ?>)instance, PlaySoundEnchantmentEffect::new));

    @Override
    public void apply(ServerWorld world, int level, EnchantmentEffectContext context, Entity user, Vec3d pos) {
        if (user.isSilent()) {
            return;
        }
        Random lv = user.getRandom();
        int j = MathHelper.clamp(level - 1, 0, this.soundEvents.size() - 1);
        world.playSound(null, pos.getX(), pos.getY(), pos.getZ(), this.soundEvents.get(j), user.getSoundCategory(), this.volume.get(lv), this.pitch.get(lv));
    }

    public MapCodec<PlaySoundEnchantmentEffect> getCodec() {
        return CODEC;
    }
}

