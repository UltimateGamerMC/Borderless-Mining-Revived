/*
 * External method calls:
 *   Lnet/minecraft/client/render/entity/MobEntityRenderer;updateRenderState(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/client/render/entity/state/LivingEntityRenderState;F)V
 *   Lnet/minecraft/client/render/entity/state/ItemHolderEntityRenderState;update(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/client/render/entity/state/ItemHolderEntityRenderState;Lnet/minecraft/client/item/ItemModelManager;)V
 *   Lnet/minecraft/util/Identifier;ofVanilla(Ljava/lang/String;)Lnet/minecraft/util/Identifier;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/client/render/entity/WitchEntityRenderer;addFeature(Lnet/minecraft/client/render/entity/feature/FeatureRenderer;)Z
 *   Lnet/minecraft/client/render/entity/WitchEntityRenderer;updateRenderState(Lnet/minecraft/entity/mob/WitchEntity;Lnet/minecraft/client/render/entity/state/WitchEntityRenderState;F)V
 *   Lnet/minecraft/client/render/entity/WitchEntityRenderer;createRenderState()Lnet/minecraft/client/render/entity/state/WitchEntityRenderState;
 */
package net.minecraft.client.render.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.feature.WitchHeldItemFeatureRenderer;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.WitchEntityModel;
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.client.render.entity.state.ItemHolderEntityRenderState;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import net.minecraft.client.render.entity.state.WitchEntityRenderState;
import net.minecraft.entity.mob.WitchEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;

@Environment(value=EnvType.CLIENT)
public class WitchEntityRenderer
extends MobEntityRenderer<WitchEntity, WitchEntityRenderState, WitchEntityModel> {
    private static final Identifier TEXTURE = Identifier.ofVanilla("textures/entity/witch.png");

    public WitchEntityRenderer(EntityRendererFactory.Context arg) {
        super(arg, new WitchEntityModel(arg.getPart(EntityModelLayers.WITCH)), 0.5f);
        this.addFeature(new WitchHeldItemFeatureRenderer(this));
    }

    @Override
    public Identifier getTexture(WitchEntityRenderState arg) {
        return TEXTURE;
    }

    @Override
    public WitchEntityRenderState createRenderState() {
        return new WitchEntityRenderState();
    }

    @Override
    public void updateRenderState(WitchEntity arg, WitchEntityRenderState arg2, float f) {
        super.updateRenderState(arg, arg2, f);
        ItemHolderEntityRenderState.update(arg, arg2, this.itemModelResolver);
        arg2.id = arg.getId();
        ItemStack lv = arg.getMainHandStack();
        arg2.holdingItem = !lv.isEmpty();
        arg2.holdingPotion = lv.isOf(Items.POTION);
    }

    @Override
    public /* synthetic */ Identifier getTexture(LivingEntityRenderState state) {
        return this.getTexture((WitchEntityRenderState)state);
    }

    @Override
    public /* synthetic */ EntityRenderState createRenderState() {
        return this.createRenderState();
    }
}

