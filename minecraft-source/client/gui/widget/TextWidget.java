/*
 * External method calls:
 *   Lnet/minecraft/text/Text;asOrderedText()Lnet/minecraft/text/OrderedText;
 *   Lnet/minecraft/client/font/DrawnTextConsumer;text(IILnet/minecraft/text/OrderedText;)V
 *   Lnet/minecraft/client/font/TextRenderer;trimToWidth(Lnet/minecraft/text/StringVisitable;I)Lnet/minecraft/text/StringVisitable;
 *   Lnet/minecraft/text/StringVisitable;concat([Lnet/minecraft/text/StringVisitable;)Lnet/minecraft/text/StringVisitable;
 *   Lnet/minecraft/util/Language;reorder(Lnet/minecraft/text/StringVisitable;)Lnet/minecraft/text/OrderedText;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/client/gui/widget/TextWidget;trim(Lnet/minecraft/text/Text;Lnet/minecraft/client/font/TextRenderer;I)Lnet/minecraft/text/OrderedText;
 *   Lnet/minecraft/client/gui/widget/TextWidget;drawTextWithMargin(Lnet/minecraft/client/font/DrawnTextConsumer;Lnet/minecraft/text/Text;I)V
 */
package net.minecraft.client.gui.widget;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.font.DrawnTextConsumer;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.widget.AbstractTextWidget;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.OrderedText;
import net.minecraft.text.StringVisitable;
import net.minecraft.text.Text;
import net.minecraft.util.Language;

@Environment(value=EnvType.CLIENT)
public class TextWidget
extends AbstractTextWidget {
    private static final int field_63885 = 2;
    private int maxWidth = 0;
    private int cachedWidth = 0;
    private boolean cachedWidthDirty = true;
    private TextOverflow textOverflow = TextOverflow.CLAMPED;

    public TextWidget(Text message, TextRenderer textRenderer) {
        this(0, 0, textRenderer.getWidth(message.asOrderedText()), textRenderer.fontHeight, message, textRenderer);
    }

    public TextWidget(int width, int height, Text message, TextRenderer textRenderer) {
        this(0, 0, width, height, message, textRenderer);
    }

    public TextWidget(int x, int y, int width, int height, Text message, TextRenderer textRenderer) {
        super(x, y, width, height, message, textRenderer);
        this.active = false;
    }

    @Override
    public void setMessage(Text message) {
        super.setMessage(message);
        this.cachedWidthDirty = true;
    }

    public TextWidget setMaxWidth(int width) {
        return this.setMaxWidth(width, TextOverflow.CLAMPED);
    }

    public TextWidget setMaxWidth(int width, TextOverflow textOverflow) {
        this.maxWidth = width;
        this.textOverflow = textOverflow;
        return this;
    }

    @Override
    public int getWidth() {
        if (this.maxWidth > 0) {
            if (this.cachedWidthDirty) {
                this.cachedWidth = Math.min(this.maxWidth, this.getTextRenderer().getWidth(this.getMessage().asOrderedText()));
                this.cachedWidthDirty = false;
            }
            return this.cachedWidth;
        }
        return super.getWidth();
    }

    @Override
    public void draw(DrawnTextConsumer textConsumer) {
        boolean bl;
        Text lv = this.getMessage();
        TextRenderer lv2 = this.getTextRenderer();
        int i = this.maxWidth > 0 ? this.maxWidth : this.getWidth();
        int j = lv2.getWidth(lv);
        int k = this.getX();
        int l = this.getY() + (this.getHeight() - lv2.fontHeight) / 2;
        boolean bl2 = bl = j > i;
        if (bl) {
            switch (this.textOverflow.ordinal()) {
                case 0: {
                    textConsumer.text(k, l, TextWidget.trim(lv, lv2, i));
                    break;
                }
                case 1: {
                    this.drawTextWithMargin(textConsumer, lv, 2);
                }
            }
        } else {
            textConsumer.text(k, l, lv.asOrderedText());
        }
    }

    public static OrderedText trim(Text text, TextRenderer textRenderer, int width) {
        StringVisitable lv = textRenderer.trimToWidth(text, width - textRenderer.getWidth(ScreenTexts.ELLIPSIS));
        return Language.getInstance().reorder(StringVisitable.concat(lv, ScreenTexts.ELLIPSIS));
    }

    @Environment(value=EnvType.CLIENT)
    public static enum TextOverflow {
        CLAMPED,
        SCROLLING;

    }
}

