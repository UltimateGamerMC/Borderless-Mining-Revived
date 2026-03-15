/*
 * External method calls:
 *   Lnet/minecraft/util/thread/NameableExecutor;named(Ljava/lang/String;)Ljava/util/concurrent/Executor;
 *   Lnet/minecraft/GameVersion;dataVersion()Lnet/minecraft/SaveVersion;
 *   Lnet/minecraft/server/world/ChunkErrorHandler;onChunkSaveFailure(Ljava/lang/Throwable;Lnet/minecraft/world/storage/StorageKey;Lnet/minecraft/util/math/ChunkPos;)V
 *   Lnet/minecraft/server/world/ChunkErrorHandler;onChunkLoadFailure(Ljava/lang/Throwable;Lnet/minecraft/world/storage/StorageKey;Lnet/minecraft/util/math/ChunkPos;)V
 *   Lnet/minecraft/world/storage/SerializingRegionBasedStorage$LoadResult;fromNbt(Lcom/mojang/serialization/Codec;Lcom/mojang/serialization/DynamicOps;Lnet/minecraft/nbt/NbtElement;Lnet/minecraft/world/storage/VersionedChunkStorage;Lnet/minecraft/world/HeightLimitView;)Lnet/minecraft/world/storage/SerializingRegionBasedStorage$LoadResult;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/world/storage/SerializingRegionBasedStorage;save(Lnet/minecraft/util/math/ChunkPos;)V
 *   Lnet/minecraft/world/storage/SerializingRegionBasedStorage;onLoad(Lnet/minecraft/util/math/ChunkPos;Lnet/minecraft/world/storage/SerializingRegionBasedStorage$LoadResult;)V
 *   Lnet/minecraft/world/storage/SerializingRegionBasedStorage;loadAndWait(Lnet/minecraft/util/math/ChunkPos;)V
 *   Lnet/minecraft/world/storage/SerializingRegionBasedStorage;chunkSectionPosAsLong(Lnet/minecraft/util/math/ChunkPos;I)J
 *   Lnet/minecraft/world/storage/SerializingRegionBasedStorage;serialize(Lnet/minecraft/util/math/ChunkPos;Lcom/mojang/serialization/DynamicOps;)Lcom/mojang/serialization/Dynamic;
 *   Lnet/minecraft/world/storage/SerializingRegionBasedStorage;onLoad(J)V
 *   Lnet/minecraft/world/storage/SerializingRegionBasedStorage;onUpdate(J)V
 *   Lnet/minecraft/world/storage/SerializingRegionBasedStorage;loadNbt(Lnet/minecraft/util/math/ChunkPos;)Ljava/util/concurrent/CompletableFuture;
 */
package net.minecraft.world.storage;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.OptionalDynamic;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectMaps;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.longs.LongLinkedOpenHashSet;
import it.unimi.dsi.fastutil.longs.LongListIterator;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import it.unimi.dsi.fastutil.longs.LongSet;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import java.io.IOException;
import java.util.HashMap;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.function.BiFunction;
import java.util.function.BooleanSupplier;
import java.util.function.Function;
import net.minecraft.SharedConstants;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtOps;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.RegistryOps;
import net.minecraft.server.world.ChunkErrorHandler;
import net.minecraft.util.Util;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.ChunkSectionPos;
import net.minecraft.world.HeightLimitView;
import net.minecraft.world.storage.VersionedChunkStorage;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;

public class SerializingRegionBasedStorage<R, P>
implements AutoCloseable {
    static final Logger LOGGER = LogUtils.getLogger();
    private static final String SECTIONS_KEY = "Sections";
    private final VersionedChunkStorage storageAccess;
    private final Long2ObjectMap<Optional<R>> loadedElements = new Long2ObjectOpenHashMap<Optional<R>>();
    private final LongLinkedOpenHashSet unsavedElements = new LongLinkedOpenHashSet();
    private final Codec<P> codec;
    private final Function<R, P> serializer;
    private final BiFunction<P, Runnable, R> deserializer;
    private final Function<Runnable, R> factory;
    private final DynamicRegistryManager registryManager;
    private final ChunkErrorHandler errorHandler;
    protected final HeightLimitView world;
    private final LongSet loadedChunks = new LongOpenHashSet();
    private final Long2ObjectMap<CompletableFuture<Optional<LoadResult<P>>>> pendingLoads = new Long2ObjectOpenHashMap<CompletableFuture<Optional<LoadResult<P>>>>();
    private final Object lock = new Object();

    public SerializingRegionBasedStorage(VersionedChunkStorage storageAccess, Codec<P> codec, Function<R, P> serializer, BiFunction<P, Runnable, R> deserializer, Function<Runnable, R> factory, DynamicRegistryManager registryManager, ChunkErrorHandler errorHandler, HeightLimitView world) {
        this.storageAccess = storageAccess;
        this.codec = codec;
        this.serializer = serializer;
        this.deserializer = deserializer;
        this.factory = factory;
        this.registryManager = registryManager;
        this.errorHandler = errorHandler;
        this.world = world;
    }

    protected void tick(BooleanSupplier shouldKeepTicking) {
        LongListIterator longIterator = this.unsavedElements.iterator();
        while (longIterator.hasNext() && shouldKeepTicking.getAsBoolean()) {
            ChunkPos lv = new ChunkPos(longIterator.nextLong());
            longIterator.remove();
            this.save(lv);
        }
        this.tickPendingLoads();
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private void tickPendingLoads() {
        Object object = this.lock;
        synchronized (object) {
            ObjectIterator<Long2ObjectMap.Entry<CompletableFuture<Optional<LoadResult<P>>>>> iterator = Long2ObjectMaps.fastIterator(this.pendingLoads);
            while (iterator.hasNext()) {
                Long2ObjectMap.Entry entry = (Long2ObjectMap.Entry)iterator.next();
                Optional optional = ((CompletableFuture)entry.getValue()).getNow(null);
                if (optional == null) continue;
                long l = entry.getLongKey();
                this.onLoad(new ChunkPos(l), optional.orElse(null));
                iterator.remove();
                this.loadedChunks.add(l);
            }
        }
    }

    public void save() {
        if (!this.unsavedElements.isEmpty()) {
            this.unsavedElements.forEach(chunkPos -> this.save(new ChunkPos(chunkPos)));
            this.unsavedElements.clear();
        }
    }

    public boolean hasUnsavedElements() {
        return !this.unsavedElements.isEmpty();
    }

    protected @Nullable Optional<R> getIfLoaded(long pos) {
        return (Optional)this.loadedElements.get(pos);
    }

    protected Optional<R> get(long pos) {
        if (this.isPosInvalid(pos)) {
            return Optional.empty();
        }
        Optional<R> optional = this.getIfLoaded(pos);
        if (optional != null) {
            return optional;
        }
        this.loadAndWait(ChunkSectionPos.from(pos).toChunkPos());
        optional = this.getIfLoaded(pos);
        if (optional == null) {
            throw Util.getFatalOrPause(new IllegalStateException());
        }
        return optional;
    }

    protected boolean isPosInvalid(long pos) {
        int i = ChunkSectionPos.getBlockCoord(ChunkSectionPos.unpackY(pos));
        return this.world.isOutOfHeightLimit(i);
    }

    protected R getOrCreate(long pos) {
        if (this.isPosInvalid(pos)) {
            throw Util.getFatalOrPause(new IllegalArgumentException("sectionPos out of bounds"));
        }
        Optional<R> optional = this.get(pos);
        if (optional.isPresent()) {
            return optional.get();
        }
        R object = this.factory.apply(() -> this.onUpdate(pos));
        this.loadedElements.put(pos, Optional.of(object));
        return object;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public CompletableFuture<?> load(ChunkPos chunkPos) {
        Object object = this.lock;
        synchronized (object) {
            long l = chunkPos.toLong();
            if (this.loadedChunks.contains(l)) {
                return CompletableFuture.completedFuture(null);
            }
            return this.pendingLoads.computeIfAbsent(l, pos -> this.loadNbt(chunkPos));
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private void loadAndWait(ChunkPos chunkPos) {
        CompletableFuture completableFuture;
        long l = chunkPos.toLong();
        Object object = this.lock;
        synchronized (object) {
            if (!this.loadedChunks.add(l)) {
                return;
            }
            completableFuture = this.pendingLoads.computeIfAbsent(l, pos -> this.loadNbt(chunkPos));
        }
        this.onLoad(chunkPos, ((Optional)completableFuture.join()).orElse(null));
        object = this.lock;
        synchronized (object) {
            this.pendingLoads.remove(l);
        }
    }

    private CompletableFuture<Optional<LoadResult<P>>> loadNbt(ChunkPos chunkPos) {
        RegistryOps<NbtElement> lv = this.registryManager.getOps(NbtOps.INSTANCE);
        return ((CompletableFuture)this.storageAccess.getNbt(chunkPos).thenApplyAsync(chunkNbt -> chunkNbt.map(nbt -> LoadResult.fromNbt(this.codec, lv, nbt, this.storageAccess, this.world)), Util.getMainWorkerExecutor().named("parseSection"))).exceptionally(throwable -> {
            if (throwable instanceof CompletionException) {
                throwable = throwable.getCause();
            }
            if (throwable instanceof IOException) {
                IOException iOException = (IOException)throwable;
                LOGGER.error("Error reading chunk {} data from disk", (Object)chunkPos, (Object)iOException);
                this.errorHandler.onChunkLoadFailure(iOException, this.storageAccess.getStorageKey(), chunkPos);
                return Optional.empty();
            }
            throw new CompletionException((Throwable)throwable);
        });
    }

    private void onLoad(ChunkPos chunkPos, @Nullable LoadResult<P> result) {
        if (result == null) {
            for (int i = this.world.getBottomSectionCoord(); i <= this.world.getTopSectionCoord(); ++i) {
                this.loadedElements.put(SerializingRegionBasedStorage.chunkSectionPosAsLong(chunkPos, i), (Optional<R>)Optional.empty());
            }
        } else {
            boolean bl = result.versionChanged();
            for (int j = this.world.getBottomSectionCoord(); j <= this.world.getTopSectionCoord(); ++j) {
                long l = SerializingRegionBasedStorage.chunkSectionPosAsLong(chunkPos, j);
                Optional<Object> optional = Optional.ofNullable(result.sectionsByY.get(j)).map(section -> this.deserializer.apply(section, () -> this.onUpdate(l)));
                this.loadedElements.put(l, (Optional<R>)optional);
                optional.ifPresent(object -> {
                    this.onLoad(l);
                    if (bl) {
                        this.onUpdate(l);
                    }
                });
            }
        }
    }

    private void save(ChunkPos pos) {
        RegistryOps<NbtElement> lv = this.registryManager.getOps(NbtOps.INSTANCE);
        Dynamic<NbtElement> dynamic = this.serialize(pos, lv);
        NbtElement lv2 = dynamic.getValue();
        if (lv2 instanceof NbtCompound) {
            NbtCompound lv3 = (NbtCompound)lv2;
            this.storageAccess.setNbt(pos, lv3).exceptionally(throwable -> {
                this.errorHandler.onChunkSaveFailure((Throwable)throwable, this.storageAccess.getStorageKey(), pos);
                return null;
            });
        } else {
            LOGGER.error("Expected compound tag, got {}", (Object)lv2);
        }
    }

    private <T> Dynamic<T> serialize(ChunkPos chunkPos, DynamicOps<T> ops) {
        HashMap map = Maps.newHashMap();
        for (int i = this.world.getBottomSectionCoord(); i <= this.world.getTopSectionCoord(); ++i) {
            long l = SerializingRegionBasedStorage.chunkSectionPosAsLong(chunkPos, i);
            Optional optional = (Optional)this.loadedElements.get(l);
            if (optional == null || optional.isEmpty()) continue;
            DataResult<T> dataResult = this.codec.encodeStart(ops, this.serializer.apply(optional.get()));
            String string = Integer.toString(i);
            dataResult.resultOrPartial(LOGGER::error).ifPresent(value -> map.put(ops.createString(string), value));
        }
        return new Dynamic<T>(ops, ops.createMap(ImmutableMap.of(ops.createString(SECTIONS_KEY), ops.createMap(map), ops.createString("DataVersion"), ops.createInt(SharedConstants.getGameVersion().dataVersion().id()))));
    }

    private static long chunkSectionPosAsLong(ChunkPos chunkPos, int y) {
        return ChunkSectionPos.asLong(chunkPos.x, y, chunkPos.z);
    }

    protected void onLoad(long pos) {
    }

    protected void onUpdate(long pos) {
        Optional optional = (Optional)this.loadedElements.get(pos);
        if (optional == null || optional.isEmpty()) {
            LOGGER.warn("No data for position: {}", (Object)ChunkSectionPos.from(pos));
            return;
        }
        this.unsavedElements.add(ChunkPos.toLong(ChunkSectionPos.unpackX(pos), ChunkSectionPos.unpackZ(pos)));
    }

    public void saveChunk(ChunkPos pos) {
        if (this.unsavedElements.remove(pos.toLong())) {
            this.save(pos);
        }
    }

    @Override
    public void close() throws IOException {
        this.storageAccess.close();
    }

    record LoadResult<T>(Int2ObjectMap<T> sectionsByY, boolean versionChanged) {
        public static <T> LoadResult<T> fromNbt(Codec<T> sectionCodec, DynamicOps<NbtElement> ops, NbtElement nbt, VersionedChunkStorage storage, HeightLimitView world) {
            Dynamic<NbtElement> dynamic = new Dynamic<NbtElement>(ops, nbt);
            Dynamic<NbtElement> dynamic2 = storage.updateChunkNbt(dynamic, 1945);
            boolean bl = dynamic != dynamic2;
            OptionalDynamic<NbtElement> optionalDynamic = dynamic2.get(SerializingRegionBasedStorage.SECTIONS_KEY);
            Int2ObjectOpenHashMap int2ObjectMap = new Int2ObjectOpenHashMap();
            for (int i = world.getBottomSectionCoord(); i <= world.getTopSectionCoord(); ++i) {
                Optional optional = optionalDynamic.get(Integer.toString(i)).result().flatMap(section -> sectionCodec.parse(section).resultOrPartial(LOGGER::error));
                if (!optional.isPresent()) continue;
                int2ObjectMap.put(i, optional.get());
            }
            return new LoadResult(int2ObjectMap, bl);
        }
    }
}

