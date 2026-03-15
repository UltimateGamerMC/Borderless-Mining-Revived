/*
 * External method calls:
 *   Lnet/minecraft/world/attribute/AmbientSounds;loop()Ljava/util/Optional;
 *   Lnet/minecraft/world/attribute/AmbientSounds;additions()Ljava/util/List;
 *   Lnet/minecraft/sound/BiomeAdditionsSound;sound()Lnet/minecraft/registry/entry/RegistryEntry;
 *   Lnet/minecraft/client/sound/PositionedSoundInstance;ambient(Lnet/minecraft/sound/SoundEvent;)Lnet/minecraft/client/sound/PositionedSoundInstance;
 *   Lnet/minecraft/client/sound/SoundManager;play(Lnet/minecraft/client/sound/SoundInstance;)Lnet/minecraft/client/sound/SoundSystem$PlayResult;
 *   Lnet/minecraft/world/attribute/AmbientSounds;mood()Ljava/util/Optional;
 *   Lnet/minecraft/sound/BiomeMoodSound;sound()Lnet/minecraft/registry/entry/RegistryEntry;
 *   Lnet/minecraft/client/sound/PositionedSoundInstance;ambient(Lnet/minecraft/sound/SoundEvent;Lnet/minecraft/util/math/random/Random;DDD)Lnet/minecraft/client/sound/PositionedSoundInstance;
 */
package net.minecraft.client.sound;

import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import java.util.Objects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.sound.MovingSoundInstance;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.client.sound.SoundManager;
import net.minecraft.client.util.ClientPlayerTickable;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.sound.BiomeAdditionsSound;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.LightType;
import net.minecraft.world.World;
import net.minecraft.world.attribute.AmbientSounds;
import net.minecraft.world.attribute.EnvironmentAttributes;
import net.minecraft.world.attribute.WorldEnvironmentAttributeAccess;
import org.jspecify.annotations.Nullable;

@Environment(value=EnvType.CLIENT)
public class BiomeEffectSoundPlayer
implements ClientPlayerTickable {
    private static final int MAX_STRENGTH = 40;
    private static final float field_32995 = 0.001f;
    private final ClientPlayerEntity player;
    private final SoundManager soundManager;
    private final Random random;
    private final Object2ObjectArrayMap<RegistryEntry<SoundEvent>, MusicLoop> soundLoops = new Object2ObjectArrayMap();
    private float moodPercentage;
    private @Nullable RegistryEntry<SoundEvent> field_63919;

    public BiomeEffectSoundPlayer(ClientPlayerEntity player, SoundManager soundManager) {
        this.random = player.getEntityWorld().getRandom();
        this.player = player;
        this.soundManager = soundManager;
    }

    public float getMoodPercentage() {
        return this.moodPercentage;
    }

    @Override
    public void tick() {
        this.soundLoops.values().removeIf(MovingSoundInstance::isDone);
        World lv = this.player.getEntityWorld();
        WorldEnvironmentAttributeAccess lv2 = lv.getEnvironmentAttributes();
        AmbientSounds lv3 = lv2.getAttributeValue(EnvironmentAttributes.AMBIENT_SOUNDS_AUDIO, this.player.getEntityPos());
        RegistryEntry lv4 = lv3.loop().orElse(null);
        if (!Objects.equals(lv4, this.field_63919)) {
            this.field_63919 = lv4;
            this.soundLoops.values().forEach(MusicLoop::fadeOut);
            if (lv4 != null) {
                this.soundLoops.compute(lv4, (arg2, loop) -> {
                    if (loop == null) {
                        loop = new MusicLoop((SoundEvent)lv4.value());
                        this.soundManager.play((SoundInstance)loop);
                    }
                    loop.fadeIn();
                    return loop;
                });
            }
        }
        for (BiomeAdditionsSound lv5 : lv3.additions()) {
            if (!(this.random.nextDouble() < lv5.tickChance())) continue;
            this.soundManager.play(PositionedSoundInstance.ambient(lv5.sound().value()));
        }
        lv3.mood().ifPresent(arg2 -> {
            int i = arg2.blockSearchExtent() * 2 + 1;
            BlockPos lv = BlockPos.ofFloored(this.player.getX() + (double)this.random.nextInt(i) - (double)arg2.blockSearchExtent(), this.player.getEyeY() + (double)this.random.nextInt(i) - (double)arg2.blockSearchExtent(), this.player.getZ() + (double)this.random.nextInt(i) - (double)arg2.blockSearchExtent());
            int j = lv.getLightLevel(LightType.SKY, lv);
            this.moodPercentage = j > 0 ? (this.moodPercentage -= (float)j / 15.0f * 0.001f) : (this.moodPercentage -= (float)(lv.getLightLevel(LightType.BLOCK, lv) - 1) / (float)arg2.tickDelay());
            if (this.moodPercentage >= 1.0f) {
                double d = (double)lv.getX() + 0.5;
                double e = (double)lv.getY() + 0.5;
                double f = (double)lv.getZ() + 0.5;
                double g = d - this.player.getX();
                double h = e - this.player.getEyeY();
                double k = f - this.player.getZ();
                double l = Math.sqrt(g * g + h * h + k * k);
                double m = l + arg2.offset();
                PositionedSoundInstance lv2 = PositionedSoundInstance.ambient(arg2.sound().value(), this.random, this.player.getX() + g / l * m, this.player.getEyeY() + h / l * m, this.player.getZ() + k / l * m);
                this.soundManager.play(lv2);
                this.moodPercentage = 0.0f;
            } else {
                this.moodPercentage = Math.max(this.moodPercentage, 0.0f);
            }
        });
    }

    @Environment(value=EnvType.CLIENT)
    public static class MusicLoop
    extends MovingSoundInstance {
        private int delta;
        private int strength;

        public MusicLoop(SoundEvent sound) {
            super(sound, SoundCategory.AMBIENT, SoundInstance.createRandom());
            this.repeat = true;
            this.repeatDelay = 0;
            this.volume = 1.0f;
            this.relative = true;
        }

        @Override
        public void tick() {
            if (this.strength < 0) {
                this.setDone();
            }
            this.strength += this.delta;
            this.volume = MathHelper.clamp((float)this.strength / 40.0f, 0.0f, 1.0f);
        }

        public void fadeOut() {
            this.strength = Math.min(this.strength, 40);
            this.delta = -1;
        }

        public void fadeIn() {
            this.strength = Math.max(0, this.strength);
            this.delta = 1;
        }
    }
}

