package net.fabricmc.fabric.mixin.registry.sync;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.core.WritableRegistry;
import net.minecraft.core.registries.BuiltInRegistries;

@Mixin(BuiltInRegistries.class)
public interface BuiltInRegistriesAccessor<T> {
	@Accessor()
	static WritableRegistry<WritableRegistry<?>> getWRITABLE_REGISTRY() {
		throw new UnsupportedOperationException();
	}
}
