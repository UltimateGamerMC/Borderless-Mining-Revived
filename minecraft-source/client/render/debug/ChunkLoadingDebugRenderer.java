/*
 * External method calls:
 *   Lnet/minecraft/world/debug/gizmo/TextGizmo$Style;left()Lnet/minecraft/world/debug/gizmo/TextGizmo$Style;
 *   Lnet/minecraft/world/debug/gizmo/TextGizmo$Style;scaled(F)Lnet/minecraft/world/debug/gizmo/TextGizmo$Style;
 *   Lnet/minecraft/world/debug/gizmo/GizmoDrawing;text(Ljava/lang/String;Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/world/debug/gizmo/TextGizmo$Style;)Lnet/minecraft/world/debug/gizmo/VisibilityConfigurable;
 *   Lnet/minecraft/world/debug/gizmo/VisibilityConfigurable;ignoreOcclusion()Lnet/minecraft/world/debug/gizmo/VisibilityConfigurable;
 */
package net.minecraft.client.render.debug;

import com.google.common.collect.ImmutableMap;
import java.lang.invoke.CallSite;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Frustum;
import net.minecraft.client.render.debug.DebugRenderer;
import net.minecraft.client.world.ClientChunkManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.integrated.IntegratedServer;
import net.minecraft.server.world.ServerChunkManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Util;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.ChunkSectionPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.chunk.WorldChunk;
import net.minecraft.world.debug.DebugDataStore;
import net.minecraft.world.debug.gizmo.GizmoDrawing;
import net.minecraft.world.debug.gizmo.TextGizmo;
import org.jspecify.annotations.Nullable;

@Environment(value=EnvType.CLIENT)
public class ChunkLoadingDebugRenderer
implements DebugRenderer.Renderer {
    final MinecraftClient client;
    private double lastUpdateTime = Double.MIN_VALUE;
    private final int LOADING_DATA_CHUNK_RANGE = 12;
    private @Nullable ChunkLoadingStatus loadingData;

    public ChunkLoadingDebugRenderer(MinecraftClient client) {
        this.client = client;
    }

    @Override
    public void render(double cameraX, double cameraY, double cameraZ, DebugDataStore store, Frustum frustum, float tickProgress) {
        double h = Util.getMeasuringTimeNano();
        if (h - this.lastUpdateTime > 3.0E9) {
            this.lastUpdateTime = h;
            IntegratedServer lv = this.client.getServer();
            this.loadingData = lv != null ? new ChunkLoadingStatus(this, lv, cameraX, cameraZ) : null;
        }
        if (this.loadingData != null) {
            Map map = this.loadingData.serverStates.getNow(null);
            double i = this.client.gameRenderer.getCamera().getCameraPos().y * 0.85;
            for (Map.Entry<ChunkPos, String> entry : this.loadingData.clientStates.entrySet()) {
                ChunkPos lv2 = entry.getKey();
                Object string = entry.getValue();
                if (map != null) {
                    string = (String)string + (String)map.get(lv2);
                }
                String[] strings = ((String)string).split("\n");
                int j = 0;
                for (String string2 : strings) {
                    GizmoDrawing.text(string2, new Vec3d(ChunkSectionPos.getOffsetPos(lv2.x, 8), i + (double)j, ChunkSectionPos.getOffsetPos(lv2.z, 8)), TextGizmo.Style.left().scaled(2.4f)).ignoreOcclusion();
                    j -= 2;
                }
            }
        }
    }

    @Environment(value=EnvType.CLIENT)
    final class ChunkLoadingStatus {
        final Map<ChunkPos, String> clientStates;
        final CompletableFuture<Map<ChunkPos, String>> serverStates;

        ChunkLoadingStatus(ChunkLoadingDebugRenderer arg, IntegratedServer server, double x, double z) {
            ClientWorld lv = arg.client.world;
            RegistryKey<World> lv2 = lv.getRegistryKey();
            int i = ChunkSectionPos.getSectionCoord(x);
            int j = ChunkSectionPos.getSectionCoord(z);
            ImmutableMap.Builder<ChunkPos, Object> builder = ImmutableMap.builder();
            ClientChunkManager lv3 = lv.getChunkManager();
            for (int k = i - 12; k <= i + 12; ++k) {
                for (int l = j - 12; l <= j + 12; ++l) {
                    ChunkPos lv4 = new ChunkPos(k, l);
                    Object string = "";
                    WorldChunk lv5 = lv3.getWorldChunk(k, l, false);
                    string = (String)string + "Client: ";
                    if (lv5 == null) {
                        string = (String)string + "0n/a\n";
                    } else {
                        string = (String)string + (lv5.isEmpty() ? " E" : "");
                        string = (String)string + "\n";
                    }
                    builder.put(lv4, string);
                }
            }
            this.clientStates = builder.build();
            this.serverStates = server.submit(() -> {
                ServerWorld lv = server.getWorld(lv2);
                if (lv == null) {
                    return ImmutableMap.of();
                }
                ImmutableMap.Builder<ChunkPos, CallSite> builder = ImmutableMap.builder();
                ServerChunkManager lv2 = lv.getChunkManager();
                for (int k = i - 12; k <= i + 12; ++k) {
                    for (int l = j - 12; l <= j + 12; ++l) {
                        ChunkPos lv3 = new ChunkPos(k, l);
                        builder.put(lv3, (CallSite)((Object)("Server: " + lv2.getChunkLoadingDebugInfo(lv3))));
                    }
                }
                return builder.build();
            });
        }
    }
}

