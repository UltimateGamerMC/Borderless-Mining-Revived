/*
 * External method calls:
 *   Lnet/minecraft/text/Text;translatable(Ljava/lang/String;)Lnet/minecraft/text/MutableText;
 *   Lnet/minecraft/client/font/MultilineText;create(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/text/Text;I)Lnet/minecraft/client/font/MultilineText;
 *   Lnet/minecraft/client/gui/widget/ButtonWidget;builder(Lnet/minecraft/text/Text;Lnet/minecraft/client/gui/widget/ButtonWidget$PressAction;)Lnet/minecraft/client/gui/widget/ButtonWidget$Builder;
 *   Lnet/minecraft/client/gui/widget/ButtonWidget$Builder;dimensions(IIII)Lnet/minecraft/client/gui/widget/ButtonWidget$Builder;
 *   Lnet/minecraft/client/gui/widget/ButtonWidget$Builder;build()Lnet/minecraft/client/gui/widget/ButtonWidget;
 *   Lnet/minecraft/client/gui/screen/Screen;render(Lnet/minecraft/client/gui/DrawContext;IIF)V
 *   Lnet/minecraft/client/font/MultilineText;draw(Lnet/minecraft/client/font/Alignment;IIILnet/minecraft/client/font/DrawnTextConsumer;)I
 *
 * Internal private/static methods:
 *   Lnet/minecraft/client/gui/screen/world/DataPackFailureScreen;addDrawableChild(Lnet/minecraft/client/gui/Element;)Lnet/minecraft/client/gui/Element;
 */
package net.minecraft.client.gui.screen.world;

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

@Environment(value=EnvType.CLIENT)
public class DataPackFailureScreen
extends Screen {
    private MultilineText wrappedText = MultilineText.EMPTY;
    private final Runnable goBack;
    private final Runnable runServerInSafeMode;

    public DataPackFailureScreen(Runnable goBack, Runnable runServerInSafeMode) {
        super(Text.translatable("datapackFailure.title"));
        this.goBack = goBack;
        this.runServerInSafeMode = runServerInSafeMode;
    }

    @Override
    protected void init() {
        super.init();
        this.wrappedText = MultilineText.create(this.textRenderer, this.getTitle(), this.width - 50);
        this.addDrawableChild(ButtonWidget.builder(Text.translatable("datapackFailure.safeMode"), button -> this.runServerInSafeMode.run()).dimensions(this.width / 2 - 155, this.height / 6 + 96, 150, 20).build());
        this.addDrawableChild(ButtonWidget.builder(ScreenTexts.BACK, button -> this.goBack.run()).dimensions(this.width / 2 - 155 + 160, this.height / 6 + 96, 150, 20).build());
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float deltaTicks) {
        super.render(context, mouseX, mouseY, deltaTicks);
        DrawnTextConsumer lv = context.getTextConsumer();
        this.wrappedText.draw(Alignment.CENTER, this.width / 2, 70, this.textRenderer.fontHeight, lv);
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return false;
    }
}

