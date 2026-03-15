/*
 * External method calls:
 *   Lnet/minecraft/client/render/DrawStyle;filled(I)Lnet/minecraft/client/render/DrawStyle;
 *   Lnet/minecraft/world/debug/gizmo/GizmoDrawing;box(Lnet/minecraft/util/math/Box;Lnet/minecraft/client/render/DrawStyle;)Lnet/minecraft/world/debug/gizmo/VisibilityConfigurable;
 */
package net.minecraft.client.render.debug;

import java.util.Map;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.DrawStyle;
import net.minecraft.client.render.Frustum;
import net.minecraft.client.render.debug.DebugRenderer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.ChunkSectionPos;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.world.Heightmap;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.debug.DebugDataStore;
import net.minecraft.world.debug.gizmo.GizmoDrawing;
import org.joml.Vector3f;

@Environment(value=EnvType.CLIENT)
public class HeightmapDebugRenderer
implements DebugRenderer.Renderer {
    private final MinecraftClient client;
    private static final int CHUNK_RANGE = 2;
    private static final float BOX_HEIGHT = 0.09375f;

    public HeightmapDebugRenderer(MinecraftClient client) {
        this.client = client;
    }

    @Override
    public void render(double cameraX, double cameraY, double cameraZ, DebugDataStore store, Frustum frustum, float tickProgress) {
        ClientWorld lv = this.client.world;
        BlockPos lv2 = BlockPos.ofFloored(cameraX, 0.0, cameraZ);
        for (int i = -2; i <= 2; ++i) {
            for (int j = -2; j <= 2; ++j) {
                Chunk lv3 = lv.getChunk(lv2.add(i * 16, 0, j * 16));
                for (Map.Entry<Heightmap.Type, Heightmap> entry : lv3.getHeightmaps()) {
                    Heightmap.Type lv4 = entry.getKey();
                    ChunkPos lv5 = lv3.getPos();
                    Vector3f vector3f = this.getColorForHeightmapType(lv4);
                    for (int k = 0; k < 16; ++k) {
                        for (int l = 0; l < 16; ++l) {
                            int m = ChunkSectionPos.getOffsetPos(lv5.x, k);
                            int n = ChunkSectionPos.getOffsetPos(lv5.z, l);
                            float h = (float)lv.getTopY(lv4, m, n) + (float)lv4.ordinal() * 0.09375f;
                            GizmoDrawing.box(new Box((float)m + 0.25f, h, (float)n + 0.25f, (float)m + 0.75f, h + 0.09375f, (float)n + 0.75f), DrawStyle.filled(ColorHelper.fromFloats(1.0f, vector3f.x(), vector3f.y(), vector3f.z())));
                        }
                    }
                }
            }
        }
    }

    private Vector3f getColorForHeightmapType(Heightmap.Type type) {
        return switch (type) {
            default -> throw new MatchException(null, null);
            case Heightmap.Type.WORLD_SURFACE_WG -> new Vector3f(1.0f, 1.0f, 0.0f);
            case Heightmap.Type.OCEAN_FLOOR_WG -> new Vector3f(1.0f, 0.0f, 1.0f);
            case Heightmap.Type.WORLD_SURFACE -> new Vector3f(0.0f, 0.7f, 0.0f);
            case Heightmap.Type.OCEAN_FLOOR -> new Vector3f(0.0f, 0.0f, 0.5f);
            case Heightmap.Type.MOTION_BLOCKING -> new Vector3f(0.0f, 0.3f, 0.3f);
            case Heightmap.Type.MOTION_BLOCKING_NO_LEAVES -> new Vector3f(0.0f, 0.5f, 0.5f);
        };
    }
}

