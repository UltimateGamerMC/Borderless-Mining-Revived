package net.fabricmc.fabric.mixin.transfer;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.level.material.Fluid;

import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariantAttributeHandler;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariantAttributes;

/**
 * Automatically uses the correct bucket emptying sound for
 * fluid attributes handlers overriding {@link FluidVariantAttributeHandler#getEmptySound}.
 */
@Mixin(BucketItem.class)
public class BucketItemMixin {
	@Shadow
	@Final
	private Fluid content;

	@ModifyVariable(
			method = "playEmptySound",
			at = @At("STORE"),
			index = 4
	)
	private SoundEvent hookEmptyingSound(SoundEvent previous) {
		return FluidVariantAttributes.getHandlerOrDefault(content).getEmptySound(FluidVariant.of(content)).orElse(previous);
	}
}
