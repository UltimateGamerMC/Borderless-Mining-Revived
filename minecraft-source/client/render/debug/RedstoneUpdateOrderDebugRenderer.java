/*
 * External method calls:
 *   Lnet/minecraft/world/debug/DebugDataStore;forEachBlockData(Lnet/minecraft/world/debug/DebugSubscriptionType;Ljava/util/function/BiConsumer;)V
 *   Lnet/minecraft/world/debug/gizmo/GizmoDrawing;arrow(Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/util/math/Vec3d;I)Lnet/minecraft/world/debug/gizmo/VisibilityConfigurable;
 */
package net.minecraft.client.render.debug;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.Frustum;
import net.minecraft.client.render.debug.DebugRenderer;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.debug.DebugDataStore;
import net.minecraft.world.debug.DebugSubscriptionTypes;
import net.minecraft.world.debug.gizmo.GizmoDrawing;

@Environment(value=EnvType.CLIENT)
public class RedstoneUpdateOrderDebugRenderer
implements DebugRenderer.Renderer {
    @Override
    public void render(double cameraX, double cameraY, double cameraZ, DebugDataStore store, Frustum frustum, float tickProgress) {
        store.forEachBlockData(DebugSubscriptionTypes.REDSTONE_WIRE_ORIENTATIONS, (blockPos, orientation) -> {
            Vec3d lv = blockPos.toBottomCenterPos().subtract(0.0, 0.1, 0.0);
            GizmoDrawing.arrow(lv, lv.add(orientation.getFront().getDoubleVector().multiply(0.5)), -16776961);
            GizmoDrawing.arrow(lv, lv.add(orientation.getUp().getDoubleVector().multiply(0.4)), -65536);
            GizmoDrawing.arrow(lv, lv.add(orientation.getRight().getDoubleVector().multiply(0.3)), -256);
        });
    }
}

