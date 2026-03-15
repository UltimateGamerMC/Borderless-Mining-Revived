/*
 * External method calls:
 *   Lnet/minecraft/world/debug/DebugDataStore;forEachEntityData(Lnet/minecraft/world/debug/DebugSubscriptionType;Ljava/util/function/BiConsumer;)V
 *   Lnet/minecraft/client/render/DrawStyle;filled(I)Lnet/minecraft/client/render/DrawStyle;
 *   Lnet/minecraft/world/debug/gizmo/GizmoDrawing;box(Lnet/minecraft/util/math/Box;Lnet/minecraft/client/render/DrawStyle;)Lnet/minecraft/world/debug/gizmo/VisibilityConfigurable;
 *   Lnet/minecraft/entity/ai/pathing/Path$DebugNodeInfo;closedSet()[Lnet/minecraft/entity/ai/pathing/PathNode;
 *   Lnet/minecraft/entity/ai/pathing/Path$DebugNodeInfo;openSet()[Lnet/minecraft/entity/ai/pathing/PathNode;
 *   Lnet/minecraft/world/debug/gizmo/TextGizmo$Style;left()Lnet/minecraft/world/debug/gizmo/TextGizmo$Style;
 *   Lnet/minecraft/world/debug/gizmo/TextGizmo$Style;scaled(F)Lnet/minecraft/world/debug/gizmo/TextGizmo$Style;
 *   Lnet/minecraft/world/debug/gizmo/GizmoDrawing;text(Ljava/lang/String;Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/world/debug/gizmo/TextGizmo$Style;)Lnet/minecraft/world/debug/gizmo/VisibilityConfigurable;
 *   Lnet/minecraft/world/debug/gizmo/VisibilityConfigurable;ignoreOcclusion()Lnet/minecraft/world/debug/gizmo/VisibilityConfigurable;
 *   Lnet/minecraft/world/debug/gizmo/GizmoDrawing;arrow(Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/util/math/Vec3d;I)Lnet/minecraft/world/debug/gizmo/VisibilityConfigurable;
 *   Lnet/minecraft/world/debug/data/EntityPathDebugData;path()Lnet/minecraft/entity/ai/pathing/Path;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/client/render/debug/PathfindingDebugRenderer;drawPath(Lnet/minecraft/entity/ai/pathing/Path;FZZDDD)V
 *   Lnet/minecraft/client/render/debug/PathfindingDebugRenderer;drawPathLines(Lnet/minecraft/entity/ai/pathing/Path;DDD)V
 *   Lnet/minecraft/client/render/debug/PathfindingDebugRenderer;render(DDDLnet/minecraft/entity/ai/pathing/Path;F)V
 */
package net.minecraft.client.render.debug;

import java.util.Locale;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.DrawStyle;
import net.minecraft.client.render.Frustum;
import net.minecraft.client.render.debug.DebugRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.pathing.Path;
import net.minecraft.entity.ai.pathing.PathNode;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.debug.DebugDataStore;
import net.minecraft.world.debug.DebugSubscriptionTypes;
import net.minecraft.world.debug.data.EntityPathDebugData;
import net.minecraft.world.debug.gizmo.GizmoDrawing;
import net.minecraft.world.debug.gizmo.TextGizmo;

@Environment(value=EnvType.CLIENT)
public class PathfindingDebugRenderer
implements DebugRenderer.Renderer {
    private static final float RANGE = 80.0f;
    private static final int field_62974 = 8;
    private static final boolean field_62975 = false;
    private static final boolean field_32908 = true;
    private static final boolean field_32909 = false;
    private static final boolean field_32910 = false;
    private static final boolean field_32911 = true;
    private static final boolean field_32912 = true;
    private static final float DRAWN_STRING_SIZE = 0.32f;

    @Override
    public void render(double cameraX, double cameraY, double cameraZ, DebugDataStore store, Frustum frustum, float tickProgress) {
        store.forEachEntityData(DebugSubscriptionTypes.ENTITY_PATHS, (entity, debugData) -> PathfindingDebugRenderer.render(cameraX, cameraY, cameraZ, debugData.path(), debugData.maxNodeDistance()));
    }

    private static void render(double cameraX, double cameraY, double cameraZ, Path path, float maxNodeDistance) {
        PathfindingDebugRenderer.drawPath(path, maxNodeDistance, true, true, cameraX, cameraY, cameraZ);
    }

    public static void drawPath(Path path, float maxNodeDistance, boolean bl, boolean bl2, double cameraX, double cameraY, double cameraZ) {
        PathfindingDebugRenderer.drawPathLines(path, cameraX, cameraY, cameraZ);
        BlockPos lv = path.getTarget();
        if (PathfindingDebugRenderer.getManhattanDistance(lv, cameraX, cameraY, cameraZ) <= 80.0f) {
            GizmoDrawing.box(new Box((float)lv.getX() + 0.25f, (float)lv.getY() + 0.25f, (double)lv.getZ() + 0.25, (float)lv.getX() + 0.75f, (float)lv.getY() + 0.75f, (float)lv.getZ() + 0.75f), DrawStyle.filled(ColorHelper.fromFloats(0.5f, 0.0f, 1.0f, 0.0f)));
            for (int i = 0; i < path.getLength(); ++i) {
                PathNode lv2 = path.getNode(i);
                if (!(PathfindingDebugRenderer.getManhattanDistance(lv2.getBlockPos(), cameraX, cameraY, cameraZ) <= 80.0f)) continue;
                float h = i == path.getCurrentNodeIndex() ? 1.0f : 0.0f;
                float j = i == path.getCurrentNodeIndex() ? 0.0f : 1.0f;
                Box lv3 = new Box((float)lv2.x + 0.5f - maxNodeDistance, (float)lv2.y + 0.01f * (float)i, (float)lv2.z + 0.5f - maxNodeDistance, (float)lv2.x + 0.5f + maxNodeDistance, (float)lv2.y + 0.25f + 0.01f * (float)i, (float)lv2.z + 0.5f + maxNodeDistance);
                GizmoDrawing.box(lv3, DrawStyle.filled(ColorHelper.fromFloats(0.5f, h, 0.0f, j)));
            }
        }
        Path.DebugNodeInfo lv4 = path.getDebugNodeInfos();
        if (bl && lv4 != null) {
            for (PathNode lv5 : lv4.closedSet()) {
                if (!(PathfindingDebugRenderer.getManhattanDistance(lv5.getBlockPos(), cameraX, cameraY, cameraZ) <= 80.0f)) continue;
                GizmoDrawing.box(new Box((float)lv5.x + 0.5f - maxNodeDistance / 2.0f, (float)lv5.y + 0.01f, (float)lv5.z + 0.5f - maxNodeDistance / 2.0f, (float)lv5.x + 0.5f + maxNodeDistance / 2.0f, (double)lv5.y + 0.1, (float)lv5.z + 0.5f + maxNodeDistance / 2.0f), DrawStyle.filled(ColorHelper.fromFloats(0.5f, 1.0f, 0.8f, 0.8f)));
            }
            for (PathNode lv5 : lv4.openSet()) {
                if (!(PathfindingDebugRenderer.getManhattanDistance(lv5.getBlockPos(), cameraX, cameraY, cameraZ) <= 80.0f)) continue;
                GizmoDrawing.box(new Box((float)lv5.x + 0.5f - maxNodeDistance / 2.0f, (float)lv5.y + 0.01f, (float)lv5.z + 0.5f - maxNodeDistance / 2.0f, (float)lv5.x + 0.5f + maxNodeDistance / 2.0f, (double)lv5.y + 0.1, (float)lv5.z + 0.5f + maxNodeDistance / 2.0f), DrawStyle.filled(ColorHelper.fromFloats(0.5f, 0.8f, 1.0f, 1.0f)));
            }
        }
        if (bl2) {
            for (int k = 0; k < path.getLength(); ++k) {
                PathNode lv6 = path.getNode(k);
                if (!(PathfindingDebugRenderer.getManhattanDistance(lv6.getBlockPos(), cameraX, cameraY, cameraZ) <= 80.0f)) continue;
                GizmoDrawing.text(String.valueOf((Object)lv6.type), new Vec3d((double)lv6.x + 0.5, (double)lv6.y + 0.75, (double)lv6.z + 0.5), TextGizmo.Style.left().scaled(0.32f)).ignoreOcclusion();
                GizmoDrawing.text(String.format(Locale.ROOT, "%.2f", Float.valueOf(lv6.penalty)), new Vec3d((double)lv6.x + 0.5, (double)lv6.y + 0.25, (double)lv6.z + 0.5), TextGizmo.Style.left().scaled(0.32f)).ignoreOcclusion();
            }
        }
    }

    public static void drawPathLines(Path path, double cameraX, double cameraY, double cameraZ) {
        if (path.getLength() < 2) {
            return;
        }
        Vec3d lv = path.getNode(0).getPos();
        for (int i = 1; i < path.getLength(); ++i) {
            PathNode lv2 = path.getNode(i);
            if (PathfindingDebugRenderer.getManhattanDistance(lv2.getBlockPos(), cameraX, cameraY, cameraZ) > 80.0f) {
                lv = lv2.getPos();
                continue;
            }
            float g = (float)i / (float)path.getLength() * 0.33f;
            int j = ColorHelper.fullAlpha(MathHelper.hsvToRgb(g, 0.9f, 0.9f));
            GizmoDrawing.arrow(lv.add(0.5, 0.5, 0.5), lv2.getPos().add(0.5, 0.5, 0.5), j);
            lv = lv2.getPos();
        }
    }

    private static float getManhattanDistance(BlockPos pos, double x, double y, double z) {
        return (float)(Math.abs((double)pos.getX() - x) + Math.abs((double)pos.getY() - y) + Math.abs((double)pos.getZ() - z));
    }

    private static /* synthetic */ void method_75445(DebugDataStore arg, double d, double e, double f, Entity arg2) {
        EntityPathDebugData lv = arg.getEntityData(DebugSubscriptionTypes.ENTITY_PATHS, arg2);
        if (lv != null) {
            PathfindingDebugRenderer.render(d, e, f, lv.path(), lv.maxNodeDistance());
        }
    }
}

