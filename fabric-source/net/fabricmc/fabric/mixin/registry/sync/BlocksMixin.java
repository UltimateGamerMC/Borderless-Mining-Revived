package net.fabricmc.fabric.mixin.registry.sync;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import net.fabricmc.fabric.api.event.registry.RegistryEntryAddedCallback;

@Mixin(Blocks.class)
public class BlocksMixin {
	@Inject(method = "<clinit>", at = @At("TAIL"))
	private static void initShapeCache(CallbackInfo ci) {
		// Ensure that any blocks added after this point have their shape cache initialized.
		RegistryEntryAddedCallback.event(BuiltInRegistries.BLOCK).register((rawId, id, block) -> {
			for (BlockState state : block.getStateDefinition().getPossibleStates()) {
				state.initCache();
			}
		});
	}
}
