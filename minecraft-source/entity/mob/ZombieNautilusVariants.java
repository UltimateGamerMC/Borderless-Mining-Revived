/*
 * External method calls:
 *   Lnet/minecraft/registry/RegistryKey;of(Lnet/minecraft/registry/RegistryKey;Lnet/minecraft/util/Identifier;)Lnet/minecraft/registry/RegistryKey;
 *   Lnet/minecraft/entity/spawn/SpawnConditionSelectors;createFallback(I)Lnet/minecraft/entity/spawn/SpawnConditionSelectors;
 *   Lnet/minecraft/entity/spawn/SpawnConditionSelectors;createSingle(Lnet/minecraft/entity/spawn/SpawnCondition;I)Lnet/minecraft/entity/spawn/SpawnConditionSelectors;
 *   Lnet/minecraft/util/Identifier;ofVanilla(Ljava/lang/String;)Lnet/minecraft/util/Identifier;
 *   Lnet/minecraft/registry/Registerable;register(Lnet/minecraft/registry/RegistryKey;Ljava/lang/Object;)Lnet/minecraft/registry/entry/RegistryEntry$Reference;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/entity/mob/ZombieNautilusVariants;register(Lnet/minecraft/registry/Registerable;Lnet/minecraft/registry/RegistryKey;Lnet/minecraft/entity/mob/ZombieNautilusVariant$Model;Ljava/lang/String;Lnet/minecraft/entity/spawn/SpawnConditionSelectors;)V
 *   Lnet/minecraft/entity/mob/ZombieNautilusVariants;register(Lnet/minecraft/registry/Registerable;Lnet/minecraft/registry/RegistryKey;Lnet/minecraft/entity/mob/ZombieNautilusVariant$Model;Ljava/lang/String;Lnet/minecraft/registry/tag/TagKey;)V
 *   Lnet/minecraft/entity/mob/ZombieNautilusVariants;of(Lnet/minecraft/util/Identifier;)Lnet/minecraft/registry/RegistryKey;
 */
package net.minecraft.entity.mob;

import net.minecraft.entity.mob.ZombieNautilusVariant;
import net.minecraft.entity.passive.AnimalTemperature;
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

public class ZombieNautilusVariants {
    public static final RegistryKey<ZombieNautilusVariant> TEMPERATE = ZombieNautilusVariants.of(AnimalTemperature.TEMPERATE);
    public static final RegistryKey<ZombieNautilusVariant> WARM = ZombieNautilusVariants.of(AnimalTemperature.WARM);
    public static final RegistryKey<ZombieNautilusVariant> DEFAULT = TEMPERATE;

    private static RegistryKey<ZombieNautilusVariant> of(Identifier id) {
        return RegistryKey.of(RegistryKeys.ZOMBIE_NAUTILUS_VARIANT, id);
    }

    public static void bootstrap(Registerable<ZombieNautilusVariant> registry) {
        ZombieNautilusVariants.register(registry, TEMPERATE, ZombieNautilusVariant.Model.NORMAL, "zombie_nautilus", SpawnConditionSelectors.createFallback(0));
        ZombieNautilusVariants.register(registry, WARM, ZombieNautilusVariant.Model.WARM, "zombie_nautilus_coral", BiomeTags.SPAWNS_CORAL_VARIANT_ZOMBIE_NAUTILUS);
    }

    private static void register(Registerable<ZombieNautilusVariant> registry, RegistryKey<ZombieNautilusVariant> key, ZombieNautilusVariant.Model model, String textureName, TagKey<Biome> biomes) {
        RegistryEntryList.Named<Biome> lv = registry.getRegistryLookup(RegistryKeys.BIOME).getOrThrow(biomes);
        ZombieNautilusVariants.register(registry, key, model, textureName, SpawnConditionSelectors.createSingle(new BiomeSpawnCondition(lv), 1));
    }

    private static void register(Registerable<ZombieNautilusVariant> registry, RegistryKey<ZombieNautilusVariant> key, ZombieNautilusVariant.Model model, String textureName, SpawnConditionSelectors spawnConditions) {
        Identifier lv = Identifier.ofVanilla("entity/nautilus/" + textureName);
        registry.register(key, new ZombieNautilusVariant(new ModelAndTexture<ZombieNautilusVariant.Model>(model, lv), spawnConditions));
    }
}

