/*
 * External method calls:
 *   Lnet/minecraft/world/debug/gizmo/GizmoDrawing;line(Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/util/math/Vec3d;I)Lnet/minecraft/world/debug/gizmo/VisibilityConfigurable;
 *   Lnet/minecraft/client/render/DrawStyle;filled(I)Lnet/minecraft/client/render/DrawStyle;
 *   Lnet/minecraft/world/debug/gizmo/GizmoDrawing;box(Lnet/minecraft/util/math/Box;Lnet/minecraft/client/render/DrawStyle;)Lnet/minecraft/world/debug/gizmo/VisibilityConfigurable;
 *   Lnet/minecraft/world/debug/gizmo/GizmoDrawing;quad(Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/client/render/DrawStyle;)Lnet/minecraft/world/debug/gizmo/VisibilityConfigurable;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/client/render/debug/ChunkDebugRenderer;addFace(Lnet/minecraft/util/math/Vec3d;[Lorg/joml/Vector4f;IIIIIII)V
 *   Lnet/minecraft/client/render/debug/ChunkDebugRenderer;addFrustumEdge(Lnet/minecraft/util/math/Vec3d;Lorg/joml/Vector4f;Lorg/joml/Vector4f;)V
 */
package net.minecraft.client.render.debug;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.debug.DebugHudEntries;
import net.minecraft.client.render.ChunkRenderingDataPreparer;
import net.minecraft.client.render.DrawStyle;
import net.minecraft.client.render.Frustum;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.render.chunk.ChunkBuilder;
import net.minecraft.client.render.debug.DebugRenderer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.debug.DebugDataStore;
import net.minecraft.world.debug.gizmo.GizmoDrawing;
import org.joml.Vector4f;

@Environment(value=EnvType.CLIENT)
public class ChunkDebugRenderer
implements DebugRenderer.Renderer {
    public static final Direction[] DIRECTIONS = Direction.values();
    private final MinecraftClient client;

    public ChunkDebugRenderer(MinecraftClient client) {
        this.client = client;
    }

    @Override
    public void render(double cameraX, double cameraY, double cameraZ, DebugDataStore store, Frustum frustum, float tickProgress) {
        Frustum lv9;
        WorldRenderer lv = this.client.worldRenderer;
        boolean bl = this.client.debugHudEntryList.isEntryVisible(DebugHudEntries.CHUNK_SECTION_PATHS);
        boolean bl2 = this.client.debugHudEntryList.isEntryVisible(DebugHudEntries.CHUNK_SECTION_VISIBILITY);
        if (bl || bl2) {
            ChunkRenderingDataPreparer lv2 = lv.getChunkRenderingDataPreparer();
            for (ChunkBuilder.BuiltChunk lv3 : lv.getBuiltChunks()) {
                int i;
                ChunkRenderingDataPreparer.ChunkInfo lv4 = lv2.getInfo(lv3);
                if (lv4 == null) continue;
                BlockPos lv5 = lv3.getOrigin();
                if (bl) {
                    i = lv4.propagationLevel == 0 ? 0 : MathHelper.hsvToRgb((float)lv4.propagationLevel / 50.0f, 0.9f, 0.9f);
                    for (int j = 0; j < DIRECTIONS.length; ++j) {
                        if (!lv4.hasDirection(j)) continue;
                        Direction lv6 = DIRECTIONS[j];
                        GizmoDrawing.line(Vec3d.add(lv5, 8.0, 8.0, 8.0), Vec3d.add(lv5, 8 - 16 * lv6.getOffsetX(), 8 - 16 * lv6.getOffsetY(), 8 - 16 * lv6.getOffsetZ()), ColorHelper.fullAlpha(i));
                    }
                }
                if (!bl2 || !lv3.getCurrentRenderData().hasData()) continue;
                i = 0;
                for (Direction lv7 : DIRECTIONS) {
                    for (Direction lv8 : DIRECTIONS) {
                        boolean bl3 = lv3.getCurrentRenderData().isVisibleThrough(lv7, lv8);
                        if (bl3) continue;
                        ++i;
                        GizmoDrawing.line(Vec3d.add(lv5, 8 + 8 * lv7.getOffsetX(), 8 + 8 * lv7.getOffsetY(), 8 + 8 * lv7.getOffsetZ()), Vec3d.add(lv5, 8 + 8 * lv8.getOffsetX(), 8 + 8 * lv8.getOffsetY(), 8 + 8 * lv8.getOffsetZ()), ColorHelper.getArgb(255, 255, 0, 0));
                    }
                }
                if (i <= 0) continue;
                float h = 0.5f;
                float k = 0.2f;
                GizmoDrawing.box(lv3.getBoundingBox().contract(0.5), DrawStyle.filled(ColorHelper.fromFloats(0.2f, 0.9f, 0.9f, 0.0f)));
            }
        }
        if ((lv9 = lv.getCapturedFrustum()) != null) {
            Vec3d lv10 = new Vec3d(lv9.getX(), lv9.getY(), lv9.getZ());
            Vector4f[] vector4fs = lv9.getBoundaryPoints();
            this.addFace(lv10, vector4fs, 0, 1, 2, 3, 0, 1, 1);
            this.addFace(lv10, vector4fs, 4, 5, 6, 7, 1, 0, 0);
            this.addFace(lv10, vector4fs, 0, 1, 5, 4, 1, 1, 0);
            this.addFace(lv10, vector4fs, 2, 3, 7, 6, 0, 0, 1);
            this.addFace(lv10, vector4fs, 0, 4, 7, 3, 0, 1, 0);
            this.addFace(lv10, vector4fs, 1, 5, 6, 2, 1, 0, 1);
            this.addFrustumEdge(lv10, vector4fs[0], vector4fs[1]);
            this.addFrustumEdge(lv10, vector4fs[1], vector4fs[2]);
            this.addFrustumEdge(lv10, vector4fs[2], vector4fs[3]);
            this.addFrustumEdge(lv10, vector4fs[3], vector4fs[0]);
            this.addFrustumEdge(lv10, vector4fs[4], vector4fs[5]);
            this.addFrustumEdge(lv10, vector4fs[5], vector4fs[6]);
            this.addFrustumEdge(lv10, vector4fs[6], vector4fs[7]);
            this.addFrustumEdge(lv10, vector4fs[7], vector4fs[4]);
            this.addFrustumEdge(lv10, vector4fs[0], vector4fs[4]);
            this.addFrustumEdge(lv10, vector4fs[1], vector4fs[5]);
            this.addFrustumEdge(lv10, vector4fs[2], vector4fs[6]);
            this.addFrustumEdge(lv10, vector4fs[3], vector4fs[7]);
        }
    }

    private void addFrustumEdge(Vec3d origin, Vector4f startOffset, Vector4f endOffset) {
        GizmoDrawing.line(new Vec3d(origin.x + (double)startOffset.x, origin.y + (double)startOffset.y, origin.z + (double)startOffset.z), new Vec3d(origin.x + (double)endOffset.x, origin.y + (double)endOffset.y, origin.z + (double)endOffset.z), -16777216);
    }

    private void addFace(Vec3d origin, Vector4f[] vertexOffsets, int i1, int i2, int i3, int i4, int red, int green, int blue) {
        float f = 0.25f;
        GizmoDrawing.quad(new Vec3d(vertexOffsets[i1].x(), vertexOffsets[i1].y(), vertexOffsets[i1].z()).add(origin), new Vec3d(vertexOffsets[i2].x(), vertexOffsets[i2].y(), vertexOffsets[i2].z()).add(origin), new Vec3d(vertexOffsets[i3].x(), vertexOffsets[i3].y(), vertexOffsets[i3].z()).add(origin), new Vec3d(vertexOffsets[i4].x(), vertexOffsets[i4].y(), vertexOffsets[i4].z()).add(origin), DrawStyle.filled(ColorHelper.fromFloats(0.25f, red, green, blue)));
    }
}

