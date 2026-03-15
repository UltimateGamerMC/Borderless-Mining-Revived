package net.fabricmc.fabric.impl.registry.sync;

import java.util.EnumSet;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.jetbrains.annotations.VisibleForTesting;

import net.minecraft.resources.ResourceKey;

import net.fabricmc.fabric.api.event.registry.RegistryAttribute;
import net.fabricmc.fabric.api.event.registry.RegistryAttributeHolder;
import net.fabricmc.loader.api.FabricLoader;

public final class RegistryAttributeImpl implements RegistryAttributeHolder {
	private static final Map<ResourceKey<?>, RegistryAttributeHolder> HOLDER_MAP = new ConcurrentHashMap<>();

	public static RegistryAttributeHolder getHolder(ResourceKey<?> registryKey) {
		return HOLDER_MAP.computeIfAbsent(registryKey, key -> new RegistryAttributeImpl());
	}

	private final EnumSet<RegistryAttribute> attributes = EnumSet.noneOf(RegistryAttribute.class);

	private RegistryAttributeImpl() {
	}

	@Override
	public RegistryAttributeHolder addAttribute(RegistryAttribute attribute) {
		attributes.add(attribute);
		return this;
	}

	@VisibleForTesting
	public void removeAttribute(RegistryAttribute attribute) {
		if (!FabricLoader.getInstance().isDevelopmentEnvironment()) {
			throw new AssertionError();
		}

		attributes.remove(attribute);
	}

	@Override
	public boolean hasAttribute(RegistryAttribute attribute) {
		return attributes.contains(attribute);
	}

	public EnumSet<RegistryAttribute> getAttributes() {
		return attributes;
	}
}
