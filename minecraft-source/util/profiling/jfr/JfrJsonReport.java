/*
 * External method calls:
 *   Lnet/minecraft/util/profiling/jfr/sample/PacketSample;protocolId()Ljava/lang/String;
 *   Lnet/minecraft/util/profiling/jfr/sample/PacketSample;packetId()Ljava/lang/String;
 *   Lnet/minecraft/util/profiling/jfr/sample/ChunkRegionSample;level()Ljava/lang/String;
 *   Lnet/minecraft/util/profiling/jfr/sample/ChunkRegionSample;dimension()Ljava/lang/String;
 *   Lnet/minecraft/util/profiling/jfr/JfrProfile;startTime()Ljava/time/Instant;
 *   Lnet/minecraft/util/profiling/jfr/JfrProfile;endTime()Ljava/time/Instant;
 *   Lnet/minecraft/util/profiling/jfr/JfrProfile;duration()Ljava/time/Duration;
 *   Lnet/minecraft/util/profiling/jfr/JfrProfile;worldGenDuration()Ljava/time/Duration;
 *   Lnet/minecraft/util/profiling/jfr/JfrProfile;gcHeapSummaryStatistics()Lnet/minecraft/util/profiling/jfr/sample/GcHeapSummarySample$Statistics;
 *   Lnet/minecraft/util/profiling/jfr/JfrProfile;cpuLoadSamples()Ljava/util/List;
 *   Lnet/minecraft/util/profiling/jfr/JfrProfile;fps()Ljava/util/List;
 *   Lnet/minecraft/util/profiling/jfr/JfrProfile;serverTickTimeSamples()Ljava/util/List;
 *   Lnet/minecraft/util/profiling/jfr/JfrProfile;threadAllocationMap()Lnet/minecraft/util/profiling/jfr/sample/ThreadAllocationStatisticsSample$AllocationMap;
 *   Lnet/minecraft/util/profiling/jfr/JfrProfile;structureGenerationSamples()Ljava/util/List;
 *   Lnet/minecraft/util/profiling/jfr/sample/GcHeapSummarySample$Statistics;gcDuration()Ljava/time/Duration;
 *   Lnet/minecraft/util/profiling/jfr/sample/LongRunningSampleStatistics;fromSamples(Ljava/util/List;)Ljava/util/Optional;
 *   Lnet/minecraft/util/Util;make(Ljava/lang/Object;Ljava/util/function/Consumer;)Ljava/lang/Object;
 *   Lnet/minecraft/util/profiling/jfr/sample/LongRunningSampleStatistics;totalDuration()Ljava/time/Duration;
 *   Lnet/minecraft/util/profiling/jfr/sample/LongRunningSampleStatistics;quantiles()Ljava/util/Map;
 *   Lnet/minecraft/util/profiling/jfr/sample/LongRunningSampleStatistics;fastestSample()Lnet/minecraft/util/profiling/jfr/sample/LongRunningSample;
 *   Lnet/minecraft/util/profiling/jfr/sample/LongRunningSampleStatistics;slowestSample()Lnet/minecraft/util/profiling/jfr/sample/LongRunningSample;
 *   Lnet/minecraft/util/profiling/jfr/sample/LongRunningSampleStatistics;secondSlowestSample()Lnet/minecraft/util/profiling/jfr/sample/LongRunningSample;
 *   Lnet/minecraft/util/profiling/jfr/sample/ThreadAllocationStatisticsSample$AllocationMap;allocations()Ljava/util/Map;
 *   Lnet/minecraft/util/profiling/jfr/JfrProfile;fileWriteStatistics()Lnet/minecraft/util/profiling/jfr/sample/FileIoSample$Statistics;
 *   Lnet/minecraft/util/profiling/jfr/JfrProfile;fileReadStatistics()Lnet/minecraft/util/profiling/jfr/sample/FileIoSample$Statistics;
 *   Lnet/minecraft/util/profiling/jfr/JfrProfile;readChunks()Lnet/minecraft/util/profiling/jfr/sample/NetworkIoStatistics;
 *   Lnet/minecraft/util/profiling/jfr/JfrProfile;writtenChunks()Lnet/minecraft/util/profiling/jfr/sample/NetworkIoStatistics;
 *   Lnet/minecraft/util/profiling/jfr/sample/FileIoSample$Statistics;topContributors()Ljava/util/List;
 *   Lnet/minecraft/util/profiling/jfr/JfrProfile;packetSentStatistics()Lnet/minecraft/util/profiling/jfr/sample/NetworkIoStatistics;
 *   Lnet/minecraft/util/profiling/jfr/JfrProfile;packetReadStatistics()Lnet/minecraft/util/profiling/jfr/sample/NetworkIoStatistics;
 *   Lnet/minecraft/util/profiling/jfr/sample/ServerTickTimeSample;averageTickMs()Ljava/time/Duration;
 *   Lnet/minecraft/util/profiling/jfr/sample/ChunkGenerationSample;duration()Ljava/time/Duration;
 *   Lnet/minecraft/util/profiling/jfr/sample/ChunkGenerationSample;worldKey()Ljava/lang/String;
 *   Lnet/minecraft/util/profiling/jfr/sample/ChunkGenerationSample;chunkPos()Lnet/minecraft/util/math/ChunkPos;
 *   Lnet/minecraft/util/profiling/jfr/sample/ChunkGenerationSample;centerPos()Lnet/minecraft/util/math/ColumnPos;
 *   Lnet/minecraft/util/profiling/jfr/sample/StructureGenerationSample;duration()Ljava/time/Duration;
 *   Lnet/minecraft/util/profiling/jfr/sample/StructureGenerationSample;chunkPos()Lnet/minecraft/util/math/ChunkPos;
 *   Lnet/minecraft/util/profiling/jfr/sample/StructureGenerationSample;structureName()Ljava/lang/String;
 *   Lnet/minecraft/util/profiling/jfr/sample/StructureGenerationSample;level()Ljava/lang/String;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/util/profiling/jfr/JfrJsonReport;collectHeapSection(Lnet/minecraft/util/profiling/jfr/sample/GcHeapSummarySample$Statistics;)Lcom/google/gson/JsonElement;
 *   Lnet/minecraft/util/profiling/jfr/JfrJsonReport;collectCpuPercentSection(Ljava/util/List;)Lcom/google/gson/JsonElement;
 *   Lnet/minecraft/util/profiling/jfr/JfrJsonReport;collectNetworkSection(Lnet/minecraft/util/profiling/jfr/JfrProfile;)Lcom/google/gson/JsonElement;
 *   Lnet/minecraft/util/profiling/jfr/JfrJsonReport;collectFileIoSection(Lnet/minecraft/util/profiling/jfr/JfrProfile;)Lcom/google/gson/JsonElement;
 *   Lnet/minecraft/util/profiling/jfr/JfrJsonReport;collectClientFpsSection(Ljava/util/List;)Lcom/google/gson/JsonElement;
 *   Lnet/minecraft/util/profiling/jfr/JfrJsonReport;collectServerTickSection(Ljava/util/List;)Lcom/google/gson/JsonElement;
 *   Lnet/minecraft/util/profiling/jfr/JfrJsonReport;collectThreadAllocationSection(Lnet/minecraft/util/profiling/jfr/sample/ThreadAllocationStatisticsSample$AllocationMap;)Lcom/google/gson/JsonElement;
 *   Lnet/minecraft/util/profiling/jfr/JfrJsonReport;collectChunkGenSection(Ljava/util/List;)Lcom/google/gson/JsonElement;
 *   Lnet/minecraft/util/profiling/jfr/JfrJsonReport;collectStructureGenSection(Ljava/util/List;)Lcom/google/gson/JsonElement;
 *   Lnet/minecraft/util/profiling/jfr/JfrJsonReport;collectFileIoSection(Lnet/minecraft/util/profiling/jfr/sample/FileIoSample$Statistics;)Lcom/google/gson/JsonElement;
 *   Lnet/minecraft/util/profiling/jfr/JfrJsonReport;collectPacketSection(Lnet/minecraft/util/profiling/jfr/sample/NetworkIoStatistics;Ljava/util/function/BiConsumer;)Lcom/google/gson/JsonElement;
 */
package net.minecraft.util.profiling.jfr;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.LongSerializationPolicy;
import com.mojang.datafixers.util.Pair;
import java.time.Duration;
import java.util.DoubleSummaryStatistics;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.ToDoubleFunction;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import net.minecraft.util.Util;
import net.minecraft.util.math.Quantiles;
import net.minecraft.util.profiling.jfr.JfrProfile;
import net.minecraft.util.profiling.jfr.sample.ChunkGenerationSample;
import net.minecraft.util.profiling.jfr.sample.ChunkRegionSample;
import net.minecraft.util.profiling.jfr.sample.ClientFpsSample;
import net.minecraft.util.profiling.jfr.sample.CpuLoadSample;
import net.minecraft.util.profiling.jfr.sample.FileIoSample;
import net.minecraft.util.profiling.jfr.sample.GcHeapSummarySample;
import net.minecraft.util.profiling.jfr.sample.LongRunningSampleStatistics;
import net.minecraft.util.profiling.jfr.sample.NetworkIoStatistics;
import net.minecraft.util.profiling.jfr.sample.PacketSample;
import net.minecraft.util.profiling.jfr.sample.ServerTickTimeSample;
import net.minecraft.util.profiling.jfr.sample.StructureGenerationSample;
import net.minecraft.util.profiling.jfr.sample.ThreadAllocationStatisticsSample;
import net.minecraft.world.chunk.ChunkStatus;

public class JfrJsonReport {
    private static final String BYTES_PER_SECOND = "bytesPerSecond";
    private static final String COUNT = "count";
    private static final String DURATION_NANOS_TOTAL = "durationNanosTotal";
    private static final String TOTAL_BYTES = "totalBytes";
    private static final String COUNT_PER_SECOND = "countPerSecond";
    final Gson gson = new GsonBuilder().setPrettyPrinting().setLongSerializationPolicy(LongSerializationPolicy.DEFAULT).create();

    private static void addPacketData(PacketSample packet, JsonObject json) {
        json.addProperty("protocolId", packet.protocolId());
        json.addProperty("packetId", packet.packetId());
    }

    private static void addChunkData(ChunkRegionSample chunk, JsonObject json) {
        json.addProperty("level", chunk.level());
        json.addProperty("dimension", chunk.dimension());
        json.addProperty("x", chunk.x());
        json.addProperty("z", chunk.z());
    }

    public String toString(JfrProfile profile) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("startedEpoch", profile.startTime().toEpochMilli());
        jsonObject.addProperty("endedEpoch", profile.endTime().toEpochMilli());
        jsonObject.addProperty("durationMs", profile.duration().toMillis());
        Duration duration = profile.worldGenDuration();
        if (duration != null) {
            jsonObject.addProperty("worldGenDurationMs", duration.toMillis());
        }
        jsonObject.add("heap", this.collectHeapSection(profile.gcHeapSummaryStatistics()));
        jsonObject.add("cpuPercent", this.collectCpuPercentSection(profile.cpuLoadSamples()));
        jsonObject.add("network", this.collectNetworkSection(profile));
        jsonObject.add("fileIO", this.collectFileIoSection(profile));
        jsonObject.add("fps", this.collectClientFpsSection(profile.fps()));
        jsonObject.add("serverTick", this.collectServerTickSection(profile.serverTickTimeSamples()));
        jsonObject.add("threadAllocation", this.collectThreadAllocationSection(profile.threadAllocationMap()));
        jsonObject.add("chunkGen", this.collectChunkGenSection(profile.getChunkGenerationSampleStatistics()));
        jsonObject.add("structureGen", this.collectStructureGenSection(profile.structureGenerationSamples()));
        return this.gson.toJson(jsonObject);
    }

    private JsonElement collectHeapSection(GcHeapSummarySample.Statistics statistics) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("allocationRateBytesPerSecond", statistics.allocatedBytesPerSecond());
        jsonObject.addProperty("gcCount", statistics.count());
        jsonObject.addProperty("gcOverHeadPercent", Float.valueOf(statistics.getGcDurationRatio()));
        jsonObject.addProperty("gcTotalDurationMs", statistics.gcDuration().toMillis());
        return jsonObject;
    }

    private JsonElement collectStructureGenSection(List<StructureGenerationSample> structureGenerationSamples) {
        JsonObject jsonObject = new JsonObject();
        Optional<LongRunningSampleStatistics<StructureGenerationSample>> optional = LongRunningSampleStatistics.fromSamples(structureGenerationSamples);
        if (optional.isEmpty()) {
            return jsonObject;
        }
        LongRunningSampleStatistics<StructureGenerationSample> lv = optional.get();
        JsonArray jsonArray = new JsonArray();
        jsonObject.add("structure", jsonArray);
        structureGenerationSamples.stream().collect(Collectors.groupingBy(StructureGenerationSample::structureName)).forEach((name, samples) -> {
            Optional optional = LongRunningSampleStatistics.fromSamples(samples);
            if (optional.isEmpty()) {
                return;
            }
            LongRunningSampleStatistics lv = optional.get();
            JsonObject jsonObject2 = new JsonObject();
            jsonArray.add(jsonObject2);
            jsonObject2.addProperty("name", (String)name);
            jsonObject2.addProperty(COUNT, lv.count());
            jsonObject2.addProperty(DURATION_NANOS_TOTAL, lv.totalDuration().toNanos());
            jsonObject2.addProperty("durationNanosAvg", lv.totalDuration().toNanos() / (long)lv.count());
            JsonObject jsonObject3 = Util.make(new JsonObject(), json -> jsonObject2.add("durationNanosPercentiles", (JsonElement)json));
            lv.quantiles().forEach((quantile, value) -> jsonObject3.addProperty("p" + quantile, (Number)value));
            Function<StructureGenerationSample, JsonElement> function = sample -> {
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("durationNanos", sample.duration().toNanos());
                jsonObject.addProperty("chunkPosX", sample.chunkPos().x);
                jsonObject.addProperty("chunkPosZ", sample.chunkPos().z);
                jsonObject.addProperty("structureName", sample.structureName());
                jsonObject.addProperty("level", sample.level());
                jsonObject.addProperty("success", sample.success());
                return jsonObject;
            };
            jsonObject.add("fastest", function.apply((StructureGenerationSample)lv.fastestSample()));
            jsonObject.add("slowest", function.apply((StructureGenerationSample)lv.slowestSample()));
            jsonObject.add("secondSlowest", lv.secondSlowestSample() != null ? function.apply((StructureGenerationSample)lv.secondSlowestSample()) : JsonNull.INSTANCE);
        });
        return jsonObject;
    }

    private JsonElement collectChunkGenSection(List<Pair<ChunkStatus, LongRunningSampleStatistics<ChunkGenerationSample>>> statistics) {
        JsonObject jsonObject = new JsonObject();
        if (statistics.isEmpty()) {
            return jsonObject;
        }
        jsonObject.addProperty(DURATION_NANOS_TOTAL, statistics.stream().mapToDouble(pair -> ((LongRunningSampleStatistics)pair.getSecond()).totalDuration().toNanos()).sum());
        JsonArray jsonArray = Util.make(new JsonArray(), json -> jsonObject.add("status", (JsonElement)json));
        for (Pair<ChunkStatus, LongRunningSampleStatistics<ChunkGenerationSample>> pair2 : statistics) {
            LongRunningSampleStatistics<ChunkGenerationSample> lv = pair2.getSecond();
            JsonObject jsonObject2 = Util.make(new JsonObject(), jsonArray::add);
            jsonObject2.addProperty("state", pair2.getFirst().toString());
            jsonObject2.addProperty(COUNT, lv.count());
            jsonObject2.addProperty(DURATION_NANOS_TOTAL, lv.totalDuration().toNanos());
            jsonObject2.addProperty("durationNanosAvg", lv.totalDuration().toNanos() / (long)lv.count());
            JsonObject jsonObject3 = Util.make(new JsonObject(), json -> jsonObject2.add("durationNanosPercentiles", (JsonElement)json));
            lv.quantiles().forEach((quantile, value) -> jsonObject3.addProperty("p" + quantile, (Number)value));
            Function<ChunkGenerationSample, JsonElement> function = sample -> {
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("durationNanos", sample.duration().toNanos());
                jsonObject.addProperty("level", sample.worldKey());
                jsonObject.addProperty("chunkPosX", sample.chunkPos().x);
                jsonObject.addProperty("chunkPosZ", sample.chunkPos().z);
                jsonObject.addProperty("worldPosX", sample.centerPos().x());
                jsonObject.addProperty("worldPosZ", sample.centerPos().z());
                return jsonObject;
            };
            jsonObject2.add("fastest", function.apply(lv.fastestSample()));
            jsonObject2.add("slowest", function.apply(lv.slowestSample()));
            jsonObject2.add("secondSlowest", lv.secondSlowestSample() != null ? function.apply(lv.secondSlowestSample()) : JsonNull.INSTANCE);
        }
        return jsonObject;
    }

    private JsonElement collectThreadAllocationSection(ThreadAllocationStatisticsSample.AllocationMap statistics) {
        JsonArray jsonArray = new JsonArray();
        statistics.allocations().forEach((threadName, allocation) -> jsonArray.add(Util.make(new JsonObject(), json -> {
            json.addProperty("thread", (String)threadName);
            json.addProperty(BYTES_PER_SECOND, (Number)allocation);
        })));
        return jsonArray;
    }

    private JsonElement collectServerTickSection(List<ServerTickTimeSample> samples) {
        if (samples.isEmpty()) {
            return JsonNull.INSTANCE;
        }
        JsonObject jsonObject = new JsonObject();
        double[] ds = samples.stream().mapToDouble(sample -> (double)sample.averageTickMs().toNanos() / 1000000.0).toArray();
        DoubleSummaryStatistics doubleSummaryStatistics = DoubleStream.of(ds).summaryStatistics();
        jsonObject.addProperty("minMs", doubleSummaryStatistics.getMin());
        jsonObject.addProperty("averageMs", doubleSummaryStatistics.getAverage());
        jsonObject.addProperty("maxMs", doubleSummaryStatistics.getMax());
        Map<Integer, Double> map = Quantiles.create(ds);
        map.forEach((quantile, value) -> jsonObject.addProperty("p" + quantile, (Number)value));
        return jsonObject;
    }

    private JsonElement collectClientFpsSection(List<ClientFpsSample> samples) {
        if (samples.isEmpty()) {
            return JsonNull.INSTANCE;
        }
        JsonObject jsonObject = new JsonObject();
        int[] is = samples.stream().mapToInt(ClientFpsSample::fps).toArray();
        IntSummaryStatistics intSummaryStatistics = IntStream.of(is).summaryStatistics();
        jsonObject.addProperty("minFPS", intSummaryStatistics.getMin());
        jsonObject.addProperty("averageFPS", intSummaryStatistics.getAverage());
        jsonObject.addProperty("maxFPS", intSummaryStatistics.getMax());
        Map<Integer, Double> map = Quantiles.create(is);
        map.forEach((quantile, value) -> jsonObject.addProperty("p" + quantile, (Number)value));
        return jsonObject;
    }

    private JsonElement collectFileIoSection(JfrProfile profile) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.add("write", this.collectFileIoSection(profile.fileWriteStatistics()));
        jsonObject.add("read", this.collectFileIoSection(profile.fileReadStatistics()));
        jsonObject.add("chunksRead", this.collectPacketSection(profile.readChunks(), JfrJsonReport::addChunkData));
        jsonObject.add("chunksWritten", this.collectPacketSection(profile.writtenChunks(), JfrJsonReport::addChunkData));
        return jsonObject;
    }

    private JsonElement collectFileIoSection(FileIoSample.Statistics statistics) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(TOTAL_BYTES, statistics.totalBytes());
        jsonObject.addProperty(COUNT, statistics.count());
        jsonObject.addProperty(BYTES_PER_SECOND, statistics.bytesPerSecond());
        jsonObject.addProperty(COUNT_PER_SECOND, statistics.countPerSecond());
        JsonArray jsonArray = new JsonArray();
        jsonObject.add("topContributors", jsonArray);
        statistics.topContributors().forEach(pair -> {
            JsonObject jsonObject = new JsonObject();
            jsonArray.add(jsonObject);
            jsonObject.addProperty("path", (String)pair.getFirst());
            jsonObject.addProperty(TOTAL_BYTES, (Number)pair.getSecond());
        });
        return jsonObject;
    }

    private JsonElement collectNetworkSection(JfrProfile profile) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.add("sent", this.collectPacketSection(profile.packetSentStatistics(), JfrJsonReport::addPacketData));
        jsonObject.add("received", this.collectPacketSection(profile.packetReadStatistics(), JfrJsonReport::addPacketData));
        return jsonObject;
    }

    private <T> JsonElement collectPacketSection(NetworkIoStatistics<T> statistics, BiConsumer<T, JsonObject> callback) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(TOTAL_BYTES, statistics.getTotalSize());
        jsonObject.addProperty(COUNT, statistics.getTotalCount());
        jsonObject.addProperty(BYTES_PER_SECOND, statistics.getBytesPerSecond());
        jsonObject.addProperty(COUNT_PER_SECOND, statistics.getCountPerSecond());
        JsonArray jsonArray = new JsonArray();
        jsonObject.add("topContributors", jsonArray);
        statistics.getTopContributors().forEach(topContributor -> {
            JsonObject jsonObject = new JsonObject();
            jsonArray.add(jsonObject);
            Object object = topContributor.getFirst();
            NetworkIoStatistics.PacketStatistics lv = (NetworkIoStatistics.PacketStatistics)topContributor.getSecond();
            callback.accept(object, jsonObject);
            jsonObject.addProperty(TOTAL_BYTES, lv.totalSize());
            jsonObject.addProperty(COUNT, lv.totalCount());
            jsonObject.addProperty("averageSize", Float.valueOf(lv.getAverageSize()));
        });
        return jsonObject;
    }

    private JsonElement collectCpuPercentSection(List<CpuLoadSample> samples) {
        JsonObject jsonObject = new JsonObject();
        BiFunction<List, ToDoubleFunction, JsonObject> biFunction = (samplesx, valueGetter) -> {
            JsonObject jsonObject = new JsonObject();
            DoubleSummaryStatistics doubleSummaryStatistics = samplesx.stream().mapToDouble(valueGetter).summaryStatistics();
            jsonObject.addProperty("min", doubleSummaryStatistics.getMin());
            jsonObject.addProperty("average", doubleSummaryStatistics.getAverage());
            jsonObject.addProperty("max", doubleSummaryStatistics.getMax());
            return jsonObject;
        };
        jsonObject.add("jvm", biFunction.apply(samples, CpuLoadSample::jvm));
        jsonObject.add("userJvm", biFunction.apply(samples, CpuLoadSample::userJvm));
        jsonObject.add("system", biFunction.apply(samples, CpuLoadSample::system));
        return jsonObject;
    }
}

