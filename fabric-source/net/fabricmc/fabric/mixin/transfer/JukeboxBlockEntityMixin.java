package net.fabricmc.fabric.mixin.transfer;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.JukeboxBlockEntity;

import net.fabricmc.fabric.impl.transfer.item.SpecialLogicInventory;

@Mixin(JukeboxBlockEntity.class)
public abstract class JukeboxBlockEntityMixin implements SpecialLogicInventory {
	@Shadow
	private ItemStack item;

	@Shadow
	public abstract void setTheItem(ItemStack stack);

	@Unique
	private boolean fabric_suppressSpecialLogic = false;

	@Override
	public void fabric_setSuppress(boolean suppress) {
		fabric_suppressSpecialLogic = suppress;
	}

	@Inject(method = "setTheItem", at = @At("HEAD"), cancellable = true)
	private void setStackBypass(ItemStack stack, CallbackInfo ci) {
		if (fabric_suppressSpecialLogic) {
			item = stack;
			ci.cancel();
		}
	}

	@Override
	public void fabric_onFinalCommit(int slot, ItemStack oldStack, ItemStack newStack) {
		// Call setStack again without suppressing vanilla logic,
		// where now the record will actually getting played/stopped.
		setTheItem(newStack);
	}
}
