/*
 * External method calls:
 *   Lnet/minecraft/world/debug/gizmo/GizmoDrawer;addLine(Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/util/math/Vec3d;IF)V
 */
package net.minecraft.world.debug.gizmo;

import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.debug.gizmo.Gizmo;
import net.minecraft.world.debug.gizmo.GizmoDrawer;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public record ArrowGizmo(Vec3d start, Vec3d end, int color, float width) implements Gizmo
{
    public static final float field_63652 = 2.5f;

    @Override
    public void draw(GizmoDrawer consumer, float opacity) {
        Vector3f[] vector3fs;
        int i = ColorHelper.scaleAlpha(this.color, opacity);
        consumer.addLine(this.start, this.end, i, this.width);
        Quaternionf quaternionf = new Quaternionf().rotationTo(new Vector3f(1.0f, 0.0f, 0.0f), this.end.subtract(this.start).toVector3f().normalize());
        float g = (float)MathHelper.clamp(this.end.distanceTo(this.start) * (double)0.1f, (double)0.1f, 1.0);
        for (Vector3f vector3f : vector3fs = new Vector3f[]{quaternionf.transform(-g, g, 0.0f, new Vector3f()), quaternionf.transform(-g, 0.0f, g, new Vector3f()), quaternionf.transform(-g, -g, 0.0f, new Vector3f()), quaternionf.transform(-g, 0.0f, -g, new Vector3f())}) {
            consumer.addLine(this.end.add(vector3f.x, vector3f.y, vector3f.z), this.end, i, this.width);
        }
    }
}

