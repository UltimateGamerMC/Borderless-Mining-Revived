/*
 * External method calls:
 *   Lnet/minecraft/entity/player/SkinTextures;body()Lnet/minecraft/util/AssetInfo$TextureAsset;
 *   Lnet/minecraft/util/AssetInfo$TextureAsset;texturePath()Lnet/minecraft/util/Identifier;
 *   Lnet/minecraft/client/gui/DrawContext;drawTexture(Lcom/mojang/blaze3d/pipeline/RenderPipeline;Lnet/minecraft/util/Identifier;IIFFIIIIIII)V
 *
 * Internal private/static methods:
 *   Lnet/minecraft/client/gui/PlayerSkinDrawer;draw(Lnet/minecraft/client/gui/DrawContext;Lnet/minecraft/entity/player/SkinTextures;IIII)V
 *   Lnet/minecraft/client/gui/PlayerSkinDrawer;draw(Lnet/minecraft/client/gui/DrawContext;Lnet/minecraft/util/Identifier;IIIZZI)V
 *   Lnet/minecraft/client/gui/PlayerSkinDrawer;drawHat(Lnet/minecraft/client/gui/DrawContext;Lnet/minecraft/util/Identifier;IIIZI)V
 */
package net.minecraft.client.gui;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.player.SkinTextures;
import net.minecraft.util.Identifier;

@Environment(value=EnvType.CLIENT)
public class PlayerSkinDrawer {
    public static final int FACE_WIDTH = 8;
    public static final int FACE_HEIGHT = 8;
    public static final int FACE_X = 8;
    public static final int FACE_Y = 8;
    public static final int FACE_OVERLAY_X = 40;
    public static final int FACE_OVERLAY_Y = 8;
    public static final int field_39531 = 8;
    public static final int field_39532 = 8;
    public static final int SKIN_TEXTURE_WIDTH = 64;
    public static final int SKIN_TEXTURE_HEIGHT = 64;

    public static void draw(DrawContext context, SkinTextures textures, int x, int y, int size) {
        PlayerSkinDrawer.draw(context, textures, x, y, size, -1);
    }

    public static void draw(DrawContext context, SkinTextures textures, int x, int y, int size, int color) {
        PlayerSkinDrawer.draw(context, textures.body().texturePath(), x, y, size, true, false, color);
    }

    public static void draw(DrawContext context, Identifier texture, int x, int y, int size, boolean hatVisible, boolean upsideDown, int color) {
        int m = 8 + (upsideDown ? 8 : 0);
        int n = 8 * (upsideDown ? -1 : 1);
        context.drawTexture(RenderPipelines.GUI_TEXTURED, texture, x, y, 8.0f, m, size, size, 8, n, 64, 64, color);
        if (hatVisible) {
            PlayerSkinDrawer.drawHat(context, texture, x, y, size, upsideDown, color);
        }
    }

    private static void drawHat(DrawContext context, Identifier texture, int x, int y, int size, boolean upsideDown, int color) {
        int m = 8 + (upsideDown ? 8 : 0);
        int n = 8 * (upsideDown ? -1 : 1);
        context.drawTexture(RenderPipelines.GUI_TEXTURED, texture, x, y, 40.0f, m, size, size, 8, n, 64, 64, color);
    }
}

