/*
 * External method calls:
 *   Lnet/minecraft/client/gui/hud/debug/DebugHudLines;addLine(Ljava/lang/String;)V
 */
package net.minecraft.client.gui.hud.debug;

import java.util.Locale;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.debug.DebugHudEntry;
import net.minecraft.client.gui.hud.debug.DebugHudLines;
import net.minecraft.client.option.CloudRenderMode;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.TextureFilteringMode;
import net.minecraft.world.World;
import net.minecraft.world.chunk.WorldChunk;
import org.jspecify.annotations.Nullable;

@Environment(value=EnvType.CLIENT)
public class SimplePerformanceImpactorsDebugHudEntry
implements DebugHudEntry {
    @Override
    public void render(DebugHudLines lines, @Nullable World world, @Nullable WorldChunk clientChunk, @Nullable WorldChunk chunk) {
        MinecraftClient lv = MinecraftClient.getInstance();
        GameOptions lv2 = lv.options;
        Object[] objectArray = new Object[3];
        Object object = objectArray[0] = lv2.getImprovedTransparency().getValue() != false ? "improved-transparency" : "";
        objectArray[1] = lv2.getCloudRenderMode().getValue() == CloudRenderMode.OFF ? "" : (lv2.getCloudRenderMode().getValue() == CloudRenderMode.FAST ? " fast-clouds" : " fancy-clouds");
        objectArray[2] = lv2.getBiomeBlendRadius().getValue();
        lines.addLine(String.format(Locale.ROOT, "%s%s B: %d", objectArray));
        TextureFilteringMode lv3 = lv2.getTextureFiltering().getValue();
        if (lv3 == TextureFilteringMode.ANISOTROPIC) {
            lines.addLine(String.format(Locale.ROOT, "Filtering: %s %dx", lv3.getText().getString(), lv2.getEffectiveAnisotropy()));
        } else {
            lines.addLine(String.format(Locale.ROOT, "Filtering: %s", lv3.getText().getString()));
        }
    }

    @Override
    public boolean canShow(boolean reducedDebugInfo) {
        return true;
    }
}

