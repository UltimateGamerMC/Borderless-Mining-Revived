/*
 * External method calls:
 *   Lnet/minecraft/text/Text;literal(Ljava/lang/String;)Lnet/minecraft/text/MutableText;
 *   Lnet/minecraft/text/MutableText;formatted(Lnet/minecraft/util/Formatting;)Lnet/minecraft/text/MutableText;
 *   Lnet/minecraft/text/MutableText;append(Lnet/minecraft/text/Text;)Lnet/minecraft/text/MutableText;
 *   Lnet/minecraft/text/Text;translatable(Ljava/lang/String;)Lnet/minecraft/text/MutableText;
 *   Lnet/minecraft/client/sound/SoundContainer;preload(Lnet/minecraft/client/sound/SoundSystem;)V
 */
package net.minecraft.client.sound;

import com.google.common.collect.Lists;
import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.SharedConstants;
import net.minecraft.client.sound.Sound;
import net.minecraft.client.sound.SoundContainer;
import net.minecraft.client.sound.SoundManager;
import net.minecraft.client.sound.SoundSystem;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.random.Random;
import org.jspecify.annotations.Nullable;

@Environment(value=EnvType.CLIENT)
public class WeightedSoundSet
implements SoundContainer<Sound> {
    private final List<SoundContainer<Sound>> sounds = Lists.newArrayList();
    private final @Nullable Text subtitle;

    public WeightedSoundSet(Identifier id, @Nullable String subtitle) {
        if (SharedConstants.SUBTITLES) {
            MutableText lv = Text.literal(id.getPath());
            if ("FOR THE DEBUG!".equals(subtitle)) {
                lv = lv.append(Text.literal(" missing").formatted(Formatting.RED));
            }
            this.subtitle = lv;
        } else {
            this.subtitle = subtitle == null ? null : Text.translatable(subtitle);
        }
    }

    @Override
    public int getWeight() {
        int i = 0;
        for (SoundContainer<Sound> lv : this.sounds) {
            i += lv.getWeight();
        }
        return i;
    }

    @Override
    public Sound getSound(Random arg) {
        int i = this.getWeight();
        if (this.sounds.isEmpty() || i == 0) {
            return SoundManager.MISSING_SOUND;
        }
        int j = arg.nextInt(i);
        for (SoundContainer<Sound> lv : this.sounds) {
            if ((j -= lv.getWeight()) >= 0) continue;
            return lv.getSound(arg);
        }
        return SoundManager.MISSING_SOUND;
    }

    public void add(SoundContainer<Sound> container) {
        this.sounds.add(container);
    }

    public @Nullable Text getSubtitle() {
        return this.subtitle;
    }

    @Override
    public void preload(SoundSystem soundSystem) {
        for (SoundContainer<Sound> lv : this.sounds) {
            lv.preload(soundSystem);
        }
    }

    @Override
    public /* synthetic */ Object getSound(Random random) {
        return this.getSound(random);
    }
}

