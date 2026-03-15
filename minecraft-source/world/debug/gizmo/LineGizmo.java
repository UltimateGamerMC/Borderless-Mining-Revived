/*
 * External method calls:
 *   Lnet/minecraft/world/debug/gizmo/GizmoDrawer;addLine(Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/util/math/Vec3d;IF)V
 */
package net.minecraft.world.debug.gizmo;

import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.debug.gizmo.Gizmo;
import net.minecraft.world.debug.gizmo.GizmoDrawer;

public record LineGizmo(Vec3d start, Vec3d end, int color, float width) implements Gizmo
{
    public static final float field_63659 = 3.0f;

    @Override
    public void draw(GizmoDrawer consumer, float opacity) {
        consumer.addLine(this.start, this.end, ColorHelper.scaleAlpha(this.color, opacity), this.width);
    }
}

