package net.fabricmc.fabric.api.loot.v3;

/**
 * Describes where a loot table has been loaded from.
 */
public enum LootTableSource {
	/**
	 * A loot table loaded from the default data pack.
	 */
	VANILLA(true),

	/**
	 * A loot table loaded from mods' bundled resources.
	 *
	 * <p>This includes the additional builtin data packs registered by mods
	 * with Fabric Resource Loader.
	 */
	MOD(true),

	/**
	 * A loot table loaded from an external data pack.
	 */
	DATA_PACK(false),

	/**
	 * A loot table created in {@link LootTableEvents#REPLACE}.
	 */
	REPLACED(false);

	private final boolean builtin;

	LootTableSource(boolean builtin) {
		this.builtin = builtin;
	}

	/**
	 * Returns whether this loot table source is builtin
	 * and bundled in the vanilla or mod resources.
	 *
	 * <p>{@link #VANILLA} and {@link #MOD} are builtin.
	 *
	 * @return {@code true} if builtin, {@code false} otherwise
	 */
	public boolean isBuiltin() {
		return builtin;
	}
}
