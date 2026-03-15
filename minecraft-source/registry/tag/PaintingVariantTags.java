/*
 * External method calls:
 *   Lnet/minecraft/util/Identifier;ofVanilla(Ljava/lang/String;)Lnet/minecraft/util/Identifier;
 *   Lnet/minecraft/registry/tag/TagKey;of(Lnet/minecraft/registry/RegistryKey;Lnet/minecraft/util/Identifier;)Lnet/minecraft/registry/tag/TagKey;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/registry/tag/PaintingVariantTags;of(Ljava/lang/String;)Lnet/minecraft/registry/tag/TagKey;
 */
package net.minecraft.registry.tag;

import net.minecraft.entity.decoration.painting.PaintingVariant;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

public class PaintingVariantTags {
    public static final TagKey<PaintingVariant> PLACEABLE = PaintingVariantTags.of("placeable");

    private PaintingVariantTags() {
    }

    private static TagKey<PaintingVariant> of(String id) {
        return TagKey.of(RegistryKeys.PAINTING_VARIANT, Identifier.ofVanilla(id));
    }
}

