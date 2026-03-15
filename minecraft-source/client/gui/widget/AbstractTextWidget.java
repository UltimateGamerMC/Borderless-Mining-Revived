/*
 * External method calls:
 *   Lnet/minecraft/client/gui/widget/ClickableWidget;onClick(Lnet/minecraft/client/gui/Click;Z)V
 *   Lnet/minecraft/text/Text;asOrderedText()Lnet/minecraft/text/OrderedText;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/client/gui/widget/AbstractTextWidget;draw(Lnet/minecraft/client/font/DrawnTextConsumer;)V
 */
package net.minecraft.client.gui.widget;

import java.util.function.Consumer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.font.DrawnTextConsumer;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import org.jspecify.annotations.Nullable;

@Environment(value=EnvType.CLIENT)
public abstract class AbstractTextWidget
extends ClickableWidget {
    private @Nullable Consumer<Style> clickedStyleConsumer = null;
    private final TextRenderer textRenderer;

    public AbstractTextWidget(int x, int y, int width, int height, Text message, TextRenderer textRenderer) {
        super(x, y, width, height, message);
        this.textRenderer = textRenderer;
    }

    public abstract void draw(DrawnTextConsumer var1);

    @Override
    public void renderWidget(DrawContext context, int mouseX, int mouseY, float deltaTicks) {
        DrawContext.HoverType lv = this.isHovered() ? (this.clickedStyleConsumer != null ? DrawContext.HoverType.TOOLTIP_AND_CURSOR : DrawContext.HoverType.TOOLTIP_ONLY) : DrawContext.HoverType.NONE;
        this.draw(context.getHoverListener(this, lv));
    }

    @Override
    public void onClick(Click click, boolean doubled) {
        if (this.clickedStyleConsumer != null) {
            DrawnTextConsumer.ClickHandler lv = new DrawnTextConsumer.ClickHandler(this.getTextRenderer(), (int)click.x(), (int)click.y());
            this.draw(lv);
            Style lv2 = lv.getStyle();
            if (lv2 != null) {
                this.clickedStyleConsumer.accept(lv2);
                return;
            }
        }
        super.onClick(click, doubled);
    }

    @Override
    protected void appendClickableNarrations(NarrationMessageBuilder builder) {
    }

    protected final TextRenderer getTextRenderer() {
        return this.textRenderer;
    }

    @Override
    public void setMessage(Text message) {
        super.setMessage(message);
        this.setWidth(this.getTextRenderer().getWidth(message.asOrderedText()));
    }

    public AbstractTextWidget onClick(@Nullable Consumer<Style> clickedStyleConsumer) {
        this.clickedStyleConsumer = clickedStyleConsumer;
        return this;
    }
}

