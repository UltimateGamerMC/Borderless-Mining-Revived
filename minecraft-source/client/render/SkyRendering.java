/*
 * External method calls:
 *   Lnet/minecraft/client/util/BufferAllocator;fixedSized(I)Lnet/minecraft/client/util/BufferAllocator;
 *   Lnet/minecraft/client/render/BufferBuilder;end()Lnet/minecraft/client/render/BuiltBuffer;
 *   Lnet/minecraft/client/render/BufferBuilder;vertex(FFF)Lnet/minecraft/client/render/VertexConsumer;
 *   Lnet/minecraft/client/render/VertexConsumer;color(I)Lnet/minecraft/client/render/VertexConsumer;
 *   Lnet/minecraft/client/render/VertexConsumer;texture(FF)Lnet/minecraft/client/render/VertexConsumer;
 *   Lnet/minecraft/world/MoonPhase;values()[Lnet/minecraft/world/MoonPhase;
 *   Lnet/minecraft/world/MoonPhase;asString()Ljava/lang/String;
 *   Lnet/minecraft/util/Identifier;ofVanilla(Ljava/lang/String;)Lnet/minecraft/util/Identifier;
 *   Lnet/minecraft/client/render/BufferBuilder;vertex(Lorg/joml/Vector3fc;)Lnet/minecraft/client/render/VertexConsumer;
 *   Lnet/minecraft/client/render/VertexConsumer;vertex(FFF)Lnet/minecraft/client/render/VertexConsumer;
 *   Lnet/minecraft/client/render/BufferBuilder;vertex(Lorg/joml/Matrix4fc;FFF)Lnet/minecraft/client/render/VertexConsumer;
 *   Lnet/minecraft/client/gl/DynamicUniforms;write(Lorg/joml/Matrix4fc;Lorg/joml/Vector4fc;Lorg/joml/Vector3fc;Lorg/joml/Matrix4fc;)Lcom/mojang/blaze3d/buffers/GpuBufferSlice;
 *   Lnet/minecraft/world/dimension/DimensionType;skybox()Lnet/minecraft/world/dimension/DimensionType$Skybox;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/client/render/SkyRendering;createStars()Lcom/mojang/blaze3d/buffers/GpuBuffer;
 *   Lnet/minecraft/client/render/SkyRendering;createEndSky()Lcom/mojang/blaze3d/buffers/GpuBuffer;
 *   Lnet/minecraft/client/render/SkyRendering;bindTexture(Lnet/minecraft/client/texture/TextureManager;Lnet/minecraft/util/Identifier;)Lnet/minecraft/client/texture/AbstractTexture;
 *   Lnet/minecraft/client/render/SkyRendering;createEndFlash(Lnet/minecraft/client/texture/SpriteAtlasTexture;)Lcom/mojang/blaze3d/buffers/GpuBuffer;
 *   Lnet/minecraft/client/render/SkyRendering;createSun(Lnet/minecraft/client/texture/SpriteAtlasTexture;)Lcom/mojang/blaze3d/buffers/GpuBuffer;
 *   Lnet/minecraft/client/render/SkyRendering;createMoonPhases(Lnet/minecraft/client/texture/SpriteAtlasTexture;)Lcom/mojang/blaze3d/buffers/GpuBuffer;
 *   Lnet/minecraft/client/render/SkyRendering;createSunRise()Lcom/mojang/blaze3d/buffers/GpuBuffer;
 *   Lnet/minecraft/client/render/SkyRendering;createSky(Lnet/minecraft/client/render/VertexConsumer;F)V
 *   Lnet/minecraft/client/render/SkyRendering;createQuadVertexBuffer(Ljava/lang/String;Lnet/minecraft/client/texture/Sprite;)Lcom/mojang/blaze3d/buffers/GpuBuffer;
 *   Lnet/minecraft/client/render/SkyRendering;renderSun(FLnet/minecraft/client/util/math/MatrixStack;)V
 *   Lnet/minecraft/client/render/SkyRendering;renderMoon(Lnet/minecraft/world/MoonPhase;FLnet/minecraft/client/util/math/MatrixStack;)V
 *   Lnet/minecraft/client/render/SkyRendering;renderStars(FLnet/minecraft/client/util/math/MatrixStack;)V
 */
package net.minecraft.client.render;

import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.buffers.GpuBufferSlice;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.systems.RenderPass;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.GpuTextureView;
import com.mojang.blaze3d.vertex.VertexFormat;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.BuiltBuffer;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.EndLightFlashManager;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.render.state.SkyRenderState;
import net.minecraft.client.texture.AbstractTexture;
import net.minecraft.client.texture.AtlasManager;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.client.util.BufferAllocator;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.Atlases;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.MoonPhase;
import net.minecraft.world.attribute.EnvironmentAttributeInterpolator;
import net.minecraft.world.attribute.EnvironmentAttributes;
import net.minecraft.world.dimension.DimensionType;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Matrix4fStack;
import org.joml.Vector3f;
import org.joml.Vector4f;

@Environment(value=EnvType.CLIENT)
public class SkyRendering
implements AutoCloseable {
    private static final Identifier SUN_TEXTURE = Identifier.ofVanilla("sun");
    private static final Identifier END_FLASH_TEXTURE = Identifier.ofVanilla("end_flash");
    private static final Identifier END_SKY_TEXTURE = Identifier.ofVanilla("textures/environment/end_sky.png");
    private static final float field_53144 = 512.0f;
    private static final int field_57932 = 10;
    private static final int field_57933 = 1500;
    private static final float field_62950 = 30.0f;
    private static final float field_62951 = 100.0f;
    private static final float field_62952 = 20.0f;
    private static final float field_62953 = 100.0f;
    private static final int field_62954 = 16;
    private static final int field_57934 = 6;
    private static final float field_62955 = 100.0f;
    private static final float field_62956 = 60.0f;
    private final SpriteAtlasTexture celestialAtlasTexture;
    private final GpuBuffer starVertexBuffer;
    private final GpuBuffer topSkyVertexBuffer;
    private final GpuBuffer bottomSkyVertexBuffer;
    private final GpuBuffer endSkyVertexBuffer;
    private final GpuBuffer sunVertexBuffer;
    private final GpuBuffer moonPhaseVertexBuffer;
    private final GpuBuffer sunRiseVertexBuffer;
    private final GpuBuffer endFlashVertexBuffer;
    private final RenderSystem.ShapeIndexBuffer indexBuffer2 = RenderSystem.getSequentialBuffer(VertexFormat.DrawMode.QUADS);
    private final AbstractTexture endSkyTexture;
    private int starIndexCount;

    public SkyRendering(TextureManager textureManager, AtlasManager atlasManager) {
        this.celestialAtlasTexture = atlasManager.getAtlasTexture(Atlases.CELESTIALS);
        this.starVertexBuffer = this.createStars();
        this.endSkyVertexBuffer = SkyRendering.createEndSky();
        this.endSkyTexture = this.bindTexture(textureManager, END_SKY_TEXTURE);
        this.endFlashVertexBuffer = SkyRendering.createEndFlash(this.celestialAtlasTexture);
        this.sunVertexBuffer = SkyRendering.createSun(this.celestialAtlasTexture);
        this.moonPhaseVertexBuffer = SkyRendering.createMoonPhases(this.celestialAtlasTexture);
        this.sunRiseVertexBuffer = this.createSunRise();
        try (BufferAllocator lv = BufferAllocator.fixedSized(10 * VertexFormats.POSITION.getVertexSize());){
            BufferBuilder lv2 = new BufferBuilder(lv, VertexFormat.DrawMode.TRIANGLE_FAN, VertexFormats.POSITION);
            this.createSky(lv2, 16.0f);
            try (BuiltBuffer lv3 = lv2.end();){
                this.topSkyVertexBuffer = RenderSystem.getDevice().createBuffer(() -> "Top sky vertex buffer", GpuBuffer.USAGE_VERTEX, lv3.getBuffer());
            }
            lv2 = new BufferBuilder(lv, VertexFormat.DrawMode.TRIANGLE_FAN, VertexFormats.POSITION);
            this.createSky(lv2, -16.0f);
            lv3 = lv2.end();
            try {
                this.bottomSkyVertexBuffer = RenderSystem.getDevice().createBuffer(() -> "Bottom sky vertex buffer", GpuBuffer.USAGE_VERTEX, lv3.getBuffer());
            } finally {
                if (lv3 != null) {
                    lv3.close();
                }
            }
        }
    }

    private AbstractTexture bindTexture(TextureManager textureManager, Identifier texture) {
        return textureManager.getTexture(texture);
    }

    private GpuBuffer createSunRise() {
        int i = 18;
        int j = VertexFormats.POSITION_COLOR.getVertexSize();
        try (BufferAllocator lv = BufferAllocator.fixedSized(18 * j);){
            BufferBuilder lv2 = new BufferBuilder(lv, VertexFormat.DrawMode.TRIANGLE_FAN, VertexFormats.POSITION_COLOR);
            int k = ColorHelper.getWhite(1.0f);
            int l = ColorHelper.getWhite(0.0f);
            lv2.vertex(0.0f, 100.0f, 0.0f).color(k);
            for (int m = 0; m <= 16; ++m) {
                float f = (float)m * ((float)Math.PI * 2) / 16.0f;
                float g = MathHelper.sin(f);
                float h = MathHelper.cos(f);
                lv2.vertex(g * 120.0f, h * 120.0f, -h * 40.0f).color(l);
            }
            BuiltBuffer lv3 = lv2.end();
            try {
                GpuBuffer gpuBuffer = RenderSystem.getDevice().createBuffer(() -> "Sunrise/Sunset fan", GpuBuffer.USAGE_VERTEX, lv3.getBuffer());
                if (lv3 != null) {
                    lv3.close();
                }
                return gpuBuffer;
            } catch (Throwable throwable) {
                if (lv3 != null) {
                    try {
                        lv3.close();
                    } catch (Throwable throwable2) {
                        throwable.addSuppressed(throwable2);
                    }
                }
                throw throwable;
            }
        }
    }

    private static GpuBuffer createSun(SpriteAtlasTexture atlas) {
        return SkyRendering.createQuadVertexBuffer("Sun quad", atlas.getSprite(SUN_TEXTURE));
    }

    private static GpuBuffer createEndFlash(SpriteAtlasTexture atlas) {
        return SkyRendering.createQuadVertexBuffer("End flash quad", atlas.getSprite(END_FLASH_TEXTURE));
    }

    private static GpuBuffer createQuadVertexBuffer(String description, Sprite sprite) {
        VertexFormat vertexFormat = VertexFormats.POSITION_TEXTURE;
        try (BufferAllocator lv = BufferAllocator.fixedSized(4 * vertexFormat.getVertexSize());){
            BufferBuilder lv2 = new BufferBuilder(lv, VertexFormat.DrawMode.QUADS, vertexFormat);
            lv2.vertex(-1.0f, 0.0f, -1.0f).texture(sprite.getMinU(), sprite.getMinV());
            lv2.vertex(1.0f, 0.0f, -1.0f).texture(sprite.getMaxU(), sprite.getMinV());
            lv2.vertex(1.0f, 0.0f, 1.0f).texture(sprite.getMaxU(), sprite.getMaxV());
            lv2.vertex(-1.0f, 0.0f, 1.0f).texture(sprite.getMinU(), sprite.getMaxV());
            BuiltBuffer lv3 = lv2.end();
            try {
                GpuBuffer gpuBuffer = RenderSystem.getDevice().createBuffer(() -> description, GpuBuffer.USAGE_VERTEX, lv3.getBuffer());
                if (lv3 != null) {
                    lv3.close();
                }
                return gpuBuffer;
            } catch (Throwable throwable) {
                if (lv3 != null) {
                    try {
                        lv3.close();
                    } catch (Throwable throwable2) {
                        throwable.addSuppressed(throwable2);
                    }
                }
                throw throwable;
            }
        }
    }

    private static GpuBuffer createMoonPhases(SpriteAtlasTexture atlas) {
        MoonPhase[] lvs = MoonPhase.values();
        VertexFormat vertexFormat = VertexFormats.POSITION_TEXTURE;
        try (BufferAllocator lv = BufferAllocator.fixedSized(lvs.length * 4 * vertexFormat.getVertexSize());){
            BufferBuilder lv2 = new BufferBuilder(lv, VertexFormat.DrawMode.QUADS, vertexFormat);
            for (MoonPhase lv3 : lvs) {
                Sprite lv4 = atlas.getSprite(Identifier.ofVanilla("moon/" + lv3.asString()));
                lv2.vertex(-1.0f, 0.0f, -1.0f).texture(lv4.getMaxU(), lv4.getMaxV());
                lv2.vertex(1.0f, 0.0f, -1.0f).texture(lv4.getMinU(), lv4.getMaxV());
                lv2.vertex(1.0f, 0.0f, 1.0f).texture(lv4.getMinU(), lv4.getMinV());
                lv2.vertex(-1.0f, 0.0f, 1.0f).texture(lv4.getMaxU(), lv4.getMinV());
            }
            BuiltBuffer lv5 = lv2.end();
            try {
                GpuBuffer gpuBuffer = RenderSystem.getDevice().createBuffer(() -> "Moon phases", GpuBuffer.USAGE_VERTEX, lv5.getBuffer());
                if (lv5 != null) {
                    lv5.close();
                }
                return gpuBuffer;
            } catch (Throwable throwable) {
                if (lv5 != null) {
                    try {
                        lv5.close();
                    } catch (Throwable throwable2) {
                        throwable.addSuppressed(throwable2);
                    }
                }
                throw throwable;
            }
        }
    }

    private GpuBuffer createStars() {
        Random lv = Random.create(10842L);
        float f = 100.0f;
        try (BufferAllocator lv2 = BufferAllocator.fixedSized(VertexFormats.POSITION.getVertexSize() * 1500 * 4);){
            BufferBuilder lv3 = new BufferBuilder(lv2, VertexFormat.DrawMode.QUADS, VertexFormats.POSITION);
            for (int i = 0; i < 1500; ++i) {
                float g = lv.nextFloat() * 2.0f - 1.0f;
                float h = lv.nextFloat() * 2.0f - 1.0f;
                float j = lv.nextFloat() * 2.0f - 1.0f;
                float k = 0.15f + lv.nextFloat() * 0.1f;
                float l = MathHelper.magnitude(g, h, j);
                if (l <= 0.010000001f || l >= 1.0f) continue;
                Vector3f vector3f = new Vector3f(g, h, j).normalize(100.0f);
                float m = (float)(lv.nextDouble() * 3.1415927410125732 * 2.0);
                Matrix3f matrix3f = new Matrix3f().rotateTowards(new Vector3f(vector3f).negate(), new Vector3f(0.0f, 1.0f, 0.0f)).rotateZ(-m);
                lv3.vertex(new Vector3f(k, -k, 0.0f).mul(matrix3f).add(vector3f));
                lv3.vertex(new Vector3f(k, k, 0.0f).mul(matrix3f).add(vector3f));
                lv3.vertex(new Vector3f(-k, k, 0.0f).mul(matrix3f).add(vector3f));
                lv3.vertex(new Vector3f(-k, -k, 0.0f).mul(matrix3f).add(vector3f));
            }
            BuiltBuffer lv4 = lv3.end();
            try {
                this.starIndexCount = lv4.getDrawParameters().indexCount();
                GpuBuffer gpuBuffer = RenderSystem.getDevice().createBuffer(() -> "Stars vertex buffer", GpuBuffer.USAGE_VERTEX | GpuBuffer.USAGE_COPY_DST, lv4.getBuffer());
                if (lv4 != null) {
                    lv4.close();
                }
                return gpuBuffer;
            } catch (Throwable throwable) {
                if (lv4 != null) {
                    try {
                        lv4.close();
                    } catch (Throwable throwable2) {
                        throwable.addSuppressed(throwable2);
                    }
                }
                throw throwable;
            }
        }
    }

    private void createSky(VertexConsumer vertexConsumer, float height) {
        float g = Math.signum(height) * 512.0f;
        vertexConsumer.vertex(0.0f, height, 0.0f);
        for (int i = -180; i <= 180; i += 45) {
            vertexConsumer.vertex(g * MathHelper.cos((float)i * ((float)Math.PI / 180)), height, 512.0f * MathHelper.sin((float)i * ((float)Math.PI / 180)));
        }
    }

    private static GpuBuffer createEndSky() {
        try (BufferAllocator lv = BufferAllocator.fixedSized(24 * VertexFormats.POSITION_TEXTURE_COLOR.getVertexSize());){
            BufferBuilder lv2 = new BufferBuilder(lv, VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);
            for (int i = 0; i < 6; ++i) {
                Matrix4f matrix4f = new Matrix4f();
                switch (i) {
                    case 1: {
                        matrix4f.rotationX(1.5707964f);
                        break;
                    }
                    case 2: {
                        matrix4f.rotationX(-1.5707964f);
                        break;
                    }
                    case 3: {
                        matrix4f.rotationX((float)Math.PI);
                        break;
                    }
                    case 4: {
                        matrix4f.rotationZ(1.5707964f);
                        break;
                    }
                    case 5: {
                        matrix4f.rotationZ(-1.5707964f);
                    }
                }
                lv2.vertex(matrix4f, -100.0f, -100.0f, -100.0f).texture(0.0f, 0.0f).color(-14145496);
                lv2.vertex(matrix4f, -100.0f, -100.0f, 100.0f).texture(0.0f, 16.0f).color(-14145496);
                lv2.vertex(matrix4f, 100.0f, -100.0f, 100.0f).texture(16.0f, 16.0f).color(-14145496);
                lv2.vertex(matrix4f, 100.0f, -100.0f, -100.0f).texture(16.0f, 0.0f).color(-14145496);
            }
            BuiltBuffer lv3 = lv2.end();
            try {
                GpuBuffer gpuBuffer = RenderSystem.getDevice().createBuffer(() -> "End sky vertex buffer", GpuBuffer.USAGE_VERTEX | GpuBuffer.USAGE_COPY_DST, lv3.getBuffer());
                if (lv3 != null) {
                    lv3.close();
                }
                return gpuBuffer;
            } catch (Throwable throwable) {
                if (lv3 != null) {
                    try {
                        lv3.close();
                    } catch (Throwable throwable2) {
                        throwable.addSuppressed(throwable2);
                    }
                }
                throw throwable;
            }
        }
    }

    public void renderTopSky(int i) {
        GpuBufferSlice gpuBufferSlice = RenderSystem.getDynamicUniforms().write(RenderSystem.getModelViewMatrix(), ColorHelper.toRgbaVector(i), new Vector3f(), new Matrix4f());
        GpuTextureView gpuTextureView = MinecraftClient.getInstance().getFramebuffer().getColorAttachmentView();
        GpuTextureView gpuTextureView2 = MinecraftClient.getInstance().getFramebuffer().getDepthAttachmentView();
        try (RenderPass renderPass = RenderSystem.getDevice().createCommandEncoder().createRenderPass(() -> "Sky disc", gpuTextureView, OptionalInt.empty(), gpuTextureView2, OptionalDouble.empty());){
            renderPass.setPipeline(RenderPipelines.POSITION_SKY);
            RenderSystem.bindDefaultUniforms(renderPass);
            renderPass.setUniform("DynamicTransforms", gpuBufferSlice);
            renderPass.setVertexBuffer(0, this.topSkyVertexBuffer);
            renderPass.draw(0, 10);
        }
    }

    public void updateRenderState(ClientWorld world, float tickProgress, Camera camera, SkyRenderState state) {
        state.skybox = world.getDimension().skybox();
        if (state.skybox == DimensionType.Skybox.NONE) {
            return;
        }
        if (state.skybox == DimensionType.Skybox.END) {
            EndLightFlashManager lv = world.getEndLightFlashManager();
            if (lv == null) {
                return;
            }
            state.endFlashIntensity = lv.getSkyFactor(tickProgress);
            state.endFlashPitch = lv.getPitch();
            state.endFlashYaw = lv.getYaw();
            return;
        }
        EnvironmentAttributeInterpolator lv2 = camera.getEnvironmentAttributeInterpolator();
        state.sunAngle = lv2.get(EnvironmentAttributes.SUN_ANGLE_VISUAL, tickProgress).floatValue() * ((float)Math.PI / 180);
        state.moonAngle = lv2.get(EnvironmentAttributes.MOON_ANGLE_VISUAL, tickProgress).floatValue() * ((float)Math.PI / 180);
        state.starAngle = lv2.get(EnvironmentAttributes.STAR_ANGLE_VISUAL, tickProgress).floatValue() * ((float)Math.PI / 180);
        state.rainGradient = 1.0f - world.getRainGradient(tickProgress);
        state.starBrightness = lv2.get(EnvironmentAttributes.STAR_BRIGHTNESS_VISUAL, tickProgress).floatValue();
        state.sunriseAndSunsetColor = camera.getEnvironmentAttributeInterpolator().get(EnvironmentAttributes.SUNRISE_SUNSET_COLOR_VISUAL, tickProgress);
        state.moonPhase = lv2.get(EnvironmentAttributes.MOON_PHASE_VISUAL, tickProgress);
        state.skyColor = lv2.get(EnvironmentAttributes.SKY_COLOR_VISUAL, tickProgress);
        state.shouldRenderSkyDark = this.isSkyDark(tickProgress, world);
    }

    private boolean isSkyDark(float tickProgress, ClientWorld world) {
        return MinecraftClient.getInstance().player.getCameraPosVec((float)tickProgress).y - world.getLevelProperties().getSkyDarknessHeight(world) < 0.0;
    }

    public void renderSkyDark() {
        Matrix4fStack matrix4fStack = RenderSystem.getModelViewStack();
        matrix4fStack.pushMatrix();
        matrix4fStack.translate(0.0f, 12.0f, 0.0f);
        GpuBufferSlice gpuBufferSlice = RenderSystem.getDynamicUniforms().write(matrix4fStack, new Vector4f(0.0f, 0.0f, 0.0f, 1.0f), new Vector3f(), new Matrix4f());
        GpuTextureView gpuTextureView = MinecraftClient.getInstance().getFramebuffer().getColorAttachmentView();
        GpuTextureView gpuTextureView2 = MinecraftClient.getInstance().getFramebuffer().getDepthAttachmentView();
        try (RenderPass renderPass = RenderSystem.getDevice().createCommandEncoder().createRenderPass(() -> "Sky dark", gpuTextureView, OptionalInt.empty(), gpuTextureView2, OptionalDouble.empty());){
            renderPass.setPipeline(RenderPipelines.POSITION_SKY);
            RenderSystem.bindDefaultUniforms(renderPass);
            renderPass.setUniform("DynamicTransforms", gpuBufferSlice);
            renderPass.setVertexBuffer(0, this.bottomSkyVertexBuffer);
            renderPass.draw(0, 10);
        }
        matrix4fStack.popMatrix();
    }

    public void renderCelestialBodies(MatrixStack matrices, float sunAngle, float moonAngle, float starAngle, MoonPhase moonPhase, float alpha, float starBrightness) {
        matrices.push();
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-90.0f));
        matrices.push();
        matrices.multiply(RotationAxis.POSITIVE_X.rotation(sunAngle));
        this.renderSun(alpha, matrices);
        matrices.pop();
        matrices.push();
        matrices.multiply(RotationAxis.POSITIVE_X.rotation(moonAngle));
        this.renderMoon(moonPhase, alpha, matrices);
        matrices.pop();
        if (starBrightness > 0.0f) {
            matrices.push();
            matrices.multiply(RotationAxis.POSITIVE_X.rotation(starAngle));
            this.renderStars(starBrightness, matrices);
            matrices.pop();
        }
        matrices.pop();
    }

    private void renderSun(float alpha, MatrixStack matrices) {
        Matrix4fStack matrix4fStack = RenderSystem.getModelViewStack();
        matrix4fStack.pushMatrix();
        matrix4fStack.mul(matrices.peek().getPositionMatrix());
        matrix4fStack.translate(0.0f, 100.0f, 0.0f);
        matrix4fStack.scale(30.0f, 1.0f, 30.0f);
        GpuBufferSlice gpuBufferSlice = RenderSystem.getDynamicUniforms().write(matrix4fStack, new Vector4f(1.0f, 1.0f, 1.0f, alpha), new Vector3f(), new Matrix4f());
        GpuTextureView gpuTextureView = MinecraftClient.getInstance().getFramebuffer().getColorAttachmentView();
        GpuTextureView gpuTextureView2 = MinecraftClient.getInstance().getFramebuffer().getDepthAttachmentView();
        GpuBuffer gpuBuffer = this.indexBuffer2.getIndexBuffer(6);
        try (RenderPass renderPass = RenderSystem.getDevice().createCommandEncoder().createRenderPass(() -> "Sky sun", gpuTextureView, OptionalInt.empty(), gpuTextureView2, OptionalDouble.empty());){
            renderPass.setPipeline(RenderPipelines.POSITION_TEX_COLOR_CELESTIAL);
            RenderSystem.bindDefaultUniforms(renderPass);
            renderPass.setUniform("DynamicTransforms", gpuBufferSlice);
            renderPass.bindTexture("Sampler0", this.celestialAtlasTexture.getGlTextureView(), this.celestialAtlasTexture.getSampler());
            renderPass.setVertexBuffer(0, this.sunVertexBuffer);
            renderPass.setIndexBuffer(gpuBuffer, this.indexBuffer2.getIndexType());
            renderPass.drawIndexed(0, 0, 6, 1);
        }
        matrix4fStack.popMatrix();
    }

    private void renderMoon(MoonPhase moonPhase, float alpha, MatrixStack matrices) {
        int i = moonPhase.getIndex() * 4;
        Matrix4fStack matrix4fStack = RenderSystem.getModelViewStack();
        matrix4fStack.pushMatrix();
        matrix4fStack.mul(matrices.peek().getPositionMatrix());
        matrix4fStack.translate(0.0f, 100.0f, 0.0f);
        matrix4fStack.scale(20.0f, 1.0f, 20.0f);
        GpuBufferSlice gpuBufferSlice = RenderSystem.getDynamicUniforms().write(matrix4fStack, new Vector4f(1.0f, 1.0f, 1.0f, alpha), new Vector3f(), new Matrix4f());
        GpuTextureView gpuTextureView = MinecraftClient.getInstance().getFramebuffer().getColorAttachmentView();
        GpuTextureView gpuTextureView2 = MinecraftClient.getInstance().getFramebuffer().getDepthAttachmentView();
        GpuBuffer gpuBuffer = this.indexBuffer2.getIndexBuffer(6);
        try (RenderPass renderPass = RenderSystem.getDevice().createCommandEncoder().createRenderPass(() -> "Sky moon", gpuTextureView, OptionalInt.empty(), gpuTextureView2, OptionalDouble.empty());){
            renderPass.setPipeline(RenderPipelines.POSITION_TEX_COLOR_CELESTIAL);
            RenderSystem.bindDefaultUniforms(renderPass);
            renderPass.setUniform("DynamicTransforms", gpuBufferSlice);
            renderPass.bindTexture("Sampler0", this.celestialAtlasTexture.getGlTextureView(), this.celestialAtlasTexture.getSampler());
            renderPass.setVertexBuffer(0, this.moonPhaseVertexBuffer);
            renderPass.setIndexBuffer(gpuBuffer, this.indexBuffer2.getIndexType());
            renderPass.drawIndexed(i, 0, 6, 1);
        }
        matrix4fStack.popMatrix();
    }

    private void renderStars(float brightness, MatrixStack matrices) {
        Matrix4fStack matrix4fStack = RenderSystem.getModelViewStack();
        matrix4fStack.pushMatrix();
        matrix4fStack.mul(matrices.peek().getPositionMatrix());
        RenderPipeline renderPipeline = RenderPipelines.POSITION_STARS;
        GpuTextureView gpuTextureView = MinecraftClient.getInstance().getFramebuffer().getColorAttachmentView();
        GpuTextureView gpuTextureView2 = MinecraftClient.getInstance().getFramebuffer().getDepthAttachmentView();
        GpuBuffer gpuBuffer = this.indexBuffer2.getIndexBuffer(this.starIndexCount);
        GpuBufferSlice gpuBufferSlice = RenderSystem.getDynamicUniforms().write(matrix4fStack, new Vector4f(brightness, brightness, brightness, brightness), new Vector3f(), new Matrix4f());
        try (RenderPass renderPass = RenderSystem.getDevice().createCommandEncoder().createRenderPass(() -> "Stars", gpuTextureView, OptionalInt.empty(), gpuTextureView2, OptionalDouble.empty());){
            renderPass.setPipeline(renderPipeline);
            RenderSystem.bindDefaultUniforms(renderPass);
            renderPass.setUniform("DynamicTransforms", gpuBufferSlice);
            renderPass.setVertexBuffer(0, this.starVertexBuffer);
            renderPass.setIndexBuffer(gpuBuffer, this.indexBuffer2.getIndexType());
            renderPass.drawIndexed(0, 0, this.starIndexCount, 1);
        }
        matrix4fStack.popMatrix();
    }

    public void renderGlowingSky(MatrixStack matrices, float solarAngle, int color) {
        float g = ColorHelper.getAlphaFloat(color);
        if (g <= 0.001f) {
            return;
        }
        matrices.push();
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(90.0f));
        float h = MathHelper.sin(solarAngle) < 0.0f ? 180.0f : 0.0f;
        matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(h + 90.0f));
        Matrix4fStack matrix4fStack = RenderSystem.getModelViewStack();
        matrix4fStack.pushMatrix();
        matrix4fStack.mul(matrices.peek().getPositionMatrix());
        matrix4fStack.scale(1.0f, 1.0f, g);
        GpuBufferSlice gpuBufferSlice = RenderSystem.getDynamicUniforms().write(matrix4fStack, ColorHelper.toRgbaVector(color), new Vector3f(), new Matrix4f());
        GpuTextureView gpuTextureView = MinecraftClient.getInstance().getFramebuffer().getColorAttachmentView();
        GpuTextureView gpuTextureView2 = MinecraftClient.getInstance().getFramebuffer().getDepthAttachmentView();
        try (RenderPass renderPass = RenderSystem.getDevice().createCommandEncoder().createRenderPass(() -> "Sunrise sunset", gpuTextureView, OptionalInt.empty(), gpuTextureView2, OptionalDouble.empty());){
            renderPass.setPipeline(RenderPipelines.POSITION_COLOR_SUNRISE_SUNSET);
            RenderSystem.bindDefaultUniforms(renderPass);
            renderPass.setUniform("DynamicTransforms", gpuBufferSlice);
            renderPass.setVertexBuffer(0, this.sunRiseVertexBuffer);
            renderPass.draw(0, 18);
        }
        matrix4fStack.popMatrix();
        matrices.pop();
    }

    public void renderEndSky() {
        RenderSystem.ShapeIndexBuffer lv = RenderSystem.getSequentialBuffer(VertexFormat.DrawMode.QUADS);
        GpuBuffer gpuBuffer = lv.getIndexBuffer(36);
        GpuTextureView gpuTextureView = MinecraftClient.getInstance().getFramebuffer().getColorAttachmentView();
        GpuTextureView gpuTextureView2 = MinecraftClient.getInstance().getFramebuffer().getDepthAttachmentView();
        GpuBufferSlice gpuBufferSlice = RenderSystem.getDynamicUniforms().write(RenderSystem.getModelViewMatrix(), new Vector4f(1.0f, 1.0f, 1.0f, 1.0f), new Vector3f(), new Matrix4f());
        try (RenderPass renderPass = RenderSystem.getDevice().createCommandEncoder().createRenderPass(() -> "End sky", gpuTextureView, OptionalInt.empty(), gpuTextureView2, OptionalDouble.empty());){
            renderPass.setPipeline(RenderPipelines.POSITION_TEX_COLOR_END_SKY);
            RenderSystem.bindDefaultUniforms(renderPass);
            renderPass.setUniform("DynamicTransforms", gpuBufferSlice);
            renderPass.bindTexture("Sampler0", this.endSkyTexture.getGlTextureView(), this.endSkyTexture.getSampler());
            renderPass.setVertexBuffer(0, this.endSkyVertexBuffer);
            renderPass.setIndexBuffer(gpuBuffer, lv.getIndexType());
            renderPass.drawIndexed(0, 0, 36, 1);
        }
    }

    public void drawEndLightFlash(MatrixStack matrices, float intensity, float pitch, float yaw) {
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180.0f - yaw));
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(-90.0f - pitch));
        Matrix4fStack matrix4fStack = RenderSystem.getModelViewStack();
        matrix4fStack.pushMatrix();
        matrix4fStack.mul(matrices.peek().getPositionMatrix());
        matrix4fStack.translate(0.0f, 100.0f, 0.0f);
        matrix4fStack.scale(60.0f, 1.0f, 60.0f);
        GpuBufferSlice gpuBufferSlice = RenderSystem.getDynamicUniforms().write(matrix4fStack, new Vector4f(intensity, intensity, intensity, intensity), new Vector3f(), new Matrix4f());
        GpuTextureView gpuTextureView = MinecraftClient.getInstance().getFramebuffer().getColorAttachmentView();
        GpuTextureView gpuTextureView2 = MinecraftClient.getInstance().getFramebuffer().getDepthAttachmentView();
        GpuBuffer gpuBuffer = this.indexBuffer2.getIndexBuffer(6);
        try (RenderPass renderPass = RenderSystem.getDevice().createCommandEncoder().createRenderPass(() -> "End flash", gpuTextureView, OptionalInt.empty(), gpuTextureView2, OptionalDouble.empty());){
            renderPass.setPipeline(RenderPipelines.POSITION_TEX_COLOR_CELESTIAL);
            RenderSystem.bindDefaultUniforms(renderPass);
            renderPass.setUniform("DynamicTransforms", gpuBufferSlice);
            renderPass.bindTexture("Sampler0", this.celestialAtlasTexture.getGlTextureView(), this.celestialAtlasTexture.getSampler());
            renderPass.setVertexBuffer(0, this.endFlashVertexBuffer);
            renderPass.setIndexBuffer(gpuBuffer, this.indexBuffer2.getIndexType());
            renderPass.drawIndexed(0, 0, 6, 1);
        }
        matrix4fStack.popMatrix();
    }

    @Override
    public void close() {
        this.sunVertexBuffer.close();
        this.moonPhaseVertexBuffer.close();
        this.starVertexBuffer.close();
        this.topSkyVertexBuffer.close();
        this.bottomSkyVertexBuffer.close();
        this.endSkyVertexBuffer.close();
        this.sunRiseVertexBuffer.close();
        this.endFlashVertexBuffer.close();
    }
}

