/*
 * External method calls:
 *   Lnet/minecraft/client/render/DrawStyle;stroked(I)Lnet/minecraft/client/render/DrawStyle;
 *   Lnet/minecraft/world/debug/gizmo/GizmoDrawing;box(Lnet/minecraft/util/math/Box;Lnet/minecraft/client/render/DrawStyle;)Lnet/minecraft/world/debug/gizmo/VisibilityConfigurable;
 */
package net.minecraft.client.render.debug;

import com.google.common.collect.ImmutableList;
import java.util.Collections;
import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.DrawStyle;
import net.minecraft.client.render.Frustum;
import net.minecraft.client.render.debug.DebugRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.Util;
import net.minecraft.util.math.Box;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.debug.DebugDataStore;
import net.minecraft.world.debug.gizmo.GizmoDrawing;

@Environment(value=EnvType.CLIENT)
public class CollisionDebugRenderer
implements DebugRenderer.Renderer {
    private final MinecraftClient client;
    private double lastUpdateTime = Double.MIN_VALUE;
    private List<VoxelShape> collisions = Collections.emptyList();

    public CollisionDebugRenderer(MinecraftClient client) {
        this.client = client;
    }

    @Override
    public void render(double cameraX, double cameraY, double cameraZ, DebugDataStore store, Frustum frustum, float tickProgress) {
        double h = Util.getMeasuringTimeNano();
        if (h - this.lastUpdateTime > 1.0E8) {
            this.lastUpdateTime = h;
            Entity lv = this.client.gameRenderer.getCamera().getFocusedEntity();
            this.collisions = ImmutableList.copyOf(lv.getEntityWorld().getCollisions(lv, lv.getBoundingBox().expand(6.0)));
        }
        for (VoxelShape lv2 : this.collisions) {
            DrawStyle lv3 = DrawStyle.stroked(-1);
            for (Box lv4 : lv2.getBoundingBoxes()) {
                GizmoDrawing.box(lv4, lv3);
            }
        }
    }
}

