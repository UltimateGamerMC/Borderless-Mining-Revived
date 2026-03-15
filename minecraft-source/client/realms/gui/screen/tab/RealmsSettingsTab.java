/*
 * External method calls:
 *   Lnet/minecraft/client/gui/widget/GridWidget;createAdder(I)Lnet/minecraft/client/gui/widget/GridWidget$Adder;
 *   Lnet/minecraft/text/Text;translatable(Ljava/lang/String;)Lnet/minecraft/text/MutableText;
 *   Lnet/minecraft/client/gui/widget/EmptyWidget;ofHeight(I)Lnet/minecraft/client/gui/widget/EmptyWidget;
 *   Lnet/minecraft/text/Text;empty()Lnet/minecraft/text/MutableText;
 *   Lnet/minecraft/client/gui/widget/IconWidget;create(IILnet/minecraft/util/Identifier;)Lnet/minecraft/client/gui/widget/IconWidget;
 *   Lnet/minecraft/client/gui/widget/ButtonWidget;builder(Lnet/minecraft/text/Text;Lnet/minecraft/client/gui/widget/ButtonWidget$PressAction;)Lnet/minecraft/client/gui/widget/ButtonWidget$Builder;
 *   Lnet/minecraft/client/gui/widget/ButtonWidget$Builder;dimensions(IIII)Lnet/minecraft/client/gui/widget/ButtonWidget$Builder;
 *   Lnet/minecraft/client/gui/widget/ButtonWidget$Builder;build()Lnet/minecraft/client/gui/widget/ButtonWidget;
 *   Lnet/minecraft/client/realms/gui/screen/tab/RealmsSettingsTab$Region;preference()Lnet/minecraft/client/realms/dto/RegionSelectionMethod;
 *   Lnet/minecraft/client/realms/gui/screen/tab/RealmsSettingsTab$Region;region()Lnet/minecraft/client/realms/dto/RealmsRegion;
 *   Lnet/minecraft/text/MutableText;formatted(Lnet/minecraft/util/Formatting;)Lnet/minecraft/text/MutableText;
 *   Lnet/minecraft/client/realms/gui/screen/RealmsConfigureWorldScreen;saveSettings(Ljava/lang/String;Ljava/lang/String;Lnet/minecraft/client/realms/dto/RegionSelectionMethod;Lnet/minecraft/client/realms/dto/RealmsRegion;)V
 *   Lnet/minecraft/client/realms/gui/RealmsPopups;createCustomPopup(Lnet/minecraft/client/gui/screen/Screen;Lnet/minecraft/text/Text;Lnet/minecraft/text/Text;Ljava/util/function/Consumer;)Lnet/minecraft/client/gui/screen/PopupScreen;
 *   Lnet/minecraft/client/realms/gui/screen/RealmsConfigureWorldScreen;openTheWorld(Z)V
 *   Lnet/minecraft/client/gui/tooltip/Tooltip;of(Lnet/minecraft/text/Text;)Lnet/minecraft/client/gui/tooltip/Tooltip;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/client/realms/gui/screen/tab/RealmsSettingsTab;update(Lnet/minecraft/client/realms/dto/RealmsServer;)V
 */
package net.minecraft.client.realms.gui.screen.tab;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.tab.GridScreenTab;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.AxisGridWidget;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.EmptyWidget;
import net.minecraft.client.gui.widget.GridWidget;
import net.minecraft.client.gui.widget.IconWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.client.realms.ServiceQuality;
import net.minecraft.client.realms.dto.RealmsRegion;
import net.minecraft.client.realms.dto.RealmsRegionSelectionPreference;
import net.minecraft.client.realms.dto.RealmsServer;
import net.minecraft.client.realms.dto.RegionSelectionMethod;
import net.minecraft.client.realms.gui.RealmsPopups;
import net.minecraft.client.realms.gui.screen.RealmsConfigureWorldScreen;
import net.minecraft.client.realms.gui.screen.RealmsRegionPreferenceScreen;
import net.minecraft.client.realms.gui.screen.tab.RealmsUpdatableTab;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import org.jspecify.annotations.Nullable;

@Environment(value=EnvType.CLIENT)
public class RealmsSettingsTab
extends GridScreenTab
implements RealmsUpdatableTab {
    private static final int field_60267 = 212;
    private static final int field_60268 = 2;
    private static final int field_60269 = 6;
    static final Text TITLE_TEXT = Text.translatable("mco.configure.world.settings.title");
    private static final Text WORLD_NAME_TEXT = Text.translatable("mco.configure.world.name");
    private static final Text DESCRIPTION_TEXT = Text.translatable("mco.configure.world.description");
    private static final Text REGION_PREFERENCE_TEXT = Text.translatable("mco.configure.world.region_preference");
    private static final Tooltip WORLD_NAME_VALIDATION_TOOLTIP = Tooltip.of(Text.translatable("mco.configure.world.name.validation.whitespace"));
    private final RealmsConfigureWorldScreen screen;
    private final MinecraftClient client;
    private RealmsServer server;
    private final Map<RealmsRegion, ServiceQuality> availableRegions;
    final ButtonWidget switchStateButton;
    private final TextFieldWidget descriptionTextField;
    private final TextFieldWidget worldNameTextField;
    private final TextWidget regionText;
    private final IconWidget serviceQualityIcon;
    private Region region;

    RealmsSettingsTab(RealmsConfigureWorldScreen screen, MinecraftClient client, RealmsServer server, Map<RealmsRegion, ServiceQuality> availableRegions) {
        super(TITLE_TEXT);
        this.screen = screen;
        this.client = client;
        this.server = server;
        this.availableRegions = availableRegions;
        GridWidget.Adder lv = this.grid.setRowSpacing(6).createAdder(1);
        lv.add(new TextWidget(WORLD_NAME_TEXT, screen.getTextRenderer()));
        this.worldNameTextField = new TextFieldWidget(client.textRenderer, 0, 0, 212, 20, Text.translatable("mco.configure.world.name"));
        this.worldNameTextField.setMaxLength(32);
        this.worldNameTextField.setChangedListener(name -> {
            if (!this.isWorldNameValid()) {
                this.worldNameTextField.setEditableColor(-2142128);
                this.worldNameTextField.setTooltip(WORLD_NAME_VALIDATION_TOOLTIP);
                return;
            }
            this.worldNameTextField.setTooltip(null);
            this.worldNameTextField.setEditableColor(-2039584);
        });
        lv.add(this.worldNameTextField);
        lv.add(EmptyWidget.ofHeight(2));
        lv.add(new TextWidget(DESCRIPTION_TEXT, screen.getTextRenderer()));
        this.descriptionTextField = new TextFieldWidget(client.textRenderer, 0, 0, 212, 20, Text.translatable("mco.configure.world.description"));
        this.descriptionTextField.setMaxLength(32);
        lv.add(this.descriptionTextField);
        lv.add(EmptyWidget.ofHeight(2));
        lv.add(new TextWidget(REGION_PREFERENCE_TEXT, screen.getTextRenderer()));
        AxisGridWidget lv2 = new AxisGridWidget(0, 0, 212, screen.getTextRenderer().fontHeight, AxisGridWidget.DisplayAxis.HORIZONTAL);
        this.regionText = lv2.add(new TextWidget(192, screen.getTextRenderer().fontHeight, Text.empty(), screen.getTextRenderer()));
        this.serviceQualityIcon = lv2.add(IconWidget.create(10, 8, ServiceQuality.UNKNOWN.getIcon()));
        lv.add(lv2);
        lv.add(ButtonWidget.builder(Text.translatable("mco.configure.world.buttons.region_preference"), button -> this.showRegionPreferenceScreen()).dimensions(0, 0, 212, 20).build());
        lv.add(EmptyWidget.ofHeight(2));
        this.switchStateButton = lv.add(ButtonWidget.builder(Text.empty(), button -> {
            if (arg.state == RealmsServer.State.OPEN) {
                client.setScreen(RealmsPopups.createCustomPopup(screen, Text.translatable("mco.configure.world.close.question.title"), Text.translatable("mco.configure.world.close.question.line1"), popup -> {
                    this.saveSettings();
                    screen.closeTheWorld();
                }));
            } else {
                this.saveSettings();
                screen.openTheWorld(false);
            }
        }).dimensions(0, 0, 212, 20).build());
        this.switchStateButton.active = false;
        this.update(server);
    }

    private static MutableText getRegionText(Region region) {
        return (region.preference().equals((Object)RegionSelectionMethod.MANUAL) && region.region() != null ? Text.translatable(region.region().translationKey) : Text.translatable(region.preference().translationKey)).formatted(Formatting.GRAY);
    }

    private static Identifier getQualityIcon(Region region, Map<RealmsRegion, ServiceQuality> qualityByRegion) {
        if (region.region() != null && qualityByRegion.containsKey((Object)region.region())) {
            ServiceQuality lv = qualityByRegion.getOrDefault((Object)region.region(), ServiceQuality.UNKNOWN);
            return lv.getIcon();
        }
        return ServiceQuality.UNKNOWN.getIcon();
    }

    private boolean isWorldNameValid() {
        String string = this.worldNameTextField.getText();
        String string2 = string.trim();
        return !string2.isEmpty() && string.length() == string2.length();
    }

    private void showRegionPreferenceScreen() {
        this.client.setScreen(new RealmsRegionPreferenceScreen(this.screen, this::onRegionChanged, this.availableRegions, this.region));
    }

    private void onRegionChanged(RegionSelectionMethod selectionMethod, RealmsRegion region) {
        this.region = new Region(selectionMethod, region);
        this.refreshRegionText();
    }

    private void refreshRegionText() {
        this.regionText.setMessage(RealmsSettingsTab.getRegionText(this.region));
        this.serviceQualityIcon.setTexture(RealmsSettingsTab.getQualityIcon(this.region, this.availableRegions));
        this.serviceQualityIcon.visible = this.region.preference == RegionSelectionMethod.MANUAL;
    }

    @Override
    public void onLoaded(RealmsServer server) {
        this.update(server);
    }

    @Override
    public void update(RealmsServer server) {
        this.server = server;
        if (server.regionSelectionPreference == null) {
            server.regionSelectionPreference = RealmsRegionSelectionPreference.DEFAULT;
        }
        if (server.regionSelectionPreference.selectionMethod == RegionSelectionMethod.MANUAL && server.regionSelectionPreference.preferredRegion == null) {
            Optional optional = this.availableRegions.keySet().stream().findFirst();
            optional.ifPresent(region -> {
                arg.regionSelectionPreference.preferredRegion = region;
            });
        }
        String string = server.state == RealmsServer.State.OPEN ? "mco.configure.world.buttons.close" : "mco.configure.world.buttons.open";
        this.switchStateButton.setMessage(Text.translatable(string));
        this.switchStateButton.active = true;
        this.region = new Region(server.regionSelectionPreference.selectionMethod, server.regionSelectionPreference.preferredRegion);
        this.worldNameTextField.setText(Objects.requireNonNullElse(server.getName(), ""));
        this.descriptionTextField.setText(server.getDescription());
        this.refreshRegionText();
    }

    @Override
    public void onUnloaded(RealmsServer server) {
        this.saveSettings();
    }

    public void saveSettings() {
        String string = this.worldNameTextField.getText().trim();
        if (this.server.regionSelectionPreference != null && Objects.equals(string, this.server.name) && Objects.equals(this.descriptionTextField.getText(), this.server.description) && this.region.preference() == this.server.regionSelectionPreference.selectionMethod && this.region.region() == this.server.regionSelectionPreference.preferredRegion) {
            return;
        }
        this.screen.saveSettings(string, this.descriptionTextField.getText(), this.region.preference(), this.region.region());
    }

    @Environment(value=EnvType.CLIENT)
    public record Region(RegionSelectionMethod preference, @Nullable RealmsRegion region) {
    }
}

