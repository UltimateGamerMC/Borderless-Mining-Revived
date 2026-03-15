package net.fabricmc.fabric.mixin.serialization;

import java.util.Collection;
import java.util.Optional;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.storage.TagValueInput;

import net.fabricmc.fabric.api.serialization.v1.view.FabricReadView;

@Mixin(TagValueInput.class)
public class TagValueInputMixin implements FabricReadView {
	@Shadow
	@Final
	private CompoundTag input;

	@Override
	public Collection<String> keys() {
		return this.input.keySet();
	}

	@Override
	public boolean contains(String key) {
		return this.input.contains(key);
	}

	@Override
	public Optional<byte[]> getOptionalByteArray(String key) {
		return this.input.getByteArray(key);
	}

	@Override
	public Optional<long[]> getOptionalLongArray(String key) {
		return this.input.getLongArray(key);
	}
}
