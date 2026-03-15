package net.fabricmc.fabric.mixin.transfer;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import net.fabricmc.fabric.impl.transfer.item.SpecialLogicInventory;

/**
 * Defer cook time updates for furnaces, so that aborted transactions don't reset the cook time.
 */
@Mixin(AbstractFurnaceBlockEntity.class)
public abstract class AbstractFurnaceBlockEntityMixin extends BaseContainerBlockEntity implements SpecialLogicInventory {
	@Shadow
	protected NonNullList<ItemStack> items;
	@Shadow
	int cookingTimer;
	@Shadow
	int cookingTotalTime;
	@Unique
	private boolean fabric_suppressSpecialLogic = false;

	protected AbstractFurnaceBlockEntityMixin(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState) {
		super(blockEntityType, blockPos, blockState);
		throw new AssertionError();
	}

	@Inject(at = @At("HEAD"), method = "setItem", cancellable = true)
	public void setStackSuppressUpdate(int slot, ItemStack stack, CallbackInfo ci) {
		if (fabric_suppressSpecialLogic) {
			items.set(slot, stack);
			ci.cancel();
		}
	}

	@Override
	public void fabric_setSuppress(boolean suppress) {
		fabric_suppressSpecialLogic = suppress;
	}

	@Override
	public void fabric_onFinalCommit(int slot, ItemStack oldStack, ItemStack newStack) {
		if (slot == 0) {
			ItemStack itemStack = oldStack;
			ItemStack stack = newStack;

			// Update cook time if needed. Code taken from AbstractFurnaceBlockEntity#setStack.
			boolean bl = !stack.isEmpty() && ItemStack.isSameItemSameComponents(stack, itemStack);

			if (!bl && this.level instanceof ServerLevel world) {
				this.cookingTotalTime = getTotalCookTime(world, (AbstractFurnaceBlockEntity) (Object) this);
				this.cookingTimer = 0;
			}
		}
	}

	@Shadow
	private static int getTotalCookTime(ServerLevel world, AbstractFurnaceBlockEntity abstractFurnaceBlockEntity) {
		throw new AssertionError();
	}
}
