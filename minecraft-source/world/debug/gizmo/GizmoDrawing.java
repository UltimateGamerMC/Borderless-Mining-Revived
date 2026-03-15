/*
 * External method calls:
 *   Lnet/minecraft/world/debug/gizmo/GizmoCollector;collect(Lnet/minecraft/world/debug/gizmo/Gizmo;)Lnet/minecraft/world/debug/gizmo/VisibilityConfigurable;
 *   Lnet/minecraft/world/debug/gizmo/QuadGizmo;ofFace(Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/util/math/Direction;Lnet/minecraft/client/render/DrawStyle;)Lnet/minecraft/world/debug/gizmo/QuadGizmo;
 *   Lnet/minecraft/world/debug/gizmo/TextGizmo$Style;left(I)Lnet/minecraft/world/debug/gizmo/TextGizmo$Style;
 *   Lnet/minecraft/world/debug/gizmo/TextGizmo$Style;scaled(F)Lnet/minecraft/world/debug/gizmo/TextGizmo$Style;
 *   Lnet/minecraft/world/debug/gizmo/VisibilityConfigurable;ignoreOcclusion()Lnet/minecraft/world/debug/gizmo/VisibilityConfigurable;
 *   Lnet/minecraft/world/debug/gizmo/TextGizmo$Style;centered(I)Lnet/minecraft/world/debug/gizmo/TextGizmo$Style;
 *   Lnet/minecraft/world/debug/gizmo/TextGizmo$Style;adjusted(F)Lnet/minecraft/world/debug/gizmo/TextGizmo$Style;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/world/debug/gizmo/GizmoDrawing;box(Lnet/minecraft/util/math/Box;Lnet/minecraft/client/render/DrawStyle;Z)Lnet/minecraft/world/debug/gizmo/VisibilityConfigurable;
 *   Lnet/minecraft/world/debug/gizmo/GizmoDrawing;collect(Lnet/minecraft/world/debug/gizmo/Gizmo;)Lnet/minecraft/world/debug/gizmo/VisibilityConfigurable;
 *   Lnet/minecraft/world/debug/gizmo/GizmoDrawing;box(Lnet/minecraft/util/math/Box;Lnet/minecraft/client/render/DrawStyle;)Lnet/minecraft/world/debug/gizmo/VisibilityConfigurable;
 *   Lnet/minecraft/world/debug/gizmo/GizmoDrawing;text(Ljava/lang/String;Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/world/debug/gizmo/TextGizmo$Style;)Lnet/minecraft/world/debug/gizmo/VisibilityConfigurable;
 */
package net.minecraft.world.debug.gizmo;

import net.minecraft.client.render.DrawStyle;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.debug.gizmo.ArrowGizmo;
import net.minecraft.world.debug.gizmo.BoxGizmo;
import net.minecraft.world.debug.gizmo.CircleGizmo;
import net.minecraft.world.debug.gizmo.Gizmo;
import net.minecraft.world.debug.gizmo.GizmoCollector;
import net.minecraft.world.debug.gizmo.LineGizmo;
import net.minecraft.world.debug.gizmo.PointGizmo;
import net.minecraft.world.debug.gizmo.QuadGizmo;
import net.minecraft.world.debug.gizmo.TextGizmo;
import net.minecraft.world.debug.gizmo.VisibilityConfigurable;
import org.jspecify.annotations.Nullable;

public class GizmoDrawing {
    static final ThreadLocal<@Nullable GizmoCollector> CURRENT_GIZMO_COLLECTOR = new ThreadLocal();

    private GizmoDrawing() {
    }

    public static CollectorScope using(GizmoCollector gizmoCollector) {
        CollectorScope lv = new CollectorScope();
        CURRENT_GIZMO_COLLECTOR.set(gizmoCollector);
        return lv;
    }

    public static VisibilityConfigurable collect(Gizmo gizmo) {
        GizmoCollector lv = CURRENT_GIZMO_COLLECTOR.get();
        if (lv == null) {
            throw new IllegalStateException("Gizmos cannot be created here! No GizmoCollector has been registered.");
        }
        return lv.collect(gizmo);
    }

    public static VisibilityConfigurable box(Box box, DrawStyle style) {
        return GizmoDrawing.box(box, style, false);
    }

    public static VisibilityConfigurable box(Box box, DrawStyle style, boolean coloredCornerStroke) {
        return GizmoDrawing.collect(new BoxGizmo(box, style, coloredCornerStroke));
    }

    public static VisibilityConfigurable box(BlockPos blockPos, DrawStyle style) {
        return GizmoDrawing.box(new Box(blockPos), style);
    }

    public static VisibilityConfigurable box(BlockPos blockPos, float expansion, DrawStyle style) {
        return GizmoDrawing.box(new Box(blockPos).expand(expansion), style);
    }

    public static VisibilityConfigurable circle(Vec3d pos, float radius, DrawStyle style) {
        return GizmoDrawing.collect(new CircleGizmo(pos, radius, style));
    }

    public static VisibilityConfigurable line(Vec3d start, Vec3d end, int color) {
        return GizmoDrawing.collect(new LineGizmo(start, end, color, 3.0f));
    }

    public static VisibilityConfigurable line(Vec3d start, Vec3d end, int color, float width) {
        return GizmoDrawing.collect(new LineGizmo(start, end, color, width));
    }

    public static VisibilityConfigurable arrow(Vec3d start, Vec3d end, int color) {
        return GizmoDrawing.collect(new ArrowGizmo(start, end, color, 2.5f));
    }

    public static VisibilityConfigurable arrow(Vec3d start, Vec3d end, int color, float width) {
        return GizmoDrawing.collect(new ArrowGizmo(start, end, color, width));
    }

    public static VisibilityConfigurable face(Vec3d nwd, Vec3d seu, Direction direction, DrawStyle style) {
        return GizmoDrawing.collect(QuadGizmo.ofFace(nwd, seu, direction, style));
    }

    public static VisibilityConfigurable quad(Vec3d a, Vec3d b, Vec3d c, Vec3d d, DrawStyle style) {
        return GizmoDrawing.collect(new QuadGizmo(a, b, c, d, style));
    }

    public static VisibilityConfigurable point(Vec3d pos, int color, float size) {
        return GizmoDrawing.collect(new PointGizmo(pos, color, size));
    }

    public static VisibilityConfigurable blockLabel(String text, BlockPos blockPos, int yOffset, int color, float scale) {
        double d = 1.3;
        double e = 0.2;
        VisibilityConfigurable lv = GizmoDrawing.text(text, Vec3d.add(blockPos, 0.5, 1.3 + (double)yOffset * 0.2, 0.5), TextGizmo.Style.left(color).scaled(scale));
        lv.ignoreOcclusion();
        return lv;
    }

    public static VisibilityConfigurable entityLabel(Entity entity, int yOffset, String text, int color, float scale) {
        double d = 2.4;
        double e = 0.25;
        double g = (double)entity.getBlockX() + 0.5;
        double h = entity.getY() + 2.4 + (double)yOffset * 0.25;
        double k = (double)entity.getBlockZ() + 0.5;
        float l = 0.5f;
        VisibilityConfigurable lv = GizmoDrawing.text(text, new Vec3d(g, h, k), TextGizmo.Style.centered(color).scaled(scale).adjusted(0.5f));
        lv.ignoreOcclusion();
        return lv;
    }

    public static VisibilityConfigurable text(String text, Vec3d pos, TextGizmo.Style style) {
        return GizmoDrawing.collect(new TextGizmo(pos, text, style));
    }

    public static class CollectorScope
    implements AutoCloseable {
        private final @Nullable GizmoCollector prevCollector = CURRENT_GIZMO_COLLECTOR.get();
        private boolean closed;

        CollectorScope() {
        }

        @Override
        public void close() {
            if (!this.closed) {
                this.closed = true;
                CURRENT_GIZMO_COLLECTOR.set(this.prevCollector);
            }
        }
    }
}

