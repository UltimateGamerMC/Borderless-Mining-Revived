/*
 * External method calls:
 *   Lnet/minecraft/client/gui/widget/CheckboxWidget$Callback;onValueChange(Lnet/minecraft/client/gui/widget/CheckboxWidget;Z)V
 *   Lnet/minecraft/text/Text;translatable(Ljava/lang/String;)Lnet/minecraft/text/MutableText;
 *   Lnet/minecraft/client/gui/DrawContext;drawGuiTexture(Lcom/mojang/blaze3d/pipeline/RenderPipeline;Lnet/minecraft/util/Identifier;IIIII)V
 *   Lnet/minecraft/client/gui/DrawContext$HoverType;fromTooltip(Z)Lnet/minecraft/client/gui/DrawContext$HoverType;
 *   Lnet/minecraft/client/gui/widget/MultilineTextWidget;draw(Lnet/minecraft/client/font/DrawnTextConsumer;)V
 *   Lnet/minecraft/util/Identifier;ofVanilla(Ljava/lang/String;)Lnet/minecraft/util/Identifier;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/client/gui/widget/CheckboxWidget;calculateHeight(Lnet/minecraft/client/font/TextRenderer;)I
 *   Lnet/minecraft/client/gui/widget/CheckboxWidget;calculateWidth(ILnet/minecraft/text/Text;Lnet/minecraft/client/font/TextRenderer;)I
 *   Lnet/minecraft/client/gui/widget/CheckboxWidget;calculateWidth(Lnet/minecraft/text/Text;Lnet/minecraft/client/font/TextRenderer;)I
 */
package net.minecraft.client.gui.widget;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.screen.narration.NarrationPart;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.MultilineTextWidget;
import net.minecraft.client.gui.widget.PressableWidget;
import net.minecraft.client.input.AbstractInput;
import net.minecraft.client.option.SimpleOption;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.ColorHelper;
import org.jspecify.annotations.Nullable;

@Environment(value=EnvType.CLIENT)
public class CheckboxWidget
extends PressableWidget {
    private static final Identifier SELECTED_HIGHLIGHTED_TEXTURE = Identifier.ofVanilla("widget/checkbox_selected_highlighted");
    private static final Identifier SELECTED_TEXTURE = Identifier.ofVanilla("widget/checkbox_selected");
    private static final Identifier HIGHLIGHTED_TEXTURE = Identifier.ofVanilla("widget/checkbox_highlighted");
    private static final Identifier TEXTURE = Identifier.ofVanilla("widget/checkbox");
    private static final int field_47105 = 4;
    private static final int field_47106 = 8;
    private boolean checked;
    private final Callback callback;
    private final MultilineTextWidget textWidget;

    CheckboxWidget(int x, int y, int maxWidth, Text message, TextRenderer textRenderer, boolean checked, Callback callback) {
        super(x, y, 0, 0, message);
        this.textWidget = new MultilineTextWidget(message, textRenderer);
        this.textWidget.setMaxRows(2);
        this.width = this.setMaxWidth(maxWidth, textRenderer);
        this.height = this.calculateHeight(textRenderer);
        this.checked = checked;
        this.callback = callback;
    }

    public int setMaxWidth(int max, TextRenderer textRenderer) {
        this.width = this.calculateWidth(max, this.getMessage(), textRenderer);
        this.textWidget.setMaxWidth(this.width);
        return this.width;
    }

    private int calculateWidth(int max, Text text, TextRenderer textRenderer) {
        return Math.min(CheckboxWidget.calculateWidth(text, textRenderer), max);
    }

    private int calculateHeight(TextRenderer textRenderer) {
        return Math.max(CheckboxWidget.getCheckboxSize(textRenderer), this.textWidget.getHeight());
    }

    static int calculateWidth(Text text, TextRenderer textRenderer) {
        return CheckboxWidget.getCheckboxSize(textRenderer) + 4 + textRenderer.getWidth(text);
    }

    public static Builder builder(Text text, TextRenderer textRenderer) {
        return new Builder(text, textRenderer);
    }

    public static int getCheckboxSize(TextRenderer textRenderer) {
        return textRenderer.fontHeight + 8;
    }

    @Override
    public void onPress(AbstractInput input) {
        this.checked = !this.checked;
        this.callback.onValueChange(this, this.checked);
    }

    public boolean isChecked() {
        return this.checked;
    }

    @Override
    public void appendClickableNarrations(NarrationMessageBuilder builder) {
        builder.put(NarrationPart.TITLE, (Text)this.getNarrationMessage());
        if (this.active) {
            if (this.isFocused()) {
                builder.put(NarrationPart.USAGE, (Text)Text.translatable(this.checked ? "narration.checkbox.usage.focused.uncheck" : "narration.checkbox.usage.focused.check"));
            } else {
                builder.put(NarrationPart.USAGE, (Text)Text.translatable(this.checked ? "narration.checkbox.usage.hovered.uncheck" : "narration.checkbox.usage.hovered.check"));
            }
        }
    }

    @Override
    public void drawIcon(DrawContext context, int mouseX, int mouseY, float deltaTicks) {
        MinecraftClient lv = MinecraftClient.getInstance();
        TextRenderer lv2 = lv.textRenderer;
        Identifier lv3 = this.checked ? (this.isFocused() ? SELECTED_HIGHLIGHTED_TEXTURE : SELECTED_TEXTURE) : (this.isFocused() ? HIGHLIGHTED_TEXTURE : TEXTURE);
        int k = CheckboxWidget.getCheckboxSize(lv2);
        context.drawGuiTexture(RenderPipelines.GUI_TEXTURED, lv3, this.getX(), this.getY(), k, k, ColorHelper.getWhite(this.alpha));
        int l = this.getX() + k + 4;
        int m = this.getY() + k / 2 - this.textWidget.getHeight() / 2;
        this.textWidget.setPosition(l, m);
        this.textWidget.draw(context.getHoverListener(this, DrawContext.HoverType.fromTooltip(this.isHovered())));
    }

    @Environment(value=EnvType.CLIENT)
    public static interface Callback {
        public static final Callback EMPTY = (checkbox, checked) -> {};

        public void onValueChange(CheckboxWidget var1, boolean var2);
    }

    @Environment(value=EnvType.CLIENT)
    public static class Builder {
        private final Text message;
        private final TextRenderer textRenderer;
        private int maxWidth;
        private int x = 0;
        private int y = 0;
        private Callback callback = Callback.EMPTY;
        private boolean checked = false;
        private @Nullable SimpleOption<Boolean> option = null;
        private @Nullable Tooltip tooltip = null;

        Builder(Text message, TextRenderer textRenderer) {
            this.message = message;
            this.textRenderer = textRenderer;
            this.maxWidth = CheckboxWidget.calculateWidth(message, textRenderer);
        }

        public Builder pos(int x, int y) {
            this.x = x;
            this.y = y;
            return this;
        }

        public Builder callback(Callback callback) {
            this.callback = callback;
            return this;
        }

        public Builder checked(boolean checked) {
            this.checked = checked;
            this.option = null;
            return this;
        }

        public Builder option(SimpleOption<Boolean> option) {
            this.option = option;
            this.checked = option.getValue();
            return this;
        }

        public Builder tooltip(Tooltip tooltip) {
            this.tooltip = tooltip;
            return this;
        }

        public Builder maxWidth(int maxWidth) {
            this.maxWidth = maxWidth;
            return this;
        }

        public CheckboxWidget build() {
            Callback lv = this.option == null ? this.callback : (checkbox, checked) -> {
                this.option.setValue(checked);
                this.callback.onValueChange(checkbox, checked);
            };
            CheckboxWidget lv2 = new CheckboxWidget(this.x, this.y, this.maxWidth, this.message, this.textRenderer, this.checked, lv);
            lv2.setTooltip(this.tooltip);
            return lv2;
        }
    }
}

