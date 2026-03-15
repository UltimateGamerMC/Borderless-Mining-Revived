/*
 * External method calls:
 *   Lnet/minecraft/client/render/entity/AgeableMobEntityRenderer;updateRenderState(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/client/render/entity/state/LivingEntityRenderState;F)V
 *   Lnet/minecraft/util/Identifier;ofVanilla(Ljava/lang/String;)Lnet/minecraft/util/Identifier;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/client/render/entity/NautilusEntityRenderer;addFeature(Lnet/minecraft/client/render/entity/feature/FeatureRenderer;)Z
 *   Lnet/minecraft/client/render/entity/NautilusEntityRenderer;updateRenderState(Lnet/minecraft/entity/passive/AbstractNautilusEntity;Lnet/minecraft/client/render/entity/state/NautilusEntityRenderState;F)V
 *   Lnet/minecraft/client/render/entity/NautilusEntityRenderer;createRenderState()Lnet/minecraft/client/render/entity/state/NautilusEntityRenderState;
 */
package net.minecraft.client.render.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.AgeableMobEntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.equipment.EquipmentModel;
import net.minecraft.client.render.entity.feature.SaddleFeatureRenderer;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.NautilusArmorEntityModel;
import net.minecraft.client.render.entity.model.NautilusEntityModel;
import net.minecraft.client.render.entity.model.NautilusSaddleEntityModel;
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import net.minecraft.client.render.entity.state.NautilusEntityRenderState;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.AbstractNautilusEntity;
import net.minecraft.util.Identifier;

@Environment(value=EnvType.CLIENT)
public class NautilusEntityRenderer<T extends AbstractNautilusEntity>
extends AgeableMobEntityRenderer<T, NautilusEntityRenderState, NautilusEntityModel> {
    private static final Identifier TEXTURE = Identifier.ofVanilla("textures/entity/nautilus/nautilus.png");
    private static final Identifier BABY_TEXTURE = Identifier.ofVanilla("textures/entity/nautilus/nautilus_baby.png");

    public NautilusEntityRenderer(EntityRendererFactory.Context context) {
        super(context, new NautilusEntityModel(context.getPart(EntityModelLayers.NAUTILUS)), new NautilusEntityModel(context.getPart(EntityModelLayers.NAUTILUS_BABY)), 0.7f);
        this.addFeature(new SaddleFeatureRenderer<NautilusEntityRenderState, NautilusEntityModel, Object>(this, context.getEquipmentRenderer(), EquipmentModel.LayerType.NAUTILUS_BODY, state -> state.armorStack, new NautilusArmorEntityModel(context.getPart(EntityModelLayers.NAUTILUS_ARMOR)), null));
        this.addFeature(new SaddleFeatureRenderer<NautilusEntityRenderState, NautilusEntityModel, Object>(this, context.getEquipmentRenderer(), EquipmentModel.LayerType.NAUTILUS_SADDLE, state -> state.saddleStack, new NautilusSaddleEntityModel(context.getPart(EntityModelLayers.NAUTILUS_SADDLE)), null));
    }

    @Override
    public Identifier getTexture(NautilusEntityRenderState arg) {
        return arg.baby ? BABY_TEXTURE : TEXTURE;
    }

    @Override
    public NautilusEntityRenderState createRenderState() {
        return new NautilusEntityRenderState();
    }

    @Override
    public void updateRenderState(T arg, NautilusEntityRenderState arg2, float f) {
        super.updateRenderState(arg, arg2, f);
        arg2.saddleStack = ((LivingEntity)arg).getEquippedStack(EquipmentSlot.SADDLE).copy();
        arg2.armorStack = ((MobEntity)arg).getBodyArmor().copy();
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

