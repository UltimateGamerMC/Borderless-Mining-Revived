/*
 * External method calls:
 *   Lnet/minecraft/util/crash/CrashReport;create(Ljava/lang/Throwable;Ljava/lang/String;)Lnet/minecraft/util/crash/CrashReport;
 *   Lnet/minecraft/util/crash/CrashReport;addElement(Ljava/lang/String;)Lnet/minecraft/util/crash/CrashReportSection;
 *   Lnet/minecraft/client/resource/metadata/AnimationResourceMetadata;frames()Ljava/util/Optional;
 *   Lnet/minecraft/client/texture/SpriteContents$Animation;createAnimator(Lcom/mojang/blaze3d/buffers/GpuBufferSlice;I)Lnet/minecraft/client/texture/SpriteContents$Animator;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/client/texture/SpriteContents;createAnimation(Lnet/minecraft/client/texture/SpriteDimensions;IILnet/minecraft/client/resource/metadata/AnimationResourceMetadata;)Lnet/minecraft/client/texture/SpriteContents$Animation;
 */
package net.minecraft.client.texture;

import com.mojang.blaze3d.buffers.GpuBufferSlice;
import com.mojang.blaze3d.buffers.Std140SizeCalculator;
import com.mojang.blaze3d.systems.GpuDevice;
import com.mojang.blaze3d.systems.RenderPass;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.FilterMode;
import com.mojang.blaze3d.textures.GpuTexture;
import com.mojang.blaze3d.textures.GpuTextureView;
import com.mojang.blaze3d.textures.TextureFormat;
import com.mojang.logging.LogUtils;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gl.GpuSampler;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.resource.metadata.AnimationFrameResourceMetadata;
import net.minecraft.client.resource.metadata.AnimationResourceMetadata;
import net.minecraft.client.resource.metadata.TextureResourceMetadata;
import net.minecraft.client.texture.MipmapHelper;
import net.minecraft.client.texture.MipmapStrategy;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.SpriteDimensions;
import net.minecraft.client.texture.TextureStitcher;
import net.minecraft.resource.metadata.ResourceMetadataSerializer;
import net.minecraft.util.Identifier;
import net.minecraft.util.crash.CrashException;
import net.minecraft.util.crash.CrashReport;
import net.minecraft.util.crash.CrashReportSection;
import net.minecraft.util.math.ColorHelper;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;

@Environment(value=EnvType.CLIENT)
public class SpriteContents
implements TextureStitcher.Stitchable,
AutoCloseable {
    private static final Logger LOGGER = LogUtils.getLogger();
    public static final int SPRITE_INFO_SIZE = new Std140SizeCalculator().putMat4f().putMat4f().putFloat().putFloat().putInt().get();
    final Identifier id;
    final int width;
    final int height;
    private final NativeImage image;
    NativeImage[] mipmapLevelsImages;
    private final @Nullable Animation animation;
    private final List<ResourceMetadataSerializer.Value<?>> additionalMetadata;
    private final MipmapStrategy strategy;
    private final float cutoffBias;

    public SpriteContents(Identifier id, SpriteDimensions dimensions, NativeImage image) {
        this(id, dimensions, image, Optional.empty(), List.of(), Optional.empty());
    }

    public SpriteContents(Identifier id, SpriteDimensions dimensions, NativeImage image, Optional<AnimationResourceMetadata> animationResourceMetadata, List<ResourceMetadataSerializer.Value<?>> additionalMetadata, Optional<TextureResourceMetadata> metadata) {
        this.id = id;
        this.width = dimensions.width();
        this.height = dimensions.height();
        this.additionalMetadata = additionalMetadata;
        this.animation = animationResourceMetadata.map(animationMetadata -> this.createAnimation(dimensions, image.getWidth(), image.getHeight(), (AnimationResourceMetadata)animationMetadata)).orElse(null);
        this.image = image;
        this.mipmapLevelsImages = new NativeImage[]{this.image};
        this.strategy = metadata.map(TextureResourceMetadata::mipmapStrategy).orElse(MipmapStrategy.AUTO);
        this.cutoffBias = metadata.map(TextureResourceMetadata::alphaCutoffBias).orElse(Float.valueOf(0.0f)).floatValue();
    }

    public void generateMipmaps(int mipmapLevels) {
        try {
            this.mipmapLevelsImages = MipmapHelper.getMipmapLevelsImages(this.id, this.mipmapLevelsImages, mipmapLevels, this.strategy, this.cutoffBias);
        } catch (Throwable throwable) {
            CrashReport lv = CrashReport.create(throwable, "Generating mipmaps for frame");
            CrashReportSection lv2 = lv.addElement("Frame being iterated");
            lv2.add("Sprite name", this.id);
            lv2.add("Sprite size", () -> this.width + " x " + this.height);
            lv2.add("Sprite frames", () -> this.getFrameCount() + " frames");
            lv2.add("Mipmap levels", mipmapLevels);
            lv2.add("Original image size", () -> this.image.getWidth() + "x" + this.image.getHeight());
            throw new CrashException(lv);
        }
    }

    private int getFrameCount() {
        return this.animation != null ? this.animation.frames.size() : 1;
    }

    public boolean isAnimated() {
        return this.getFrameCount() > 1;
    }

    private @Nullable Animation createAnimation(SpriteDimensions dimensions, int imageWidth, int imageHeight, AnimationResourceMetadata metadata) {
        ArrayList<AnimationFrame> list;
        int k = imageWidth / dimensions.width();
        int l = imageHeight / dimensions.height();
        int m = k * l;
        int n = metadata.defaultFrameTime();
        if (metadata.frames().isEmpty()) {
            list = new ArrayList<AnimationFrame>(m);
            for (int o = 0; o < m; ++o) {
                list.add(new AnimationFrame(o, n));
            }
        } else {
            List<AnimationFrameResourceMetadata> list2 = metadata.frames().get();
            list = new ArrayList(list2.size());
            for (AnimationFrameResourceMetadata lv : list2) {
                list.add(new AnimationFrame(lv.index(), lv.getTime(n)));
            }
            int p = 0;
            IntOpenHashSet intSet = new IntOpenHashSet();
            Iterator iterator = list.iterator();
            while (iterator.hasNext()) {
                AnimationFrame lv2 = (AnimationFrame)iterator.next();
                boolean bl = true;
                if (lv2.time <= 0) {
                    LOGGER.warn("Invalid frame duration on sprite {} frame {}: {}", this.id, p, lv2.time);
                    bl = false;
                }
                if (lv2.index < 0 || lv2.index >= m) {
                    LOGGER.warn("Invalid frame index on sprite {} frame {}: {}", this.id, p, lv2.index);
                    bl = false;
                }
                if (bl) {
                    intSet.add(lv2.index);
                } else {
                    iterator.remove();
                }
                ++p;
            }
            int[] is = IntStream.range(0, m).filter(i -> !intSet.contains(i)).toArray();
            if (is.length > 0) {
                LOGGER.warn("Unused frames in sprite {}: {}", (Object)this.id, (Object)Arrays.toString(is));
            }
        }
        if (list.size() <= 1) {
            return null;
        }
        return new Animation(List.copyOf(list), k, metadata.interpolate());
    }

    @Override
    public int getWidth() {
        return this.width;
    }

    @Override
    public int getHeight() {
        return this.height;
    }

    @Override
    public Identifier getId() {
        return this.id;
    }

    public IntStream getDistinctFrameCount() {
        return this.animation != null ? this.animation.getDistinctFrameCount() : IntStream.of(1);
    }

    public @Nullable Animator createAnimator(GpuBufferSlice bufferSlice, int animationInfoSize) {
        return this.animation != null ? this.animation.createAnimator(bufferSlice, animationInfoSize) : null;
    }

    public <T> Optional<T> getAdditionalMetadataValue(ResourceMetadataSerializer<T> serializer) {
        for (ResourceMetadataSerializer.Value<?> lv : this.additionalMetadata) {
            Optional<T> optional = lv.getValueIfMatching(serializer);
            if (!optional.isPresent()) continue;
            return optional;
        }
        return Optional.empty();
    }

    @Override
    public void close() {
        for (NativeImage lv : this.mipmapLevelsImages) {
            lv.close();
        }
    }

    public String toString() {
        return "SpriteContents{name=" + String.valueOf(this.id) + ", frameCount=" + this.getFrameCount() + ", height=" + this.height + ", width=" + this.width + "}";
    }

    public boolean isPixelTransparent(int frame, int x, int y) {
        int l = x;
        int m = y;
        if (this.animation != null) {
            l += this.animation.getFrameX(frame) * this.width;
            m += this.animation.getFrameY(frame) * this.height;
        }
        return ColorHelper.getAlpha(this.image.getColorArgb(l, m)) == 0;
    }

    public void upload(GpuTexture texture, int mipmap) {
        RenderSystem.getDevice().createCommandEncoder().writeToTexture(texture, this.mipmapLevelsImages[mipmap], mipmap, 0, 0, 0, this.width >> mipmap, this.height >> mipmap, 0, 0);
    }

    @Environment(value=EnvType.CLIENT)
    class Animation {
        final List<AnimationFrame> frames;
        private final int frameCount;
        final boolean interpolated;

        Animation(List<AnimationFrame> frames, int frameCount, boolean interpolated) {
            this.frames = frames;
            this.frameCount = frameCount;
            this.interpolated = interpolated;
        }

        int getFrameX(int frame) {
            return frame % this.frameCount;
        }

        int getFrameY(int frame) {
            return frame / this.frameCount;
        }

        public Animator createAnimator(GpuBufferSlice bufferSlice, int animationInfoSize) {
            GpuDevice gpuDevice = RenderSystem.getDevice();
            Int2ObjectOpenHashMap<GpuTextureView> int2ObjectMap = new Int2ObjectOpenHashMap<GpuTextureView>();
            GpuBufferSlice[] gpuBufferSlices = new GpuBufferSlice[SpriteContents.this.mipmapLevelsImages.length];
            for (int j : this.getDistinctFrameCount().toArray()) {
                GpuTexture gpuTexture = gpuDevice.createTexture(() -> String.valueOf(SpriteContents.this.id) + " animation frame " + j, 5, TextureFormat.RGBA8, SpriteContents.this.width, SpriteContents.this.height, 1, SpriteContents.this.mipmapLevelsImages.length + 1);
                int k = this.getFrameX(j) * SpriteContents.this.width;
                int l = this.getFrameY(j) * SpriteContents.this.height;
                for (int m = 0; m < SpriteContents.this.mipmapLevelsImages.length; ++m) {
                    RenderSystem.getDevice().createCommandEncoder().writeToTexture(gpuTexture, SpriteContents.this.mipmapLevelsImages[m], m, 0, 0, 0, SpriteContents.this.width >> m, SpriteContents.this.height >> m, k >> m, l >> m);
                }
                int2ObjectMap.put(j, RenderSystem.getDevice().createTextureView(gpuTexture));
            }
            for (int n = 0; n < SpriteContents.this.mipmapLevelsImages.length; ++n) {
                gpuBufferSlices[n] = bufferSlice.slice(n * animationInfoSize, animationInfoSize);
            }
            return new Animator(SpriteContents.this, this, int2ObjectMap, gpuBufferSlices);
        }

        public IntStream getDistinctFrameCount() {
            return this.frames.stream().mapToInt(frame -> frame.index).distinct();
        }
    }

    @Environment(value=EnvType.CLIENT)
    record AnimationFrame(int index, int time) {
    }

    @Environment(value=EnvType.CLIENT)
    public class Animator
    implements AutoCloseable {
        private int frame;
        private int elapsedTimeInFrame;
        private final Animation animation;
        private final Int2ObjectMap<GpuTextureView> textureViewsByFrame;
        private final GpuBufferSlice[] animationInfosByFrame;
        private boolean changedFrame = true;

        Animator(SpriteContents arg, Animation animation, Int2ObjectMap<GpuTextureView> textureViewsByFrame, GpuBufferSlice[] bufferSlices) {
            this.animation = animation;
            this.textureViewsByFrame = textureViewsByFrame;
            this.animationInfosByFrame = bufferSlices;
        }

        public void tick() {
            ++this.elapsedTimeInFrame;
            this.changedFrame = false;
            AnimationFrame lv = this.animation.frames.get(this.frame);
            if (this.elapsedTimeInFrame >= lv.time) {
                int i = lv.index;
                this.frame = (this.frame + 1) % this.animation.frames.size();
                this.elapsedTimeInFrame = 0;
                int j = this.animation.frames.get((int)this.frame).index;
                if (i != j) {
                    this.changedFrame = true;
                }
            }
        }

        public GpuBufferSlice getBufferSlice(int frame) {
            return this.animationInfosByFrame[frame];
        }

        public boolean isDirty() {
            return this.animation.interpolated || this.changedFrame;
        }

        public void upload(RenderPass renderPass, GpuBufferSlice bufferSlice) {
            GpuSampler lv = RenderSystem.getSamplerCache().get(FilterMode.NEAREST, true);
            List<AnimationFrame> list = this.animation.frames;
            int i = list.get((int)this.frame).index;
            float f = (float)this.elapsedTimeInFrame / (float)this.animation.frames.get((int)this.frame).time;
            int j = (int)(f * 1000.0f);
            if (this.animation.interpolated) {
                int k = list.get((int)((this.frame + 1) % list.size())).index;
                renderPass.setPipeline(RenderPipelines.ANIMATE_SPRITE_INTERPOLATE);
                renderPass.bindTexture("CurrentSprite", (GpuTextureView)this.textureViewsByFrame.get(i), lv);
                renderPass.bindTexture("NextSprite", (GpuTextureView)this.textureViewsByFrame.get(k), lv);
            } else if (this.changedFrame) {
                renderPass.setPipeline(RenderPipelines.ANIMATE_SPRITE_BLIT);
                renderPass.bindTexture("Sprite", (GpuTextureView)this.textureViewsByFrame.get(i), lv);
            }
            renderPass.setUniform("SpriteAnimationInfo", bufferSlice);
            renderPass.draw(j << 3, 6);
        }

        @Override
        public void close() {
            for (GpuTextureView gpuTextureView : this.textureViewsByFrame.values()) {
                gpuTextureView.texture().close();
                gpuTextureView.close();
            }
        }
    }
}

