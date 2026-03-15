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
import net.minecraft.entity.Entity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.World;
import net.minecraft.world.chunk.WorldChunk;
import org.jspecify.annotations.Nullable;

@Environment(value=EnvType.CLIENT)
public class LocalDifficultyDebugHudEntry
implements DebugHudEntry {
    @Override
    public void render(DebugHudLines lines, @Nullable World world, @Nullable WorldChunk clientChunk, @Nullable WorldChunk chunk) {
        MinecraftClient lv = MinecraftClient.getInstance();
        Entity lv2 = lv.getCameraEntity();
        if (lv2 == null || chunk == null || !(world instanceof ServerWorld)) {
            return;
        }
        ServerWorld lv3 = (ServerWorld)world;
        BlockPos lv4 = lv2.getBlockPos();
        if (lv3.isInHeightLimit(lv4.getY())) {
            float f = lv3.getMoonSize(lv4);
            long l = chunk.getInhabitedTime();
            LocalDifficulty lv5 = new LocalDifficulty(lv3.getDifficulty(), lv3.getTimeOfDay(), l, f);
            lines.addLine(String.format(Locale.ROOT, "Local Difficulty: %.2f // %.2f (Day %d)", Float.valueOf(lv5.getLocalDifficulty()), Float.valueOf(lv5.getClampedLocalDifficulty()), lv3.getDay()));
        }
    }
}

