/*
 * External method calls:
 *   Lnet/minecraft/client/render/chunk/Octree;visit(Lnet/minecraft/client/render/chunk/Octree$Visitor;Lnet/minecraft/client/render/Frustum;I)V
 *   Lnet/minecraft/world/debug/gizmo/TextGizmo$Style;left(I)Lnet/minecraft/world/debug/gizmo/TextGizmo$Style;
 *   Lnet/minecraft/world/debug/gizmo/TextGizmo$Style;scaled(F)Lnet/minecraft/world/debug/gizmo/TextGizmo$Style;
 *   Lnet/minecraft/world/debug/gizmo/GizmoDrawing;text(Ljava/lang/String;Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/world/debug/gizmo/TextGizmo$Style;)Lnet/minecraft/world/debug/gizmo/VisibilityConfigurable;
 *   Lnet/minecraft/client/render/DrawStyle;stroked(I)Lnet/minecraft/client/render/DrawStyle;
 *   Lnet/minecraft/world/debug/gizmo/GizmoDrawing;box(Lnet/minecraft/util/math/Box;Lnet/minecraft/client/render/DrawStyle;)Lnet/minecraft/world/debug/gizmo/VisibilityConfigurable;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/client/render/debug/OctreeDebugRenderer;renderNode(Lnet/minecraft/client/render/chunk/Octree$Node;IZLorg/apache/commons/lang3/mutable/MutableInt;Z)V
 */
package net.minecraft.client.render.debug;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.DrawStyle;
import net.minecraft.client.render.Frustum;
import net.minecraft.client.render.chunk.Octree;
import net.minecraft.client.render.debug.DebugRenderer;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.debug.DebugDataStore;
import net.minecraft.world.debug.gizmo.GizmoDrawing;
import net.minecraft.world.debug.gizmo.TextGizmo;
import org.apache.commons.lang3.mutable.MutableInt;

@Environment(value=EnvType.CLIENT)
public class OctreeDebugRenderer
implements DebugRenderer.Renderer {
    private final MinecraftClient client;

    public OctreeDebugRenderer(MinecraftClient client) {
        this.client = client;
    }

    @Override
    public void render(double cameraX, double cameraY, double cameraZ, DebugDataStore store, Frustum frustum, float tickProgress) {
        Octree lv = this.client.worldRenderer.getChunkRenderingDataPreparer().getOctree();
        MutableInt mutableInt = new MutableInt(0);
        lv.visit((arg, bl, i, bl2) -> this.renderNode(arg, i, bl, mutableInt, bl2), frustum, 32);
    }

    private void renderNode(Octree.Node node, int i, boolean bl, MutableInt mutableInt, boolean bl2) {
        Box lv = node.getBoundingBox();
        double d = lv.getLengthX();
        long l = Math.round(d / 16.0);
        if (l == 1L) {
            mutableInt.add(1);
            int j = bl2 ? -16711936 : -1;
            GizmoDrawing.text(String.valueOf(mutableInt.intValue()), lv.getCenter(), TextGizmo.Style.left(j).scaled(4.8f));
        }
        long m = l + 5L;
        GizmoDrawing.box(lv.contract(0.1 * (double)i), DrawStyle.stroked(ColorHelper.fromFloats(bl ? 0.4f : 1.0f, OctreeDebugRenderer.getColorComponent(m, 0.3f), OctreeDebugRenderer.getColorComponent(m, 0.8f), OctreeDebugRenderer.getColorComponent(m, 0.5f))));
    }

    private static float getColorComponent(long size, float gradient) {
        float g = 0.1f;
        return MathHelper.fractionalPart(gradient * (float)size) * 0.9f + 0.1f;
    }
}

