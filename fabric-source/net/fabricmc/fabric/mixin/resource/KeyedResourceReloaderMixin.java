package net.fabricmc.fabric.mixin.resource;

import java.util.Locale;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import net.minecraft.resources.Identifier;
import net.minecraft.server.ServerAdvancementManager;
import net.minecraft.server.ServerFunctionLibrary;
import net.minecraft.world.item.crafting.RecipeManager;

import net.fabricmc.fabric.api.resource.v1.reloader.ResourceReloaderKeys;
import net.fabricmc.fabric.impl.resource.FabricResourceReloader;

@Mixin({
		/* public */
		RecipeManager.class, ServerAdvancementManager.class, ServerFunctionLibrary.class
		/* private */
})
public abstract class KeyedResourceReloaderMixin implements FabricResourceReloader {
	@Unique
	private Identifier id;

	@Override
	@SuppressWarnings({"ConstantConditions"})
	public Identifier fabric$getId() {
		if (this.id == null) {
			Object self = this;

			if (self instanceof RecipeManager) {
				this.id = ResourceReloaderKeys.Server.RECIPES;
			} else if (self instanceof ServerAdvancementManager) {
				this.id = ResourceReloaderKeys.Server.ADVANCEMENTS;
			} else if (self instanceof ServerFunctionLibrary) {
				this.id = ResourceReloaderKeys.Server.FUNCTIONS;
			} else {
				this.id = Identifier.withDefaultNamespace("private/" + self.getClass().getSimpleName().toLowerCase(Locale.ROOT));
			}
		}

		return this.id;
	}
}
