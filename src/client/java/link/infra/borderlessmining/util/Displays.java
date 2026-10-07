package link.infra.borderlessmining.util;

import com.mojang.blaze3d.platform.Monitor;
import org.lwjgl.sdl.SDLStdinc;
import org.lwjgl.sdl.SDLVideo;

import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

public final class Displays {
	private Displays() {}

	public static List<Monitor> list() {
		List<Monitor> result = new ArrayList<>();
		IntBuffer displays = SDLVideo.SDL_GetDisplays();
		if (displays == null) {
			return result;
		}
		try {
			for (int i = 0; i < displays.limit(); i++) {
				Monitor monitor = Monitor.tryCreate(displays.get(i));
				if (monitor != null) {
					result.add(monitor);
				}
			}
		} finally {
			SDLStdinc.SDL_free(displays);
		}
		return result;
	}
}
