package net.fabricmc.fabric.api.registry;

import net.minecraft.core.Holder;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.crafting.Ingredient;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

/**
 * An extension of {@link PotionBrewing.Builder} to support ingredients.
 */
public interface FabricBrewingRecipeRegistryBuilder {
	/**
	 * An event that is called when the brewing recipe registry is being built.
	 */
	Event<FabricBrewingRecipeRegistryBuilder.BuildCallback> BUILD = EventFactory.createArrayBacked(FabricBrewingRecipeRegistryBuilder.BuildCallback.class, listeners -> builder -> {
		for (FabricBrewingRecipeRegistryBuilder.BuildCallback listener : listeners) {
			listener.build(builder);
		}
	});

	default void registerItemRecipe(Item input, Ingredient ingredient, Item output) {
		throw new AssertionError("Must be implemented via interface injection");
	}

	default void registerPotionRecipe(Holder<Potion> input, Ingredient ingredient, Holder<Potion> output) {
		throw new AssertionError("Must be implemented via interface injection");
	}

	default void registerRecipes(Ingredient ingredient, Holder<Potion> potion) {
		throw new AssertionError("Must be implemented via interface injection");
	}

	default FeatureFlagSet getEnabledFeatures() {
		throw new AssertionError("Must be implemented via interface injection");
	}

	/**
	 * Use this event to register custom brewing recipes.
	 */
	@FunctionalInterface
	interface BuildCallback {
		/**
		 * Called when the brewing recipe registry is being built.
		 *
		 * @param builder the {@link PotionBrewing} instance
		 */
		void build(PotionBrewing.Builder builder);
	}
}
