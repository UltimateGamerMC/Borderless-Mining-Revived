package net.minecraft.test;

import java.util.stream.Stream;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.test.TestInstance;

@FunctionalInterface
public interface TestInstanceFinder {
    public Stream<RegistryEntry.Reference<TestInstance>> findTests();
}

