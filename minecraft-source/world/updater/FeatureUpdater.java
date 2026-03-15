/*
 * External method calls:
 *   Lnet/minecraft/world/ChunkUpdateState;markResolved(J)V
 *   Lnet/minecraft/datafixer/DataFixTypes;update(Lcom/mojang/datafixers/DataFixer;Lnet/minecraft/nbt/NbtCompound;II)Lnet/minecraft/nbt/NbtCompound;
 *   Lnet/minecraft/nbt/NbtCompound;putLongArray(Ljava/lang/String;[J)V
 *   Lnet/minecraft/world/PersistentStateManager;readNbt(Ljava/lang/String;Lnet/minecraft/datafixer/DataFixTypes;I)Lnet/minecraft/nbt/NbtCompound;
 *   Lnet/minecraft/nbt/NbtCompound;forEach(Ljava/util/function/BiConsumer;)V
 *   Lnet/minecraft/world/ChunkUpdateState;createStateType(Ljava/lang/String;)Lnet/minecraft/world/PersistentStateType;
 *   Lnet/minecraft/nbt/NbtCompound;putString(Ljava/lang/String;Ljava/lang/String;)V
 *   Lnet/minecraft/util/Util;make(Ljava/lang/Object;Ljava/util/function/Consumer;)Ljava/lang/Object;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/world/updater/FeatureUpdater;init(Lnet/minecraft/world/PersistentStateManager;)V
 */
package net.minecraft.world.updater;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import com.mojang.datafixers.DataFixer;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.longs.LongArrayList;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;
import net.minecraft.datafixer.DataFixTypes;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.nbt.NbtList;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Util;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.ChunkUpdateState;
import net.minecraft.world.PersistentStateManager;
import net.minecraft.world.World;
import net.minecraft.world.updater.ChunkUpdater;
import org.jspecify.annotations.Nullable;

public class FeatureUpdater
implements ChunkUpdater {
    public static final int TARGET_DATA_VERSION = 1493;
    private static final Map<String, String> OLD_TO_NEW = Util.make(Maps.newHashMap(), map -> {
        map.put("Village", "Village");
        map.put("Mineshaft", "Mineshaft");
        map.put("Mansion", "Mansion");
        map.put("Igloo", "Temple");
        map.put("Desert_Pyramid", "Temple");
        map.put("Jungle_Pyramid", "Temple");
        map.put("Swamp_Hut", "Temple");
        map.put("Stronghold", "Stronghold");
        map.put("Monument", "Monument");
        map.put("Fortress", "Fortress");
        map.put("EndCity", "EndCity");
    });
    private static final Map<String, String> ANCIENT_TO_OLD = Util.make(Maps.newHashMap(), map -> {
        map.put("Iglu", "Igloo");
        map.put("TeDP", "Desert_Pyramid");
        map.put("TeJP", "Jungle_Pyramid");
        map.put("TeSH", "Swamp_Hut");
    });
    private static final Set<String> NEW_STRUCTURE_NAMES = Set.of("pillager_outpost", "mineshaft", "mansion", "jungle_pyramid", "desert_pyramid", "igloo", "ruined_portal", "shipwreck", "swamp_hut", "stronghold", "monument", "ocean_ruin", "fortress", "endcity", "buried_treasure", "village", "nether_fossil", "bastion_remnant");
    private final boolean needsUpdate;
    private final Map<String, Long2ObjectMap<NbtCompound>> featureIdToChunkNbt = Maps.newHashMap();
    private final Map<String, ChunkUpdateState> updateStates = Maps.newHashMap();
    private final @Nullable PersistentStateManager persistentStateManager;
    private final List<String> oldNames;
    private final List<String> newNames;
    private final DataFixer dataFixer;
    private boolean initialized;

    public FeatureUpdater(@Nullable PersistentStateManager persistentStateManager, List<String> oldNames, List<String> newNames, DataFixer dataFixer) {
        this.persistentStateManager = persistentStateManager;
        this.oldNames = oldNames;
        this.newNames = newNames;
        this.dataFixer = dataFixer;
        boolean bl = false;
        for (String string : this.newNames) {
            bl |= this.featureIdToChunkNbt.get(string) != null;
        }
        this.needsUpdate = bl;
    }

    @Override
    public void markChunkDone(ChunkPos arg) {
        long l = arg.toLong();
        for (String string : this.oldNames) {
            ChunkUpdateState lv = this.updateStates.get(string);
            if (lv == null || !lv.isRemaining(l)) continue;
            lv.markResolved(l);
        }
    }

    @Override
    public int targetDataVersion() {
        return 1493;
    }

    @Override
    public NbtCompound applyFix(NbtCompound arg) {
        int i;
        if (!this.initialized && this.persistentStateManager != null) {
            this.init(this.persistentStateManager);
        }
        if ((i = NbtHelper.getDataVersion(arg)) < 1493 && (arg = DataFixTypes.CHUNK.update(this.dataFixer, arg, i, 1493)).getCompound("Level").flatMap(levelTag -> levelTag.getBoolean("hasLegacyStructureData")).orElse(false).booleanValue()) {
            arg = this.getUpdatedReferences(arg);
        }
        return arg;
    }

    private NbtCompound getUpdatedReferences(NbtCompound nbt) {
        NbtCompound lv = nbt.getCompoundOrEmpty("Level");
        ChunkPos lv2 = new ChunkPos(lv.getInt("xPos", 0), lv.getInt("zPos", 0));
        if (this.needsUpdate(lv2.x, lv2.z)) {
            nbt = this.getUpdatedStarts(nbt, lv2);
        }
        NbtCompound lv3 = lv.getCompoundOrEmpty("Structures");
        NbtCompound lv4 = lv3.getCompoundOrEmpty("References");
        for (String string : this.newNames) {
            boolean bl = NEW_STRUCTURE_NAMES.contains(string.toLowerCase(Locale.ROOT));
            if (lv4.getLongArray(string).isPresent() || !bl) continue;
            int i = 8;
            LongArrayList longList = new LongArrayList();
            for (int j = lv2.x - 8; j <= lv2.x + 8; ++j) {
                for (int k = lv2.z - 8; k <= lv2.z + 8; ++k) {
                    if (!this.needsUpdate(j, k, string)) continue;
                    longList.add(ChunkPos.toLong(j, k));
                }
            }
            lv4.putLongArray(string, longList.toLongArray());
        }
        lv3.put("References", lv4);
        lv.put("Structures", lv3);
        nbt.put("Level", lv);
        return nbt;
    }

    private boolean needsUpdate(int chunkX, int chunkZ, String id) {
        if (!this.needsUpdate) {
            return false;
        }
        return this.featureIdToChunkNbt.get(id) != null && this.updateStates.get(OLD_TO_NEW.get(id)).contains(ChunkPos.toLong(chunkX, chunkZ));
    }

    private boolean needsUpdate(int chunkX, int chunkZ) {
        if (!this.needsUpdate) {
            return false;
        }
        for (String string : this.newNames) {
            if (this.featureIdToChunkNbt.get(string) == null || !this.updateStates.get(OLD_TO_NEW.get(string)).isRemaining(ChunkPos.toLong(chunkX, chunkZ))) continue;
            return true;
        }
        return false;
    }

    private NbtCompound getUpdatedStarts(NbtCompound nbt, ChunkPos pos) {
        NbtCompound lv = nbt.getCompoundOrEmpty("Level");
        NbtCompound lv2 = lv.getCompoundOrEmpty("Structures");
        NbtCompound lv3 = lv2.getCompoundOrEmpty("Starts");
        for (String string : this.newNames) {
            NbtCompound lv4;
            Long2ObjectMap<NbtCompound> long2ObjectMap = this.featureIdToChunkNbt.get(string);
            if (long2ObjectMap == null) continue;
            long l = pos.toLong();
            if (!this.updateStates.get(OLD_TO_NEW.get(string)).isRemaining(l) || (lv4 = (NbtCompound)long2ObjectMap.get(l)) == null) continue;
            lv3.put(string, lv4);
        }
        lv2.put("Starts", lv3);
        lv.put("Structures", lv2);
        nbt.put("Level", lv);
        return nbt;
    }

    private synchronized void init(PersistentStateManager persistentStateManager) {
        if (this.initialized) {
            return;
        }
        for (String string : this.oldNames) {
            NbtCompound lv = new NbtCompound();
            try {
                lv = persistentStateManager.readNbt(string, DataFixTypes.SAVED_DATA_STRUCTURE_FEATURE_INDICES, 1493).getCompoundOrEmpty("data").getCompoundOrEmpty("Features");
                if (lv.isEmpty()) {
                    continue;
                }
            } catch (IOException iOException) {
                // empty catch block
            }
            lv.forEach((key, nbt) -> {
                if (!(nbt instanceof NbtCompound)) {
                    return;
                }
                NbtCompound lv = (NbtCompound)nbt;
                long l = ChunkPos.toLong(lv.getInt("ChunkX", 0), lv.getInt("ChunkZ", 0));
                NbtList lv2 = lv.getListOrEmpty("Children");
                if (!lv2.isEmpty()) {
                    Optional<String> optional = lv2.getCompound(0).flatMap(child -> child.getString("id"));
                    optional.map(ANCIENT_TO_OLD::get).ifPresent(id -> lv.putString("id", (String)id));
                }
                lv.getString("id").ifPresent(id -> this.featureIdToChunkNbt.computeIfAbsent((String)id, featureId -> new Long2ObjectOpenHashMap()).put(l, lv));
            });
            String string2 = string + "_index";
            ChunkUpdateState lv2 = persistentStateManager.getOrCreate(ChunkUpdateState.createStateType(string2));
            if (lv2.getAll().isEmpty()) {
                ChunkUpdateState lv3 = new ChunkUpdateState();
                this.updateStates.put(string, lv3);
                lv.forEach((key, nbt) -> {
                    if (nbt instanceof NbtCompound) {
                        NbtCompound lv = (NbtCompound)nbt;
                        lv3.add(ChunkPos.toLong(lv.getInt("ChunkX", 0), lv.getInt("ChunkZ", 0)));
                    }
                });
                continue;
            }
            this.updateStates.put(string, lv2);
        }
        this.initialized = true;
    }

    public static Supplier<ChunkUpdater> create(RegistryKey<World> world, Supplier<@Nullable PersistentStateManager> persistentStateManagerSupplier, DataFixer dataFixer) {
        if (world == World.OVERWORLD) {
            return () -> new FeatureUpdater((PersistentStateManager)persistentStateManagerSupplier.get(), ImmutableList.of("Monument", "Stronghold", "Village", "Mineshaft", "Temple", "Mansion"), ImmutableList.of("Village", "Mineshaft", "Mansion", "Igloo", "Desert_Pyramid", "Jungle_Pyramid", "Swamp_Hut", "Stronghold", "Monument"), dataFixer);
        }
        if (world == World.NETHER) {
            ImmutableList<String> list = ImmutableList.of("Fortress");
            return () -> new FeatureUpdater((PersistentStateManager)persistentStateManagerSupplier.get(), list, list, dataFixer);
        }
        if (world == World.END) {
            ImmutableList<String> list = ImmutableList.of("EndCity");
            return () -> new FeatureUpdater((PersistentStateManager)persistentStateManagerSupplier.get(), list, list, dataFixer);
        }
        return ChunkUpdater.PASSTHROUGH_FACTORY;
    }
}

