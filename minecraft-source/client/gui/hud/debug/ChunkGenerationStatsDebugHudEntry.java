/*
 * External method calls:
 *   Lnet/minecraft/world/gen/chunk/ChunkGenerator;appendDebugHudText(Ljava/util/List;Lnet/minecraft/world/gen/noise/NoiseConfig;Lnet/minecraft/util/math/BlockPos;)V
 *   Lnet/minecraft/world/biome/source/BiomeSource;addDebugInfo(Ljava/util/List;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/world/biome/source/util/MultiNoiseUtil$MultiNoiseSampler;)V
 *   Lnet/minecraft/client/gui/hud/debug/DebugHudLines;addLinesToSection(Lnet/minecraft/util/Identifier;Ljava/util/Collection;)V
 *   Lnet/minecraft/util/Identifier;ofVanilla(Ljava/lang/String;)Lnet/minecraft/util/Identifier;
 */
package net.minecraft.client.gui.hud.debug;

import java.util.ArrayList;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.debug.DebugHudEntry;
import net.minecraft.client.gui.hud.debug.DebugHudLines;
import net.minecraft.entity.Entity;
import net.minecraft.server.world.ServerChunkManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.biome.source.util.MultiNoiseUtil;
import net.minecraft.world.chunk.WorldChunk;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.noise.NoiseConfig;
import org.jspecify.annotations.Nullable;

@Environment(value=EnvType.CLIENT)
public class ChunkGenerationStatsDebugHudEntry
implements DebugHudEntry {
    private static final Identifier SECTION_ID = Identifier.ofVanilla("chunk_generation");

    @Override
    public void render(DebugHudLines lines, @Nullable World world, @Nullable WorldChunk clientChunk, @Nullable WorldChunk chunk) {
        ServerWorld lv3;
        MinecraftClient lv = MinecraftClient.getInstance();
        Entity lv2 = lv.getCameraEntity();
        ServerWorld serverWorld = lv3 = world instanceof ServerWorld ? (ServerWorld)world : null;
        if (lv2 == null || lv3 == null) {
            return;
        }
        BlockPos lv4 = lv2.getBlockPos();
        ServerChunkManager lv5 = lv3.getChunkManager();
        ArrayList<String> list = new ArrayList<String>();
        ChunkGenerator lv6 = lv5.getChunkGenerator();
        NoiseConfig lv7 = lv5.getNoiseConfig();
        lv6.appendDebugHudText(list, lv7, lv4);
        MultiNoiseUtil.MultiNoiseSampler lv8 = lv7.getMultiNoiseSampler();
        BiomeSource lv9 = lv6.getBiomeSource();
        lv9.addDebugInfo(list, lv4, lv8);
        if (chunk != null && chunk.usesOldNoise()) {
            list.add("Blending: Old");
        }
        lines.addLinesToSection(SECTION_ID, list);
    }
}

