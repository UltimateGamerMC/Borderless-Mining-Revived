package net.fabricmc.fabric.impl.attachment;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.entity.event.v1.ServerEntityWorldChangeEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;

public class AttachmentEntrypoint implements ModInitializer {
	public static final Logger LOGGER = LoggerFactory.getLogger("fabric-data-attachment-api-v1");

	@Override
	public void onInitialize() {
		ServerPlayerEvents.AFTER_RESPAWN.register((oldPlayer, newPlayer, alive) ->
				AttachmentTargetImpl.transfer(oldPlayer, newPlayer, !alive)
		);
		ServerEntityWorldChangeEvents.AFTER_ENTITY_CHANGE_WORLD.register(((originalEntity, newEntity, origin, destination) ->
				AttachmentTargetImpl.transfer(originalEntity, newEntity, false))
		);
		// using the corresponding player event is unnecessary as no new instance is created
		ServerLivingEntityEvents.MOB_CONVERSION.register((previous, converted, keepEquipment) ->
				AttachmentTargetImpl.transfer(previous, converted, true)
		);
	}
}
