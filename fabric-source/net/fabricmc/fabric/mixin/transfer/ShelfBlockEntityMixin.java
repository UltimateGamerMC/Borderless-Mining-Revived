package net.fabricmc.fabric.mixin.transfer;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.ShelfBlockEntity;

import net.fabricmc.fabric.impl.transfer.item.SpecialLogicAccess;
import net.fabricmc.fabric.impl.transfer.item.SpecialLogicInventory;

@Mixin(ShelfBlockEntity.class)
public abstract class ShelfBlockEntityMixin implements SpecialLogicInventory, SpecialLogicAccess {
	@Unique
	boolean fabric_suppressSpecialLogic;

	@Override
	public void fabric_setSuppress(boolean suppress) {
		fabric_suppressSpecialLogic = suppress;
	}

	@Override
	public boolean fabric_shouldSuppressSpecialLogic() {
		return fabric_suppressSpecialLogic;
	}

	@Override
	public void fabric_onFinalCommit(int slot, ItemStack oldStack, ItemStack newStack) {
	}
}
