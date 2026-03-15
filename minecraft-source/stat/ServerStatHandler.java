/*
 * External method calls:
 *   Lnet/minecraft/util/StrictJsonParser;parse(Ljava/io/Reader;)Lcom/google/gson/JsonElement;
 *   Lnet/minecraft/util/path/PathUtil;createDirectories(Ljava/nio/file/Path;)V
 *   Lnet/minecraft/datafixer/DataFixTypes;update(Lcom/mojang/datafixers/DataFixer;Lcom/mojang/serialization/Dynamic;I)Lcom/mojang/serialization/Dynamic;
 *   Lnet/minecraft/GameVersion;dataVersion()Lnet/minecraft/SaveVersion;
 *   Lnet/minecraft/server/network/ServerPlayNetworkHandler;sendPacket(Lnet/minecraft/network/packet/Packet;)V
 *   Lnet/minecraft/util/Util;toMap()Ljava/util/stream/Collector;
 *   Lnet/minecraft/util/Util;memoize(Ljava/util/function/Function;)Ljava/util/function/Function;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/stat/ServerStatHandler;parse(Lcom/mojang/datafixers/DataFixer;Lcom/google/gson/JsonElement;)V
 *   Lnet/minecraft/stat/ServerStatHandler;asString()Lcom/google/gson/JsonElement;
 *   Lnet/minecraft/stat/ServerStatHandler;takePendingStats()Ljava/util/Set;
 */
package net.minecraft.stat;

import com.google.common.collect.Sets;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.mojang.datafixers.DataFixer;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.JsonOps;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import net.minecraft.SharedConstants;
import net.minecraft.datafixer.DataFixTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.network.packet.s2c.play.StatisticsS2CPacket;
import net.minecraft.registry.Registries;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.stat.Stat;
import net.minecraft.stat.StatHandler;
import net.minecraft.stat.StatType;
import net.minecraft.util.StrictJsonParser;
import net.minecraft.util.Util;
import net.minecraft.util.path.PathUtil;
import org.slf4j.Logger;

public class ServerStatHandler
extends StatHandler {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final Codec<Map<Stat<?>, Integer>> CODEC = Codec.dispatchedMap(Registries.STAT_TYPE.getCodec(), Util.memoize(ServerStatHandler::createCodec)).xmap(statsByTypes -> {
        HashMap map2 = new HashMap();
        statsByTypes.forEach((type, stats) -> map2.putAll(stats));
        return map2;
    }, stats -> stats.entrySet().stream().collect(Collectors.groupingBy(entry -> ((Stat)entry.getKey()).getType(), Util.toMap())));
    private final Path path;
    private final Set<Stat<?>> pendingStats = Sets.newHashSet();

    private static <T> Codec<Map<Stat<?>, Integer>> createCodec(StatType<T> statType) {
        Codec<T> codec = statType.getRegistry().getCodec();
        Codec<Stat> codec2 = codec.flatComapMap(statType::getOrCreateStat, stat -> {
            if (stat.getType() == statType) {
                return DataResult.success(stat.getValue());
            }
            return DataResult.error(() -> "Expected type " + String.valueOf(statType) + ", but got " + String.valueOf(stat.getType()));
        });
        return Codec.unboundedMap(codec2, Codec.INT);
    }

    public ServerStatHandler(MinecraftServer server, Path path) {
        this.path = path;
        if (Files.isRegularFile(path, new LinkOption[0])) {
            try (BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8);){
                JsonElement jsonElement = StrictJsonParser.parse(reader);
                this.parse(server.getDataFixer(), jsonElement);
            } catch (IOException iOException) {
                LOGGER.error("Couldn't read statistics file {}", (Object)path, (Object)iOException);
            } catch (JsonParseException jsonParseException) {
                LOGGER.error("Couldn't parse statistics file {}", (Object)path, (Object)jsonParseException);
            }
        }
    }

    public void save() {
        try {
            PathUtil.createDirectories(this.path.getParent());
            try (BufferedWriter writer = Files.newBufferedWriter(this.path, StandardCharsets.UTF_8, new OpenOption[0]);){
                GSON.toJson(this.asString(), GSON.newJsonWriter(writer));
            }
        } catch (JsonIOException | IOException exception) {
            LOGGER.error("Couldn't save stats to {}", (Object)this.path, (Object)exception);
        }
    }

    @Override
    public void setStat(PlayerEntity player, Stat<?> stat, int value) {
        super.setStat(player, stat, value);
        this.pendingStats.add(stat);
    }

    private Set<Stat<?>> takePendingStats() {
        HashSet<Stat<?>> set = Sets.newHashSet(this.pendingStats);
        this.pendingStats.clear();
        return set;
    }

    public void parse(DataFixer dataFixer, JsonElement json) {
        Dynamic<JsonElement> dynamic = new Dynamic<JsonElement>(JsonOps.INSTANCE, json);
        dynamic = DataFixTypes.STATS.update(dataFixer, dynamic, NbtHelper.getDataVersion(dynamic, 1343));
        this.statMap.putAll(CODEC.parse(dynamic.get("stats").orElseEmptyMap()).resultOrPartial(error -> LOGGER.error("Failed to parse statistics for {}: {}", (Object)this.path, error)).orElse(Map.of()));
    }

    protected JsonElement asString() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.add("stats", CODEC.encodeStart(JsonOps.INSTANCE, this.statMap).getOrThrow());
        jsonObject.addProperty("DataVersion", SharedConstants.getGameVersion().dataVersion().id());
        return jsonObject;
    }

    public void updateStatSet() {
        this.pendingStats.addAll(this.statMap.keySet());
    }

    public void sendStats(ServerPlayerEntity player) {
        Object2IntOpenHashMap object2IntMap = new Object2IntOpenHashMap();
        for (Stat<?> lv : this.takePendingStats()) {
            object2IntMap.put(lv, this.getStat(lv));
        }
        player.networkHandler.sendPacket(new StatisticsS2CPacket(object2IntMap));
    }
}

