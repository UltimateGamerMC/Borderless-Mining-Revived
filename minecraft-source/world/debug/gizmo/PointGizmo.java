/*
 * External method calls:
 *   Lnet/minecraft/world/debug/gizmo/GizmoDrawer;addPoint(Lnet/minecraft/util/math/Vec3d;IF)V
 */
package net.minecraft.world.debug.gizmo;

import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.debug.gizmo.Gizmo;
import net.minecraft.world.debug.gizmo.GizmoDrawer;

public record PointGizmo(Vec3d pos, int color, float size) implements Gizmo
{
    @Override
    public void draw(GizmoDrawer consumer, float opacity) {
        consumer.addPoint(this.pos, ColorHelper.scaleAlpha(this.color, opacity), this.size);
    }
}

