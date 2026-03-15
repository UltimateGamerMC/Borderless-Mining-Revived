/*
 * External method calls:
 *   Lnet/minecraft/client/render/entity/AgeableMobEntityRenderer;updateRenderState(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/client/render/entity/state/LivingEntityRenderState;F)V
 *   Lnet/minecraft/util/Identifier;ofVanilla(Ljava/lang/String;)Lnet/minecraft/util/Identifier;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/client/render/entity/BeeEntityRenderer;updateRenderState(Lnet/minecraft/entity/passive/BeeEntity;Lnet/minecraft/client/render/entity/state/BeeEntityRenderState;F)V
 *   Lnet/minecraft/client/render/entity/BeeEntityRenderer;createRenderState()Lnet/minecraft/client/render/entity/state/BeeEntityRenderState;
 */
package net.minecraft.client.render.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.AgeableMobEntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.model.BeeEntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.state.BeeEntityRenderState;
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.util.Identifier;

@Environment(value=EnvType.CLIENT)
public class BeeEntityRenderer
extends AgeableMobEntityRenderer<BeeEntity, BeeEntityRenderState, BeeEntityModel> {
    private static final Identifier ANGRY_TEXTURE = Identifier.ofVanilla("textures/entity/bee/bee_angry.png");
    private static final Identifier ANGRY_NECTAR_TEXTURE = Identifier.ofVanilla("textures/entity/bee/bee_angry_nectar.png");
    private static final Identifier PASSIVE_TEXTURE = Identifier.ofVanilla("textures/entity/bee/bee.png");
    private static final Identifier NECTAR_TEXTURE = Identifier.ofVanilla("textures/entity/bee/bee_nectar.png");

    public BeeEntityRenderer(EntityRendererFactory.Context arg) {
        super(arg, new BeeEntityModel(arg.getPart(EntityModelLayers.BEE)), new BeeEntityModel(arg.getPart(EntityModelLayers.BEE_BABY)), 0.4f);
    }

    @Override
    public Identifier getTexture(BeeEntityRenderState arg) {
        if (arg.angry) {
            if (arg.hasNectar) {
                return ANGRY_NECTAR_TEXTURE;
            }
            return ANGRY_TEXTURE;
        }
        if (arg.hasNectar) {
            return NECTAR_TEXTURE;
        }
        return PASSIVE_TEXTURE;
    }

    @Override
    public BeeEntityRenderState createRenderState() {
        return new BeeEntityRenderState();
    }

    @Override
    public void updateRenderState(BeeEntity arg, BeeEntityRenderState arg2, float f) {
        super.updateRenderState(arg, arg2, f);
        arg2.bodyPitch = arg.getBodyPitch(f);
        arg2.hasStinger = !arg.hasStung();
        arg2.stoppedOnGround = arg.isOnGround() && arg.getVelocity().lengthSquared() < 1.0E-7;
        arg2.angry = arg.hasAngerTime();
        arg2.hasNectar = arg.hasNectar();
    }

    @Override
    public /* synthetic */ Identifier getTexture(LivingEntityRenderState state) {
        return this.getTexture((BeeEntityRenderState)state);
    }

    @Override
    public /* synthetic */ EntityRenderState createRenderState() {
        return this.createRenderState();
    }
}

