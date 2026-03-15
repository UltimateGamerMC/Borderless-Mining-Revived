package net.fabricmc.fabric.impl.transfer.item;

import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;

/**
 * Implemented by items to cache the ItemVariant with a null tag inside the Item object directly.
 */
public interface ItemVariantCache {
	ItemVariant fabric_getCachedItemVariant();
}
