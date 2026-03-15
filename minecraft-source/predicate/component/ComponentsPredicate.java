/*
 * External method calls:
 *   Lnet/minecraft/predicate/component/ComponentMapPredicate;test(Lnet/minecraft/component/ComponentsAccess;)Z
 *   Lnet/minecraft/predicate/component/ComponentPredicate;test(Lnet/minecraft/component/ComponentsAccess;)Z
 *   Lnet/minecraft/network/codec/PacketCodec;tuple(Lnet/minecraft/network/codec/PacketCodec;Ljava/util/function/Function;Lnet/minecraft/network/codec/PacketCodec;Ljava/util/function/Function;Ljava/util/function/BiFunction;)Lnet/minecraft/network/codec/PacketCodec;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/predicate/component/ComponentsPredicate;test(Lnet/minecraft/component/ComponentsAccess;)Z
 */
package net.minecraft.predicate.component;

import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Map;
import java.util.function.Predicate;
import net.minecraft.component.ComponentType;
import net.minecraft.component.ComponentsAccess;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.predicate.component.ComponentMapPredicate;
import net.minecraft.predicate.component.ComponentPredicate;

public record ComponentsPredicate(ComponentMapPredicate exact, Map<ComponentPredicate.Type<?>, ComponentPredicate> partial) implements Predicate<ComponentsAccess>
{
    public static final ComponentsPredicate EMPTY = new ComponentsPredicate(ComponentMapPredicate.EMPTY, Map.of());
    public static final MapCodec<ComponentsPredicate> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(ComponentMapPredicate.CODEC.optionalFieldOf("components", ComponentMapPredicate.EMPTY).forGetter(ComponentsPredicate::exact), ComponentPredicate.PREDICATES_MAP_CODEC.optionalFieldOf("predicates", Map.of()).forGetter(ComponentsPredicate::partial)).apply((Applicative<ComponentsPredicate, ?>)instance, ComponentsPredicate::new));
    public static final PacketCodec<RegistryByteBuf, ComponentsPredicate> PACKET_CODEC = PacketCodec.tuple(ComponentMapPredicate.PACKET_CODEC, ComponentsPredicate::exact, ComponentPredicate.PREDICATES_MAP_PACKET_CODEC, ComponentsPredicate::partial, ComponentsPredicate::new);

    @Override
    public boolean test(ComponentsAccess arg) {
        if (!this.exact.test(arg)) {
            return false;
        }
        for (ComponentPredicate lv : this.partial.values()) {
            if (lv.test(arg)) continue;
            return false;
        }
        return true;
    }

    public boolean isEmpty() {
        return this.exact.isEmpty() && this.partial.isEmpty();
    }

    @Override
    public /* synthetic */ boolean test(Object components) {
        return this.test((ComponentsAccess)components);
    }

    public static class Builder {
        private ComponentMapPredicate exact = ComponentMapPredicate.EMPTY;
        private final ImmutableMap.Builder<ComponentPredicate.Type<?>, ComponentPredicate> partial = ImmutableMap.builder();

        private Builder() {
        }

        public static Builder create() {
            return new Builder();
        }

        public <T extends ComponentType<?>> Builder has(ComponentType<?> type) {
            ComponentPredicate.OfExistence lv = ComponentPredicate.OfExistence.toPredicateType(type);
            this.partial.put(lv, lv.getPredicate());
            return this;
        }

        public <T extends ComponentPredicate> Builder partial(ComponentPredicate.Type<T> type, T predicate) {
            this.partial.put(type, predicate);
            return this;
        }

        public Builder exact(ComponentMapPredicate exact) {
            this.exact = exact;
            return this;
        }

        public ComponentsPredicate build() {
            return new ComponentsPredicate(this.exact, this.partial.buildOrThrow());
        }
    }
}

