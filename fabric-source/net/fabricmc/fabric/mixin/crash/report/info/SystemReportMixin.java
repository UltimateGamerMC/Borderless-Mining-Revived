package net.fabricmc.fabric.mixin.crash.report.info;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.function.Supplier;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.SystemReport;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;

@Mixin(SystemReport.class)
public abstract class SystemReportMixin {
	@Shadow
	public abstract void setDetail(String string, Supplier<String> supplier);

	@Inject(at = @At("RETURN"), method = "<init>")
	private void fillSystemDetails(CallbackInfo info) {
		setDetail("Fabric Mods", () -> {
			ArrayList<ModContainer> topLevelMods = new ArrayList<>();

			for (ModContainer container : FabricLoader.getInstance().getAllMods()) {
				if (container.getContainingMod().isEmpty()) {
					topLevelMods.add(container);
				}
			}

			StringBuilder modString = new StringBuilder();

			appendMods(modString, 2, topLevelMods);

			return modString.toString();
		});
	}

	@Unique
	private static void appendMods(StringBuilder modString, int depth, ArrayList<ModContainer> mods) {
		mods.sort(Comparator.comparing(mod -> mod.getMetadata().getId()));

		for (ModContainer mod : mods) {
			modString.append('\n');
			modString.append("\t".repeat(depth));
			modString.append(mod.getMetadata().getId());
			modString.append(": ");
			modString.append(mod.getMetadata().getName());
			modString.append(' ');
			modString.append(mod.getMetadata().getVersion().getFriendlyString());

			if (!mod.getContainedMods().isEmpty()) {
				ArrayList<ModContainer> childMods = new ArrayList<>(mod.getContainedMods());
				appendMods(modString, depth + 1, childMods);
			}
		}
	}
}
