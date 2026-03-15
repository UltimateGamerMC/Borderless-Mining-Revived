/*
 * External method calls:
 *   Lnet/minecraft/util/Identifier;ofVanilla(Ljava/lang/String;)Lnet/minecraft/util/Identifier;
 *   Lnet/minecraft/registry/RegistryKey;of(Lnet/minecraft/registry/RegistryKey;Lnet/minecraft/util/Identifier;)Lnet/minecraft/registry/RegistryKey;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/test/BuiltinTestFunctions;addProvider(Lnet/minecraft/test/TestFunctionProvider;)V
 *   Lnet/minecraft/test/BuiltinTestFunctions;registerAll(Lnet/minecraft/registry/Registry;)V
 *   Lnet/minecraft/test/BuiltinTestFunctions;of(Ljava/lang/String;)Lnet/minecraft/registry/RegistryKey;
 */
package net.minecraft.test;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.test.TestContext;
import net.minecraft.test.TestFunctionProvider;
import net.minecraft.util.Identifier;

public class BuiltinTestFunctions
extends TestFunctionProvider {
    public static final RegistryKey<Consumer<TestContext>> ALWAYS_PASS = BuiltinTestFunctions.of("always_pass");
    public static final Consumer<TestContext> ALWAYS_PASS_FUNCTION = TestContext::complete;

    private static RegistryKey<Consumer<TestContext>> of(String id) {
        return RegistryKey.of(RegistryKeys.TEST_FUNCTION, Identifier.ofVanilla(id));
    }

    public static Consumer<TestContext> registerAndGetDefault(Registry<Consumer<TestContext>> registry) {
        BuiltinTestFunctions.addProvider(new BuiltinTestFunctions());
        BuiltinTestFunctions.registerAll(registry);
        return ALWAYS_PASS_FUNCTION;
    }

    @Override
    public void register(BiConsumer<RegistryKey<Consumer<TestContext>>, Consumer<TestContext>> registry) {
        registry.accept(ALWAYS_PASS, ALWAYS_PASS_FUNCTION);
    }
}

