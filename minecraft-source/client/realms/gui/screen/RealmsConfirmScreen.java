/*
 * External method calls:
 *   Lnet/minecraft/client/gui/widget/ButtonWidget;builder(Lnet/minecraft/text/Text;Lnet/minecraft/client/gui/widget/ButtonWidget$PressAction;)Lnet/minecraft/client/gui/widget/ButtonWidget$Builder;
 *   Lnet/minecraft/client/gui/widget/ButtonWidget$Builder;dimensions(IIII)Lnet/minecraft/client/gui/widget/ButtonWidget$Builder;
 *   Lnet/minecraft/client/gui/widget/ButtonWidget$Builder;build()Lnet/minecraft/client/gui/widget/ButtonWidget;
 *   Lnet/minecraft/client/realms/gui/screen/RealmsScreen;render(Lnet/minecraft/client/gui/DrawContext;IIF)V
 *   Lnet/minecraft/client/gui/DrawContext;drawCenteredTextWithShadow(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/text/Text;III)V
 *
 * Internal private/static methods:
 *   Lnet/minecraft/client/realms/gui/screen/RealmsConfirmScreen;row(I)I
 *   Lnet/minecraft/client/realms/gui/screen/RealmsConfirmScreen;addDrawableChild(Lnet/minecraft/client/gui/Element;)Lnet/minecraft/client/gui/Element;
 */
package net.minecraft.client.realms.gui.screen;

import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.realms.gui.screen.RealmsScreen;
import net.minecraft.client.util.NarratorManager;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.minecraft.util.Colors;

@Environment(value=EnvType.CLIENT)
public class RealmsConfirmScreen
extends RealmsScreen {
    protected BooleanConsumer callback;
    private final Text title1;
    private final Text title2;

    public RealmsConfirmScreen(BooleanConsumer callback, Text title1, Text title2) {
        super(NarratorManager.EMPTY);
        this.callback = callback;
        this.title1 = title1;
        this.title2 = title2;
    }

    @Override
    public void init() {
        this.addDrawableChild(ButtonWidget.builder(ScreenTexts.YES, button -> this.callback.accept(true)).dimensions(this.width / 2 - 105, RealmsConfirmScreen.row(9), 100, 20).build());
        this.addDrawableChild(ButtonWidget.builder(ScreenTexts.NO, button -> this.callback.accept(false)).dimensions(this.width / 2 + 5, RealmsConfirmScreen.row(9), 100, 20).build());
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float deltaTicks) {
        super.render(context, mouseX, mouseY, deltaTicks);
        context.drawCenteredTextWithShadow(this.textRenderer, this.title1, this.width / 2, RealmsConfirmScreen.row(3), Colors.WHITE);
        context.drawCenteredTextWithShadow(this.textRenderer, this.title2, this.width / 2, RealmsConfirmScreen.row(5), Colors.WHITE);
    }
}

