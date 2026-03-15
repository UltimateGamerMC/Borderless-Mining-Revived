/*
 * External method calls:
 *   Lnet/minecraft/client/option/SimpleOption;emptyTooltip()Lnet/minecraft/client/option/SimpleOption$TooltipFactory;
 *   Lnet/minecraft/client/gui/widget/OptionListWidget;addHeader(Lnet/minecraft/text/Text;)V
 *   Lnet/minecraft/client/gui/widget/OptionListWidget;addSingleOptionEntry(Lnet/minecraft/client/option/SimpleOption;)V
 *   Lnet/minecraft/client/gui/widget/OptionListWidget;addAll([Lnet/minecraft/client/option/SimpleOption;)V
 *   Lnet/minecraft/client/MinecraftClient;reloadResourcesConcurrently()Ljava/util/concurrent/CompletableFuture;
 *   Lnet/minecraft/client/gui/screen/option/GameOptionsScreen;mouseClicked(Lnet/minecraft/client/gui/Click;Z)Z
 *   Lnet/minecraft/text/Text;translatable(Ljava/lang/String;[Ljava/lang/Object;)Lnet/minecraft/text/MutableText;
 *   Lnet/minecraft/text/MutableText;formatted(Lnet/minecraft/util/Formatting;)Lnet/minecraft/text/MutableText;
 *   Lnet/minecraft/client/gui/screen/option/GameOptionsScreen;mouseScrolled(DDDD)Z
 *   Lnet/minecraft/text/Text;translatable(Ljava/lang/String;)Lnet/minecraft/text/MutableText;
 */
package net.minecraft.client.gui.screen.option;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Optional;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.screen.GraphicsWarningScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.GameOptionsScreen;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.CyclingButtonWidget;
import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.SimpleOption;
import net.minecraft.client.option.TextureFilteringMode;
import net.minecraft.client.resource.VideoWarningManager;
import net.minecraft.client.util.Monitor;
import net.minecraft.client.util.VideoMode;
import net.minecraft.client.util.Window;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

@Environment(value=EnvType.CLIENT)
public class VideoOptionsScreen
extends GameOptionsScreen {
    private static final Text TITLE_TEXT = Text.translatable("options.videoTitle");
    private static final Text IMPROVED_TRANSPARENCY_TEXT = Text.translatable("options.improvedTransparency").formatted(Formatting.ITALIC);
    private static final Text GRAPHICS_WARNING_MESSAGE_TEXT = Text.translatable("options.graphics.warning.message", IMPROVED_TRANSPARENCY_TEXT, IMPROVED_TRANSPARENCY_TEXT);
    private static final Text GRAPHICS_WARNING_TITLE_TEXT = Text.translatable("options.graphics.warning.title").formatted(Formatting.RED);
    private static final Text GRAPHICS_WARNING_ACCEPT_TEXT = Text.translatable("options.graphics.warning.accept");
    private static final Text GRAPHICS_WARNING_CANCEL_TEXT = Text.translatable("options.graphics.warning.cancel");
    private static final Text DISPLAY_HEADER_TEXT = Text.translatable("options.video.display.header");
    private static final Text QUALITY_HEADER_TEXT = Text.translatable("options.video.quality.header");
    private static final Text INTERFACE_HEADER_TEXT = Text.translatable("options.video.preferences.header");
    private final VideoWarningManager warningManager;
    private final int mipmapLevels;
    private final int maxAnisotropy;
    private final TextureFilteringMode field_64673;

    private static SimpleOption<?>[] getQualityOptions(GameOptions options) {
        return new SimpleOption[]{options.getBiomeBlendRadius(), options.getViewDistance(), options.getChunkBuilderMode(), options.getSimulationDistance(), options.getAo(), options.getCloudRenderMode(), options.getParticles(), options.getMipmapLevels(), options.getEntityShadows(), options.getEntityDistanceScaling(), options.getMenuBackgroundBlurriness(), options.getCloudRenderDistance(), options.getCutoutLeaves(), options.getImprovedTransparency(), options.getTextureFiltering(), options.getMaxAnisotropy(), options.getWeatherRadius()};
    }

    private static SimpleOption<?>[] getDisplayOptions(GameOptions options) {
        return new SimpleOption[]{options.getMaxFps(), options.getEnableVsync(), options.getInactivityFpsLimit(), options.getGuiScale(), options.getFullscreen(), options.getGamma()};
    }

    private static SimpleOption<?>[] getInterfaceOptions(GameOptions options) {
        return new SimpleOption[]{options.getShowAutosaveIndicator(), options.getVignette(), options.getAttackIndicator(), options.getChunkFade()};
    }

    public VideoOptionsScreen(Screen parent, MinecraftClient client, GameOptions gameOptions) {
        super(parent, gameOptions, TITLE_TEXT);
        this.warningManager = client.getVideoWarningManager();
        this.warningManager.reset();
        if (gameOptions.getImprovedTransparency().getValue().booleanValue()) {
            this.warningManager.acceptAfterWarnings();
        }
        this.mipmapLevels = gameOptions.getMipmapLevels().getValue();
        this.maxAnisotropy = gameOptions.getMaxAnisotropy().getValue();
        this.field_64673 = gameOptions.getTextureFiltering().getValue();
    }

    @Override
    protected void addOptions() {
        int j;
        int i = -1;
        Window lv = this.client.getWindow();
        Monitor lv2 = lv.getMonitor();
        if (lv2 == null) {
            j = -1;
        } else {
            Optional<VideoMode> optional = lv.getFullscreenVideoMode();
            j = optional.map(lv2::findClosestVideoModeIndex).orElse(-1);
        }
        SimpleOption<Integer> lv3 = new SimpleOption<Integer>("options.fullscreen.resolution", SimpleOption.emptyTooltip(), (optionText, value) -> {
            if (lv2 == null) {
                return Text.translatable("options.fullscreen.unavailable");
            }
            if (value == -1) {
                return GameOptions.getGenericValueText(optionText, Text.translatable("options.fullscreen.current"));
            }
            VideoMode lv = lv2.getVideoMode((int)value);
            return GameOptions.getGenericValueText(optionText, Text.translatable("options.fullscreen.entry", lv.getWidth(), lv.getHeight(), lv.getRefreshRate(), lv.getRedBits() + lv.getGreenBits() + lv.getBlueBits()));
        }, new SimpleOption.ValidatingIntSliderCallbacks(-1, lv2 != null ? lv2.getVideoModeCount() - 1 : -1), j, value -> {
            if (lv2 == null) {
                return;
            }
            lv.setFullscreenVideoMode(value == -1 ? Optional.empty() : Optional.of(lv2.getVideoMode((int)value)));
        });
        this.body.addHeader(DISPLAY_HEADER_TEXT);
        this.body.addSingleOptionEntry(lv3);
        this.body.addAll(VideoOptionsScreen.getDisplayOptions(this.gameOptions));
        this.body.addHeader(QUALITY_HEADER_TEXT);
        this.body.addSingleOptionEntry(this.gameOptions.getPreset());
        this.body.addAll(VideoOptionsScreen.getQualityOptions(this.gameOptions));
        this.body.addHeader(INTERFACE_HEADER_TEXT);
        this.body.addAll(VideoOptionsScreen.getInterfaceOptions(this.gameOptions));
    }

    @Override
    public void tick() {
        ClickableWidget clickableWidget;
        if (this.body != null && (clickableWidget = this.body.getWidgetFor(this.gameOptions.getMaxAnisotropy())) instanceof SliderWidget) {
            SliderWidget lv = (SliderWidget)clickableWidget;
            lv.active = this.gameOptions.getTextureFiltering().getValue() == TextureFilteringMode.ANISOTROPIC;
        }
        super.tick();
    }

    @Override
    public void close() {
        this.client.getWindow().applyFullscreenVideoMode();
        super.close();
    }

    @Override
    public void removed() {
        if (this.gameOptions.getMipmapLevels().getValue() != this.mipmapLevels || this.gameOptions.getMaxAnisotropy().getValue() != this.maxAnisotropy || this.gameOptions.getTextureFiltering().getValue() != this.field_64673) {
            this.client.setMipmapLevels(this.gameOptions.getMipmapLevels().getValue());
            this.client.reloadResourcesConcurrently();
        }
        super.removed();
    }

    @Override
    public boolean mouseClicked(Click click, boolean doubled) {
        if (super.mouseClicked(click, doubled)) {
            if (this.warningManager.shouldWarn()) {
                String string3;
                String string2;
                ArrayList<Text> list = Lists.newArrayList(GRAPHICS_WARNING_MESSAGE_TEXT, ScreenTexts.LINE_BREAK);
                String string = this.warningManager.getRendererWarning();
                if (string != null) {
                    list.add(ScreenTexts.LINE_BREAK);
                    list.add(Text.translatable("options.graphics.warning.renderer", string).formatted(Formatting.GRAY));
                }
                if ((string2 = this.warningManager.getVendorWarning()) != null) {
                    list.add(ScreenTexts.LINE_BREAK);
                    list.add(Text.translatable("options.graphics.warning.vendor", string2).formatted(Formatting.GRAY));
                }
                if ((string3 = this.warningManager.getVersionWarning()) != null) {
                    list.add(ScreenTexts.LINE_BREAK);
                    list.add(Text.translatable("options.graphics.warning.version", string3).formatted(Formatting.GRAY));
                }
                this.client.setScreen(new GraphicsWarningScreen(GRAPHICS_WARNING_TITLE_TEXT, list, ImmutableList.of(new GraphicsWarningScreen.ChoiceButton(GRAPHICS_WARNING_ACCEPT_TEXT, button -> {
                    this.gameOptions.getImprovedTransparency().setValue(true);
                    MinecraftClient.getInstance().worldRenderer.reload();
                    this.warningManager.acceptAfterWarnings();
                    this.client.setScreen(this);
                }), new GraphicsWarningScreen.ChoiceButton(GRAPHICS_WARNING_CANCEL_TEXT, button -> {
                    this.warningManager.acceptAfterWarnings();
                    this.gameOptions.getImprovedTransparency().setValue(false);
                    this.updateImprovedTransparencyButtonValue();
                    this.client.setScreen(this);
                }))));
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        if (this.client.isCtrlPressed()) {
            SimpleOption<Integer> lv = this.gameOptions.getGuiScale();
            SimpleOption.Callbacks<Integer> callbacks = lv.getCallbacks();
            if (callbacks instanceof SimpleOption.MaxSuppliableIntCallbacks) {
                CyclingButtonWidget lv3;
                SimpleOption.MaxSuppliableIntCallbacks lv2 = (SimpleOption.MaxSuppliableIntCallbacks)callbacks;
                int i = lv.getValue();
                int j = i == 0 ? lv2.maxInclusive() + 1 : i;
                int k = j + (int)Math.signum(verticalAmount);
                if (k != 0 && k <= lv2.maxInclusive() && k >= lv2.minInclusive() && (lv3 = (CyclingButtonWidget)this.body.getWidgetFor(lv)) != null) {
                    lv.setValue(k);
                    lv3.setValue(k);
                    this.body.setScrollY(0.0);
                    return true;
                }
            }
            return false;
        }
        return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
    }

    public void updateFullscreenButtonValue(boolean fullscreen) {
        ClickableWidget lv;
        if (this.body != null && (lv = this.body.getWidgetFor(this.gameOptions.getFullscreen())) != null) {
            CyclingButtonWidget lv2 = (CyclingButtonWidget)lv;
            lv2.setValue(fullscreen);
        }
    }

    public void updateImprovedTransparencyButtonValue() {
        SimpleOption<Boolean> lv;
        ClickableWidget lv2;
        if (this.body != null && (lv2 = this.body.getWidgetFor(lv = this.gameOptions.getImprovedTransparency())) != null) {
            CyclingButtonWidget lv3 = (CyclingButtonWidget)lv2;
            lv3.setValue(lv.getValue());
        }
    }
}

