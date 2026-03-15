/*
 * External method calls:
 *   Lnet/minecraft/registry/RegistryKey;of(Lnet/minecraft/registry/RegistryKey;Lnet/minecraft/util/Identifier;)Lnet/minecraft/registry/RegistryKey;
 *   Lnet/minecraft/entity/spawn/SpawnConditionSelectors;createFallback(I)Lnet/minecraft/entity/spawn/SpawnConditionSelectors;
 *   Lnet/minecraft/entity/spawn/SpawnConditionSelectors;createSingle(Lnet/minecraft/entity/spawn/SpawnCondition;I)Lnet/minecraft/entity/spawn/SpawnConditionSelectors;
 *   Lnet/minecraft/util/Identifier;ofVanilla(Ljava/lang/String;)Lnet/minecraft/util/Identifier;
 *   Lnet/minecraft/registry/Registerable;register(Lnet/minecraft/registry/RegistryKey;Ljava/lang/Object;)Lnet/minecraft/registry/entry/RegistryEntry$Reference;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/entity/passive/CowVariants;register(Lnet/minecraft/registry/Registerable;Lnet/minecraft/registry/RegistryKey;Lnet/minecraft/entity/passive/CowVariant$Model;Ljava/lang/String;Lnet/minecraft/entity/spawn/SpawnConditionSelectors;)V
 *   Lnet/minecraft/entity/passive/CowVariants;register(Lnet/minecraft/registry/Registerable;Lnet/minecraft/registry/RegistryKey;Lnet/minecraft/entity/passive/CowVariant$Model;Ljava/lang/String;Lnet/minecraft/registry/tag/TagKey;)V
 *   Lnet/minecraft/entity/passive/CowVariants;of(Lnet/minecraft/util/Identifier;)Lnet/minecraft/registry/RegistryKey;
 */
package net.minecraft.entity.passive;

import net.minecraft.entity.passive.AnimalTemperature;
import net.minecraft.entity.passive.CowVariant;
import net.minecraft.entity.spawn.BiomeSpawnCondition;
import net.minecraft.entity.spawn.SpawnConditionSelectors;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.registry.tag.BiomeTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.ModelAndTexture;
import net.minecraft.world.biome.Biome;

public class CowVariants {
    public static final RegistryKey<CowVariant> TEMPERATE = CowVariants.of(AnimalTemperature.TEMPERATE);
    public static final RegistryKey<CowVariant> WARM = CowVariants.of(AnimalTemperature.WARM);
    public static final RegistryKey<CowVariant> COLD = CowVariants.of(AnimalTemperature.COLD);
    public static final RegistryKey<CowVariant> DEFAULT = TEMPERATE;

    private static RegistryKey<CowVariant> of(Identifier id) {
        return RegistryKey.of(RegistryKeys.COW_VARIANT, id);
    }

    public static void bootstrap(Registerable<CowVariant> registry) {
        CowVariants.register(registry, TEMPERATE, CowVariant.Model.NORMAL, "temperate_cow", SpawnConditionSelectors.createFallback(0));
        CowVariants.register(registry, WARM, CowVariant.Model.WARM, "warm_cow", BiomeTags.SPAWNS_WARM_VARIANT_FARM_ANIMALS);
        CowVariants.register(registry, COLD, CowVariant.Model.COLD, "cold_cow", BiomeTags.SPAWNS_COLD_VARIANT_FARM_ANIMALS);
    }

    private static void register(Registerable<CowVariant> registry, RegistryKey<CowVariant> key, CowVariant.Model model, String textureName, TagKey<Biome> biomes) {
        RegistryEntryList.Named<Biome> lv = registry.getRegistryLookup(RegistryKeys.BIOME).getOrThrow(biomes);
        CowVariants.register(registry, key, model, textureName, SpawnConditionSelectors.createSingle(new BiomeSpawnCondition(lv), 1));
    }

    private static void register(Registerable<CowVariant> registry, RegistryKey<CowVariant> key, CowVariant.Model model, String textureName, SpawnConditionSelectors spawnConditions) {
        Identifier lv = Identifier.ofVanilla("entity/cow/" + textureName);
        registry.register(key, new CowVariant(new ModelAndTexture<CowVariant.Model>(model, lv), spawnConditions));
    }
}

