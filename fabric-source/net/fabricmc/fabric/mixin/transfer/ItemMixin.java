package net.fabricmc.fabric.mixin.transfer;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.world.item.Item;

import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.impl.transfer.item.ItemVariantCache;
import net.fabricmc.fabric.impl.transfer.item.ItemVariantImpl;

/**
 * Cache the ItemVariant with a null tag inside each Item directly.
 */
@Mixin(Item.class)
public class ItemMixin implements ItemVariantCache {
	@Unique
	@SuppressWarnings("ConstantConditions")
	private final ItemVariant cachedItemVariant = new ItemVariantImpl((Item) (Object) this, DataComponentPatch.EMPTY);

	@Override
	public ItemVariant fabric_getCachedItemVariant() {
		return cachedItemVariant;
	}
}
