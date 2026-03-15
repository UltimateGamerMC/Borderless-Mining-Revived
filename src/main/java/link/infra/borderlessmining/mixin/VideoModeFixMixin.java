package link.infra.borderlessmining.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.option.VideoOptionsScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(VideoOptionsScreen.class)
public class VideoModeFixMixin {
	@Inject(at = @At("HEAD"), method = "removed()V")
	public void screenRemoved(CallbackInfo ci) {
		MinecraftClient client = MinecraftClient.getInstance();
		if (client != null && client.getWindow() != null) {
			client.getWindow().applyFullscreenVideoMode();
		}
	}
}
