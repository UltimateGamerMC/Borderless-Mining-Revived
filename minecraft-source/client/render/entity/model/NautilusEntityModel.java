/*
 * External method calls:
 *   Lnet/minecraft/client/render/entity/animation/AnimationDefinition;createAnimation(Lnet/minecraft/client/model/ModelPart;)Lnet/minecraft/client/render/entity/animation/Animation;
 *   Lnet/minecraft/client/model/TexturedModelData;of(Lnet/minecraft/client/model/ModelData;II)Lnet/minecraft/client/model/TexturedModelData;
 *   Lnet/minecraft/client/model/ModelPartBuilder;create()Lnet/minecraft/client/model/ModelPartBuilder;
 *   Lnet/minecraft/client/model/ModelTransform;origin(FFF)Lnet/minecraft/client/model/ModelTransform;
 *   Lnet/minecraft/client/model/ModelPartData;addChild(Ljava/lang/String;Lnet/minecraft/client/model/ModelPartBuilder;Lnet/minecraft/client/model/ModelTransform;)Lnet/minecraft/client/model/ModelPartData;
 *   Lnet/minecraft/client/model/ModelPartBuilder;uv(II)Lnet/minecraft/client/model/ModelPartBuilder;
 *   Lnet/minecraft/client/model/ModelPartBuilder;cuboid(FFFFFFLnet/minecraft/client/model/Dilation;)Lnet/minecraft/client/model/ModelPartBuilder;
 *   Lnet/minecraft/client/render/entity/animation/Animation;applyWalking(FFFF)V
 */
package net.minecraft.client.render.entity.model;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.Dilation;
import net.minecraft.client.model.ModelData;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.model.ModelPartBuilder;
import net.minecraft.client.model.ModelPartData;
import net.minecraft.client.model.ModelTransform;
import net.minecraft.client.model.TexturedModelData;
import net.minecraft.client.render.entity.animation.Animation;
import net.minecraft.client.render.entity.animation.NautilusAnimations;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.EntityModelPartNames;
import net.minecraft.client.render.entity.state.NautilusEntityRenderState;
import net.minecraft.util.math.MathHelper;

@Environment(value=EnvType.CLIENT)
public class NautilusEntityModel
extends EntityModel<NautilusEntityRenderState> {
    private static final float field_63546 = 2.0f;
    private static final float field_63547 = 3.0f;
    private static final float field_63548 = 0.2f;
    private static final float field_63549 = 5.0f;
    protected final ModelPart body;
    protected final ModelPart nautilusRoot;
    private final Animation animation;

    public NautilusEntityModel(ModelPart arg) {
        super(arg);
        this.nautilusRoot = arg.getChild(EntityModelPartNames.ROOT);
        this.body = this.nautilusRoot.getChild(EntityModelPartNames.BODY);
        this.animation = NautilusAnimations.ANIMATION.createAnimation(arg);
    }

    public static TexturedModelData getTexturedModelData() {
        return TexturedModelData.of(NautilusEntityModel.getModelData(), 128, 128);
    }

    public static ModelData getModelData() {
        ModelData lv = new ModelData();
        ModelPartData lv2 = lv.getRoot();
        ModelPartData lv3 = lv2.addChild(EntityModelPartNames.ROOT, ModelPartBuilder.create(), ModelTransform.origin(0.0f, 29.0f, -6.0f));
        lv3.addChild(EntityModelPartNames.SHELL, ModelPartBuilder.create().uv(0, 0).cuboid(-7.0f, -10.0f, -7.0f, 14.0f, 10.0f, 16.0f, new Dilation(0.0f)).uv(0, 26).cuboid(-7.0f, 0.0f, -7.0f, 14.0f, 8.0f, 20.0f, new Dilation(0.0f)).uv(48, 26).cuboid(-7.0f, 0.0f, 6.0f, 14.0f, 8.0f, 0.0f, new Dilation(0.0f)), ModelTransform.origin(0.0f, -13.0f, 5.0f));
        ModelPartData lv4 = lv3.addChild(EntityModelPartNames.BODY, ModelPartBuilder.create().uv(0, 54).cuboid(-5.0f, -4.51f, -3.0f, 10.0f, 8.0f, 14.0f, new Dilation(0.0f)).uv(0, 76).cuboid(-5.0f, -4.51f, 7.0f, 10.0f, 8.0f, 0.0f, new Dilation(0.0f)), ModelTransform.origin(0.0f, -8.5f, 12.3f));
        lv4.addChild(EntityModelPartNames.UPPER_MOUTH, ModelPartBuilder.create().uv(54, 54).cuboid(-5.0f, -2.0f, 0.0f, 10.0f, 4.0f, 4.0f, new Dilation(-0.001f)), ModelTransform.origin(0.0f, -2.51f, 7.0f));
        lv4.addChild(EntityModelPartNames.INNER_MOUTH, ModelPartBuilder.create().uv(54, 70).cuboid(-3.0f, -2.0f, -0.5f, 6.0f, 4.0f, 4.0f, new Dilation(0.0f)), ModelTransform.origin(0.0f, -0.51f, 7.5f));
        lv4.addChild(EntityModelPartNames.LOWER_MOUTH, ModelPartBuilder.create().uv(54, 62).cuboid(-5.0f, -1.98f, 0.0f, 10.0f, 4.0f, 4.0f, new Dilation(-0.001f)), ModelTransform.origin(0.0f, 1.49f, 7.0f));
        return lv;
    }

    public static TexturedModelData getBabyTexturedModelData() {
        ModelData lv = new ModelData();
        ModelPartData lv2 = lv.getRoot();
        ModelPartData lv3 = lv2.addChild(EntityModelPartNames.ROOT, ModelPartBuilder.create(), ModelTransform.origin(-0.5f, 28.0f, -0.5f));
        lv3.addChild(EntityModelPartNames.SHELL, ModelPartBuilder.create().uv(0, 0).cuboid(-6.0f, -4.0f, -1.0f, 7.0f, 4.0f, 7.0f, new Dilation(0.0f)).uv(0, 11).cuboid(-6.0f, 0.0f, -1.0f, 7.0f, 4.0f, 9.0f, new Dilation(0.0f)).uv(23, 11).cuboid(-6.0f, 0.0f, 5.0f, 7.0f, 4.0f, 0.0f, new Dilation(0.0f)), ModelTransform.origin(3.0f, -8.0f, -2.0f));
        ModelPartData lv4 = lv3.addChild(EntityModelPartNames.BODY, ModelPartBuilder.create().uv(0, 24).cuboid(-2.5f, -3.01f, -1.0f, 5.0f, 4.0f, 7.0f, new Dilation(0.0f)).uv(0, 35).cuboid(-2.5f, -3.01f, 4.1f, 5.0f, 4.0f, 0.0f, new Dilation(0.0f)), ModelTransform.origin(0.5f, -5.0f, 3.0f));
        lv4.addChild(EntityModelPartNames.UPPER_MOUTH, ModelPartBuilder.create().uv(24, 24).cuboid(-2.5f, -1.0f, 0.0f, 5.0f, 2.0f, 2.0f, new Dilation(-0.001f)), ModelTransform.origin(0.0f, -2.01f, 3.9f));
        lv4.addChild(EntityModelPartNames.INNER_MOUTH, ModelPartBuilder.create().uv(24, 32).cuboid(-1.5f, -1.0f, -1.0f, 3.0f, 2.0f, 2.0f, new Dilation(0.0f)), ModelTransform.origin(0.0f, -1.01f, 4.9f));
        lv4.addChild(EntityModelPartNames.LOWER_MOUTH, ModelPartBuilder.create().uv(24, 28).cuboid(-2.5f, -1.0f, 0.0f, 5.0f, 2.0f, 2.0f, new Dilation(-0.001f)), ModelTransform.origin(0.0f, -0.01f, 3.9f));
        return TexturedModelData.of(lv, 64, 64);
    }

    @Override
    public void setAngles(NautilusEntityRenderState arg) {
        super.setAngles(arg);
        this.setHeadAngles(arg.relativeHeadYaw, arg.pitch);
        this.animation.applyWalking(arg.limbSwingAnimationProgress + arg.age / 5.0f, arg.limbSwingAmplitude + 0.2f, 2.0f, 3.0f);
    }

    private void setHeadAngles(float yaw, float pitch) {
        yaw = MathHelper.clamp(yaw, -10.0f, 10.0f);
        pitch = MathHelper.clamp(pitch, -10.0f, 10.0f);
        this.body.yaw = yaw * ((float)Math.PI / 180);
        this.body.pitch = pitch * ((float)Math.PI / 180);
    }
}

