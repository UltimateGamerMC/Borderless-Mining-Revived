package net.fabricmc.fabric.mixin.content.registry;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.world.level.block.WeatheringCopper;

@Mixin(WeatheringCopper.class)
public interface WeatheringCopperMixin {
	@Dynamic("method_34740: Synthetic lambda body for Suppliers.memoize in initialization of OXIDATION_LEVEL_INCREASES")
	@Inject(method = "method_34740", at = @At("RETURN"), cancellable = true)
	private static void createOxidationLevelIncreasesMap(CallbackInfoReturnable<BiMap> cir) {
		cir.setReturnValue(HashBiMap.create(cir.getReturnValue()));
	}
}
