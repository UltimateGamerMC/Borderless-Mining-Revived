package net.fabricmc.fabric.api.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import net.fabricmc.fabric.api.event.player.AttackBlockCallback;

/**
 * Convenience interface for blocks which listen to "break interactions" (left-click).
 *
 * @deprecated Use {@link AttackBlockCallback} instead and check for the block.
 * This gives more control over the different cancellation outcomes.
 */
@Deprecated
public interface BlockAttackInteractionAware {
	/**
	 * @return True if the block accepted the player and it should no longer be processed.
	 */
	boolean onAttackInteraction(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, Direction direction);
}
