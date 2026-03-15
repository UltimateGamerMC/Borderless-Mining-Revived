/*
 * External method calls:
 *   Lnet/minecraft/client/texture/SpriteLoader$StitchResult;sprites()Ljava/util/Map;
 *   Lnet/minecraft/client/texture/Sprite;putSpriteInfo(Ljava/nio/ByteBuffer;IIIII)V
 *   Lnet/minecraft/client/texture/Sprite;createAnimator(Lcom/mojang/blaze3d/buffers/GpuBufferSlice;I)Lnet/minecraft/client/texture/SpriteContents$Animator;
 *   Lnet/minecraft/client/texture/Sprite;upload(Lcom/mojang/blaze3d/textures/GpuTexture;I)V
 *   Lnet/minecraft/util/Identifier;toUnderscoreSeparatedString()Ljava/lang/String;
 *   Lnet/minecraft/client/texture/SpriteContents$Animator;upload(Lcom/mojang/blaze3d/systems/RenderPass;Lcom/mojang/blaze3d/buffers/GpuBufferSlice;)V
 *   Lnet/minecraft/util/Identifier;ofVanilla(Ljava/lang/String;)Lnet/minecraft/util/Identifier;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/client/texture/SpriteAtlasTexture;createTexture(III)V
 *   Lnet/minecraft/client/texture/SpriteAtlasTexture;save(Lnet/minecraft/util/Identifier;Ljava/nio/file/Path;)V
 *   Lnet/minecraft/client/texture/SpriteAtlasTexture;dumpAtlasInfos(Ljava/nio/file/Path;Ljava/lang/String;Ljava/util/Map;)V
 */
package net.minecraft.client.texture;

import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.platform.TextureUtil;
import com.mojang.blaze3d.systems.GpuDevice;
import com.mojang.blaze3d.systems.RenderPass;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.FilterMode;
import com.mojang.blaze3d.textures.GpuTexture;
import com.mojang.blaze3d.textures.GpuTextureView;
import com.mojang.blaze3d.textures.TextureFormat;
import com.mojang.logging.LogUtils;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.attribute.FileAttribute;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.OptionalInt;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.SharedConstants;
import net.minecraft.client.gl.GpuSampler;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.texture.AbstractTexture;
import net.minecraft.client.texture.DynamicTexture;
import net.minecraft.client.texture.MissingSprite;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.SpriteContents;
import net.minecraft.client.texture.SpriteLoader;
import net.minecraft.client.texture.TextureTickListener;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import org.jspecify.annotations.Nullable;
import org.lwjgl.system.MemoryUtil;
import org.slf4j.Logger;

@Environment(value=EnvType.CLIENT)
public class SpriteAtlasTexture
extends AbstractTexture
implements DynamicTexture,
TextureTickListener {
    private static final Logger LOGGER = LogUtils.getLogger();
    @Deprecated
    public static final Identifier BLOCK_ATLAS_TEXTURE = Identifier.ofVanilla("textures/atlas/blocks.png");
    @Deprecated
    public static final Identifier ITEMS_ATLAS_TEXTURE = Identifier.ofVanilla("textures/atlas/items.png");
    @Deprecated
    public static final Identifier PARTICLE_ATLAS_TEXTURE = Identifier.ofVanilla("textures/atlas/particles.png");
    private List<Sprite> spritesToLoad = List.of();
    private List<SpriteContents.Animator> animators = List.of();
    private Map<Identifier, Sprite> sprites = Map.of();
    private @Nullable Sprite missingSprite;
    private final Identifier id;
    private final int maxTextureSize;
    private int width;
    private int height;
    private int mipLevel;
    private int numMipLevels;
    private GpuTextureView[] mipTextures = new GpuTextureView[0];
    private @Nullable GpuBuffer uniformBuffer;

    public SpriteAtlasTexture(Identifier id) {
        this.id = id;
        this.maxTextureSize = RenderSystem.getDevice().getMaxTextureSize();
    }

    private void createTexture(int width, int height, int mipLevel) {
        LOGGER.info("Created: {}x{}x{} {}-atlas", width, height, mipLevel, this.id);
        GpuDevice gpuDevice = RenderSystem.getDevice();
        this.close();
        this.glTexture = gpuDevice.createTexture(this.id::toString, 15, TextureFormat.RGBA8, width, height, 1, mipLevel + 1);
        this.glTextureView = gpuDevice.createTextureView(this.glTexture);
        this.width = width;
        this.height = height;
        this.mipLevel = mipLevel;
        this.numMipLevels = mipLevel + 1;
        this.mipTextures = new GpuTextureView[this.numMipLevels];
        for (int l = 0; l <= this.mipLevel; ++l) {
            this.mipTextures[l] = gpuDevice.createTextureView(this.glTexture, l, 1);
        }
    }

    public void create(SpriteLoader.StitchResult stitchResult) {
        this.createTexture(stitchResult.width(), stitchResult.height(), stitchResult.mipLevel());
        this.clear();
        this.sampler = RenderSystem.getSamplerCache().get(FilterMode.NEAREST);
        this.sprites = Map.copyOf(stitchResult.sprites());
        this.missingSprite = this.sprites.get(MissingSprite.getMissingSpriteId());
        if (this.missingSprite == null) {
            throw new IllegalStateException("Atlas '" + String.valueOf(this.id) + "' (" + this.sprites.size() + " sprites) has no missing texture sprite");
        }
        ArrayList<Sprite> list = new ArrayList<Sprite>();
        ArrayList<SpriteContents.Animator> list2 = new ArrayList<SpriteContents.Animator>();
        int i = (int)stitchResult.sprites().values().stream().filter(Sprite::isAnimated).count();
        int j = MathHelper.roundUpToMultiple(SpriteContents.SPRITE_INFO_SIZE, RenderSystem.getDevice().getUniformOffsetAlignment());
        int k = j * this.numMipLevels;
        ByteBuffer byteBuffer = MemoryUtil.memAlloc(i * k);
        int l = 0;
        for (Sprite sprite : stitchResult.sprites().values()) {
            if (!sprite.isAnimated()) continue;
            sprite.putSpriteInfo(byteBuffer, l * k, this.mipLevel, this.width, this.height, j);
            ++l;
        }
        GpuBuffer gpuBuffer = l > 0 ? RenderSystem.getDevice().createBuffer(() -> String.valueOf(this.id) + " sprite UBOs", GpuBuffer.USAGE_UNIFORM, byteBuffer) : null;
        l = 0;
        for (Sprite lv2 : stitchResult.sprites().values()) {
            list.add(lv2);
            if (!lv2.isAnimated() || gpuBuffer == null) continue;
            SpriteContents.Animator lv3 = lv2.createAnimator(gpuBuffer.slice(l * k, k), j);
            ++l;
            if (lv3 == null) continue;
            list2.add(lv3);
        }
        this.uniformBuffer = gpuBuffer;
        this.spritesToLoad = list;
        this.animators = List.copyOf(list2);
        this.upload();
        if (SharedConstants.DUMP_TEXTURE_ATLAS) {
            Path path = TextureUtil.getDebugTexturePath();
            try {
                Files.createDirectories(path, new FileAttribute[0]);
                this.save(this.id, path);
            } catch (Exception exception) {
                LOGGER.warn("Failed to dump atlas contents to {}", (Object)path);
            }
        }
    }

    private void upload() {
        GpuDevice gpuDevice = RenderSystem.getDevice();
        int i = MathHelper.roundUpToMultiple(SpriteContents.SPRITE_INFO_SIZE, RenderSystem.getDevice().getUniformOffsetAlignment());
        int j = i * this.numMipLevels;
        GpuSampler lv = RenderSystem.getSamplerCache().get(FilterMode.NEAREST, true);
        List<Sprite> list = this.spritesToLoad.stream().filter(sprite -> !sprite.isAnimated()).toList();
        ArrayList<GpuTextureView[]> list2 = new ArrayList<GpuTextureView[]>();
        ByteBuffer byteBuffer = MemoryUtil.memAlloc(list.size() * j);
        for (int k = 0; k < list.size(); ++k) {
            Sprite lv2 = list.get(k);
            lv2.putSpriteInfo(byteBuffer, k * j, this.mipLevel, this.width, this.height, i);
            GpuTexture gpuTexture = gpuDevice.createTexture(() -> lv2.getContents().getId().toString(), 5, TextureFormat.RGBA8, lv2.getContents().getWidth(), lv2.getContents().getHeight(), 1, this.numMipLevels);
            GpuTextureView[] gpuTextureViews = new GpuTextureView[this.numMipLevels];
            for (int l = 0; l <= this.mipLevel; ++l) {
                lv2.upload(gpuTexture, l);
                gpuTextureViews[l] = gpuDevice.createTextureView(gpuTexture);
            }
            list2.add(gpuTextureViews);
        }
        try (GpuBuffer gpuBuffer = gpuDevice.createBuffer(() -> "SpriteAnimationInfo", GpuBuffer.USAGE_UNIFORM, byteBuffer);){
            for (int m = 0; m < this.numMipLevels; ++m) {
                try (RenderPass renderPass = RenderSystem.getDevice().createCommandEncoder().createRenderPass(() -> "Animate " + String.valueOf(this.id), this.mipTextures[m], OptionalInt.empty());){
                    renderPass.setPipeline(RenderPipelines.ANIMATE_SPRITE_BLIT);
                    for (int n = 0; n < list.size(); ++n) {
                        renderPass.bindTexture("Sprite", ((GpuTextureView[])list2.get(n))[m], lv);
                        renderPass.setUniform("SpriteAnimationInfo", gpuBuffer.slice(n * j + m * i, SpriteContents.SPRITE_INFO_SIZE));
                        renderPass.draw(0, 6);
                    }
                    continue;
                }
            }
        }
        Iterator iterator = list2.iterator();
        while (iterator.hasNext()) {
            GpuTextureView[] gpuTextureViews2;
            for (GpuTextureView gpuTextureView : gpuTextureViews2 = (GpuTextureView[])iterator.next()) {
                gpuTextureView.close();
                gpuTextureView.texture().close();
            }
        }
        MemoryUtil.memFree(byteBuffer);
        this.uploadAnimations();
    }

    @Override
    public void save(Identifier id, Path path) throws IOException {
        String string = id.toUnderscoreSeparatedString();
        TextureUtil.writeAsPNG(path, string, this.getGlTexture(), this.mipLevel, color -> color);
        SpriteAtlasTexture.dumpAtlasInfos(path, string, this.sprites);
    }

    private static void dumpAtlasInfos(Path path, String id, Map<Identifier, Sprite> sprites) {
        Path path2 = path.resolve(id + ".txt");
        try (BufferedWriter writer = Files.newBufferedWriter(path2, new OpenOption[0]);){
            for (Map.Entry entry : sprites.entrySet().stream().sorted(Map.Entry.comparingByKey()).toList()) {
                Sprite lv = (Sprite)entry.getValue();
                writer.write(String.format(Locale.ROOT, "%s\tx=%d\ty=%d\tw=%d\th=%d%n", entry.getKey(), lv.getX(), lv.getY(), lv.getContents().getWidth(), lv.getContents().getHeight()));
            }
        } catch (IOException iOException) {
            LOGGER.warn("Failed to write file {}", (Object)path2, (Object)iOException);
        }
    }

    public void tickAnimatedSprites() {
        if (this.glTexture == null) {
            return;
        }
        for (SpriteContents.Animator lv : this.animators) {
            lv.tick();
        }
        this.uploadAnimations();
    }

    private void uploadAnimations() {
        if (this.animators.stream().anyMatch(SpriteContents.Animator::isDirty)) {
            for (int i = 0; i <= this.mipLevel; ++i) {
                try (RenderPass renderPass = RenderSystem.getDevice().createCommandEncoder().createRenderPass(() -> "Animate " + String.valueOf(this.id), this.mipTextures[i], OptionalInt.empty());){
                    for (SpriteContents.Animator lv : this.animators) {
                        if (!lv.isDirty()) continue;
                        lv.upload(renderPass, lv.getBufferSlice(i));
                    }
                    continue;
                }
            }
        }
    }

    @Override
    public void tick() {
        this.tickAnimatedSprites();
    }

    public Sprite getSprite(Identifier id) {
        Sprite lv = this.sprites.getOrDefault(id, this.missingSprite);
        if (lv == null) {
            throw new IllegalStateException("Tried to lookup sprite, but atlas is not initialized");
        }
        return lv;
    }

    public Sprite getMissingSprite() {
        return Objects.requireNonNull(this.missingSprite, "Atlas not initialized");
    }

    public void clear() {
        this.spritesToLoad.forEach(Sprite::close);
        this.spritesToLoad = List.of();
        this.animators = List.of();
        this.sprites = Map.of();
        this.missingSprite = null;
    }

    @Override
    public void close() {
        super.close();
        for (GpuTextureView gpuTextureView : this.mipTextures) {
            gpuTextureView.close();
        }
        for (SpriteContents.Animator lv : this.animators) {
            lv.close();
        }
        if (this.uniformBuffer != null) {
            this.uniformBuffer.close();
            this.uniformBuffer = null;
        }
    }

    public Identifier getId() {
        return this.id;
    }

    public int getMaxTextureSize() {
        return this.maxTextureSize;
    }

    int getWidth() {
        return this.width;
    }

    int getHeight() {
        return this.height;
    }
}

