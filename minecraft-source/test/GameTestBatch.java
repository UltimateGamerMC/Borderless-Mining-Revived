package net.minecraft.test;

import java.util.Collection;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.test.GameTestState;
import net.minecraft.test.TestEnvironmentDefinition;

public record GameTestBatch(int index, Collection<GameTestState> states, RegistryEntry<TestEnvironmentDefinition> environment) {
    public GameTestBatch {
        if (testFunctions.isEmpty()) {
            throw new IllegalArgumentException("A GameTestBatch must include at least one GameTestInfo!");
        }
    }
}

