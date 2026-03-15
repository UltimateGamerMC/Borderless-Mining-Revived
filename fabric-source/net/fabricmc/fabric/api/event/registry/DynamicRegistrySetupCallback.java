package net.fabricmc.fabric.api.event.registry;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

/**
 * This event gets triggered before a dynamic registry is being loaded.
 * Therefore, this is the ideal place to register callbacks to dynamic registries.
 * For example, the following code is used to register a callback that gets triggered for any registered Biome:
 *
 * <pre>
 * {@code
 * DynamicRegistrySetupCallback.EVENT.register(registryView -> {
 *     registryView.registerEntryAdded(RegistryKeys.BIOME, (rawId, id, object) -> {
 *         // Do something
 *     });
 * });
 * }
 * </pre>
 *
 * @see DynamicRegistryView
 * @see net.minecraft.server.RegistryLayer
 */
@FunctionalInterface
public interface DynamicRegistrySetupCallback {
	void onRegistrySetup(DynamicRegistryView registryView);

	Event<DynamicRegistrySetupCallback> EVENT = EventFactory.createArrayBacked(
			DynamicRegistrySetupCallback.class,
			callbacks -> registryView -> {
				for (DynamicRegistrySetupCallback callback : callbacks) {
					callback.onRegistrySetup(registryView);
				}
			}
	);
}
