package net.fabricmc.fabric.impl.item;

import org.jspecify.annotations.Nullable;

import net.fabricmc.fabric.api.item.v1.CustomDamageHandler;
import net.fabricmc.fabric.api.item.v1.EquipmentSlotProvider;

public interface ItemExtensions {
	@Nullable EquipmentSlotProvider fabric_getEquipmentSlotProvider();
	void fabric_setEquipmentSlotProvider(EquipmentSlotProvider equipmentSlotProvider);
	@Nullable CustomDamageHandler fabric_getCustomDamageHandler();
	void fabric_setCustomDamageHandler(CustomDamageHandler handler);
}
