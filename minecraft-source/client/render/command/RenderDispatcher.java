/*
 * External method calls:
 *   Lnet/minecraft/client/render/command/ShadowPiecesCommandRenderer;render(Lnet/minecraft/client/render/command/BatchingRenderCommandQueue;Lnet/minecraft/client/render/VertexConsumerProvider$Immediate;)V
 *   Lnet/minecraft/client/render/command/ModelCommandRenderer;render(Lnet/minecraft/client/render/command/BatchingRenderCommandQueue;Lnet/minecraft/client/render/VertexConsumerProvider$Immediate;Lnet/minecraft/client/render/OutlineVertexConsumerProvider;Lnet/minecraft/client/render/VertexConsumerProvider$Immediate;)V
 *   Lnet/minecraft/client/render/command/ModelPartCommandRenderer;render(Lnet/minecraft/client/render/command/BatchingRenderCommandQueue;Lnet/minecraft/client/render/VertexConsumerProvider$Immediate;Lnet/minecraft/client/render/OutlineVertexConsumerProvider;Lnet/minecraft/client/render/VertexConsumerProvider$Immediate;)V
 *   Lnet/minecraft/client/render/command/FireCommandRenderer;render(Lnet/minecraft/client/render/command/BatchingRenderCommandQueue;Lnet/minecraft/client/render/VertexConsumerProvider$Immediate;Lnet/minecraft/client/texture/AtlasManager;)V
 *   Lnet/minecraft/client/render/command/LabelCommandRenderer;render(Lnet/minecraft/client/render/command/BatchingRenderCommandQueue;Lnet/minecraft/client/render/VertexConsumerProvider$Immediate;Lnet/minecraft/client/font/TextRenderer;)V
 *   Lnet/minecraft/client/render/command/TextCommandRenderer;render(Lnet/minecraft/client/render/command/BatchingRenderCommandQueue;Lnet/minecraft/client/render/VertexConsumerProvider$Immediate;)V
 *   Lnet/minecraft/client/render/command/LeashCommandRenderer;render(Lnet/minecraft/client/render/command/BatchingRenderCommandQueue;Lnet/minecraft/client/render/VertexConsumerProvider$Immediate;)V
 *   Lnet/minecraft/client/render/command/ItemCommandRenderer;render(Lnet/minecraft/client/render/command/BatchingRenderCommandQueue;Lnet/minecraft/client/render/VertexConsumerProvider$Immediate;Lnet/minecraft/client/render/OutlineVertexConsumerProvider;)V
 *   Lnet/minecraft/client/render/command/FallingBlockCommandRenderer;render(Lnet/minecraft/client/render/command/BatchingRenderCommandQueue;Lnet/minecraft/client/render/VertexConsumerProvider$Immediate;Lnet/minecraft/client/render/block/BlockRenderManager;Lnet/minecraft/client/render/OutlineVertexConsumerProvider;)V
 *   Lnet/minecraft/client/render/command/CustomCommandRenderer;render(Lnet/minecraft/client/render/command/BatchingRenderCommandQueue;Lnet/minecraft/client/render/VertexConsumerProvider$Immediate;)V
 *   Lnet/minecraft/client/render/command/LayeredCustomCommandRenderer;render(Lnet/minecraft/client/render/command/BatchingRenderCommandQueue;)V
 */
package net.minecraft.client.render.command;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.OutlineVertexConsumerProvider;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.command.BatchingRenderCommandQueue;
import net.minecraft.client.render.command.CustomCommandRenderer;
import net.minecraft.client.render.command.FallingBlockCommandRenderer;
import net.minecraft.client.render.command.FireCommandRenderer;
import net.minecraft.client.render.command.ItemCommandRenderer;
import net.minecraft.client.render.command.LabelCommandRenderer;
import net.minecraft.client.render.command.LayeredCustomCommandRenderer;
import net.minecraft.client.render.command.LeashCommandRenderer;
import net.minecraft.client.render.command.ModelCommandRenderer;
import net.minecraft.client.render.command.ModelPartCommandRenderer;
import net.minecraft.client.render.command.OrderedRenderCommandQueueImpl;
import net.minecraft.client.render.command.ShadowPiecesCommandRenderer;
import net.minecraft.client.render.command.TextCommandRenderer;
import net.minecraft.client.texture.AtlasManager;

@Environment(value=EnvType.CLIENT)
public class RenderDispatcher
implements AutoCloseable {
    private final OrderedRenderCommandQueueImpl queue;
    private final BlockRenderManager blockRenderManager;
    private final VertexConsumerProvider.Immediate vertexConsumers;
    private final AtlasManager atlasManager;
    private final OutlineVertexConsumerProvider outlineVertexConsumers;
    private final VertexConsumerProvider.Immediate crumblingOverlayVertexConsumers;
    private final TextRenderer textRenderer;
    private final ShadowPiecesCommandRenderer shadowPiecesCommandRenderer = new ShadowPiecesCommandRenderer();
    private final FireCommandRenderer fireCommandRenderer = new FireCommandRenderer();
    private final ModelCommandRenderer modelCommandRenderer = new ModelCommandRenderer();
    private final ModelPartCommandRenderer modelPartCommandRenderer = new ModelPartCommandRenderer();
    private final LabelCommandRenderer labelCommandRenderer = new LabelCommandRenderer();
    private final TextCommandRenderer textCommandRenderer = new TextCommandRenderer();
    private final LeashCommandRenderer leashCommandRenderer = new LeashCommandRenderer();
    private final ItemCommandRenderer itemCommandRenderer = new ItemCommandRenderer();
    private final CustomCommandRenderer customCommandRenderer = new CustomCommandRenderer();
    private final FallingBlockCommandRenderer fallingBlockCommandRenderer = new FallingBlockCommandRenderer();
    private final LayeredCustomCommandRenderer layeredCustomCommandRenderer = new LayeredCustomCommandRenderer();

    public RenderDispatcher(OrderedRenderCommandQueueImpl queue, BlockRenderManager blockRenderManager, VertexConsumerProvider.Immediate vertexConsumers, AtlasManager atlasManager, OutlineVertexConsumerProvider outlineVertexConsumers, VertexConsumerProvider.Immediate crumblingOverlayVertexConsumers, TextRenderer textRenderer) {
        this.queue = queue;
        this.blockRenderManager = blockRenderManager;
        this.vertexConsumers = vertexConsumers;
        this.atlasManager = atlasManager;
        this.outlineVertexConsumers = outlineVertexConsumers;
        this.crumblingOverlayVertexConsumers = crumblingOverlayVertexConsumers;
        this.textRenderer = textRenderer;
    }

    public void render() {
        for (BatchingRenderCommandQueue lv : this.queue.getBatchingQueues().values()) {
            this.shadowPiecesCommandRenderer.render(lv, this.vertexConsumers);
            this.modelCommandRenderer.render(lv, this.vertexConsumers, this.outlineVertexConsumers, this.crumblingOverlayVertexConsumers);
            this.modelPartCommandRenderer.render(lv, this.vertexConsumers, this.outlineVertexConsumers, this.crumblingOverlayVertexConsumers);
            this.fireCommandRenderer.render(lv, this.vertexConsumers, this.atlasManager);
            this.labelCommandRenderer.render(lv, this.vertexConsumers, this.textRenderer);
            this.textCommandRenderer.render(lv, this.vertexConsumers);
            this.leashCommandRenderer.render(lv, this.vertexConsumers);
            this.itemCommandRenderer.render(lv, this.vertexConsumers, this.outlineVertexConsumers);
            this.fallingBlockCommandRenderer.render(lv, this.vertexConsumers, this.blockRenderManager, this.outlineVertexConsumers);
            this.customCommandRenderer.render(lv, this.vertexConsumers);
            this.layeredCustomCommandRenderer.render(lv);
        }
        this.queue.clear();
    }

    public void endLayeredCustoms() {
        this.layeredCustomCommandRenderer.end();
    }

    public OrderedRenderCommandQueueImpl getQueue() {
        return this.queue;
    }

    @Override
    public void close() {
        this.layeredCustomCommandRenderer.close();
    }
}

