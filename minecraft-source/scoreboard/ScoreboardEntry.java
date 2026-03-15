/*
 * External method calls:
 *   Lnet/minecraft/text/Text;literal(Ljava/lang/String;)Lnet/minecraft/text/MutableText;
 *   Lnet/minecraft/scoreboard/number/NumberFormat;format(I)Lnet/minecraft/text/MutableText;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/scoreboard/ScoreboardEntry;owner()Ljava/lang/String;
 */
package net.minecraft.scoreboard;

import java.util.Objects;
import net.minecraft.scoreboard.number.NumberFormat;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import org.jspecify.annotations.Nullable;

public record ScoreboardEntry(String owner, int value, @Nullable Text display, @Nullable NumberFormat numberFormatOverride) {
    public boolean hidden() {
        return this.owner.startsWith("#");
    }

    public Text name() {
        if (this.display != null) {
            return this.display;
        }
        return Text.literal(this.owner());
    }

    public MutableText formatted(NumberFormat format) {
        return Objects.requireNonNullElse(this.numberFormatOverride, format).format(this.value);
    }
}

