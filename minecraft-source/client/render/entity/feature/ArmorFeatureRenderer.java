/*
 * External method calls:
 *   Lnet/minecraft/component/type/EquippableComponent;assetId()Ljava/util/Optional;
 *   Lnet/minecraft/component/type/EquippableComponent;slot()Lnet/minecraft/entity/EquipmentSlot;
 *   Lnet/minecraft/client/render/entity/equipment/EquipmentRenderer;render(Lnet/minecraft/client/render/entity/equipment/EquipmentModel$LayerType;Lnet/minecraft/registry/RegistryKey;Lnet/minecraft/client/model/Model;Ljava/lang/Object;Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;II)V
 *
 * Internal private/static methods:
 *   Lnet/minecraft/client/render/entity/feature/ArmorFeatureRenderer;renderArmor(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;Lnet/minecraft/item/ItemStack;Lnet/minecraft/entity/EquipmentSlot;ILnet/minecraft/client/render/entity/state/BipedEntityRenderState;)V
 *   Lnet/minecraft/client/render/entity/feature/ArmorFeatureRenderer;usesInnerModel(Lnet/minecraft/entity/EquipmentSlot;)Z
 *   Lnet/minecraft/client/render/entity/feature/ArmorFeatureRenderer;render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;ILnet/minecraft/client/render/entity/state/BipedEntityRenderState;FF)V
 */
package net.minecraft.client.render.entity.feature;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.entity.equipment.EquipmentModel;
import net.minecraft.client.render.entity.equipment.EquipmentRenderer;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.EquipmentModelData;
import net.minecraft.client.render.entity.state.BipedEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.EquippableComponent;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;

@Environment(value=EnvType.CLIENT)
public class ArmorFeatureRenderer<S extends BipedEntityRenderState, M extends BipedEntityModel<S>, A extends BipedEntityModel<S>>
extends FeatureRenderer<S, M> {
    private final EquipmentModelData<A> adultModelData;
    private final EquipmentModelData<A> babyModelData;
    private final EquipmentRenderer equipmentRenderer;

    public ArmorFeatureRenderer(FeatureRendererContext<S, M> context, EquipmentModelData<A> modelData, EquipmentRenderer equipmentRenderer) {
        this(context, modelData, modelData, equipmentRenderer);
    }

    public ArmorFeatureRenderer(FeatureRendererContext<S, M> context, EquipmentModelData<A> adultModelData, EquipmentModelData<A> babyModelData, EquipmentRenderer equipmentRenderer) {
        super(context);
        this.adultModelData = adultModelData;
        this.babyModelData = babyModelData;
        this.equipmentRenderer = equipmentRenderer;
    }

    public static boolean hasModel(ItemStack stack, EquipmentSlot slot) {
        EquippableComponent lv = stack.get(DataComponentTypes.EQUIPPABLE);
        return lv != null && ArmorFeatureRenderer.hasModel(lv, slot);
    }

    private static boolean hasModel(EquippableComponent component, EquipmentSlot slot) {
        return component.assetId().isPresent() && component.slot() == slot;
    }

    @Override
    public void render(MatrixStack arg, OrderedRenderCommandQueue arg2, int i, S arg3, float f, float g) {
        this.renderArmor(arg, arg2, ((BipedEntityRenderState)arg3).equippedChestStack, EquipmentSlot.CHEST, i, arg3);
        this.renderArmor(arg, arg2, ((BipedEntityRenderState)arg3).equippedLegsStack, EquipmentSlot.LEGS, i, arg3);
        this.renderArmor(arg, arg2, ((BipedEntityRenderState)arg3).equippedFeetStack, EquipmentSlot.FEET, i, arg3);
        this.renderArmor(arg, arg2, ((BipedEntityRenderState)arg3).equippedHeadStack, EquipmentSlot.HEAD, i, arg3);
    }

    private void renderArmor(MatrixStack matrices, OrderedRenderCommandQueue queue, ItemStack stack, EquipmentSlot slot, int light, S state) {
        EquippableComponent lv = stack.get(DataComponentTypes.EQUIPPABLE);
        if (lv == null || !ArmorFeatureRenderer.hasModel(lv, slot)) {
            return;
        }
        A lv2 = this.getModel(state, slot);
        EquipmentModel.LayerType lv3 = this.usesInnerModel(slot) ? EquipmentModel.LayerType.HUMANOID_LEGGINGS : EquipmentModel.LayerType.HUMANOID;
        this.equipmentRenderer.render(lv3, lv.assetId().orElseThrow(), lv2, state, stack, matrices, queue, light, ((BipedEntityRenderState)state).outlineColor);
    }

    private A getModel(S state, EquipmentSlot slot) {
        return (A)((BipedEntityModel)(((BipedEntityRenderState)state).baby ? this.babyModelData : this.adultModelData).getModelData(slot));
    }

    private boolean usesInnerModel(EquipmentSlot slot) {
        return slot == EquipmentSlot.LEGS;
    }
}

