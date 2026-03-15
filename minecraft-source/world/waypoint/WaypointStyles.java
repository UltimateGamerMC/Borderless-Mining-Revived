/*
 * External method calls:
 *   Lnet/minecraft/util/Identifier;ofVanilla(Ljava/lang/String;)Lnet/minecraft/util/Identifier;
 *   Lnet/minecraft/registry/RegistryKey;of(Lnet/minecraft/registry/RegistryKey;Lnet/minecraft/util/Identifier;)Lnet/minecraft/registry/RegistryKey;
 *   Lnet/minecraft/registry/RegistryKey;ofRegistry(Lnet/minecraft/util/Identifier;)Lnet/minecraft/registry/RegistryKey;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/world/waypoint/WaypointStyles;of(Ljava/lang/String;)Lnet/minecraft/registry/RegistryKey;
 */
package net.minecraft.world.waypoint;

import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;
import net.minecraft.world.waypoint.WaypointStyle;

public interface WaypointStyles {
    public static final RegistryKey<? extends Registry<WaypointStyle>> REGISTRY = RegistryKey.ofRegistry(Identifier.ofVanilla("waypoint_style_asset"));
    public static final RegistryKey<WaypointStyle> DEFAULT = WaypointStyles.of("default");
    public static final RegistryKey<WaypointStyle> BOWTIE = WaypointStyles.of("bowtie");

    public static RegistryKey<WaypointStyle> of(String id) {
        return RegistryKey.of(REGISTRY, Identifier.ofVanilla(id));
    }
}

