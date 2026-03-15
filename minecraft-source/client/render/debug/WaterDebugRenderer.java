/*
 * External method calls:
 *   Lnet/minecraft/client/render/DrawStyle;filled(I)Lnet/minecraft/client/render/DrawStyle;
 *   Lnet/minecraft/world/debug/gizmo/GizmoDrawing;box(Lnet/minecraft/util/math/Box;Lnet/minecraft/client/render/DrawStyle;)Lnet/minecraft/world/debug/gizmo/VisibilityConfigurable;
 *   Lnet/minecraft/world/debug/gizmo/TextGizmo$Style;left(I)Lnet/minecraft/world/debug/gizmo/TextGizmo$Style;
 *   Lnet/minecraft/world/debug/gizmo/GizmoDrawing;text(Ljava/lang/String;Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/world/debug/gizmo/TextGizmo$Style;)Lnet/minecraft/world/debug/gizmo/VisibilityConfigurable;
 */
package net.minecraft.client.render.debug;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.DrawStyle;
import net.minecraft.client.render.Frustum;
import net.minecraft.client.render.debug.DebugRenderer;
import net.minecraft.fluid.FluidState;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.debug.DebugDataStore;
import net.minecraft.world.debug.gizmo.GizmoDrawing;
import net.minecraft.world.debug.gizmo.TextGizmo;

@Environment(value=EnvType.CLIENT)
public class WaterDebugRenderer
implements DebugRenderer.Renderer {
    private final MinecraftClient client;

    public WaterDebugRenderer(MinecraftClient client) {
        this.client = client;
    }

    @Override
    public void render(double cameraX, double cameraY, double cameraZ, DebugDataStore store, Frustum frustum, float tickProgress) {
        FluidState lv4;
        BlockPos lv = this.client.player.getBlockPos();
        World lv2 = this.client.player.getEntityWorld();
        for (BlockPos lv3 : BlockPos.iterate(lv.add(-10, -10, -10), lv.add(10, 10, 10))) {
            lv4 = lv2.getFluidState(lv3);
            if (!lv4.isIn(FluidTags.WATER)) continue;
            double h = (float)lv3.getY() + lv4.getHeight(lv2, lv3);
            GizmoDrawing.box(new Box((float)lv3.getX() + 0.01f, (float)lv3.getY() + 0.01f, (float)lv3.getZ() + 0.01f, (float)lv3.getX() + 0.99f, h, (float)lv3.getZ() + 0.99f), DrawStyle.filled(ColorHelper.fromFloats(0.15f, 0.0f, 1.0f, 0.0f)));
        }
        for (BlockPos lv3 : BlockPos.iterate(lv.add(-10, -10, -10), lv.add(10, 10, 10))) {
            lv4 = lv2.getFluidState(lv3);
            if (!lv4.isIn(FluidTags.WATER)) continue;
            GizmoDrawing.text(String.valueOf(lv4.getLevel()), Vec3d.add(lv3, 0.5, lv4.getHeight(lv2, lv3), 0.5), TextGizmo.Style.left(-16777216));
        }
    }
}

