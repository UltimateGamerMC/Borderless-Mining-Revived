package link.infra.borderlessmining.mixin;

import link.infra.borderlessmining.config.ConfigHandler;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.option.VideoOptionsScreen;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.SimpleOption;
import net.minecraft.client.util.Monitor;
import net.minecraft.client.util.VideoMode;
import net.minecraft.client.util.Window;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.Optional;

@Mixin(VideoOptionsScreen.class)
public abstract class FullScreenOptionMixin {
	@ModifyVariable(method = "addOptions", at = @At("STORE"), ordinal = 0)
	private SimpleOption<Integer> borderlessmining_modifyResolutionOption(SimpleOption<Integer> original) {
		if (!ConfigHandler.getInstance().addToVanillaVideoSettings) {
			return original;
		}
		Window window = MinecraftClient.getInstance().getWindow();
		Monitor monitor = window.getMonitor();
		if (monitor == null) {
			return original;
		}
		int bmOption = monitor.getVideoModeCount();
		@SuppressWarnings("unchecked")
		var accessor = (SimpleOptionAccessor<Integer>) (Object) original;
		SimpleOption.ValidatingIntSliderCallbacks newCallbacks = new SimpleOption.ValidatingIntSliderCallbacks(-1, bmOption);
		int initialValue = ConfigHandler.getInstance().isEnabledOrPending() ? bmOption : original.getValue();
		return new SimpleOption<>(
			"options.fullscreen.resolution",
			SimpleOption.emptyTooltip(),
			(optionText, value) -> {
				if (value == bmOption) {
					return Text.translatable("text.borderlessmining.videomodename");
				}
				if (value == -1) {
					return GameOptions.getGenericValueText(optionText, Text.translatable("options.fullscreen.current"));
				}
				VideoMode mode = monitor.getVideoMode(value);
				return GameOptions.getGenericValueText(optionText, Text.translatable("options.fullscreen.entry", mode.getWidth(), mode.getHeight(), mode.getRefreshRate(), mode.getRedBits() + mode.getGreenBits() + mode.getBlueBits()));
			},
			newCallbacks,
			initialValue,
			value -> {
				if (value == bmOption) {
					ConfigHandler.getInstance().setEnabledPending(true);
					window.setFullscreenVideoMode(Optional.empty());
				} else {
					ConfigHandler.getInstance().setEnabledPending(false);
					accessor.borderlessmining_getChangeCallback().accept(value);
				}
			}
		);
	}
}
