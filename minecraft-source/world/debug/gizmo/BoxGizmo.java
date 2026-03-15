/*
 * External method calls:
 *   Lnet/minecraft/client/render/DrawStyle;fill(F)I
 *   Lnet/minecraft/world/debug/gizmo/GizmoDrawer;addQuad(Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/util/math/Vec3d;I)V
 *   Lnet/minecraft/client/render/DrawStyle;stroke(F)I
 *   Lnet/minecraft/world/debug/gizmo/GizmoDrawer;addLine(Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/util/math/Vec3d;IF)V
 */
package net.minecraft.world.debug.gizmo;

import net.minecraft.client.render.DrawStyle;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.debug.gizmo.Gizmo;
import net.minecraft.world.debug.gizmo.GizmoDrawer;

public record BoxGizmo(Box aabb, DrawStyle style, boolean coloredCornerStroke) implements Gizmo
{
    @Override
    public void draw(GizmoDrawer consumer, float opacity) {
        int k;
        double d = this.aabb.minX;
        double e = this.aabb.minY;
        double g = this.aabb.minZ;
        double h = this.aabb.maxX;
        double i = this.aabb.maxY;
        double j = this.aabb.maxZ;
        if (this.style.hasFill()) {
            k = this.style.fill(opacity);
            consumer.addQuad(new Vec3d(h, e, g), new Vec3d(h, i, g), new Vec3d(h, i, j), new Vec3d(h, e, j), k);
            consumer.addQuad(new Vec3d(d, e, g), new Vec3d(d, e, j), new Vec3d(d, i, j), new Vec3d(d, i, g), k);
            consumer.addQuad(new Vec3d(d, e, g), new Vec3d(d, i, g), new Vec3d(h, i, g), new Vec3d(h, e, g), k);
            consumer.addQuad(new Vec3d(d, e, j), new Vec3d(h, e, j), new Vec3d(h, i, j), new Vec3d(d, i, j), k);
            consumer.addQuad(new Vec3d(d, i, g), new Vec3d(d, i, j), new Vec3d(h, i, j), new Vec3d(h, i, g), k);
            consumer.addQuad(new Vec3d(d, e, g), new Vec3d(h, e, g), new Vec3d(h, e, j), new Vec3d(d, e, j), k);
        }
        if (this.style.hasStroke()) {
            k = this.style.stroke(opacity);
            consumer.addLine(new Vec3d(d, e, g), new Vec3d(h, e, g), this.coloredCornerStroke ? ColorHelper.mix(k, -34953) : k, this.style.strokeWidth());
            consumer.addLine(new Vec3d(d, e, g), new Vec3d(d, i, g), this.coloredCornerStroke ? ColorHelper.mix(k, -8913033) : k, this.style.strokeWidth());
            consumer.addLine(new Vec3d(d, e, g), new Vec3d(d, e, j), this.coloredCornerStroke ? ColorHelper.mix(k, -8947713) : k, this.style.strokeWidth());
            consumer.addLine(new Vec3d(h, e, g), new Vec3d(h, i, g), k, this.style.strokeWidth());
            consumer.addLine(new Vec3d(h, i, g), new Vec3d(d, i, g), k, this.style.strokeWidth());
            consumer.addLine(new Vec3d(d, i, g), new Vec3d(d, i, j), k, this.style.strokeWidth());
            consumer.addLine(new Vec3d(d, i, j), new Vec3d(d, e, j), k, this.style.strokeWidth());
            consumer.addLine(new Vec3d(d, e, j), new Vec3d(h, e, j), k, this.style.strokeWidth());
            consumer.addLine(new Vec3d(h, e, j), new Vec3d(h, e, g), k, this.style.strokeWidth());
            consumer.addLine(new Vec3d(d, i, j), new Vec3d(h, i, j), k, this.style.strokeWidth());
            consumer.addLine(new Vec3d(h, e, j), new Vec3d(h, i, j), k, this.style.strokeWidth());
            consumer.addLine(new Vec3d(h, i, g), new Vec3d(h, i, j), k, this.style.strokeWidth());
        }
    }
}

