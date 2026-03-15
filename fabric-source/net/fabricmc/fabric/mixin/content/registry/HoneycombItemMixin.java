package net.fabricmc.fabric.mixin.content.registry;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.world.item.HoneycombItem;

@Mixin(HoneycombItem.class)
public class HoneycombItemMixin {
	@Dynamic("method_34723: Synthetic lambda body for Suppliers.memoize in initialization of UNWAXED_TO_WAXED_BLOCKS")
	@Inject(method = "method_34723", at = @At("RETURN"), cancellable = true)
	private static void createUnwaxedToWaxedMap(CallbackInfoReturnable<BiMap> cir) {
		cir.setReturnValue(HashBiMap.create(cir.getReturnValue()));
	}
}
