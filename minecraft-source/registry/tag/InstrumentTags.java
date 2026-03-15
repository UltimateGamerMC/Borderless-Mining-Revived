/*
 * External method calls:
 *   Lnet/minecraft/util/Identifier;ofVanilla(Ljava/lang/String;)Lnet/minecraft/util/Identifier;
 *   Lnet/minecraft/registry/tag/TagKey;of(Lnet/minecraft/registry/RegistryKey;Lnet/minecraft/util/Identifier;)Lnet/minecraft/registry/tag/TagKey;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/registry/tag/InstrumentTags;of(Ljava/lang/String;)Lnet/minecraft/registry/tag/TagKey;
 */
package net.minecraft.registry.tag;

import net.minecraft.item.Instrument;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

public interface InstrumentTags {
    public static final TagKey<Instrument> REGULAR_GOAT_HORNS = InstrumentTags.of("regular_goat_horns");
    public static final TagKey<Instrument> SCREAMING_GOAT_HORNS = InstrumentTags.of("screaming_goat_horns");
    public static final TagKey<Instrument> GOAT_HORNS = InstrumentTags.of("goat_horns");

    private static TagKey<Instrument> of(String id) {
        return TagKey.of(RegistryKeys.INSTRUMENT, Identifier.ofVanilla(id));
    }
}

