package net.fabricmc.fabric.api.attachment.v1;

import java.util.function.BiPredicate;

import org.jetbrains.annotations.ApiStatus;

import net.minecraft.server.level.ServerPlayer;

/**
 * A predicate that determines, for a specific attachment type, whether the data should be synchronized with a
 * player's client, given the player's {@link ServerPlayer} and the {@linkplain AttachmentTarget} the data is linked to.
 *
 * <p>The class extends {@link BiPredicate} to allow for custom predicates, outside the ones provided by methods.</p>
 */
@ApiStatus.NonExtendable
@FunctionalInterface
public interface AttachmentSyncPredicate extends BiPredicate<AttachmentTarget, ServerPlayer> {
	/**
	 * @return a predicate that syncs an attachment with all clients
	 */
	static AttachmentSyncPredicate all() {
		return (t, p) -> true;
	}

	/**
	 * @return a predicate that syncs an attachment only with the target it is attached to, when that is a player. If the
	 * target isn't a player, the attachment will be synced with no clients.
	 */
	static AttachmentSyncPredicate targetOnly() {
		return (target, player) -> target == player;
	}

	/**
	 * @return a predicate that syncs an attachment with every client except the target it is attached to, when that is a player.
	 * When the target isn't a player, the attachment will be synced with all clients.
	 */
	static AttachmentSyncPredicate allButTarget() {
		return (target, player) -> target != player;
	}
}
