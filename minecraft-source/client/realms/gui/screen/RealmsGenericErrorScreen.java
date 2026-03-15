/*
 * External method calls:
 *   Lnet/minecraft/client/realms/gui/screen/RealmsGenericErrorScreen$ErrorMessages;of(Lnet/minecraft/client/realms/exception/RealmsServiceException;)Lnet/minecraft/client/realms/gui/screen/RealmsGenericErrorScreen$ErrorMessages;
 *   Lnet/minecraft/text/Style;withColor(I)Lnet/minecraft/text/Style;
 *   Lnet/minecraft/text/Texts;withStyle(Lnet/minecraft/text/Text;Lnet/minecraft/text/Style;)Lnet/minecraft/text/Text;
 *   Lnet/minecraft/client/gui/widget/ButtonWidget;builder(Lnet/minecraft/text/Text;Lnet/minecraft/client/gui/widget/ButtonWidget$PressAction;)Lnet/minecraft/client/gui/widget/ButtonWidget$Builder;
 *   Lnet/minecraft/client/gui/widget/ButtonWidget$Builder;dimensions(IIII)Lnet/minecraft/client/gui/widget/ButtonWidget$Builder;
 *   Lnet/minecraft/client/gui/widget/ButtonWidget$Builder;build()Lnet/minecraft/client/gui/widget/ButtonWidget;
 *   Lnet/minecraft/client/font/MultilineText;create(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/text/Text;I)Lnet/minecraft/client/font/MultilineText;
 *   Lnet/minecraft/screen/ScreenTexts;joinSentences([Lnet/minecraft/text/Text;)Lnet/minecraft/text/MutableText;
 *   Lnet/minecraft/client/realms/gui/screen/RealmsScreen;render(Lnet/minecraft/client/gui/DrawContext;IIF)V
 *   Lnet/minecraft/client/gui/DrawContext;drawCenteredTextWithShadow(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/text/Text;III)V
 *   Lnet/minecraft/client/font/MultilineText;draw(Lnet/minecraft/client/font/Alignment;IIILnet/minecraft/client/font/DrawnTextConsumer;)I
 *   Lnet/minecraft/text/Text;translatable(Ljava/lang/String;)Lnet/minecraft/text/MutableText;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/client/realms/gui/screen/RealmsGenericErrorScreen;addDrawableChild(Lnet/minecraft/client/gui/Element;)Lnet/minecraft/client/gui/Element;
 */
package net.minecraft.client.realms.gui.screen;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.font.Alignment;
import net.minecraft.client.font.DrawnTextConsumer;
import net.minecraft.client.font.MultilineText;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.realms.RealmsError;
import net.minecraft.client.realms.exception.RealmsServiceException;
import net.minecraft.client.realms.gui.screen.RealmsScreen;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.Texts;
import net.minecraft.util.Colors;

@Environment(value=EnvType.CLIENT)
public class RealmsGenericErrorScreen
extends RealmsScreen {
    private static final Text GENERIC_ERROR_TEXT = Text.translatable("mco.errorMessage.generic");
    private final Screen parent;
    private final Text detail;
    private MultilineText text = MultilineText.EMPTY;

    public RealmsGenericErrorScreen(RealmsServiceException realmsServiceException, Screen parent) {
        this(ErrorMessages.of(realmsServiceException), parent);
    }

    public RealmsGenericErrorScreen(Text description, Screen parent) {
        this(new ErrorMessages(GENERIC_ERROR_TEXT, description), parent);
    }

    public RealmsGenericErrorScreen(Text title, Text description, Screen parent) {
        this(new ErrorMessages(title, description), parent);
    }

    private RealmsGenericErrorScreen(ErrorMessages messages, Screen parent) {
        super(messages.title);
        this.parent = parent;
        this.detail = Texts.withStyle(messages.detail, Style.EMPTY.withColor(Colors.LIGHT_RED));
    }

    @Override
    public void init() {
        this.addDrawableChild(ButtonWidget.builder(ScreenTexts.OK, button -> this.close()).dimensions(this.width / 2 - 100, this.height - 52, 200, 20).build());
        this.text = MultilineText.create(this.textRenderer, this.detail, this.width * 3 / 4);
    }

    @Override
    public void close() {
        this.client.setScreen(this.parent);
    }

    @Override
    public Text getNarratedTitle() {
        return ScreenTexts.joinSentences(super.getNarratedTitle(), this.detail);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float deltaTicks) {
        super.render(context, mouseX, mouseY, deltaTicks);
        context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, 80, Colors.WHITE);
        DrawnTextConsumer lv = context.getTextConsumer();
        this.text.draw(Alignment.CENTER, this.width / 2, 100, this.client.textRenderer.fontHeight, lv);
    }

    @Environment(value=EnvType.CLIENT)
    record ErrorMessages(Text title, Text detail) {
        static ErrorMessages of(RealmsServiceException exception) {
            RealmsError lv = exception.error;
            return new ErrorMessages(Text.translatable("mco.errorMessage.realmsService.realmsError", lv.getErrorCode()), lv.getText());
        }
    }
}

