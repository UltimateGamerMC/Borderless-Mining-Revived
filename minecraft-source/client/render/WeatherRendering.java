/*
 * External method calls:
 *   Lnet/minecraft/client/render/RenderLayers;weather(Lnet/minecraft/util/Identifier;Z)Lnet/minecraft/client/render/RenderLayer;
 *   Lnet/minecraft/client/render/LightmapTextureManager;pack(II)I
 *   Lnet/minecraft/client/render/VertexConsumer;vertex(FFF)Lnet/minecraft/client/render/VertexConsumer;
 *   Lnet/minecraft/client/render/VertexConsumer;texture(FF)Lnet/minecraft/client/render/VertexConsumer;
 *   Lnet/minecraft/client/render/VertexConsumer;color(I)Lnet/minecraft/client/render/VertexConsumer;
 *   Lnet/minecraft/client/render/VertexConsumer;light(I)Lnet/minecraft/client/render/VertexConsumer;
 *   Lnet/minecraft/client/world/ClientWorld;addParticleClient(Lnet/minecraft/particle/ParticleEffect;DDDDDD)V
 *   Lnet/minecraft/client/world/ClientWorld;playSoundAtBlockCenterClient(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/sound/SoundEvent;Lnet/minecraft/sound/SoundCategory;FFZ)V
 *   Lnet/minecraft/util/Identifier;ofVanilla(Ljava/lang/String;)Lnet/minecraft/util/Identifier;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/client/render/WeatherRendering;createRainPiece(Lnet/minecraft/util/math/random/Random;IIIIIIF)Lnet/minecraft/client/render/WeatherRendering$Piece;
 *   Lnet/minecraft/client/render/WeatherRendering;createSnowPiece(Lnet/minecraft/util/math/random/Random;IIIIIIF)Lnet/minecraft/client/render/WeatherRendering$Piece;
 *   Lnet/minecraft/client/render/WeatherRendering;renderPieces(Lnet/minecraft/client/render/VertexConsumer;Ljava/util/List;Lnet/minecraft/util/math/Vec3d;FIF)V
 */
package net.minecraft.client.render;

import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CampfireBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.render.state.WeatherRenderState;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.fluid.FluidState;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.particle.ParticlesMode;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkSectionPos;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.Heightmap;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

@Environment(value=EnvType.CLIENT)
public class WeatherRendering {
    private static final float field_63581 = 0.225f;
    private static final int field_53148 = 10;
    private static final Identifier RAIN_TEXTURE = Identifier.ofVanilla("textures/environment/rain.png");
    private static final Identifier SNOW_TEXTURE = Identifier.ofVanilla("textures/environment/snow.png");
    private static final int field_53152 = 32;
    private static final int field_53153 = 16;
    private int soundChance;
    private final float[] NORMAL_LINE_DX = new float[1024];
    private final float[] NORMAL_LINE_DZ = new float[1024];

    public WeatherRendering() {
        for (int i = 0; i < 32; ++i) {
            for (int j = 0; j < 32; ++j) {
                float f = j - 16;
                float g = i - 16;
                float h = MathHelper.hypot(f, g);
                this.NORMAL_LINE_DX[i * 32 + j] = -g / h;
                this.NORMAL_LINE_DZ[i * 32 + j] = f / h;
            }
        }
    }

    public void buildPrecipitationPieces(World world, int ticks, float tickProgress, Vec3d cameraPos, WeatherRenderState state) {
        state.intensity = world.getRainGradient(tickProgress);
        if (state.intensity <= 0.0f) {
            return;
        }
        state.radius = MinecraftClient.getInstance().options.getWeatherRadius().getValue();
        int j = MathHelper.floor(cameraPos.x);
        int k = MathHelper.floor(cameraPos.y);
        int l = MathHelper.floor(cameraPos.z);
        BlockPos.Mutable lv = new BlockPos.Mutable();
        Random lv2 = Random.create();
        for (int m = l - state.radius; m <= l + state.radius; ++m) {
            for (int n = j - state.radius; n <= j + state.radius; ++n) {
                Biome.Precipitation lv3;
                int o = world.getTopY(Heightmap.Type.MOTION_BLOCKING, n, m);
                int p = Math.max(k - state.radius, o);
                int q = Math.max(k + state.radius, o);
                if (q - p == 0 || (lv3 = this.getPrecipitationAt(world, lv.set(n, k, m))) == Biome.Precipitation.NONE) continue;
                int r = n * n * 3121 + n * 45238971 ^ m * m * 418711 + m * 13761;
                lv2.setSeed(r);
                int s = Math.max(k, o);
                int t = WorldRenderer.getLightmapCoordinates(world, lv.set(n, s, m));
                if (lv3 == Biome.Precipitation.RAIN) {
                    state.rainPieces.add(this.createRainPiece(lv2, ticks, n, p, q, m, t, tickProgress));
                    continue;
                }
                if (lv3 != Biome.Precipitation.SNOW) continue;
                state.snowPieces.add(this.createSnowPiece(lv2, ticks, n, p, q, m, t, tickProgress));
            }
        }
    }

    public void renderPrecipitation(VertexConsumerProvider vertexConsumers, Vec3d pos, WeatherRenderState state) {
        RenderLayer lv;
        if (!state.rainPieces.isEmpty()) {
            lv = RenderLayers.weather(RAIN_TEXTURE, MinecraftClient.usesImprovedTransparency());
            this.renderPieces(vertexConsumers.getBuffer(lv), state.rainPieces, pos, 1.0f, state.radius, state.intensity);
        }
        if (!state.snowPieces.isEmpty()) {
            lv = RenderLayers.weather(SNOW_TEXTURE, MinecraftClient.usesImprovedTransparency());
            this.renderPieces(vertexConsumers.getBuffer(lv), state.snowPieces, pos, 0.8f, state.radius, state.intensity);
        }
    }

    private Piece createRainPiece(Random random, int ticks, int x, int yMin, int yMax, int z, int light, float tickProgress) {
        int o = ticks & 0x1FFFF;
        int p = x * x * 3121 + x * 45238971 + z * z * 418711 + z * 13761 & 0xFF;
        float g = 3.0f + random.nextFloat();
        float h = -((float)(o + p) + tickProgress) / 32.0f * g;
        float q = h % 32.0f;
        return new Piece(x, z, yMin, yMax, 0.0f, q, light);
    }

    private Piece createSnowPiece(Random random, int ticks, int x, int yMin, int yMax, int z, int light, float tickProgress) {
        float g = (float)ticks + tickProgress;
        float h = (float)(random.nextDouble() + (double)(g * 0.01f * (float)random.nextGaussian()));
        float o = (float)(random.nextDouble() + (double)(g * (float)random.nextGaussian() * 0.001f));
        float p = -((float)(ticks & 0x1FF) + tickProgress) / 512.0f;
        int q = LightmapTextureManager.pack((LightmapTextureManager.getBlockLightCoordinates(light) * 3 + 15) / 4, (LightmapTextureManager.getSkyLightCoordinates(light) * 3 + 15) / 4);
        return new Piece(x, z, yMin, yMax, h, p + o, q);
    }

    private void renderPieces(VertexConsumer vertexConsumer, List<Piece> pieces, Vec3d pos, float intensity, int range, float gradient) {
        float h = range * range;
        for (Piece lv : pieces) {
            float j = (float)((double)lv.x + 0.5 - pos.x);
            float k = (float)((double)lv.z + 0.5 - pos.z);
            float l = (float)MathHelper.squaredHypot(j, k);
            float m = MathHelper.lerp(Math.min(l / h, 1.0f), intensity, 0.5f) * gradient;
            int n = ColorHelper.getWhite(m);
            int o = (lv.z - MathHelper.floor(pos.z) + 16) * 32 + lv.x - MathHelper.floor(pos.x) + 16;
            float p = this.NORMAL_LINE_DX[o] / 2.0f;
            float q = this.NORMAL_LINE_DZ[o] / 2.0f;
            float r = j - p;
            float s = j + p;
            float t = (float)((double)lv.topY - pos.y);
            float u = (float)((double)lv.bottomY - pos.y);
            float v = k - q;
            float w = k + q;
            float x = lv.uOffset + 0.0f;
            float y = lv.uOffset + 1.0f;
            float z = (float)lv.bottomY * 0.25f + lv.vOffset;
            float aa = (float)lv.topY * 0.25f + lv.vOffset;
            vertexConsumer.vertex(r, t, v).texture(x, z).color(n).light(lv.lightCoords);
            vertexConsumer.vertex(s, t, w).texture(y, z).color(n).light(lv.lightCoords);
            vertexConsumer.vertex(s, u, w).texture(y, aa).color(n).light(lv.lightCoords);
            vertexConsumer.vertex(r, u, v).texture(x, aa).color(n).light(lv.lightCoords);
        }
    }

    public void addParticlesAndSound(ClientWorld world, Camera camera, int ticks, ParticlesMode particlesMode, int weatherRadius) {
        float f = world.getRainGradient(1.0f);
        if (f <= 0.0f) {
            return;
        }
        Random lv = Random.create((long)ticks * 312987231L);
        BlockPos lv2 = BlockPos.ofFloored(camera.getCameraPos());
        Vec3i lv3 = null;
        int k = 2 * weatherRadius + 1;
        int l = k * k;
        int m = (int)(0.225f * (float)l * f * f) / (particlesMode == ParticlesMode.DECREASED ? 2 : 1);
        for (int n = 0; n < m; ++n) {
            int p;
            int o = lv.nextInt(k) - weatherRadius;
            BlockPos lv4 = world.getTopPosition(Heightmap.Type.MOTION_BLOCKING, lv2.add(o, 0, p = lv.nextInt(k) - weatherRadius));
            if (lv4.getY() <= world.getBottomY() || lv4.getY() > lv2.getY() + 10 || lv4.getY() < lv2.getY() - 10 || this.getPrecipitationAt(world, lv4) != Biome.Precipitation.RAIN) continue;
            lv3 = lv4.down();
            if (particlesMode == ParticlesMode.MINIMAL) break;
            double d = lv.nextDouble();
            double e = lv.nextDouble();
            BlockState lv5 = world.getBlockState((BlockPos)lv3);
            FluidState lv6 = world.getFluidState((BlockPos)lv3);
            VoxelShape lv7 = lv5.getCollisionShape(world, (BlockPos)lv3);
            double g = lv7.getEndingCoord(Direction.Axis.Y, d, e);
            double h = lv6.getHeight(world, (BlockPos)lv3);
            double q = Math.max(g, h);
            SimpleParticleType lv8 = lv6.isIn(FluidTags.LAVA) || lv5.isOf(Blocks.MAGMA_BLOCK) || CampfireBlock.isLitCampfire(lv5) ? ParticleTypes.SMOKE : ParticleTypes.RAIN;
            world.addParticleClient(lv8, (double)lv3.getX() + d, (double)lv3.getY() + q, (double)lv3.getZ() + e, 0.0, 0.0, 0.0);
        }
        if (lv3 != null && lv.nextInt(3) < this.soundChance++) {
            this.soundChance = 0;
            if (lv3.getY() > lv2.getY() + 1 && world.getTopPosition(Heightmap.Type.MOTION_BLOCKING, lv2).getY() > MathHelper.floor(lv2.getY())) {
                world.playSoundAtBlockCenterClient((BlockPos)lv3, SoundEvents.WEATHER_RAIN_ABOVE, SoundCategory.WEATHER, 0.1f, 0.5f, false);
            } else {
                world.playSoundAtBlockCenterClient((BlockPos)lv3, SoundEvents.WEATHER_RAIN, SoundCategory.WEATHER, 0.2f, 1.0f, false);
            }
        }
    }

    private Biome.Precipitation getPrecipitationAt(World world, BlockPos pos) {
        if (!world.getChunkManager().isChunkLoaded(ChunkSectionPos.getSectionCoord(pos.getX()), ChunkSectionPos.getSectionCoord(pos.getZ()))) {
            return Biome.Precipitation.NONE;
        }
        Biome lv = world.getBiome(pos).value();
        return lv.getPrecipitation(pos, world.getSeaLevel());
    }

    @Environment(value=EnvType.CLIENT)
    public record Piece(int x, int z, int bottomY, int topY, float uOffset, float vOffset, int lightCoords) {
    }
}

