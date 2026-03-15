/*
 * External method calls:
 *   Lnet/minecraft/util/shape/VoxelSet;forEachDirection(Lnet/minecraft/util/shape/VoxelSet$PositionConsumer;)V
 *   Lnet/minecraft/util/shape/VoxelSet;forEachEdge(Lnet/minecraft/util/shape/VoxelSet$PositionBiConsumer;Z)V
 *   Lnet/minecraft/client/render/DrawStyle;filled(I)Lnet/minecraft/client/render/DrawStyle;
 *   Lnet/minecraft/world/debug/gizmo/GizmoDrawing;face(Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/util/math/Direction;Lnet/minecraft/client/render/DrawStyle;)Lnet/minecraft/world/debug/gizmo/VisibilityConfigurable;
 *   Lnet/minecraft/world/debug/gizmo/GizmoDrawing;line(Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/util/math/Vec3d;I)Lnet/minecraft/world/debug/gizmo/VisibilityConfigurable;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/client/render/debug/LightDebugRenderer;drawEdges(Lnet/minecraft/util/shape/VoxelSet;Lnet/minecraft/util/math/ChunkSectionPos;I)V
 *   Lnet/minecraft/client/render/debug/LightDebugRenderer;drawFaces(Lnet/minecraft/util/shape/VoxelSet;Lnet/minecraft/util/math/ChunkSectionPos;I)V
 *   Lnet/minecraft/client/render/debug/LightDebugRenderer;drawEdge(IIIIIII)V
 *   Lnet/minecraft/client/render/debug/LightDebugRenderer;drawFace(Lnet/minecraft/util/math/Direction;IIII)V
 */
package net.minecraft.client.render.debug;

import java.time.Duration;
import java.time.Instant;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.DrawStyle;
import net.minecraft.client.render.Frustum;
import net.minecraft.client.render.debug.DebugRenderer;
import net.minecraft.util.math.ChunkSectionPos;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.BitSetVoxelSet;
import net.minecraft.util.shape.VoxelSet;
import net.minecraft.world.LightType;
import net.minecraft.world.chunk.light.LightStorage;
import net.minecraft.world.chunk.light.LightingProvider;
import net.minecraft.world.debug.DebugDataStore;
import net.minecraft.world.debug.gizmo.GizmoDrawing;
import org.jspecify.annotations.Nullable;

@Environment(value=EnvType.CLIENT)
public class LightDebugRenderer
implements DebugRenderer.Renderer {
    private static final Duration UPDATE_INTERVAL = Duration.ofMillis(500L);
    private static final int RADIUS = 10;
    private static final int READY_SHAPE_COLOR = ColorHelper.fromFloats(0.25f, 1.0f, 1.0f, 0.0f);
    private static final int DEFAULT_SHAPE_COLOR = ColorHelper.fromFloats(0.125f, 0.25f, 0.125f, 0.0f);
    private final MinecraftClient client;
    private final LightType lightType;
    private Instant lastUpdateTime = Instant.now();
    private @Nullable Data data;

    public LightDebugRenderer(MinecraftClient client, LightType lightType) {
        this.client = client;
        this.lightType = lightType;
    }

    @Override
    public void render(double cameraX, double cameraY, double cameraZ, DebugDataStore store, Frustum frustum, float tickProgress) {
        Instant instant = Instant.now();
        if (this.data == null || Duration.between(this.lastUpdateTime, instant).compareTo(UPDATE_INTERVAL) > 0) {
            this.lastUpdateTime = instant;
            this.data = new Data(this.client.world.getLightingProvider(), ChunkSectionPos.from(this.client.player.getBlockPos()), 10, this.lightType);
        }
        LightDebugRenderer.drawEdges(this.data.readyShape, this.data.minSectionPos, READY_SHAPE_COLOR);
        LightDebugRenderer.drawEdges(this.data.shape, this.data.minSectionPos, DEFAULT_SHAPE_COLOR);
        LightDebugRenderer.drawFaces(this.data.readyShape, this.data.minSectionPos, READY_SHAPE_COLOR);
        LightDebugRenderer.drawFaces(this.data.shape, this.data.minSectionPos, DEFAULT_SHAPE_COLOR);
    }

    private static void drawFaces(VoxelSet arg, ChunkSectionPos arg22, int i) {
        arg.forEachDirection((arg2, j, k, l) -> {
            int m = j + arg22.getX();
            int n = k + arg22.getY();
            int o = l + arg22.getZ();
            LightDebugRenderer.drawFace(arg2, m, n, o, i);
        });
    }

    private static void drawEdges(VoxelSet arg, ChunkSectionPos arg2, int i) {
        arg.forEachEdge((j, k, l, m, n, o) -> {
            int p = j + arg2.getX();
            int q = k + arg2.getY();
            int r = l + arg2.getZ();
            int s = m + arg2.getX();
            int t = n + arg2.getY();
            int u = o + arg2.getZ();
            LightDebugRenderer.drawEdge(p, q, r, s, t, u, i);
        }, true);
    }

    private static void drawFace(Direction arg, int i, int j, int k, int l) {
        Vec3d lv = new Vec3d(ChunkSectionPos.getBlockCoord(i), ChunkSectionPos.getBlockCoord(j), ChunkSectionPos.getBlockCoord(k));
        Vec3d lv2 = lv.add(16.0, 16.0, 16.0);
        GizmoDrawing.face(lv, lv2, arg, DrawStyle.filled(l));
    }

    private static void drawEdge(int i, int j, int k, int l, int m, int n, int o) {
        double d = ChunkSectionPos.getBlockCoord(i);
        double e = ChunkSectionPos.getBlockCoord(j);
        double f = ChunkSectionPos.getBlockCoord(k);
        double g = ChunkSectionPos.getBlockCoord(l);
        double h = ChunkSectionPos.getBlockCoord(m);
        double p = ChunkSectionPos.getBlockCoord(n);
        int q = ColorHelper.fullAlpha(o);
        GizmoDrawing.line(new Vec3d(d, e, f), new Vec3d(g, h, p), q);
    }

    @Environment(value=EnvType.CLIENT)
    static final class Data {
        final VoxelSet readyShape;
        final VoxelSet shape;
        final ChunkSectionPos minSectionPos;

        Data(LightingProvider lightingProvider, ChunkSectionPos sectionPos, int radius, LightType lightType) {
            int j = radius * 2 + 1;
            this.readyShape = new BitSetVoxelSet(j, j, j);
            this.shape = new BitSetVoxelSet(j, j, j);
            for (int k = 0; k < j; ++k) {
                for (int l = 0; l < j; ++l) {
                    for (int m = 0; m < j; ++m) {
                        ChunkSectionPos lv = ChunkSectionPos.from(sectionPos.getSectionX() + m - radius, sectionPos.getSectionY() + l - radius, sectionPos.getSectionZ() + k - radius);
                        LightStorage.Status lv2 = lightingProvider.getStatus(lightType, lv);
                        if (lv2 == LightStorage.Status.LIGHT_AND_DATA) {
                            this.readyShape.set(m, l, k);
                            this.shape.set(m, l, k);
                            continue;
                        }
                        if (lv2 != LightStorage.Status.LIGHT_ONLY) continue;
                        this.shape.set(m, l, k);
                    }
                }
            }
            this.minSectionPos = ChunkSectionPos.from(sectionPos.getSectionX() - radius, sectionPos.getSectionY() - radius, sectionPos.getSectionZ() - radius);
        }
    }
}

