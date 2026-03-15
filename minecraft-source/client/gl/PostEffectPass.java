/*
 * External method calls:
 *   Lnet/minecraft/client/gl/UniformValue;addSize(Lcom/mojang/blaze3d/buffers/Std140SizeCalculator;)V
 *   Lnet/minecraft/client/gl/UniformValue;write(Lcom/mojang/blaze3d/buffers/Std140Builder;)V
 *   Lnet/minecraft/client/render/FrameGraphBuilder;createPass(Ljava/lang/String;)Lnet/minecraft/client/render/FramePass;
 *   Lnet/minecraft/client/gl/PostEffectPass$Sampler;preRender(Lnet/minecraft/client/render/FramePass;Ljava/util/Map;)V
 *   Lnet/minecraft/client/gl/PostEffectPass$Target;samplerName()Ljava/lang/String;
 *   Lnet/minecraft/client/gl/PostEffectPass$Target;view()Lcom/mojang/blaze3d/textures/GpuTextureView;
 *   Lnet/minecraft/client/gl/PostEffectPass$Target;sampler()Lnet/minecraft/client/gl/GpuSampler;
 *   Lnet/minecraft/client/gl/PostEffectPass$Sampler;postRender(Ljava/util/Map;)V
 *   Lnet/minecraft/client/gl/PostEffectPass$Sampler;samplerName()Ljava/lang/String;
 *   Lnet/minecraft/client/render/FramePass;transfer(Lnet/minecraft/client/util/Handle;)Lnet/minecraft/client/util/Handle;
 */
package net.minecraft.client.gl;

import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.buffers.GpuBufferSlice;
import com.mojang.blaze3d.buffers.Std140Builder;
import com.mojang.blaze3d.buffers.Std140SizeCalculator;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.systems.CommandEncoder;
import com.mojang.blaze3d.systems.ProjectionType;
import com.mojang.blaze3d.systems.RenderPass;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.FilterMode;
import com.mojang.blaze3d.textures.GpuTextureView;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.gl.GpuSampler;
import net.minecraft.client.gl.MappableRingBuffer;
import net.minecraft.client.gl.SamplerCache;
import net.minecraft.client.gl.UniformValue;
import net.minecraft.client.render.FrameGraphBuilder;
import net.minecraft.client.render.FramePass;
import net.minecraft.client.texture.AbstractTexture;
import net.minecraft.client.util.Handle;
import net.minecraft.util.Identifier;
import org.lwjgl.system.MemoryStack;

@Environment(value=EnvType.CLIENT)
public class PostEffectPass
implements AutoCloseable {
    private static final int SIZE = new Std140SizeCalculator().putVec2().get();
    private final String id;
    private final RenderPipeline pipeline;
    private final Identifier outputTargetId;
    private final Map<String, GpuBuffer> uniformBuffers = new HashMap<String, GpuBuffer>();
    private final MappableRingBuffer samplerInfoBuffer;
    private final List<Sampler> samplers;

    public PostEffectPass(RenderPipeline pipeline, Identifier outputTargetId, Map<String, List<UniformValue>> uniforms, List<Sampler> samplers) {
        this.pipeline = pipeline;
        this.id = pipeline.getLocation().toString();
        this.outputTargetId = outputTargetId;
        this.samplers = samplers;
        for (Map.Entry<String, List<UniformValue>> entry : uniforms.entrySet()) {
            List<UniformValue> list2 = entry.getValue();
            if (list2.isEmpty()) continue;
            Std140SizeCalculator std140SizeCalculator = new Std140SizeCalculator();
            for (UniformValue lv : list2) {
                lv.addSize(std140SizeCalculator);
            }
            int i = std140SizeCalculator.get();
            MemoryStack memoryStack = MemoryStack.stackPush();
            try {
                Std140Builder std140Builder = Std140Builder.onStack(memoryStack, i);
                for (UniformValue lv2 : list2) {
                    lv2.write(std140Builder);
                }
                this.uniformBuffers.put(entry.getKey(), RenderSystem.getDevice().createBuffer(() -> this.id + " / " + (String)entry.getKey(), GpuBuffer.USAGE_UNIFORM, std140Builder.get()));
            } finally {
                if (memoryStack == null) continue;
                memoryStack.close();
            }
        }
        this.samplerInfoBuffer = new MappableRingBuffer(() -> this.id + " SamplerInfo", 130, (samplers.size() + 1) * SIZE);
    }

    public void render(FrameGraphBuilder builder, Map<Identifier, Handle<Framebuffer>> handles, GpuBufferSlice slice) {
        FramePass lv = builder.createPass(this.id);
        for (Sampler lv2 : this.samplers) {
            lv2.preRender(lv, handles);
        }
        Handle lv3 = handles.computeIfPresent(this.outputTargetId, (id, handle) -> lv.transfer(handle));
        if (lv3 == null) {
            throw new IllegalStateException("Missing handle for target " + String.valueOf(this.outputTargetId));
        }
        lv.setRenderer(() -> {
            Framebuffer lv = (Framebuffer)lv3.get();
            RenderSystem.backupProjectionMatrix();
            RenderSystem.setProjectionMatrix(slice, ProjectionType.ORTHOGRAPHIC);
            CommandEncoder commandEncoder = RenderSystem.getDevice().createCommandEncoder();
            SamplerCache lv2 = RenderSystem.getSamplerCache();
            List<Target> list = this.samplers.stream().map(sampler -> new Target(sampler.samplerName(), sampler.getTexture(handles), lv2.get(sampler.bilinear() ? FilterMode.LINEAR : FilterMode.NEAREST))).toList();
            try (GpuBuffer.MappedView mappedView = commandEncoder.mapBuffer(this.samplerInfoBuffer.getBlocking(), false, true);){
                Iterator<Target> std140Builder = Std140Builder.intoBuffer(mappedView.data());
                ((Std140Builder)((Object)std140Builder)).putVec2(lv.textureWidth, lv.textureHeight);
                for (Target lv3 : list) {
                    ((Std140Builder)((Object)std140Builder)).putVec2(lv3.view.getWidth(0), lv3.view.getHeight(0));
                }
            }
            try (RenderPass renderPass = commandEncoder.createRenderPass(() -> "Post pass " + this.id, lv.getColorAttachmentView(), OptionalInt.empty(), lv.useDepthAttachment ? lv.getDepthAttachmentView() : null, OptionalDouble.empty());){
                renderPass.setPipeline(this.pipeline);
                RenderSystem.bindDefaultUniforms(renderPass);
                renderPass.setUniform("SamplerInfo", this.samplerInfoBuffer.getBlocking());
                for (Map.Entry entry : this.uniformBuffers.entrySet()) {
                    renderPass.setUniform((String)entry.getKey(), (GpuBuffer)entry.getValue());
                }
                for (Target target : list) {
                    renderPass.bindTexture(target.samplerName() + "Sampler", target.view(), target.sampler());
                }
                renderPass.draw(0, 3);
            }
            this.samplerInfoBuffer.rotate();
            RenderSystem.restoreProjectionMatrix();
            for (Sampler lv5 : this.samplers) {
                lv5.postRender(handles);
            }
        });
    }

    @Override
    public void close() {
        for (GpuBuffer gpuBuffer : this.uniformBuffers.values()) {
            gpuBuffer.close();
        }
        this.samplerInfoBuffer.close();
    }

    @Environment(value=EnvType.CLIENT)
    public static interface Sampler {
        public void preRender(FramePass var1, Map<Identifier, Handle<Framebuffer>> var2);

        default public void postRender(Map<Identifier, Handle<Framebuffer>> internalTargets) {
        }

        public GpuTextureView getTexture(Map<Identifier, Handle<Framebuffer>> var1);

        public String samplerName();

        public boolean bilinear();
    }

    @Environment(value=EnvType.CLIENT)
    record Target(String samplerName, GpuTextureView view, GpuSampler sampler) {
    }

    @Environment(value=EnvType.CLIENT)
    public record TargetSampler(String samplerName, Identifier targetId, boolean depthBuffer, boolean bilinear) implements Sampler
    {
        private Handle<Framebuffer> getTarget(Map<Identifier, Handle<Framebuffer>> internalTargets) {
            Handle<Framebuffer> lv = internalTargets.get(this.targetId);
            if (lv == null) {
                throw new IllegalStateException("Missing handle for target " + String.valueOf(this.targetId));
            }
            return lv;
        }

        @Override
        public void preRender(FramePass pass, Map<Identifier, Handle<Framebuffer>> internalTargets) {
            pass.dependsOn(this.getTarget(internalTargets));
        }

        @Override
        public GpuTextureView getTexture(Map<Identifier, Handle<Framebuffer>> internalTargets) {
            GpuTextureView gpuTextureView;
            Handle<Framebuffer> lv = this.getTarget(internalTargets);
            Framebuffer lv2 = lv.get();
            GpuTextureView gpuTextureView2 = gpuTextureView = this.depthBuffer ? lv2.getDepthAttachmentView() : lv2.getColorAttachmentView();
            if (gpuTextureView == null) {
                throw new IllegalStateException("Missing " + (this.depthBuffer ? "depth" : "color") + "texture for target " + String.valueOf(this.targetId));
            }
            return gpuTextureView;
        }
    }

    @Environment(value=EnvType.CLIENT)
    public record TextureSampler(String samplerName, AbstractTexture texture, int width, int height, boolean bilinear) implements Sampler
    {
        @Override
        public void preRender(FramePass pass, Map<Identifier, Handle<Framebuffer>> internalTargets) {
        }

        @Override
        public GpuTextureView getTexture(Map<Identifier, Handle<Framebuffer>> internalTargets) {
            return this.texture.getGlTextureView();
        }
    }
}

