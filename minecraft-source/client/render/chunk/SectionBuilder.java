/*
 * External method calls:
 *   Lnet/minecraft/client/render/chunk/ChunkOcclusionDataBuilder;markClosed(Lnet/minecraft/util/math/BlockPos;)V
 *   Lnet/minecraft/client/render/block/BlockRenderManager;renderFluid(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/world/BlockRenderView;Lnet/minecraft/client/render/VertexConsumer;Lnet/minecraft/block/BlockState;Lnet/minecraft/fluid/FluidState;)V
 *   Lnet/minecraft/client/render/model/BlockStateModel;addParts(Lnet/minecraft/util/math/random/Random;Ljava/util/List;)V
 *   Lnet/minecraft/client/render/block/BlockRenderManager;renderBlock(Lnet/minecraft/block/BlockState;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/world/BlockRenderView;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumer;ZLjava/util/List;)V
 *   Lnet/minecraft/client/render/BufferBuilder;endNullable()Lnet/minecraft/client/render/BuiltBuffer;
 *   Lnet/minecraft/client/render/BuiltBuffer;sortQuads(Lnet/minecraft/client/util/BufferAllocator;Lcom/mojang/blaze3d/systems/VertexSorter;)Lnet/minecraft/client/render/BuiltBuffer$SortState;
 *   Lnet/minecraft/client/render/chunk/ChunkOcclusionDataBuilder;build()Lnet/minecraft/client/render/chunk/ChunkOcclusionData;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/client/render/chunk/SectionBuilder;addBlockEntity(Lnet/minecraft/client/render/chunk/SectionBuilder$RenderData;Lnet/minecraft/block/entity/BlockEntity;)V
 *   Lnet/minecraft/client/render/chunk/SectionBuilder;beginBufferBuilding(Ljava/util/Map;Lnet/minecraft/client/render/chunk/BlockBufferAllocatorStorage;Lnet/minecraft/client/render/BlockRenderLayer;)Lnet/minecraft/client/render/BufferBuilder;
 */
package net.minecraft.client.render.chunk;

import com.mojang.blaze3d.systems.VertexSorter;
import com.mojang.blaze3d.vertex.VertexFormat;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.render.BlockRenderLayer;
import net.minecraft.client.render.BlockRenderLayers;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.BuiltBuffer;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.render.block.BlockModelRenderer;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.block.entity.BlockEntityRenderManager;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.chunk.BlockBufferAllocatorStorage;
import net.minecraft.client.render.chunk.ChunkOcclusionData;
import net.minecraft.client.render.chunk.ChunkOcclusionDataBuilder;
import net.minecraft.client.render.chunk.ChunkRendererRegion;
import net.minecraft.client.render.model.BlockModelPart;
import net.minecraft.client.util.BufferAllocator;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.fluid.FluidState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkSectionPos;
import net.minecraft.util.math.random.Random;
import org.jspecify.annotations.Nullable;

@Environment(value=EnvType.CLIENT)
public class SectionBuilder {
    private final BlockRenderManager blockRenderManager;
    private final BlockEntityRenderManager blockEntityRenderDispatcher;

    public SectionBuilder(BlockRenderManager blockRenderManager, BlockEntityRenderManager blockEntityRenderDispatcher) {
        this.blockRenderManager = blockRenderManager;
        this.blockEntityRenderDispatcher = blockEntityRenderDispatcher;
    }

    public RenderData build(ChunkSectionPos sectionPos, ChunkRendererRegion renderRegion, VertexSorter vertexSorter, BlockBufferAllocatorStorage allocatorStorage) {
        RenderData lv = new RenderData();
        BlockPos lv2 = sectionPos.getMinPos();
        BlockPos lv3 = lv2.add(15, 15, 15);
        ChunkOcclusionDataBuilder lv4 = new ChunkOcclusionDataBuilder();
        MatrixStack lv5 = new MatrixStack();
        BlockModelRenderer.enableBrightnessCache();
        EnumMap<BlockRenderLayer, BufferBuilder> map = new EnumMap<BlockRenderLayer, BufferBuilder>(BlockRenderLayer.class);
        Random lv6 = Random.create();
        ObjectArrayList<BlockModelPart> list = new ObjectArrayList<BlockModelPart>();
        for (BlockPos blockPos : BlockPos.iterate(lv2, lv3)) {
            BufferBuilder lv12;
            BlockRenderLayer lv11;
            FluidState lv10;
            BlockEntity lv9;
            BlockState lv8 = renderRegion.getBlockState(blockPos);
            if (lv8.isOpaqueFullCube()) {
                lv4.markClosed(blockPos);
            }
            if (lv8.hasBlockEntity() && (lv9 = renderRegion.getBlockEntity(blockPos)) != null) {
                this.addBlockEntity(lv, lv9);
            }
            if (!(lv10 = lv8.getFluidState()).isEmpty()) {
                lv11 = BlockRenderLayers.getFluidLayer(lv10);
                lv12 = this.beginBufferBuilding(map, allocatorStorage, lv11);
                this.blockRenderManager.renderFluid(blockPos, renderRegion, lv12, lv8, lv10);
            }
            if (lv8.getRenderType() != BlockRenderType.MODEL) continue;
            lv11 = BlockRenderLayers.getBlockLayer(lv8);
            lv12 = this.beginBufferBuilding(map, allocatorStorage, lv11);
            lv6.setSeed(lv8.getRenderingSeed(blockPos));
            this.blockRenderManager.getModel(lv8).addParts(lv6, list);
            lv5.push();
            lv5.translate(ChunkSectionPos.getLocalCoord(blockPos.getX()), ChunkSectionPos.getLocalCoord(blockPos.getY()), ChunkSectionPos.getLocalCoord(blockPos.getZ()));
            this.blockRenderManager.renderBlock(lv8, blockPos, renderRegion, lv5, lv12, true, list);
            lv5.pop();
            list.clear();
        }
        for (Map.Entry entry : map.entrySet()) {
            BlockRenderLayer lv13 = (BlockRenderLayer)((Object)entry.getKey());
            BuiltBuffer lv14 = ((BufferBuilder)entry.getValue()).endNullable();
            if (lv14 == null) continue;
            if (lv13 == BlockRenderLayer.TRANSLUCENT) {
                lv.translucencySortingData = lv14.sortQuads(allocatorStorage.get(lv13), vertexSorter);
            }
            lv.buffers.put(lv13, lv14);
        }
        BlockModelRenderer.disableBrightnessCache();
        lv.chunkOcclusionData = lv4.build();
        return lv;
    }

    private BufferBuilder beginBufferBuilding(Map<BlockRenderLayer, BufferBuilder> builders, BlockBufferAllocatorStorage allocatorStorage, BlockRenderLayer layer) {
        BufferBuilder lv = builders.get((Object)layer);
        if (lv == null) {
            BufferAllocator lv2 = allocatorStorage.get(layer);
            lv = new BufferBuilder(lv2, VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR_TEXTURE_LIGHT_NORMAL);
            builders.put(layer, lv);
        }
        return lv;
    }

    private <E extends BlockEntity> void addBlockEntity(RenderData data, E blockEntity) {
        BlockEntityRenderer lv = this.blockEntityRenderDispatcher.get(blockEntity);
        if (lv != null && !lv.rendersOutsideBoundingBox()) {
            data.blockEntities.add(blockEntity);
        }
    }

    @Environment(value=EnvType.CLIENT)
    public static final class RenderData {
        public final List<BlockEntity> blockEntities = new ArrayList<BlockEntity>();
        public final Map<BlockRenderLayer, BuiltBuffer> buffers = new EnumMap<BlockRenderLayer, BuiltBuffer>(BlockRenderLayer.class);
        public ChunkOcclusionData chunkOcclusionData = new ChunkOcclusionData();
        public @Nullable BuiltBuffer.SortState translucencySortingData;

        public void close() {
            this.buffers.values().forEach(BuiltBuffer::close);
        }
    }
}

