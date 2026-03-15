/*
 * External method calls:
 *   Lnet/minecraft/client/gui/widget/DirectionalLayoutWidget;horizontal()Lnet/minecraft/client/gui/widget/DirectionalLayoutWidget;
 *   Lnet/minecraft/client/gui/widget/DirectionalLayoutWidget;spacing(I)Lnet/minecraft/client/gui/widget/DirectionalLayoutWidget;
 *   Lnet/minecraft/client/gui/widget/ButtonWidget;builder(Lnet/minecraft/text/Text;Lnet/minecraft/client/gui/widget/ButtonWidget$PressAction;)Lnet/minecraft/client/gui/widget/ButtonWidget$Builder;
 *   Lnet/minecraft/client/gui/widget/ButtonWidget$Builder;build()Lnet/minecraft/client/gui/widget/ButtonWidget;
 *   Lnet/minecraft/text/Text;translatable(Ljava/lang/String;)Lnet/minecraft/text/MutableText;
 *   Lnet/minecraft/text/MutableText;formatted(Lnet/minecraft/util/Formatting;)Lnet/minecraft/text/MutableText;
 *   Lnet/minecraft/text/MutableText;withColor(I)Lnet/minecraft/text/MutableText;
 *   Lnet/minecraft/text/MutableText;append(Ljava/lang/String;)Lnet/minecraft/text/MutableText;
 *   Lnet/minecraft/text/MutableText;append(Lnet/minecraft/text/Text;)Lnet/minecraft/text/MutableText;
 */
package net.minecraft.client.gui.screen.multiplayer;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.WarningScreen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.DirectionalLayoutWidget;
import net.minecraft.client.gui.widget.LayoutWidget;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.minecraft.util.Colors;
import net.minecraft.util.Formatting;

@Environment(value=EnvType.CLIENT)
public class MultiplayerWarningScreen
extends WarningScreen {
    private static final Text HEADER = Text.translatable("multiplayerWarning.header").formatted(Formatting.BOLD);
    private static final Text MESSAGE = Text.translatable("multiplayerWarning.message");
    private static final Text CHECK_MESSAGE = Text.translatable("multiplayerWarning.check").withColor(Colors.LIGHTER_GRAY);
    private static final Text NARRATED_TEXT = HEADER.copy().append("\n").append(MESSAGE);
    private final Screen parent;

    public MultiplayerWarningScreen(Screen parent) {
        super(HEADER, MESSAGE, CHECK_MESSAGE, NARRATED_TEXT);
        this.parent = parent;
    }

    @Override
    protected LayoutWidget getLayout() {
        DirectionalLayoutWidget lv = DirectionalLayoutWidget.horizontal().spacing(8);
        lv.add(ButtonWidget.builder(ScreenTexts.PROCEED, button -> {
            if (this.checkbox.isChecked()) {
                this.client.options.skipMultiplayerWarning = true;
                this.client.options.write();
            }
            this.client.setScreen(new MultiplayerScreen(this.parent));
        }).build());
        lv.add(ButtonWidget.builder(ScreenTexts.BACK, button -> this.close()).build());
        return lv;
    }

    @Override
    public void close() {
        this.client.setScreen(this.parent);
    }
}

