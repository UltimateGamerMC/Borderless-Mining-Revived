/*
 * External method calls:
 *   Lnet/minecraft/client/render/item/model/special/SpecialModelTypes;buildBlockToModelTypeMap(Lnet/minecraft/client/render/item/model/special/SpecialModelRenderer$BakeContext;)Ljava/util/Map;
 *   Lnet/minecraft/client/render/item/model/special/SpecialModelRenderer;render(Ljava/lang/Object;Lnet/minecraft/item/ItemDisplayContext;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;IIZI)V
 */
package net.minecraft.client.render.block.entity;

import java.util.Map;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.item.model.special.SpecialModelRenderer;
import net.minecraft.client.render.item.model.special.SpecialModelTypes;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemDisplayContext;

@Environment(value=EnvType.CLIENT)
public class LoadedBlockEntityModels {
    public static final LoadedBlockEntityModels EMPTY = new LoadedBlockEntityModels(Map.of());
    private final Map<Block, SpecialModelRenderer<?>> renderers;

    public LoadedBlockEntityModels(Map<Block, SpecialModelRenderer<?>> renderers) {
        this.renderers = renderers;
    }

    public static LoadedBlockEntityModels fromModels(SpecialModelRenderer.BakeContext context) {
        return new LoadedBlockEntityModels(SpecialModelTypes.buildBlockToModelTypeMap(context));
    }

    public void render(Block block, ItemDisplayContext displayContext, MatrixStack matrices, OrderedRenderCommandQueue queue, int light, int overlay, int outlineColor) {
        SpecialModelRenderer<?> lv = this.renderers.get(block);
        if (lv != null) {
            lv.render(null, displayContext, matrices, queue, light, overlay, false, outlineColor);
        }
    }
}

