/*
 * External method calls:
 *   Lnet/minecraft/world/debug/DebugDataStore;forEachEntityData(Lnet/minecraft/world/debug/DebugSubscriptionType;Ljava/util/function/BiConsumer;)V
 *   Lnet/minecraft/world/debug/data/GoalSelectorDebugData;goals()Ljava/util/List;
 *   Lnet/minecraft/world/debug/data/GoalSelectorDebugData$Goal;name()Ljava/lang/String;
 *   Lnet/minecraft/world/debug/gizmo/TextGizmo$Style;left(I)Lnet/minecraft/world/debug/gizmo/TextGizmo$Style;
 *   Lnet/minecraft/world/debug/gizmo/GizmoDrawing;text(Ljava/lang/String;Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/world/debug/gizmo/TextGizmo$Style;)Lnet/minecraft/world/debug/gizmo/VisibilityConfigurable;
 */
package net.minecraft.client.render.debug;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.Frustum;
import net.minecraft.client.render.debug.DebugRenderer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.debug.DebugDataStore;
import net.minecraft.world.debug.DebugSubscriptionTypes;
import net.minecraft.world.debug.data.GoalSelectorDebugData;
import net.minecraft.world.debug.gizmo.GizmoDrawing;
import net.minecraft.world.debug.gizmo.TextGizmo;

@Environment(value=EnvType.CLIENT)
public class GoalSelectorDebugRenderer
implements DebugRenderer.Renderer {
    private static final int RANGE = 160;
    private final MinecraftClient client;

    public GoalSelectorDebugRenderer(MinecraftClient client) {
        this.client = client;
    }

    @Override
    public void render(double cameraX, double cameraY, double cameraZ, DebugDataStore store, Frustum frustum, float tickProgress) {
        Camera lv = this.client.gameRenderer.getCamera();
        BlockPos lv2 = BlockPos.ofFloored(lv.getCameraPos().x, 0.0, lv.getCameraPos().z);
        store.forEachEntityData(DebugSubscriptionTypes.GOAL_SELECTORS, (entity, data) -> {
            if (lv2.isWithinDistance(entity.getBlockPos(), 160.0)) {
                for (int i = 0; i < data.goals().size(); ++i) {
                    GoalSelectorDebugData.Goal lv = data.goals().get(i);
                    double d = (double)entity.getBlockX() + 0.5;
                    double e = entity.getY() + 2.0 + (double)i * 0.25;
                    double f = (double)entity.getBlockZ() + 0.5;
                    int j = lv.isRunning() ? -16711936 : -3355444;
                    GizmoDrawing.text(lv.name(), new Vec3d(d, e, f), TextGizmo.Style.left(j));
                }
            }
        });
    }
}

