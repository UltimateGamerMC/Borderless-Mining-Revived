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
import net.minecraft.world.World;
import net.minecraft.world.chunk.WorldChunk;
import org.jspecify.annotations.Nullable;

@Environment(value=EnvType.CLIENT)
public class SoundMoodDebugHudEntry
implements DebugHudEntry {
    @Override
    public void render(DebugHudLines lines, @Nullable World world, @Nullable WorldChunk clientChunk, @Nullable WorldChunk chunk) {
        MinecraftClient lv = MinecraftClient.getInstance();
        if (lv.player == null) {
            return;
        }
        lines.addLine(lv.getSoundManager().getDebugString() + String.format(Locale.ROOT, " (Mood %d%%)", Math.round(lv.player.getMoodPercentage() * 100.0f)));
    }
}

