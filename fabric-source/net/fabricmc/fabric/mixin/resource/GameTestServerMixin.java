package net.fabricmc.fabric.mixin.resource;

import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.gametest.framework.GameTestServer;
import net.minecraft.world.level.DataPackConfig;

import net.fabricmc.fabric.impl.resource.pack.ModPackResourcesUtil;

/**
 * @see ModPackResourcesUtil#createTestServerSettings
 */
@Mixin(GameTestServer.class)
public class GameTestServerMixin {
	@Redirect(method = "create", at = @At(value = "NEW", target = "(Ljava/util/List;Ljava/util/List;)Lnet/minecraft/world/level/DataPackConfig;"))
	private static DataPackConfig replaceDefaultDataPackSettings(List<String> enabled, List<String> disabled) {
		return ModPackResourcesUtil.createTestServerSettings(enabled, disabled);
	}
}
