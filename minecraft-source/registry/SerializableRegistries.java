/*
 * External method calls:
 *   Lnet/minecraft/registry/RegistryLoader$Entry;key()Lnet/minecraft/registry/RegistryKey;
 *   Lnet/minecraft/registry/DynamicRegistryManager;streamAllRegistries()Ljava/util/stream/Stream;
 *   Lnet/minecraft/registry/DynamicRegistryManager$Immutable;streamAllRegistries()Ljava/util/stream/Stream;
 *   Lnet/minecraft/registry/DynamicRegistryManager$Entry;key()Lnet/minecraft/registry/RegistryKey;
 *   Lnet/minecraft/registry/Registry;streamEntries()Ljava/util/stream/Stream;
 *   Lnet/minecraft/registry/entry/RegistryEntry$Reference;registryKey()Lnet/minecraft/registry/RegistryKey;
 *   Lnet/minecraft/registry/RegistryLoader$Entry;elementCodec()Lcom/mojang/serialization/Codec;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/registry/SerializableRegistries;stream(Lnet/minecraft/registry/DynamicRegistryManager;)Ljava/util/stream/Stream;
 *   Lnet/minecraft/registry/SerializableRegistries;streamDynamicEntries(Lnet/minecraft/registry/CombinedDynamicRegistries;)Ljava/util/stream/Stream;
 *   Lnet/minecraft/registry/SerializableRegistries;serialize(Lcom/mojang/serialization/DynamicOps;Lnet/minecraft/registry/RegistryLoader$Entry;Lnet/minecraft/registry/DynamicRegistryManager;Ljava/util/Set;Ljava/util/function/BiConsumer;)V
 */
package net.minecraft.registry;

import com.mojang.serialization.DynamicOps;
import io.netty.buffer.ByteBuf;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import net.minecraft.nbt.NbtElement;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.CombinedDynamicRegistries;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryLoader;
import net.minecraft.registry.ServerDynamicRegistryType;
import net.minecraft.registry.VersionedIdentifier;
import net.minecraft.registry.entry.RegistryEntryInfo;
import net.minecraft.util.Identifier;

public class SerializableRegistries {
    private static final Set<RegistryKey<? extends Registry<?>>> SYNCED_REGISTRIES = RegistryLoader.SYNCED_REGISTRIES.stream().map(RegistryLoader.Entry::key).collect(Collectors.toUnmodifiableSet());

    public static void forEachSyncedRegistry(DynamicOps<NbtElement> nbtOps, DynamicRegistryManager registryManager, Set<VersionedIdentifier> knownPacks, BiConsumer<RegistryKey<? extends Registry<?>>, List<SerializedRegistryEntry>> callback) {
        RegistryLoader.SYNCED_REGISTRIES.forEach(registry -> SerializableRegistries.serialize(nbtOps, registry, registryManager, knownPacks, callback));
    }

    private static <T> void serialize(DynamicOps<NbtElement> nbtOps, RegistryLoader.Entry<T> entry, DynamicRegistryManager registryManager, Set<VersionedIdentifier> knownPacks, BiConsumer<RegistryKey<? extends Registry<?>>, List<SerializedRegistryEntry>> callback) {
        registryManager.getOptional(entry.key()).ifPresent(registry -> {
            ArrayList list = new ArrayList(registry.size());
            registry.streamEntries().forEach(registryEntry -> {
                Optional<NbtElement> optional;
                boolean bl = registry.getEntryInfo(registryEntry.registryKey()).flatMap(RegistryEntryInfo::knownPackInfo).filter(knownPacks::contains).isPresent();
                if (bl) {
                    optional = Optional.empty();
                } else {
                    NbtElement lv = (NbtElement)entry.elementCodec().encodeStart(nbtOps, registryEntry.value()).getOrThrow(error -> new IllegalArgumentException("Failed to serialize " + String.valueOf(registryEntry.registryKey()) + ": " + error));
                    optional = Optional.of(lv);
                }
                list.add(new SerializedRegistryEntry(registryEntry.registryKey().getValue(), optional));
            });
            callback.accept(registry.getKey(), list);
        });
    }

    private static Stream<DynamicRegistryManager.Entry<?>> stream(DynamicRegistryManager dynamicRegistryManager) {
        return dynamicRegistryManager.streamAllRegistries().filter(registry -> SerializableRegistries.isSynced(registry.key()));
    }

    public static Stream<DynamicRegistryManager.Entry<?>> streamDynamicEntries(CombinedDynamicRegistries<ServerDynamicRegistryType> combinedRegistries) {
        return SerializableRegistries.stream(combinedRegistries.getSucceedingRegistryManagers(ServerDynamicRegistryType.WORLDGEN));
    }

    public static Stream<DynamicRegistryManager.Entry<?>> streamRegistryManagerEntries(CombinedDynamicRegistries<ServerDynamicRegistryType> combinedRegistries) {
        Stream<DynamicRegistryManager.Entry<?>> stream = combinedRegistries.get(ServerDynamicRegistryType.STATIC).streamAllRegistries();
        Stream<DynamicRegistryManager.Entry<?>> stream2 = SerializableRegistries.streamDynamicEntries(combinedRegistries);
        return Stream.concat(stream2, stream);
    }

    public static boolean isSynced(RegistryKey<? extends Registry<?>> key) {
        return SYNCED_REGISTRIES.contains(key);
    }

    public record SerializedRegistryEntry(Identifier id, Optional<NbtElement> data) {
        public static final PacketCodec<ByteBuf, SerializedRegistryEntry> PACKET_CODEC = PacketCodec.tuple(Identifier.PACKET_CODEC, SerializedRegistryEntry::id, PacketCodecs.NBT_ELEMENT.collect(PacketCodecs::optional), SerializedRegistryEntry::data, SerializedRegistryEntry::new);
    }
}

