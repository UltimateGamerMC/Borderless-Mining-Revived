package net.fabricmc.fabric.api.block.v1;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

/**
 * Contains block tags that add extra functionality to blocks.
 */
public final class BlockFunctionalityTags {
	/**
	 * Blocks in this tag let the player climb open trapdoors above them.
	 *
	 * <p>If a tagged block is a {@link net.minecraft.world.level.block.LadderBlock}, the block state's {@code facing}
	 * property must additionally match the trapdoor's direction, to match how vanilla ladders work.
	 */
	public static final TagKey<Block> CAN_CLIMB_TRAPDOOR_ABOVE = create("can_climb_trapdoor_above");

	private BlockFunctionalityTags() {
	}

	private static TagKey<Block> create(String name) {
		return TagKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath("fabric", name));
	}
}
