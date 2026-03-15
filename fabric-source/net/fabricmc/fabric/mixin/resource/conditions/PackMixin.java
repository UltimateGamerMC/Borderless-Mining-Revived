package net.fabricmc.fabric.mixin.resource.conditions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.llamalad7.mixinextras.sugar.Local;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.repository.Pack;

import net.fabricmc.fabric.impl.resource.conditions.OverlayConditionsMetadata;

@Mixin(Pack.class)
public class PackMixin {
	@ModifyVariable(method = "readPackMetadata", at = @At("STORE"))
	private static List<String> applyOverlayConditions(List<String> overlays, @Local PackResources resourcePack) throws IOException {
		List<String> appliedOverlays = new ArrayList<>(overlays);
		OverlayConditionsMetadata overlayMetadata = resourcePack.getMetadataSection(OverlayConditionsMetadata.SERIALIZER);

		if (overlayMetadata != null) {
			appliedOverlays.addAll(overlayMetadata.appliedOverlays());
		}

		return List.copyOf(appliedOverlays);
	}
}
