/*
 * External method calls:
 *   Lnet/minecraft/text/Text;literal(Ljava/lang/String;)Lnet/minecraft/text/MutableText;
 *   Lnet/minecraft/text/Style;withParent(Lnet/minecraft/text/Style;)Lnet/minecraft/text/Style;
 *   Lnet/minecraft/text/Style;withFormatting([Lnet/minecraft/util/Formatting;)Lnet/minecraft/text/Style;
 *   Lnet/minecraft/text/Style;withFormatting(Lnet/minecraft/util/Formatting;)Lnet/minecraft/text/Style;
 *   Lnet/minecraft/text/Style;withColor(I)Lnet/minecraft/text/Style;
 *   Lnet/minecraft/text/Style;withoutShadow()Lnet/minecraft/text/Style;
 *   Lnet/minecraft/util/Language;reorder(Lnet/minecraft/text/StringVisitable;)Lnet/minecraft/text/OrderedText;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/text/MutableText;append(Lnet/minecraft/text/Text;)Lnet/minecraft/text/MutableText;
 */
package net.minecraft.text;

import com.google.common.collect.Lists;
import java.util.List;
import java.util.function.UnaryOperator;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TextContent;
import net.minecraft.util.Formatting;
import net.minecraft.util.Language;
import org.jspecify.annotations.Nullable;

public final class MutableText
implements Text {
    private final TextContent content;
    private final List<Text> siblings;
    private Style style;
    private OrderedText ordered = OrderedText.EMPTY;
    private @Nullable Language language;

    MutableText(TextContent content, List<Text> siblings, Style style) {
        this.content = content;
        this.siblings = siblings;
        this.style = style;
    }

    public static MutableText of(TextContent content) {
        return new MutableText(content, Lists.newArrayList(), Style.EMPTY);
    }

    @Override
    public TextContent getContent() {
        return this.content;
    }

    @Override
    public List<Text> getSiblings() {
        return this.siblings;
    }

    public MutableText setStyle(Style style) {
        this.style = style;
        return this;
    }

    @Override
    public Style getStyle() {
        return this.style;
    }

    public MutableText append(String text) {
        if (text.isEmpty()) {
            return this;
        }
        return this.append(Text.literal(text));
    }

    public MutableText append(Text text) {
        this.siblings.add(text);
        return this;
    }

    public MutableText styled(UnaryOperator<Style> styleUpdater) {
        this.setStyle((Style)styleUpdater.apply(this.getStyle()));
        return this;
    }

    public MutableText fillStyle(Style styleOverride) {
        this.setStyle(styleOverride.withParent(this.getStyle()));
        return this;
    }

    public MutableText formatted(Formatting ... formattings) {
        this.setStyle(this.getStyle().withFormatting(formattings));
        return this;
    }

    public MutableText formatted(Formatting formatting) {
        this.setStyle(this.getStyle().withFormatting(formatting));
        return this;
    }

    public MutableText withColor(int color) {
        this.setStyle(this.getStyle().withColor(color));
        return this;
    }

    public MutableText withoutShadow() {
        this.setStyle(this.getStyle().withoutShadow());
        return this;
    }

    @Override
    public OrderedText asOrderedText() {
        Language lv = Language.getInstance();
        if (this.language != lv) {
            this.ordered = lv.reorder(this);
            this.language = lv;
        }
        return this.ordered;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MutableText)) return false;
        MutableText lv = (MutableText)o;
        if (!this.content.equals(lv.content)) return false;
        if (!this.style.equals(lv.style)) return false;
        if (!this.siblings.equals(lv.siblings)) return false;
        return true;
    }

    public int hashCode() {
        int i = 1;
        i = 31 * i + this.content.hashCode();
        i = 31 * i + this.style.hashCode();
        i = 31 * i + this.siblings.hashCode();
        return i;
    }

    public String toString() {
        boolean bl2;
        StringBuilder stringBuilder = new StringBuilder(this.content.toString());
        boolean bl = !this.style.isEmpty();
        boolean bl3 = bl2 = !this.siblings.isEmpty();
        if (bl || bl2) {
            stringBuilder.append('[');
            if (bl) {
                stringBuilder.append("style=");
                stringBuilder.append(this.style);
            }
            if (bl && bl2) {
                stringBuilder.append(", ");
            }
            if (bl2) {
                stringBuilder.append("siblings=");
                stringBuilder.append(this.siblings);
            }
            stringBuilder.append(']');
        }
        return stringBuilder.toString();
    }
}

