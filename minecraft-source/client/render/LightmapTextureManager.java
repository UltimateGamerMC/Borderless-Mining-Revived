/*
 * External method calls:
 *   Lnet/minecraft/util/profiler/Profiler;push(Ljava/lang/String;)V
 *
 * Internal private/static methods:
 *   Lnet/minecraft/client/render/LightmapTextureManager;pack(II)I
 */
package net.minecraft.client.render;

import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.buffers.Std140Builder;
import com.mojang.blaze3d.buffers.Std140SizeCalculator;
import com.mojang.blaze3d.systems.CommandEncoder;
import com.mojang.blaze3d.systems.GpuDevice;
import com.mojang.blaze3d.systems.RenderPass;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.GpuTexture;
import com.mojang.blaze3d.textures.GpuTextureView;
import com.mojang.blaze3d.textures.TextureFormat;
import java.util.OptionalInt;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.MappableRingBuffer;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.EndLightFlashManager;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.util.profiler.Profilers;
import net.minecraft.world.attribute.EnvironmentAttributes;
import net.minecraft.world.dimension.DimensionType;
import org.joml.Vector3f;

@Environment(value=EnvType.CLIENT)
public class LightmapTextureManager
implements AutoCloseable {
    public static final int MAX_LIGHT_COORDINATE = 0xF000F0;
    public static final int MAX_SKY_LIGHT_COORDINATE = 0xF00000;
    public static final int MAX_BLOCK_LIGHT_COORDINATE = 240;
    private static final int field_53098 = 16;
    private static final int UBO_SIZE = new Std140SizeCalculator().putFloat().putFloat().putFloat().putFloat().putFloat().putFloat().putFloat().putVec3().putVec3().get();
    private final GpuTexture glTexture;
    private final GpuTextureView glTextureView;
    private boolean dirty;
    private float flickerIntensity;
    private final GameRenderer renderer;
    private final MinecraftClient client;
    private final MappableRingBuffer buffer;
    private final Random field_64675 = Random.create();

    public LightmapTextureManager(GameRenderer arg, MinecraftClient client) {
        this.renderer = arg;
        this.client = client;
        GpuDevice gpuDevice = RenderSystem.getDevice();
        this.glTexture = gpuDevice.createTexture("Light Texture", 12, TextureFormat.RGBA8, 16, 16, 1, 1);
        this.glTextureView = gpuDevice.createTextureView(this.glTexture);
        gpuDevice.createCommandEncoder().clearColorTexture(this.glTexture, -1);
        this.buffer = new MappableRingBuffer(() -> "Lightmap UBO", 130, UBO_SIZE);
    }

    public GpuTextureView getGlTextureView() {
        return this.glTextureView;
    }

    @Override
    public void close() {
        this.glTexture.close();
        this.glTextureView.close();
        this.buffer.close();
    }

    public void tick() {
        this.flickerIntensity += (this.field_64675.nextFloat() - this.field_64675.nextFloat()) * this.field_64675.nextFloat() * this.field_64675.nextFloat() * 0.1f;
        this.flickerIntensity *= 0.9f;
        this.dirty = true;
    }

    private float getDarkness(LivingEntity entity, float factor, float tickProgress) {
        float h = 0.45f * factor;
        return Math.max(0.0f, MathHelper.cos(((float)entity.age - tickProgress) * (float)Math.PI * 0.025f) * h);
    }

    public void update(float tickProgress) {
        float j;
        Vector3f vector3f;
        if (!this.dirty) {
            return;
        }
        this.dirty = false;
        Profiler lv = Profilers.get();
        lv.push("lightTex");
        ClientWorld lv2 = this.client.world;
        if (lv2 == null) {
            return;
        }
        Camera lv3 = this.client.gameRenderer.getCamera();
        int i = lv3.getEnvironmentAttributeInterpolator().get(EnvironmentAttributes.SKY_LIGHT_COLOR_VISUAL, tickProgress);
        float g = lv2.getDimension().ambientLight();
        float h = lv3.getEnvironmentAttributeInterpolator().get(EnvironmentAttributes.SKY_LIGHT_FACTOR_VISUAL, tickProgress).floatValue();
        EndLightFlashManager lv4 = lv2.getEndLightFlashManager();
        if (lv4 != null) {
            vector3f = new Vector3f(0.99f, 1.12f, 1.0f);
            if (!this.client.options.getHideLightningFlashes().getValue().booleanValue()) {
                j = lv4.getSkyFactor(tickProgress);
                h = this.client.inGameHud.getBossBarHud().shouldThickenFog() ? (h += j / 3.0f) : (h += j);
            }
        } else {
            vector3f = new Vector3f(1.0f, 1.0f, 1.0f);
        }
        j = this.client.options.getDarknessEffectScale().getValue().floatValue();
        float k = this.client.player.getEffectFadeFactor(StatusEffects.DARKNESS, tickProgress) * j;
        float l = this.getDarkness(this.client.player, k, tickProgress) * j;
        float m = this.client.player.getUnderwaterVisibility();
        float n = this.client.player.hasStatusEffect(StatusEffects.NIGHT_VISION) ? GameRenderer.getNightVisionStrength(this.client.player, tickProgress) : (m > 0.0f && this.client.player.hasStatusEffect(StatusEffects.CONDUIT_POWER) ? m : 0.0f);
        float o = this.flickerIntensity + 1.5f;
        float p = this.client.options.getGamma().getValue().floatValue();
        CommandEncoder commandEncoder = RenderSystem.getDevice().createCommandEncoder();
        try (GpuBuffer.MappedView mappedView = commandEncoder.mapBuffer(this.buffer.getBlocking(), false, true);){
            Std140Builder.intoBuffer(mappedView.data()).putFloat(g).putFloat(h).putFloat(o).putFloat(n).putFloat(l).putFloat(this.renderer.getSkyDarkness(tickProgress)).putFloat(Math.max(0.0f, p - k)).putVec3(ColorHelper.toRgbVector(i)).putVec3(vector3f);
        }
        try (RenderPass renderPass = commandEncoder.createRenderPass(() -> "Update light", this.glTextureView, OptionalInt.empty());){
            renderPass.setPipeline(RenderPipelines.BILT_SCREEN_LIGHTMAP);
            RenderSystem.bindDefaultUniforms(renderPass);
            renderPass.setUniform("LightmapInfo", this.buffer.getBlocking());
            renderPass.draw(0, 3);
        }
        this.buffer.rotate();
        lv.pop();
    }

    public static float getBrightness(DimensionType type, int lightLevel) {
        return LightmapTextureManager.getBrightness(type.ambientLight(), lightLevel);
    }

    public static float getBrightness(float ambientLight, int lightLevel) {
        float g = (float)lightLevel / 15.0f;
        float h = g / (4.0f - 3.0f * g);
        return MathHelper.lerp(ambientLight, h, 1.0f);
    }

    public static int pack(int block, int sky) {
        return block << 4 | sky << 20;
    }

    public static int getBlockLightCoordinates(int light) {
        return light >>> 4 & 0xF;
    }

    public static int getSkyLightCoordinates(int light) {
        return light >>> 20 & 0xF;
    }

    public static int applyEmission(int light, int lightEmission) {
        if (lightEmission == 0) {
            return light;
        }
        int k = Math.max(LightmapTextureManager.getSkyLightCoordinates(light), lightEmission);
        int l = Math.max(LightmapTextureManager.getBlockLightCoordinates(light), lightEmission);
        return LightmapTextureManager.pack(l, k);
    }
}

