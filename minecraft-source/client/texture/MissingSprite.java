/*
 * External method calls:
 *   Lnet/minecraft/util/Identifier;ofVanilla(Ljava/lang/String;)Lnet/minecraft/util/Identifier;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/client/texture/MissingSprite;createImage(II)Lnet/minecraft/client/texture/NativeImage;
 */
package net.minecraft.client.texture;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.SpriteContents;
import net.minecraft.client.texture.SpriteDimensions;
import net.minecraft.util.Identifier;

@Environment(value=EnvType.CLIENT)
public final class MissingSprite {
    private static final int WIDTH = 16;
    private static final int HEIGHT = 16;
    private static final String MISSINGNO_ID = "missingno";
    private static final Identifier MISSINGNO = Identifier.ofVanilla("missingno");

    public static NativeImage createImage() {
        return MissingSprite.createImage(16, 16);
    }

    public static NativeImage createImage(int width, int height) {
        NativeImage lv = new NativeImage(width, height, false);
        int k = -524040;
        for (int l = 0; l < height; ++l) {
            for (int m = 0; m < width; ++m) {
                if (l < height / 2 ^ m < width / 2) {
                    lv.setColorArgb(m, l, -524040);
                    continue;
                }
                lv.setColorArgb(m, l, -16777216);
            }
        }
        return lv;
    }

    public static SpriteContents createSpriteContents() {
        NativeImage lv = MissingSprite.createImage(16, 16);
        return new SpriteContents(MISSINGNO, new SpriteDimensions(16, 16), lv);
    }

    public static Identifier getMissingSpriteId() {
        return MISSINGNO;
    }
}

