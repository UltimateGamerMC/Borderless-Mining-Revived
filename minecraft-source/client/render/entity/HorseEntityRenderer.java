/*
 * External method calls:
 *   Lnet/minecraft/client/render/entity/AbstractHorseEntityRenderer;updateRenderState(Lnet/minecraft/entity/passive/AbstractHorseEntity;Lnet/minecraft/client/render/entity/state/LivingHorseEntityRenderState;F)V
 *   Lnet/minecraft/util/Identifier;ofVanilla(Ljava/lang/String;)Lnet/minecraft/util/Identifier;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/client/render/entity/HorseEntityRenderer;addFeature(Lnet/minecraft/client/render/entity/feature/FeatureRenderer;)Z
 *   Lnet/minecraft/client/render/entity/HorseEntityRenderer;updateRenderState(Lnet/minecraft/entity/passive/HorseEntity;Lnet/minecraft/client/render/entity/state/HorseEntityRenderState;F)V
 *   Lnet/minecraft/client/render/entity/HorseEntityRenderer;createRenderState()Lnet/minecraft/client/render/entity/state/HorseEntityRenderState;
 */
package net.minecraft.client.render.entity;

import com.google.common.collect.Maps;
import java.util.Map;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.AbstractHorseEntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.equipment.EquipmentModel;
import net.minecraft.client.render.entity.feature.HorseMarkingFeatureRenderer;
import net.minecraft.client.render.entity.feature.SaddleFeatureRenderer;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.HorseEntityModel;
import net.minecraft.client.render.entity.model.HorseSaddleEntityModel;
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.client.render.entity.state.HorseEntityRenderState;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import net.minecraft.entity.passive.HorseColor;
import net.minecraft.entity.passive.HorseEntity;
import net.minecraft.util.Identifier;

@Environment(value=EnvType.CLIENT)
public final class HorseEntityRenderer
extends AbstractHorseEntityRenderer<HorseEntity, HorseEntityRenderState, HorseEntityModel> {
    private static final Map<HorseColor, Identifier> TEXTURES = Maps.newEnumMap(Map.of(HorseColor.WHITE, Identifier.ofVanilla("textures/entity/horse/horse_white.png"), HorseColor.CREAMY, Identifier.ofVanilla("textures/entity/horse/horse_creamy.png"), HorseColor.CHESTNUT, Identifier.ofVanilla("textures/entity/horse/horse_chestnut.png"), HorseColor.BROWN, Identifier.ofVanilla("textures/entity/horse/horse_brown.png"), HorseColor.BLACK, Identifier.ofVanilla("textures/entity/horse/horse_black.png"), HorseColor.GRAY, Identifier.ofVanilla("textures/entity/horse/horse_gray.png"), HorseColor.DARK_BROWN, Identifier.ofVanilla("textures/entity/horse/horse_darkbrown.png")));

    public HorseEntityRenderer(EntityRendererFactory.Context arg) {
        super(arg, new HorseEntityModel(arg.getPart(EntityModelLayers.HORSE)), new HorseEntityModel(arg.getPart(EntityModelLayers.HORSE_BABY)));
        this.addFeature(new HorseMarkingFeatureRenderer(this));
        this.addFeature(new SaddleFeatureRenderer<HorseEntityRenderState, HorseEntityModel, HorseEntityModel>(this, arg.getEquipmentRenderer(), EquipmentModel.LayerType.HORSE_BODY, state -> state.armorStack, new HorseEntityModel(arg.getPart(EntityModelLayers.HORSE_ARMOR)), new HorseEntityModel(arg.getPart(EntityModelLayers.HORSE_ARMOR_BABY)), 2));
        this.addFeature(new SaddleFeatureRenderer<HorseEntityRenderState, HorseEntityModel, HorseSaddleEntityModel>(this, arg.getEquipmentRenderer(), EquipmentModel.LayerType.HORSE_SADDLE, state -> state.saddleStack, new HorseSaddleEntityModel(arg.getPart(EntityModelLayers.HORSE_SADDLE)), new HorseSaddleEntityModel(arg.getPart(EntityModelLayers.HORSE_BABY_SADDLE)), 2));
    }

    @Override
    public Identifier getTexture(HorseEntityRenderState arg) {
        return TEXTURES.get(arg.color);
    }

    @Override
    public HorseEntityRenderState createRenderState() {
        return new HorseEntityRenderState();
    }

    @Override
    public void updateRenderState(HorseEntity arg, HorseEntityRenderState arg2, float f) {
        super.updateRenderState(arg, arg2, f);
        arg2.color = arg.getHorseColor();
        arg2.marking = arg.getMarking();
        arg2.armorStack = arg.getBodyArmor().copy();
    }

    @Override
    public /* synthetic */ Identifier getTexture(LivingEntityRenderState state) {
        return this.getTexture((HorseEntityRenderState)state);
    }

    @Override
    public /* synthetic */ EntityRenderState createRenderState() {
        return this.createRenderState();
    }
}

