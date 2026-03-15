/*
 * External method calls:
 *   Lnet/minecraft/client/render/entity/MobEntityRenderer;render(Lnet/minecraft/client/render/entity/state/LivingEntityRenderState;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;Lnet/minecraft/client/render/state/CameraRenderState;)V
 *   Lnet/minecraft/client/render/entity/MobEntityRenderer;updateRenderState(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/client/render/entity/state/LivingEntityRenderState;F)V
 *   Lnet/minecraft/client/render/entity/MobEntityRenderer;setupTransforms(Lnet/minecraft/client/render/entity/state/LivingEntityRenderState;Lnet/minecraft/client/util/math/MatrixStack;FF)V
 *   Lnet/minecraft/util/Identifier;ofVanilla(Ljava/lang/String;)Lnet/minecraft/util/Identifier;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/client/render/entity/PufferfishEntityRenderer;updateRenderState(Lnet/minecraft/entity/passive/PufferfishEntity;Lnet/minecraft/client/render/entity/state/PufferfishEntityRenderState;F)V
 *   Lnet/minecraft/client/render/entity/PufferfishEntityRenderer;setupTransforms(Lnet/minecraft/client/render/entity/state/PufferfishEntityRenderState;Lnet/minecraft/client/util/math/MatrixStack;FF)V
 *   Lnet/minecraft/client/render/entity/PufferfishEntityRenderer;render(Lnet/minecraft/client/render/entity/state/PufferfishEntityRenderState;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;Lnet/minecraft/client/render/state/CameraRenderState;)V
 *   Lnet/minecraft/client/render/entity/PufferfishEntityRenderer;createRenderState()Lnet/minecraft/client/render/entity/state/PufferfishEntityRenderState;
 */
package net.minecraft.client.render.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.LargePufferfishEntityModel;
import net.minecraft.client.render.entity.model.MediumPufferfishEntityModel;
import net.minecraft.client.render.entity.model.SmallPufferfishEntityModel;
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import net.minecraft.client.render.entity.state.PufferfishEntityRenderState;
import net.minecraft.client.render.state.CameraRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.passive.PufferfishEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

@Environment(value=EnvType.CLIENT)
public class PufferfishEntityRenderer
extends MobEntityRenderer<PufferfishEntity, PufferfishEntityRenderState, EntityModel<EntityRenderState>> {
    private static final Identifier TEXTURE = Identifier.ofVanilla("textures/entity/fish/pufferfish.png");
    private final EntityModel<EntityRenderState> smallModel;
    private final EntityModel<EntityRenderState> mediumModel;
    private final EntityModel<EntityRenderState> largeModel = this.getModel();

    public PufferfishEntityRenderer(EntityRendererFactory.Context arg) {
        super(arg, new LargePufferfishEntityModel(arg.getPart(EntityModelLayers.PUFFERFISH_BIG)), 0.2f);
        this.mediumModel = new MediumPufferfishEntityModel(arg.getPart(EntityModelLayers.PUFFERFISH_MEDIUM));
        this.smallModel = new SmallPufferfishEntityModel(arg.getPart(EntityModelLayers.PUFFERFISH_SMALL));
    }

    @Override
    public Identifier getTexture(PufferfishEntityRenderState arg) {
        return TEXTURE;
    }

    @Override
    public PufferfishEntityRenderState createRenderState() {
        return new PufferfishEntityRenderState();
    }

    @Override
    protected float getShadowRadius(PufferfishEntityRenderState arg) {
        return 0.1f + 0.1f * (float)arg.puffState;
    }

    @Override
    public void render(PufferfishEntityRenderState arg, MatrixStack arg2, OrderedRenderCommandQueue arg3, CameraRenderState arg4) {
        this.model = switch (arg.puffState) {
            case 0 -> this.smallModel;
            case 1 -> this.mediumModel;
            default -> this.largeModel;
        };
        super.render(arg, arg2, arg3, arg4);
    }

    @Override
    public void updateRenderState(PufferfishEntity arg, PufferfishEntityRenderState arg2, float f) {
        super.updateRenderState(arg, arg2, f);
        arg2.puffState = arg.getPuffState();
    }

    @Override
    protected void setupTransforms(PufferfishEntityRenderState arg, MatrixStack arg2, float f, float g) {
        arg2.translate(0.0f, MathHelper.cos(arg.age * 0.05f) * 0.08f, 0.0f);
        super.setupTransforms(arg, arg2, f, g);
    }

    @Override
    protected /* synthetic */ float getShadowRadius(LivingEntityRenderState arg) {
        return this.getShadowRadius((PufferfishEntityRenderState)arg);
    }

    @Override
    public /* synthetic */ Identifier getTexture(LivingEntityRenderState state) {
        return this.getTexture((PufferfishEntityRenderState)state);
    }

    @Override
    public /* synthetic */ EntityRenderState createRenderState() {
        return this.createRenderState();
    }

    @Override
    protected /* synthetic */ float getShadowRadius(EntityRenderState state) {
        return this.getShadowRadius((PufferfishEntityRenderState)state);
    }
}

