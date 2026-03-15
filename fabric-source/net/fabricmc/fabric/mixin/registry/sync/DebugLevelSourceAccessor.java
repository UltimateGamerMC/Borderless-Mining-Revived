package net.fabricmc.fabric.mixin.registry.sync;

import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.DebugLevelSource;

@Mixin(DebugLevelSource.class)
public interface DebugLevelSourceAccessor {
	@Accessor
	@Mutable
	static void setALL_BLOCKS(List<BlockState> blockStates) {
		throw new UnsupportedOperationException();
	}

	@Accessor
	@Mutable
	static void setGRID_WIDTH(int length) {
		throw new UnsupportedOperationException();
	}

	@Accessor
	@Mutable
	static void setGRID_HEIGHT(int length) {
		throw new UnsupportedOperationException();
	}
}
