/*
 * External method calls:
 *   Lnet/minecraft/client/font/TextRenderer;prepare(Lnet/minecraft/text/OrderedText;FFIZZI)Lnet/minecraft/client/font/TextRenderer$GlyphDrawable;
 *   Lnet/minecraft/client/gui/ScreenRect;transformEachVertex(Lorg/joml/Matrix3x2fc;)Lnet/minecraft/client/gui/ScreenRect;
 *   Lnet/minecraft/client/gui/ScreenRect;intersection(Lnet/minecraft/client/gui/ScreenRect;)Lnet/minecraft/client/gui/ScreenRect;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/client/gui/render/state/TextGuiElementRenderState;prepare()Lnet/minecraft/client/font/TextRenderer$GlyphDrawable;
 */
package net.minecraft.client.gui.render.state;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.ScreenRect;
import net.minecraft.client.gui.render.state.GuiElementRenderState;
import net.minecraft.text.OrderedText;
import org.joml.Matrix3x2fc;
import org.jspecify.annotations.Nullable;

@Environment(value=EnvType.CLIENT)
public final class TextGuiElementRenderState
implements GuiElementRenderState {
    public final TextRenderer textRenderer;
    public final OrderedText orderedText;
    public final Matrix3x2fc matrix;
    public final int x;
    public final int y;
    public final int color;
    public final int backgroundColor;
    public final boolean shadow;
    final boolean trackEmpty;
    public final @Nullable ScreenRect clipBounds;
    private @Nullable TextRenderer.GlyphDrawable preparation;
    private @Nullable ScreenRect bounds;

    public TextGuiElementRenderState(TextRenderer textRenderer, OrderedText orderedText, Matrix3x2fc matrix, int x, int y, int color, int backgroundColor, boolean shadow, boolean trackEmpty, @Nullable ScreenRect clipBounds) {
        this.textRenderer = textRenderer;
        this.orderedText = orderedText;
        this.matrix = matrix;
        this.x = x;
        this.y = y;
        this.color = color;
        this.backgroundColor = backgroundColor;
        this.shadow = shadow;
        this.trackEmpty = trackEmpty;
        this.clipBounds = clipBounds;
    }

    public TextRenderer.GlyphDrawable prepare() {
        if (this.preparation == null) {
            this.preparation = this.textRenderer.prepare(this.orderedText, this.x, this.y, this.color, this.shadow, this.trackEmpty, this.backgroundColor);
            ScreenRect lv = this.preparation.getScreenRect();
            if (lv != null) {
                lv = lv.transformEachVertex(this.matrix);
                this.bounds = this.clipBounds != null ? this.clipBounds.intersection(lv) : lv;
            }
        }
        return this.preparation;
    }

    @Override
    public @Nullable ScreenRect bounds() {
        this.prepare();
        return this.bounds;
    }
}

