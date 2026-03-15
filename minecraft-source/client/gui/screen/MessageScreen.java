/*
 * External method calls:
 *   Lnet/minecraft/client/gui/widget/NarratedMultilineTextWidget;builder(Lnet/minecraft/text/Text;Lnet/minecraft/client/font/TextRenderer;I)Lnet/minecraft/client/gui/widget/NarratedMultilineTextWidget$Builder;
 *   Lnet/minecraft/client/gui/widget/NarratedMultilineTextWidget$Builder;innerWidth(I)Lnet/minecraft/client/gui/widget/NarratedMultilineTextWidget$Builder;
 *   Lnet/minecraft/client/gui/widget/NarratedMultilineTextWidget$Builder;build()Lnet/minecraft/client/gui/widget/NarratedMultilineTextWidget;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/client/gui/screen/MessageScreen;addDrawableChild(Lnet/minecraft/client/gui/Element;)Lnet/minecraft/client/gui/Element;
 *   Lnet/minecraft/client/gui/screen/MessageScreen;renderPanoramaBackground(Lnet/minecraft/client/gui/DrawContext;F)V
 *   Lnet/minecraft/client/gui/screen/MessageScreen;applyBlur(Lnet/minecraft/client/gui/DrawContext;)V
 *   Lnet/minecraft/client/gui/screen/MessageScreen;renderDarkening(Lnet/minecraft/client/gui/DrawContext;)V
 */
package net.minecraft.client.gui.screen;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.NarratedMultilineTextWidget;
import net.minecraft.text.Text;
import org.jspecify.annotations.Nullable;

@Environment(value=EnvType.CLIENT)
public class MessageScreen
extends Screen {
    private @Nullable NarratedMultilineTextWidget textWidget;

    public MessageScreen(Text arg) {
        super(arg);
    }

    @Override
    protected void init() {
        this.textWidget = this.addDrawableChild(NarratedMultilineTextWidget.builder(this.title, this.textRenderer, 12).innerWidth(this.textRenderer.getWidth(this.title)).build());
        this.refreshWidgetPositions();
    }

    @Override
    protected void refreshWidgetPositions() {
        if (this.textWidget != null) {
            this.textWidget.setPosition(this.width / 2 - this.textWidget.getWidth() / 2, this.height / 2 - this.textRenderer.fontHeight / 2);
        }
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return false;
    }

    @Override
    protected boolean hasUsageText() {
        return false;
    }

    @Override
    public void renderBackground(DrawContext context, int mouseX, int mouseY, float deltaTicks) {
        this.renderPanoramaBackground(context, deltaTicks);
        this.applyBlur(context);
        this.renderDarkening(context);
    }
}

