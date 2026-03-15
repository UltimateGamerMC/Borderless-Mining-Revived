/*
 * External method calls:
 *   Lnet/minecraft/util/thread/NameableExecutor;named(Ljava/lang/String;)Ljava/util/concurrent/Executor;
 *   Lnet/minecraft/client/texture/NativeImage;read(Ljava/io/InputStream;)Lnet/minecraft/client/texture/NativeImage;
 *   Lnet/minecraft/util/path/PathUtil;createDirectories(Ljava/nio/file/Path;)V
 *   Lnet/minecraft/client/texture/NativeImage;read([B)Lnet/minecraft/client/texture/NativeImage;
 *   Lnet/minecraft/client/texture/NativeImage;copyFrom(Lnet/minecraft/client/texture/NativeImage;)V
 *   Lnet/minecraft/client/texture/NativeImage;fillRect(IIIII)V
 *   Lnet/minecraft/client/texture/NativeImage;copyRect(IIIIIIZZ)V
 *   Lnet/minecraft/util/AssetInfo$TextureAsset;texturePath()Lnet/minecraft/util/Identifier;
 *   Lnet/minecraft/client/texture/TextureManager;registerTexture(Lnet/minecraft/util/Identifier;Lnet/minecraft/client/texture/AbstractTexture;)V
 *   Lnet/minecraft/util/AssetInfo$SkinAssetInfo;url()Ljava/lang/String;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/client/texture/PlayerSkinTextureDownloader;stripAlpha(Lnet/minecraft/client/texture/NativeImage;IIII)V
 *   Lnet/minecraft/client/texture/PlayerSkinTextureDownloader;stripColor(Lnet/minecraft/client/texture/NativeImage;IIII)V
 *   Lnet/minecraft/client/texture/PlayerSkinTextureDownloader;registerTexture(Lnet/minecraft/util/AssetInfo$TextureAsset;Lnet/minecraft/client/texture/NativeImage;)Ljava/util/concurrent/CompletableFuture;
 *   Lnet/minecraft/client/texture/PlayerSkinTextureDownloader;download(Ljava/nio/file/Path;Ljava/lang/String;)Lnet/minecraft/client/texture/NativeImage;
 *   Lnet/minecraft/client/texture/PlayerSkinTextureDownloader;remapTexture(Lnet/minecraft/client/texture/NativeImage;Ljava/lang/String;)Lnet/minecraft/client/texture/NativeImage;
 */
package net.minecraft.client.texture;

import com.mojang.logging.LogUtils;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.util.AssetInfo;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.path.PathUtil;
import org.slf4j.Logger;

@Environment(value=EnvType.CLIENT)
public class PlayerSkinTextureDownloader {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final int SKIN_WIDTH = 64;
    private static final int SKIN_HEIGHT = 64;
    private static final int OLD_SKIN_HEIGHT = 32;
    private final Proxy proxy;
    private final TextureManager textureManager;
    private final Executor executor;

    public PlayerSkinTextureDownloader(Proxy proxy, TextureManager textureManager, Executor executor) {
        this.proxy = proxy;
        this.textureManager = textureManager;
        this.executor = executor;
    }

    public CompletableFuture<AssetInfo.TextureAsset> downloadAndRegisterTexture(Identifier id, Path path, String url, boolean remap) {
        AssetInfo.SkinAssetInfo lv = new AssetInfo.SkinAssetInfo(id, url);
        return CompletableFuture.supplyAsync(() -> {
            NativeImage lv;
            try {
                lv = this.download(path, lv.url());
            } catch (IOException iOException) {
                throw new UncheckedIOException(iOException);
            }
            return remap ? PlayerSkinTextureDownloader.remapTexture(lv, lv.url()) : lv;
        }, Util.getDownloadWorkerExecutor().named("downloadTexture")).thenCompose(image -> this.registerTexture(lv, (NativeImage)image));
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private NativeImage download(Path path, String url) throws IOException {
        if (Files.isRegularFile(path, new LinkOption[0])) {
            LOGGER.debug("Loading HTTP texture from local cache ({})", (Object)path);
            try (InputStream inputStream = Files.newInputStream(path, new OpenOption[0]);){
                NativeImage nativeImage = NativeImage.read(inputStream);
                return nativeImage;
            }
        }
        HttpURLConnection httpURLConnection = null;
        LOGGER.debug("Downloading HTTP texture from {} to {}", (Object)url, (Object)path);
        URI uRI = URI.create(url);
        try {
            httpURLConnection = (HttpURLConnection)uRI.toURL().openConnection(this.proxy);
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(false);
            httpURLConnection.connect();
            int i = httpURLConnection.getResponseCode();
            if (i / 100 != 2) {
                throw new IOException("Failed to open " + String.valueOf(uRI) + ", HTTP error code: " + i);
            }
            byte[] bs = httpURLConnection.getInputStream().readAllBytes();
            try {
                PathUtil.createDirectories(path.getParent());
                Files.write(path, bs, new OpenOption[0]);
            } catch (IOException iOException) {
                LOGGER.warn("Failed to cache texture {} in {}", (Object)url, (Object)path);
            }
            NativeImage nativeImage = NativeImage.read(bs);
            return nativeImage;
        } finally {
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
        }
    }

    private CompletableFuture<AssetInfo.TextureAsset> registerTexture(AssetInfo.TextureAsset arg, NativeImage image) {
        return CompletableFuture.supplyAsync(() -> {
            NativeImageBackedTexture lv = new NativeImageBackedTexture(arg.texturePath()::toString, image);
            this.textureManager.registerTexture(arg.texturePath(), lv);
            return arg;
        }, this.executor);
    }

    private static NativeImage remapTexture(NativeImage image, String uri) {
        boolean bl;
        int i = image.getHeight();
        int j = image.getWidth();
        if (j != 64 || i != 32 && i != 64) {
            image.close();
            throw new IllegalStateException("Discarding incorrectly sized (" + j + "x" + i + ") skin texture from " + uri);
        }
        boolean bl2 = bl = i == 32;
        if (bl) {
            NativeImage lv = new NativeImage(64, 64, true);
            lv.copyFrom(image);
            image.close();
            image = lv;
            image.fillRect(0, 32, 64, 32, 0);
            image.copyRect(4, 16, 16, 32, 4, 4, true, false);
            image.copyRect(8, 16, 16, 32, 4, 4, true, false);
            image.copyRect(0, 20, 24, 32, 4, 12, true, false);
            image.copyRect(4, 20, 16, 32, 4, 12, true, false);
            image.copyRect(8, 20, 8, 32, 4, 12, true, false);
            image.copyRect(12, 20, 16, 32, 4, 12, true, false);
            image.copyRect(44, 16, -8, 32, 4, 4, true, false);
            image.copyRect(48, 16, -8, 32, 4, 4, true, false);
            image.copyRect(40, 20, 0, 32, 4, 12, true, false);
            image.copyRect(44, 20, -8, 32, 4, 12, true, false);
            image.copyRect(48, 20, -16, 32, 4, 12, true, false);
            image.copyRect(52, 20, -8, 32, 4, 12, true, false);
        }
        PlayerSkinTextureDownloader.stripAlpha(image, 0, 0, 32, 16);
        if (bl) {
            PlayerSkinTextureDownloader.stripColor(image, 32, 0, 64, 32);
        }
        PlayerSkinTextureDownloader.stripAlpha(image, 0, 16, 64, 32);
        PlayerSkinTextureDownloader.stripAlpha(image, 16, 48, 48, 64);
        return image;
    }

    private static void stripColor(NativeImage image, int x1, int y1, int x2, int y2) {
        int n;
        int m;
        for (m = x1; m < x2; ++m) {
            for (n = y1; n < y2; ++n) {
                int o = image.getColorArgb(m, n);
                if (ColorHelper.getAlpha(o) >= 128) continue;
                return;
            }
        }
        for (m = x1; m < x2; ++m) {
            for (n = y1; n < y2; ++n) {
                image.setColorArgb(m, n, image.getColorArgb(m, n) & 0xFFFFFF);
            }
        }
    }

    private static void stripAlpha(NativeImage image, int x1, int y1, int x2, int y2) {
        for (int m = x1; m < x2; ++m) {
            for (int n = y1; n < y2; ++n) {
                image.setColorArgb(m, n, ColorHelper.fullAlpha(image.getColorArgb(m, n)));
            }
        }
    }
}

