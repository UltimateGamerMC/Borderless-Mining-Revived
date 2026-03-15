/*
 * External method calls:
 *   Lnet/minecraft/world/attribute/EnvironmentAttributeFunction$Constant;applyConstant(Ljava/lang/Object;)Ljava/lang/Object;
 *   Lnet/minecraft/world/dimension/DimensionType;timelines()Lnet/minecraft/registry/entry/RegistryEntryList;
 *   Lnet/minecraft/registry/entry/RegistryEntryList;forEach(Ljava/util/function/Consumer;)V
 *   Lnet/minecraft/world/attribute/WeatherAttributes$WeatherAccess;ofWorld(Lnet/minecraft/world/World;)Lnet/minecraft/world/attribute/WeatherAttributes$WeatherAccess;
 *   Lnet/minecraft/world/attribute/WeatherAttributes;addWeatherAttributes(Lnet/minecraft/world/attribute/WorldEnvironmentAttributeAccess$Builder;Lnet/minecraft/world/attribute/WeatherAttributes$WeatherAccess;)V
 *   Lnet/minecraft/world/dimension/DimensionType;attributes()Lnet/minecraft/world/attribute/EnvironmentAttributeMap;
 *   Lnet/minecraft/world/attribute/WorldEnvironmentAttributeAccess$Builder;addFromMap(Lnet/minecraft/world/attribute/EnvironmentAttributeMap;)Lnet/minecraft/world/attribute/WorldEnvironmentAttributeAccess$Builder;
 *   Lnet/minecraft/registry/RegistryWrapper;streamEntries()Ljava/util/stream/Stream;
 *   Lnet/minecraft/world/attribute/WorldEnvironmentAttributeAccess$Builder;positional(Lnet/minecraft/world/attribute/EnvironmentAttribute;Lnet/minecraft/world/attribute/EnvironmentAttributeFunction$Positional;)Lnet/minecraft/world/attribute/WorldEnvironmentAttributeAccess$Builder;
 *   Lnet/minecraft/world/attribute/WeightedAttributeList;interpolate(Lnet/minecraft/world/attribute/EnvironmentAttribute;Ljava/lang/Object;)Ljava/lang/Object;
 *   Lnet/minecraft/world/attribute/EnvironmentAttributeMap;apply(Lnet/minecraft/world/attribute/EnvironmentAttribute;Ljava/lang/Object;)Ljava/lang/Object;
 *   Lnet/minecraft/world/attribute/EnvironmentAttributeMap;keySet()Ljava/util/Set;
 *   Lnet/minecraft/world/attribute/WorldEnvironmentAttributeAccess$Builder;addFromTimeline(Lnet/minecraft/registry/entry/RegistryEntry;Ljava/util/function/LongSupplier;)Lnet/minecraft/world/attribute/WorldEnvironmentAttributeAccess$Builder;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/world/attribute/WorldEnvironmentAttributeAccess;addModifiersFromDimension(Lnet/minecraft/world/attribute/WorldEnvironmentAttributeAccess$Builder;Lnet/minecraft/world/dimension/DimensionType;)V
 *   Lnet/minecraft/world/attribute/WorldEnvironmentAttributeAccess;addModifiersFromBiomes(Lnet/minecraft/world/attribute/WorldEnvironmentAttributeAccess$Builder;Lnet/minecraft/registry/RegistryWrapper;Lnet/minecraft/world/biome/source/BiomeAccess;)V
 *   Lnet/minecraft/world/attribute/WorldEnvironmentAttributeAccess;addModifiersFromBiomes(Lnet/minecraft/world/attribute/WorldEnvironmentAttributeAccess$Builder;Lnet/minecraft/world/attribute/EnvironmentAttribute;Lnet/minecraft/world/biome/source/BiomeAccess;)V
 *   Lnet/minecraft/world/attribute/WorldEnvironmentAttributeAccess;computeEntry(Lnet/minecraft/world/attribute/EnvironmentAttribute;Ljava/util/List;)Lnet/minecraft/world/attribute/WorldEnvironmentAttributeAccess$Entry;
 */
package net.minecraft.world.attribute;

import com.google.common.annotations.VisibleForTesting;
import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap;
import java.lang.runtime.SwitchBootstraps;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.LongSupplier;
import java.util.stream.Stream;
import net.minecraft.SharedConstants;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.attribute.EnvironmentAttribute;
import net.minecraft.world.attribute.EnvironmentAttributeAccess;
import net.minecraft.world.attribute.EnvironmentAttributeFunction;
import net.minecraft.world.attribute.EnvironmentAttributeMap;
import net.minecraft.world.attribute.WeatherAttributes;
import net.minecraft.world.attribute.WeightedAttributeList;
import net.minecraft.world.attribute.timeline.Timeline;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeAccess;
import net.minecraft.world.dimension.DimensionType;
import org.jspecify.annotations.Nullable;

public class WorldEnvironmentAttributeAccess
implements EnvironmentAttributeAccess {
    private final Map<EnvironmentAttribute<?>, Entry<?>> entries = new Reference2ObjectOpenHashMap();

    WorldEnvironmentAttributeAccess(Map<EnvironmentAttribute<?>, List<EnvironmentAttributeFunction<?>>> modificationsByAttribute) {
        modificationsByAttribute.forEach((attribute, mods) -> this.entries.put((EnvironmentAttribute<?>)attribute, this.computeEntry((EnvironmentAttribute)attribute, (List<? extends EnvironmentAttributeFunction<?>>)mods)));
    }

    private <Value> Entry<Value> computeEntry(EnvironmentAttribute<Value> attribute, List<? extends EnvironmentAttributeFunction<?>> mods) {
        Object e;
        ArrayList list2 = new ArrayList(mods);
        Value object = attribute.getDefaultValue();
        while (!list2.isEmpty() && (e = list2.getFirst()) instanceof EnvironmentAttributeFunction.Constant) {
            EnvironmentAttributeFunction.Constant lv = (EnvironmentAttributeFunction.Constant)e;
            object = lv.applyConstant(object);
            list2.removeFirst();
        }
        boolean bl = list2.stream().anyMatch(function -> function instanceof EnvironmentAttributeFunction.Positional);
        return new Entry<Value>(attribute, object, List.copyOf(list2), bl);
    }

    public static Builder builder() {
        return new Builder();
    }

    static void addModifiersFromWorld(Builder builder, World world) {
        DynamicRegistryManager lv = world.getRegistryManager();
        BiomeAccess lv2 = world.getBiomeAccess();
        LongSupplier longSupplier = world::getTimeOfDay;
        WorldEnvironmentAttributeAccess.addModifiersFromDimension(builder, world.getDimension());
        WorldEnvironmentAttributeAccess.addModifiersFromBiomes(builder, lv.getOrThrow(RegistryKeys.BIOME), lv2);
        world.getDimension().timelines().forEach(attribute -> builder.addFromTimeline((RegistryEntry<Timeline>)attribute, longSupplier));
        if (world.canHaveWeather()) {
            WeatherAttributes.addWeatherAttributes(builder, WeatherAttributes.WeatherAccess.ofWorld(world));
        }
    }

    private static void addModifiersFromDimension(Builder builder, DimensionType dimensionType) {
        builder.addFromMap(dimensionType.attributes());
    }

    private static void addModifiersFromBiomes(Builder builder, RegistryWrapper<Biome> biome2, BiomeAccess biomeAccess) {
        Stream stream = biome2.streamEntries().flatMap(biome -> ((Biome)biome.value()).getEnvironmentAttributes().keySet().stream()).distinct();
        stream.forEach(attribute -> WorldEnvironmentAttributeAccess.addModifiersFromBiomes(builder, attribute, biomeAccess));
    }

    private static <Value> void addModifiersFromBiomes(Builder builder, EnvironmentAttribute<Value> attribute, BiomeAccess biomeAccess) {
        builder.positional(attribute, (value, pos, weightedAttributeList) -> {
            if (weightedAttributeList != null && attribute.isInterpolated()) {
                return weightedAttributeList.interpolate(attribute, value);
            }
            RegistryEntry<Biome> lv = biomeAccess.getBiomeForNoiseGen(pos.x, pos.y, pos.z);
            return lv.value().getEnvironmentAttributes().apply(attribute, value);
        });
    }

    public void tick() {
        this.entries.values().forEach(Entry::tick);
    }

    private <Value> @Nullable Entry<Value> getEntry(EnvironmentAttribute<Value> attribute) {
        return this.entries.get(attribute);
    }

    @Override
    public <Value> Value getAttributeValue(EnvironmentAttribute<Value> attribute) {
        if (SharedConstants.isDevelopment && attribute.isPositional()) {
            throw new IllegalStateException("Position must always be provided for positional attribute " + String.valueOf(attribute));
        }
        Entry<Value> lv = this.getEntry(attribute);
        if (lv == null) {
            return attribute.getDefaultValue();
        }
        return lv.get();
    }

    @Override
    public <Value> Value getAttributeValue(EnvironmentAttribute<Value> attribute, Vec3d pos, @Nullable WeightedAttributeList pool) {
        Entry<Value> lv = this.getEntry(attribute);
        if (lv == null) {
            return attribute.getDefaultValue();
        }
        return lv.getAt(pos, pool);
    }

    @VisibleForTesting
    <Value> Value getDefaultValue(EnvironmentAttribute<Value> attribute) {
        Entry<Value> lv = this.getEntry(attribute);
        return lv != null ? lv.defaultValue : attribute.getDefaultValue();
    }

    @VisibleForTesting
    boolean isPositional(EnvironmentAttribute<?> attribute) {
        Entry<?> lv = this.getEntry(attribute);
        return lv != null && lv.positional;
    }

    static class Entry<Value> {
        private final EnvironmentAttribute<Value> attribute;
        final Value defaultValue;
        private final List<EnvironmentAttributeFunction<Value>> modifications;
        final boolean positional;
        private @Nullable Value cachedValue;
        private int age;

        Entry(EnvironmentAttribute<Value> attribute, Value defaultValue, List<EnvironmentAttributeFunction<Value>> modifications, boolean positional) {
            this.attribute = attribute;
            this.defaultValue = defaultValue;
            this.modifications = modifications;
            this.positional = positional;
        }

        public void tick() {
            this.cachedValue = null;
            ++this.age;
        }

        public Value get() {
            if (this.cachedValue != null) {
                return this.cachedValue;
            }
            Value object = this.compute();
            this.cachedValue = object;
            return object;
        }

        public Value getAt(Vec3d pos, @Nullable WeightedAttributeList weightedAttributeList) {
            if (!this.positional) {
                return this.get();
            }
            return this.computeAt(pos, weightedAttributeList);
        }

        private Value computeAt(Vec3d pos, @Nullable WeightedAttributeList weightedAttributeList) {
            Value object = this.defaultValue;
            for (EnvironmentAttributeFunction<Value> lv : this.modifications) {
                EnvironmentAttributeFunction<Value> environmentAttributeFunction;
                Objects.requireNonNull(lv);
                int n = 0;
                object = switch (SwitchBootstraps.typeSwitch("typeSwitch", new Object[]{EnvironmentAttributeFunction.Constant.class, EnvironmentAttributeFunction.TimeBased.class, EnvironmentAttributeFunction.Positional.class}, environmentAttributeFunction, n)) {
                    default -> throw new MatchException(null, null);
                    case 0 -> {
                        EnvironmentAttributeFunction.Constant lv2 = (EnvironmentAttributeFunction.Constant)environmentAttributeFunction;
                        yield lv2.applyConstant(object);
                    }
                    case 1 -> {
                        EnvironmentAttributeFunction.TimeBased lv3 = (EnvironmentAttributeFunction.TimeBased)environmentAttributeFunction;
                        yield lv3.applyTimeBased(object, this.age);
                    }
                    case 2 -> {
                        EnvironmentAttributeFunction.Positional lv4 = (EnvironmentAttributeFunction.Positional)environmentAttributeFunction;
                        yield lv4.applyPositional(object, Objects.requireNonNull(pos), weightedAttributeList);
                    }
                };
            }
            return this.attribute.clamp(object);
        }

        private Value compute() {
            Value object = this.defaultValue;
            for (EnvironmentAttributeFunction<Value> lv : this.modifications) {
                EnvironmentAttributeFunction<Value> environmentAttributeFunction;
                Objects.requireNonNull(lv);
                int n = 0;
                object = switch (SwitchBootstraps.typeSwitch("typeSwitch", new Object[]{EnvironmentAttributeFunction.Constant.class, EnvironmentAttributeFunction.TimeBased.class, EnvironmentAttributeFunction.Positional.class}, environmentAttributeFunction, n)) {
                    default -> throw new MatchException(null, null);
                    case 0 -> {
                        EnvironmentAttributeFunction.Constant lv2 = (EnvironmentAttributeFunction.Constant)environmentAttributeFunction;
                        yield lv2.applyConstant(object);
                    }
                    case 1 -> {
                        EnvironmentAttributeFunction.TimeBased lv3 = (EnvironmentAttributeFunction.TimeBased)environmentAttributeFunction;
                        yield lv3.applyTimeBased(object, this.age);
                    }
                    case 2 -> {
                        EnvironmentAttributeFunction.Positional lv4 = (EnvironmentAttributeFunction.Positional)environmentAttributeFunction;
                        yield object;
                    }
                };
            }
            return this.attribute.clamp(object);
        }
    }

    public static class Builder {
        private final Map<EnvironmentAttribute<?>, List<EnvironmentAttributeFunction<?>>> modifications = new HashMap();

        Builder() {
        }

        public Builder world(World world) {
            WorldEnvironmentAttributeAccess.addModifiersFromWorld(this, world);
            return this;
        }

        public Builder addFromMap(EnvironmentAttributeMap attributes) {
            for (EnvironmentAttribute<?> lv : attributes.keySet()) {
                this.addFromMap(lv, attributes);
            }
            return this;
        }

        private <Value> Builder addFromMap(EnvironmentAttribute<Value> attribute, EnvironmentAttributeMap attributeMap) {
            EnvironmentAttributeMap.Entry<Value, ?> lv = attributeMap.getEntry(attribute);
            if (lv == null) {
                throw new IllegalArgumentException("Missing attribute " + String.valueOf(attribute));
            }
            return this.constant(attribute, lv::apply);
        }

        public <Value> Builder constant(EnvironmentAttribute<Value> attribute, EnvironmentAttributeFunction.Constant<Value> mod) {
            return this.addModification(attribute, mod);
        }

        public <Value> Builder timeBased(EnvironmentAttribute<Value> attribute, EnvironmentAttributeFunction.TimeBased<Value> mod) {
            return this.addModification(attribute, mod);
        }

        public <Value> Builder positional(EnvironmentAttribute<Value> attribute, EnvironmentAttributeFunction.Positional<Value> mod) {
            return this.addModification(attribute, mod);
        }

        private <Value> Builder addModification(EnvironmentAttribute<Value> attribute, EnvironmentAttributeFunction<Value> mod) {
            this.modifications.computeIfAbsent(attribute, attr -> new ArrayList()).add(mod);
            return this;
        }

        public Builder addFromTimeline(RegistryEntry<Timeline> timeline, LongSupplier timeSupplier) {
            for (EnvironmentAttribute<?> lv : timeline.value().getAttributes()) {
                this.addModificationFromTimeline(timeline, lv, timeSupplier);
            }
            return this;
        }

        private <Value> void addModificationFromTimeline(RegistryEntry<Timeline> timeline, EnvironmentAttribute<Value> attribute, LongSupplier timeSupplier) {
            this.timeBased(attribute, timeline.value().getModification(attribute, timeSupplier));
        }

        public WorldEnvironmentAttributeAccess build() {
            return new WorldEnvironmentAttributeAccess(this.modifications);
        }
    }
}

