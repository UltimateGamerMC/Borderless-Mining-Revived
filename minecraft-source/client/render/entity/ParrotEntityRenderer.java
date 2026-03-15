/*
 * External method calls:
 *   Lnet/minecraft/client/render/entity/MobEntityRenderer;updateRenderState(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/client/render/entity/state/LivingEntityRenderState;F)V
 *   Lnet/minecraft/util/Identifier;ofVanilla(Ljava/lang/String;)Lnet/minecraft/util/Identifier;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/client/render/entity/ParrotEntityRenderer;updateRenderState(Lnet/minecraft/entity/passive/ParrotEntity;Lnet/minecraft/client/render/entity/state/ParrotEntityRenderState;F)V
 *   Lnet/minecraft/client/render/entity/ParrotEntityRenderer;createRenderState()Lnet/minecraft/client/render/entity/state/ParrotEntityRenderState;
 */
package net.minecraft.client.render.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.ParrotEntityModel;
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import net.minecraft.client.render.entity.state.ParrotEntityRenderState;
import net.minecraft.entity.passive.ParrotEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

@Environment(value=EnvType.CLIENT)
public class ParrotEntityRenderer
extends MobEntityRenderer<ParrotEntity, ParrotEntityRenderState, ParrotEntityModel> {
    private static final Identifier RED_BLUE_TEXTURE = Identifier.ofVanilla("textures/entity/parrot/parrot_red_blue.png");
    private static final Identifier BLUE_TEXTURE = Identifier.ofVanilla("textures/entity/parrot/parrot_blue.png");
    private static final Identifier GREEN_TEXTURE = Identifier.ofVanilla("textures/entity/parrot/parrot_green.png");
    private static final Identifier YELLOW_TEXTURE = Identifier.ofVanilla("textures/entity/parrot/parrot_yellow_blue.png");
    private static final Identifier GREY_TEXTURE = Identifier.ofVanilla("textures/entity/parrot/parrot_grey.png");

    public ParrotEntityRenderer(EntityRendererFactory.Context arg) {
        super(arg, new ParrotEntityModel(arg.getPart(EntityModelLayers.PARROT)), 0.3f);
    }

    @Override
    public Identifier getTexture(ParrotEntityRenderState arg) {
        return ParrotEntityRenderer.getTexture(arg.variant);
    }

    @Override
    public ParrotEntityRenderState createRenderState() {
        return new ParrotEntityRenderState();
    }

    @Override
    public void updateRenderState(ParrotEntity arg, ParrotEntityRenderState arg2, float f) {
        super.updateRenderState(arg, arg2, f);
        arg2.variant = arg.getVariant();
        float g = MathHelper.lerp(f, arg.lastFlapProgress, arg.flapProgress);
        float h = MathHelper.lerp(f, arg.lastMaxWingDeviation, arg.maxWingDeviation);
        arg2.flapAngle = (MathHelper.sin(g) + 1.0f) * h;
        arg2.parrotPose = ParrotEntityModel.getPose(arg);
    }

    public static Identifier getTexture(ParrotEntity.Variant variant) {
        return switch (variant) {
            default -> throw new MatchException(null, null);
            case ParrotEntity.Variant.RED_BLUE -> RED_BLUE_TEXTURE;
            case ParrotEntity.Variant.BLUE -> BLUE_TEXTURE;
            case ParrotEntity.Variant.GREEN -> GREEN_TEXTURE;
            case ParrotEntity.Variant.YELLOW_BLUE -> YELLOW_TEXTURE;
            case ParrotEntity.Variant.GRAY -> GREY_TEXTURE;
        };
    }

    @Override
    public /* synthetic */ Identifier getTexture(LivingEntityRenderState state) {
        return this.getTexture((ParrotEntityRenderState)state);
    }

    @Override
    public /* synthetic */ EntityRenderState createRenderState() {
        return this.createRenderState();
    }
}

