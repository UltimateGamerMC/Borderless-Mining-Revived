/*
 * External method calls:
 *   Lnet/minecraft/world/storage/StorageIoWorker;readChunkData(Lnet/minecraft/util/math/ChunkPos;)Ljava/util/concurrent/CompletableFuture;
 *   Lnet/minecraft/GameVersion;dataVersion()Lnet/minecraft/SaveVersion;
 *   Lnet/minecraft/world/updater/ChunkUpdater;applyFix(Lnet/minecraft/nbt/NbtCompound;)Lnet/minecraft/nbt/NbtCompound;
 *   Lnet/minecraft/datafixer/DataFixTypes;update(Lcom/mojang/datafixers/DataFixer;Lnet/minecraft/nbt/NbtCompound;I)Lnet/minecraft/nbt/NbtCompound;
 *   Lnet/minecraft/nbt/NbtHelper;putDataVersion(Lnet/minecraft/nbt/NbtCompound;)Lnet/minecraft/nbt/NbtCompound;
 *   Lnet/minecraft/util/crash/CrashReport;create(Ljava/lang/Throwable;Ljava/lang/String;)Lnet/minecraft/util/crash/CrashReport;
 *   Lnet/minecraft/util/crash/CrashReport;addElement(Ljava/lang/String;)Lnet/minecraft/util/crash/CrashReportSection;
 *   Lnet/minecraft/world/updater/ChunkUpdater;markChunkDone(Lnet/minecraft/util/math/ChunkPos;)V
 *   Lnet/minecraft/world/storage/StorageIoWorker;completeAll(Z)Ljava/util/concurrent/CompletableFuture;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/world/storage/VersionedChunkStorage;markChunkDone(Lnet/minecraft/util/math/ChunkPos;)V
 *   Lnet/minecraft/world/storage/VersionedChunkStorage;saveContextToNbt(Lnet/minecraft/nbt/NbtCompound;Lnet/minecraft/nbt/NbtCompound;)V
 *   Lnet/minecraft/world/storage/VersionedChunkStorage;removeContext(Lnet/minecraft/nbt/NbtCompound;)V
 *   Lnet/minecraft/world/storage/VersionedChunkStorage;updateChunkNbt(Lnet/minecraft/nbt/NbtCompound;ILnet/minecraft/nbt/NbtCompound;)Lnet/minecraft/nbt/NbtCompound;
 */
package net.minecraft.world.storage;

import com.google.common.base.Suppliers;
import com.mojang.datafixers.DataFixer;
import com.mojang.serialization.Dynamic;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;
import net.minecraft.SharedConstants;
import net.minecraft.datafixer.DataFixTypes;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.util.crash.CrashException;
import net.minecraft.util.crash.CrashReport;
import net.minecraft.util.crash.CrashReportSection;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.storage.NbtScannable;
import net.minecraft.world.storage.StorageIoWorker;
import net.minecraft.world.storage.StorageKey;
import net.minecraft.world.updater.ChunkUpdater;
import org.jspecify.annotations.Nullable;

public class VersionedChunkStorage
implements AutoCloseable {
    private final StorageIoWorker worker;
    private final DataFixer dataFixer;
    private final DataFixTypes dataFixTypes;
    private final Supplier<ChunkUpdater> updaterFactory;

    public VersionedChunkStorage(StorageKey storageKey, Path directory, DataFixer dataFixer, boolean dsync, DataFixTypes dataFixTypes) {
        this(storageKey, directory, dataFixer, dsync, dataFixTypes, ChunkUpdater.PASSTHROUGH_FACTORY);
    }

    public VersionedChunkStorage(StorageKey storageKey, Path directory, DataFixer dataFixer, boolean dsync, DataFixTypes dataFixTypes, Supplier<ChunkUpdater> updaterFactory) {
        this.dataFixer = dataFixer;
        this.dataFixTypes = dataFixTypes;
        this.worker = new StorageIoWorker(storageKey, directory, dsync);
        this.updaterFactory = Suppliers.memoize(updaterFactory::get);
    }

    public boolean needsBlending(ChunkPos chunkPos, int checkRadius) {
        return this.worker.needsBlending(chunkPos, checkRadius);
    }

    public CompletableFuture<Optional<NbtCompound>> getNbt(ChunkPos chunkPos) {
        return this.worker.readChunkData(chunkPos);
    }

    public CompletableFuture<Void> setNbt(ChunkPos chunkPos, NbtCompound chunkTag) {
        return this.set(chunkPos, () -> chunkTag);
    }

    public CompletableFuture<Void> set(ChunkPos chunkPos, Supplier<NbtCompound> chunkTagFactory) {
        this.markChunkDone(chunkPos);
        return this.worker.setResult(chunkPos, chunkTagFactory);
    }

    public NbtCompound updateChunkNbt(NbtCompound chunkNbt, int fallbackVersion, @Nullable NbtCompound context) {
        int j = NbtHelper.getDataVersion(chunkNbt, fallbackVersion);
        if (j == SharedConstants.getGameVersion().dataVersion().id()) {
            return chunkNbt;
        }
        try {
            chunkNbt = this.updaterFactory.get().applyFix(chunkNbt);
            VersionedChunkStorage.saveContextToNbt(chunkNbt, context);
            chunkNbt = this.dataFixTypes.update(this.dataFixer, chunkNbt, Math.max(this.updaterFactory.get().targetDataVersion(), j));
            VersionedChunkStorage.removeContext(chunkNbt);
            NbtHelper.putDataVersion(chunkNbt);
            return chunkNbt;
        } catch (Exception exception) {
            CrashReport lv = CrashReport.create(exception, "Updated chunk");
            CrashReportSection lv2 = lv.addElement("Updated chunk details");
            lv2.add("Data version", j);
            throw new CrashException(lv);
        }
    }

    public NbtCompound updateChunkNbt(NbtCompound chunkNbt, int fallbackVersion) {
        return this.updateChunkNbt(chunkNbt, fallbackVersion, null);
    }

    public Dynamic<NbtElement> updateChunkNbt(Dynamic<NbtElement> chunkNbt, int fallbackVersion) {
        return new Dynamic<NbtElement>(chunkNbt.getOps(), this.updateChunkNbt((NbtCompound)chunkNbt.getValue(), fallbackVersion, null));
    }

    public static void saveContextToNbt(NbtCompound nbt, @Nullable NbtCompound context) {
        if (context != null) {
            nbt.put("__context", context);
        }
    }

    private static void removeContext(NbtCompound nbt) {
        nbt.remove("__context");
    }

    protected void markChunkDone(ChunkPos chunkPos) {
        this.updaterFactory.get().markChunkDone(chunkPos);
    }

    public CompletableFuture<Void> completeAll(boolean sync) {
        return this.worker.completeAll(sync);
    }

    @Override
    public void close() throws IOException {
        this.worker.close();
    }

    public NbtScannable getWorker() {
        return this.worker;
    }

    public StorageKey getStorageKey() {
        return this.worker.getStorageKey();
    }
}

