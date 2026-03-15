/*
 * External method calls:
 *   Lnet/minecraft/client/render/DrawStyle;stroked(I)Lnet/minecraft/client/render/DrawStyle;
 *   Lnet/minecraft/world/debug/gizmo/GizmoDrawing;box(Lnet/minecraft/util/math/Box;Lnet/minecraft/client/render/DrawStyle;)Lnet/minecraft/world/debug/gizmo/VisibilityConfigurable;
 *   Lnet/minecraft/block/ShapeContext;absent()Lnet/minecraft/block/ShapeContext;
 *   Lnet/minecraft/util/shape/VoxelShape;offset(Lnet/minecraft/util/math/Vec3i;)Lnet/minecraft/util/shape/VoxelShape;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/client/render/debug/SupportingBlockDebugRenderer;renderBlockHighlights(Lnet/minecraft/entity/Entity;Ljava/util/function/DoubleSupplier;I)V
 *   Lnet/minecraft/client/render/debug/SupportingBlockDebugRenderer;renderBlockHighlight(Lnet/minecraft/util/math/BlockPos;DI)V
 */
package net.minecraft.client.render.debug;

import com.google.common.collect.ImmutableList;
import java.util.Collections;
import java.util.List;
import java.util.function.DoubleSupplier;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.ShapeContext;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.DrawStyle;
import net.minecraft.client.render.Frustum;
import net.minecraft.client.render.debug.DebugRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.debug.DebugDataStore;
import net.minecraft.world.debug.gizmo.GizmoDrawing;

@Environment(value=EnvType.CLIENT)
public class SupportingBlockDebugRenderer
implements DebugRenderer.Renderer {
    private final MinecraftClient client;
    private double lastEntityCheckTime = Double.MIN_VALUE;
    private List<Entity> entities = Collections.emptyList();

    public SupportingBlockDebugRenderer(MinecraftClient client) {
        this.client = client;
    }

    @Override
    public void render(double cameraX, double cameraY, double cameraZ, DebugDataStore store, Frustum frustum, float tickProgress) {
        ClientPlayerEntity lv2;
        double h = Util.getMeasuringTimeNano();
        if (h - this.lastEntityCheckTime > 1.0E8) {
            this.lastEntityCheckTime = h;
            Entity lv = this.client.gameRenderer.getCamera().getFocusedEntity();
            this.entities = ImmutableList.copyOf(lv.getEntityWorld().getOtherEntities(lv, lv.getBoundingBox().expand(16.0)));
        }
        if ((lv2 = this.client.player) != null && lv2.supportingBlockPos.isPresent()) {
            this.renderBlockHighlights(lv2, () -> 0.0, -65536);
        }
        for (Entity lv3 : this.entities) {
            if (lv3 == lv2) continue;
            this.renderBlockHighlights(lv3, () -> this.getAdditionalDilation(lv3), -16711936);
        }
    }

    private void renderBlockHighlights(Entity entity, DoubleSupplier dilationSupplier, int colr) {
        entity.supportingBlockPos.ifPresent(pos -> {
            double d = dilationSupplier.getAsDouble();
            BlockPos lv = entity.getSteppingPos();
            this.renderBlockHighlight(lv, 0.02 + d, colr);
            BlockPos lv2 = entity.getLandingPos();
            if (!lv2.equals(lv)) {
                this.renderBlockHighlight(lv2, 0.04 + d, -16711681);
            }
        });
    }

    private double getAdditionalDilation(Entity entity) {
        return 0.02 * (double)(String.valueOf((double)entity.getId() + 0.132453657).hashCode() % 1000) / 1000.0;
    }

    private void renderBlockHighlight(BlockPos pos, double d, int i) {
        double e = (double)pos.getX() - 2.0 * d;
        double f = (double)pos.getY() - 2.0 * d;
        double g = (double)pos.getZ() - 2.0 * d;
        double h = e + 1.0 + 4.0 * d;
        double j = f + 1.0 + 4.0 * d;
        double k = g + 1.0 + 4.0 * d;
        GizmoDrawing.box(new Box(e, f, g, h, j, k), DrawStyle.stroked(ColorHelper.withAlpha(0.4f, i)));
        VoxelShape lv = this.client.world.getBlockState(pos).getCollisionShape(this.client.world, pos, ShapeContext.absent()).offset(pos);
        DrawStyle lv2 = DrawStyle.stroked(i);
        for (Box lv3 : lv.getBoundingBoxes()) {
            GizmoDrawing.box(lv3, lv2);
        }
    }
}

