/*
 * External method calls:
 *   Lnet/minecraft/screen/ScreenTexts;joinSentences([Lnet/minecraft/text/Text;)Lnet/minecraft/text/MutableText;
 *   Lnet/minecraft/client/font/MultilineText;create(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/text/Text;I)Lnet/minecraft/client/font/MultilineText;
 *   Lnet/minecraft/client/gui/widget/ButtonWidget;builder(Lnet/minecraft/text/Text;Lnet/minecraft/client/gui/widget/ButtonWidget$PressAction;)Lnet/minecraft/client/gui/widget/ButtonWidget$Builder;
 *   Lnet/minecraft/client/gui/widget/ButtonWidget$Builder;dimensions(IIII)Lnet/minecraft/client/gui/widget/ButtonWidget$Builder;
 *   Lnet/minecraft/client/gui/widget/ButtonWidget$Builder;build()Lnet/minecraft/client/gui/widget/ButtonWidget;
 *   Lnet/minecraft/client/gui/screen/Screen;render(Lnet/minecraft/client/gui/DrawContext;IIF)V
 *   Lnet/minecraft/client/gui/DrawContext;drawCenteredTextWithShadow(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/text/Text;III)V
 *   Lnet/minecraft/client/font/MultilineText;draw(Lnet/minecraft/client/font/Alignment;IIILnet/minecraft/client/font/DrawnTextConsumer;)I
 *
 * Internal private/static methods:
 *   Lnet/minecraft/client/gui/screen/NoticeScreen;addDrawableChild(Lnet/minecraft/client/gui/Element;)Lnet/minecraft/client/gui/Element;
 */
package net.minecraft.client.gui.screen;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.font.Alignment;
import net.minecraft.client.font.DrawnTextConsumer;
import net.minecraft.client.font.MultilineText;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.minecraft.util.Colors;
import net.minecraft.util.math.MathHelper;

@Environment(value=EnvType.CLIENT)
public class NoticeScreen
extends Screen {
    private static final int NOTICE_TEXT_Y = 90;
    private final Text notice;
    private MultilineText noticeLines = MultilineText.EMPTY;
    private final Runnable actionHandler;
    private final Text buttonText;
    private final boolean shouldCloseOnEsc;

    public NoticeScreen(Runnable actionHandler, Text title, Text notice) {
        this(actionHandler, title, notice, ScreenTexts.BACK, true);
    }

    public NoticeScreen(Runnable actionHandler, Text title, Text notice, Text buttonText, boolean shouldCloseOnEsc) {
        super(title);
        this.actionHandler = actionHandler;
        this.notice = notice;
        this.buttonText = buttonText;
        this.shouldCloseOnEsc = shouldCloseOnEsc;
    }

    @Override
    public Text getNarratedTitle() {
        return ScreenTexts.joinSentences(super.getNarratedTitle(), this.notice);
    }

    @Override
    protected void init() {
        super.init();
        this.noticeLines = MultilineText.create(this.textRenderer, this.notice, this.width - 50);
        int i = this.noticeLines.getLineCount() * this.textRenderer.fontHeight;
        int j = MathHelper.clamp(90 + i + 12, this.height / 6 + 96, this.height - 24);
        int k = 150;
        this.addDrawableChild(ButtonWidget.builder(this.buttonText, button -> this.actionHandler.run()).dimensions((this.width - 150) / 2, j, 150, 20).build());
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float deltaTicks) {
        super.render(context, mouseX, mouseY, deltaTicks);
        DrawnTextConsumer lv = context.getTextConsumer();
        context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, 70, Colors.WHITE);
        this.noticeLines.draw(Alignment.CENTER, this.width / 2, 90, this.textRenderer.fontHeight, lv);
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return this.shouldCloseOnEsc;
    }
}

