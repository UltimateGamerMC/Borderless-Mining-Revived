/*
 * External method calls:
 *   Lnet/minecraft/client/gui/DrawContext;drawTextWithShadow(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/text/Text;III)V
 *   Lnet/minecraft/client/gui/DrawContext;drawTextWithShadow(Lnet/minecraft/client/font/TextRenderer;Ljava/lang/String;III)V
 */
package net.minecraft.client.gui.widget;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.navigation.GuiNavigation;
import net.minecraft.client.gui.navigation.GuiNavigationPath;
import net.minecraft.client.gui.screen.LoadingDisplay;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.sound.SoundManager;
import net.minecraft.text.Text;
import net.minecraft.util.Colors;
import net.minecraft.util.Util;
import org.jspecify.annotations.Nullable;

@Environment(value=EnvType.CLIENT)
public class LoadingWidget
extends ClickableWidget {
    private final TextRenderer textRenderer;

    public LoadingWidget(TextRenderer textRenderer, Text message) {
        super(0, 0, textRenderer.getWidth(message), textRenderer.fontHeight * 3, message);
        this.textRenderer = textRenderer;
    }

    @Override
    protected void renderWidget(DrawContext context, int mouseX, int mouseY, float deltaTicks) {
        int k = this.getX() + this.getWidth() / 2;
        int l = this.getY() + this.getHeight() / 2;
        Text lv = this.getMessage();
        context.drawTextWithShadow(this.textRenderer, lv, k - this.textRenderer.getWidth(lv) / 2, l - this.textRenderer.fontHeight, Colors.WHITE);
        String string = LoadingDisplay.get(Util.getMeasuringTimeMs());
        context.drawTextWithShadow(this.textRenderer, string, k - this.textRenderer.getWidth(string) / 2, l + this.textRenderer.fontHeight, Colors.GRAY);
    }

    @Override
    protected void appendClickableNarrations(NarrationMessageBuilder builder) {
    }

    @Override
    public void playDownSound(SoundManager soundManager) {
    }

    @Override
    public boolean isInteractable() {
        return false;
    }

    @Override
    public @Nullable GuiNavigationPath getNavigationPath(GuiNavigation navigation) {
        return null;
    }
}

