package net.fabricmc.fabric.mixin.item;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.world.item.enchantment.Enchantment;

import net.fabricmc.fabric.impl.item.EnchantmentUtil;

@Mixin(Enchantment.Builder.class)
public class EnchantmentBuilderMixin implements EnchantmentUtil.BuilderExtensions {
	@Unique
	private boolean didModify = false;

	// Target all methods in the builder, but only mark as modified if the return value is the builder itself.
	@Inject(method = "*", at = @At(value = "RETURN"))
	private void markModified(CallbackInfoReturnable<?> cir) {
		if (cir.getReturnValue() == this) {
			didModify = true;
		}
	}

	@Override
	public void fabric$resetModified() {
		didModify = false;
	}

	@Override
	public boolean fabric$didModify() {
		return didModify;
	}
}
