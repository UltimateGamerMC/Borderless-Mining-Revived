/*
 * External method calls:
 *   Lnet/minecraft/client/render/DrawStyle;filled(I)Lnet/minecraft/client/render/DrawStyle;
 *   Lnet/minecraft/world/debug/gizmo/GizmoDrawing;box(Lnet/minecraft/util/math/BlockPos;FLnet/minecraft/client/render/DrawStyle;)Lnet/minecraft/world/debug/gizmo/VisibilityConfigurable;
 *   Lnet/minecraft/world/debug/gizmo/TextGizmo$Style;left()Lnet/minecraft/world/debug/gizmo/TextGizmo$Style;
 *   Lnet/minecraft/world/debug/gizmo/TextGizmo$Style;scaled(F)Lnet/minecraft/world/debug/gizmo/TextGizmo$Style;
 *   Lnet/minecraft/world/debug/gizmo/GizmoDrawing;text(Ljava/lang/String;Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/world/debug/gizmo/TextGizmo$Style;)Lnet/minecraft/world/debug/gizmo/VisibilityConfigurable;
 *   Lnet/minecraft/world/debug/gizmo/VisibilityConfigurable;ignoreOcclusion()Lnet/minecraft/world/debug/gizmo/VisibilityConfigurable;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/client/render/debug/GameTestDebugRenderer;render(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/client/render/debug/GameTestDebugRenderer$Marker;)V
 */
package net.minecraft.client.render.debug;

import com.google.common.collect.Maps;
import java.util.Map;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.DrawStyle;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.debug.gizmo.GizmoDrawing;
import net.minecraft.world.debug.gizmo.TextGizmo;

@Environment(value=EnvType.CLIENT)
public class GameTestDebugRenderer {
    private static final int MARKER_LIFESPAN_MS = 10000;
    private static final float MARKER_BOX_SIZE = 0.02f;
    private final Map<BlockPos, Marker> markers = Maps.newHashMap();

    public void addMarker(BlockPos absolutePos, BlockPos relativePos) {
        String string = relativePos.toShortString();
        this.markers.put(absolutePos, new Marker(0x6000FF00, string, Util.getMeasuringTimeMs() + 10000L));
    }

    public void clear() {
        this.markers.clear();
    }

    public void render() {
        long l = Util.getMeasuringTimeMs();
        this.markers.entrySet().removeIf(marker -> l > ((Marker)marker.getValue()).removalTime);
        this.markers.forEach((pos, marker) -> this.render((BlockPos)pos, (Marker)marker));
    }

    private void render(BlockPos blockPos, Marker marker) {
        GizmoDrawing.box(blockPos, 0.02f, DrawStyle.filled(marker.color()));
        if (!marker.message.isEmpty()) {
            GizmoDrawing.text(marker.message, Vec3d.add(blockPos, 0.5, 1.2, 0.5), TextGizmo.Style.left().scaled(0.16f)).ignoreOcclusion();
        }
    }

    @Environment(value=EnvType.CLIENT)
    record Marker(int color, String message, long removalTime) {
    }
}

