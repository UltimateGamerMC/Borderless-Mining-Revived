/*
 * External method calls:
 *   Lnet/minecraft/client/gui/DrawContext;drawTooltip(Lnet/minecraft/client/font/TextRenderer;Ljava/util/List;Lnet/minecraft/client/gui/tooltip/TooltipPositioner;IIZ)V
 *   Lnet/minecraft/client/gui/tooltip/Tooltip;appendNarrations(Lnet/minecraft/client/gui/screen/narration/NarrationMessageBuilder;)V
 *
 * Internal private/static methods:
 *   Lnet/minecraft/client/gui/tooltip/TooltipState;createPositioner(Lnet/minecraft/client/gui/ScreenRect;ZZ)Lnet/minecraft/client/gui/tooltip/TooltipPositioner;
 */
package net.minecraft.client.gui.tooltip;

import java.time.Duration;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.ScreenRect;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.tooltip.FocusedTooltipPositioner;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.tooltip.TooltipPositioner;
import net.minecraft.client.gui.tooltip.WidgetTooltipPositioner;
import net.minecraft.util.Util;
import org.jspecify.annotations.Nullable;

@Environment(value=EnvType.CLIENT)
public class TooltipState {
    private @Nullable Tooltip tooltip;
    private Duration delay = Duration.ZERO;
    private long renderCheckTime;
    private boolean lastShouldRender;

    public void setDelay(Duration delay) {
        this.delay = delay;
    }

    public void setTooltip(@Nullable Tooltip tooltip) {
        this.tooltip = tooltip;
    }

    public @Nullable Tooltip getTooltip() {
        return this.tooltip;
    }

    public void render(DrawContext context, int mouseX, int mouseY, boolean hovered, boolean focused, ScreenRect navigationFocus) {
        boolean bl3;
        if (this.tooltip == null) {
            this.lastShouldRender = false;
            return;
        }
        MinecraftClient lv = MinecraftClient.getInstance();
        boolean bl = bl3 = hovered || focused && lv.getNavigationType().isKeyboard();
        if (bl3 != this.lastShouldRender) {
            if (bl3) {
                this.renderCheckTime = Util.getMeasuringTimeMs();
            }
            this.lastShouldRender = bl3;
        }
        if (bl3 && Util.getMeasuringTimeMs() - this.renderCheckTime > this.delay.toMillis()) {
            context.drawTooltip(lv.textRenderer, this.tooltip.getLines(lv), this.createPositioner(navigationFocus, hovered, focused), mouseX, mouseY, focused);
        }
    }

    private TooltipPositioner createPositioner(ScreenRect focus, boolean hovered, boolean focused) {
        if (!hovered && focused && MinecraftClient.getInstance().getNavigationType().isKeyboard()) {
            return new FocusedTooltipPositioner(focus);
        }
        return new WidgetTooltipPositioner(focus);
    }

    public void appendNarrations(NarrationMessageBuilder builder) {
        if (this.tooltip != null) {
            this.tooltip.appendNarrations(builder);
        }
    }
}

