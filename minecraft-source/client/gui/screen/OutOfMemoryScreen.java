/*
 * External method calls:
 *   Lnet/minecraft/client/gui/widget/ThreePartsLayoutWidget;addHeader(Lnet/minecraft/text/Text;Lnet/minecraft/client/font/TextRenderer;)V
 *   Lnet/minecraft/client/gui/widget/NarratedMultilineTextWidget;builder(Lnet/minecraft/text/Text;Lnet/minecraft/client/font/TextRenderer;)Lnet/minecraft/client/gui/widget/NarratedMultilineTextWidget$Builder;
 *   Lnet/minecraft/client/gui/widget/NarratedMultilineTextWidget$Builder;width(I)Lnet/minecraft/client/gui/widget/NarratedMultilineTextWidget$Builder;
 *   Lnet/minecraft/client/gui/widget/NarratedMultilineTextWidget$Builder;build()Lnet/minecraft/client/gui/widget/NarratedMultilineTextWidget;
 *   Lnet/minecraft/client/gui/widget/ThreePartsLayoutWidget;addBody(Lnet/minecraft/client/gui/widget/Widget;)Lnet/minecraft/client/gui/widget/Widget;
 *   Lnet/minecraft/client/gui/widget/DirectionalLayoutWidget;horizontal()Lnet/minecraft/client/gui/widget/DirectionalLayoutWidget;
 *   Lnet/minecraft/client/gui/widget/DirectionalLayoutWidget;spacing(I)Lnet/minecraft/client/gui/widget/DirectionalLayoutWidget;
 *   Lnet/minecraft/client/gui/widget/ThreePartsLayoutWidget;addFooter(Lnet/minecraft/client/gui/widget/Widget;)Lnet/minecraft/client/gui/widget/Widget;
 *   Lnet/minecraft/client/gui/widget/ButtonWidget;builder(Lnet/minecraft/text/Text;Lnet/minecraft/client/gui/widget/ButtonWidget$PressAction;)Lnet/minecraft/client/gui/widget/ButtonWidget$Builder;
 *   Lnet/minecraft/client/gui/widget/ButtonWidget$Builder;build()Lnet/minecraft/client/gui/widget/ButtonWidget;
 *   Lnet/minecraft/text/Text;translatable(Ljava/lang/String;)Lnet/minecraft/text/MutableText;
 *   Lnet/minecraft/client/gui/widget/ThreePartsLayoutWidget;forEachChild(Ljava/util/function/Consumer;)V
 */
package net.minecraft.client.gui.screen;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.DirectionalLayoutWidget;
import net.minecraft.client.gui.widget.NarratedMultilineTextWidget;
import net.minecraft.client.gui.widget.ThreePartsLayoutWidget;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;

@Environment(value=EnvType.CLIENT)
public class OutOfMemoryScreen
extends Screen {
    private static final Text TITLE = Text.translatable("outOfMemory.title");
    private static final Text MESSAGE = Text.translatable("outOfMemory.message");
    private static final int MAX_TEXT_WIDTH = 300;
    private final ThreePartsLayoutWidget layout = new ThreePartsLayoutWidget(this);

    public OutOfMemoryScreen() {
        super(TITLE);
    }

    @Override
    protected void init() {
        this.layout.addHeader(TITLE, this.textRenderer);
        this.layout.addBody(NarratedMultilineTextWidget.builder(MESSAGE, this.textRenderer).width(300).build());
        DirectionalLayoutWidget lv = this.layout.addFooter(DirectionalLayoutWidget.horizontal().spacing(8));
        lv.add(ButtonWidget.builder(ScreenTexts.TO_TITLE, button -> this.client.setScreen(new TitleScreen())).build());
        lv.add(ButtonWidget.builder(Text.translatable("menu.quit"), button -> this.client.scheduleStop()).build());
        this.layout.forEachChild(this::addDrawableChild);
        this.refreshWidgetPositions();
    }

    @Override
    protected void refreshWidgetPositions() {
        this.layout.refreshPositions();
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return false;
    }
}

