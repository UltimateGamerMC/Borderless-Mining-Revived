package net.fabricmc.fabric.api.event.lifecycle.v1;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

public final class ServerEntityEvents {
	private ServerEntityEvents() {
	}

	/**
	 * Called when an Entity is loaded into a ServerWorld.
	 *
	 * <p>When this event is called, the entity is already in the world.
	 */
	public static final Event<ServerEntityEvents.Load> ENTITY_LOAD = EventFactory.createArrayBacked(ServerEntityEvents.Load.class, callbacks -> (entity, world) -> {
		for (Load callback : callbacks) {
			callback.onLoad(entity, world);
		}
	});

	/**
	 * Called when an Entity is unloaded from a ServerWorld.
	 *
	 * <p>This event is called before the entity is removed from the world.
	 */
	public static final Event<ServerEntityEvents.Unload> ENTITY_UNLOAD = EventFactory.createArrayBacked(ServerEntityEvents.Unload.class, callbacks -> (entity, world) -> {
		for (Unload callback : callbacks) {
			callback.onUnload(entity, world);
		}
	});

	/**
	 * Called during {@link LivingEntity#tick()} if the Entity's equipment has been changed or mutated.
	 *
	 * <p>This event is also called when the entity joins the world.
	 * A change in equipment is determined by {@link ItemStack#matches(ItemStack, ItemStack)}.
	 */
	public static final Event<EquipmentChange> EQUIPMENT_CHANGE = EventFactory.createArrayBacked(ServerEntityEvents.EquipmentChange.class, callbacks -> (livingEntity, equipmentSlot, previous, next) -> {
		for (EquipmentChange callback : callbacks) {
			callback.onChange(livingEntity, equipmentSlot, previous, next);
		}
	});

	@FunctionalInterface
	public interface Load {
		void onLoad(Entity entity, ServerLevel world);
	}

	@FunctionalInterface
	public interface Unload {
		void onUnload(Entity entity, ServerLevel world);
	}

	@FunctionalInterface
	public interface EquipmentChange {
		void onChange(LivingEntity livingEntity, EquipmentSlot equipmentSlot, ItemStack previousStack, ItemStack currentStack);
	}
}
