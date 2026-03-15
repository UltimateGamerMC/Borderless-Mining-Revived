/*
 * External method calls:
 *   Lnet/minecraft/client/render/RenderSetup$TextureSpec;sampler()Ljava/util/function/Supplier;
 */
package net.minecraft.client.render;

import com.google.common.base.Suppliers;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.FilterMode;
import com.mojang.blaze3d.textures.GpuTextureView;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.GpuSampler;
import net.minecraft.client.render.LayeringTransform;
import net.minecraft.client.render.OutputTarget;
import net.minecraft.client.render.TextureTransform;
import net.minecraft.client.texture.AbstractTexture;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.util.Identifier;
import org.jspecify.annotations.Nullable;

@Environment(value=EnvType.CLIENT)
public final class RenderSetup {
    final RenderPipeline pipeline;
    final Map<String, TextureSpec> textures;
    final TextureTransform textureTransform;
    final OutputTarget outputTarget;
    final OutlineMode outlineMode;
    final boolean useLightmap;
    final boolean useOverlay;
    final boolean hasCrumbling;
    final boolean translucent;
    final int expectedBufferSize;
    final LayeringTransform layeringTransform;

    RenderSetup(RenderPipeline pipeline, Map<String, TextureSpec> textures, boolean useLightmap, boolean useOverlay, LayeringTransform layeringTransform, OutputTarget outputTarget, TextureTransform textureTransform, OutlineMode outlineMode, boolean hasCrumbling, boolean translucent, int expectedBufferSize) {
        this.pipeline = pipeline;
        this.textures = textures;
        this.outputTarget = outputTarget;
        this.textureTransform = textureTransform;
        this.useLightmap = useLightmap;
        this.useOverlay = useOverlay;
        this.outlineMode = outlineMode;
        this.layeringTransform = layeringTransform;
        this.hasCrumbling = hasCrumbling;
        this.translucent = translucent;
        this.expectedBufferSize = expectedBufferSize;
    }

    public String toString() {
        return "RenderSetup[layeringTransform=" + String.valueOf(this.layeringTransform) + ", textureTransform=" + String.valueOf(this.textureTransform) + ", textures=" + String.valueOf(this.textures) + ", outlineProperty=" + String.valueOf((Object)this.outlineMode) + ", useLightmap=" + this.useLightmap + ", useOverlay=" + this.useOverlay + "]";
    }

    public static Builder builder(RenderPipeline renderPipeline) {
        return new Builder(renderPipeline);
    }

    public Map<String, Texture> resolveTextures() {
        if (this.textures.isEmpty() && !this.useOverlay && !this.useLightmap) {
            return Collections.emptyMap();
        }
        HashMap<String, Texture> map = new HashMap<String, Texture>();
        if (this.useOverlay) {
            map.put("Sampler1", new Texture(MinecraftClient.getInstance().gameRenderer.getOverlayTexture().getTextureView(), RenderSystem.getSamplerCache().get(FilterMode.LINEAR)));
        }
        if (this.useLightmap) {
            map.put("Sampler2", new Texture(MinecraftClient.getInstance().gameRenderer.getLightmapTextureManager().getGlTextureView(), RenderSystem.getSamplerCache().get(FilterMode.LINEAR)));
        }
        TextureManager lv = MinecraftClient.getInstance().getTextureManager();
        for (Map.Entry<String, TextureSpec> entry : this.textures.entrySet()) {
            AbstractTexture lv2 = lv.getTexture(entry.getValue().location);
            GpuSampler lv3 = entry.getValue().sampler().get();
            map.put(entry.getKey(), new Texture(lv2.getGlTextureView(), lv3 != null ? lv3 : lv2.getSampler()));
        }
        return map;
    }

    @Environment(value=EnvType.CLIENT)
    public static enum OutlineMode {
        NONE("none"),
        IS_OUTLINE("is_outline"),
        AFFECTS_OUTLINE("affects_outline");

        private final String name;

        private OutlineMode(String name) {
            this.name = name;
        }

        public String toString() {
            return this.name;
        }
    }

    @Environment(value=EnvType.CLIENT)
    public static class Builder {
        private final RenderPipeline pipeline;
        private boolean useLightmap = false;
        private boolean useOverlay = false;
        private LayeringTransform layeringTransform = LayeringTransform.NO_LAYERING;
        private OutputTarget outputTarget = OutputTarget.MAIN_TARGET;
        private TextureTransform textureTransform = TextureTransform.DEFAULT_TEXTURING;
        private boolean hasCrumbling = false;
        private boolean translucent = false;
        private int expectedBufferSize = 1536;
        private OutlineMode outlineMode = OutlineMode.NONE;
        private final Map<String, TextureSpec> textures = new HashMap<String, TextureSpec>();

        Builder(RenderPipeline pipeline) {
            this.pipeline = pipeline;
        }

        public Builder texture(String name, Identifier id) {
            this.textures.put(name, new TextureSpec(id, () -> null));
            return this;
        }

        public Builder texture(String name, Identifier id, @Nullable Supplier<GpuSampler> samplerSupplier) {
            this.textures.put(name, new TextureSpec(id, Suppliers.memoize(() -> samplerSupplier == null ? null : (GpuSampler)samplerSupplier.get())));
            return this;
        }

        public Builder useLightmap() {
            this.useLightmap = true;
            return this;
        }

        public Builder useOverlay() {
            this.useOverlay = true;
            return this;
        }

        public Builder crumbling() {
            this.hasCrumbling = true;
            return this;
        }

        public Builder translucent() {
            this.translucent = true;
            return this;
        }

        public Builder expectedBufferSize(int expectedBufferSize) {
            this.expectedBufferSize = expectedBufferSize;
            return this;
        }

        public Builder layeringTransform(LayeringTransform layeringTransform) {
            this.layeringTransform = layeringTransform;
            return this;
        }

        public Builder outputTarget(OutputTarget outputTarget) {
            this.outputTarget = outputTarget;
            return this;
        }

        public Builder textureTransform(TextureTransform textureTransform) {
            this.textureTransform = textureTransform;
            return this;
        }

        public Builder outlineMode(OutlineMode outlineMode) {
            this.outlineMode = outlineMode;
            return this;
        }

        public RenderSetup build() {
            return new RenderSetup(this.pipeline, this.textures, this.useLightmap, this.useOverlay, this.layeringTransform, this.outputTarget, this.textureTransform, this.outlineMode, this.hasCrumbling, this.translucent, this.expectedBufferSize);
        }
    }

    @Environment(value=EnvType.CLIENT)
    public record Texture(GpuTextureView textureView, GpuSampler sampler) {
    }

    @Environment(value=EnvType.CLIENT)
    record TextureSpec(Identifier location, Supplier<@Nullable GpuSampler> sampler) {
    }
}

