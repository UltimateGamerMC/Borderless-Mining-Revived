package link.infra.borderlessmining.util;

import com.mojang.blaze3d.platform.Monitor;
import com.mojang.blaze3d.platform.Window;
import link.infra.borderlessmining.config.ConfigHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.List;

public class DimensionsResolver {
	private static final Logger LOGGER = LogManager.getLogger(DimensionsResolver.class);

	public int x;
	public int y;
	public int width;
	public int height;

	public boolean resolve(Window window) {
		if (ConfigHandler.getInstance().customWindowDimensions != null &&
			ConfigHandler.getInstance().customWindowDimensions.enabled &&
			!ConfigHandler.getInstance().customWindowDimensions.useMonitorCoordinates) {
			x = 0;
			y = 0;
			width = 0;
			height = 0;
		} else if (ConfigHandler.getInstance().forceWindowMonitor < 0) {
			Monitor monitor = window.findBestMonitor();
			if (monitor == null) {
				LOGGER.error("Failed to get a valid monitor for determining fullscreen size!");
				return false;
			}
			x = monitor.x();
			y = monitor.y();
			width = monitor.w();
			height = monitor.h();
		} else {
			List<Monitor> monitors = Displays.list();
			if (monitors.isEmpty()) {
				LOGGER.error("Failed to get a valid monitor list for determining fullscreen position!");
				return false;
			}
			int index = ConfigHandler.getInstance().forceWindowMonitor;
			if (index >= monitors.size()) {
				LOGGER.warn("Monitor " + index + " is greater than list size " + monitors.size() + ", using monitor 0");
				index = 0;
			}
			Monitor monitor = monitors.get(index);
			x = monitor.x();
			y = monitor.y();
			width = monitor.w();
			height = monitor.h();
		}

		if (ConfigHandler.getInstance().customWindowDimensions != null) {
			ConfigHandler.CustomWindowDimensions dims = ConfigHandler.getInstance().customWindowDimensions;
			if (dims.enabled) {
				if (dims.useMonitorCoordinates) {
					x += dims.x;
					y += dims.y;
				} else {
					x = dims.x;
					y = dims.y;
				}
				if (dims.width > 0 && dims.height > 0) {
					width = dims.width;
					height = dims.height;
				} else if (!dims.useMonitorCoordinates) {
					LOGGER.error("Both width and height must be > 0 when specifying absolute coordinates!");
					return false;
				}
			}
		}
		return true;
	}

}
