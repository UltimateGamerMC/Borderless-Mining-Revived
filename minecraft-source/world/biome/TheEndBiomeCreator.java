/*
 * External method calls:
 *   Lnet/minecraft/world/gen/feature/DefaultBiomeFeatures;addEndMobs(Lnet/minecraft/world/biome/SpawnSettings$Builder;)V
 *   Lnet/minecraft/world/biome/Biome$Builder;precipitation(Z)Lnet/minecraft/world/biome/Biome$Builder;
 *   Lnet/minecraft/world/biome/Biome$Builder;temperature(F)Lnet/minecraft/world/biome/Biome$Builder;
 *   Lnet/minecraft/world/biome/Biome$Builder;downfall(F)Lnet/minecraft/world/biome/Biome$Builder;
 *   Lnet/minecraft/world/biome/BiomeEffects$Builder;waterColor(I)Lnet/minecraft/world/biome/BiomeEffects$Builder;
 *   Lnet/minecraft/world/biome/BiomeEffects$Builder;build()Lnet/minecraft/world/biome/BiomeEffects;
 *   Lnet/minecraft/world/biome/Biome$Builder;effects(Lnet/minecraft/world/biome/BiomeEffects;)Lnet/minecraft/world/biome/Biome$Builder;
 *   Lnet/minecraft/world/biome/SpawnSettings$Builder;build()Lnet/minecraft/world/biome/SpawnSettings;
 *   Lnet/minecraft/world/biome/Biome$Builder;spawnSettings(Lnet/minecraft/world/biome/SpawnSettings;)Lnet/minecraft/world/biome/Biome$Builder;
 *   Lnet/minecraft/world/biome/GenerationSettings$LookupBackedBuilder;build()Lnet/minecraft/world/biome/GenerationSettings;
 *   Lnet/minecraft/world/biome/Biome$Builder;generationSettings(Lnet/minecraft/world/biome/GenerationSettings;)Lnet/minecraft/world/biome/Biome$Builder;
 *   Lnet/minecraft/world/biome/Biome$Builder;build()Lnet/minecraft/world/biome/Biome;
 *   Lnet/minecraft/world/biome/GenerationSettings$LookupBackedBuilder;feature(Lnet/minecraft/world/gen/GenerationStep$Feature;Lnet/minecraft/registry/RegistryKey;)Lnet/minecraft/world/biome/GenerationSettings$LookupBackedBuilder;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/world/biome/TheEndBiomeCreator;createEndBiome(Lnet/minecraft/world/biome/GenerationSettings$LookupBackedBuilder;)Lnet/minecraft/world/biome/Biome;
 */
package net.minecraft.world.biome;

import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeEffects;
import net.minecraft.world.biome.GenerationSettings;
import net.minecraft.world.biome.SpawnSettings;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.carver.ConfiguredCarver;
import net.minecraft.world.gen.feature.DefaultBiomeFeatures;
import net.minecraft.world.gen.feature.EndPlacedFeatures;
import net.minecraft.world.gen.feature.PlacedFeature;

public class TheEndBiomeCreator {
    private static Biome createEndBiome(GenerationSettings.LookupBackedBuilder builder) {
        SpawnSettings.Builder lv = new SpawnSettings.Builder();
        DefaultBiomeFeatures.addEndMobs(lv);
        return new Biome.Builder().precipitation(false).temperature(0.5f).downfall(0.5f).effects(new BiomeEffects.Builder().waterColor(4159204).build()).spawnSettings(lv.build()).generationSettings(builder.build()).build();
    }

    public static Biome createEndBarrens(RegistryEntryLookup<PlacedFeature> featureLookup, RegistryEntryLookup<ConfiguredCarver<?>> carverLookup) {
        GenerationSettings.LookupBackedBuilder lv = new GenerationSettings.LookupBackedBuilder(featureLookup, carverLookup);
        return TheEndBiomeCreator.createEndBiome(lv);
    }

    public static Biome createTheEnd(RegistryEntryLookup<PlacedFeature> featureLookup, RegistryEntryLookup<ConfiguredCarver<?>> carverLookup) {
        GenerationSettings.LookupBackedBuilder lv = new GenerationSettings.LookupBackedBuilder(featureLookup, carverLookup).feature(GenerationStep.Feature.SURFACE_STRUCTURES, EndPlacedFeatures.END_SPIKE).feature(GenerationStep.Feature.TOP_LAYER_MODIFICATION, EndPlacedFeatures.END_PLATFORM);
        return TheEndBiomeCreator.createEndBiome(lv);
    }

    public static Biome createEndMidlands(RegistryEntryLookup<PlacedFeature> featureLookup, RegistryEntryLookup<ConfiguredCarver<?>> carverLookup) {
        GenerationSettings.LookupBackedBuilder lv = new GenerationSettings.LookupBackedBuilder(featureLookup, carverLookup);
        return TheEndBiomeCreator.createEndBiome(lv);
    }

    public static Biome createEndHighlands(RegistryEntryLookup<PlacedFeature> featureLookup, RegistryEntryLookup<ConfiguredCarver<?>> carverLookup) {
        GenerationSettings.LookupBackedBuilder lv = new GenerationSettings.LookupBackedBuilder(featureLookup, carverLookup).feature(GenerationStep.Feature.SURFACE_STRUCTURES, EndPlacedFeatures.END_GATEWAY_RETURN).feature(GenerationStep.Feature.VEGETAL_DECORATION, EndPlacedFeatures.CHORUS_PLANT);
        return TheEndBiomeCreator.createEndBiome(lv);
    }

    public static Biome createSmallEndIslands(RegistryEntryLookup<PlacedFeature> featureLookup, RegistryEntryLookup<ConfiguredCarver<?>> carverLookup) {
        GenerationSettings.LookupBackedBuilder lv = new GenerationSettings.LookupBackedBuilder(featureLookup, carverLookup).feature(GenerationStep.Feature.RAW_GENERATION, EndPlacedFeatures.END_ISLAND_DECORATED);
        return TheEndBiomeCreator.createEndBiome(lv);
    }
}

