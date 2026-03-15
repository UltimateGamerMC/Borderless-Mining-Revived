package net.fabricmc.fabric.mixin.transfer;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.world.CompoundContainer;
import net.minecraft.world.Container;

@Mixin(CompoundContainer.class)
public interface CompoundContainerAccessor {
	@Accessor("container1")
	Container fabric_getFirst();

	@Accessor("container2")
	Container fabric_getSecond();
}
