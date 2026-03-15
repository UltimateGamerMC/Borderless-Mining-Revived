/*
 * External method calls:
 *   Lnet/minecraft/world/gen/placementmodifier/FixedPlacementModifier;of([Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/world/gen/placementmodifier/FixedPlacementModifier;
 *   Lnet/minecraft/world/gen/placementmodifier/BiomePlacementModifier;of()Lnet/minecraft/world/gen/placementmodifier/BiomePlacementModifier;
 *   Lnet/minecraft/world/gen/feature/PlacedFeatures;register(Lnet/minecraft/registry/Registerable;Lnet/minecraft/registry/RegistryKey;Lnet/minecraft/registry/entry/RegistryEntry;[Lnet/minecraft/world/gen/placementmodifier/PlacementModifier;)V
 *   Lnet/minecraft/world/gen/placementmodifier/RarityFilterPlacementModifier;of(I)Lnet/minecraft/world/gen/placementmodifier/RarityFilterPlacementModifier;
 *   Lnet/minecraft/world/gen/placementmodifier/SquarePlacementModifier;of()Lnet/minecraft/world/gen/placementmodifier/SquarePlacementModifier;
 *   Lnet/minecraft/world/gen/placementmodifier/RandomOffsetPlacementModifier;vertically(Lnet/minecraft/util/math/intprovider/IntProvider;)Lnet/minecraft/world/gen/placementmodifier/RandomOffsetPlacementModifier;
 *   Lnet/minecraft/world/gen/placementmodifier/CountPlacementModifier;of(Lnet/minecraft/util/math/intprovider/IntProvider;)Lnet/minecraft/world/gen/placementmodifier/CountPlacementModifier;
 *   Lnet/minecraft/world/gen/feature/PlacedFeatures;createCountExtraModifier(IFI)Lnet/minecraft/world/gen/placementmodifier/PlacementModifier;
 *   Lnet/minecraft/world/gen/YOffset;fixed(I)Lnet/minecraft/world/gen/YOffset;
 *   Lnet/minecraft/world/gen/placementmodifier/HeightRangePlacementModifier;uniform(Lnet/minecraft/world/gen/YOffset;Lnet/minecraft/world/gen/YOffset;)Lnet/minecraft/world/gen/placementmodifier/HeightRangePlacementModifier;
 *   Lnet/minecraft/world/gen/feature/PlacedFeatures;of(Ljava/lang/String;)Lnet/minecraft/registry/RegistryKey;
 */
package net.minecraft.world.gen.feature;

import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.minecraft.world.gen.YOffset;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.EndConfiguredFeatures;
import net.minecraft.world.gen.feature.PlacedFeature;
import net.minecraft.world.gen.feature.PlacedFeatures;
import net.minecraft.world.gen.placementmodifier.BiomePlacementModifier;
import net.minecraft.world.gen.placementmodifier.CountPlacementModifier;
import net.minecraft.world.gen.placementmodifier.FixedPlacementModifier;
import net.minecraft.world.gen.placementmodifier.HeightRangePlacementModifier;
import net.minecraft.world.gen.placementmodifier.RandomOffsetPlacementModifier;
import net.minecraft.world.gen.placementmodifier.RarityFilterPlacementModifier;
import net.minecraft.world.gen.placementmodifier.SquarePlacementModifier;

public class EndPlacedFeatures {
    public static final RegistryKey<PlacedFeature> END_PLATFORM = PlacedFeatures.of("end_platform");
    public static final RegistryKey<PlacedFeature> END_SPIKE = PlacedFeatures.of("end_spike");
    public static final RegistryKey<PlacedFeature> END_GATEWAY_RETURN = PlacedFeatures.of("end_gateway_return");
    public static final RegistryKey<PlacedFeature> CHORUS_PLANT = PlacedFeatures.of("chorus_plant");
    public static final RegistryKey<PlacedFeature> END_ISLAND_DECORATED = PlacedFeatures.of("end_island_decorated");

    public static void bootstrap(Registerable<PlacedFeature> featureRegisterable) {
        RegistryEntryLookup<ConfiguredFeature<?, ?>> lv = featureRegisterable.getRegistryLookup(RegistryKeys.CONFIGURED_FEATURE);
        RegistryEntry.Reference<ConfiguredFeature<?, ?>> lv2 = lv.getOrThrow(EndConfiguredFeatures.END_PLATFORM);
        RegistryEntry.Reference<ConfiguredFeature<?, ?>> lv3 = lv.getOrThrow(EndConfiguredFeatures.END_SPIKE);
        RegistryEntry.Reference<ConfiguredFeature<?, ?>> lv4 = lv.getOrThrow(EndConfiguredFeatures.END_GATEWAY_RETURN);
        RegistryEntry.Reference<ConfiguredFeature<?, ?>> lv5 = lv.getOrThrow(EndConfiguredFeatures.CHORUS_PLANT);
        RegistryEntry.Reference<ConfiguredFeature<?, ?>> lv6 = lv.getOrThrow(EndConfiguredFeatures.END_ISLAND);
        PlacedFeatures.register(featureRegisterable, END_PLATFORM, lv2, FixedPlacementModifier.of(ServerWorld.END_SPAWN_POS.down()), BiomePlacementModifier.of());
        PlacedFeatures.register(featureRegisterable, END_SPIKE, lv3, BiomePlacementModifier.of());
        PlacedFeatures.register(featureRegisterable, END_GATEWAY_RETURN, lv4, RarityFilterPlacementModifier.of(700), SquarePlacementModifier.of(), PlacedFeatures.MOTION_BLOCKING_HEIGHTMAP, RandomOffsetPlacementModifier.vertically(UniformIntProvider.create(3, 9)), BiomePlacementModifier.of());
        PlacedFeatures.register(featureRegisterable, CHORUS_PLANT, lv5, CountPlacementModifier.of(UniformIntProvider.create(0, 4)), SquarePlacementModifier.of(), PlacedFeatures.MOTION_BLOCKING_HEIGHTMAP, BiomePlacementModifier.of());
        PlacedFeatures.register(featureRegisterable, END_ISLAND_DECORATED, lv6, RarityFilterPlacementModifier.of(14), PlacedFeatures.createCountExtraModifier(1, 0.25f, 1), SquarePlacementModifier.of(), HeightRangePlacementModifier.uniform(YOffset.fixed(55), YOffset.fixed(70)), BiomePlacementModifier.of());
    }
}

