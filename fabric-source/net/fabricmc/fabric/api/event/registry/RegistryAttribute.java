package net.fabricmc.fabric.api.event.registry;

public enum RegistryAttribute {
	/**
	 * Registry will be synced to the client when modded.
	 */
	SYNCED,

	/**
	 * Registry has been modded.
	 */
	MODDED,

	/**
	 * Registry is optional, any connecting client will not be disconnected if the registry is not present.
	 */
	OPTIONAL
}
