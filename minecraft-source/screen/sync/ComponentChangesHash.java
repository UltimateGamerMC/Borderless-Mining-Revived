/*
 * External method calls:
 *   Lnet/minecraft/component/ComponentChanges;toAddedRemovedPair()Lnet/minecraft/component/ComponentChanges$AddedRemovedPair;
 *   Lnet/minecraft/component/ComponentChanges$AddedRemovedPair;added()Lnet/minecraft/component/ComponentMap;
 *   Lnet/minecraft/component/ComponentMap;forEach(Ljava/util/function/Consumer;)V
 *   Lnet/minecraft/component/ComponentChanges$AddedRemovedPair;removed()Ljava/util/Set;
 *   Lnet/minecraft/component/Component;type()Lnet/minecraft/component/ComponentType;
 *   Lnet/minecraft/screen/sync/ComponentChangesHash$ComponentHasher;apply(Ljava/lang/Object;)Ljava/lang/Object;
 *   Lnet/minecraft/network/codec/PacketCodecs;registryValue(Lnet/minecraft/registry/RegistryKey;)Lnet/minecraft/network/codec/PacketCodec;
 *   Lnet/minecraft/network/codec/PacketCodecs;map(Ljava/util/function/IntFunction;Lnet/minecraft/network/codec/PacketCodec;Lnet/minecraft/network/codec/PacketCodec;I)Lnet/minecraft/network/codec/PacketCodec;
 *   Lnet/minecraft/network/codec/PacketCodecs;collection(Ljava/util/function/IntFunction;Lnet/minecraft/network/codec/PacketCodec;I)Lnet/minecraft/network/codec/PacketCodec;
 *   Lnet/minecraft/network/codec/PacketCodec;tuple(Lnet/minecraft/network/codec/PacketCodec;Ljava/util/function/Function;Lnet/minecraft/network/codec/PacketCodec;Ljava/util/function/Function;Ljava/util/function/BiFunction;)Lnet/minecraft/network/codec/PacketCodec;
 */
package net.minecraft.screen.sync;

import java.util.HashMap;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import net.minecraft.component.Component;
import net.minecraft.component.ComponentChanges;
import net.minecraft.component.ComponentType;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.RegistryKeys;

public record ComponentChangesHash(Map<ComponentType<?>, Integer> addedComponents, Set<ComponentType<?>> removedComponents) {
    public static final PacketCodec<RegistryByteBuf, ComponentChangesHash> PACKET_CODEC = PacketCodec.tuple(PacketCodecs.map(HashMap::new, PacketCodecs.registryValue(RegistryKeys.DATA_COMPONENT_TYPE), PacketCodecs.INTEGER, 256), ComponentChangesHash::addedComponents, PacketCodecs.collection(HashSet::new, PacketCodecs.registryValue(RegistryKeys.DATA_COMPONENT_TYPE), 256), ComponentChangesHash::removedComponents, ComponentChangesHash::new);

    public static ComponentChangesHash fromComponents(ComponentChanges changes, ComponentHasher hasher) {
        ComponentChanges.AddedRemovedPair lv = changes.toAddedRemovedPair();
        IdentityHashMap map = new IdentityHashMap(lv.added().size());
        lv.added().forEach(component -> map.put(component.type(), (Integer)hasher.apply(component)));
        return new ComponentChangesHash(map, lv.removed());
    }

    public boolean hashEquals(ComponentChanges changes, ComponentHasher hasher) {
        ComponentChanges.AddedRemovedPair lv = changes.toAddedRemovedPair();
        if (!lv.removed().equals(this.removedComponents)) {
            return false;
        }
        if (this.addedComponents.size() != lv.added().size()) {
            return false;
        }
        for (Component<?> lv2 : lv.added()) {
            Integer integer = this.addedComponents.get(lv2.type());
            if (integer == null) {
                return false;
            }
            Integer integer2 = (Integer)hasher.apply(lv2);
            if (integer2.equals(integer)) continue;
            return false;
        }
        return true;
    }

    @FunctionalInterface
    public static interface ComponentHasher
    extends Function<Component<?>, Integer> {
    }
}

