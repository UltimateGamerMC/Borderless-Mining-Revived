/*
 * External method calls:
 *   Lnet/minecraft/world/debug/gizmo/GizmoDrawer;addText(Lnet/minecraft/util/math/Vec3d;Ljava/lang/String;Lnet/minecraft/world/debug/gizmo/TextGizmo$Style;)V
 */
package net.minecraft.world.debug.gizmo;

import java.util.OptionalDouble;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.debug.gizmo.Gizmo;
import net.minecraft.world.debug.gizmo.GizmoDrawer;

public record TextGizmo(Vec3d pos, String text, Style style) implements Gizmo
{
    @Override
    public void draw(GizmoDrawer consumer, float opacity) {
        Style lv = opacity < 1.0f ? new Style(ColorHelper.scaleAlpha(this.style.color, opacity), this.style.scale, this.style.adjustLeft) : this.style;
        consumer.addText(this.pos, this.text, lv);
    }

    public record Style(int color, float scale, OptionalDouble adjustLeft) {
        public static final float DEFAULT_SCALE = 0.32f;

        public static Style left() {
            return new Style(-1, 0.32f, OptionalDouble.empty());
        }

        public static Style left(int color) {
            return new Style(color, 0.32f, OptionalDouble.empty());
        }

        public static Style centered(int color) {
            return new Style(color, 0.32f, OptionalDouble.of(0.0));
        }

        public Style scaled(float scale) {
            return new Style(this.color, scale, this.adjustLeft);
        }

        public Style adjusted(float adjustLeft) {
            return new Style(this.color, this.scale, OptionalDouble.of(adjustLeft));
        }
    }
}

