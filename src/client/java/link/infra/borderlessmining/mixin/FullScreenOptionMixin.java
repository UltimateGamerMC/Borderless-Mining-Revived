package link.infra.borderlessmining.mixin;

import com.mojang.blaze3d.platform.Monitor;
import com.mojang.blaze3d.platform.VideoMode;
import com.mojang.blaze3d.platform.Window;
import link.infra.borderlessmining.config.ConfigHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.OptionInstance;
import net.minecraft.client.Options;
import net.minecraft.client.gui.screens.options.VideoSettingsScreen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.Optional;

@Mixin(VideoSettingsScreen.class)
public abstract class FullScreenOptionMixin {
	@ModifyVariable(method = "addOptions", at = @At("STORE"), ordinal = 0)
	private OptionInstance<Integer> borderlessmining_modifyResolutionOption(OptionInstance<Integer> original) {
		if (!ConfigHandler.getInstance().addToVanillaVideoSettings) {
			return original;
		}
		Window window = Minecraft.getInstance().getWindow();
		Monitor monitor = window.findBestMonitor();
		if (monitor == null) {
			return original;
		}
		int bmOption = monitor.getModeCount();
		@SuppressWarnings("unchecked")
		var accessor = (OptionInstanceAccessor<Integer>) (Object) original;
		var newCallbacks = new OptionInstance.IntRange(-1, bmOption);
		int initialValue = ConfigHandler.getInstance().isEnabledOrPending() ? bmOption : original.get();
		return new OptionInstance<>(
			"options.fullscreen.resolution",
			OptionInstance.noTooltip(),
			(caption, value) -> {
				if (value == bmOption) {
					return Component.translatable("text.borderlessmining.videomodename");
				}
				if (value == -1) {
					return Options.genericValueLabel(caption, Component.translatable("options.fullscreen.current"));
				}
				VideoMode mode = monitor.getMode(value);
				return Options.genericValueLabel(
					caption,
					Component.translatable("options.fullscreen.entry", mode.getWidth(), mode.getHeight(), mode.getRefreshRate(), mode.getRedBits() + mode.getGreenBits() + mode.getBlueBits())
				);
			},
			newCallbacks,
			initialValue,
			value -> {
				if (value == bmOption) {
					ConfigHandler.getInstance().setEnabledPending(true);
					window.setPreferredFullscreenVideoMode(Optional.empty());
				} else {
					ConfigHandler.getInstance().setEnabledPending(false);
					accessor.borderlessmining_getOnValueUpdate().accept(value);
				}
			}
		);
	}
}
