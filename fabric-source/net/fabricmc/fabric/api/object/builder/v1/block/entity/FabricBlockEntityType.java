package net.fabricmc.fabric.api.object.builder.v1.block.entity;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;

/**
 * General-purpose Fabric-provided extensions for {@link BlockEntityType}.
 *
 * <p>Note: This interface is automatically implemented on {@link BlockEntityType} via Mixin and interface injection.
 */
public interface FabricBlockEntityType {
	/**
	 * Adds a block to the list of blocks that this block entity type can be used with.
	 *
	 * @param block the {@link Block} to add
	 */
	default void addSupportedBlock(Block block) {
		throw new AssertionError("Implemented in Mixin");
	}
}
