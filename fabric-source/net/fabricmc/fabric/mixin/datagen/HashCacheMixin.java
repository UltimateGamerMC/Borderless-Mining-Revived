package net.fabricmc.fabric.mixin.datagen;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.data.HashCache;

@Mixin(HashCache.class)
public abstract class HashCacheMixin {
	// Lambda in write()V
	@Redirect(method = "method_46571", at = @At(value = "INVOKE", target = "Ljava/time/ZonedDateTime;now()Ljava/time/ZonedDateTime;"))
	private ZonedDateTime constantTime() {
		// Write a constant time to the .cache file to ensure datagen output is reproducible
		return ZonedDateTime.of(LocalDateTime.MIN, ZoneOffset.UTC);
	}
}
