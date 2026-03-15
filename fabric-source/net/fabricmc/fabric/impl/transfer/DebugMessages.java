package net.fabricmc.fabric.impl.transfer;

import org.jspecify.annotations.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

public final class DebugMessages {
	public static String forGlobalPos(@Nullable Level world, BlockPos pos) {
		String dimension = world != null ? world.dimensionTypeRegistration().getRegisteredName() : "<no dimension>";
		return dimension + "@" + pos.toShortString();
	}

	public static String forPlayer(Player player) {
		return player.getDisplayName() + "/" + player.getStringUUID();
	}

	public static String forInventory(@Nullable Container inventory) {
		if (inventory == null) {
			return "~~NULL~~"; // like in crash reports
		} else if (inventory instanceof Inventory playerInventory) {
			return forPlayer(playerInventory.player);
		} else {
			String result = inventory.toString();

			if (inventory instanceof BlockEntity blockEntity) {
				result += " (%s, %s)".formatted(blockEntity.getBlockState(), forGlobalPos(blockEntity.getLevel(), blockEntity.getBlockPos()));
			}

			return result;
		}
	}
}
