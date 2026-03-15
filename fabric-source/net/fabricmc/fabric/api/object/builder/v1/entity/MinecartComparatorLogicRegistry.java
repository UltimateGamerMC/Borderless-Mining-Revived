package net.fabricmc.fabric.api.object.builder.v1.entity;

import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Objects;

import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.vehicle.minecart.AbstractMinecart;

/**
 * A registry for {@linkplain MinecartComparatorLogic custom minecart comparator logic}.
 */
public final class MinecartComparatorLogicRegistry {
	private static final Logger LOGGER = LoggerFactory.getLogger(MinecartComparatorLogicRegistry.class);
	private static final Map<EntityType<?>, MinecartComparatorLogic<?>> LOGICS = new IdentityHashMap<>();

	private MinecartComparatorLogicRegistry() {
	}

	/**
	 * Gets the registered custom comparator logic for the specified minecart entity type.
	 *
	 * @param type the entity type
	 * @return the comparator logic, or {@code null} if not registered
	 */
	@Nullable
	@SuppressWarnings("unchecked")
	public static MinecartComparatorLogic<AbstractMinecart> getCustomComparatorLogic(EntityType<?> type) {
		return (MinecartComparatorLogic<AbstractMinecart>) LOGICS.get(type);
	}

	/**
	 * Registers a comparator logic for a minecart entity type.
	 *
	 * <p>Registering a second value for an entity type will replace the old logic.
	 *
	 * @param <T>   the handled minecart type
	 * @param type  the minecart entity type
	 * @param logic the logic to register
	 */
	public static <T extends AbstractMinecart> void register(EntityType<T> type, MinecartComparatorLogic<? super T> logic) {
		Objects.requireNonNull(type, "Entity type cannot be null");
		Objects.requireNonNull(logic, "Logic cannot be null");

		if (LOGICS.put(type, logic) != null) {
			LOGGER.warn("Overriding existing minecart comparator logic for entity type {}", BuiltInRegistries.ENTITY_TYPE.getKey(type));
		}
	}
}
