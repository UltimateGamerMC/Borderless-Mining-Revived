package net.fabricmc.fabric.mixin.transfer;

import org.apache.commons.lang3.math.Fraction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.BundleContents;

@Mixin(BundleContents.class)
public interface BundleContentsAccessor {
	@Invoker("getWeight")
	static Fraction getOccupancy(ItemStack stack) {
		throw new AssertionError("This shouldn't happen!");
	}
}
