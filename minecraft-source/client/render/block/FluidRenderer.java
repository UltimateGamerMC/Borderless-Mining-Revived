/*
 * External method calls:
 *   Lnet/minecraft/fluid/Fluid;matchesType(Lnet/minecraft/fluid/Fluid;)Z
 *   Lnet/minecraft/util/shape/VoxelShapes;empty()Lnet/minecraft/util/shape/VoxelShape;
 *   Lnet/minecraft/util/shape/VoxelShapes;fullCube()Lnet/minecraft/util/shape/VoxelShape;
 *   Lnet/minecraft/util/shape/VoxelShapes;cuboid(DDDDDD)Lnet/minecraft/util/shape/VoxelShape;
 *   Lnet/minecraft/client/render/VertexConsumer;vertex(FFF)Lnet/minecraft/client/render/VertexConsumer;
 *   Lnet/minecraft/client/render/VertexConsumer;color(FFFF)Lnet/minecraft/client/render/VertexConsumer;
 *   Lnet/minecraft/client/render/VertexConsumer;texture(FF)Lnet/minecraft/client/render/VertexConsumer;
 *   Lnet/minecraft/client/render/VertexConsumer;light(I)Lnet/minecraft/client/render/VertexConsumer;
 *   Lnet/minecraft/client/render/VertexConsumer;normal(FFF)Lnet/minecraft/client/render/VertexConsumer;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/client/render/block/FluidRenderer;calculateFluidHeight(Lnet/minecraft/world/BlockRenderView;Lnet/minecraft/fluid/Fluid;FFFLnet/minecraft/util/math/BlockPos;)F
 *   Lnet/minecraft/client/render/block/FluidRenderer;vertex(Lnet/minecraft/client/render/VertexConsumer;FFFFFFFFI)V
 *   Lnet/minecraft/client/render/block/FluidRenderer;addHeight([FF)V
 */
package net.minecraft.client.render.block;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.LeavesBlock;
import net.minecraft.block.TranslucentBlock;
import net.minecraft.client.color.world.BiomeColors;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.render.model.ModelBaker;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.SpriteHolder;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockRenderView;

@Environment(value=EnvType.CLIENT)
public class FluidRenderer {
    private static final float FLUID_HEIGHT = 0.8888889f;
    private final Sprite field_64568;
    private final Sprite field_64569;
    private final Sprite field_64570;
    private final Sprite field_64571;
    private final Sprite waterOverlaySprite;

    public FluidRenderer(SpriteHolder arg) {
        this.field_64568 = arg.getSprite(ModelBaker.LAVA_STILL);
        this.field_64569 = arg.getSprite(ModelBaker.LAVA_FLOW);
        this.field_64570 = arg.getSprite(ModelBaker.WATER_STILL);
        this.field_64571 = arg.getSprite(ModelBaker.WATER_FLOW);
        this.waterOverlaySprite = arg.getSprite(ModelBaker.WATER_OVERLAY);
    }

    private static boolean isSameFluid(FluidState a, FluidState b) {
        return b.getFluid().matchesType(a.getFluid());
    }

    private static boolean isSideCovered(Direction side, float height, BlockState state) {
        VoxelShape lv = state.getCullingFace(side.getOpposite());
        if (lv == VoxelShapes.empty()) {
            return false;
        }
        if (lv == VoxelShapes.fullCube()) {
            boolean bl = height == 1.0f;
            return side != Direction.UP || bl;
        }
        VoxelShape lv2 = VoxelShapes.cuboid(0.0, 0.0, 0.0, 1.0, height, 1.0);
        return VoxelShapes.isSideCovered(lv2, lv, side);
    }

    private static boolean shouldSkipRendering(Direction side, float height, BlockState state) {
        return FluidRenderer.isSideCovered(side, height, state);
    }

    private static boolean isOppositeSideCovered(BlockState state, Direction side) {
        return FluidRenderer.isSideCovered(side.getOpposite(), 1.0f, state);
    }

    public static boolean shouldRenderSide(FluidState fluid, BlockState state, Direction side, FluidState fluidFromSide) {
        return !FluidRenderer.isOppositeSideCovered(state, side) && !FluidRenderer.isSameFluid(fluid, fluidFromSide);
    }

    public void render(BlockRenderView world, BlockPos pos, VertexConsumer vertexConsumer, BlockState blockState, FluidState fluidState) {
        float ai;
        float ae;
        float ad;
        float ac;
        float ab;
        float aa;
        float z;
        float x;
        float w;
        float v;
        float u;
        float t;
        float s;
        float r;
        float q;
        float p;
        float o;
        boolean bl = fluidState.isIn(FluidTags.LAVA);
        Sprite lv = bl ? this.field_64568 : this.field_64570;
        Sprite lv2 = bl ? this.field_64569 : this.field_64571;
        int i = bl ? 0xFFFFFF : BiomeColors.getWaterColor(world, pos);
        float f = (float)(i >> 16 & 0xFF) / 255.0f;
        float g = (float)(i >> 8 & 0xFF) / 255.0f;
        float h = (float)(i & 0xFF) / 255.0f;
        BlockState lv3 = world.getBlockState(pos.offset(Direction.DOWN));
        FluidState lv4 = lv3.getFluidState();
        BlockState lv5 = world.getBlockState(pos.offset(Direction.UP));
        FluidState lv6 = lv5.getFluidState();
        BlockState lv7 = world.getBlockState(pos.offset(Direction.NORTH));
        FluidState lv8 = lv7.getFluidState();
        BlockState lv9 = world.getBlockState(pos.offset(Direction.SOUTH));
        FluidState lv10 = lv9.getFluidState();
        BlockState lv11 = world.getBlockState(pos.offset(Direction.WEST));
        FluidState lv12 = lv11.getFluidState();
        BlockState lv13 = world.getBlockState(pos.offset(Direction.EAST));
        FluidState lv14 = lv13.getFluidState();
        boolean bl2 = !FluidRenderer.isSameFluid(fluidState, lv6);
        boolean bl3 = FluidRenderer.shouldRenderSide(fluidState, blockState, Direction.DOWN, lv4) && !FluidRenderer.shouldSkipRendering(Direction.DOWN, 0.8888889f, lv3);
        boolean bl4 = FluidRenderer.shouldRenderSide(fluidState, blockState, Direction.NORTH, lv8);
        boolean bl5 = FluidRenderer.shouldRenderSide(fluidState, blockState, Direction.SOUTH, lv10);
        boolean bl6 = FluidRenderer.shouldRenderSide(fluidState, blockState, Direction.WEST, lv12);
        boolean bl7 = FluidRenderer.shouldRenderSide(fluidState, blockState, Direction.EAST, lv14);
        if (!(bl2 || bl3 || bl7 || bl6 || bl4 || bl5)) {
            return;
        }
        float j = world.getBrightness(Direction.DOWN, true);
        float k = world.getBrightness(Direction.UP, true);
        float l = world.getBrightness(Direction.NORTH, true);
        float m = world.getBrightness(Direction.WEST, true);
        Fluid lv15 = fluidState.getFluid();
        float n = this.getFluidHeight(world, lv15, pos, blockState, fluidState);
        if (n >= 1.0f) {
            o = 1.0f;
            p = 1.0f;
            q = 1.0f;
            r = 1.0f;
        } else {
            s = this.getFluidHeight(world, lv15, pos.north(), lv7, lv8);
            t = this.getFluidHeight(world, lv15, pos.south(), lv9, lv10);
            u = this.getFluidHeight(world, lv15, pos.east(), lv13, lv14);
            v = this.getFluidHeight(world, lv15, pos.west(), lv11, lv12);
            o = this.calculateFluidHeight(world, lv15, n, s, u, pos.offset(Direction.NORTH).offset(Direction.EAST));
            p = this.calculateFluidHeight(world, lv15, n, s, v, pos.offset(Direction.NORTH).offset(Direction.WEST));
            q = this.calculateFluidHeight(world, lv15, n, t, u, pos.offset(Direction.SOUTH).offset(Direction.EAST));
            r = this.calculateFluidHeight(world, lv15, n, t, v, pos.offset(Direction.SOUTH).offset(Direction.WEST));
        }
        s = pos.getX() & 0xF;
        t = pos.getY() & 0xF;
        u = pos.getZ() & 0xF;
        v = 0.001f;
        float f2 = w = bl3 ? 0.001f : 0.0f;
        if (bl2 && !FluidRenderer.shouldSkipRendering(Direction.UP, Math.min(Math.min(p, r), Math.min(q, o)), lv5)) {
            float ah;
            float ag;
            float y;
            p -= 0.001f;
            r -= 0.001f;
            q -= 0.001f;
            o -= 0.001f;
            Vec3d lv16 = fluidState.getVelocity(world, pos);
            if (lv16.x == 0.0 && lv16.z == 0.0) {
                x = lv.getFrameU(0.0f);
                y = lv.getFrameV(0.0f);
                z = x;
                aa = lv.getFrameV(1.0f);
                ab = lv.getFrameU(1.0f);
                ac = aa;
                ad = ab;
                ae = y;
            } else {
                float af = (float)MathHelper.atan2(lv16.z, lv16.x) - 1.5707964f;
                ag = MathHelper.sin(af) * 0.25f;
                ah = MathHelper.cos(af) * 0.25f;
                ai = 0.5f;
                x = lv2.getFrameU(0.5f + (-ah - ag));
                y = lv2.getFrameV(0.5f + (-ah + ag));
                z = lv2.getFrameU(0.5f + (-ah + ag));
                aa = lv2.getFrameV(0.5f + (ah + ag));
                ab = lv2.getFrameU(0.5f + (ah + ag));
                ac = lv2.getFrameV(0.5f + (ah - ag));
                ad = lv2.getFrameU(0.5f + (ah - ag));
                ae = lv2.getFrameV(0.5f + (-ah - ag));
            }
            int aj = this.getLight(world, pos);
            ag = k * f;
            ah = k * g;
            ai = k * h;
            this.vertex(vertexConsumer, s + 0.0f, t + p, u + 0.0f, ag, ah, ai, x, y, aj);
            this.vertex(vertexConsumer, s + 0.0f, t + r, u + 1.0f, ag, ah, ai, z, aa, aj);
            this.vertex(vertexConsumer, s + 1.0f, t + q, u + 1.0f, ag, ah, ai, ab, ac, aj);
            this.vertex(vertexConsumer, s + 1.0f, t + o, u + 0.0f, ag, ah, ai, ad, ae, aj);
            if (fluidState.canFlowTo(world, pos.up())) {
                this.vertex(vertexConsumer, s + 0.0f, t + p, u + 0.0f, ag, ah, ai, x, y, aj);
                this.vertex(vertexConsumer, s + 1.0f, t + o, u + 0.0f, ag, ah, ai, ad, ae, aj);
                this.vertex(vertexConsumer, s + 1.0f, t + q, u + 1.0f, ag, ah, ai, ab, ac, aj);
                this.vertex(vertexConsumer, s + 0.0f, t + r, u + 1.0f, ag, ah, ai, z, aa, aj);
            }
        }
        if (bl3) {
            x = lv.getMinU();
            z = lv.getMaxU();
            ab = lv.getMinV();
            ad = lv.getMaxV();
            int ak = this.getLight(world, pos.down());
            aa = j * f;
            ac = j * g;
            ae = j * h;
            this.vertex(vertexConsumer, s, t + w, u + 1.0f, aa, ac, ae, x, ad, ak);
            this.vertex(vertexConsumer, s, t + w, u, aa, ac, ae, x, ab, ak);
            this.vertex(vertexConsumer, s + 1.0f, t + w, u, aa, ac, ae, z, ab, ak);
            this.vertex(vertexConsumer, s + 1.0f, t + w, u + 1.0f, aa, ac, ae, z, ad, ak);
        }
        int al = this.getLight(world, pos);
        for (Direction lv17 : Direction.Type.HORIZONTAL) {
            Block lv20;
            float am;
            float y;
            if (!(switch (lv17) {
                case Direction.NORTH -> {
                    ad = p;
                    y = o;
                    aa = s;
                    ae = s + 1.0f;
                    ac = u + 0.001f;
                    am = u + 0.001f;
                    yield bl4;
                }
                case Direction.SOUTH -> {
                    ad = q;
                    y = r;
                    aa = s + 1.0f;
                    ae = s;
                    ac = u + 1.0f - 0.001f;
                    am = u + 1.0f - 0.001f;
                    yield bl5;
                }
                case Direction.WEST -> {
                    ad = r;
                    y = p;
                    aa = s + 0.001f;
                    ae = s + 0.001f;
                    ac = u + 1.0f;
                    am = u;
                    yield bl6;
                }
                default -> {
                    ad = o;
                    y = q;
                    aa = s + 1.0f - 0.001f;
                    ae = s + 1.0f - 0.001f;
                    ac = u;
                    am = u + 1.0f;
                    yield bl7;
                }
            }) || FluidRenderer.shouldSkipRendering(lv17, Math.max(ad, y), world.getBlockState(pos.offset(lv17)))) continue;
            BlockPos lv18 = pos.offset(lv17);
            Sprite lv19 = lv2;
            if (!bl && ((lv20 = world.getBlockState(lv18).getBlock()) instanceof TranslucentBlock || lv20 instanceof LeavesBlock)) {
                lv19 = this.waterOverlaySprite;
            }
            ai = lv19.getFrameU(0.0f);
            float an = lv19.getFrameU(0.5f);
            float ao = lv19.getFrameV((1.0f - ad) * 0.5f);
            float ap = lv19.getFrameV((1.0f - y) * 0.5f);
            float aq = lv19.getFrameV(0.5f);
            float ar = lv17.getAxis() == Direction.Axis.Z ? l : m;
            float as = k * ar * f;
            float at = k * ar * g;
            float au = k * ar * h;
            this.vertex(vertexConsumer, aa, t + ad, ac, as, at, au, ai, ao, al);
            this.vertex(vertexConsumer, ae, t + y, am, as, at, au, an, ap, al);
            this.vertex(vertexConsumer, ae, t + w, am, as, at, au, an, aq, al);
            this.vertex(vertexConsumer, aa, t + w, ac, as, at, au, ai, aq, al);
            if (lv19 == this.waterOverlaySprite) continue;
            this.vertex(vertexConsumer, aa, t + w, ac, as, at, au, ai, aq, al);
            this.vertex(vertexConsumer, ae, t + w, am, as, at, au, an, aq, al);
            this.vertex(vertexConsumer, ae, t + y, am, as, at, au, an, ap, al);
            this.vertex(vertexConsumer, aa, t + ad, ac, as, at, au, ai, ao, al);
        }
    }

    private float calculateFluidHeight(BlockRenderView world, Fluid fluid, float originHeight, float northSouthHeight, float eastWestHeight, BlockPos pos) {
        if (eastWestHeight >= 1.0f || northSouthHeight >= 1.0f) {
            return 1.0f;
        }
        float[] fs = new float[2];
        if (eastWestHeight > 0.0f || northSouthHeight > 0.0f) {
            float i = this.getFluidHeight(world, fluid, pos);
            if (i >= 1.0f) {
                return 1.0f;
            }
            this.addHeight(fs, i);
        }
        this.addHeight(fs, originHeight);
        this.addHeight(fs, eastWestHeight);
        this.addHeight(fs, northSouthHeight);
        return fs[0] / fs[1];
    }

    private void addHeight(float[] weightedAverageHeight, float height) {
        if (height >= 0.8f) {
            weightedAverageHeight[0] = weightedAverageHeight[0] + height * 10.0f;
            weightedAverageHeight[1] = weightedAverageHeight[1] + 10.0f;
        } else if (height >= 0.0f) {
            weightedAverageHeight[0] = weightedAverageHeight[0] + height;
            weightedAverageHeight[1] = weightedAverageHeight[1] + 1.0f;
        }
    }

    private float getFluidHeight(BlockRenderView world, Fluid fluid, BlockPos pos) {
        BlockState lv = world.getBlockState(pos);
        return this.getFluidHeight(world, fluid, pos, lv, lv.getFluidState());
    }

    private float getFluidHeight(BlockRenderView world, Fluid fluid, BlockPos pos, BlockState blockState, FluidState fluidState) {
        if (fluid.matchesType(fluidState.getFluid())) {
            BlockState lv = world.getBlockState(pos.up());
            if (fluid.matchesType(lv.getFluidState().getFluid())) {
                return 1.0f;
            }
            return fluidState.getHeight();
        }
        if (!blockState.isSolid()) {
            return 0.0f;
        }
        return -1.0f;
    }

    private void vertex(VertexConsumer vertexConsumer, float x, float y, float z, float red, float green, float blue, float u, float v, int light) {
        vertexConsumer.vertex(x, y, z).color(red, green, blue, 1.0f).texture(u, v).light(light).normal(0.0f, 1.0f, 0.0f);
    }

    private int getLight(BlockRenderView world, BlockPos pos) {
        int i = WorldRenderer.getLightmapCoordinates(world, pos);
        int j = WorldRenderer.getLightmapCoordinates(world, pos.up());
        int k = i & 0xFF;
        int l = j & 0xFF;
        int m = i >> 16 & 0xFF;
        int n = j >> 16 & 0xFF;
        return (k > l ? k : l) | (m > n ? m : n) << 16;
    }
}

