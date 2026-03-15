package net.fabricmc.fabric.mixin.attachment;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.network.VarInt;

@Mixin(VarInt.class)
public interface VarIntAccessor {
	@Accessor("MAX_VARINT_SIZE")
	static int getMaxByteSize() {
		throw new UnsupportedOperationException("implemented via mixin");
	}
}
