package net.fabricmc.fabric.mixin.transfer;

import com.llamalad7.mixinextras.sugar.Local;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.block.CrafterBlock;
import net.minecraft.world.level.block.entity.CrafterBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import net.fabricmc.fabric.api.transfer.v1.item.ItemStorage;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;

@Mixin(CrafterBlock.class)
public class CrafterBlockMixin {
	// Inject after vanilla's attempts to insert the stack into an inventory.
	@Inject(method = "dispenseItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;isEmpty()Z", shift = At.Shift.BEFORE))
	private void transferOrSpawnStack(ServerLevel world, BlockPos pos, CrafterBlockEntity blockEntity, ItemStack inputStack, BlockState state, RecipeHolder<CraftingRecipe> recipe, CallbackInfo ci, @Local Direction direction, @Local Container inventory, @Local(ordinal = 1) ItemStack itemStack) {
		if (inventory != null) {
			// Vanilla already found and tested an inventory, nothing else to do even if it failed to insert.
			return;
		}

		if (itemStack.isEmpty()) {
			// Nothing left to do, in theory should never get here.
			return;
		}

		final Storage<ItemVariant> target = ItemStorage.SIDED.find(world, pos.relative(direction), direction.getOpposite());

		if (target != null) {
			// Attempt to move the entire stack, and decrement the size of success moves.
			try (Transaction transaction = Transaction.openOuter()) {
				long moved = target.insert(ItemVariant.of(itemStack), inputStack.getCount(), transaction);

				if (moved > 0) {
					itemStack.shrink((int) moved);
					transaction.commit();
				}
			}
		}

		// Any remaining will be dropped in the world by vanilla logic
	}
}
