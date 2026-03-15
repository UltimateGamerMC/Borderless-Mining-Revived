/*
 * External method calls:
 *   Lnet/minecraft/client/render/DrawStyle;filled(I)Lnet/minecraft/client/render/DrawStyle;
 *   Lnet/minecraft/world/debug/gizmo/GizmoDrawing;face(Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/util/math/Direction;Lnet/minecraft/client/render/DrawStyle;)Lnet/minecraft/world/debug/gizmo/VisibilityConfigurable;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/client/render/debug/BlockOutlineDebugRenderer;drawFace(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;Lnet/minecraft/world/BlockView;Lnet/minecraft/util/math/Direction;Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/util/math/Vec3d;I)V
 */
package net.minecraft.client.render.debug;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.DrawStyle;
import net.minecraft.client.render.Frustum;
import net.minecraft.client.render.debug.DebugRenderer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.debug.DebugDataStore;
import net.minecraft.world.debug.gizmo.GizmoDrawing;

@Environment(value=EnvType.CLIENT)
public class BlockOutlineDebugRenderer
implements DebugRenderer.Renderer {
    private final MinecraftClient client;

    public BlockOutlineDebugRenderer(MinecraftClient client) {
        this.client = client;
    }

    @Override
    public void render(double cameraX, double cameraY, double cameraZ, DebugDataStore store, Frustum frustum, float tickProgress) {
        World lv = this.client.player.getEntityWorld();
        BlockPos lv2 = BlockPos.ofFloored(cameraX, cameraY, cameraZ);
        for (BlockPos lv3 : BlockPos.iterate(lv2.add(-6, -6, -6), lv2.add(6, 6, 6))) {
            BlockState lv4 = lv.getBlockState(lv3);
            if (lv4.isOf(Blocks.AIR)) continue;
            VoxelShape lv5 = lv4.getOutlineShape(lv, lv3);
            for (Box lv6 : lv5.getBoundingBoxes()) {
                Box lv7 = lv6.offset(lv3).expand(0.002);
                int i = -2130771968;
                Vec3d lv8 = lv7.getMinPos();
                Vec3d lv9 = lv7.getMaxPos();
                BlockOutlineDebugRenderer.drawFace(lv3, lv4, lv, Direction.WEST, lv8, lv9, -2130771968);
                BlockOutlineDebugRenderer.drawFace(lv3, lv4, lv, Direction.SOUTH, lv8, lv9, -2130771968);
                BlockOutlineDebugRenderer.drawFace(lv3, lv4, lv, Direction.EAST, lv8, lv9, -2130771968);
                BlockOutlineDebugRenderer.drawFace(lv3, lv4, lv, Direction.NORTH, lv8, lv9, -2130771968);
                BlockOutlineDebugRenderer.drawFace(lv3, lv4, lv, Direction.DOWN, lv8, lv9, -2130771968);
                BlockOutlineDebugRenderer.drawFace(lv3, lv4, lv, Direction.UP, lv8, lv9, -2130771968);
            }
        }
    }

    private static void drawFace(BlockPos pos, BlockState state, BlockView world, Direction direction, Vec3d minPos, Vec3d maxPos, int color) {
        if (state.isSideSolidFullSquare(world, pos, direction)) {
            GizmoDrawing.face(minPos, maxPos, direction, DrawStyle.filled(color));
        }
    }
}

