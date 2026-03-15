/*
 * External method calls:
 *   Lnet/minecraft/util/Identifier;ofVanilla(Ljava/lang/String;)Lnet/minecraft/util/Identifier;
 *   Lnet/minecraft/registry/tag/TagKey;of(Lnet/minecraft/registry/RegistryKey;Lnet/minecraft/util/Identifier;)Lnet/minecraft/registry/tag/TagKey;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/registry/tag/FlatLevelGeneratorPresetTags;of(Ljava/lang/String;)Lnet/minecraft/registry/tag/TagKey;
 */
package net.minecraft.registry.tag;

import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.FlatLevelGeneratorPreset;

public class FlatLevelGeneratorPresetTags {
    public static final TagKey<FlatLevelGeneratorPreset> VISIBLE = FlatLevelGeneratorPresetTags.of("visible");

    private FlatLevelGeneratorPresetTags() {
    }

    private static TagKey<FlatLevelGeneratorPreset> of(String id) {
        return TagKey.of(RegistryKeys.FLAT_LEVEL_GENERATOR_PRESET, Identifier.ofVanilla(id));
    }
}

