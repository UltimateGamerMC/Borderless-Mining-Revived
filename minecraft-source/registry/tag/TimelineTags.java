/*
 * External method calls:
 *   Lnet/minecraft/util/Identifier;ofVanilla(Ljava/lang/String;)Lnet/minecraft/util/Identifier;
 *   Lnet/minecraft/registry/tag/TagKey;of(Lnet/minecraft/registry/RegistryKey;Lnet/minecraft/util/Identifier;)Lnet/minecraft/registry/tag/TagKey;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/registry/tag/TimelineTags;of(Ljava/lang/String;)Lnet/minecraft/registry/tag/TagKey;
 */
package net.minecraft.registry.tag;

import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.world.attribute.timeline.Timeline;

public interface TimelineTags {
    public static final TagKey<Timeline> UNIVERSAL = TimelineTags.of("universal");
    public static final TagKey<Timeline> IN_OVERWORLD = TimelineTags.of("in_overworld");
    public static final TagKey<Timeline> IN_NETHER = TimelineTags.of("in_nether");
    public static final TagKey<Timeline> IN_END = TimelineTags.of("in_end");

    private static TagKey<Timeline> of(String name) {
        return TagKey.of(RegistryKeys.TIMELINE, Identifier.ofVanilla(name));
    }
}

