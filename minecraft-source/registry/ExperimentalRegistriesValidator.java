/*
 * External method calls:
 *   Lnet/minecraft/registry/DynamicRegistryManager;of(Lnet/minecraft/registry/Registry;)Lnet/minecraft/registry/DynamicRegistryManager$Immutable;
 *   Lnet/minecraft/registry/RegistryBuilder;createWrapperLookup(Lnet/minecraft/registry/DynamicRegistryManager;Lnet/minecraft/registry/RegistryWrapper$WrapperLookup;Lnet/minecraft/registry/RegistryCloner$CloneableRegistries;)Lnet/minecraft/registry/RegistryBuilder$FullPatchesRegistriesPair;
 *   Lnet/minecraft/registry/RegistryBuilder$FullPatchesRegistriesPair;full()Lnet/minecraft/registry/RegistryWrapper$WrapperLookup;
 *   Lnet/minecraft/registry/BuiltinRegistries;validate(Lnet/minecraft/registry/RegistryEntryLookup;Lnet/minecraft/registry/RegistryWrapper;)V
 *   Lnet/minecraft/registry/RegistryLoader$Entry;addToCloner(Ljava/util/function/BiConsumer;)V
 */
package net.minecraft.registry;

import com.mojang.datafixers.DataFixUtils;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import net.minecraft.registry.BuiltinRegistries;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryBuilder;
import net.minecraft.registry.RegistryCloner;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryLoader;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.PlacedFeature;

public class ExperimentalRegistriesValidator {
    public static CompletableFuture<RegistryBuilder.FullPatchesRegistriesPair> validate(CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture, RegistryBuilder builder) {
        return registriesFuture.thenApply(registries -> {
            DynamicRegistryManager.Immutable lv = DynamicRegistryManager.of(Registries.REGISTRIES);
            RegistryCloner.CloneableRegistries lv2 = new RegistryCloner.CloneableRegistries();
            RegistryLoader.DYNAMIC_REGISTRIES.forEach(entry -> entry.addToCloner(lv2::add));
            RegistryBuilder.FullPatchesRegistriesPair lv3 = builder.createWrapperLookup(lv, (RegistryWrapper.WrapperLookup)registries, lv2);
            RegistryWrapper.WrapperLookup lv4 = lv3.full();
            Optional<RegistryWrapper.Impl<Biome>> optional = lv4.getOptional(RegistryKeys.BIOME);
            Optional<RegistryWrapper.Impl<PlacedFeature>> optional2 = lv4.getOptional(RegistryKeys.PLACED_FEATURE);
            if (optional.isPresent() || optional2.isPresent()) {
                BuiltinRegistries.validate(DataFixUtils.orElseGet(optional2, () -> registries.getOrThrow(RegistryKeys.PLACED_FEATURE)), DataFixUtils.orElseGet(optional, () -> registries.getOrThrow(RegistryKeys.BIOME)));
            }
            return lv3;
        });
    }
}

