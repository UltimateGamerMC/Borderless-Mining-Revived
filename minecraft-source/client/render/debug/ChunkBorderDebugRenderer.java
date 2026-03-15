/*
 * External method calls:
 *   Lnet/minecraft/world/debug/gizmo/GizmoDrawing;line(Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/util/math/Vec3d;IF)Lnet/minecraft/world/debug/gizmo/VisibilityConfigurable;
 *   Lnet/minecraft/client/render/DrawStyle;stroked(IF)Lnet/minecraft/client/render/DrawStyle;
 *   Lnet/minecraft/world/debug/gizmo/GizmoDrawing;box(Lnet/minecraft/util/math/Box;Lnet/minecraft/client/render/DrawStyle;)Lnet/minecraft/world/debug/gizmo/VisibilityConfigurable;
 *   Lnet/minecraft/world/debug/gizmo/VisibilityConfigurable;ignoreOcclusion()Lnet/minecraft/world/debug/gizmo/VisibilityConfigurable;
 */
package net.minecraft.client.render.debug;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.DrawStyle;
import net.minecraft.client.render.Frustum;
import net.minecraft.client.render.debug.DebugRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.ChunkSectionPos;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.debug.DebugDataStore;
import net.minecraft.world.debug.gizmo.GizmoDrawing;

@Environment(value=EnvType.CLIENT)
public class ChunkBorderDebugRenderer
implements DebugRenderer.Renderer {
    private static final float field_63585 = 4.0f;
    private static final float field_63586 = 1.0f;
    private final MinecraftClient client;
    private static final int DARK_CYAN = ColorHelper.getArgb(255, 0, 155, 155);
    private static final int YELLOW = ColorHelper.getArgb(255, 255, 255, 0);
    private static final int LIGHT_RED = ColorHelper.fromFloats(1.0f, 0.25f, 0.25f, 1.0f);

    public ChunkBorderDebugRenderer(MinecraftClient client) {
        this.client = client;
    }

    @Override
    public void render(double cameraX, double cameraY, double cameraZ, DebugDataStore store, Frustum frustum, float tickProgress) {
        int m;
        int l;
        Entity lv = this.client.gameRenderer.getCamera().getFocusedEntity();
        float h = this.client.world.getBottomY();
        float i = this.client.world.getTopYInclusive() + 1;
        ChunkSectionPos lv2 = ChunkSectionPos.from(lv.getBlockPos());
        double j = lv2.getMinX();
        double k = lv2.getMinZ();
        for (l = -16; l <= 32; l += 16) {
            for (m = -16; m <= 32; m += 16) {
                GizmoDrawing.line(new Vec3d(j + (double)l, h, k + (double)m), new Vec3d(j + (double)l, i, k + (double)m), ColorHelper.fromFloats(0.5f, 1.0f, 0.0f, 0.0f), 4.0f);
            }
        }
        for (l = 2; l < 16; l += 2) {
            m = l % 4 == 0 ? DARK_CYAN : YELLOW;
            GizmoDrawing.line(new Vec3d(j + (double)l, h, k), new Vec3d(j + (double)l, i, k), m, 1.0f);
            GizmoDrawing.line(new Vec3d(j + (double)l, h, k + 16.0), new Vec3d(j + (double)l, i, k + 16.0), m, 1.0f);
        }
        for (l = 2; l < 16; l += 2) {
            m = l % 4 == 0 ? DARK_CYAN : YELLOW;
            GizmoDrawing.line(new Vec3d(j, h, k + (double)l), new Vec3d(j, i, k + (double)l), m, 1.0f);
            GizmoDrawing.line(new Vec3d(j + 16.0, h, k + (double)l), new Vec3d(j + 16.0, i, k + (double)l), m, 1.0f);
        }
        for (l = this.client.world.getBottomY(); l <= this.client.world.getTopYInclusive() + 1; l += 2) {
            float n = l;
            int o = l % 8 == 0 ? DARK_CYAN : YELLOW;
            GizmoDrawing.line(new Vec3d(j, n, k), new Vec3d(j, n, k + 16.0), o, 1.0f);
            GizmoDrawing.line(new Vec3d(j, n, k + 16.0), new Vec3d(j + 16.0, n, k + 16.0), o, 1.0f);
            GizmoDrawing.line(new Vec3d(j + 16.0, n, k + 16.0), new Vec3d(j + 16.0, n, k), o, 1.0f);
            GizmoDrawing.line(new Vec3d(j + 16.0, n, k), new Vec3d(j, n, k), o, 1.0f);
        }
        for (l = 0; l <= 16; l += 16) {
            for (int m2 = 0; m2 <= 16; m2 += 16) {
                GizmoDrawing.line(new Vec3d(j + (double)l, h, k + (double)m2), new Vec3d(j + (double)l, i, k + (double)m2), LIGHT_RED, 4.0f);
            }
        }
        GizmoDrawing.box(new Box(lv2.getMinX(), lv2.getMinY(), lv2.getMinZ(), lv2.getMaxX() + 1, lv2.getMaxY() + 1, lv2.getMaxZ() + 1), DrawStyle.stroked(LIGHT_RED, 1.0f)).ignoreOcclusion();
        for (l = this.client.world.getBottomY(); l <= this.client.world.getTopYInclusive() + 1; l += 16) {
            GizmoDrawing.line(new Vec3d(j, l, k), new Vec3d(j, l, k + 16.0), LIGHT_RED, 4.0f);
            GizmoDrawing.line(new Vec3d(j, l, k + 16.0), new Vec3d(j + 16.0, l, k + 16.0), LIGHT_RED, 4.0f);
            GizmoDrawing.line(new Vec3d(j + 16.0, l, k + 16.0), new Vec3d(j + 16.0, l, k), LIGHT_RED, 4.0f);
            GizmoDrawing.line(new Vec3d(j + 16.0, l, k), new Vec3d(j, l, k), LIGHT_RED, 4.0f);
        }
    }
}

