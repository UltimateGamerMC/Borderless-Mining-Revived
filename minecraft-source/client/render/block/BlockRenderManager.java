/*
 * External method calls:
 *   Lnet/minecraft/client/render/model/BlockStateModel;addParts(Lnet/minecraft/util/math/random/Random;Ljava/util/List;)V
 *   Lnet/minecraft/client/render/block/BlockModelRenderer;render(Lnet/minecraft/world/BlockRenderView;Ljava/util/List;Lnet/minecraft/block/BlockState;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumer;ZI)V
 *   Lnet/minecraft/util/crash/CrashReport;create(Ljava/lang/Throwable;Ljava/lang/String;)Lnet/minecraft/util/crash/CrashReport;
 *   Lnet/minecraft/util/crash/CrashReport;addElement(Ljava/lang/String;)Lnet/minecraft/util/crash/CrashReportSection;
 *   Lnet/minecraft/util/crash/CrashReportSection;addBlockInfo(Lnet/minecraft/util/crash/CrashReportSection;Lnet/minecraft/world/HeightLimitView;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;)V
 *   Lnet/minecraft/client/render/block/FluidRenderer;render(Lnet/minecraft/world/BlockRenderView;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/client/render/VertexConsumer;Lnet/minecraft/block/BlockState;Lnet/minecraft/fluid/FluidState;)V
 *   Lnet/minecraft/client/render/block/BlockModelRenderer;render(Lnet/minecraft/client/util/math/MatrixStack$Entry;Lnet/minecraft/client/render/VertexConsumer;Lnet/minecraft/client/render/model/BlockStateModel;FFFII)V
 */
package net.minecraft.client.render.block;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.render.BlockRenderLayers;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.BlockModelRenderer;
import net.minecraft.client.render.block.BlockModels;
import net.minecraft.client.render.block.FluidRenderer;
import net.minecraft.client.render.model.BlockModelPart;
import net.minecraft.client.render.model.BlockStateModel;
import net.minecraft.client.texture.SpriteHolder;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.fluid.FluidState;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.SynchronousResourceReloader;
import net.minecraft.util.crash.CrashException;
import net.minecraft.util.crash.CrashReport;
import net.minecraft.util.crash.CrashReportSection;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockRenderView;
import org.jspecify.annotations.Nullable;

@Environment(value=EnvType.CLIENT)
public class BlockRenderManager
implements SynchronousResourceReloader {
    private final BlockModels models;
    private final SpriteHolder spriteHolder;
    private final BlockModelRenderer blockModelRenderer;
    private @Nullable FluidRenderer fluidRenderer;
    private final Random random = Random.create();
    private final List<BlockModelPart> parts = new ArrayList<BlockModelPart>();
    private final BlockColors blockColors;

    public BlockRenderManager(BlockModels models, SpriteHolder spriteHolder, BlockColors arg3) {
        this.models = models;
        this.spriteHolder = spriteHolder;
        this.blockColors = arg3;
        this.blockModelRenderer = new BlockModelRenderer(this.blockColors);
    }

    public BlockModels getModels() {
        return this.models;
    }

    public void renderDamage(BlockState state, BlockPos pos, BlockRenderView world, MatrixStack matrices, VertexConsumer vertexConsumer) {
        if (state.getRenderType() != BlockRenderType.MODEL) {
            return;
        }
        BlockStateModel lv = this.models.getModel(state);
        this.random.setSeed(state.getRenderingSeed(pos));
        this.parts.clear();
        lv.addParts(this.random, this.parts);
        this.blockModelRenderer.render(world, this.parts, state, pos, matrices, vertexConsumer, true, OverlayTexture.DEFAULT_UV);
    }

    public void renderBlock(BlockState state, BlockPos pos, BlockRenderView world, MatrixStack matrices, VertexConsumer vertexConsumer, boolean cull, List<BlockModelPart> parts) {
        try {
            this.blockModelRenderer.render(world, parts, state, pos, matrices, vertexConsumer, cull, OverlayTexture.DEFAULT_UV);
        } catch (Throwable throwable) {
            CrashReport lv = CrashReport.create(throwable, "Tesselating block in world");
            CrashReportSection lv2 = lv.addElement("Block being tesselated");
            CrashReportSection.addBlockInfo(lv2, world, pos, state);
            throw new CrashException(lv);
        }
    }

    public void renderFluid(BlockPos pos, BlockRenderView world, VertexConsumer vertexConsumer, BlockState blockState, FluidState fluidState) {
        try {
            Objects.requireNonNull(this.fluidRenderer).render(world, pos, vertexConsumer, blockState, fluidState);
        } catch (Throwable throwable) {
            CrashReport lv = CrashReport.create(throwable, "Tesselating liquid in world");
            CrashReportSection lv2 = lv.addElement("Block being tesselated");
            CrashReportSection.addBlockInfo(lv2, world, pos, blockState);
            throw new CrashException(lv);
        }
    }

    public BlockModelRenderer getModelRenderer() {
        return this.blockModelRenderer;
    }

    public BlockStateModel getModel(BlockState state) {
        return this.models.getModel(state);
    }

    public void renderBlockAsEntity(BlockState state, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        BlockRenderType lv = state.getRenderType();
        if (lv == BlockRenderType.INVISIBLE) {
            return;
        }
        BlockStateModel lv2 = this.getModel(state);
        int k = this.blockColors.getColor(state, null, null, 0);
        float f = (float)(k >> 16 & 0xFF) / 255.0f;
        float g = (float)(k >> 8 & 0xFF) / 255.0f;
        float h = (float)(k & 0xFF) / 255.0f;
        BlockModelRenderer.render(matrices.peek(), vertexConsumers.getBuffer(BlockRenderLayers.getEntityBlockLayer(state)), lv2, f, g, h, light, overlay);
    }

    @Override
    public void reload(ResourceManager manager) {
        this.fluidRenderer = new FluidRenderer(this.spriteHolder);
    }
}

