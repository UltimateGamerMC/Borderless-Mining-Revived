/*
 * External method calls:
 *   Lnet/minecraft/world/attribute/EnvironmentAttributeMap;apply(Lnet/minecraft/world/attribute/EnvironmentAttribute;Ljava/lang/Object;)Ljava/lang/Object;
 *   Lnet/minecraft/world/attribute/EnvironmentAttributeType;spatialLerp()Lnet/minecraft/util/math/Interpolator;
 */
package net.minecraft.world.attribute;

import it.unimi.dsi.fastutil.objects.Reference2DoubleArrayMap;
import it.unimi.dsi.fastutil.objects.Reference2DoubleMap;
import it.unimi.dsi.fastutil.objects.Reference2DoubleMaps;
import java.util.Objects;
import net.minecraft.util.math.Interpolator;
import net.minecraft.world.attribute.EnvironmentAttribute;
import net.minecraft.world.attribute.EnvironmentAttributeMap;

public class WeightedAttributeList {
    private final Reference2DoubleArrayMap<EnvironmentAttributeMap> entries = new Reference2DoubleArrayMap();

    public void clear() {
        this.entries.clear();
    }

    public WeightedAttributeList add(double weight, EnvironmentAttributeMap attributes) {
        this.entries.mergeDouble(attributes, weight, Double::sum);
        return this;
    }

    public <Value> Value interpolate(EnvironmentAttribute<Value> attribute, Value defaultValue) {
        if (this.entries.isEmpty()) {
            return defaultValue;
        }
        if (this.entries.size() == 1) {
            EnvironmentAttributeMap lv = (EnvironmentAttributeMap)this.entries.keySet().iterator().next();
            return lv.apply(attribute, defaultValue);
        }
        Interpolator<Value> lv2 = attribute.getType().spatialLerp();
        Object object2 = null;
        double d = 0.0;
        for (Reference2DoubleMap.Entry entry : Reference2DoubleMaps.fastIterable(this.entries)) {
            EnvironmentAttributeMap lv3 = (EnvironmentAttributeMap)entry.getKey();
            double e = entry.getDoubleValue();
            Value object3 = lv3.apply(attribute, defaultValue);
            d += e;
            if (object2 == null) {
                object2 = object3;
                continue;
            }
            float f = (float)(e / d);
            object2 = lv2.apply(f, object2, object3);
        }
        return Objects.requireNonNull(object2);
    }
}

