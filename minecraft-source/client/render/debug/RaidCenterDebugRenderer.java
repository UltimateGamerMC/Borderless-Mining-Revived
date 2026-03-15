/*
 * External method calls:
 *   Lnet/minecraft/world/debug/DebugDataStore;forEachChunkData(Lnet/minecraft/world/debug/DebugSubscriptionType;Ljava/util/function/BiConsumer;)V
 *   Lnet/minecraft/client/render/DrawStyle;filled(I)Lnet/minecraft/client/render/DrawStyle;
 *   Lnet/minecraft/world/debug/gizmo/GizmoDrawing;box(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/client/render/DrawStyle;)Lnet/minecraft/world/debug/gizmo/VisibilityConfigurable;
 *   Lnet/minecraft/world/debug/gizmo/TextGizmo$Style;centered(I)Lnet/minecraft/world/debug/gizmo/TextGizmo$Style;
 *   Lnet/minecraft/world/debug/gizmo/TextGizmo$Style;scaled(F)Lnet/minecraft/world/debug/gizmo/TextGizmo$Style;
 *   Lnet/minecraft/world/debug/gizmo/GizmoDrawing;text(Ljava/lang/String;Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/world/debug/gizmo/TextGizmo$Style;)Lnet/minecraft/world/debug/gizmo/VisibilityConfigurable;
 *   Lnet/minecraft/world/debug/gizmo/VisibilityConfigurable;ignoreOcclusion()Lnet/minecraft/world/debug/gizmo/VisibilityConfigurable;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/client/render/debug/RaidCenterDebugRenderer;drawString(Ljava/lang/String;Lnet/minecraft/util/math/BlockPos;I)V
 *   Lnet/minecraft/client/render/debug/RaidCenterDebugRenderer;drawRaidCenter(Lnet/minecraft/util/math/BlockPos;)V
 */
package net.minecraft.client.render.debug;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.DrawStyle;
import net.minecraft.client.render.Frustum;
import net.minecraft.client.render.debug.DebugRenderer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.debug.DebugDataStore;
import net.minecraft.world.debug.DebugSubscriptionTypes;
import net.minecraft.world.debug.gizmo.GizmoDrawing;
import net.minecraft.world.debug.gizmo.TextGizmo;

@Environment(value=EnvType.CLIENT)
public class RaidCenterDebugRenderer
implements DebugRenderer.Renderer {
    private static final int RANGE = 160;
    private static final float DRAWN_STRING_SIZE = 0.64f;
    private final MinecraftClient client;

    public RaidCenterDebugRenderer(MinecraftClient client) {
        this.client = client;
    }

    @Override
    public void render(double cameraX, double cameraY, double cameraZ, DebugDataStore store, Frustum frustum, float tickProgress) {
        BlockPos lv = this.getCamera().getBlockPos();
        store.forEachChunkData(DebugSubscriptionTypes.RAIDS, (chunkPos, positions) -> {
            for (BlockPos lv : positions) {
                if (!lv.isWithinDistance(lv, 160.0)) continue;
                RaidCenterDebugRenderer.drawRaidCenter(lv);
            }
        });
    }

    private static void drawRaidCenter(BlockPos pos) {
        GizmoDrawing.box(pos, DrawStyle.filled(ColorHelper.fromFloats(0.15f, 1.0f, 0.0f, 0.0f)));
        RaidCenterDebugRenderer.drawString("Raid center", pos, -65536);
    }

    private static void drawString(String text, BlockPos pos, int color) {
        GizmoDrawing.text(text, Vec3d.add(pos, 0.5, 1.3, 0.5), TextGizmo.Style.centered(color).scaled(0.64f)).ignoreOcclusion();
    }

    private Camera getCamera() {
        return this.client.gameRenderer.getCamera();
    }
}

