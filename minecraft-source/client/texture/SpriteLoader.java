/*
 * External method calls:
 *   Lnet/minecraft/util/profiler/Profiler;scoped(Ljava/util/function/Supplier;)Lnet/minecraft/util/profiler/ScopedProfiler;
 *   Lnet/minecraft/util/crash/CrashReport;create(Ljava/lang/Throwable;Ljava/lang/String;)Lnet/minecraft/util/crash/CrashReport;
 *   Lnet/minecraft/util/crash/CrashReport;addElement(Ljava/lang/String;)Lnet/minecraft/util/crash/CrashReportSection;
 *   Lnet/minecraft/util/Util;combineSafe(Ljava/util/List;)Ljava/util/concurrent/CompletableFuture;
 *   Lnet/minecraft/client/texture/SpriteOpener;create(Ljava/util/Set;)Lnet/minecraft/client/texture/SpriteOpener;
 *   Lnet/minecraft/client/texture/atlas/AtlasLoader;of(Lnet/minecraft/resource/ResourceManager;Lnet/minecraft/util/Identifier;)Lnet/minecraft/client/texture/atlas/AtlasLoader;
 *   Lnet/minecraft/client/texture/atlas/AtlasLoader;loadSources(Lnet/minecraft/resource/ResourceManager;)Ljava/util/List;
 *   Lnet/minecraft/client/texture/atlas/AtlasSource$SpriteSource;load(Lnet/minecraft/client/texture/SpriteOpener;)Lnet/minecraft/client/texture/SpriteContents;
 *   Lnet/minecraft/client/texture/SpriteContents;generateMipmaps(I)V
 *
 * Internal private/static methods:
 *   Lnet/minecraft/client/texture/SpriteLoader;collectStitchedSprites(Lnet/minecraft/client/texture/TextureStitcher;II)Ljava/util/Map;
 *   Lnet/minecraft/client/texture/SpriteLoader;stitch(Ljava/util/List;ILjava/util/concurrent/Executor;)Lnet/minecraft/client/texture/SpriteLoader$StitchResult;
 *   Lnet/minecraft/client/texture/SpriteLoader;loadAll(Lnet/minecraft/client/texture/SpriteOpener;Ljava/util/List;Ljava/util/concurrent/Executor;)Ljava/util/concurrent/CompletableFuture;
 */
package net.minecraft.client.texture;

import com.mojang.logging.LogUtils;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.TextureFilteringMode;
import net.minecraft.client.texture.MissingSprite;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.texture.SpriteContents;
import net.minecraft.client.texture.SpriteOpener;
import net.minecraft.client.texture.TextureStitcher;
import net.minecraft.client.texture.TextureStitcherCannotFitException;
import net.minecraft.client.texture.atlas.AtlasLoader;
import net.minecraft.client.texture.atlas.AtlasSource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.metadata.ResourceMetadataSerializer;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.crash.CrashException;
import net.minecraft.util.crash.CrashReport;
import net.minecraft.util.crash.CrashReportSection;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.profiler.Profilers;
import net.minecraft.util.profiler.ScopedProfiler;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;

@Environment(value=EnvType.CLIENT)
public class SpriteLoader {
    private static final Logger LOGGER = LogUtils.getLogger();
    private final Identifier id;
    private final int maxTextureSize;

    public SpriteLoader(Identifier id, int maxTextureSize) {
        this.id = id;
        this.maxTextureSize = maxTextureSize;
    }

    public static SpriteLoader fromAtlas(SpriteAtlasTexture atlasTexture) {
        return new SpriteLoader(atlasTexture.getId(), atlasTexture.getMaxTextureSize());
    }

    private StitchResult stitch(List<SpriteContents> sprites, int mipLevel, Executor executor) {
        try (ScopedProfiler lv = Profilers.get().scoped(() -> "stitch " + String.valueOf(this.id));){
            int m;
            int j = this.maxTextureSize;
            int k = Integer.MAX_VALUE;
            int l = 1 << mipLevel;
            for (SpriteContents lv2 : sprites) {
                k = Math.min(k, Math.min(lv2.getWidth(), lv2.getHeight()));
                m = Math.min(Integer.lowestOneBit(lv2.getWidth()), Integer.lowestOneBit(lv2.getHeight()));
                if (m >= l) continue;
                LOGGER.warn("Texture {} with size {}x{} limits mip level from {} to {}", lv2.getId(), lv2.getWidth(), lv2.getHeight(), MathHelper.floorLog2(l), MathHelper.floorLog2(m));
                l = m;
            }
            int n = Math.min(k, l);
            int o = MathHelper.floorLog2(n);
            if (o < mipLevel) {
                LOGGER.warn("{}: dropping miplevel from {} to {}, because of minimum power of two: {}", this.id, mipLevel, o, n);
                m = o;
            } else {
                m = mipLevel;
            }
            GameOptions lv3 = MinecraftClient.getInstance().options;
            int p = m == 0 || lv3.getTextureFiltering().getValue() != TextureFilteringMode.ANISOTROPIC ? 0 : lv3.getMaxAnisotropy().getValue();
            TextureStitcher<SpriteContents> lv4 = new TextureStitcher<SpriteContents>(j, j, m, p);
            for (SpriteContents lv5 : sprites) {
                lv4.add(lv5);
            }
            try {
                lv4.stitch();
            } catch (TextureStitcherCannotFitException lv6) {
                CrashReport lv7 = CrashReport.create(lv6, "Stitching");
                CrashReportSection lv8 = lv7.addElement("Stitcher");
                lv8.add("Sprites", lv6.getSprites().stream().map(sprite -> String.format(Locale.ROOT, "%s[%dx%d]", sprite.getId(), sprite.getWidth(), sprite.getHeight())).collect(Collectors.joining(",")));
                lv8.add("Max Texture Size", j);
                throw new CrashException(lv7);
            }
            int q = lv4.getWidth();
            int r = lv4.getHeight();
            Map<Identifier, Sprite> map = this.collectStitchedSprites(lv4, q, r);
            Sprite lv9 = map.get(MissingSprite.getMissingSpriteId());
            CompletableFuture<Void> completableFuture = CompletableFuture.runAsync(() -> map.values().forEach(sprite -> sprite.getContents().generateMipmaps(m)), executor);
            StitchResult stitchResult = new StitchResult(q, r, m, lv9, map, completableFuture);
            return stitchResult;
        }
    }

    /*
     * Issues handling annotations - annotations may be inaccurate
     */
    private static CompletableFuture<List<SpriteContents>> loadAll(SpriteOpener opener, List<AtlasSource.SpriteSource> sources, Executor executor) {
        List<@Nullable CompletableFuture> list2 = sources.stream().map(source -> CompletableFuture.supplyAsync(() -> source.load(opener), executor)).toList();
        return Util.combineSafe(list2).thenApply(sprites -> sprites.stream().filter(Objects::nonNull).toList());
    }

    public CompletableFuture<StitchResult> load(ResourceManager resourceManager, Identifier path, int mipLevel, Executor executor, Set<ResourceMetadataSerializer<?>> additionalMetadata) {
        SpriteOpener lv = SpriteOpener.create(additionalMetadata);
        return ((CompletableFuture)CompletableFuture.supplyAsync(() -> AtlasLoader.of(resourceManager, path).loadSources(resourceManager), executor).thenCompose(sources -> SpriteLoader.loadAll(lv, sources, executor))).thenApply(sprites -> this.stitch((List<SpriteContents>)sprites, mipLevel, executor));
    }

    private Map<Identifier, Sprite> collectStitchedSprites(TextureStitcher<SpriteContents> stitcher, int atlasWidth, int atlasHeight) {
        HashMap<Identifier, Sprite> map = new HashMap<Identifier, Sprite>();
        stitcher.getStitchedSprites((info, x, y, padding) -> map.put(info.getId(), new Sprite(this.id, (SpriteContents)info, atlasWidth, atlasHeight, x, y, padding)));
        return map;
    }

    @Environment(value=EnvType.CLIENT)
    public record StitchResult(int width, int height, int mipLevel, Sprite missing, Map<Identifier, Sprite> sprites, CompletableFuture<Void> readyForUpload) {
        public @Nullable Sprite getSprite(Identifier id) {
            return this.sprites.get(id);
        }
    }
}

