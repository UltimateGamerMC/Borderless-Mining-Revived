/*
 * External method calls:
 *   Lnet/minecraft/client/gui/hud/debug/DebugHudLines;addLine(Ljava/lang/String;)V
 */
package net.minecraft.client.gui.hud.debug;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.debug.DebugHudEntry;
import net.minecraft.client.gui.hud.debug.DebugHudLines;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import net.minecraft.world.chunk.WorldChunk;
import org.jspecify.annotations.Nullable;

@Environment(value=EnvType.CLIENT)
public class GpuUtilizationDebugHudEntry
implements DebugHudEntry {
    @Override
    public void render(DebugHudLines lines, @Nullable World world, @Nullable WorldChunk clientChunk, @Nullable WorldChunk chunk) {
        MinecraftClient lv = MinecraftClient.getInstance();
        String string = "GPU: " + (lv.getGpuUtilizationPercentage() > 100.0 ? String.valueOf(Formatting.RED) + "100%" : Math.round(lv.getGpuUtilizationPercentage()) + "%");
        lines.addLine(string);
    }

    @Override
    public boolean canShow(boolean reducedDebugInfo) {
        return true;
    }
}

