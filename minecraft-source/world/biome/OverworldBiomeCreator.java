/*
 * External method calls:
 *   Lnet/minecraft/world/biome/Biome$Builder;precipitation(Z)Lnet/minecraft/world/biome/Biome$Builder;
 *   Lnet/minecraft/world/biome/Biome$Builder;temperature(F)Lnet/minecraft/world/biome/Biome$Builder;
 *   Lnet/minecraft/world/biome/Biome$Builder;downfall(F)Lnet/minecraft/world/biome/Biome$Builder;
 *   Lnet/minecraft/world/biome/BiomeEffects$Builder;waterColor(I)Lnet/minecraft/world/biome/BiomeEffects$Builder;
 *   Lnet/minecraft/world/biome/BiomeEffects$Builder;build()Lnet/minecraft/world/biome/BiomeEffects;
 *   Lnet/minecraft/world/biome/Biome$Builder;effects(Lnet/minecraft/world/biome/BiomeEffects;)Lnet/minecraft/world/biome/Biome$Builder;
 *   Lnet/minecraft/world/gen/feature/DefaultBiomeFeatures;addLandCarvers(Lnet/minecraft/world/biome/GenerationSettings$LookupBackedBuilder;)V
 *   Lnet/minecraft/world/gen/feature/DefaultBiomeFeatures;addAmethystGeodes(Lnet/minecraft/world/biome/GenerationSettings$LookupBackedBuilder;)V
 *   Lnet/minecraft/world/gen/feature/DefaultBiomeFeatures;addDungeons(Lnet/minecraft/world/biome/GenerationSettings$LookupBackedBuilder;)V
 *   Lnet/minecraft/world/gen/feature/DefaultBiomeFeatures;addMineables(Lnet/minecraft/world/biome/GenerationSettings$LookupBackedBuilder;)V
 *   Lnet/minecraft/world/gen/feature/DefaultBiomeFeatures;addSprings(Lnet/minecraft/world/biome/GenerationSettings$LookupBackedBuilder;)V
 *   Lnet/minecraft/world/gen/feature/DefaultBiomeFeatures;addFrozenTopLayer(Lnet/minecraft/world/biome/GenerationSettings$LookupBackedBuilder;)V
 *   Lnet/minecraft/world/gen/feature/DefaultBiomeFeatures;addFarmAnimals(Lnet/minecraft/world/biome/SpawnSettings$Builder;)V
 *   Lnet/minecraft/world/biome/SpawnSettings$Builder;spawn(Lnet/minecraft/entity/SpawnGroup;ILnet/minecraft/world/biome/SpawnSettings$SpawnEntry;)Lnet/minecraft/world/biome/SpawnSettings$Builder;
 *   Lnet/minecraft/world/gen/feature/DefaultBiomeFeatures;addCaveAndMonsters(Lnet/minecraft/world/biome/SpawnSettings$Builder;)V
 *   Lnet/minecraft/world/gen/feature/DefaultBiomeFeatures;addCaveMobs(Lnet/minecraft/world/biome/SpawnSettings$Builder;)V
 *   Lnet/minecraft/world/gen/feature/DefaultBiomeFeatures;addMonsters(Lnet/minecraft/world/biome/SpawnSettings$Builder;IIIIZ)V
 *   Lnet/minecraft/world/gen/feature/DefaultBiomeFeatures;addMossyRocks(Lnet/minecraft/world/biome/GenerationSettings$LookupBackedBuilder;)V
 *   Lnet/minecraft/world/gen/feature/DefaultBiomeFeatures;addLargeFerns(Lnet/minecraft/world/biome/GenerationSettings$LookupBackedBuilder;)V
 *   Lnet/minecraft/world/gen/feature/DefaultBiomeFeatures;addDefaultOres(Lnet/minecraft/world/biome/GenerationSettings$LookupBackedBuilder;)V
 *   Lnet/minecraft/world/gen/feature/DefaultBiomeFeatures;addDefaultDisks(Lnet/minecraft/world/biome/GenerationSettings$LookupBackedBuilder;)V
 *   Lnet/minecraft/world/biome/GenerationSettings$LookupBackedBuilder;feature(Lnet/minecraft/world/gen/GenerationStep$Feature;Lnet/minecraft/registry/RegistryKey;)Lnet/minecraft/world/biome/GenerationSettings$LookupBackedBuilder;
 *   Lnet/minecraft/world/gen/feature/DefaultBiomeFeatures;addDefaultFlowers(Lnet/minecraft/world/biome/GenerationSettings$LookupBackedBuilder;)V
 *   Lnet/minecraft/world/gen/feature/DefaultBiomeFeatures;addGiantTaigaGrass(Lnet/minecraft/world/biome/GenerationSettings$LookupBackedBuilder;)V
 *   Lnet/minecraft/world/gen/feature/DefaultBiomeFeatures;addDefaultMushrooms(Lnet/minecraft/world/biome/GenerationSettings$LookupBackedBuilder;)V
 *   Lnet/minecraft/world/gen/feature/DefaultBiomeFeatures;addDefaultVegetation(Lnet/minecraft/world/biome/GenerationSettings$LookupBackedBuilder;Z)V
 *   Lnet/minecraft/world/gen/feature/DefaultBiomeFeatures;addSweetBerryBushes(Lnet/minecraft/world/biome/GenerationSettings$LookupBackedBuilder;)V
 *   Lnet/minecraft/world/biome/SpawnSettings$Builder;build()Lnet/minecraft/world/biome/SpawnSettings;
 *   Lnet/minecraft/world/biome/Biome$Builder;spawnSettings(Lnet/minecraft/world/biome/SpawnSettings;)Lnet/minecraft/world/biome/Biome$Builder;
 *   Lnet/minecraft/world/biome/GenerationSettings$LookupBackedBuilder;build()Lnet/minecraft/world/biome/GenerationSettings;
 *   Lnet/minecraft/world/biome/Biome$Builder;generationSettings(Lnet/minecraft/world/biome/GenerationSettings;)Lnet/minecraft/world/biome/Biome$Builder;
 *   Lnet/minecraft/world/biome/Biome$Builder;build()Lnet/minecraft/world/biome/Biome;
 *   Lnet/minecraft/world/gen/feature/DefaultBiomeFeatures;addJungleMobs(Lnet/minecraft/world/biome/SpawnSettings$Builder;)V
 *   Lnet/minecraft/world/gen/feature/DefaultBiomeFeatures;addBambooJungleTrees(Lnet/minecraft/world/biome/GenerationSettings$LookupBackedBuilder;)V
 *   Lnet/minecraft/world/gen/feature/DefaultBiomeFeatures;addBamboo(Lnet/minecraft/world/biome/GenerationSettings$LookupBackedBuilder;)V
 *   Lnet/minecraft/world/gen/feature/DefaultBiomeFeatures;addSparseJungleTrees(Lnet/minecraft/world/biome/GenerationSettings$LookupBackedBuilder;)V
 *   Lnet/minecraft/world/gen/feature/DefaultBiomeFeatures;addJungleTrees(Lnet/minecraft/world/biome/GenerationSettings$LookupBackedBuilder;)V
 *   Lnet/minecraft/world/gen/feature/DefaultBiomeFeatures;addExtraDefaultFlowers(Lnet/minecraft/world/biome/GenerationSettings$LookupBackedBuilder;)V
 *   Lnet/minecraft/world/gen/feature/DefaultBiomeFeatures;addJungleGrass(Lnet/minecraft/world/biome/GenerationSettings$LookupBackedBuilder;)V
 *   Lnet/minecraft/world/gen/feature/DefaultBiomeFeatures;addVines(Lnet/minecraft/world/biome/GenerationSettings$LookupBackedBuilder;)V
 *   Lnet/minecraft/world/gen/feature/DefaultBiomeFeatures;addSparseMelons(Lnet/minecraft/world/biome/GenerationSettings$LookupBackedBuilder;)V
 *   Lnet/minecraft/world/gen/feature/DefaultBiomeFeatures;addMelons(Lnet/minecraft/world/biome/GenerationSettings$LookupBackedBuilder;)V
 *   Lnet/minecraft/world/gen/feature/DefaultBiomeFeatures;addWindsweptForestTrees(Lnet/minecraft/world/biome/GenerationSettings$LookupBackedBuilder;)V
 *   Lnet/minecraft/world/gen/feature/DefaultBiomeFeatures;addWindsweptHillsTrees(Lnet/minecraft/world/biome/GenerationSettings$LookupBackedBuilder;)V
 *   Lnet/minecraft/world/gen/feature/DefaultBiomeFeatures;addBushes(Lnet/minecraft/world/biome/GenerationSettings$LookupBackedBuilder;)V
 *   Lnet/minecraft/world/gen/feature/DefaultBiomeFeatures;addDefaultGrass(Lnet/minecraft/world/biome/GenerationSettings$LookupBackedBuilder;)V
 *   Lnet/minecraft/world/gen/feature/DefaultBiomeFeatures;addEmeraldOre(Lnet/minecraft/world/biome/GenerationSettings$LookupBackedBuilder;)V
 *   Lnet/minecraft/world/gen/feature/DefaultBiomeFeatures;addInfestedStone(Lnet/minecraft/world/biome/GenerationSettings$LookupBackedBuilder;)V
 *   Lnet/minecraft/world/gen/feature/DefaultBiomeFeatures;addDesertMobs(Lnet/minecraft/world/biome/SpawnSettings$Builder;)V
 *   Lnet/minecraft/world/gen/feature/DefaultBiomeFeatures;addFossils(Lnet/minecraft/world/biome/GenerationSettings$LookupBackedBuilder;)V
 *   Lnet/minecraft/world/gen/feature/DefaultBiomeFeatures;addDesertDryVegetation(Lnet/minecraft/world/biome/GenerationSettings$LookupBackedBuilder;)V
 *   Lnet/minecraft/world/gen/feature/DefaultBiomeFeatures;addDesertVegetation(Lnet/minecraft/world/biome/GenerationSettings$LookupBackedBuilder;)V
 *   Lnet/minecraft/world/gen/feature/DefaultBiomeFeatures;addDesertFeatures(Lnet/minecraft/world/biome/GenerationSettings$LookupBackedBuilder;)V
 *   Lnet/minecraft/world/biome/SpawnSettings$Builder;creatureSpawnProbability(F)Lnet/minecraft/world/biome/SpawnSettings$Builder;
 *   Lnet/minecraft/world/gen/feature/DefaultBiomeFeatures;addSnowyMobs(Lnet/minecraft/world/biome/SpawnSettings$Builder;Z)V
 *   Lnet/minecraft/world/gen/feature/DefaultBiomeFeatures;addPlainsMobs(Lnet/minecraft/world/biome/SpawnSettings$Builder;)V
 *   Lnet/minecraft/world/gen/feature/DefaultBiomeFeatures;addPlainsTallGrass(Lnet/minecraft/world/biome/GenerationSettings$LookupBackedBuilder;)V
 *   Lnet/minecraft/world/gen/feature/DefaultBiomeFeatures;addSnowySpruceTrees(Lnet/minecraft/world/biome/GenerationSettings$LookupBackedBuilder;)V
 *   Lnet/minecraft/world/gen/feature/DefaultBiomeFeatures;addPlainsFeatures(Lnet/minecraft/world/biome/GenerationSettings$LookupBackedBuilder;)V
 *   Lnet/minecraft/world/gen/feature/DefaultBiomeFeatures;addMushroomMobs(Lnet/minecraft/world/biome/SpawnSettings$Builder;)V
 *   Lnet/minecraft/world/gen/feature/DefaultBiomeFeatures;addMushroomFieldsFeatures(Lnet/minecraft/world/biome/GenerationSettings$LookupBackedBuilder;)V
 *   Lnet/minecraft/world/gen/feature/DefaultBiomeFeatures;addDefaultVegetationNearWater(Lnet/minecraft/world/biome/GenerationSettings$LookupBackedBuilder;)V
 *   Lnet/minecraft/world/gen/feature/DefaultBiomeFeatures;addSavannaTallGrass(Lnet/minecraft/world/biome/GenerationSettings$LookupBackedBuilder;)V
 *   Lnet/minecraft/world/gen/feature/DefaultBiomeFeatures;addExtraSavannaTrees(Lnet/minecraft/world/biome/GenerationSettings$LookupBackedBuilder;)V
 *   Lnet/minecraft/world/gen/feature/DefaultBiomeFeatures;addWindsweptSavannaGrass(Lnet/minecraft/world/biome/GenerationSettings$LookupBackedBuilder;)V
 *   Lnet/minecraft/world/gen/feature/DefaultBiomeFeatures;addSavannaTrees(Lnet/minecraft/world/biome/GenerationSettings$LookupBackedBuilder;)V
 *   Lnet/minecraft/world/gen/feature/DefaultBiomeFeatures;addSavannaGrass(Lnet/minecraft/world/biome/GenerationSettings$LookupBackedBuilder;)V
 *   Lnet/minecraft/world/gen/feature/DefaultBiomeFeatures;addCaveAndMonstersAndZombieHorse(Lnet/minecraft/world/biome/SpawnSettings$Builder;)V
 *   Lnet/minecraft/world/gen/feature/DefaultBiomeFeatures;addExtraGoldOre(Lnet/minecraft/world/biome/GenerationSettings$LookupBackedBuilder;)V
 *   Lnet/minecraft/world/gen/feature/DefaultBiomeFeatures;addBadlandsPlateauTrees(Lnet/minecraft/world/biome/GenerationSettings$LookupBackedBuilder;)V
 *   Lnet/minecraft/world/gen/feature/DefaultBiomeFeatures;addBadlandsGrass(Lnet/minecraft/world/biome/GenerationSettings$LookupBackedBuilder;)V
 *   Lnet/minecraft/world/gen/feature/DefaultBiomeFeatures;addBadlandsVegetation(Lnet/minecraft/world/biome/GenerationSettings$LookupBackedBuilder;)V
 *   Lnet/minecraft/world/biome/BiomeEffects$Builder;foliageColor(I)Lnet/minecraft/world/biome/BiomeEffects$Builder;
 *   Lnet/minecraft/world/biome/BiomeEffects$Builder;grassColor(I)Lnet/minecraft/world/biome/BiomeEffects$Builder;
 *   Lnet/minecraft/world/attribute/BackgroundMusic;withUnderwater(Lnet/minecraft/sound/MusicSound;)Lnet/minecraft/world/attribute/BackgroundMusic;
 *   Lnet/minecraft/world/gen/feature/DefaultBiomeFeatures;addWaterBiomeOakTrees(Lnet/minecraft/world/biome/GenerationSettings$LookupBackedBuilder;)V
 *   Lnet/minecraft/world/gen/feature/DefaultBiomeFeatures;addOceanMobs(Lnet/minecraft/world/biome/SpawnSettings$Builder;III)V
 *   Lnet/minecraft/world/gen/feature/DefaultBiomeFeatures;addKelp(Lnet/minecraft/world/biome/GenerationSettings$LookupBackedBuilder;)V
 *   Lnet/minecraft/world/gen/feature/DefaultBiomeFeatures;addLessKelp(Lnet/minecraft/world/biome/GenerationSettings$LookupBackedBuilder;)V
 *   Lnet/minecraft/world/gen/feature/DefaultBiomeFeatures;addWarmOceanMobs(Lnet/minecraft/world/biome/SpawnSettings$Builder;II)V
 *   Lnet/minecraft/world/gen/feature/DefaultBiomeFeatures;addIcebergs(Lnet/minecraft/world/biome/GenerationSettings$LookupBackedBuilder;)V
 *   Lnet/minecraft/world/gen/feature/DefaultBiomeFeatures;addBlueIce(Lnet/minecraft/world/biome/GenerationSettings$LookupBackedBuilder;)V
 *   Lnet/minecraft/world/biome/Biome$Builder;temperatureModifier(Lnet/minecraft/world/biome/Biome$TemperatureModifier;)Lnet/minecraft/world/biome/Biome$Builder;
 *   Lnet/minecraft/world/gen/feature/DefaultBiomeFeatures;addForestFlowers(Lnet/minecraft/world/biome/GenerationSettings$LookupBackedBuilder;)V
 *   Lnet/minecraft/world/gen/feature/DefaultBiomeFeatures;addBirchForestWildflowers(Lnet/minecraft/world/biome/GenerationSettings$LookupBackedBuilder;)V
 *   Lnet/minecraft/world/gen/feature/DefaultBiomeFeatures;addTallBirchTrees(Lnet/minecraft/world/biome/GenerationSettings$LookupBackedBuilder;)V
 *   Lnet/minecraft/world/gen/feature/DefaultBiomeFeatures;addBirchTrees(Lnet/minecraft/world/biome/GenerationSettings$LookupBackedBuilder;)V
 *   Lnet/minecraft/world/gen/feature/DefaultBiomeFeatures;addForestTrees(Lnet/minecraft/world/biome/GenerationSettings$LookupBackedBuilder;)V
 *   Lnet/minecraft/world/gen/feature/DefaultBiomeFeatures;addForestGrass(Lnet/minecraft/world/biome/GenerationSettings$LookupBackedBuilder;)V
 *   Lnet/minecraft/world/gen/feature/DefaultBiomeFeatures;addTaigaTrees(Lnet/minecraft/world/biome/GenerationSettings$LookupBackedBuilder;)V
 *   Lnet/minecraft/world/gen/feature/DefaultBiomeFeatures;addTaigaGrass(Lnet/minecraft/world/biome/GenerationSettings$LookupBackedBuilder;)V
 *   Lnet/minecraft/world/gen/feature/DefaultBiomeFeatures;addSweetBerryBushesSnowy(Lnet/minecraft/world/biome/GenerationSettings$LookupBackedBuilder;)V
 *   Lnet/minecraft/world/gen/feature/DefaultBiomeFeatures;addLeafLitter(Lnet/minecraft/world/biome/GenerationSettings$LookupBackedBuilder;)V
 *   Lnet/minecraft/world/attribute/EnvironmentAttributeMap;builder()Lnet/minecraft/world/attribute/EnvironmentAttributeMap$Builder;
 *   Lnet/minecraft/world/attribute/EnvironmentAttributeMap$Builder;with(Lnet/minecraft/world/attribute/EnvironmentAttribute;Ljava/lang/Object;)Lnet/minecraft/world/attribute/EnvironmentAttributeMap$Builder;
 *   Lnet/minecraft/world/attribute/EnvironmentAttributeMap$Builder;build()Lnet/minecraft/world/attribute/EnvironmentAttributeMap;
 *   Lnet/minecraft/world/biome/Biome$Builder;addEnvironmentAttributes(Lnet/minecraft/world/attribute/EnvironmentAttributeMap;)Lnet/minecraft/world/biome/Biome$Builder;
 *   Lnet/minecraft/world/biome/BiomeEffects$Builder;dryFoliageColor(I)Lnet/minecraft/world/biome/BiomeEffects$Builder;
 *   Lnet/minecraft/world/biome/BiomeEffects$Builder;grassColorModifier(Lnet/minecraft/world/biome/BiomeEffects$GrassColorModifier;)Lnet/minecraft/world/biome/BiomeEffects$Builder;
 *   Lnet/minecraft/world/gen/feature/DefaultBiomeFeatures;addSwampMobs(Lnet/minecraft/world/biome/SpawnSettings$Builder;I)V
 *   Lnet/minecraft/world/gen/feature/DefaultBiomeFeatures;addClayDisk(Lnet/minecraft/world/biome/GenerationSettings$LookupBackedBuilder;)V
 *   Lnet/minecraft/world/gen/feature/DefaultBiomeFeatures;addSwampFeatures(Lnet/minecraft/world/biome/GenerationSettings$LookupBackedBuilder;)V
 *   Lnet/minecraft/world/gen/feature/DefaultBiomeFeatures;addSwampVegetation(Lnet/minecraft/world/biome/GenerationSettings$LookupBackedBuilder;)V
 *   Lnet/minecraft/world/gen/feature/DefaultBiomeFeatures;addGrassAndClayDisks(Lnet/minecraft/world/biome/GenerationSettings$LookupBackedBuilder;)V
 *   Lnet/minecraft/world/gen/feature/DefaultBiomeFeatures;addMangroveSwampFeatures(Lnet/minecraft/world/biome/GenerationSettings$LookupBackedBuilder;)V
 *   Lnet/minecraft/world/gen/feature/DefaultBiomeFeatures;addMangroveSwampAquaticFeatures(Lnet/minecraft/world/biome/GenerationSettings$LookupBackedBuilder;)V
 *   Lnet/minecraft/world/gen/feature/DefaultBiomeFeatures;addCherryGroveFeatures(Lnet/minecraft/world/biome/GenerationSettings$LookupBackedBuilder;)V
 *   Lnet/minecraft/world/gen/feature/DefaultBiomeFeatures;addMeadowFlowers(Lnet/minecraft/world/biome/GenerationSettings$LookupBackedBuilder;)V
 *   Lnet/minecraft/world/gen/feature/DefaultBiomeFeatures;addFrozenLavaSpring(Lnet/minecraft/world/biome/GenerationSettings$LookupBackedBuilder;)V
 *   Lnet/minecraft/world/gen/feature/DefaultBiomeFeatures;addGroveTrees(Lnet/minecraft/world/biome/GenerationSettings$LookupBackedBuilder;)V
 *   Lnet/minecraft/world/gen/feature/DefaultBiomeFeatures;addClayOre(Lnet/minecraft/world/biome/GenerationSettings$LookupBackedBuilder;)V
 *   Lnet/minecraft/world/gen/feature/DefaultBiomeFeatures;addLushCavesDecoration(Lnet/minecraft/world/biome/GenerationSettings$LookupBackedBuilder;)V
 *   Lnet/minecraft/world/gen/feature/DefaultBiomeFeatures;addDripstoneCaveMobs(Lnet/minecraft/world/biome/SpawnSettings$Builder;)V
 *   Lnet/minecraft/world/gen/feature/DefaultBiomeFeatures;addDefaultOres(Lnet/minecraft/world/biome/GenerationSettings$LookupBackedBuilder;Z)V
 *   Lnet/minecraft/world/gen/feature/DefaultBiomeFeatures;addDripstone(Lnet/minecraft/world/biome/GenerationSettings$LookupBackedBuilder;)V
 *   Lnet/minecraft/world/biome/GenerationSettings$LookupBackedBuilder;carver(Lnet/minecraft/registry/RegistryKey;)Lnet/minecraft/world/biome/GenerationSettings$LookupBackedBuilder;
 *   Lnet/minecraft/world/gen/feature/DefaultBiomeFeatures;addSculk(Lnet/minecraft/world/biome/GenerationSettings$LookupBackedBuilder;)V
 *
 * Internal private/static methods:
 *   Lnet/minecraft/world/biome/OverworldBiomeCreator;addBasicFeatures(Lnet/minecraft/world/biome/GenerationSettings$LookupBackedBuilder;)V
 *   Lnet/minecraft/world/biome/OverworldBiomeCreator;biome(FF)Lnet/minecraft/world/biome/Biome$Builder;
 *   Lnet/minecraft/world/biome/OverworldBiomeCreator;createJungleFeatures(Lnet/minecraft/registry/RegistryEntryLookup;Lnet/minecraft/registry/RegistryEntryLookup;FZZZ)Lnet/minecraft/world/biome/Biome$Builder;
 *   Lnet/minecraft/world/biome/OverworldBiomeCreator;createOceanGenerationSettings(Lnet/minecraft/registry/RegistryEntryLookup;Lnet/minecraft/registry/RegistryEntryLookup;)Lnet/minecraft/world/biome/GenerationSettings$LookupBackedBuilder;
 *   Lnet/minecraft/world/biome/OverworldBiomeCreator;createOcean()Lnet/minecraft/world/biome/Biome$Builder;
 *   Lnet/minecraft/world/biome/OverworldBiomeCreator;createColdPeaks(Lnet/minecraft/registry/RegistryEntryLookup;Lnet/minecraft/registry/RegistryEntryLookup;)Lnet/minecraft/world/biome/Biome$Builder;
 */
package net.minecraft.world.biome;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.sound.MusicType;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.attribute.BackgroundMusic;
import net.minecraft.world.attribute.EnvironmentAttributeMap;
import net.minecraft.world.attribute.EnvironmentAttributes;
import net.minecraft.world.attribute.FloatModifier;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeEffects;
import net.minecraft.world.biome.GenerationSettings;
import net.minecraft.world.biome.SpawnSettings;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.carver.ConfiguredCarver;
import net.minecraft.world.gen.carver.ConfiguredCarvers;
import net.minecraft.world.gen.feature.DefaultBiomeFeatures;
import net.minecraft.world.gen.feature.MiscPlacedFeatures;
import net.minecraft.world.gen.feature.OceanPlacedFeatures;
import net.minecraft.world.gen.feature.PlacedFeature;
import net.minecraft.world.gen.feature.VegetationPlacedFeatures;

public class OverworldBiomeCreator {
    protected static final int DEFAULT_WATER_COLOR = 4159204;
    private static final int DEFAULT_DRY_FOLIAGE_COLOR = 8082228;
    public static final int SWAMP_SKELETON_WEIGHT = 70;

    public static int getSkyColor(float temperature) {
        float g = temperature;
        g /= 3.0f;
        g = MathHelper.clamp(g, -1.0f, 1.0f);
        return ColorHelper.fullAlpha(MathHelper.hsvToRgb(0.62222224f - g * 0.05f, 0.5f + g * 0.1f, 1.0f));
    }

    private static Biome.Builder biome(float temperature, float downfall) {
        return new Biome.Builder().precipitation(true).temperature(temperature).downfall(downfall).setEnvironmentAttribute(EnvironmentAttributes.SKY_COLOR_VISUAL, OverworldBiomeCreator.getSkyColor(temperature)).effects(new BiomeEffects.Builder().waterColor(4159204).build());
    }

    private static void addBasicFeatures(GenerationSettings.LookupBackedBuilder generationSettings) {
        DefaultBiomeFeatures.addLandCarvers(generationSettings);
        DefaultBiomeFeatures.addAmethystGeodes(generationSettings);
        DefaultBiomeFeatures.addDungeons(generationSettings);
        DefaultBiomeFeatures.addMineables(generationSettings);
        DefaultBiomeFeatures.addSprings(generationSettings);
        DefaultBiomeFeatures.addFrozenTopLayer(generationSettings);
    }

    public static Biome createOldGrowthTaiga(RegistryEntryLookup<PlacedFeature> featureLookup, RegistryEntryLookup<ConfiguredCarver<?>> carverLookup, boolean spruce) {
        SpawnSettings.Builder lv = new SpawnSettings.Builder();
        DefaultBiomeFeatures.addFarmAnimals(lv);
        lv.spawn(SpawnGroup.CREATURE, 8, new SpawnSettings.SpawnEntry(EntityType.WOLF, 4, 4));
        lv.spawn(SpawnGroup.CREATURE, 4, new SpawnSettings.SpawnEntry(EntityType.RABBIT, 2, 3));
        lv.spawn(SpawnGroup.CREATURE, 8, new SpawnSettings.SpawnEntry(EntityType.FOX, 2, 4));
        if (spruce) {
            DefaultBiomeFeatures.addCaveAndMonsters(lv);
        } else {
            DefaultBiomeFeatures.addCaveMobs(lv);
            DefaultBiomeFeatures.addMonsters(lv, 100, 25, 0, 100, false);
        }
        GenerationSettings.LookupBackedBuilder lv2 = new GenerationSettings.LookupBackedBuilder(featureLookup, carverLookup);
        OverworldBiomeCreator.addBasicFeatures(lv2);
        DefaultBiomeFeatures.addMossyRocks(lv2);
        DefaultBiomeFeatures.addLargeFerns(lv2);
        DefaultBiomeFeatures.addDefaultOres(lv2);
        DefaultBiomeFeatures.addDefaultDisks(lv2);
        lv2.feature(GenerationStep.Feature.VEGETAL_DECORATION, spruce ? VegetationPlacedFeatures.TREES_OLD_GROWTH_SPRUCE_TAIGA : VegetationPlacedFeatures.TREES_OLD_GROWTH_PINE_TAIGA);
        DefaultBiomeFeatures.addDefaultFlowers(lv2);
        DefaultBiomeFeatures.addGiantTaigaGrass(lv2);
        DefaultBiomeFeatures.addDefaultMushrooms(lv2);
        DefaultBiomeFeatures.addDefaultVegetation(lv2, true);
        DefaultBiomeFeatures.addSweetBerryBushes(lv2);
        return OverworldBiomeCreator.biome(spruce ? 0.25f : 0.3f, 0.8f).setEnvironmentAttribute(EnvironmentAttributes.BACKGROUND_MUSIC_AUDIO, new BackgroundMusic(SoundEvents.MUSIC_OVERWORLD_OLD_GROWTH_TAIGA)).spawnSettings(lv.build()).generationSettings(lv2.build()).build();
    }

    public static Biome createSparseJungle(RegistryEntryLookup<PlacedFeature> featureLookup, RegistryEntryLookup<ConfiguredCarver<?>> carverLookup) {
        SpawnSettings.Builder lv = new SpawnSettings.Builder();
        DefaultBiomeFeatures.addJungleMobs(lv);
        lv.spawn(SpawnGroup.CREATURE, 8, new SpawnSettings.SpawnEntry(EntityType.WOLF, 2, 4));
        return OverworldBiomeCreator.createJungleFeatures(featureLookup, carverLookup, 0.8f, false, true, false).spawnSettings(lv.build()).setEnvironmentAttribute(EnvironmentAttributes.BACKGROUND_MUSIC_AUDIO, new BackgroundMusic(SoundEvents.MUSIC_OVERWORLD_SPARSE_JUNGLE)).build();
    }

    public static Biome createJungle(RegistryEntryLookup<PlacedFeature> featureLookup, RegistryEntryLookup<ConfiguredCarver<?>> carverLookup) {
        SpawnSettings.Builder lv = new SpawnSettings.Builder();
        DefaultBiomeFeatures.addJungleMobs(lv);
        lv.spawn(SpawnGroup.CREATURE, 40, new SpawnSettings.SpawnEntry(EntityType.PARROT, 1, 2)).spawn(SpawnGroup.MONSTER, 2, new SpawnSettings.SpawnEntry(EntityType.OCELOT, 1, 3)).spawn(SpawnGroup.CREATURE, 1, new SpawnSettings.SpawnEntry(EntityType.PANDA, 1, 2));
        return OverworldBiomeCreator.createJungleFeatures(featureLookup, carverLookup, 0.9f, false, false, true).spawnSettings(lv.build()).setEnvironmentAttribute(EnvironmentAttributes.BACKGROUND_MUSIC_AUDIO, new BackgroundMusic(SoundEvents.MUSIC_OVERWORLD_JUNGLE)).setEnvironmentAttribute(EnvironmentAttributes.INCREASED_FIRE_BURNOUT_GAMEPLAY, true).build();
    }

    public static Biome createNormalBambooJungle(RegistryEntryLookup<PlacedFeature> featureLookup, RegistryEntryLookup<ConfiguredCarver<?>> carverLookup) {
        SpawnSettings.Builder lv = new SpawnSettings.Builder();
        DefaultBiomeFeatures.addJungleMobs(lv);
        lv.spawn(SpawnGroup.CREATURE, 40, new SpawnSettings.SpawnEntry(EntityType.PARROT, 1, 2)).spawn(SpawnGroup.CREATURE, 80, new SpawnSettings.SpawnEntry(EntityType.PANDA, 1, 2)).spawn(SpawnGroup.MONSTER, 2, new SpawnSettings.SpawnEntry(EntityType.OCELOT, 1, 1));
        return OverworldBiomeCreator.createJungleFeatures(featureLookup, carverLookup, 0.9f, true, false, true).spawnSettings(lv.build()).setEnvironmentAttribute(EnvironmentAttributes.BACKGROUND_MUSIC_AUDIO, new BackgroundMusic(SoundEvents.MUSIC_OVERWORLD_BAMBOO_JUNGLE)).setEnvironmentAttribute(EnvironmentAttributes.INCREASED_FIRE_BURNOUT_GAMEPLAY, true).build();
    }

    private static Biome.Builder createJungleFeatures(RegistryEntryLookup<PlacedFeature> featureLookup, RegistryEntryLookup<ConfiguredCarver<?>> carverLookup, float depth, boolean bamboo, boolean sparse, boolean unmodified) {
        GenerationSettings.LookupBackedBuilder lv = new GenerationSettings.LookupBackedBuilder(featureLookup, carverLookup);
        OverworldBiomeCreator.addBasicFeatures(lv);
        DefaultBiomeFeatures.addDefaultOres(lv);
        DefaultBiomeFeatures.addDefaultDisks(lv);
        if (bamboo) {
            DefaultBiomeFeatures.addBambooJungleTrees(lv);
        } else {
            if (unmodified) {
                DefaultBiomeFeatures.addBamboo(lv);
            }
            if (sparse) {
                DefaultBiomeFeatures.addSparseJungleTrees(lv);
            } else {
                DefaultBiomeFeatures.addJungleTrees(lv);
            }
        }
        DefaultBiomeFeatures.addExtraDefaultFlowers(lv);
        DefaultBiomeFeatures.addJungleGrass(lv);
        DefaultBiomeFeatures.addDefaultMushrooms(lv);
        DefaultBiomeFeatures.addDefaultVegetation(lv, true);
        DefaultBiomeFeatures.addVines(lv);
        if (sparse) {
            DefaultBiomeFeatures.addSparseMelons(lv);
        } else {
            DefaultBiomeFeatures.addMelons(lv);
        }
        return OverworldBiomeCreator.biome(0.95f, depth).generationSettings(lv.build());
    }

    public static Biome createWindsweptHills(RegistryEntryLookup<PlacedFeature> featureLookup, RegistryEntryLookup<ConfiguredCarver<?>> carverLookup, boolean forest) {
        SpawnSettings.Builder lv = new SpawnSettings.Builder();
        DefaultBiomeFeatures.addFarmAnimals(lv);
        lv.spawn(SpawnGroup.CREATURE, 5, new SpawnSettings.SpawnEntry(EntityType.LLAMA, 4, 6));
        DefaultBiomeFeatures.addCaveAndMonsters(lv);
        GenerationSettings.LookupBackedBuilder lv2 = new GenerationSettings.LookupBackedBuilder(featureLookup, carverLookup);
        OverworldBiomeCreator.addBasicFeatures(lv2);
        DefaultBiomeFeatures.addDefaultOres(lv2);
        DefaultBiomeFeatures.addDefaultDisks(lv2);
        if (forest) {
            DefaultBiomeFeatures.addWindsweptForestTrees(lv2);
        } else {
            DefaultBiomeFeatures.addWindsweptHillsTrees(lv2);
        }
        DefaultBiomeFeatures.addBushes(lv2);
        DefaultBiomeFeatures.addDefaultFlowers(lv2);
        DefaultBiomeFeatures.addDefaultGrass(lv2);
        DefaultBiomeFeatures.addDefaultMushrooms(lv2);
        DefaultBiomeFeatures.addDefaultVegetation(lv2, true);
        DefaultBiomeFeatures.addEmeraldOre(lv2);
        DefaultBiomeFeatures.addInfestedStone(lv2);
        return OverworldBiomeCreator.biome(0.2f, 0.3f).spawnSettings(lv.build()).generationSettings(lv2.build()).build();
    }

    public static Biome createDesert(RegistryEntryLookup<PlacedFeature> featureLookup, RegistryEntryLookup<ConfiguredCarver<?>> carverLookup) {
        SpawnSettings.Builder lv = new SpawnSettings.Builder();
        DefaultBiomeFeatures.addDesertMobs(lv);
        GenerationSettings.LookupBackedBuilder lv2 = new GenerationSettings.LookupBackedBuilder(featureLookup, carverLookup);
        DefaultBiomeFeatures.addFossils(lv2);
        OverworldBiomeCreator.addBasicFeatures(lv2);
        DefaultBiomeFeatures.addDefaultOres(lv2);
        DefaultBiomeFeatures.addDefaultDisks(lv2);
        DefaultBiomeFeatures.addDefaultFlowers(lv2);
        DefaultBiomeFeatures.addDefaultGrass(lv2);
        DefaultBiomeFeatures.addDesertDryVegetation(lv2);
        DefaultBiomeFeatures.addDefaultMushrooms(lv2);
        DefaultBiomeFeatures.addDesertVegetation(lv2);
        DefaultBiomeFeatures.addDesertFeatures(lv2);
        return OverworldBiomeCreator.biome(2.0f, 0.0f).precipitation(false).setEnvironmentAttribute(EnvironmentAttributes.BACKGROUND_MUSIC_AUDIO, new BackgroundMusic(SoundEvents.MUSIC_OVERWORLD_DESERT)).setEnvironmentAttribute(EnvironmentAttributes.SNOW_GOLEM_MELTS_GAMEPLAY, true).spawnSettings(lv.build()).generationSettings(lv2.build()).build();
    }

    public static Biome createPlains(RegistryEntryLookup<PlacedFeature> featureLookup, RegistryEntryLookup<ConfiguredCarver<?>> carverLookup, boolean sunflower, boolean snowy, boolean iceSpikes) {
        SpawnSettings.Builder lv = new SpawnSettings.Builder();
        GenerationSettings.LookupBackedBuilder lv2 = new GenerationSettings.LookupBackedBuilder(featureLookup, carverLookup);
        OverworldBiomeCreator.addBasicFeatures(lv2);
        if (snowy) {
            lv.creatureSpawnProbability(0.07f);
            DefaultBiomeFeatures.addSnowyMobs(lv, !iceSpikes);
            if (iceSpikes) {
                lv2.feature(GenerationStep.Feature.SURFACE_STRUCTURES, MiscPlacedFeatures.ICE_SPIKE);
                lv2.feature(GenerationStep.Feature.SURFACE_STRUCTURES, MiscPlacedFeatures.ICE_PATCH);
            }
        } else {
            DefaultBiomeFeatures.addPlainsMobs(lv);
            DefaultBiomeFeatures.addPlainsTallGrass(lv2);
            if (sunflower) {
                lv2.feature(GenerationStep.Feature.VEGETAL_DECORATION, VegetationPlacedFeatures.PATCH_SUNFLOWER);
            } else {
                DefaultBiomeFeatures.addBushes(lv2);
            }
        }
        DefaultBiomeFeatures.addDefaultOres(lv2);
        DefaultBiomeFeatures.addDefaultDisks(lv2);
        if (snowy) {
            DefaultBiomeFeatures.addSnowySpruceTrees(lv2);
            DefaultBiomeFeatures.addDefaultFlowers(lv2);
            DefaultBiomeFeatures.addDefaultGrass(lv2);
        } else {
            DefaultBiomeFeatures.addPlainsFeatures(lv2);
        }
        DefaultBiomeFeatures.addDefaultMushrooms(lv2);
        DefaultBiomeFeatures.addDefaultVegetation(lv2, true);
        return OverworldBiomeCreator.biome(snowy ? 0.0f : 0.8f, snowy ? 0.5f : 0.4f).spawnSettings(lv.build()).generationSettings(lv2.build()).build();
    }

    public static Biome createMushroomFields(RegistryEntryLookup<PlacedFeature> featureLookup, RegistryEntryLookup<ConfiguredCarver<?>> carverLookup) {
        SpawnSettings.Builder lv = new SpawnSettings.Builder();
        DefaultBiomeFeatures.addMushroomMobs(lv);
        GenerationSettings.LookupBackedBuilder lv2 = new GenerationSettings.LookupBackedBuilder(featureLookup, carverLookup);
        OverworldBiomeCreator.addBasicFeatures(lv2);
        DefaultBiomeFeatures.addDefaultOres(lv2);
        DefaultBiomeFeatures.addDefaultDisks(lv2);
        DefaultBiomeFeatures.addMushroomFieldsFeatures(lv2);
        DefaultBiomeFeatures.addDefaultVegetationNearWater(lv2);
        return OverworldBiomeCreator.biome(0.9f, 1.0f).setEnvironmentAttribute(EnvironmentAttributes.INCREASED_FIRE_BURNOUT_GAMEPLAY, true).setEnvironmentAttribute(EnvironmentAttributes.CAN_PILLAGER_PATROL_SPAWN_GAMEPLAY, false).spawnSettings(lv.build()).generationSettings(lv2.build()).build();
    }

    public static Biome createSavanna(RegistryEntryLookup<PlacedFeature> featureLookup, RegistryEntryLookup<ConfiguredCarver<?>> carverLookup, boolean windswept, boolean plateau) {
        GenerationSettings.LookupBackedBuilder lv = new GenerationSettings.LookupBackedBuilder(featureLookup, carverLookup);
        OverworldBiomeCreator.addBasicFeatures(lv);
        if (!windswept) {
            DefaultBiomeFeatures.addSavannaTallGrass(lv);
        }
        DefaultBiomeFeatures.addDefaultOres(lv);
        DefaultBiomeFeatures.addDefaultDisks(lv);
        if (windswept) {
            DefaultBiomeFeatures.addExtraSavannaTrees(lv);
            DefaultBiomeFeatures.addDefaultFlowers(lv);
            DefaultBiomeFeatures.addWindsweptSavannaGrass(lv);
        } else {
            DefaultBiomeFeatures.addSavannaTrees(lv);
            DefaultBiomeFeatures.addExtraDefaultFlowers(lv);
            DefaultBiomeFeatures.addSavannaGrass(lv);
        }
        DefaultBiomeFeatures.addDefaultMushrooms(lv);
        DefaultBiomeFeatures.addDefaultVegetation(lv, true);
        SpawnSettings.Builder lv2 = new SpawnSettings.Builder();
        DefaultBiomeFeatures.addFarmAnimals(lv2);
        lv2.spawn(SpawnGroup.CREATURE, 1, new SpawnSettings.SpawnEntry(EntityType.HORSE, 2, 6)).spawn(SpawnGroup.CREATURE, 1, new SpawnSettings.SpawnEntry(EntityType.DONKEY, 1, 1)).spawn(SpawnGroup.CREATURE, 10, new SpawnSettings.SpawnEntry(EntityType.ARMADILLO, 2, 3));
        DefaultBiomeFeatures.addCaveAndMonstersAndZombieHorse(lv2);
        if (plateau) {
            lv2.spawn(SpawnGroup.CREATURE, 8, new SpawnSettings.SpawnEntry(EntityType.LLAMA, 4, 4));
            lv2.spawn(SpawnGroup.CREATURE, 8, new SpawnSettings.SpawnEntry(EntityType.WOLF, 4, 8));
        }
        return OverworldBiomeCreator.biome(2.0f, 0.0f).precipitation(false).setEnvironmentAttribute(EnvironmentAttributes.SNOW_GOLEM_MELTS_GAMEPLAY, true).spawnSettings(lv2.build()).generationSettings(lv.build()).build();
    }

    public static Biome createBadlands(RegistryEntryLookup<PlacedFeature> featureLookup, RegistryEntryLookup<ConfiguredCarver<?>> carverLookup, boolean plateau) {
        SpawnSettings.Builder lv = new SpawnSettings.Builder();
        DefaultBiomeFeatures.addFarmAnimals(lv);
        DefaultBiomeFeatures.addCaveAndMonsters(lv);
        lv.spawn(SpawnGroup.CREATURE, 6, new SpawnSettings.SpawnEntry(EntityType.ARMADILLO, 1, 2));
        lv.creatureSpawnProbability(0.03f);
        if (plateau) {
            lv.spawn(SpawnGroup.CREATURE, 2, new SpawnSettings.SpawnEntry(EntityType.WOLF, 4, 8));
            lv.creatureSpawnProbability(0.04f);
        }
        GenerationSettings.LookupBackedBuilder lv2 = new GenerationSettings.LookupBackedBuilder(featureLookup, carverLookup);
        OverworldBiomeCreator.addBasicFeatures(lv2);
        DefaultBiomeFeatures.addDefaultOres(lv2);
        DefaultBiomeFeatures.addExtraGoldOre(lv2);
        DefaultBiomeFeatures.addDefaultDisks(lv2);
        if (plateau) {
            DefaultBiomeFeatures.addBadlandsPlateauTrees(lv2);
        }
        DefaultBiomeFeatures.addBadlandsGrass(lv2);
        DefaultBiomeFeatures.addDefaultMushrooms(lv2);
        DefaultBiomeFeatures.addBadlandsVegetation(lv2);
        return OverworldBiomeCreator.biome(2.0f, 0.0f).precipitation(false).setEnvironmentAttribute(EnvironmentAttributes.BACKGROUND_MUSIC_AUDIO, new BackgroundMusic(SoundEvents.MUSIC_OVERWORLD_BADLANDS)).setEnvironmentAttribute(EnvironmentAttributes.SNOW_GOLEM_MELTS_GAMEPLAY, true).effects(new BiomeEffects.Builder().waterColor(4159204).foliageColor(10387789).grassColor(9470285).build()).spawnSettings(lv.build()).generationSettings(lv2.build()).build();
    }

    private static Biome.Builder createOcean() {
        return OverworldBiomeCreator.biome(0.5f, 0.5f).setEnvironmentAttribute(EnvironmentAttributes.BACKGROUND_MUSIC_AUDIO, BackgroundMusic.DEFAULT.withUnderwater(MusicType.UNDERWATER));
    }

    private static GenerationSettings.LookupBackedBuilder createOceanGenerationSettings(RegistryEntryLookup<PlacedFeature> featureLookup, RegistryEntryLookup<ConfiguredCarver<?>> carverLookup) {
        GenerationSettings.LookupBackedBuilder lv = new GenerationSettings.LookupBackedBuilder(featureLookup, carverLookup);
        OverworldBiomeCreator.addBasicFeatures(lv);
        DefaultBiomeFeatures.addDefaultOres(lv);
        DefaultBiomeFeatures.addDefaultDisks(lv);
        DefaultBiomeFeatures.addWaterBiomeOakTrees(lv);
        DefaultBiomeFeatures.addDefaultFlowers(lv);
        DefaultBiomeFeatures.addDefaultGrass(lv);
        DefaultBiomeFeatures.addDefaultMushrooms(lv);
        DefaultBiomeFeatures.addDefaultVegetation(lv, true);
        return lv;
    }

    public static Biome createColdOcean(RegistryEntryLookup<PlacedFeature> featureLookup, RegistryEntryLookup<ConfiguredCarver<?>> carverLookup, boolean deep) {
        SpawnSettings.Builder lv = new SpawnSettings.Builder();
        DefaultBiomeFeatures.addOceanMobs(lv, 3, 4, 15);
        lv.spawn(SpawnGroup.WATER_AMBIENT, 15, new SpawnSettings.SpawnEntry(EntityType.SALMON, 1, 5));
        lv.spawn(SpawnGroup.WATER_CREATURE, 2, new SpawnSettings.SpawnEntry(EntityType.NAUTILUS, 1, 1));
        GenerationSettings.LookupBackedBuilder lv2 = OverworldBiomeCreator.createOceanGenerationSettings(featureLookup, carverLookup);
        lv2.feature(GenerationStep.Feature.VEGETAL_DECORATION, deep ? OceanPlacedFeatures.SEAGRASS_DEEP_COLD : OceanPlacedFeatures.SEAGRASS_COLD);
        DefaultBiomeFeatures.addKelp(lv2);
        return OverworldBiomeCreator.createOcean().effects(new BiomeEffects.Builder().waterColor(4020182).build()).spawnSettings(lv.build()).generationSettings(lv2.build()).build();
    }

    public static Biome createNormalOcean(RegistryEntryLookup<PlacedFeature> featureLookup, RegistryEntryLookup<ConfiguredCarver<?>> carverLookup, boolean deep) {
        SpawnSettings.Builder lv = new SpawnSettings.Builder();
        DefaultBiomeFeatures.addOceanMobs(lv, 1, 4, 10);
        lv.spawn(SpawnGroup.WATER_CREATURE, 1, new SpawnSettings.SpawnEntry(EntityType.DOLPHIN, 1, 2)).spawn(SpawnGroup.WATER_CREATURE, 5, new SpawnSettings.SpawnEntry(EntityType.NAUTILUS, 1, 1));
        GenerationSettings.LookupBackedBuilder lv2 = OverworldBiomeCreator.createOceanGenerationSettings(featureLookup, carverLookup);
        lv2.feature(GenerationStep.Feature.VEGETAL_DECORATION, deep ? OceanPlacedFeatures.SEAGRASS_DEEP : OceanPlacedFeatures.SEAGRASS_NORMAL);
        DefaultBiomeFeatures.addKelp(lv2);
        return OverworldBiomeCreator.createOcean().spawnSettings(lv.build()).generationSettings(lv2.build()).build();
    }

    public static Biome createLukewarmOcean(RegistryEntryLookup<PlacedFeature> featureLookup, RegistryEntryLookup<ConfiguredCarver<?>> carverLookup, boolean deep) {
        SpawnSettings.Builder lv = new SpawnSettings.Builder();
        if (deep) {
            DefaultBiomeFeatures.addOceanMobs(lv, 8, 4, 8);
        } else {
            DefaultBiomeFeatures.addOceanMobs(lv, 10, 2, 15);
        }
        lv.spawn(SpawnGroup.WATER_AMBIENT, 5, new SpawnSettings.SpawnEntry(EntityType.PUFFERFISH, 1, 3)).spawn(SpawnGroup.WATER_AMBIENT, 25, new SpawnSettings.SpawnEntry(EntityType.TROPICAL_FISH, 8, 8)).spawn(SpawnGroup.WATER_CREATURE, 2, new SpawnSettings.SpawnEntry(EntityType.DOLPHIN, 1, 2)).spawn(SpawnGroup.WATER_CREATURE, 5, new SpawnSettings.SpawnEntry(EntityType.NAUTILUS, 1, 1));
        GenerationSettings.LookupBackedBuilder lv2 = OverworldBiomeCreator.createOceanGenerationSettings(featureLookup, carverLookup);
        lv2.feature(GenerationStep.Feature.VEGETAL_DECORATION, deep ? OceanPlacedFeatures.SEAGRASS_DEEP_WARM : OceanPlacedFeatures.SEAGRASS_WARM);
        DefaultBiomeFeatures.addLessKelp(lv2);
        return OverworldBiomeCreator.createOcean().setEnvironmentAttribute(EnvironmentAttributes.WATER_FOG_COLOR_VISUAL, -16509389).effects(new BiomeEffects.Builder().waterColor(4566514).build()).spawnSettings(lv.build()).generationSettings(lv2.build()).build();
    }

    public static Biome createWarmOcean(RegistryEntryLookup<PlacedFeature> featureLookup, RegistryEntryLookup<ConfiguredCarver<?>> carverLookup) {
        SpawnSettings.Builder lv = new SpawnSettings.Builder().spawn(SpawnGroup.WATER_AMBIENT, 15, new SpawnSettings.SpawnEntry(EntityType.PUFFERFISH, 1, 3)).spawn(SpawnGroup.WATER_CREATURE, 5, new SpawnSettings.SpawnEntry(EntityType.NAUTILUS, 1, 1));
        DefaultBiomeFeatures.addWarmOceanMobs(lv, 10, 4);
        GenerationSettings.LookupBackedBuilder lv2 = OverworldBiomeCreator.createOceanGenerationSettings(featureLookup, carverLookup).feature(GenerationStep.Feature.VEGETAL_DECORATION, OceanPlacedFeatures.WARM_OCEAN_VEGETATION).feature(GenerationStep.Feature.VEGETAL_DECORATION, OceanPlacedFeatures.SEAGRASS_WARM).feature(GenerationStep.Feature.VEGETAL_DECORATION, OceanPlacedFeatures.SEA_PICKLE);
        return OverworldBiomeCreator.createOcean().setEnvironmentAttribute(EnvironmentAttributes.WATER_FOG_COLOR_VISUAL, -16507085).effects(new BiomeEffects.Builder().waterColor(4445678).build()).spawnSettings(lv.build()).generationSettings(lv2.build()).build();
    }

    public static Biome createFrozenOcean(RegistryEntryLookup<PlacedFeature> featureLookup, RegistryEntryLookup<ConfiguredCarver<?>> carverLookup, boolean deep) {
        SpawnSettings.Builder lv = new SpawnSettings.Builder().spawn(SpawnGroup.WATER_CREATURE, 1, new SpawnSettings.SpawnEntry(EntityType.SQUID, 1, 4)).spawn(SpawnGroup.WATER_AMBIENT, 15, new SpawnSettings.SpawnEntry(EntityType.SALMON, 1, 5)).spawn(SpawnGroup.CREATURE, 1, new SpawnSettings.SpawnEntry(EntityType.POLAR_BEAR, 1, 2)).spawn(SpawnGroup.WATER_CREATURE, 2, new SpawnSettings.SpawnEntry(EntityType.NAUTILUS, 1, 1));
        DefaultBiomeFeatures.addCaveAndMonsters(lv);
        lv.spawn(SpawnGroup.MONSTER, 5, new SpawnSettings.SpawnEntry(EntityType.DROWNED, 1, 1));
        float f = deep ? 0.5f : 0.0f;
        GenerationSettings.LookupBackedBuilder lv2 = new GenerationSettings.LookupBackedBuilder(featureLookup, carverLookup);
        DefaultBiomeFeatures.addIcebergs(lv2);
        OverworldBiomeCreator.addBasicFeatures(lv2);
        DefaultBiomeFeatures.addBlueIce(lv2);
        DefaultBiomeFeatures.addDefaultOres(lv2);
        DefaultBiomeFeatures.addDefaultDisks(lv2);
        DefaultBiomeFeatures.addWaterBiomeOakTrees(lv2);
        DefaultBiomeFeatures.addDefaultFlowers(lv2);
        DefaultBiomeFeatures.addDefaultGrass(lv2);
        DefaultBiomeFeatures.addDefaultMushrooms(lv2);
        DefaultBiomeFeatures.addDefaultVegetation(lv2, true);
        return OverworldBiomeCreator.biome(f, 0.5f).temperatureModifier(Biome.TemperatureModifier.FROZEN).effects(new BiomeEffects.Builder().waterColor(3750089).build()).spawnSettings(lv.build()).generationSettings(lv2.build()).build();
    }

    public static Biome createNormalForest(RegistryEntryLookup<PlacedFeature> featureLookup, RegistryEntryLookup<ConfiguredCarver<?>> carverLookup, boolean birch, boolean oldGrowth, boolean flower) {
        BackgroundMusic lv2;
        GenerationSettings.LookupBackedBuilder lv = new GenerationSettings.LookupBackedBuilder(featureLookup, carverLookup);
        OverworldBiomeCreator.addBasicFeatures(lv);
        if (flower) {
            lv2 = new BackgroundMusic(SoundEvents.MUSIC_OVERWORLD_FLOWER_FOREST);
            lv.feature(GenerationStep.Feature.VEGETAL_DECORATION, VegetationPlacedFeatures.FLOWER_FOREST_FLOWERS);
        } else {
            lv2 = new BackgroundMusic(SoundEvents.MUSIC_OVERWORLD_FOREST);
            DefaultBiomeFeatures.addForestFlowers(lv);
        }
        DefaultBiomeFeatures.addDefaultOres(lv);
        DefaultBiomeFeatures.addDefaultDisks(lv);
        if (flower) {
            lv.feature(GenerationStep.Feature.VEGETAL_DECORATION, VegetationPlacedFeatures.TREES_FLOWER_FOREST);
            lv.feature(GenerationStep.Feature.VEGETAL_DECORATION, VegetationPlacedFeatures.FLOWER_FLOWER_FOREST);
            DefaultBiomeFeatures.addDefaultGrass(lv);
        } else {
            if (birch) {
                DefaultBiomeFeatures.addBirchForestWildflowers(lv);
                if (oldGrowth) {
                    DefaultBiomeFeatures.addTallBirchTrees(lv);
                } else {
                    DefaultBiomeFeatures.addBirchTrees(lv);
                }
            } else {
                DefaultBiomeFeatures.addForestTrees(lv);
            }
            DefaultBiomeFeatures.addBushes(lv);
            DefaultBiomeFeatures.addDefaultFlowers(lv);
            DefaultBiomeFeatures.addForestGrass(lv);
        }
        DefaultBiomeFeatures.addDefaultMushrooms(lv);
        DefaultBiomeFeatures.addDefaultVegetation(lv, true);
        SpawnSettings.Builder lv3 = new SpawnSettings.Builder();
        DefaultBiomeFeatures.addFarmAnimals(lv3);
        DefaultBiomeFeatures.addCaveAndMonsters(lv3);
        if (flower) {
            lv3.spawn(SpawnGroup.CREATURE, 4, new SpawnSettings.SpawnEntry(EntityType.RABBIT, 2, 3));
        } else if (!birch) {
            lv3.spawn(SpawnGroup.CREATURE, 5, new SpawnSettings.SpawnEntry(EntityType.WOLF, 4, 4));
        }
        return OverworldBiomeCreator.biome(birch ? 0.6f : 0.7f, birch ? 0.6f : 0.8f).setEnvironmentAttribute(EnvironmentAttributes.BACKGROUND_MUSIC_AUDIO, lv2).spawnSettings(lv3.build()).generationSettings(lv.build()).build();
    }

    public static Biome createTaiga(RegistryEntryLookup<PlacedFeature> featureLookup, RegistryEntryLookup<ConfiguredCarver<?>> carverLookup, boolean snowy) {
        SpawnSettings.Builder lv = new SpawnSettings.Builder();
        DefaultBiomeFeatures.addFarmAnimals(lv);
        lv.spawn(SpawnGroup.CREATURE, 8, new SpawnSettings.SpawnEntry(EntityType.WOLF, 4, 4)).spawn(SpawnGroup.CREATURE, 4, new SpawnSettings.SpawnEntry(EntityType.RABBIT, 2, 3)).spawn(SpawnGroup.CREATURE, 8, new SpawnSettings.SpawnEntry(EntityType.FOX, 2, 4));
        DefaultBiomeFeatures.addCaveAndMonsters(lv);
        GenerationSettings.LookupBackedBuilder lv2 = new GenerationSettings.LookupBackedBuilder(featureLookup, carverLookup);
        OverworldBiomeCreator.addBasicFeatures(lv2);
        DefaultBiomeFeatures.addLargeFerns(lv2);
        DefaultBiomeFeatures.addDefaultOres(lv2);
        DefaultBiomeFeatures.addDefaultDisks(lv2);
        DefaultBiomeFeatures.addTaigaTrees(lv2);
        DefaultBiomeFeatures.addDefaultFlowers(lv2);
        DefaultBiomeFeatures.addTaigaGrass(lv2);
        DefaultBiomeFeatures.addDefaultVegetation(lv2, true);
        if (snowy) {
            DefaultBiomeFeatures.addSweetBerryBushesSnowy(lv2);
        } else {
            DefaultBiomeFeatures.addSweetBerryBushes(lv2);
        }
        int i = snowy ? 4020182 : 4159204;
        return OverworldBiomeCreator.biome(snowy ? -0.5f : 0.25f, snowy ? 0.4f : 0.8f).effects(new BiomeEffects.Builder().waterColor(i).build()).spawnSettings(lv.build()).generationSettings(lv2.build()).build();
    }

    public static Biome createDenseForest(RegistryEntryLookup<PlacedFeature> featureLookup, RegistryEntryLookup<ConfiguredCarver<?>> carverLookup, boolean paleGarden) {
        SpawnSettings.Builder lv = new SpawnSettings.Builder();
        if (!paleGarden) {
            DefaultBiomeFeatures.addFarmAnimals(lv);
        }
        DefaultBiomeFeatures.addCaveAndMonsters(lv);
        GenerationSettings.LookupBackedBuilder lv2 = new GenerationSettings.LookupBackedBuilder(featureLookup, carverLookup);
        OverworldBiomeCreator.addBasicFeatures(lv2);
        lv2.feature(GenerationStep.Feature.VEGETAL_DECORATION, paleGarden ? VegetationPlacedFeatures.PALE_GARDEN_VEGETATION : VegetationPlacedFeatures.DARK_FOREST_VEGETATION);
        if (!paleGarden) {
            DefaultBiomeFeatures.addForestFlowers(lv2);
        } else {
            lv2.feature(GenerationStep.Feature.VEGETAL_DECORATION, VegetationPlacedFeatures.PALE_MOSS_PATCH);
            lv2.feature(GenerationStep.Feature.VEGETAL_DECORATION, VegetationPlacedFeatures.PALE_GARDEN_FLOWERS);
        }
        DefaultBiomeFeatures.addDefaultOres(lv2);
        DefaultBiomeFeatures.addDefaultDisks(lv2);
        if (!paleGarden) {
            DefaultBiomeFeatures.addDefaultFlowers(lv2);
        } else {
            lv2.feature(GenerationStep.Feature.VEGETAL_DECORATION, VegetationPlacedFeatures.FLOWER_PALE_GARDEN);
        }
        DefaultBiomeFeatures.addForestGrass(lv2);
        if (!paleGarden) {
            DefaultBiomeFeatures.addDefaultMushrooms(lv2);
            DefaultBiomeFeatures.addLeafLitter(lv2);
        }
        DefaultBiomeFeatures.addDefaultVegetation(lv2, true);
        EnvironmentAttributeMap lv3 = EnvironmentAttributeMap.builder().with(EnvironmentAttributes.SKY_COLOR_VISUAL, -4605511).with(EnvironmentAttributes.FOG_COLOR_VISUAL, -8292496).with(EnvironmentAttributes.WATER_FOG_COLOR_VISUAL, -11179648).with(EnvironmentAttributes.BACKGROUND_MUSIC_AUDIO, BackgroundMusic.EMPTY).with(EnvironmentAttributes.MUSIC_VOLUME_AUDIO, Float.valueOf(0.0f)).build();
        EnvironmentAttributeMap lv4 = EnvironmentAttributeMap.builder().with(EnvironmentAttributes.BACKGROUND_MUSIC_AUDIO, new BackgroundMusic(SoundEvents.MUSIC_OVERWORLD_FOREST)).build();
        return OverworldBiomeCreator.biome(0.7f, 0.8f).addEnvironmentAttributes(paleGarden ? lv3 : lv4).effects(paleGarden ? new BiomeEffects.Builder().waterColor(7768221).grassColor(0x778272).foliageColor(8883574).dryFoliageColor(10528412).build() : new BiomeEffects.Builder().waterColor(4159204).dryFoliageColor(8082228).grassColorModifier(BiomeEffects.GrassColorModifier.DARK_FOREST).build()).spawnSettings(lv.build()).generationSettings(lv2.build()).build();
    }

    public static Biome createSwamp(RegistryEntryLookup<PlacedFeature> featureLookup, RegistryEntryLookup<ConfiguredCarver<?>> carverLookup) {
        SpawnSettings.Builder lv = new SpawnSettings.Builder();
        DefaultBiomeFeatures.addFarmAnimals(lv);
        DefaultBiomeFeatures.addSwampMobs(lv, 70);
        GenerationSettings.LookupBackedBuilder lv2 = new GenerationSettings.LookupBackedBuilder(featureLookup, carverLookup);
        DefaultBiomeFeatures.addFossils(lv2);
        OverworldBiomeCreator.addBasicFeatures(lv2);
        DefaultBiomeFeatures.addDefaultOres(lv2);
        DefaultBiomeFeatures.addClayDisk(lv2);
        DefaultBiomeFeatures.addSwampFeatures(lv2);
        DefaultBiomeFeatures.addDefaultMushrooms(lv2);
        DefaultBiomeFeatures.addSwampVegetation(lv2);
        lv2.feature(GenerationStep.Feature.VEGETAL_DECORATION, OceanPlacedFeatures.SEAGRASS_SWAMP);
        return OverworldBiomeCreator.biome(0.8f, 0.9f).setEnvironmentAttribute(EnvironmentAttributes.WATER_FOG_COLOR_VISUAL, -14474473).setEnvironmentAttributeModifier(EnvironmentAttributes.WATER_FOG_END_DISTANCE_VISUAL, FloatModifier.MULTIPLY, Float.valueOf(0.85f)).setEnvironmentAttribute(EnvironmentAttributes.BACKGROUND_MUSIC_AUDIO, new BackgroundMusic(SoundEvents.MUSIC_OVERWORLD_SWAMP)).setEnvironmentAttribute(EnvironmentAttributes.INCREASED_FIRE_BURNOUT_GAMEPLAY, true).effects(new BiomeEffects.Builder().waterColor(6388580).foliageColor(6975545).dryFoliageColor(8082228).grassColorModifier(BiomeEffects.GrassColorModifier.SWAMP).build()).spawnSettings(lv.build()).generationSettings(lv2.build()).build();
    }

    public static Biome createMangroveSwamp(RegistryEntryLookup<PlacedFeature> featureLookup, RegistryEntryLookup<ConfiguredCarver<?>> carverLookup) {
        SpawnSettings.Builder lv = new SpawnSettings.Builder();
        DefaultBiomeFeatures.addSwampMobs(lv, 70);
        lv.spawn(SpawnGroup.WATER_AMBIENT, 25, new SpawnSettings.SpawnEntry(EntityType.TROPICAL_FISH, 8, 8));
        GenerationSettings.LookupBackedBuilder lv2 = new GenerationSettings.LookupBackedBuilder(featureLookup, carverLookup);
        DefaultBiomeFeatures.addFossils(lv2);
        OverworldBiomeCreator.addBasicFeatures(lv2);
        DefaultBiomeFeatures.addDefaultOres(lv2);
        DefaultBiomeFeatures.addGrassAndClayDisks(lv2);
        DefaultBiomeFeatures.addMangroveSwampFeatures(lv2);
        DefaultBiomeFeatures.addMangroveSwampAquaticFeatures(lv2);
        return OverworldBiomeCreator.biome(0.8f, 0.9f).setEnvironmentAttribute(EnvironmentAttributes.FOG_COLOR_VISUAL, -4138753).setEnvironmentAttribute(EnvironmentAttributes.WATER_FOG_COLOR_VISUAL, -11699616).setEnvironmentAttributeModifier(EnvironmentAttributes.WATER_FOG_END_DISTANCE_VISUAL, FloatModifier.MULTIPLY, Float.valueOf(0.85f)).setEnvironmentAttribute(EnvironmentAttributes.BACKGROUND_MUSIC_AUDIO, new BackgroundMusic(SoundEvents.MUSIC_OVERWORLD_SWAMP)).setEnvironmentAttribute(EnvironmentAttributes.INCREASED_FIRE_BURNOUT_GAMEPLAY, true).effects(new BiomeEffects.Builder().waterColor(3832426).foliageColor(9285927).dryFoliageColor(8082228).grassColorModifier(BiomeEffects.GrassColorModifier.SWAMP).build()).spawnSettings(lv.build()).generationSettings(lv2.build()).build();
    }

    public static Biome createRiver(RegistryEntryLookup<PlacedFeature> featureLookup, RegistryEntryLookup<ConfiguredCarver<?>> carverLookup, boolean frozen) {
        SpawnSettings.Builder lv = new SpawnSettings.Builder().spawn(SpawnGroup.WATER_CREATURE, 2, new SpawnSettings.SpawnEntry(EntityType.SQUID, 1, 4)).spawn(SpawnGroup.WATER_AMBIENT, 5, new SpawnSettings.SpawnEntry(EntityType.SALMON, 1, 5));
        DefaultBiomeFeatures.addCaveAndMonsters(lv);
        lv.spawn(SpawnGroup.MONSTER, frozen ? 1 : 100, new SpawnSettings.SpawnEntry(EntityType.DROWNED, 1, 1));
        GenerationSettings.LookupBackedBuilder lv2 = new GenerationSettings.LookupBackedBuilder(featureLookup, carverLookup);
        OverworldBiomeCreator.addBasicFeatures(lv2);
        DefaultBiomeFeatures.addDefaultOres(lv2);
        DefaultBiomeFeatures.addDefaultDisks(lv2);
        DefaultBiomeFeatures.addWaterBiomeOakTrees(lv2);
        DefaultBiomeFeatures.addBushes(lv2);
        DefaultBiomeFeatures.addDefaultFlowers(lv2);
        DefaultBiomeFeatures.addDefaultGrass(lv2);
        DefaultBiomeFeatures.addDefaultMushrooms(lv2);
        DefaultBiomeFeatures.addDefaultVegetation(lv2, true);
        if (!frozen) {
            lv2.feature(GenerationStep.Feature.VEGETAL_DECORATION, OceanPlacedFeatures.SEAGRASS_RIVER);
        }
        return OverworldBiomeCreator.biome(frozen ? 0.0f : 0.5f, 0.5f).setEnvironmentAttribute(EnvironmentAttributes.BACKGROUND_MUSIC_AUDIO, BackgroundMusic.DEFAULT.withUnderwater(MusicType.UNDERWATER)).effects(new BiomeEffects.Builder().waterColor(frozen ? 3750089 : 4159204).build()).spawnSettings(lv.build()).generationSettings(lv2.build()).build();
    }

    public static Biome createBeach(RegistryEntryLookup<PlacedFeature> featureLookup, RegistryEntryLookup<ConfiguredCarver<?>> carverLookup, boolean snowy, boolean stony) {
        boolean bl3;
        SpawnSettings.Builder lv = new SpawnSettings.Builder();
        boolean bl = bl3 = !stony && !snowy;
        if (bl3) {
            lv.spawn(SpawnGroup.CREATURE, 5, new SpawnSettings.SpawnEntry(EntityType.TURTLE, 2, 5));
        }
        DefaultBiomeFeatures.addCaveAndMonsters(lv);
        GenerationSettings.LookupBackedBuilder lv2 = new GenerationSettings.LookupBackedBuilder(featureLookup, carverLookup);
        OverworldBiomeCreator.addBasicFeatures(lv2);
        DefaultBiomeFeatures.addDefaultOres(lv2);
        DefaultBiomeFeatures.addDefaultDisks(lv2);
        DefaultBiomeFeatures.addDefaultFlowers(lv2);
        DefaultBiomeFeatures.addDefaultGrass(lv2);
        DefaultBiomeFeatures.addDefaultMushrooms(lv2);
        DefaultBiomeFeatures.addDefaultVegetation(lv2, true);
        float f = snowy ? 0.05f : (stony ? 0.2f : 0.8f);
        int i = snowy ? 4020182 : 4159204;
        return OverworldBiomeCreator.biome(f, bl3 ? 0.4f : 0.3f).effects(new BiomeEffects.Builder().waterColor(i).build()).spawnSettings(lv.build()).generationSettings(lv2.build()).build();
    }

    public static Biome createTheVoid(RegistryEntryLookup<PlacedFeature> featureLookup, RegistryEntryLookup<ConfiguredCarver<?>> carverLookup) {
        GenerationSettings.LookupBackedBuilder lv = new GenerationSettings.LookupBackedBuilder(featureLookup, carverLookup);
        lv.feature(GenerationStep.Feature.TOP_LAYER_MODIFICATION, MiscPlacedFeatures.VOID_START_PLATFORM);
        return OverworldBiomeCreator.biome(0.5f, 0.5f).precipitation(false).spawnSettings(new SpawnSettings.Builder().build()).generationSettings(lv.build()).build();
    }

    public static Biome createMeadow(RegistryEntryLookup<PlacedFeature> featureLookup, RegistryEntryLookup<ConfiguredCarver<?>> carverLookup, boolean cherryGrove) {
        GenerationSettings.LookupBackedBuilder lv = new GenerationSettings.LookupBackedBuilder(featureLookup, carverLookup);
        SpawnSettings.Builder lv2 = new SpawnSettings.Builder();
        lv2.spawn(SpawnGroup.CREATURE, 1, new SpawnSettings.SpawnEntry(cherryGrove ? EntityType.PIG : EntityType.DONKEY, 1, 2)).spawn(SpawnGroup.CREATURE, 2, new SpawnSettings.SpawnEntry(EntityType.RABBIT, 2, 6)).spawn(SpawnGroup.CREATURE, 2, new SpawnSettings.SpawnEntry(EntityType.SHEEP, 2, 4));
        DefaultBiomeFeatures.addCaveAndMonsters(lv2);
        OverworldBiomeCreator.addBasicFeatures(lv);
        DefaultBiomeFeatures.addPlainsTallGrass(lv);
        DefaultBiomeFeatures.addDefaultOres(lv);
        DefaultBiomeFeatures.addDefaultDisks(lv);
        if (cherryGrove) {
            DefaultBiomeFeatures.addCherryGroveFeatures(lv);
        } else {
            DefaultBiomeFeatures.addMeadowFlowers(lv);
        }
        DefaultBiomeFeatures.addEmeraldOre(lv);
        DefaultBiomeFeatures.addInfestedStone(lv);
        if (cherryGrove) {
            BiomeEffects.Builder lv3 = new BiomeEffects.Builder().waterColor(6141935).grassColor(11983713).foliageColor(11983713);
            return OverworldBiomeCreator.biome(0.5f, 0.8f).setEnvironmentAttribute(EnvironmentAttributes.WATER_FOG_COLOR_VISUAL, -10635281).setEnvironmentAttribute(EnvironmentAttributes.BACKGROUND_MUSIC_AUDIO, new BackgroundMusic(SoundEvents.MUSIC_OVERWORLD_CHERRY_GROVE)).effects(lv3.build()).spawnSettings(lv2.build()).generationSettings(lv.build()).build();
        }
        return OverworldBiomeCreator.biome(0.5f, 0.8f).setEnvironmentAttribute(EnvironmentAttributes.BACKGROUND_MUSIC_AUDIO, new BackgroundMusic(SoundEvents.MUSIC_OVERWORLD_MEADOW)).effects(new BiomeEffects.Builder().waterColor(937679).build()).spawnSettings(lv2.build()).generationSettings(lv.build()).build();
    }

    private static Biome.Builder createColdPeaks(RegistryEntryLookup<PlacedFeature> featureLookup, RegistryEntryLookup<ConfiguredCarver<?>> carverLookup) {
        GenerationSettings.LookupBackedBuilder lv = new GenerationSettings.LookupBackedBuilder(featureLookup, carverLookup);
        SpawnSettings.Builder lv2 = new SpawnSettings.Builder();
        lv2.spawn(SpawnGroup.CREATURE, 5, new SpawnSettings.SpawnEntry(EntityType.GOAT, 1, 3));
        DefaultBiomeFeatures.addCaveAndMonsters(lv2);
        OverworldBiomeCreator.addBasicFeatures(lv);
        DefaultBiomeFeatures.addFrozenLavaSpring(lv);
        DefaultBiomeFeatures.addDefaultOres(lv);
        DefaultBiomeFeatures.addDefaultDisks(lv);
        DefaultBiomeFeatures.addEmeraldOre(lv);
        DefaultBiomeFeatures.addInfestedStone(lv);
        return OverworldBiomeCreator.biome(-0.7f, 0.9f).setEnvironmentAttribute(EnvironmentAttributes.INCREASED_FIRE_BURNOUT_GAMEPLAY, true).spawnSettings(lv2.build()).generationSettings(lv.build());
    }

    public static Biome createFrozenPeaks(RegistryEntryLookup<PlacedFeature> featureLookup, RegistryEntryLookup<ConfiguredCarver<?>> carverLookup) {
        return OverworldBiomeCreator.createColdPeaks(featureLookup, carverLookup).setEnvironmentAttribute(EnvironmentAttributes.BACKGROUND_MUSIC_AUDIO, new BackgroundMusic(SoundEvents.MUSIC_OVERWORLD_FROZEN_PEAKS)).build();
    }

    public static Biome createJaggedPeaks(RegistryEntryLookup<PlacedFeature> featureLookup, RegistryEntryLookup<ConfiguredCarver<?>> carverLookup) {
        return OverworldBiomeCreator.createColdPeaks(featureLookup, carverLookup).setEnvironmentAttribute(EnvironmentAttributes.BACKGROUND_MUSIC_AUDIO, new BackgroundMusic(SoundEvents.MUSIC_OVERWORLD_JAGGED_PEAKS)).build();
    }

    public static Biome createStonyPeaks(RegistryEntryLookup<PlacedFeature> featureLookup, RegistryEntryLookup<ConfiguredCarver<?>> carverLookup) {
        GenerationSettings.LookupBackedBuilder lv = new GenerationSettings.LookupBackedBuilder(featureLookup, carverLookup);
        SpawnSettings.Builder lv2 = new SpawnSettings.Builder();
        DefaultBiomeFeatures.addCaveAndMonsters(lv2);
        OverworldBiomeCreator.addBasicFeatures(lv);
        DefaultBiomeFeatures.addDefaultOres(lv);
        DefaultBiomeFeatures.addDefaultDisks(lv);
        DefaultBiomeFeatures.addEmeraldOre(lv);
        DefaultBiomeFeatures.addInfestedStone(lv);
        return OverworldBiomeCreator.biome(1.0f, 0.3f).setEnvironmentAttribute(EnvironmentAttributes.BACKGROUND_MUSIC_AUDIO, new BackgroundMusic(SoundEvents.MUSIC_OVERWORLD_STONY_PEAKS)).spawnSettings(lv2.build()).generationSettings(lv.build()).build();
    }

    public static Biome createSnowySlopes(RegistryEntryLookup<PlacedFeature> featureLookup, RegistryEntryLookup<ConfiguredCarver<?>> carverLookup) {
        GenerationSettings.LookupBackedBuilder lv = new GenerationSettings.LookupBackedBuilder(featureLookup, carverLookup);
        SpawnSettings.Builder lv2 = new SpawnSettings.Builder();
        lv2.spawn(SpawnGroup.CREATURE, 4, new SpawnSettings.SpawnEntry(EntityType.RABBIT, 2, 3)).spawn(SpawnGroup.CREATURE, 5, new SpawnSettings.SpawnEntry(EntityType.GOAT, 1, 3));
        DefaultBiomeFeatures.addCaveAndMonsters(lv2);
        OverworldBiomeCreator.addBasicFeatures(lv);
        DefaultBiomeFeatures.addFrozenLavaSpring(lv);
        DefaultBiomeFeatures.addDefaultOres(lv);
        DefaultBiomeFeatures.addDefaultDisks(lv);
        DefaultBiomeFeatures.addDefaultVegetation(lv, false);
        DefaultBiomeFeatures.addEmeraldOre(lv);
        DefaultBiomeFeatures.addInfestedStone(lv);
        return OverworldBiomeCreator.biome(-0.3f, 0.9f).setEnvironmentAttribute(EnvironmentAttributes.BACKGROUND_MUSIC_AUDIO, new BackgroundMusic(SoundEvents.MUSIC_OVERWORLD_SNOWY_SLOPES)).setEnvironmentAttribute(EnvironmentAttributes.INCREASED_FIRE_BURNOUT_GAMEPLAY, true).spawnSettings(lv2.build()).generationSettings(lv.build()).build();
    }

    public static Biome createGrove(RegistryEntryLookup<PlacedFeature> featureLookup, RegistryEntryLookup<ConfiguredCarver<?>> carverLookup) {
        GenerationSettings.LookupBackedBuilder lv = new GenerationSettings.LookupBackedBuilder(featureLookup, carverLookup);
        SpawnSettings.Builder lv2 = new SpawnSettings.Builder();
        lv2.spawn(SpawnGroup.CREATURE, 1, new SpawnSettings.SpawnEntry(EntityType.WOLF, 1, 1)).spawn(SpawnGroup.CREATURE, 8, new SpawnSettings.SpawnEntry(EntityType.RABBIT, 2, 3)).spawn(SpawnGroup.CREATURE, 4, new SpawnSettings.SpawnEntry(EntityType.FOX, 2, 4));
        DefaultBiomeFeatures.addCaveAndMonsters(lv2);
        OverworldBiomeCreator.addBasicFeatures(lv);
        DefaultBiomeFeatures.addFrozenLavaSpring(lv);
        DefaultBiomeFeatures.addDefaultOres(lv);
        DefaultBiomeFeatures.addDefaultDisks(lv);
        DefaultBiomeFeatures.addGroveTrees(lv);
        DefaultBiomeFeatures.addDefaultVegetation(lv, false);
        DefaultBiomeFeatures.addEmeraldOre(lv);
        DefaultBiomeFeatures.addInfestedStone(lv);
        return OverworldBiomeCreator.biome(-0.2f, 0.8f).setEnvironmentAttribute(EnvironmentAttributes.BACKGROUND_MUSIC_AUDIO, new BackgroundMusic(SoundEvents.MUSIC_OVERWORLD_GROVE)).spawnSettings(lv2.build()).generationSettings(lv.build()).build();
    }

    public static Biome createLushCaves(RegistryEntryLookup<PlacedFeature> featureLookup, RegistryEntryLookup<ConfiguredCarver<?>> carverLookup) {
        SpawnSettings.Builder lv = new SpawnSettings.Builder();
        lv.spawn(SpawnGroup.AXOLOTLS, 10, new SpawnSettings.SpawnEntry(EntityType.AXOLOTL, 4, 6));
        lv.spawn(SpawnGroup.WATER_AMBIENT, 25, new SpawnSettings.SpawnEntry(EntityType.TROPICAL_FISH, 8, 8));
        DefaultBiomeFeatures.addCaveAndMonsters(lv);
        GenerationSettings.LookupBackedBuilder lv2 = new GenerationSettings.LookupBackedBuilder(featureLookup, carverLookup);
        OverworldBiomeCreator.addBasicFeatures(lv2);
        DefaultBiomeFeatures.addPlainsTallGrass(lv2);
        DefaultBiomeFeatures.addDefaultOres(lv2);
        DefaultBiomeFeatures.addClayOre(lv2);
        DefaultBiomeFeatures.addDefaultDisks(lv2);
        DefaultBiomeFeatures.addLushCavesDecoration(lv2);
        return OverworldBiomeCreator.biome(0.5f, 0.5f).setEnvironmentAttribute(EnvironmentAttributes.BACKGROUND_MUSIC_AUDIO, new BackgroundMusic(SoundEvents.MUSIC_OVERWORLD_LUSH_CAVES)).spawnSettings(lv.build()).generationSettings(lv2.build()).build();
    }

    public static Biome createDripstoneCaves(RegistryEntryLookup<PlacedFeature> featureLookup, RegistryEntryLookup<ConfiguredCarver<?>> carverLookup) {
        SpawnSettings.Builder lv = new SpawnSettings.Builder();
        DefaultBiomeFeatures.addDripstoneCaveMobs(lv);
        GenerationSettings.LookupBackedBuilder lv2 = new GenerationSettings.LookupBackedBuilder(featureLookup, carverLookup);
        OverworldBiomeCreator.addBasicFeatures(lv2);
        DefaultBiomeFeatures.addPlainsTallGrass(lv2);
        DefaultBiomeFeatures.addDefaultOres(lv2, true);
        DefaultBiomeFeatures.addDefaultDisks(lv2);
        DefaultBiomeFeatures.addPlainsFeatures(lv2);
        DefaultBiomeFeatures.addDefaultMushrooms(lv2);
        DefaultBiomeFeatures.addDefaultVegetation(lv2, false);
        DefaultBiomeFeatures.addDripstone(lv2);
        return OverworldBiomeCreator.biome(0.8f, 0.4f).setEnvironmentAttribute(EnvironmentAttributes.BACKGROUND_MUSIC_AUDIO, new BackgroundMusic(SoundEvents.MUSIC_OVERWORLD_DRIPSTONE_CAVES)).spawnSettings(lv.build()).generationSettings(lv2.build()).build();
    }

    public static Biome createDeepDark(RegistryEntryLookup<PlacedFeature> featureLookup, RegistryEntryLookup<ConfiguredCarver<?>> carverLookup) {
        SpawnSettings.Builder lv = new SpawnSettings.Builder();
        GenerationSettings.LookupBackedBuilder lv2 = new GenerationSettings.LookupBackedBuilder(featureLookup, carverLookup);
        lv2.carver(ConfiguredCarvers.CAVE);
        lv2.carver(ConfiguredCarvers.CAVE_EXTRA_UNDERGROUND);
        lv2.carver(ConfiguredCarvers.CANYON);
        DefaultBiomeFeatures.addAmethystGeodes(lv2);
        DefaultBiomeFeatures.addDungeons(lv2);
        DefaultBiomeFeatures.addMineables(lv2);
        DefaultBiomeFeatures.addFrozenTopLayer(lv2);
        DefaultBiomeFeatures.addPlainsTallGrass(lv2);
        DefaultBiomeFeatures.addDefaultOres(lv2);
        DefaultBiomeFeatures.addDefaultDisks(lv2);
        DefaultBiomeFeatures.addPlainsFeatures(lv2);
        DefaultBiomeFeatures.addDefaultMushrooms(lv2);
        DefaultBiomeFeatures.addDefaultVegetation(lv2, false);
        DefaultBiomeFeatures.addSculk(lv2);
        return OverworldBiomeCreator.biome(0.8f, 0.4f).setEnvironmentAttribute(EnvironmentAttributes.BACKGROUND_MUSIC_AUDIO, new BackgroundMusic(SoundEvents.MUSIC_OVERWORLD_DEEP_DARK)).spawnSettings(lv.build()).generationSettings(lv2.build()).build();
    }
}

