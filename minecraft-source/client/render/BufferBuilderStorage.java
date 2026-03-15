/*
 * External method calls:
 *   Lnet/minecraft/client/render/chunk/BlockBufferBuilderPool;allocate(I)Lnet/minecraft/client/render/chunk/BlockBufferBuilderPool;
 *   Lnet/minecraft/util/Util;make(Ljava/lang/Object;Ljava/util/function/Consumer;)Ljava/lang/Object;
 *   Lnet/minecraft/client/render/VertexConsumerProvider;immediate(Ljava/util/SequencedMap;Lnet/minecraft/client/util/BufferAllocator;)Lnet/minecraft/client/render/VertexConsumerProvider$Immediate;
 *   Lnet/minecraft/client/render/RenderLayers;armorEntityGlint()Lnet/minecraft/client/render/RenderLayer;
 *   Lnet/minecraft/client/render/RenderLayers;glint()Lnet/minecraft/client/render/RenderLayer;
 *   Lnet/minecraft/client/render/RenderLayers;glintTranslucent()Lnet/minecraft/client/render/RenderLayer;
 *   Lnet/minecraft/client/render/RenderLayers;entityGlint()Lnet/minecraft/client/render/RenderLayer;
 *   Lnet/minecraft/client/render/RenderLayers;waterMask()Lnet/minecraft/client/render/RenderLayer;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/client/render/BufferBuilderStorage;assignBufferBuilder(Lit/unimi/dsi/fastutil/objects/Object2ObjectLinkedOpenHashMap;Lnet/minecraft/client/render/RenderLayer;)V
 */
package net.minecraft.client.render;

import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import java.util.SequencedMap;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.BlockRenderLayer;
import net.minecraft.client.render.OutlineVertexConsumerProvider;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.client.render.TexturedRenderLayers;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.chunk.BlockBufferAllocatorStorage;
import net.minecraft.client.render.chunk.BlockBufferBuilderPool;
import net.minecraft.client.render.model.ModelBaker;
import net.minecraft.client.util.BufferAllocator;
import net.minecraft.util.Util;

@Environment(value=EnvType.CLIENT)
public class BufferBuilderStorage {
    private final BlockBufferAllocatorStorage blockBufferBuilders = new BlockBufferAllocatorStorage();
    private final BlockBufferBuilderPool blockBufferBuildersPool;
    private final VertexConsumerProvider.Immediate entityVertexConsumers;
    private final VertexConsumerProvider.Immediate effectVertexConsumers;
    private final OutlineVertexConsumerProvider outlineVertexConsumers;

    public BufferBuilderStorage(int maxBlockBuildersPoolSize) {
        this.blockBufferBuildersPool = BlockBufferBuilderPool.allocate(maxBlockBuildersPoolSize);
        SequencedMap sequencedMap = Util.make(new Object2ObjectLinkedOpenHashMap(), map -> {
            map.put(TexturedRenderLayers.getEntitySolid(), this.blockBufferBuilders.get(BlockRenderLayer.SOLID));
            map.put(TexturedRenderLayers.getEntityCutout(), this.blockBufferBuilders.get(BlockRenderLayer.CUTOUT));
            map.put(TexturedRenderLayers.getItemTranslucentCull(), this.blockBufferBuilders.get(BlockRenderLayer.TRANSLUCENT));
            BufferBuilderStorage.assignBufferBuilder(map, TexturedRenderLayers.getBlockTranslucentCull());
            BufferBuilderStorage.assignBufferBuilder(map, TexturedRenderLayers.getShieldPatterns());
            BufferBuilderStorage.assignBufferBuilder(map, TexturedRenderLayers.getBeds());
            BufferBuilderStorage.assignBufferBuilder(map, TexturedRenderLayers.getShulkerBoxes());
            BufferBuilderStorage.assignBufferBuilder(map, TexturedRenderLayers.getSign());
            BufferBuilderStorage.assignBufferBuilder(map, TexturedRenderLayers.getHangingSign());
            map.put(TexturedRenderLayers.getChest(), new BufferAllocator(786432));
            BufferBuilderStorage.assignBufferBuilder(map, RenderLayers.armorEntityGlint());
            BufferBuilderStorage.assignBufferBuilder(map, RenderLayers.glint());
            BufferBuilderStorage.assignBufferBuilder(map, RenderLayers.glintTranslucent());
            BufferBuilderStorage.assignBufferBuilder(map, RenderLayers.entityGlint());
            BufferBuilderStorage.assignBufferBuilder(map, RenderLayers.waterMask());
        });
        this.entityVertexConsumers = VertexConsumerProvider.immediate(sequencedMap, new BufferAllocator(786432));
        this.outlineVertexConsumers = new OutlineVertexConsumerProvider();
        SequencedMap sequencedMap2 = Util.make(new Object2ObjectLinkedOpenHashMap(), objectMap -> ModelBaker.BLOCK_DESTRUCTION_RENDER_LAYERS.forEach(renderLayer -> BufferBuilderStorage.assignBufferBuilder(objectMap, renderLayer)));
        this.effectVertexConsumers = VertexConsumerProvider.immediate(sequencedMap2, new BufferAllocator(0));
    }

    private static void assignBufferBuilder(Object2ObjectLinkedOpenHashMap<RenderLayer, BufferAllocator> builderStorage, RenderLayer layer) {
        builderStorage.put(layer, new BufferAllocator(layer.getExpectedBufferSize()));
    }

    public BlockBufferAllocatorStorage getBlockBufferBuilders() {
        return this.blockBufferBuilders;
    }

    public BlockBufferBuilderPool getBlockBufferBuildersPool() {
        return this.blockBufferBuildersPool;
    }

    public VertexConsumerProvider.Immediate getEntityVertexConsumers() {
        return this.entityVertexConsumers;
    }

    public VertexConsumerProvider.Immediate getEffectVertexConsumers() {
        return this.effectVertexConsumers;
    }

    public OutlineVertexConsumerProvider getOutlineVertexConsumers() {
        return this.outlineVertexConsumers;
    }
}

