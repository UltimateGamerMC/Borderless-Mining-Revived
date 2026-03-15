package net.fabricmc.fabric.api.tag.convention.v2;

import net.minecraft.tags.TagKey;
import net.minecraft.world.level.levelgen.structure.Structure;

import net.fabricmc.fabric.impl.tag.convention.v2.TagRegistration;

/**
 * See {@link net.minecraft.tags.StructureTags} for vanilla tags.
 */
public final class ConventionalStructureTags {
	private ConventionalStructureTags() {
	}

	/**
	 * Structures that should not show up on minimaps or world map views from mods/sites.
	 * No effect on vanilla map items.
	 */
	public static final TagKey<Structure> HIDDEN_FROM_DISPLAYERS = register("hidden_from_displayers");

	/**
	 * Structures that should not be locatable/selectable by modded structure-locating items or abilities.
	 * No effect on vanilla map items.
	 */
	public static final TagKey<Structure> HIDDEN_FROM_LOCATOR_SELECTION = register("hidden_from_locator_selection");

	private static TagKey<Structure> register(String tagId) {
		return TagRegistration.STRUCTURE_TAG.registerC(tagId);
	}
}
