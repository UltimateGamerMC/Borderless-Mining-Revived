/*
 * External method calls:
 *   Lnet/minecraft/registry/Registry;register(Lnet/minecraft/registry/Registry;Lnet/minecraft/registry/RegistryKey;Ljava/lang/Object;)Ljava/lang/Object;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/test/TestFunctionProvider;register(Ljava/util/function/BiConsumer;)V
 */
package net.minecraft.test;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.test.TestContext;

public abstract class TestFunctionProvider {
    private static final List<TestFunctionProvider> PROVIDERS = new ArrayList<TestFunctionProvider>();

    public static void addProvider(TestFunctionProvider provider) {
        PROVIDERS.add(provider);
    }

    public static void registerAll(Registry<Consumer<TestContext>> registry) {
        for (TestFunctionProvider lv : PROVIDERS) {
            lv.register((key, value) -> Registry.register(registry, key, value));
        }
    }

    public abstract void register(BiConsumer<RegistryKey<Consumer<TestContext>>, Consumer<TestContext>> var1);
}

