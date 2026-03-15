package net.fabricmc.fabric.api.datagen.v1;

/**
 * Provides a callback for setting the sort priority of object keys in generated JSON files.
 */
@FunctionalInterface
public interface JsonKeySortOrderCallback {
	/**
	 * Sets the sort priority for a given object key within generated JSON files.
	 * @param key the key to set priority for
	 * @param priority the priority for the key, where keys with lower priority are sorted before keys with higher priority
	 * @implNote The default priority is 2.
	 * @see net.minecraft.data.DataProvider#FIXED_ORDER_FIELDS
	 */
	void add(String key, int priority);
}
