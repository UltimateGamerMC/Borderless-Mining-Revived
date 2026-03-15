/*
 * External method calls:
 *   Lnet/minecraft/client/render/RenderLayers;textIntensity(Lnet/minecraft/util/Identifier;)Lnet/minecraft/client/render/RenderLayer;
 *   Lnet/minecraft/client/render/RenderLayers;textIntensitySeeThrough(Lnet/minecraft/util/Identifier;)Lnet/minecraft/client/render/RenderLayer;
 *   Lnet/minecraft/client/render/RenderLayers;textIntensityPolygonOffset(Lnet/minecraft/util/Identifier;)Lnet/minecraft/client/render/RenderLayer;
 *   Lnet/minecraft/client/render/RenderLayers;text(Lnet/minecraft/util/Identifier;)Lnet/minecraft/client/render/RenderLayer;
 *   Lnet/minecraft/client/render/RenderLayers;textSeeThrough(Lnet/minecraft/util/Identifier;)Lnet/minecraft/client/render/RenderLayer;
 *   Lnet/minecraft/client/render/RenderLayers;textPolygonOffset(Lnet/minecraft/util/Identifier;)Lnet/minecraft/client/render/RenderLayer;
 */
package net.minecraft.client.font;

import com.mojang.blaze3d.pipeline.RenderPipeline;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.util.Identifier;

@Environment(value=EnvType.CLIENT)
public record TextRenderLayerSet(RenderLayer normal, RenderLayer seeThrough, RenderLayer polygonOffset, RenderPipeline guiPipeline) {
    public static TextRenderLayerSet ofIntensity(Identifier textureId) {
        return new TextRenderLayerSet(RenderLayers.textIntensity(textureId), RenderLayers.textIntensitySeeThrough(textureId), RenderLayers.textIntensityPolygonOffset(textureId), RenderPipelines.GUI_TEXT_INTENSITY);
    }

    public static TextRenderLayerSet of(Identifier textureId) {
        return new TextRenderLayerSet(RenderLayers.text(textureId), RenderLayers.textSeeThrough(textureId), RenderLayers.textPolygonOffset(textureId), RenderPipelines.GUI_TEXT);
    }

    public RenderLayer getRenderLayer(TextRenderer.TextLayerType layerType) {
        return switch (layerType) {
            default -> throw new MatchException(null, null);
            case TextRenderer.TextLayerType.NORMAL -> this.normal;
            case TextRenderer.TextLayerType.SEE_THROUGH -> this.seeThrough;
            case TextRenderer.TextLayerType.POLYGON_OFFSET -> this.polygonOffset;
        };
    }
}

