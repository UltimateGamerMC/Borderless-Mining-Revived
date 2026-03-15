/*
 * External method calls:
 *   Lnet/minecraft/client/render/DrawStyle;fill(F)I
 *   Lnet/minecraft/world/debug/gizmo/GizmoDrawer;addPolygon([Lnet/minecraft/util/math/Vec3d;I)V
 *   Lnet/minecraft/client/render/DrawStyle;stroke(F)I
 *   Lnet/minecraft/world/debug/gizmo/GizmoDrawer;addLine(Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/util/math/Vec3d;IF)V
 */
package net.minecraft.world.debug.gizmo;

import net.minecraft.client.render.DrawStyle;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.debug.gizmo.Gizmo;
import net.minecraft.world.debug.gizmo.GizmoDrawer;

public record CircleGizmo(Vec3d pos, float radius, DrawStyle style) implements Gizmo
{
    private static final int NUM_VERTICES = 20;
    private static final float ANGLE_INTERVAL = 0.31415927f;

    @Override
    public void draw(GizmoDrawer consumer, float opacity) {
        int i;
        if (!this.style.hasStroke() && !this.style.hasFill()) {
            return;
        }
        Vec3d[] lvs = new Vec3d[21];
        for (i = 0; i < 20; ++i) {
            Vec3d lv;
            float g = (float)i * 0.31415927f;
            lvs[i] = lv = this.pos.add((float)((double)this.radius * Math.cos(g)), 0.0, (float)((double)this.radius * Math.sin(g)));
        }
        lvs[20] = lvs[0];
        if (this.style.hasFill()) {
            i = this.style.fill(opacity);
            consumer.addPolygon(lvs, i);
        }
        if (this.style.hasStroke()) {
            i = this.style.stroke(opacity);
            for (int j = 0; j < 20; ++j) {
                consumer.addLine(lvs[j], lvs[j + 1], i, this.style.strokeWidth());
            }
        }
    }
}

