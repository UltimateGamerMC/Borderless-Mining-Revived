/*
 * External method calls:
 *   Lnet/minecraft/world/PersistentStateType;constructor()Ljava/util/function/Supplier;
 *   Lnet/minecraft/world/PersistentStateType;id()Ljava/lang/String;
 *   Lnet/minecraft/world/PersistentStateType;dataFixType()Lnet/minecraft/datafixer/DataFixTypes;
 *   Lnet/minecraft/GameVersion;dataVersion()Lnet/minecraft/SaveVersion;
 *   Lnet/minecraft/world/PersistentStateType;codec()Lcom/mojang/serialization/Codec;
 *   Lnet/minecraft/nbt/NbtSizeTracker;ofUnlimitedBytes()Lnet/minecraft/nbt/NbtSizeTracker;
 *   Lnet/minecraft/nbt/NbtIo;readCompressed(Ljava/io/InputStream;Lnet/minecraft/nbt/NbtSizeTracker;)Lnet/minecraft/nbt/NbtCompound;
 *   Lnet/minecraft/nbt/NbtIo;readCompound(Ljava/io/DataInput;)Lnet/minecraft/nbt/NbtCompound;
 *   Lnet/minecraft/datafixer/DataFixTypes;update(Lcom/mojang/datafixers/DataFixer;Lnet/minecraft/nbt/NbtCompound;II)Lnet/minecraft/nbt/NbtCompound;
 *   Lnet/minecraft/nbt/NbtHelper;putDataVersion(Lnet/minecraft/nbt/NbtCompound;)Lnet/minecraft/nbt/NbtCompound;
 *   Lnet/minecraft/nbt/NbtIo;writeCompressed(Lnet/minecraft/nbt/NbtCompound;Ljava/nio/file/Path;)V
 *
 * Internal private/static methods:
 *   Lnet/minecraft/world/PersistentStateManager;readFromFile(Lnet/minecraft/world/PersistentStateType;)Lnet/minecraft/world/PersistentState;
 *   Lnet/minecraft/world/PersistentStateManager;readNbt(Ljava/lang/String;Lnet/minecraft/datafixer/DataFixTypes;I)Lnet/minecraft/nbt/NbtCompound;
 *   Lnet/minecraft/world/PersistentStateManager;collectStatesToSave()Ljava/util/Map;
 *   Lnet/minecraft/world/PersistentStateManager;startSaving()Ljava/util/concurrent/CompletableFuture;
 *   Lnet/minecraft/world/PersistentStateManager;encode(Lnet/minecraft/world/PersistentStateType;Lnet/minecraft/world/PersistentState;Lnet/minecraft/registry/RegistryOps;)Lnet/minecraft/nbt/NbtCompound;
 *   Lnet/minecraft/world/PersistentStateManager;save(Lnet/minecraft/world/PersistentStateType;Lnet/minecraft/nbt/NbtCompound;)V
 */
package net.minecraft.world;

import com.google.common.collect.Iterables;
import com.mojang.datafixers.DataFixer;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import net.minecraft.SharedConstants;
import net.minecraft.datafixer.DataFixTypes;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.nbt.NbtIo;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.NbtSizeTracker;
import net.minecraft.registry.RegistryOps;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.FixedBufferInputStream;
import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.PersistentState;
import net.minecraft.world.PersistentStateType;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;

public class PersistentStateManager
implements AutoCloseable {
    private static final Logger LOGGER = LogUtils.getLogger();
    private final Map<PersistentStateType<?>, Optional<PersistentState>> loadedStates = new HashMap();
    private final DataFixer dataFixer;
    private final RegistryWrapper.WrapperLookup registries;
    private final Path directory;
    private CompletableFuture<?> savingFuture = CompletableFuture.completedFuture(null);

    public PersistentStateManager(Path directory, DataFixer dataFixer, RegistryWrapper.WrapperLookup registries) {
        this.dataFixer = dataFixer;
        this.directory = directory;
        this.registries = registries;
    }

    private Path getFile(String id) {
        return this.directory.resolve(id + ".dat");
    }

    public <T extends PersistentState> T getOrCreate(PersistentStateType<T> type) {
        T lv = this.get(type);
        if (lv != null) {
            return lv;
        }
        PersistentState lv2 = (PersistentState)type.constructor().get();
        this.set(type, lv2);
        return (T)lv2;
    }

    public <T extends PersistentState> @Nullable T get(PersistentStateType<T> type) {
        Optional<PersistentState> optional = this.loadedStates.get(type);
        if (optional == null) {
            optional = Optional.ofNullable(this.readFromFile(type));
            this.loadedStates.put(type, optional);
        }
        return (T)((PersistentState)optional.orElse(null));
    }

    private <T extends PersistentState> @Nullable T readFromFile(PersistentStateType<T> type) {
        try {
            Path path = this.getFile(type.id());
            if (Files.exists(path, new LinkOption[0])) {
                NbtCompound lv = this.readNbt(type.id(), type.dataFixType(), SharedConstants.getGameVersion().dataVersion().id());
                RegistryOps<NbtElement> lv2 = this.registries.getOps(NbtOps.INSTANCE);
                return (T)((PersistentState)type.codec().parse(lv2, lv.get("data")).resultOrPartial(error -> LOGGER.error("Failed to parse saved data for '{}': {}", (Object)type, error)).orElse(null));
            }
        } catch (Exception exception) {
            LOGGER.error("Error loading saved data: {}", (Object)type, (Object)exception);
        }
        return null;
    }

    public <T extends PersistentState> void set(PersistentStateType<T> type, T state) {
        this.loadedStates.put(type, Optional.of(state));
        state.markDirty();
    }

    public NbtCompound readNbt(String id, DataFixTypes dataFixTypes, int currentSaveVersion) throws IOException {
        try (InputStream inputStream = Files.newInputStream(this.getFile(id), new OpenOption[0]);){
            NbtCompound nbtCompound;
            try (PushbackInputStream pushbackInputStream = new PushbackInputStream(new FixedBufferInputStream(inputStream), 2);){
                NbtCompound lv;
                if (this.isCompressed(pushbackInputStream)) {
                    lv = NbtIo.readCompressed(pushbackInputStream, NbtSizeTracker.ofUnlimitedBytes());
                } else {
                    try (DataInputStream dataInputStream = new DataInputStream(pushbackInputStream);){
                        lv = NbtIo.readCompound(dataInputStream);
                    }
                }
                int j = NbtHelper.getDataVersion(lv, 1343);
                nbtCompound = dataFixTypes.update(this.dataFixer, lv, j, currentSaveVersion);
            }
            return nbtCompound;
        }
    }

    private boolean isCompressed(PushbackInputStream stream) throws IOException {
        int j;
        byte[] bs = new byte[2];
        boolean bl = false;
        int i = stream.read(bs, 0, 2);
        if (i == 2 && (j = (bs[1] & 0xFF) << 8 | bs[0] & 0xFF) == 35615) {
            bl = true;
        }
        if (i != 0) {
            stream.unread(bs, 0, i);
        }
        return bl;
    }

    public CompletableFuture<?> startSaving() {
        Map<PersistentStateType<?>, NbtCompound> map = this.collectStatesToSave();
        if (map.isEmpty()) {
            return CompletableFuture.completedFuture(null);
        }
        int i = Util.getAvailableBackgroundThreads();
        int j = map.size();
        this.savingFuture = j > i ? this.savingFuture.thenCompose(object -> {
            ArrayList<CompletableFuture<Void>> list = new ArrayList<CompletableFuture<Void>>(i);
            int k = MathHelper.ceilDiv(j, i);
            for (List list2 : Iterables.partition(map.entrySet(), k)) {
                list.add(CompletableFuture.runAsync(() -> {
                    for (Map.Entry entry : list2) {
                        this.save((PersistentStateType)entry.getKey(), (NbtCompound)entry.getValue());
                    }
                }, Util.getIoWorkerExecutor()));
            }
            return CompletableFuture.allOf((CompletableFuture[])list.toArray(CompletableFuture[]::new));
        }) : this.savingFuture.thenCompose(object -> CompletableFuture.allOf((CompletableFuture[])map.entrySet().stream().map(entry -> CompletableFuture.runAsync(() -> this.save((PersistentStateType)entry.getKey(), (NbtCompound)entry.getValue()), Util.getIoWorkerExecutor())).toArray(CompletableFuture[]::new)));
        return this.savingFuture;
    }

    private Map<PersistentStateType<?>, NbtCompound> collectStatesToSave() {
        Object2ObjectArrayMap map = new Object2ObjectArrayMap();
        RegistryOps<NbtElement> lv = this.registries.getOps(NbtOps.INSTANCE);
        this.loadedStates.forEach((type, optionalState) -> optionalState.filter(PersistentState::isDirty).ifPresent(state -> {
            map.put((PersistentStateType<?>)type, this.encode((PersistentStateType)type, (PersistentState)state, lv));
            state.setDirty(false);
        }));
        return map;
    }

    private <T extends PersistentState> NbtCompound encode(PersistentStateType<T> type, PersistentState state, RegistryOps<NbtElement> ops) {
        Codec<NbtElement> codec = type.codec();
        NbtCompound lv = new NbtCompound();
        lv.put("data", codec.encodeStart(ops, (NbtElement)((Object)state)).getOrThrow());
        NbtHelper.putDataVersion(lv);
        return lv;
    }

    private void save(PersistentStateType<?> type, NbtCompound nbt) {
        Path path = this.getFile(type.id());
        try {
            NbtIo.writeCompressed(nbt, path);
        } catch (IOException iOException) {
            LOGGER.error("Could not save data to {}", (Object)path.getFileName(), (Object)iOException);
        }
    }

    public void save() {
        this.startSaving().join();
    }

    @Override
    public void close() {
        this.save();
    }
}

