/*
 * External method calls:
 *   Lnet/minecraft/util/shape/VoxelShape;forEachEdge(Lnet/minecraft/util/shape/VoxelShapes$BoxConsumer;)V
 *   Lnet/minecraft/client/render/VertexConsumer;vertex(Lnet/minecraft/client/util/math/MatrixStack$Entry;FFF)Lnet/minecraft/client/render/VertexConsumer;
 *   Lnet/minecraft/client/render/VertexConsumer;color(I)Lnet/minecraft/client/render/VertexConsumer;
 *   Lnet/minecraft/client/render/VertexConsumer;normal(Lnet/minecraft/client/util/math/MatrixStack$Entry;Lorg/joml/Vector3f;)Lnet/minecraft/client/render/VertexConsumer;
 *   Lnet/minecraft/client/render/VertexConsumer;lineWidth(F)Lnet/minecraft/client/render/VertexConsumer;
 */
package net.minecraft.client.render;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.shape.VoxelShape;
import org.joml.Vector3f;

@Environment(value=EnvType.CLIENT)
public class VertexRendering {
    public static void drawOutline(MatrixStack matrices, VertexConsumer vertexConsumers, VoxelShape shape, double offsetX, double offsetY, double offsetZ, int color, float lineWidth) {
        MatrixStack.Entry lv = matrices.peek();
        shape.forEachEdge((minX, minY, minZ, maxX, maxY, maxZ) -> {
            Vector3f vector3f = new Vector3f((float)(maxX - minX), (float)(maxY - minY), (float)(maxZ - minZ)).normalize();
            vertexConsumers.vertex(lv, (float)(minX + offsetX), (float)(minY + offsetY), (float)(minZ + offsetZ)).color(color).normal(lv, vector3f).lineWidth(lineWidth);
            vertexConsumers.vertex(lv, (float)(maxX + offsetX), (float)(maxY + offsetY), (float)(maxZ + offsetZ)).color(color).normal(lv, vector3f).lineWidth(lineWidth);
        });
    }
}

