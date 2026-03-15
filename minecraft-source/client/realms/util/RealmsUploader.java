/*
 * External method calls:
 *   Lnet/minecraft/client/realms/RealmsClient;create()Lnet/minecraft/client/realms/RealmsClient;
 *   Lnet/minecraft/client/realms/RealmsClient;upload(J)Lnet/minecraft/client/realms/dto/UploadInfo;
 *   Lnet/minecraft/client/realms/util/UploadCompressor;compress(Ljava/nio/file/Path;Ljava/util/function/BooleanSupplier;)Ljava/io/File;
 *   Lnet/minecraft/GameVersion;name()Ljava/lang/String;
 *   Lnet/minecraft/client/realms/FileUpload;upload()Ljava/util/concurrent/CompletableFuture;
 *   Lnet/minecraft/client/realms/util/UploadTokenCache;invalidate(J)V
 *   Lnet/minecraft/client/realms/RealmsClient;updateSlot(JILnet/minecraft/client/realms/dto/RealmsWorldOptions;Ljava/util/List;)V
 *
 * Internal private/static methods:
 *   Lnet/minecraft/client/realms/util/RealmsUploader;uploadSync()Lnet/minecraft/client/realms/dto/UploadInfo;
 */
package net.minecraft.client.realms.util;

import com.mojang.logging.LogUtils;
import java.io.File;
import java.nio.file.Path;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.SharedConstants;
import net.minecraft.client.realms.FileUpload;
import net.minecraft.client.realms.RealmsClient;
import net.minecraft.client.realms.dto.RealmsSlot;
import net.minecraft.client.realms.dto.UploadInfo;
import net.minecraft.client.realms.exception.RealmsServiceException;
import net.minecraft.client.realms.exception.RealmsUploadException;
import net.minecraft.client.realms.exception.RetryCallException;
import net.minecraft.client.realms.exception.upload.CancelledRealmsUploadException;
import net.minecraft.client.realms.exception.upload.CloseFailureRealmsUploadException;
import net.minecraft.client.realms.exception.upload.FailedRealmsUploadException;
import net.minecraft.client.realms.util.UploadCompressor;
import net.minecraft.client.realms.util.UploadProgressTracker;
import net.minecraft.client.realms.util.UploadResult;
import net.minecraft.client.realms.util.UploadTokenCache;
import net.minecraft.client.session.Session;
import net.minecraft.util.Util;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;

@Environment(value=EnvType.CLIENT)
public class RealmsUploader {
    private static final Logger LOGGER = LogUtils.getLogger();
    public static final int MAX_ATTEMPTS = 20;
    private final RealmsClient client = RealmsClient.create();
    private final Path directory;
    private final RealmsSlot options;
    private final Session session;
    private final long worldId;
    private final UploadProgressTracker progressTracker;
    private volatile boolean cancelled;
    private volatile @Nullable CompletableFuture<?> upload;

    public RealmsUploader(Path directory, RealmsSlot options, Session session, long worldId, UploadProgressTracker progressTracker) {
        this.directory = directory;
        this.options = options;
        this.session = session;
        this.worldId = worldId;
        this.progressTracker = progressTracker;
    }

    public CompletableFuture<?> upload() {
        return CompletableFuture.runAsync(() -> {
            File file = null;
            try {
                UploadInfo lv = this.uploadSync();
                file = UploadCompressor.compress(this.directory, () -> this.cancelled);
                this.progressTracker.updateProgressDisplay();
                try (FileUpload lv2 = new FileUpload(file, this.worldId, this.options.slotId, lv, this.session, SharedConstants.getGameVersion().name(), this.options.options.version, this.progressTracker.getUploadProgress());){
                    UploadResult lv3;
                    CompletableFuture<UploadResult> completableFuture = lv2.upload();
                    this.upload = completableFuture;
                    if (this.cancelled) {
                        completableFuture.cancel(true);
                        return;
                    }
                    try {
                        lv3 = completableFuture.join();
                    } catch (CompletionException completionException) {
                        throw completionException.getCause();
                    }
                    String string = lv3.getErrorMessage();
                    if (string != null) {
                        throw new FailedRealmsUploadException(string);
                    }
                    UploadTokenCache.invalidate(this.worldId);
                    this.client.updateSlot(this.worldId, this.options.slotId, this.options.options, this.options.settings);
                }
            } catch (RealmsServiceException lv4) {
                throw new FailedRealmsUploadException(lv4.error.getText());
            } catch (InterruptedException | CancellationException exception) {
                throw new CancelledRealmsUploadException();
            } catch (RealmsUploadException lv5) {
                throw lv5;
            } catch (Throwable throwable) {
                if (throwable instanceof Error) {
                    Error error = (Error)throwable;
                    throw error;
                }
                throw new FailedRealmsUploadException(throwable.getMessage());
            } finally {
                if (file != null) {
                    LOGGER.debug("Deleting file {}", (Object)file.getAbsolutePath());
                    file.delete();
                }
            }
        }, Util.getMainWorkerExecutor());
    }

    public void cancel() {
        this.cancelled = true;
        CompletableFuture<?> completableFuture = this.upload;
        if (completableFuture != null) {
            completableFuture.cancel(true);
        }
    }

    private UploadInfo uploadSync() throws RealmsServiceException, InterruptedException {
        for (int i = 0; i < 20; ++i) {
            try {
                UploadInfo lv = this.client.upload(this.worldId);
                if (this.cancelled) {
                    throw new CancelledRealmsUploadException();
                }
                if (lv == null) continue;
                if (!lv.worldClosed()) {
                    throw new CloseFailureRealmsUploadException();
                }
                return lv;
            } catch (RetryCallException lv2) {
                Thread.sleep((long)lv2.delaySeconds * 1000L);
            }
        }
        throw new CloseFailureRealmsUploadException();
    }
}

