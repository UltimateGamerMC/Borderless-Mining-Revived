/*
 * External method calls:
 *   Lnet/minecraft/storage/ReadView;read(Ljava/lang/String;Lcom/mojang/serialization/Codec;)Ljava/util/Optional;
 *   Lnet/minecraft/entity/spawn/SpawnContext;world()Lnet/minecraft/world/ServerWorldAccess;
 *   Lnet/minecraft/registry/Registry;streamEntries()Ljava/util/stream/Stream;
 *   Lnet/minecraft/entity/VariantSelectorProvider;select(Ljava/util/stream/Stream;Ljava/util/function/Function;Lnet/minecraft/util/math/random/Random;Ljava/lang/Object;)Ljava/util/Optional;
 *   Lnet/minecraft/registry/RegistryKey;of(Lnet/minecraft/registry/RegistryKey;Lnet/minecraft/util/Identifier;)Lnet/minecraft/registry/RegistryKey;
 */
package net.minecraft.entity;

import java.util.Optional;
import java.util.stream.Stream;
import net.minecraft.entity.VariantSelectorProvider;
import net.minecraft.entity.spawn.SpawnContext;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.util.Identifier;
import net.minecraft.world.ServerWorldAccess;

public class Variants {
    public static final String VARIANT_NBT_KEY = "variant";

    public static <T> RegistryEntry<T> getOrDefaultOrThrow(DynamicRegistryManager registries, RegistryKey<T> variantKey) {
        RegistryWrapper.Impl lv = registries.getOrThrow(variantKey.getRegistryRef());
        return lv.getOptional(variantKey).or(((Registry)lv)::getDefaultEntry).orElseThrow();
    }

    public static <T> RegistryEntry<T> getDefaultOrThrow(DynamicRegistryManager registries, RegistryKey<? extends Registry<T>> registryRef) {
        return registries.getOrThrow(registryRef).getDefaultEntry().orElseThrow();
    }

    public static <T> void writeData(WriteView view, RegistryEntry<T> variantEntry) {
        variantEntry.getKey().ifPresent(key -> view.put(VARIANT_NBT_KEY, Identifier.CODEC, key.getValue()));
    }

    public static <T> Optional<RegistryEntry<T>> fromData(ReadView view, RegistryKey<? extends Registry<T>> registryRef) {
        return view.read(VARIANT_NBT_KEY, Identifier.CODEC).map(id -> RegistryKey.of(registryRef, id)).flatMap(view.getRegistries()::getOptionalEntry);
    }

    public static <T extends VariantSelectorProvider<SpawnContext, ?>> Optional<RegistryEntry.Reference<T>> select(SpawnContext context, RegistryKey<Registry<T>> registryRef) {
        ServerWorldAccess lv = context.world();
        Stream stream = lv.getRegistryManager().getOrThrow(registryRef).streamEntries();
        return VariantSelectorProvider.select(stream, RegistryEntry::value, lv.getRandom(), context);
    }
}

