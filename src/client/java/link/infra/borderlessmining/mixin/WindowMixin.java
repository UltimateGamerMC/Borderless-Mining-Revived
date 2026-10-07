package link.infra.borderlessmining.mixin;

import com.mojang.blaze3d.platform.Window;
import link.infra.borderlessmining.config.ConfigHandler;
import link.infra.borderlessmining.util.DimensionsResolver;
import link.infra.borderlessmining.util.WindowHooks;
import org.lwjgl.sdl.SDLVideo;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

// Vanilla 26.3 ships its own borderless fullscreen (Exclusive Fullscreen: OFF), but always uses the
// "best" monitor's bounds. When it would go borderless, this takes over so the forced monitor and
// custom window dimensions from the config are honoured on every platform.
@Mixin(Window.class)
public abstract class WindowMixin implements WindowHooks {
	@Shadow @Final private long handle;
	@Shadow private boolean exclusiveFullscreen;
	@Shadow private boolean borderlessFullscreen;

	@Shadow protected abstract boolean setWindowSizeAndPosition(int windowX, int windowY, int windowWidth, int windowHeight);
	@Shadow protected abstract void restoreWindow();

	@Inject(method = "applyFullscreen", at = @At("HEAD"), cancellable = true)
	private void borderlessmining_applyFullscreen(CallbackInfoReturnable<Boolean> cir) {
		if (!ConfigHandler.getInstance().isEnabled() || this.exclusiveFullscreen) {
			return;
		}
		DimensionsResolver res = new DimensionsResolver();
		if (!res.resolve((Window) (Object) this)) {
			return;
		}
		if (!SDLVideo.SDL_SetWindowFullscreen(this.handle, false)) {
			cir.setReturnValue(false);
			return;
		}
		this.restoreWindow();
		this.borderlessFullscreen = true;
		SDLVideo.SDL_SetWindowBordered(this.handle, false);
		// +1 width matches vanilla: stops Windows promoting the window to exclusive fullscreen
		cir.setReturnValue(this.setWindowSizeAndPosition(res.x, res.y, res.width + 1, res.height));
	}

	@Inject(method = "setFullscreen", at = @At("HEAD"))
	private void borderlessmining_onSetFullscreen(boolean fullscreen, CallbackInfo info) {
		ConfigHandler.getInstance().saveIfDirty();
	}

	@Inject(method = "changeFullscreenVideoMode", at = @At("HEAD"))
	private void borderlessmining_onChangeFullscreenVideoMode(CallbackInfo info) {
		ConfigHandler.getInstance().saveIfDirty();
	}

	@Override
	public void borderlessmining_apply() {
		((WindowDirtyAccessor) (Object) this).borderlessmining_setDirty(true);
		((Window) (Object) this).changeFullscreenVideoMode();
	}
}
