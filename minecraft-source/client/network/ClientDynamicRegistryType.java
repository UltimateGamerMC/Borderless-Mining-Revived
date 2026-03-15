/*
 * External method calls:
 *   Lnet/minecraft/registry/CombinedDynamicRegistries;with(Ljava/lang/Object;[Lnet/minecraft/registry/DynamicRegistryManager$Immutable;)Lnet/minecraft/registry/CombinedDynamicRegistries;
 *   Lnet/minecraft/registry/DynamicRegistryManager;of(Lnet/minecraft/registry/Registry;)Lnet/minecraft/registry/DynamicRegistryManager$Immutable;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/client/network/ClientDynamicRegistryType;method_45739()[Lnet/minecraft/client/network/ClientDynamicRegistryType;
 *   Lnet/minecraft/client/network/ClientDynamicRegistryType;values()[Lnet/minecraft/client/network/ClientDynamicRegistryType;
 */
package net.minecraft.client.network;

import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.registry.CombinedDynamicRegistries;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.Registries;

@Environment(value=EnvType.CLIENT)
public enum ClientDynamicRegistryType {
    STATIC,
    REMOTE;

    private static final List<ClientDynamicRegistryType> VALUES;
    private static final DynamicRegistryManager.Immutable STATIC_REGISTRY_MANAGER;

    public static CombinedDynamicRegistries<ClientDynamicRegistryType> createCombinedDynamicRegistries() {
        return new CombinedDynamicRegistries<ClientDynamicRegistryType>(VALUES).with(STATIC, STATIC_REGISTRY_MANAGER);
    }

    static {
        VALUES = List.of(ClientDynamicRegistryType.values());
        STATIC_REGISTRY_MANAGER = DynamicRegistryManager.of(Registries.REGISTRIES);
    }
}

