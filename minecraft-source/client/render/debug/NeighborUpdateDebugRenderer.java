/*
 * External method calls:
 *   Lnet/minecraft/world/debug/DebugDataStore;forEachEvent(Lnet/minecraft/world/debug/DebugSubscriptionType;Lnet/minecraft/world/debug/DebugDataStore$EventConsumer;)V
 *   Lnet/minecraft/client/render/DrawStyle;stroked(I)Lnet/minecraft/client/render/DrawStyle;
 *   Lnet/minecraft/world/debug/gizmo/GizmoDrawing;box(Lnet/minecraft/util/math/Box;Lnet/minecraft/client/render/DrawStyle;)Lnet/minecraft/world/debug/gizmo/VisibilityConfigurable;
 *   Lnet/minecraft/world/debug/gizmo/TextGizmo$Style;left()Lnet/minecraft/world/debug/gizmo/TextGizmo$Style;
 *   Lnet/minecraft/world/debug/gizmo/GizmoDrawing;text(Ljava/lang/String;Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/world/debug/gizmo/TextGizmo$Style;)Lnet/minecraft/world/debug/gizmo/VisibilityConfigurable;
 *   Lnet/minecraft/client/render/debug/NeighborUpdateDebugRenderer$Update;withAge(I)Lnet/minecraft/client/render/debug/NeighborUpdateDebugRenderer$Update;
 */
package net.minecraft.client.render.debug;

import java.util.HashMap;
import java.util.Map;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.DrawStyle;
import net.minecraft.client.render.Frustum;
import net.minecraft.client.render.debug.DebugRenderer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.debug.DebugDataStore;
import net.minecraft.world.debug.DebugSubscriptionTypes;
import net.minecraft.world.debug.gizmo.GizmoDrawing;
import net.minecraft.world.debug.gizmo.TextGizmo;

@Environment(value=EnvType.CLIENT)
public class NeighborUpdateDebugRenderer
implements DebugRenderer.Renderer {
    @Override
    public void render(double cameraX, double cameraY, double cameraZ, DebugDataStore store, Frustum frustum, float tickProgress) {
        Update lv2;
        BlockPos lv;
        int i = DebugSubscriptionTypes.NEIGHBOR_UPDATES.getExpiry();
        double h = 1.0 / (double)(i * 2);
        HashMap map = new HashMap();
        store.forEachEvent(DebugSubscriptionTypes.NEIGHBOR_UPDATES, (pos, remainingTime, expiry) -> {
            long l = expiry - remainingTime;
            Update lv = map.getOrDefault(pos, Update.EMPTY);
            map.put(pos, lv.withAge((int)l));
        });
        for (Map.Entry entry : map.entrySet()) {
            lv = (BlockPos)entry.getKey();
            lv2 = (Update)entry.getValue();
            Box lv3 = new Box(lv).expand(0.002).contract(h * (double)lv2.age);
            GizmoDrawing.box(lv3, DrawStyle.stroked(-1));
        }
        for (Map.Entry entry : map.entrySet()) {
            lv = (BlockPos)entry.getKey();
            lv2 = (Update)entry.getValue();
            GizmoDrawing.text(String.valueOf(lv2.count), Vec3d.ofCenter(lv), TextGizmo.Style.left());
        }
    }

    @Environment(value=EnvType.CLIENT)
    record Update(int count, int age) {
        static final Update EMPTY = new Update(0, Integer.MAX_VALUE);

        public Update withAge(int age) {
            if (age == this.age) {
                return new Update(this.count + 1, age);
            }
            if (age < this.age) {
                return new Update(1, age);
            }
            return this;
        }
    }
}

