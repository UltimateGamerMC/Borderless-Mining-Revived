/*
 * External method calls:
 *   Lnet/minecraft/text/StringVisitable;concat(Ljava/util/List;)Lnet/minecraft/text/StringVisitable;
 */
package net.minecraft.client.util;

import com.google.common.collect.Lists;
import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.text.StringVisitable;
import org.jspecify.annotations.Nullable;

@Environment(value=EnvType.CLIENT)
public class TextCollector {
    private final List<StringVisitable> texts = Lists.newArrayList();

    public void add(StringVisitable text) {
        this.texts.add(text);
    }

    public @Nullable StringVisitable getRawCombined() {
        if (this.texts.isEmpty()) {
            return null;
        }
        if (this.texts.size() == 1) {
            return this.texts.get(0);
        }
        return StringVisitable.concat(this.texts);
    }

    public StringVisitable getCombined() {
        StringVisitable lv = this.getRawCombined();
        return lv != null ? lv : StringVisitable.EMPTY;
    }

    public void clear() {
        this.texts.clear();
    }
}

