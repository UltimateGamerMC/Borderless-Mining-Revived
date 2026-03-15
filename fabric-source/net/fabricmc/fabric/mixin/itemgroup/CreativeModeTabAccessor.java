package net.fabricmc.fabric.mixin.itemgroup;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.world.item.CreativeModeTab;

@Mixin(CreativeModeTab.class)
public interface CreativeModeTabAccessor {
	@Accessor
	@Mutable
	@Final
	void setRow(CreativeModeTab.Row row);

	@Accessor
	@Mutable
	@Final
	void setColumn(int column);
}
