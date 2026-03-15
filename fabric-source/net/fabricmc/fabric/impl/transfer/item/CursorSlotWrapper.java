package net.fabricmc.fabric.impl.transfer.item;

import java.util.Map;

import com.google.common.collect.MapMaker;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;

import net.fabricmc.fabric.api.transfer.v1.item.base.SingleStackStorage;

/**
 * Wrapper around the cursor slot of a screen handler.
 */
public class CursorSlotWrapper extends SingleStackStorage {
	private static final Map<AbstractContainerMenu, CursorSlotWrapper> WRAPPERS = new MapMaker().weakValues().makeMap();

	public static CursorSlotWrapper get(AbstractContainerMenu screenHandler) {
		return WRAPPERS.computeIfAbsent(screenHandler, CursorSlotWrapper::new);
	}

	private final AbstractContainerMenu screenHandler;

	private CursorSlotWrapper(AbstractContainerMenu screenHandler) {
		this.screenHandler = screenHandler;
	}

	@Override
	protected ItemStack getStack() {
		return screenHandler.getCarried();
	}

	@Override
	protected void setStack(ItemStack stack) {
		screenHandler.setCarried(stack);
	}

	@Override
	public String toString() {
		return "CursorSlotWrapper[" + screenHandler + "/" + BuiltInRegistries.MENU.getKey(screenHandler.getType()) + "]";
	}
}
