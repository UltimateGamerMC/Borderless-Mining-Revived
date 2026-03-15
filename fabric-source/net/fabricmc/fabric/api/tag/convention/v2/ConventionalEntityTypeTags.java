package net.fabricmc.fabric.api.tag.convention.v2;

import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;

import net.fabricmc.fabric.impl.tag.convention.v2.TagRegistration;

/**
 * See {@link net.minecraft.tags.EntityTypeTags} for vanilla tags.
 * Note that addition to some vanilla tags implies having certain functionality.
 */
public final class ConventionalEntityTypeTags {
	private ConventionalEntityTypeTags() {
	}

	/**
	 * Tag containing entity types that display a boss health bar.
	 */
	public static final TagKey<EntityType<?>> BOSSES = register("bosses");

	public static final TagKey<EntityType<?>> MINECARTS = register("minecarts");
	public static final TagKey<EntityType<?>> BOATS = register("boats");

	/**
	 * Entities should be included in this tag if they are not allowed to be picked up by items or grabbed in a way
	 * that a player can easily move the entity to anywhere they want. Ideal for special entities that should not
	 * be able to be put into a mob jar for example.
	 */
	public static final TagKey<EntityType<?>> CAPTURING_NOT_SUPPORTED = register("capturing_not_supported");

	/**
	 * Entities should be included in this tag if they are not allowed to be teleported in any way.
	 * This is more for mods that allow teleporting entities within the same dimension. Any mod that is
	 * teleporting entities to new dimensions should be checking canUsePortals method on the entity itself.
	 */
	public static final TagKey<EntityType<?>> TELEPORTING_NOT_SUPPORTED = register("teleporting_not_supported");

	private static TagKey<EntityType<?>> register(String tagId) {
		return TagRegistration.ENTITY_TYPE_TAG.registerC(tagId);
	}
}
