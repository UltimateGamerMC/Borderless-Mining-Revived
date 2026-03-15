/*
 * External method calls:
 *   Lnet/minecraft/world/debug/DebugDataStore;forEachEntityData(Lnet/minecraft/world/debug/DebugSubscriptionType;Ljava/util/function/BiConsumer;)V
 *   Lnet/minecraft/world/debug/data/BreezeDebugData;attackTarget()Ljava/util/Optional;
 *   Lnet/minecraft/world/debug/data/BreezeDebugData;jumpTarget()Ljava/util/Optional;
 *   Lnet/minecraft/world/debug/gizmo/GizmoDrawing;arrow(Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/util/math/Vec3d;I)Lnet/minecraft/world/debug/gizmo/VisibilityConfigurable;
 *   Lnet/minecraft/client/render/DrawStyle;filled(I)Lnet/minecraft/client/render/DrawStyle;
 *   Lnet/minecraft/world/debug/gizmo/GizmoDrawing;box(Lnet/minecraft/util/math/Box;Lnet/minecraft/client/render/DrawStyle;)Lnet/minecraft/world/debug/gizmo/VisibilityConfigurable;
 *   Lnet/minecraft/client/render/DrawStyle;stroked(I)Lnet/minecraft/client/render/DrawStyle;
 *   Lnet/minecraft/world/debug/gizmo/GizmoDrawing;circle(Lnet/minecraft/util/math/Vec3d;FLnet/minecraft/client/render/DrawStyle;)Lnet/minecraft/world/debug/gizmo/VisibilityConfigurable;
 */
package net.minecraft.client.render.debug;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.DrawStyle;
import net.minecraft.client.render.Frustum;
import net.minecraft.client.render.debug.DebugRenderer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.debug.DebugDataStore;
import net.minecraft.world.debug.DebugSubscriptionTypes;
import net.minecraft.world.debug.gizmo.GizmoDrawing;

@Environment(value=EnvType.CLIENT)
public class BreezeDebugRenderer
implements DebugRenderer.Renderer {
    private static final int PINK = ColorHelper.getArgb(255, 255, 100, 255);
    private static final int LIGHT_BLUE = ColorHelper.getArgb(255, 100, 255, 255);
    private static final int GREEN = ColorHelper.getArgb(255, 0, 255, 0);
    private static final int ORANGE = ColorHelper.getArgb(255, 255, 165, 0);
    private static final int RED = ColorHelper.getArgb(255, 255, 0, 0);
    private final MinecraftClient client;

    public BreezeDebugRenderer(MinecraftClient client) {
        this.client = client;
    }

    @Override
    public void render(double cameraX, double cameraY, double cameraZ, DebugDataStore store, Frustum frustum, float tickProgress) {
        ClientWorld lv = this.client.world;
        store.forEachEntityData(DebugSubscriptionTypes.BREEZES, (entity, data) -> {
            data.attackTarget().map(lv::getEntityById).map(target -> target.getLerpedPos(this.client.getRenderTickCounter().getTickProgress(true))).ifPresent(targetPos -> {
                GizmoDrawing.arrow(entity.getEntityPos(), targetPos, LIGHT_BLUE);
                Vec3d lv = targetPos.add(0.0, 0.01f, 0.0);
                GizmoDrawing.circle(lv, 4.0f, DrawStyle.stroked(GREEN));
                GizmoDrawing.circle(lv, 8.0f, DrawStyle.stroked(ORANGE));
                GizmoDrawing.circle(lv, 24.0f, DrawStyle.stroked(RED));
            });
            data.jumpTarget().ifPresent(jumpTarget -> {
                GizmoDrawing.arrow(entity.getEntityPos(), jumpTarget.toCenterPos(), PINK);
                GizmoDrawing.box(Box.from(Vec3d.of(jumpTarget)), DrawStyle.filled(ColorHelper.fromFloats(1.0f, 1.0f, 0.0f, 0.0f)));
            });
        });
    }
}

