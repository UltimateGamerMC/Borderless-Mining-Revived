/*
 * External method calls:
 *   Lnet/minecraft/text/MutableText;styled(Ljava/util/function/UnaryOperator;)Lnet/minecraft/text/MutableText;
 *   Lnet/minecraft/text/Text;literal(Ljava/lang/String;)Lnet/minecraft/text/MutableText;
 *   Lnet/minecraft/text/Style;withHoverEvent(Lnet/minecraft/text/HoverEvent;)Lnet/minecraft/text/Style;
 */
package net.minecraft.scoreboard;

import com.mojang.authlib.GameProfile;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import org.jspecify.annotations.Nullable;

public interface ScoreHolder {
    public static final String WILDCARD_NAME = "*";
    public static final ScoreHolder WILDCARD = new ScoreHolder(){

        @Override
        public String getNameForScoreboard() {
            return ScoreHolder.WILDCARD_NAME;
        }
    };

    public String getNameForScoreboard();

    default public @Nullable Text getDisplayName() {
        return null;
    }

    default public Text getStyledDisplayName() {
        Text lv = this.getDisplayName();
        if (lv != null) {
            return lv.copy().styled(style -> style.withHoverEvent(new HoverEvent.ShowText(Text.literal(this.getNameForScoreboard()))));
        }
        return Text.literal(this.getNameForScoreboard());
    }

    public static ScoreHolder fromName(final String name) {
        if (name.equals(WILDCARD_NAME)) {
            return WILDCARD;
        }
        final MutableText lv = Text.literal(name);
        return new ScoreHolder(){

            @Override
            public String getNameForScoreboard() {
                return name;
            }

            @Override
            public Text getStyledDisplayName() {
                return lv;
            }
        };
    }

    public static ScoreHolder fromProfile(GameProfile gameProfile) {
        final String string = gameProfile.name();
        return new ScoreHolder(){

            @Override
            public String getNameForScoreboard() {
                return string;
            }
        };
    }
}

