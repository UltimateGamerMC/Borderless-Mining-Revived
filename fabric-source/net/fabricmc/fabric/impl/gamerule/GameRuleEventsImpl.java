package net.fabricmc.fabric.impl.gamerule;

import java.util.IdentityHashMap;
import java.util.Map;
import java.util.function.Function;

import org.jspecify.annotations.Nullable;

import net.minecraft.world.level.gamerules.GameRule;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleEvents;

public final class GameRuleEventsImpl {
	private GameRuleEventsImpl() {
	}

	private static final Map<GameRule<?>, Event<GameRuleEvents.ValueUpdate<?>>> VALUE_UPDATES = new IdentityHashMap<>();

	public static <T> Event<GameRuleEvents.ValueUpdate<T>> changeCallback(GameRule<T> rule) {
		//noinspection unchecked
		return (Event<GameRuleEvents.ValueUpdate<T>>) (Event<?>) VALUE_UPDATES.computeIfAbsent(rule, gameRule -> {
			//noinspection unchecked
			return (Event<GameRuleEvents.ValueUpdate<?>>) (Event<?>) EventFactory.createArrayBacked(GameRuleEvents.ValueUpdate.class, (Function<GameRuleEvents.ValueUpdate<T>[], GameRuleEvents.ValueUpdate<T>>) callbacks -> (value, server) -> {
				for (GameRuleEvents.ValueUpdate<T> changedCallback : callbacks) {
					changedCallback.onGameRuleUpdated(value, server);
				}
			});
		});
	}

	@Nullable
	public static <T> Event<GameRuleEvents.ValueUpdate<T>> getValueUpdate(GameRule<T> rule) {
		//noinspection unchecked
		return (Event<GameRuleEvents.ValueUpdate<T>>) (Event<?>) VALUE_UPDATES.get(rule);
	}
}
