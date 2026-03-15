/*
 * External method calls:
 *   Lnet/minecraft/client/render/entity/AgeableMobEntityRenderer;updateRenderState(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/client/render/entity/state/LivingEntityRenderState;F)V
 *   Lnet/minecraft/entity/passive/AxolotlEntity$Variant;values()[Lnet/minecraft/entity/passive/AxolotlEntity$Variant;
 *   Lnet/minecraft/util/Identifier;ofVanilla(Ljava/lang/String;)Lnet/minecraft/util/Identifier;
 *   Lnet/minecraft/util/Util;make(Ljava/lang/Object;Ljava/util/function/Consumer;)Ljava/lang/Object;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/client/render/entity/AxolotlEntityRenderer;updateRenderState(Lnet/minecraft/entity/passive/AxolotlEntity;Lnet/minecraft/client/render/entity/state/AxolotlEntityRenderState;F)V
 *   Lnet/minecraft/client/render/entity/AxolotlEntityRenderer;createRenderState()Lnet/minecraft/client/render/entity/state/AxolotlEntityRenderState;
 */
package net.minecraft.client.render.entity;

import com.google.common.collect.Maps;
import java.util.Locale;
import java.util.Map;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.AgeableMobEntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.model.AxolotlEntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.state.AxolotlEntityRenderState;
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import net.minecraft.entity.passive.AxolotlEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;

@Environment(value=EnvType.CLIENT)
public class AxolotlEntityRenderer
extends AgeableMobEntityRenderer<AxolotlEntity, AxolotlEntityRenderState, AxolotlEntityModel> {
    private static final Map<AxolotlEntity.Variant, Identifier> TEXTURES = Util.make(Maps.newHashMap(), variants -> {
        for (AxolotlEntity.Variant lv : AxolotlEntity.Variant.values()) {
            variants.put(lv, Identifier.ofVanilla(String.format(Locale.ROOT, "textures/entity/axolotl/axolotl_%s.png", lv.getId())));
        }
    });

    public AxolotlEntityRenderer(EntityRendererFactory.Context arg) {
        super(arg, new AxolotlEntityModel(arg.getPart(EntityModelLayers.AXOLOTL)), new AxolotlEntityModel(arg.getPart(EntityModelLayers.AXOLOTL_BABY)), 0.5f);
    }

    @Override
    public Identifier getTexture(AxolotlEntityRenderState arg) {
        return TEXTURES.get(arg.variant);
    }

    @Override
    public AxolotlEntityRenderState createRenderState() {
        return new AxolotlEntityRenderState();
    }

    @Override
    public void updateRenderState(AxolotlEntity arg, AxolotlEntityRenderState arg2, float f) {
        super.updateRenderState(arg, arg2, f);
        arg2.variant = arg.getVariant();
        arg2.playingDeadValue = arg.playingDeadFf.getValue(f);
        arg2.inWaterValue = arg.inWaterFf.getValue(f);
        arg2.onGroundValue = arg.onGroundFf.getValue(f);
        arg2.isMovingValue = arg.isMovingFf.getValue(f);
    }

    @Override
    public /* synthetic */ Identifier getTexture(LivingEntityRenderState state) {
        return this.getTexture((AxolotlEntityRenderState)state);
    }

    @Override
    public /* synthetic */ EntityRenderState createRenderState() {
        return this.createRenderState();
    }
}

