/*
 * External method calls:
 *   Lnet/minecraft/world/attribute/EnvironmentAttributeType;modifierCodec()Lcom/mojang/serialization/Codec;
 *   Lnet/minecraft/world/attribute/EnvironmentAttributeModifier;override()Lnet/minecraft/world/attribute/EnvironmentAttributeModifier;
 *   Lnet/minecraft/util/Util;memoize(Ljava/util/function/Function;)Ljava/util/function/Function;
 *   Lnet/minecraft/world/attribute/EnvironmentAttributeModifier;argumentCodec(Lnet/minecraft/world/attribute/EnvironmentAttribute;)Lcom/mojang/serialization/Codec;
 *   Lnet/minecraft/world/attribute/timeline/Track;createCodec(Lcom/mojang/serialization/Codec;)Lcom/mojang/serialization/MapCodec;
 *   Lnet/minecraft/world/attribute/EnvironmentAttributeModifier;argumentKeyframeLerp(Lnet/minecraft/world/attribute/EnvironmentAttribute;)Lnet/minecraft/util/math/Interpolator;
 *   Lnet/minecraft/world/attribute/timeline/Track;validateKeyframesInPeriod(Lnet/minecraft/world/attribute/timeline/Track;I)Lcom/mojang/serialization/DataResult;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/world/attribute/timeline/TimelineEntry;argumentTrack()Lnet/minecraft/world/attribute/timeline/Track;
 *   Lnet/minecraft/world/attribute/timeline/TimelineEntry;createMapCodec(Lnet/minecraft/world/attribute/EnvironmentAttribute;Lnet/minecraft/world/attribute/EnvironmentAttributeModifier;)Lcom/mojang/serialization/MapCodec;
 */
package net.minecraft.world.attribute.timeline;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import java.util.Optional;
import java.util.function.LongSupplier;
import net.minecraft.util.Util;
import net.minecraft.world.attribute.EnvironmentAttribute;
import net.minecraft.world.attribute.EnvironmentAttributeModifier;
import net.minecraft.world.attribute.timeline.Track;
import net.minecraft.world.attribute.timeline.TrackAttributeModification;

public record TimelineEntry<Value, Argument>(EnvironmentAttributeModifier<Value, Argument> modifier, Track<Argument> argumentTrack) {
    public static <Value> Codec<TimelineEntry<Value, ?>> createCodec(EnvironmentAttribute<Value> attribute) {
        MapCodec mapCodec = attribute.getType().modifierCodec().optionalFieldOf("modifier", EnvironmentAttributeModifier.override());
        return mapCodec.dispatch(TimelineEntry::modifier, Util.memoize(modifier -> TimelineEntry.createMapCodec(attribute, modifier)));
    }

    private static <Value, Argument> MapCodec<TimelineEntry<Value, Argument>> createMapCodec(EnvironmentAttribute<Value> attribute, EnvironmentAttributeModifier<Value, Argument> modifier) {
        return Track.createCodec(modifier.argumentCodec(attribute)).xmap(argumentTrack -> new TimelineEntry(modifier, argumentTrack), TimelineEntry::argumentTrack);
    }

    public TrackAttributeModification<Value, Argument> toModification(EnvironmentAttribute<Value> attribute, Optional<Integer> period, LongSupplier timeSupplier) {
        return new TrackAttributeModification<Value, Argument>(period, this.modifier, this.argumentTrack, this.modifier.argumentKeyframeLerp(attribute), timeSupplier);
    }

    public static DataResult<TimelineEntry<?, ?>> validateKeyframesInPeriod(TimelineEntry<?, ?> entry, int period) {
        return Track.validateKeyframesInPeriod(entry.argumentTrack(), period).map(track -> entry);
    }
}

