/*
 * External method calls:
 *   Lnet/minecraft/world/chunk/light/LightingProvider;displaySectionLevel(Lnet/minecraft/world/LightType;Lnet/minecraft/util/math/ChunkSectionPos;)Ljava/lang/String;
 *   Lnet/minecraft/world/debug/gizmo/TextGizmo$Style;left(I)Lnet/minecraft/world/debug/gizmo/TextGizmo$Style;
 *   Lnet/minecraft/world/debug/gizmo/TextGizmo$Style;scaled(F)Lnet/minecraft/world/debug/gizmo/TextGizmo$Style;
 *   Lnet/minecraft/world/debug/gizmo/GizmoDrawing;text(Ljava/lang/String;Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/world/debug/gizmo/TextGizmo$Style;)Lnet/minecraft/world/debug/gizmo/VisibilityConfigurable;
 */
package net.minecraft.client.render.debug;

import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Frustum;
import net.minecraft.client.render.debug.DebugRenderer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.Colors;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkSectionPos;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.LightType;
import net.minecraft.world.debug.DebugDataStore;
import net.minecraft.world.debug.gizmo.GizmoDrawing;
import net.minecraft.world.debug.gizmo.TextGizmo;

@Environment(value=EnvType.CLIENT)
public class SkyLightDebugRenderer
implements DebugRenderer.Renderer {
    private final MinecraftClient client;
    private final boolean visualizeBlockLightLevels;
    private final boolean visualizeSkyLightLevels;
    private static final int RANGE = 10;

    public SkyLightDebugRenderer(MinecraftClient client, boolean visualizeBlockLightLevels, boolean visualizeSkyLightLevels) {
        this.client = client;
        this.visualizeBlockLightLevels = visualizeBlockLightLevels;
        this.visualizeSkyLightLevels = visualizeSkyLightLevels;
    }

    @Override
    public void render(double cameraX, double cameraY, double cameraZ, DebugDataStore store, Frustum frustum, float tickProgress) {
        ClientWorld lv = this.client.world;
        BlockPos lv2 = BlockPos.ofFloored(cameraX, cameraY, cameraZ);
        LongOpenHashSet longSet = new LongOpenHashSet();
        for (BlockPos lv3 : BlockPos.iterate(lv2.add(-10, -10, -10), lv2.add(10, 10, 10))) {
            int j;
            int i = lv.getLightLevel(LightType.SKY, lv3);
            long l = ChunkSectionPos.fromBlockPos(lv3.asLong());
            if (longSet.add(l)) {
                GizmoDrawing.text(lv.getChunkManager().getLightingProvider().displaySectionLevel(LightType.SKY, ChunkSectionPos.from(l)), new Vec3d(ChunkSectionPos.getOffsetPos(ChunkSectionPos.unpackX(l), 8), ChunkSectionPos.getOffsetPos(ChunkSectionPos.unpackY(l), 8), ChunkSectionPos.getOffsetPos(ChunkSectionPos.unpackZ(l), 8)), TextGizmo.Style.left(-65536).scaled(4.8f));
            }
            if (i != 15 && this.visualizeSkyLightLevels) {
                j = ColorHelper.lerp((float)i / 15.0f, Colors.BLUE, -16711681);
                GizmoDrawing.text(String.valueOf(i), Vec3d.add(lv3, 0.5, 0.25, 0.5), TextGizmo.Style.left(j));
            }
            if (!this.visualizeBlockLightLevels || (j = lv.getLightLevel(LightType.BLOCK, lv3)) == 0) continue;
            int k = ColorHelper.lerp((float)j / 15.0f, -5636096, Colors.YELLOW);
            GizmoDrawing.text(String.valueOf(lv.getLightLevel(LightType.BLOCK, lv3)), Vec3d.ofCenter(lv3), TextGizmo.Style.left(k));
        }
    }
}

