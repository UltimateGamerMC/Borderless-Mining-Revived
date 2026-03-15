package net.fabricmc.fabric.mixin.item;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.core.Holder;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.storage.loot.functions.EnchantRandomlyFunction;

import net.fabricmc.fabric.api.item.v1.EnchantingContext;

@Mixin(EnchantRandomlyFunction.class)
abstract class EnchantRandomlyFunctionMixin {
	@Redirect(
			method = "method_60291",
			at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/enchantment/Enchantment;canEnchant(Lnet/minecraft/world/item/ItemStack;)Z")
	)
	private static boolean callAllowEnchantingEvent(Enchantment enchantment, ItemStack stack, boolean bl, ItemStack itemStack, Holder<Enchantment> registryEntry) {
		return stack.canBeEnchantedWith(registryEntry, EnchantingContext.ACCEPTABLE);
	}
}
