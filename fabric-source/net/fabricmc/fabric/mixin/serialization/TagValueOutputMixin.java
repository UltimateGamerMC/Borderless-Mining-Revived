package net.fabricmc.fabric.mixin.serialization;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.storage.TagValueOutput;

import net.fabricmc.fabric.api.serialization.v1.view.FabricWriteView;

@Mixin(TagValueOutput.class)
public class TagValueOutputMixin implements FabricWriteView {
	@Shadow
	@Final
	private CompoundTag output;

	@Override
	public void putByteArray(String key, byte[] value) {
		this.output.putByteArray(key, value);
	}

	@Override
	public void putLongArray(String key, long[] value) {
		this.output.putLongArray(key, value);
	}
}
