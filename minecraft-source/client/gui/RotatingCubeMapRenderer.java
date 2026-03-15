/*
 * External method calls:
 *   Lnet/minecraft/client/gui/CubeMapRenderer;draw(Lnet/minecraft/client/MinecraftClient;FF)V
 *   Lnet/minecraft/client/gui/DrawContext;drawTexture(Lcom/mojang/blaze3d/pipeline/RenderPipeline;Lnet/minecraft/util/Identifier;IIFFIIIIII)V
 *   Lnet/minecraft/client/gui/CubeMapRenderer;registerTextures(Lnet/minecraft/client/texture/TextureManager;)V
 *   Lnet/minecraft/util/Identifier;ofVanilla(Ljava/lang/String;)Lnet/minecraft/util/Identifier;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/client/gui/RotatingCubeMapRenderer;wrapOnce(FF)F
 */
package net.minecraft.client.gui;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.CubeMapRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.util.Identifier;

@Environment(value=EnvType.CLIENT)
public class RotatingCubeMapRenderer {
    public static final Identifier OVERLAY_TEXTURE = Identifier.ofVanilla("textures/gui/title/background/panorama_overlay.png");
    private final MinecraftClient client;
    private final CubeMapRenderer cubeMap;
    private float pitch;

    public RotatingCubeMapRenderer(CubeMapRenderer cubeMap) {
        this.cubeMap = cubeMap;
        this.client = MinecraftClient.getInstance();
    }

    public void render(DrawContext context, int width, int height, boolean rotate) {
        if (rotate) {
            float f = this.client.getRenderTickCounter().getFixedDeltaTicks();
            float g = (float)((double)f * this.client.options.getPanoramaSpeed().getValue());
            this.pitch = RotatingCubeMapRenderer.wrapOnce(this.pitch + g * 0.1f, 360.0f);
        }
        this.cubeMap.draw(this.client, 10.0f, -this.pitch);
        context.drawTexture(RenderPipelines.GUI_TEXTURED, OVERLAY_TEXTURE, 0, 0, 0.0f, 0.0f, width, height, 16, 128, 16, 128);
    }

    private static float wrapOnce(float a, float b) {
        return a > b ? a - b : a;
    }

    public void registerTextures(TextureManager textureManager) {
        this.cubeMap.registerTextures(textureManager);
    }
}

