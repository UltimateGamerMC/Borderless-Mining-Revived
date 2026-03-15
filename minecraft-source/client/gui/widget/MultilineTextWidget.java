/*
 * External method calls:
 *   Lnet/minecraft/util/Util;cachedMapper(Ljava/util/function/Function;)Lnet/minecraft/util/CachedMapper;
 *   Lnet/minecraft/util/CachedMapper;map(Ljava/lang/Object;)Ljava/lang/Object;
 *   Lnet/minecraft/client/font/MultilineText;draw(Lnet/minecraft/client/font/Alignment;IIILnet/minecraft/client/font/DrawnTextConsumer;)I
 *   Lnet/minecraft/client/font/MultilineText;create(Lnet/minecraft/client/font/TextRenderer;II[Lnet/minecraft/text/Text;)Lnet/minecraft/client/font/MultilineText;
 *   Lnet/minecraft/client/font/MultilineText;create(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/text/Text;I)Lnet/minecraft/client/font/MultilineText;
 */
package net.minecraft.client.gui.widget;

import java.util.OptionalInt;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.font.Alignment;
import net.minecraft.client.font.DrawnTextConsumer;
import net.minecraft.client.font.MultilineText;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.widget.AbstractTextWidget;
import net.minecraft.text.Text;
import net.minecraft.util.CachedMapper;
import net.minecraft.util.Util;

@Environment(value=EnvType.CLIENT)
public class MultilineTextWidget
extends AbstractTextWidget {
    private OptionalInt maxWidth = OptionalInt.empty();
    private OptionalInt maxRows = OptionalInt.empty();
    private final CachedMapper<CacheKey, MultilineText> cacheKeyToText = Util.cachedMapper(cacheKey -> {
        if (cacheKey.maxRows.isPresent()) {
            return MultilineText.create(textRenderer, cacheKey.maxWidth, cacheKey.maxRows.getAsInt(), cacheKey.message);
        }
        return MultilineText.create(textRenderer, cacheKey.message, cacheKey.maxWidth);
    });
    private boolean centered = false;

    public MultilineTextWidget(Text message, TextRenderer textRenderer) {
        this(0, 0, message, textRenderer);
    }

    public MultilineTextWidget(int x, int y, Text message, TextRenderer textRenderer) {
        super(x, y, 0, 0, message, textRenderer);
        this.active = false;
    }

    public MultilineTextWidget setMaxWidth(int maxWidth) {
        this.maxWidth = OptionalInt.of(maxWidth);
        return this;
    }

    public MultilineTextWidget setMaxRows(int maxRows) {
        this.maxRows = OptionalInt.of(maxRows);
        return this;
    }

    public MultilineTextWidget setCentered(boolean centered) {
        this.centered = centered;
        return this;
    }

    @Override
    public int getWidth() {
        return this.cacheKeyToText.map(this.getCacheKey()).getMaxWidth();
    }

    @Override
    public int getHeight() {
        return this.cacheKeyToText.map(this.getCacheKey()).getLineCount() * this.getTextRenderer().fontHeight;
    }

    @Override
    public void draw(DrawnTextConsumer textConsumer) {
        MultilineText lv = this.cacheKeyToText.map(this.getCacheKey());
        int i = this.getTextX();
        int j = this.getTextY();
        int k = this.getTextRenderer().fontHeight;
        if (this.centered) {
            int l = this.getX() + this.getWidth() / 2;
            lv.draw(Alignment.CENTER, l, j, k, textConsumer);
        } else {
            lv.draw(Alignment.LEFT, i, j, k, textConsumer);
        }
    }

    protected int getTextX() {
        return this.getX();
    }

    protected int getTextY() {
        return this.getY();
    }

    private CacheKey getCacheKey() {
        return new CacheKey(this.getMessage(), this.maxWidth.orElse(Integer.MAX_VALUE), this.maxRows);
    }

    @Environment(value=EnvType.CLIENT)
    record CacheKey(Text message, int maxWidth, OptionalInt maxRows) {
    }
}

