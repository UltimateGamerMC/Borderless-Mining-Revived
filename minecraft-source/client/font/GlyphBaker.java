/*
 * External method calls:
 *   Lnet/minecraft/client/texture/TextureManager;destroyTexture(Lnet/minecraft/util/Identifier;)V
 *   Lnet/minecraft/client/font/GlyphAtlasTexture;bake(Lnet/minecraft/client/font/GlyphMetrics;Lnet/minecraft/client/font/UploadableGlyph;)Lnet/minecraft/client/font/BakedGlyphImpl;
 *   Lnet/minecraft/client/font/TextRenderLayerSet;of(Lnet/minecraft/util/Identifier;)Lnet/minecraft/client/font/TextRenderLayerSet;
 *   Lnet/minecraft/client/font/TextRenderLayerSet;ofIntensity(Lnet/minecraft/util/Identifier;)Lnet/minecraft/client/font/TextRenderLayerSet;
 *   Lnet/minecraft/client/texture/TextureManager;registerTexture(Lnet/minecraft/util/Identifier;Lnet/minecraft/client/texture/AbstractTexture;)V
 *   Lnet/minecraft/util/Identifier;withSuffixedPath(Ljava/lang/String;)Lnet/minecraft/util/Identifier;
 */
package net.minecraft.client.font;

import java.util.ArrayList;
import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.font.BakedGlyphImpl;
import net.minecraft.client.font.GlyphAtlasTexture;
import net.minecraft.client.font.GlyphMetrics;
import net.minecraft.client.font.TextRenderLayerSet;
import net.minecraft.client.font.UploadableGlyph;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.util.Identifier;
import org.jspecify.annotations.Nullable;

@Environment(value=EnvType.CLIENT)
public class GlyphBaker
implements AutoCloseable {
    private final TextureManager textureManager;
    private final Identifier fontId;
    private final List<GlyphAtlasTexture> glyphAtlases = new ArrayList<GlyphAtlasTexture>();

    public GlyphBaker(TextureManager textureManager, Identifier fontId) {
        this.textureManager = textureManager;
        this.fontId = fontId;
    }

    public void clear() {
        int i = this.glyphAtlases.size();
        this.glyphAtlases.clear();
        for (int j = 0; j < i; ++j) {
            this.textureManager.destroyTexture(this.getAtlasId(j));
        }
    }

    @Override
    public void close() {
        this.clear();
    }

    public @Nullable BakedGlyphImpl bake(GlyphMetrics metrics, UploadableGlyph glyph) {
        for (GlyphAtlasTexture lv : this.glyphAtlases) {
            BakedGlyphImpl lv2 = lv.bake(metrics, glyph);
            if (lv2 == null) continue;
            return lv2;
        }
        int i = this.glyphAtlases.size();
        Identifier lv3 = this.getAtlasId(i);
        boolean bl = glyph.hasColor();
        TextRenderLayerSet lv4 = bl ? TextRenderLayerSet.of(lv3) : TextRenderLayerSet.ofIntensity(lv3);
        GlyphAtlasTexture lv5 = new GlyphAtlasTexture(lv3::toString, lv4, bl);
        this.glyphAtlases.add(lv5);
        this.textureManager.registerTexture(lv3, lv5);
        return lv5.bake(metrics, glyph);
    }

    private Identifier getAtlasId(int atlasIndex) {
        return this.fontId.withSuffixedPath("/" + atlasIndex);
    }
}

