package net.fabricmc.fabric.mixin.transfer;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.world.item.BucketItem;
import net.minecraft.world.level.material.Fluid;

@Mixin(BucketItem.class)
public interface BucketItemAccessor {
	@Accessor("content")
	Fluid fabric_getFluid();
}
