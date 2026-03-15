package net.fabricmc.fabric.mixin.content.registry;

import java.util.function.Function;

import com.llamalad7.mixinextras.sugar.Local;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import net.minecraft.world.item.AxeItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import net.fabricmc.fabric.api.registry.StrippableBlockRegistry;
import net.fabricmc.fabric.impl.content.registry.StrippableBlockRegistryImpl;

@Mixin(AxeItem.class)
public class AxeItemMixin {
	@ModifyArg(method = "getStripped", at = @At(value = "INVOKE", target = "Ljava/util/Optional;map(Ljava/util/function/Function;)Ljava/util/Optional;"))
	private Function<Block, BlockState> handleCustomStrippingBehavior(Function<Block, BlockState> mapper, @Local(argsOnly = true) BlockState state) {
		StrippableBlockRegistry.StrippingTransformer transformer = StrippableBlockRegistryImpl.getTransformer(state.getBlock());

		if (transformer != null) {
			return block -> transformer.getStrippedBlockState(block, state);
		}

		return mapper;
	}
}
