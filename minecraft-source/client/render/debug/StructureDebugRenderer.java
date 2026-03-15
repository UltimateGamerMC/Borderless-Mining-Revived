/*
 * External method calls:
 *   Lnet/minecraft/world/debug/DebugDataStore;forEachChunkData(Lnet/minecraft/world/debug/DebugSubscriptionType;Ljava/util/function/BiConsumer;)V
 *   Lnet/minecraft/world/debug/data/StructureDebugData;boundingBox()Lnet/minecraft/util/math/BlockBox;
 *   Lnet/minecraft/client/render/DrawStyle;stroked(I)Lnet/minecraft/client/render/DrawStyle;
 *   Lnet/minecraft/world/debug/gizmo/GizmoDrawing;box(Lnet/minecraft/util/math/Box;Lnet/minecraft/client/render/DrawStyle;)Lnet/minecraft/world/debug/gizmo/VisibilityConfigurable;
 *   Lnet/minecraft/world/debug/data/StructureDebugData;pieces()Ljava/util/List;
 *   Lnet/minecraft/world/debug/data/StructureDebugData$Piece;boundingBox()Lnet/minecraft/util/math/BlockBox;
 */
package net.minecraft.client.render.debug;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.DrawStyle;
import net.minecraft.client.render.Frustum;
import net.minecraft.client.render.debug.DebugRenderer;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.world.debug.DebugDataStore;
import net.minecraft.world.debug.DebugSubscriptionTypes;
import net.minecraft.world.debug.data.StructureDebugData;
import net.minecraft.world.debug.gizmo.GizmoDrawing;

@Environment(value=EnvType.CLIENT)
public class StructureDebugRenderer
implements DebugRenderer.Renderer {
    @Override
    public void render(double cameraX, double cameraY, double cameraZ, DebugDataStore store, Frustum frustum, float tickProgress) {
        store.forEachChunkData(DebugSubscriptionTypes.STRUCTURES, (chunkPos, structures) -> {
            for (StructureDebugData lv : structures) {
                GizmoDrawing.box(Box.from(lv.boundingBox()), DrawStyle.stroked(ColorHelper.fromFloats(1.0f, 1.0f, 1.0f, 1.0f)));
                for (StructureDebugData.Piece lv2 : lv.pieces()) {
                    if (lv2.isStart()) {
                        GizmoDrawing.box(Box.from(lv2.boundingBox()), DrawStyle.stroked(ColorHelper.fromFloats(1.0f, 0.0f, 1.0f, 0.0f)));
                        continue;
                    }
                    GizmoDrawing.box(Box.from(lv2.boundingBox()), DrawStyle.stroked(ColorHelper.fromFloats(1.0f, 0.0f, 0.0f, 1.0f)));
                }
            }
        });
    }
}

