/*
 * External method calls:
 *   Lnet/minecraft/text/Text;translatable(Ljava/lang/String;)Lnet/minecraft/text/MutableText;
 *   Lnet/minecraft/client/gui/widget/OptionListWidget;addAll([Lnet/minecraft/client/option/SimpleOption;)V
 */
package net.minecraft.client.gui.screen.option;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.GameOptionsScreen;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.SimpleOption;
import net.minecraft.text.Text;

@Environment(value=EnvType.CLIENT)
public class FontOptionsScreen
extends GameOptionsScreen {
    private static SimpleOption<?>[] getOptions(GameOptions gameOptions) {
        return new SimpleOption[]{gameOptions.getForceUnicodeFont(), gameOptions.getJapaneseGlyphVariants()};
    }

    public FontOptionsScreen(Screen parent, GameOptions gameOptions) {
        super(parent, gameOptions, (Text)Text.translatable("options.font.title"));
    }

    @Override
    protected void addOptions() {
        this.body.addAll(FontOptionsScreen.getOptions(this.gameOptions));
    }
}

