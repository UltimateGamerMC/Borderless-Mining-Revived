/*
 * External method calls:
 *   Lnet/minecraft/world/attribute/timeline/TimelineEntry;validateKeyframesInPeriod(Lnet/minecraft/world/attribute/timeline/TimelineEntry;I)Lcom/mojang/serialization/DataResult;
 *   Lnet/minecraft/world/attribute/timeline/TimelineEntry;toModification(Lnet/minecraft/world/attribute/EnvironmentAttribute;Ljava/util/Optional;Ljava/util/function/LongSupplier;)Lnet/minecraft/world/attribute/timeline/TrackAttributeModification;
 *   Lnet/minecraft/registry/entry/RegistryFixedCodec;of(Lnet/minecraft/registry/RegistryKey;)Lnet/minecraft/registry/entry/RegistryFixedCodec;
 *   Lnet/minecraft/util/Util;memoize(Ljava/util/function/Function;)Ljava/util/function/Function;
 */
package net.minecraft.world.attribute.timeline;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.LongSupplier;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryFixedCodec;
import net.minecraft.util.Util;
import net.minecraft.util.dynamic.Codecs;
import net.minecraft.world.World;
import net.minecraft.world.attribute.EnvironmentAttribute;
import net.minecraft.world.attribute.EnvironmentAttributeModifier;
import net.minecraft.world.attribute.EnvironmentAttributes;
import net.minecraft.world.attribute.timeline.TimelineEntry;
import net.minecraft.world.attribute.timeline.Track;
import net.minecraft.world.attribute.timeline.TrackAttributeModification;

public class Timeline {
    public static final Codec<RegistryEntry<Timeline>> REGISTRY_CODEC = RegistryFixedCodec.of(RegistryKeys.TIMELINE);
    private static final Codec<Map<EnvironmentAttribute<?>, TimelineEntry<?, ?>>> TRACKS_BY_ATTRIBUTE_CODEC = Codec.dispatchedMap(EnvironmentAttributes.CODEC, Util.memoize(TimelineEntry::createCodec));
    public static final Codec<Timeline> CODEC = RecordCodecBuilder.create(instance -> instance.group(Codecs.POSITIVE_INT.optionalFieldOf("period_ticks").forGetter(timeline -> timeline.periodTicks), TRACKS_BY_ATTRIBUTE_CODEC.optionalFieldOf("tracks", Map.of()).forGetter(timeline -> timeline.tracks)).apply((Applicative<Timeline, ?>)instance, Timeline::new)).validate(Timeline::validate);
    public static final Codec<Timeline> NETWORK_CODEC = CODEC.xmap(Timeline::retainSyncedAttributes, Timeline::retainSyncedAttributes);
    private final Optional<Integer> periodTicks;
    private final Map<EnvironmentAttribute<?>, TimelineEntry<?, ?>> tracks;

    private static Timeline retainSyncedAttributes(Timeline timeline) {
        Map<EnvironmentAttribute<?>, TimelineEntry<?, ?>> map = Map.copyOf(Maps.filterKeys(timeline.tracks, EnvironmentAttribute::isSynced));
        return new Timeline(timeline.periodTicks, map);
    }

    Timeline(Optional<Integer> periodTicks, Map<EnvironmentAttribute<?>, TimelineEntry<?, ?>> entries) {
        this.periodTicks = periodTicks;
        this.tracks = entries;
    }

    private static DataResult<Timeline> validate(Timeline timeline) {
        if (timeline.periodTicks.isEmpty()) {
            return DataResult.success(timeline);
        }
        int i = timeline.periodTicks.get();
        DataResult<Timeline> dataResult = DataResult.success(timeline);
        for (TimelineEntry<?, ?> lv : timeline.tracks.values()) {
            dataResult = dataResult.apply2stable((arg, arg2) -> arg, TimelineEntry.validateKeyframesInPeriod(lv, i));
        }
        return dataResult;
    }

    public static Builder builder() {
        return new Builder();
    }

    public long getEffectiveTimeOfDay(World world) {
        long l = this.getRawTimeOfDay(world);
        if (this.periodTicks.isEmpty()) {
            return l;
        }
        return l % (long)this.periodTicks.get().intValue();
    }

    public long getRawTimeOfDay(World world) {
        return world.getTimeOfDay();
    }

    public Optional<Integer> getPeriod() {
        return this.periodTicks;
    }

    public Set<EnvironmentAttribute<?>> getAttributes() {
        return this.tracks.keySet();
    }

    public <Value> TrackAttributeModification<Value, ?> getModification(EnvironmentAttribute<Value> attribute, LongSupplier timeSupplier) {
        TimelineEntry<?, ?> lv = this.tracks.get(attribute);
        if (lv == null) {
            throw new IllegalStateException("Timeline has no track for " + String.valueOf(attribute));
        }
        return lv.toModification(attribute, this.periodTicks, timeSupplier);
    }

    public static class Builder {
        private Optional<Integer> periodTicks = Optional.empty();
        private final ImmutableMap.Builder<EnvironmentAttribute<?>, TimelineEntry<?, ?>> entries = ImmutableMap.builder();

        Builder() {
        }

        public Builder period(int periodTicks) {
            this.periodTicks = Optional.of(periodTicks);
            return this;
        }

        public <Value, Argument> Builder entry(EnvironmentAttribute<Value> attribute, EnvironmentAttributeModifier<Value, Argument> modifier, Consumer<Track.Builder<Argument>> builderCallback) {
            attribute.getType().validate(modifier);
            Track.Builder lv = new Track.Builder();
            builderCallback.accept(lv);
            this.entries.put(attribute, new TimelineEntry<Value, Argument>(modifier, lv.build()));
            return this;
        }

        public <Value> Builder entry(EnvironmentAttribute<Value> attribute, Consumer<Track.Builder<Value>> builderCallback) {
            return this.entry(attribute, EnvironmentAttributeModifier.override(), builderCallback);
        }

        public Timeline build() {
            return new Timeline(this.periodTicks, this.entries.build());
        }
    }
}

