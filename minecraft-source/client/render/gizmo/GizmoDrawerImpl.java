/*
 * External method calls:
 *   Lnet/minecraft/client/render/gizmo/GizmoDrawerImpl$Division;draw(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;Lnet/minecraft/client/render/state/CameraRenderState;Lorg/joml/Matrix4f;)V
 */
package net.minecraft.client.render.gizmo;

import java.util.ArrayList;
import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.state.CameraRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.debug.gizmo.GizmoDrawer;
import net.minecraft.world.debug.gizmo.TextGizmo;
import org.joml.Matrix4f;
import org.joml.Vector4f;

@Environment(value=EnvType.CLIENT)
public class GizmoDrawerImpl
implements GizmoDrawer {
    private final Division opaque = new Division(true);
    private final Division transparent = new Division(false);
    private boolean empty = true;

    private Division getDivision(int color) {
        if (ColorHelper.getAlpha(color) < 255) {
            return this.transparent;
        }
        return this.opaque;
    }

    @Override
    public void addPoint(Vec3d pos, int color, float size) {
        this.getDivision((int)color).points.add(new Point(pos, color, size));
        this.empty = false;
    }

    @Override
    public void addLine(Vec3d start, Vec3d end, int color, float width) {
        this.getDivision((int)color).lines.add(new Line(start, end, color, width));
        this.empty = false;
    }

    @Override
    public void addPolygon(Vec3d[] vertices, int color) {
        this.getDivision((int)color).triangleFans.add(new Polygon(vertices, color));
        this.empty = false;
    }

    @Override
    public void addQuad(Vec3d a, Vec3d b, Vec3d c, Vec3d d, int color) {
        this.getDivision((int)color).quads.add(new Quad(a, b, c, d, color));
        this.empty = false;
    }

    @Override
    public void addText(Vec3d pos, String text, TextGizmo.Style style) {
        this.getDivision((int)style.color()).texts.add(new Text(pos, text, style));
        this.empty = false;
    }

    public void draw(MatrixStack matrices, VertexConsumerProvider vertexConsumers, CameraRenderState cameraRenderState, Matrix4f posMatrix) {
        this.opaque.draw(matrices, vertexConsumers, cameraRenderState, posMatrix);
        this.transparent.draw(matrices, vertexConsumers, cameraRenderState, posMatrix);
    }

    public boolean isEmpty() {
        return this.empty;
    }

    @Environment(value=EnvType.CLIENT)
    record Division(boolean opaque, List<Line> lines, List<Quad> quads, List<Polygon> triangleFans, List<Text> texts, List<Point> points) {
        Division(boolean opaque) {
            this(opaque, new ArrayList<Line>(), new ArrayList<Quad>(), new ArrayList<Polygon>(), new ArrayList<Text>(), new ArrayList<Point>());
        }

        public void draw(MatrixStack matrices, VertexConsumerProvider vertexConsumers, CameraRenderState cameraRenderState, Matrix4f posMatrix) {
            this.drawQuads(matrices, vertexConsumers, cameraRenderState);
            this.drawTriangleFans(matrices, vertexConsumers, cameraRenderState);
            this.drawLines(matrices, vertexConsumers, cameraRenderState, posMatrix);
            this.drawText(matrices, vertexConsumers, cameraRenderState);
            this.drawPoints(matrices, vertexConsumers, cameraRenderState);
        }

        private void drawText(MatrixStack matrices, VertexConsumerProvider vertexConsumers, CameraRenderState cameraRenderState) {
            MinecraftClient lv = MinecraftClient.getInstance();
            TextRenderer lv2 = lv.textRenderer;
            if (!cameraRenderState.initialized) {
                return;
            }
            double d = cameraRenderState.pos.getX();
            double e = cameraRenderState.pos.getY();
            double f = cameraRenderState.pos.getZ();
            for (Text lv3 : this.texts) {
                matrices.push();
                matrices.translate((float)(lv3.pos().getX() - d), (float)(lv3.pos().getY() - e), (float)(lv3.pos().getZ() - f));
                matrices.multiply(cameraRenderState.orientation);
                matrices.scale(lv3.style.scale() / 16.0f, -lv3.style.scale() / 16.0f, lv3.style.scale() / 16.0f);
                float g = lv3.style.adjustLeft().isEmpty() ? (float)(-lv2.getWidth(lv3.text)) / 2.0f : (float)(-lv3.style.adjustLeft().getAsDouble()) / lv3.style.scale();
                lv2.draw(lv3.text, g, 0.0f, lv3.style.color(), false, matrices.peek().getPositionMatrix(), vertexConsumers, TextRenderer.TextLayerType.NORMAL, 0, LightmapTextureManager.MAX_LIGHT_COORDINATE);
                matrices.pop();
            }
        }

        private void drawLines(MatrixStack matrices, VertexConsumerProvider vertexConsumers, CameraRenderState cameraRenderState, Matrix4f posMatrix) {
            VertexConsumer lv = vertexConsumers.getBuffer(this.opaque ? RenderLayers.lines() : RenderLayers.linesTranslucent());
            MatrixStack.Entry lv2 = matrices.peek();
            Vector4f vector4f = new Vector4f();
            Vector4f vector4f2 = new Vector4f();
            Vector4f vector4f3 = new Vector4f();
            Vector4f vector4f4 = new Vector4f();
            Vector4f vector4f5 = new Vector4f();
            double d = cameraRenderState.pos.getX();
            double e = cameraRenderState.pos.getY();
            double f = cameraRenderState.pos.getZ();
            for (Line lv3 : this.lines) {
                boolean bl2;
                vector4f.set(lv3.start().getX() - d, lv3.start().getY() - e, lv3.start().getZ() - f, 1.0);
                vector4f2.set(lv3.end().getX() - d, lv3.end().getY() - e, lv3.end().getZ() - f, 1.0);
                vector4f.mul(posMatrix, vector4f3);
                vector4f2.mul(posMatrix, vector4f4);
                boolean bl = vector4f3.z > -0.05f;
                boolean bl3 = bl2 = vector4f4.z > -0.05f;
                if (bl && bl2) continue;
                if (bl || bl2) {
                    float g = vector4f4.z - vector4f3.z;
                    if (Math.abs(g) < 1.0E-9f) continue;
                    float h = MathHelper.clamp((-0.05f - vector4f3.z) / g, 0.0f, 1.0f);
                    vector4f.lerp(vector4f2, h, vector4f5);
                    if (bl) {
                        vector4f.set(vector4f5);
                    } else {
                        vector4f2.set(vector4f5);
                    }
                }
                lv.vertex(lv2, vector4f.x, vector4f.y, vector4f.z).normal(lv2, vector4f2.x - vector4f.x, vector4f2.y - vector4f.y, vector4f2.z - vector4f.z).color(lv3.color()).lineWidth(lv3.width());
                lv.vertex(lv2, vector4f2.x, vector4f2.y, vector4f2.z).normal(lv2, vector4f2.x - vector4f.x, vector4f2.y - vector4f.y, vector4f2.z - vector4f.z).color(lv3.color()).lineWidth(lv3.width());
            }
        }

        private void drawTriangleFans(MatrixStack matrices, VertexConsumerProvider vertexConsumers, CameraRenderState cameraRenderState) {
            MatrixStack.Entry lv = matrices.peek();
            double d = cameraRenderState.pos.getX();
            double e = cameraRenderState.pos.getY();
            double f = cameraRenderState.pos.getZ();
            for (Polygon lv2 : this.triangleFans) {
                VertexConsumer lv3 = vertexConsumers.getBuffer(RenderLayers.debugTriangleFan());
                for (Vec3d lv4 : lv2.points()) {
                    lv3.vertex(lv, (float)(lv4.getX() - d), (float)(lv4.getY() - e), (float)(lv4.getZ() - f)).color(lv2.color());
                }
            }
        }

        private void drawQuads(MatrixStack matrices, VertexConsumerProvider vertexConsumers, CameraRenderState cameraRenderState) {
            VertexConsumer lv = vertexConsumers.getBuffer(RenderLayers.debugFilledBox());
            MatrixStack.Entry lv2 = matrices.peek();
            double d = cameraRenderState.pos.getX();
            double e = cameraRenderState.pos.getY();
            double f = cameraRenderState.pos.getZ();
            for (Quad lv3 : this.quads) {
                lv.vertex(lv2, (float)(lv3.a().getX() - d), (float)(lv3.a().getY() - e), (float)(lv3.a().getZ() - f)).color(lv3.color());
                lv.vertex(lv2, (float)(lv3.b().getX() - d), (float)(lv3.b().getY() - e), (float)(lv3.b().getZ() - f)).color(lv3.color());
                lv.vertex(lv2, (float)(lv3.c().getX() - d), (float)(lv3.c().getY() - e), (float)(lv3.c().getZ() - f)).color(lv3.color());
                lv.vertex(lv2, (float)(lv3.d().getX() - d), (float)(lv3.d().getY() - e), (float)(lv3.d().getZ() - f)).color(lv3.color());
            }
        }

        private void drawPoints(MatrixStack matrices, VertexConsumerProvider vertexConsumers, CameraRenderState cameraRenderState) {
            VertexConsumer lv = vertexConsumers.getBuffer(RenderLayers.debugPoint());
            MatrixStack.Entry lv2 = matrices.peek();
            double d = cameraRenderState.pos.getX();
            double e = cameraRenderState.pos.getY();
            double f = cameraRenderState.pos.getZ();
            for (Point lv3 : this.points) {
                lv.vertex(lv2, (float)(lv3.pos.getX() - d), (float)(lv3.pos.getY() - e), (float)(lv3.pos.getZ() - f)).color(lv3.color()).lineWidth(lv3.size());
            }
        }
    }

    @Environment(value=EnvType.CLIENT)
    record Point(Vec3d pos, int color, float size) {
    }

    @Environment(value=EnvType.CLIENT)
    record Line(Vec3d start, Vec3d end, int color, float width) {
    }

    @Environment(value=EnvType.CLIENT)
    record Polygon(Vec3d[] points, int color) {
    }

    @Environment(value=EnvType.CLIENT)
    record Quad(Vec3d a, Vec3d b, Vec3d c, Vec3d d, int color) {
    }

    @Environment(value=EnvType.CLIENT)
    record Text(Vec3d pos, String text, TextGizmo.Style style) {
    }
}

