/*
 * External method calls:
 *   Lnet/minecraft/client/gl/DynamicUniforms;write(Lorg/joml/Matrix4fc;Lorg/joml/Vector4fc;Lorg/joml/Vector3fc;Lorg/joml/Matrix4fc;)Lcom/mojang/blaze3d/buffers/GpuBufferSlice;
 *   Lnet/minecraft/client/render/RenderSetup;resolveTextures()Ljava/util/Map;
 *   Lnet/minecraft/client/render/BuiltBuffer$DrawParameters;mode()Lcom/mojang/blaze3d/vertex/VertexFormat$DrawMode;
 *   Lnet/minecraft/client/render/BuiltBuffer$DrawParameters;indexType()Lcom/mojang/blaze3d/vertex/VertexFormat$IndexType;
 *   Lnet/minecraft/client/render/RenderSetup$Texture;textureView()Lcom/mojang/blaze3d/textures/GpuTextureView;
 *   Lnet/minecraft/client/render/RenderSetup$Texture;sampler()Lnet/minecraft/client/gl/GpuSampler;
 *   Lnet/minecraft/client/render/RenderSetup$TextureSpec;location()Lnet/minecraft/util/Identifier;
 */
package net.minecraft.client.render;

import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.buffers.GpuBufferSlice;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.systems.RenderPass;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.GpuTextureView;
import com.mojang.blaze3d.vertex.VertexFormat;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.function.Consumer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.gl.ScissorState;
import net.minecraft.client.render.BuiltBuffer;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.client.render.RenderSetup;
import org.joml.Matrix4fStack;
import org.joml.Vector3f;
import org.joml.Vector4f;

@Environment(value=EnvType.CLIENT)
public class RenderLayer {
    private static final int field_64012 = 0x100000;
    public static final int field_64008 = 0x400000;
    public static final int field_64009 = 786432;
    public static final int field_64010 = 1536;
    private final RenderSetup renderSetup;
    private final Optional<RenderLayer> affectedOutline;
    protected final String name;

    private RenderLayer(String name, RenderSetup renderSetup) {
        this.name = name;
        this.renderSetup = renderSetup;
        this.affectedOutline = renderSetup.outlineMode == RenderSetup.OutlineMode.AFFECTS_OUTLINE ? renderSetup.textures.values().stream().findFirst().map(texture -> RenderLayers.OUTLINE.apply(texture.location(), arg.pipeline.isCull())) : Optional.empty();
    }

    static RenderLayer of(String name, RenderSetup renderSetup) {
        return new RenderLayer(name, renderSetup);
    }

    public String toString() {
        return "RenderType[" + this.name + ":" + String.valueOf(this.renderSetup) + "]";
    }

    public void draw(BuiltBuffer buffer) {
        Matrix4fStack matrix4fStack = RenderSystem.getModelViewStack();
        Consumer<Matrix4fStack> consumer = this.renderSetup.layeringTransform.getTransform();
        if (consumer != null) {
            matrix4fStack.pushMatrix();
            consumer.accept(matrix4fStack);
        }
        GpuBufferSlice gpuBufferSlice = RenderSystem.getDynamicUniforms().write(RenderSystem.getModelViewMatrix(), new Vector4f(1.0f, 1.0f, 1.0f, 1.0f), new Vector3f(), this.renderSetup.textureTransform.getTransformSupplier());
        Map<String, RenderSetup.Texture> map = this.renderSetup.resolveTextures();
        try (BuiltBuffer builtBuffer = buffer;){
            GpuTextureView gpuTextureView;
            VertexFormat.IndexType lv2;
            GpuBuffer gpuBuffer2;
            GpuBuffer gpuBuffer = this.renderSetup.pipeline.getVertexFormat().uploadImmediateVertexBuffer(buffer.getBuffer());
            if (buffer.getSortedBuffer() == null) {
                RenderSystem.ShapeIndexBuffer lv = RenderSystem.getSequentialBuffer(buffer.getDrawParameters().mode());
                gpuBuffer2 = lv.getIndexBuffer(buffer.getDrawParameters().indexCount());
                lv2 = lv.getIndexType();
            } else {
                gpuBuffer2 = this.renderSetup.pipeline.getVertexFormat().uploadImmediateIndexBuffer(buffer.getSortedBuffer());
                lv2 = buffer.getDrawParameters().indexType();
            }
            Framebuffer lv3 = this.renderSetup.outputTarget.getFramebuffer();
            GpuTextureView gpuTextureView2 = gpuTextureView = RenderSystem.outputColorTextureOverride != null ? RenderSystem.outputColorTextureOverride : lv3.getColorAttachmentView();
            GpuTextureView gpuTextureView22 = lv3.useDepthAttachment ? (RenderSystem.outputDepthTextureOverride != null ? RenderSystem.outputDepthTextureOverride : lv3.getDepthAttachmentView()) : null;
            try (RenderPass renderPass = RenderSystem.getDevice().createCommandEncoder().createRenderPass(() -> "Immediate draw for " + this.name, gpuTextureView, OptionalInt.empty(), gpuTextureView22, OptionalDouble.empty());){
                renderPass.setPipeline(this.renderSetup.pipeline);
                ScissorState lv4 = RenderSystem.getScissorStateForRenderTypeDraws();
                if (lv4.isEnabled()) {
                    renderPass.enableScissor(lv4.getX(), lv4.getY(), lv4.getWidth(), lv4.getHeight());
                }
                RenderSystem.bindDefaultUniforms(renderPass);
                renderPass.setUniform("DynamicTransforms", gpuBufferSlice);
                renderPass.setVertexBuffer(0, gpuBuffer);
                for (Map.Entry<String, RenderSetup.Texture> entry : map.entrySet()) {
                    renderPass.bindTexture(entry.getKey(), entry.getValue().textureView(), entry.getValue().sampler());
                }
                renderPass.setIndexBuffer(gpuBuffer2, lv2);
                renderPass.drawIndexed(0, 0, buffer.getDrawParameters().indexCount(), 1);
            }
        }
        if (consumer != null) {
            matrix4fStack.popMatrix();
        }
    }

    public int getExpectedBufferSize() {
        return this.renderSetup.expectedBufferSize;
    }

    public VertexFormat getVertexFormat() {
        return this.renderSetup.pipeline.getVertexFormat();
    }

    public VertexFormat.DrawMode getDrawMode() {
        return this.renderSetup.pipeline.getVertexFormatMode();
    }

    public Optional<RenderLayer> getAffectedOutline() {
        return this.affectedOutline;
    }

    public boolean isOutline() {
        return this.renderSetup.outlineMode == RenderSetup.OutlineMode.IS_OUTLINE;
    }

    public RenderPipeline getRenderPipeline() {
        return this.renderSetup.pipeline;
    }

    public boolean hasCrumbling() {
        return this.renderSetup.hasCrumbling;
    }

    public boolean areVerticesNotShared() {
        return !this.getDrawMode().shareVertices;
    }

    public boolean isTranslucent() {
        return this.renderSetup.translucent;
    }
}

