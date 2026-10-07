package link.infra.borderlessmining.config;

import com.mojang.serialization.Codec;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.OptionInstance;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.options.OptionsSubScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.mojang.blaze3d.platform.Monitor;
import link.infra.borderlessmining.util.Displays;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.IntStream;

public class BorderlessMiningConfigScreen extends OptionsSubScreen {
	private static final Logger LOGGER = LogManager.getLogger(BorderlessMiningConfigScreen.class);
	private final ConfigHandler configHandler;

	public BorderlessMiningConfigScreen(Screen parent, ConfigHandler configHandler) {
		super(parent, Minecraft.getInstance().options, Component.translatable("config.borderlessmining.title"));
		this.configHandler = configHandler;
	}

	@Override
	protected void addOptions() {
		if (this.list == null) {
			return;
		}
		this.list.addBig(OptionInstance.createBoolean(
			"config.borderlessmining.general.enabled",
			OptionInstance.cachedConstantTooltip(Component.translatable("config.borderlessmining.general.enabled.tooltip")),
			configHandler.isEnabledOrPending(),
			configHandler::setEnabledPending
		));
		this.list.addBig(OptionInstance.createBoolean(
			"config.borderlessmining.general.enabledmac",
			OptionInstance.cachedConstantTooltip(Component.translatable("config.borderlessmining.general.enabledmac.tooltip")),
			configHandler.enableMacOS,
			value -> configHandler.enableMacOS = value
		));

		List<String> monitorNames = new ArrayList<>();
		monitorNames.add(Component.translatable("config.borderlessmining.general.forcemonitor.current").getString());
		int currentMonitor = configHandler.forceWindowMonitor + 1;
		if (currentMonitor < 0) {
			currentMonitor = 0;
		}
		List<Monitor> monitors = Displays.list();
		if (configHandler.forceWindowMonitor >= monitors.size()) {
			LOGGER.warn("Monitor " + configHandler.forceWindowMonitor + " is greater than list size " + monitors.size() + ", using monitor 0");
			currentMonitor = 0;
		}
		for (Monitor monitor : monitors) {
			monitorNames.add(monitor.name() + " (" + (monitorNames.size() - 1) + ")");
		}

		List<Integer> monitorIndices = IntStream.range(0, monitorNames.size()).boxed().toList();
		this.list.addBig(new OptionInstance<>(
			"config.borderlessmining.general.forcemonitor",
			OptionInstance.noTooltip(),
			(caption, value) -> Component.literal(monitorNames.get(value)),
			new OptionInstance.Enum<>(monitorIndices, Codec.INT),
			Mth.clamp(currentMonitor, 0, monitorNames.size() - 1),
			i -> configHandler.forceWindowMonitor = i - 1
		));

		this.list.addHeader(Component.translatable("config.borderlessmining.dimensions").withStyle(ChatFormatting.BOLD));
		this.list.addBig(OptionInstance.createBoolean(
			"config.borderlessmining.dimensions.enabled",
			configHandler.customWindowDimensions.enabled,
			value -> configHandler.customWindowDimensions = configHandler.customWindowDimensions.setEnabled(value)
		));
		this.list.addBig(OptionInstance.createBoolean(
			"config.borderlessmining.dimensions.monitorcoordinates",
			OptionInstance.cachedConstantTooltip(Component.translatable("config.borderlessmining.dimensions.monitorcoordinates.tooltip")),
			configHandler.customWindowDimensions.useMonitorCoordinates,
			value -> configHandler.customWindowDimensions = configHandler.customWindowDimensions.setUseMonitorCoordinates(value)
		));
		addIntRow("config.borderlessmining.dimensions.x", () -> configHandler.customWindowDimensions.x, v -> configHandler.customWindowDimensions = configHandler.customWindowDimensions.setX(v));
		addIntRow("config.borderlessmining.dimensions.y", () -> configHandler.customWindowDimensions.y, v -> configHandler.customWindowDimensions = configHandler.customWindowDimensions.setY(v));
		addIntRow("config.borderlessmining.dimensions.width", () -> configHandler.customWindowDimensions.width, v -> configHandler.customWindowDimensions = configHandler.customWindowDimensions.setWidth(v));
		addIntRow("config.borderlessmining.dimensions.height", () -> configHandler.customWindowDimensions.height, v -> configHandler.customWindowDimensions = configHandler.customWindowDimensions.setHeight(v));
	}

	private void addIntRow(String labelKey, Supplier<Integer> get, Consumer<Integer> set) {
		Component label = Component.translatable(labelKey);
		EditBox box = new EditBox(this.font, 0, 0, 150, 20, label);
		box.setValue(String.valueOf(get.get()));
		box.setResponder(s -> {
			try {
				set.accept(Integer.parseInt(s));
				box.setTextColor(EditBox.DEFAULT_TEXT_COLOR);
			} catch (NumberFormatException e) {
				box.setTextColor(0xFF0000);
			}
		});
		this.list.addSmall(new StringWidget(150, 20, label, this.font), box);
	}

	@Override
	public void removed() {
		configHandler.save();
		super.removed();
	}
}
