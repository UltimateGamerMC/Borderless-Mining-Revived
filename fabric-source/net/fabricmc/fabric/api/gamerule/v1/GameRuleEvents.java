package net.fabricmc.fabric.api.gamerule.v1;

import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.gamerules.GameRule;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.impl.gamerule.GameRuleEventsImpl;

/**
 * Provides events for updating {@link GameRule}s.
 */
public final class GameRuleEvents {
	private GameRuleEvents() {
	}

	public static <T> Event<ValueUpdate<T>> changeCallback(GameRule<T> rule) {
		return GameRuleEventsImpl.changeCallback(rule);
	}

	/**
	 * A functional interface used as a change callback for {@link GameRule} updates.
	 * @param <T> the type of the value
	 */
	@FunctionalInterface
	public interface ValueUpdate<T> {
		/**
		 * Called when a GameRule's value is updated in the server.
		 * @param value the updated value
		 * @param server the server
		 * @see MinecraftServer#onGameRuleChanged(GameRule, Object)
		 */
		void onGameRuleUpdated(
				T value,
				MinecraftServer server
		);
	}
}
