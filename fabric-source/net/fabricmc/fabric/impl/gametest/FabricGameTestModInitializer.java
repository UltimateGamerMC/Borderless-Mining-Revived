package net.fabricmc.fabric.impl.gametest;

import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.gametest.framework.GameTestInstance;
import net.minecraft.gametest.framework.TestEnvironmentDefinition;
import net.minecraft.resources.RegistryDataLoader;
import net.minecraft.resources.ResourceKey;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;

public final class FabricGameTestModInitializer implements ModInitializer {
	private static final Logger LOGGER = LoggerFactory.getLogger(FabricGameTestModInitializer.class);
	private static TestAnnotationLocator locator = new TestAnnotationLocator(FabricLoader.getInstance());

	@Override
	public void onInitialize() {
		if (!(FabricGameTestRunner.ENABLED || FabricLoader.getInstance().isDevelopmentEnvironment())) {
			// Don't try to load the tests if the game test runner is disabled or we are not in a development environment
			return;
		}

		for (TestAnnotationLocator.TestMethod testMethod : locator.getTestMethods()) {
			LOGGER.debug("Registering test method: {}", testMethod.identifier());
			Registry.register(BuiltInRegistries.TEST_FUNCTION, testMethod.identifier(), testMethod.testFunction());
		}
	}

	public static void registerDynamicEntries(List<RegistryDataLoader.Loader<?>> registriesList) {
		Map<ResourceKey<? extends Registry<?>>, Registry<?>> registries = new IdentityHashMap<>(registriesList.size());

		for (RegistryDataLoader.Loader<?> entry : registriesList) {
			registries.put(entry.registry().key(), entry.registry());
		}

		Registry<GameTestInstance> testInstances = (Registry<GameTestInstance>) registries.get(Registries.TEST_INSTANCE);
		Registry<TestEnvironmentDefinition> testEnvironmentDefinitionRegistry = (Registry<TestEnvironmentDefinition>) Objects.requireNonNull(registries.get(Registries.TEST_ENVIRONMENT));

		for (TestAnnotationLocator.TestMethod testMethod : locator.getTestMethods()) {
			GameTestInstance testInstance = testMethod.testInstance(testEnvironmentDefinitionRegistry);
			Registry.register(testInstances, testMethod.identifier(), testInstance);
		}
	}
}
