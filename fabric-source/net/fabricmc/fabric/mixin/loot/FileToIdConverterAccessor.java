package net.fabricmc.fabric.mixin.loot;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.resources.FileToIdConverter;

@Mixin(FileToIdConverter.class)
public interface FileToIdConverterAccessor {
	@Accessor("prefix")
	String getDirectoryName();
}
