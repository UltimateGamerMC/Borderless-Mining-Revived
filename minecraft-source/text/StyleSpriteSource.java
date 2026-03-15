/*
 * External method calls:
 *   Lnet/minecraft/text/StyleSpriteSource$Font;id()Lnet/minecraft/util/Identifier;
 *   Lnet/minecraft/util/Identifier;ofVanilla(Ljava/lang/String;)Lnet/minecraft/util/Identifier;
 */
package net.minecraft.text;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import net.minecraft.component.type.ProfileComponent;
import net.minecraft.util.Identifier;

public interface StyleSpriteSource {
    public static final Codec<StyleSpriteSource> FONT_CODEC = Identifier.CODEC.flatComapMap(Font::new, instance -> {
        if (instance instanceof Font) {
            Font lv = (Font)instance;
            return DataResult.success(lv.id());
        }
        return DataResult.error(() -> "Unsupported font description type: " + String.valueOf(instance));
    });
    public static final Font DEFAULT = new Font(Identifier.ofVanilla("default"));

    public record Font(Identifier id) implements StyleSpriteSource
    {
    }

    public record Player(ProfileComponent profile, boolean hat) implements StyleSpriteSource
    {
    }

    public record Sprite(Identifier atlasId, Identifier spriteId) implements StyleSpriteSource
    {
    }
}

