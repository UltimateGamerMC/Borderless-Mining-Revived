/*
 * External method calls:
 *   Lnet/minecraft/client/gui/widget/OptionListWidget;addSingleOptionEntry(Lnet/minecraft/client/option/SimpleOption;)V
 *   Lnet/minecraft/client/gui/widget/OptionListWidget;addAll([Lnet/minecraft/client/option/SimpleOption;)V
 *   Lnet/minecraft/sound/SoundCategory;values()[Lnet/minecraft/sound/SoundCategory;
 *   Lnet/minecraft/text/Text;translatable(Ljava/lang/String;)Lnet/minecraft/text/MutableText;
 */
package net.minecraft.client.gui.screen.option;

import java.util.Arrays;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.GameOptionsScreen;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.SimpleOption;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;

@Environment(value=EnvType.CLIENT)
public class SoundOptionsScreen
extends GameOptionsScreen {
    private static final Text TITLE_TEXT = Text.translatable("options.sounds.title");

    public SoundOptionsScreen(Screen parent, GameOptions options) {
        super(parent, options, TITLE_TEXT);
    }

    @Override
    protected void addOptions() {
        this.body.addSingleOptionEntry(this.gameOptions.getSoundVolumeOption(SoundCategory.MASTER));
        this.body.addAll(this.getVolumeOptions());
        this.body.addSingleOptionEntry(this.gameOptions.getSoundDevice());
        this.body.addAll(this.gameOptions.getShowSubtitles(), this.gameOptions.getDirectionalAudio());
        this.body.addAll(this.gameOptions.getMusicFrequency(), this.gameOptions.getMusicToast());
    }

    private SimpleOption<?>[] getVolumeOptions() {
        return (SimpleOption[])Arrays.stream(SoundCategory.values()).filter(category -> category != SoundCategory.MASTER).map(this.gameOptions::getSoundVolumeOption).toArray(SimpleOption[]::new);
    }
}

