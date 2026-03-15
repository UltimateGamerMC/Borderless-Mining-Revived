/*
 * External method calls:
 *   Lnet/minecraft/util/Identifier;ofVanilla(Ljava/lang/String;)Lnet/minecraft/util/Identifier;
 *   Lnet/minecraft/registry/RegistryKey;of(Lnet/minecraft/registry/RegistryKey;Lnet/minecraft/util/Identifier;)Lnet/minecraft/registry/RegistryKey;
 *   Lnet/minecraft/registry/Registerable;register(Lnet/minecraft/registry/RegistryKey;Ljava/lang/Object;)Lnet/minecraft/registry/entry/RegistryEntry$Reference;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/test/TestEnvironments;of(Ljava/lang/String;)Lnet/minecraft/registry/RegistryKey;
 */
package net.minecraft.test;

import java.util.List;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.test.TestEnvironmentDefinition;
import net.minecraft.util.Identifier;

public interface TestEnvironments {
    public static final String DEFAULT_ID = "default";
    public static final RegistryKey<TestEnvironmentDefinition> DEFAULT = TestEnvironments.of("default");

    private static RegistryKey<TestEnvironmentDefinition> of(String id) {
        return RegistryKey.of(RegistryKeys.TEST_ENVIRONMENT, Identifier.ofVanilla(id));
    }

    public static void bootstrap(Registerable<TestEnvironmentDefinition> registry) {
        registry.register(DEFAULT, new TestEnvironmentDefinition.AllOf(List.of()));
    }
}

