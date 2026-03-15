/*
 * External method calls:
 *   Lnet/minecraft/component/type/EquippableComponent;assetId()Ljava/util/Optional;
 *   Lnet/minecraft/client/render/entity/equipment/EquipmentRenderer;render(Lnet/minecraft/client/render/entity/equipment/EquipmentModel$LayerType;Lnet/minecraft/registry/RegistryKey;Lnet/minecraft/client/model/Model;Ljava/lang/Object;Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;II)V
 *
 * Internal private/static methods:
 *   Lnet/minecraft/client/render/entity/feature/LlamaDecorFeatureRenderer;render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;Lnet/minecraft/client/render/entity/state/LlamaEntityRenderState;Lnet/minecraft/item/ItemStack;Lnet/minecraft/registry/RegistryKey;I)V
 *   Lnet/minecraft/client/render/entity/feature/LlamaDecorFeatureRenderer;render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;ILnet/minecraft/client/render/entity/state/LlamaEntityRenderState;FF)V
 */
package net.minecraft.client.render.entity.feature;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.entity.equipment.EquipmentModel;
import net.minecraft.client.render.entity.equipment.EquipmentRenderer;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.LlamaEntityModel;
import net.minecraft.client.render.entity.model.LoadedEntityModels;
import net.minecraft.client.render.entity.state.LlamaEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.EquippableComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.item.equipment.EquipmentAsset;
import net.minecraft.item.equipment.EquipmentAssetKeys;
import net.minecraft.registry.RegistryKey;

@Environment(value=EnvType.CLIENT)
public class LlamaDecorFeatureRenderer
extends FeatureRenderer<LlamaEntityRenderState, LlamaEntityModel> {
    private final LlamaEntityModel model;
    private final LlamaEntityModel babyModel;
    private final EquipmentRenderer equipmentRenderer;

    public LlamaDecorFeatureRenderer(FeatureRendererContext<LlamaEntityRenderState, LlamaEntityModel> context, LoadedEntityModels loader, EquipmentRenderer equipmentRenderer) {
        super(context);
        this.equipmentRenderer = equipmentRenderer;
        this.model = new LlamaEntityModel(loader.getModelPart(EntityModelLayers.LLAMA_DECOR));
        this.babyModel = new LlamaEntityModel(loader.getModelPart(EntityModelLayers.LLAMA_BABY_DECOR));
    }

    @Override
    public void render(MatrixStack arg, OrderedRenderCommandQueue arg2, int i, LlamaEntityRenderState arg3, float f, float g) {
        ItemStack lv = arg3.bodyArmor;
        EquippableComponent lv2 = lv.get(DataComponentTypes.EQUIPPABLE);
        if (lv2 != null && lv2.assetId().isPresent()) {
            this.render(arg, arg2, arg3, lv, lv2.assetId().get(), i);
        } else if (arg3.trader) {
            this.render(arg, arg2, arg3, ItemStack.EMPTY, EquipmentAssetKeys.TRADER_LLAMA, i);
        }
    }

    private void render(MatrixStack matrices, OrderedRenderCommandQueue queue, LlamaEntityRenderState state, ItemStack stack, RegistryKey<EquipmentAsset> assetKey, int light) {
        LlamaEntityModel lv = state.baby ? this.babyModel : this.model;
        this.equipmentRenderer.render(EquipmentModel.LayerType.LLAMA_BODY, assetKey, lv, state, stack, matrices, queue, light, state.outlineColor);
    }
}

