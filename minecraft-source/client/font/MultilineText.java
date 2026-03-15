/*
 * Internal private/static methods:
 *   Lnet/minecraft/client/font/MultilineText;create(Lnet/minecraft/client/font/TextRenderer;II[Lnet/minecraft/text/Text;)Lnet/minecraft/client/font/MultilineText;
 */
package net.minecraft.client.font;

import java.util.ArrayList;
import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.font.Alignment;
import net.minecraft.client.font.DrawnTextConsumer;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.OrderedText;
import net.minecraft.text.StringVisitable;
import net.minecraft.text.Text;
import net.minecraft.util.Language;
import org.jspecify.annotations.Nullable;

@Environment(value=EnvType.CLIENT)
public interface MultilineText {
    public static final MultilineText EMPTY = new MultilineText(){

        @Override
        public int draw(Alignment alignment, int x, int y, int lineHeight, DrawnTextConsumer consumer) {
            return y;
        }

        @Override
        public int getLineCount() {
            return 0;
        }

        @Override
        public int getMaxWidth() {
            return 0;
        }
    };

    public static MultilineText create(TextRenderer renderer, Text ... texts) {
        return MultilineText.create(renderer, Integer.MAX_VALUE, Integer.MAX_VALUE, texts);
    }

    public static MultilineText create(TextRenderer renderer, int maxWidth, Text ... texts) {
        return MultilineText.create(renderer, maxWidth, Integer.MAX_VALUE, texts);
    }

    public static MultilineText create(TextRenderer renderer, Text text, int maxWidth) {
        return MultilineText.create(renderer, maxWidth, Integer.MAX_VALUE, text);
    }

    public static MultilineText create(final TextRenderer textRenderer, final int maxWidth, final int maxLines, final Text ... texts) {
        if (texts.length == 0) {
            return EMPTY;
        }
        return new MultilineText(){
            private @Nullable List<Line> lines;
            private @Nullable Language language;

            @Override
            public int draw(Alignment alignment, int x, int y, int lineHeight, DrawnTextConsumer consumer) {
                int l = y;
                for (Line lv : this.getLines()) {
                    int m = alignment.getAdjustedX(x, lv.width);
                    consumer.text(m, l, lv.text);
                    l += lineHeight;
                }
                return l;
            }

            private List<Line> getLines() {
                Language lv = Language.getInstance();
                if (this.lines != null && lv == this.language) {
                    return this.lines;
                }
                this.language = lv;
                ArrayList<StringVisitable> list = new ArrayList<StringVisitable>();
                for (Text lv2 : texts) {
                    list.addAll(textRenderer.wrapLinesWithoutLanguage(lv2, maxWidth));
                }
                this.lines = new ArrayList<Line>();
                int i = Math.min(list.size(), maxLines);
                List list2 = list.subList(0, i);
                for (int j = 0; j < list2.size(); ++j) {
                    StringVisitable lv3 = (StringVisitable)list2.get(j);
                    OrderedText lv4 = Language.getInstance().reorder(lv3);
                    if (j == list2.size() - 1 && i == maxLines && i != list.size()) {
                        StringVisitable lv5 = textRenderer.trimToWidth(lv3, textRenderer.getWidth(lv3) - textRenderer.getWidth(ScreenTexts.ELLIPSIS));
                        StringVisitable lv6 = StringVisitable.concat(lv5, ScreenTexts.ELLIPSIS.copy().fillStyle(texts[texts.length - 1].getStyle()));
                        this.lines.add(new Line(Language.getInstance().reorder(lv6), textRenderer.getWidth(lv6)));
                        continue;
                    }
                    this.lines.add(new Line(lv4, textRenderer.getWidth(lv4)));
                }
                return this.lines;
            }

            @Override
            public int getLineCount() {
                return this.getLines().size();
            }

            @Override
            public int getMaxWidth() {
                return Math.min(maxWidth, this.getLines().stream().mapToInt(Line::width).max().orElse(0));
            }
        };
    }

    public int draw(Alignment var1, int var2, int var3, int var4, DrawnTextConsumer var5);

    public int getLineCount();

    public int getMaxWidth();

    @Environment(value=EnvType.CLIENT)
    public record Line(OrderedText text, int width) {
    }
}

