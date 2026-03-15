package net.fabricmc.fabric.mixin.resource;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.server.dedicated.DedicatedServerProperties;
import net.minecraft.world.level.WorldDataConfiguration;

import net.fabricmc.fabric.impl.resource.pack.ModPackResourcesUtil;

@Mixin(DedicatedServerProperties.class)
public class DedicatedServerPropertiesMixin {
	@Redirect(method = "<init>", at = @At(value = "FIELD", target = "Lnet/minecraft/world/level/WorldDataConfiguration;DEFAULT:Lnet/minecraft/world/level/WorldDataConfiguration;"))
	private WorldDataConfiguration replaceDefaultDataConfiguration() {
		return ModPackResourcesUtil.createDefaultDataConfiguration();
	}
}
