package link.infra.borderlessmining.mixin;

import com.mojang.blaze3d.platform.DisplayData;
import com.mojang.blaze3d.platform.Window;
import link.infra.borderlessmining.config.ConfigHandler;
import link.infra.borderlessmining.util.DimensionsResolver;
import link.infra.borderlessmining.util.WindowHooks;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Window.class)
public abstract class WindowMixin implements WindowHooks {
	@Shadow private int x;
	@Shadow private int y;
	@Shadow private int width;
	@Shadow private int height;

	@Shadow private int windowedX;
	@Shadow private int windowedY;
	@Shadow private int windowedWidth;
	@Shadow private int windowedHeight;

	@Shadow private boolean fullscreen;

	@Shadow @Final private long handle;

	@Redirect(method = "<init>", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/platform/DisplayData;isFullscreen()Z"))
	private boolean borderlessmining_initFullscreen(DisplayData displayData) {
		boolean fs = displayData.isFullscreen();
		if (ConfigHandler.getInstance().isEnabled()) {
			return false;
		}
		return fs;
	}

	@Inject(method = "setMode", at = @At("HEAD"), cancellable = true)
	private void borderlessmining_beforeSetMode(CallbackInfo ci) {
		boolean currFullscreen = GLFW.glfwGetWindowMonitor(this.handle) != 0L;
		if (ConfigHandler.getInstance().isEnabled() && this.fullscreen) {
			if (!currFullscreen) {
				this.windowedX = this.x;
				this.windowedY = this.y;
				this.windowedWidth = this.width;
				this.windowedHeight = this.height;
			}

			int rwX = this.windowedX;
			int rwY = this.windowedY;
			int rwW = this.windowedWidth;
			int rwH = this.windowedHeight;

			GLFW.glfwSetWindowAttrib(this.handle, GLFW.GLFW_DECORATED, GLFW.GLFW_FALSE);
			DimensionsResolver res = new DimensionsResolver();
			if (res.resolve((Window) (Object) this)) {
				this.x = res.x;
				this.y = res.y;
				this.width = res.width;
				this.height = res.height;
				GLFW.glfwSetWindowMonitor(this.handle, 0L, this.x, this.y, this.width, this.height, GLFW.GLFW_DONT_CARE);

				ci.cancel();
				this.windowedX = rwX;
				this.windowedY = rwY;
				this.windowedWidth = rwW;
				this.windowedHeight = rwH;
			} else {
				GLFW.glfwSetWindowAttrib(this.handle, GLFW.GLFW_DECORATED, GLFW.GLFW_TRUE);
			}
		} else {
			GLFW.glfwSetWindowAttrib(this.handle, GLFW.GLFW_DECORATED, GLFW.GLFW_TRUE);
		}
	}

	@Inject(method = "toggleFullScreen", at = @At("HEAD"))
	public void borderlessmining_onToggleFullscreen(CallbackInfo info) {
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
