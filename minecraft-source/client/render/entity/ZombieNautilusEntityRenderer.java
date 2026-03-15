/*
 * External method calls:
 *   Lnet/minecraft/entity/mob/ZombieNautilusVariant;modelAndTexture()Lnet/minecraft/util/ModelAndTexture;
 *   Lnet/minecraft/util/ModelAndTexture;model()Ljava/lang/Object;
 *   Lnet/minecraft/client/render/entity/MobEntityRenderer;render(Lnet/minecraft/client/render/entity/state/LivingEntityRenderState;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;Lnet/minecraft/client/render/state/CameraRenderState;)V
 *   Lnet/minecraft/util/ModelAndTexture;asset()Lnet/minecraft/util/AssetInfo$TextureAssetInfo;
 *   Lnet/minecraft/util/AssetInfo$TextureAssetInfo;texturePath()Lnet/minecraft/util/Identifier;
 *   Lnet/minecraft/client/render/entity/MobEntityRenderer;updateRenderState(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/client/render/entity/state/LivingEntityRenderState;F)V
 *
 * Internal private/static methods:
 *   Lnet/minecraft/client/render/entity/ZombieNautilusEntityRenderer;addFeature(Lnet/minecraft/client/render/entity/feature/FeatureRenderer;)Z
 *   Lnet/minecraft/client/render/entity/ZombieNautilusEntityRenderer;createModels(Lnet/minecraft/client/render/entity/EntityRendererFactory$Context;)Ljava/util/Map;
 *   Lnet/minecraft/client/render/entity/ZombieNautilusEntityRenderer;updateRenderState(Lnet/minecraft/entity/mob/ZombieNautilusEntity;Lnet/minecraft/client/render/entity/state/NautilusEntityRenderState;F)V
 *   Lnet/minecraft/client/render/entity/ZombieNautilusEntityRenderer;render(Lnet/minecraft/client/render/entity/state/NautilusEntityRenderState;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;Lnet/minecraft/client/render/state/CameraRenderState;)V
 *   Lnet/minecraft/client/render/entity/ZombieNautilusEntityRenderer;createRenderState()Lnet/minecraft/client/render/entity/state/NautilusEntityRenderState;
 */
package net.minecraft.client.render.entity;

import com.google.common.collect.Maps;
import java.util.Map;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.equipment.EquipmentModel;
import net.minecraft.client.render.entity.feature.SaddleFeatureRenderer;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.NautilusArmorEntityModel;
import net.minecraft.client.render.entity.model.NautilusEntityModel;
import net.minecraft.client.render.entity.model.NautilusSaddleEntityModel;
import net.minecraft.client.render.entity.model.ZombieNautilusCoralEntityModel;
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import net.minecraft.client.render.entity.state.NautilusEntityRenderState;
import net.minecraft.client.render.state.CameraRenderState;
import net.minecraft.client.texture.MissingSprite;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.mob.ZombieNautilusEntity;
import net.minecraft.entity.mob.ZombieNautilusVariant;
import net.minecraft.util.Identifier;

@Environment(value=EnvType.CLIENT)
public class ZombieNautilusEntityRenderer
extends MobEntityRenderer<ZombieNautilusEntity, NautilusEntityRenderState, NautilusEntityModel> {
    private final Map<ZombieNautilusVariant.Model, NautilusEntityModel> models;

    public ZombieNautilusEntityRenderer(EntityRendererFactory.Context arg) {
        super(arg, new NautilusEntityModel(arg.getPart(EntityModelLayers.ZOMBIE_NAUTILUS)), 0.7f);
        this.addFeature(new SaddleFeatureRenderer<NautilusEntityRenderState, NautilusEntityModel, Object>(this, arg.getEquipmentRenderer(), EquipmentModel.LayerType.NAUTILUS_BODY, state -> state.armorStack, new NautilusArmorEntityModel(arg.getPart(EntityModelLayers.NAUTILUS_ARMOR)), null));
        this.addFeature(new SaddleFeatureRenderer<NautilusEntityRenderState, NautilusEntityModel, Object>(this, arg.getEquipmentRenderer(), EquipmentModel.LayerType.NAUTILUS_SADDLE, state -> state.saddleStack, new NautilusSaddleEntityModel(arg.getPart(EntityModelLayers.NAUTILUS_SADDLE)), null));
        this.models = ZombieNautilusEntityRenderer.createModels(arg);
    }

    private static Map<ZombieNautilusVariant.Model, NautilusEntityModel> createModels(EntityRendererFactory.Context context) {
        return Maps.newEnumMap(Map.of(ZombieNautilusVariant.Model.NORMAL, new NautilusEntityModel(context.getPart(EntityModelLayers.ZOMBIE_NAUTILUS)), ZombieNautilusVariant.Model.WARM, new ZombieNautilusCoralEntityModel(context.getPart(EntityModelLayers.ZOMBIE_NAUTILUS_CORAL))));
    }

    @Override
    public void render(NautilusEntityRenderState arg, MatrixStack arg2, OrderedRenderCommandQueue arg3, CameraRenderState arg4) {
        if (arg.variant == null) {
            return;
        }
        this.model = this.models.get(arg.variant.modelAndTexture().model());
        super.render(arg, arg2, arg3, arg4);
    }

    @Override
    public Identifier getTexture(NautilusEntityRenderState arg) {
        return arg.variant == null ? MissingSprite.getMissingSpriteId() : arg.variant.modelAndTexture().asset().texturePath();
    }

    @Override
    public NautilusEntityRenderState createRenderState() {
        return new NautilusEntityRenderState();
    }

    @Override
    public void updateRenderState(ZombieNautilusEntity arg, NautilusEntityRenderState arg2, float f) {
        super.updateRenderState(arg, arg2, f);
        arg2.saddleStack = arg.getEquippedStack(EquipmentSlot.SADDLE).copy();
        arg2.armorStack = arg.getBodyArmor().copy();
        arg2.variant = arg.getVariant().value();
    }

    @Override
    public /* synthetic */ Identifier getTexture(LivingEntityRenderState state) {
        return this.getTexture((NautilusEntityRenderState)state);
    }

    @Override
    public /* synthetic */ EntityRenderState createRenderState() {
        return this.createRenderState();
    }
}

