/*
 * External method calls:
 *   Lnet/minecraft/component/type/EquippableComponent;assetId()Ljava/util/Optional;
 *   Lnet/minecraft/client/render/entity/equipment/EquipmentRenderer;render(Lnet/minecraft/client/render/entity/equipment/EquipmentModel$LayerType;Lnet/minecraft/registry/RegistryKey;Lnet/minecraft/client/model/Model;Ljava/lang/Object;Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;II)V
 *   Lnet/minecraft/client/render/RenderLayers;armorTranslucent(Lnet/minecraft/util/Identifier;)Lnet/minecraft/client/render/RenderLayer;
 *   Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;submitModel(Lnet/minecraft/client/model/Model;Ljava/lang/Object;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/RenderLayer;IIILnet/minecraft/client/render/command/ModelCommandRenderer$CrumblingOverlayCommand;)V
 *   Lnet/minecraft/util/Identifier;ofVanilla(Ljava/lang/String;)Lnet/minecraft/util/Identifier;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/client/render/entity/feature/WolfArmorFeatureRenderer;renderCracks(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;ILnet/minecraft/item/ItemStack;Lnet/minecraft/client/model/Model;Lnet/minecraft/client/render/entity/state/WolfEntityRenderState;)V
 *   Lnet/minecraft/client/render/entity/feature/WolfArmorFeatureRenderer;render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;ILnet/minecraft/client/render/entity/state/WolfEntityRenderState;FF)V
 */
package net.minecraft.client.render.entity.feature;

import java.util.Map;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.Model;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.entity.equipment.EquipmentModel;
import net.minecraft.client.render.entity.equipment.EquipmentRenderer;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.LoadedEntityModels;
import net.minecraft.client.render.entity.model.WolfEntityModel;
import net.minecraft.client.render.entity.state.WolfEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.EquippableComponent;
import net.minecraft.entity.passive.Cracks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

@Environment(value=EnvType.CLIENT)
public class WolfArmorFeatureRenderer
extends FeatureRenderer<WolfEntityRenderState, WolfEntityModel> {
    private final WolfEntityModel model;
    private final WolfEntityModel babyModel;
    private final EquipmentRenderer equipmentRenderer;
    private static final Map<Cracks.CrackLevel, Identifier> CRACK_TEXTURES = Map.of(Cracks.CrackLevel.LOW, Identifier.ofVanilla("textures/entity/wolf/wolf_armor_crackiness_low.png"), Cracks.CrackLevel.MEDIUM, Identifier.ofVanilla("textures/entity/wolf/wolf_armor_crackiness_medium.png"), Cracks.CrackLevel.HIGH, Identifier.ofVanilla("textures/entity/wolf/wolf_armor_crackiness_high.png"));

    public WolfArmorFeatureRenderer(FeatureRendererContext<WolfEntityRenderState, WolfEntityModel> context, LoadedEntityModels loader, EquipmentRenderer equipmentRenderer) {
        super(context);
        this.model = new WolfEntityModel(loader.getModelPart(EntityModelLayers.WOLF_ARMOR));
        this.babyModel = new WolfEntityModel(loader.getModelPart(EntityModelLayers.WOLF_BABY_ARMOR));
        this.equipmentRenderer = equipmentRenderer;
    }

    @Override
    public void render(MatrixStack arg, OrderedRenderCommandQueue arg2, int i, WolfEntityRenderState arg3, float f, float g) {
        ItemStack lv = arg3.bodyArmor;
        EquippableComponent lv2 = lv.get(DataComponentTypes.EQUIPPABLE);
        if (lv2 == null || lv2.assetId().isEmpty()) {
            return;
        }
        WolfEntityModel lv3 = arg3.baby ? this.babyModel : this.model;
        this.equipmentRenderer.render(EquipmentModel.LayerType.WOLF_BODY, lv2.assetId().get(), lv3, arg3, lv, arg, arg2, i, arg3.outlineColor);
        this.renderCracks(arg, arg2, i, lv, lv3, arg3);
    }

    private void renderCracks(MatrixStack matrices, OrderedRenderCommandQueue queue, int light, ItemStack stack, Model<WolfEntityRenderState> model, WolfEntityRenderState state) {
        Cracks.CrackLevel lv = Cracks.WOLF_ARMOR.getCrackLevel(stack);
        if (lv == Cracks.CrackLevel.NONE) {
            return;
        }
        Identifier lv2 = CRACK_TEXTURES.get((Object)lv);
        queue.submitModel(model, state, matrices, RenderLayers.armorTranslucent(lv2), light, OverlayTexture.DEFAULT_UV, state.outlineColor, null);
    }
}

