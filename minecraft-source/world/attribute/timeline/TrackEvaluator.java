/*
 * External method calls:
 *   Lnet/minecraft/world/attribute/timeline/Track;keyframes()Ljava/util/List;
 *   Lnet/minecraft/world/attribute/timeline/EasingType;apply(F)F
 *
 * Internal private/static methods:
 *   Lnet/minecraft/world/attribute/timeline/TrackEvaluator;convertToSegments(Lnet/minecraft/world/attribute/timeline/Track;Ljava/util/Optional;)Ljava/util/List;
 *   Lnet/minecraft/world/attribute/timeline/TrackEvaluator;addSegmentsOfKeyframe(Lnet/minecraft/world/attribute/timeline/Track;Ljava/util/List;Ljava/util/List;)V
 *   Lnet/minecraft/world/attribute/timeline/TrackEvaluator;periodize(J)J
 */
package net.minecraft.world.attribute.timeline;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import net.minecraft.util.math.Interpolator;
import net.minecraft.world.attribute.timeline.EasingType;
import net.minecraft.world.attribute.timeline.Keyframe;
import net.minecraft.world.attribute.timeline.Track;

public class TrackEvaluator<T> {
    private final Optional<Integer> period;
    private final Interpolator<T> interpolator;
    private final List<Segment<T>> segments;

    TrackEvaluator(Track<T> track, Optional<Integer> period, Interpolator<T> interpolator) {
        this.period = period;
        this.interpolator = interpolator;
        this.segments = TrackEvaluator.convertToSegments(track, period);
    }

    private static <T> List<Segment<T>> convertToSegments(Track<T> track, Optional<Integer> period) {
        List<Keyframe<T>> list = track.keyframes();
        if (list.size() == 1) {
            T object = list.getFirst().value();
            return List.of(new Segment<T>(EasingType.CONSTANT, object, 0, object, 0));
        }
        ArrayList<Segment<T>> list2 = new ArrayList<Segment<T>>();
        if (period.isPresent()) {
            Keyframe<T> lv = list.getFirst();
            Keyframe<T> lv2 = list.getLast();
            list2.add(new Segment<T>(track, lv2, lv2.ticks() - period.get(), lv, lv.ticks()));
            TrackEvaluator.addSegmentsOfKeyframe(track, list, list2);
            list2.add(new Segment<T>(track, lv2, lv2.ticks(), lv, lv.ticks() + period.get()));
        } else {
            TrackEvaluator.addSegmentsOfKeyframe(track, list, list2);
        }
        return List.copyOf(list2);
    }

    private static <T> void addSegmentsOfKeyframe(Track<T> track, List<Keyframe<T>> keyframes, List<Segment<T>> segmentsOut) {
        for (int i = 0; i < keyframes.size() - 1; ++i) {
            Keyframe<T> lv = keyframes.get(i);
            Keyframe<T> lv2 = keyframes.get(i + 1);
            segmentsOut.add(new Segment<T>(track, lv, lv.ticks(), lv2, lv2.ticks()));
        }
    }

    public T get(long time) {
        long m = this.periodize(time);
        Segment<T> lv = this.getSegmentForTime(m);
        if (m <= (long)lv.fromTicks) {
            return lv.fromValue;
        }
        if (m >= (long)lv.toTicks) {
            return lv.toValue;
        }
        float f = (float)(m - (long)lv.fromTicks) / (float)(lv.toTicks - lv.fromTicks);
        float g = lv.easing.apply(f);
        return this.interpolator.apply(g, lv.fromValue, lv.toValue);
    }

    private Segment<T> getSegmentForTime(long time) {
        for (Segment<T> lv : this.segments) {
            if (time >= (long)lv.toTicks) continue;
            return lv;
        }
        return this.segments.getLast();
    }

    private long periodize(long time) {
        if (this.period.isPresent()) {
            return Math.floorMod(time, (int)this.period.get());
        }
        return time;
    }

    record Segment<T>(EasingType easing, T fromValue, int fromTicks, T toValue, int toTicks) {
        public Segment(Track<T> track, Keyframe<T> fromKeyframe, int fromTicks, Keyframe<T> toKeyframe, int toTicks) {
            this(track.easingType(), fromKeyframe.value(), fromTicks, toKeyframe.value(), toTicks);
        }
    }
}

