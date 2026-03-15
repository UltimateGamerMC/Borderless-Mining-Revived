package net.fabricmc.fabric.api.event.registry;

import org.jetbrains.annotations.ApiStatus;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

import net.fabricmc.fabric.impl.registry.sync.RegistryAttributeImpl;

@ApiStatus.NonExtendable
public interface RegistryAttributeHolder {
	static RegistryAttributeHolder get(ResourceKey<?> registryKey) {
		return RegistryAttributeImpl.getHolder(registryKey);
	}

	static RegistryAttributeHolder get(Registry<?> registry) {
		return get(registry.key());
	}

	RegistryAttributeHolder addAttribute(RegistryAttribute attribute);

	boolean hasAttribute(RegistryAttribute attribute);
}
