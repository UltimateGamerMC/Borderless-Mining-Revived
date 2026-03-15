/*
 * External method calls:
 *   Lnet/minecraft/registry/CombinedDynamicRegistries;with(Ljava/lang/Object;[Lnet/minecraft/registry/DynamicRegistryManager$Immutable;)Lnet/minecraft/registry/CombinedDynamicRegistries;
 *   Lnet/minecraft/registry/DynamicRegistryManager;of(Lnet/minecraft/registry/Registry;)Lnet/minecraft/registry/DynamicRegistryManager$Immutable;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/registry/ServerDynamicRegistryType;method_45140()[Lnet/minecraft/registry/ServerDynamicRegistryType;
 *   Lnet/minecraft/registry/ServerDynamicRegistryType;values()[Lnet/minecraft/registry/ServerDynamicRegistryType;
 */
package net.minecraft.registry;

import java.util.List;
import net.minecraft.registry.CombinedDynamicRegistries;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.Registries;

public enum ServerDynamicRegistryType {
    STATIC,
    WORLDGEN,
    DIMENSIONS,
    RELOADABLE;

    private static final List<ServerDynamicRegistryType> VALUES;
    private static final DynamicRegistryManager.Immutable STATIC_REGISTRY_MANAGER;

    public static CombinedDynamicRegistries<ServerDynamicRegistryType> createCombinedDynamicRegistries() {
        return new CombinedDynamicRegistries<ServerDynamicRegistryType>(VALUES).with(STATIC, STATIC_REGISTRY_MANAGER);
    }

    static {
        VALUES = List.of(ServerDynamicRegistryType.values());
        STATIC_REGISTRY_MANAGER = DynamicRegistryManager.of(Registries.REGISTRIES);
    }
}

