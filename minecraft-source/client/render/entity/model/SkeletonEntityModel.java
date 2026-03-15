/*
 * External method calls:
 *   Lnet/minecraft/client/model/TexturedModelData;of(Lnet/minecraft/client/model/ModelData;II)Lnet/minecraft/client/model/TexturedModelData;
 *   Lnet/minecraft/client/model/ModelPartBuilder;create()Lnet/minecraft/client/model/ModelPartBuilder;
 *   Lnet/minecraft/client/model/ModelPartBuilder;uv(II)Lnet/minecraft/client/model/ModelPartBuilder;
 *   Lnet/minecraft/client/model/ModelPartBuilder;cuboid(FFFFFF)Lnet/minecraft/client/model/ModelPartBuilder;
 *   Lnet/minecraft/client/model/ModelTransform;origin(FFF)Lnet/minecraft/client/model/ModelTransform;
 *   Lnet/minecraft/client/model/ModelPartData;addChild(Ljava/lang/String;Lnet/minecraft/client/model/ModelPartBuilder;Lnet/minecraft/client/model/ModelTransform;)Lnet/minecraft/client/model/ModelPartData;
 *   Lnet/minecraft/client/model/ModelPartBuilder;mirrored()Lnet/minecraft/client/model/ModelPartBuilder;
 *   Lnet/minecraft/client/model/ModelPartBuilder;cuboid(FFFFFFLnet/minecraft/client/model/Dilation;)Lnet/minecraft/client/model/ModelPartBuilder;
 *   Lnet/minecraft/client/render/entity/model/ArmPosing;swingArms(Lnet/minecraft/client/model/ModelPart;Lnet/minecraft/client/model/ModelPart;F)V
 *   Lnet/minecraft/client/model/ModelPart;applyTransform(Lnet/minecraft/client/util/math/MatrixStack;)V
 *
 * Internal private/static methods:
 *   Lnet/minecraft/client/render/entity/model/SkeletonEntityModel;addLimbs(Lnet/minecraft/client/model/ModelPartData;)V
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
import net.minecraft.client.render.entity.model.ArmPosing;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.EntityModelPartNames;
import net.minecraft.client.render.entity.state.SkeletonEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Arm;
import net.minecraft.util.math.MathHelper;

@Environment(value=EnvType.CLIENT)
public class SkeletonEntityModel<S extends SkeletonEntityRenderState>
extends BipedEntityModel<S> {
    public SkeletonEntityModel(ModelPart arg) {
        super(arg);
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData lv = BipedEntityModel.getModelData(Dilation.NONE, 0.0f);
        ModelPartData lv2 = lv.getRoot();
        SkeletonEntityModel.addLimbs(lv2);
        return TexturedModelData.of(lv, 64, 32);
    }

    protected static void addLimbs(ModelPartData data) {
        data.addChild(EntityModelPartNames.RIGHT_ARM, ModelPartBuilder.create().uv(40, 16).cuboid(-1.0f, -2.0f, -1.0f, 2.0f, 12.0f, 2.0f), ModelTransform.origin(-5.0f, 2.0f, 0.0f));
        data.addChild(EntityModelPartNames.LEFT_ARM, ModelPartBuilder.create().uv(40, 16).mirrored().cuboid(-1.0f, -2.0f, -1.0f, 2.0f, 12.0f, 2.0f), ModelTransform.origin(5.0f, 2.0f, 0.0f));
        data.addChild(EntityModelPartNames.RIGHT_LEG, ModelPartBuilder.create().uv(0, 16).cuboid(-1.0f, 0.0f, -1.0f, 2.0f, 12.0f, 2.0f), ModelTransform.origin(-2.0f, 12.0f, 0.0f));
        data.addChild(EntityModelPartNames.LEFT_LEG, ModelPartBuilder.create().uv(0, 16).mirrored().cuboid(-1.0f, 0.0f, -1.0f, 2.0f, 12.0f, 2.0f), ModelTransform.origin(2.0f, 12.0f, 0.0f));
    }

    public static TexturedModelData getParchedTexturedModelData() {
        ModelData lv = new ModelData();
        ModelPartData lv2 = lv.getRoot();
        lv2.addChild(EntityModelPartNames.BODY, ModelPartBuilder.create().uv(16, 16).cuboid(-4.0f, 0.0f, -2.0f, 8.0f, 12.0f, 4.0f).uv(28, 0).cuboid(-4.0f, 10.0f, -2.0f, 8.0f, 1.0f, 4.0f).uv(16, 48).cuboid(-4.0f, 0.0f, -2.0f, 8.0f, 12.0f, 4.0f, new Dilation(0.025f)), ModelTransform.origin(0.0f, 0.0f, 0.0f));
        lv2.addChild(EntityModelPartNames.HEAD, ModelPartBuilder.create().uv(0, 0).cuboid(-4.0f, -8.0f, -4.0f, 8.0f, 8.0f, 8.0f).uv(0, 32).cuboid(-4.0f, -8.0f, -4.0f, 8.0f, 8.0f, 8.0f, new Dilation(0.2f)), ModelTransform.origin(0.0f, 0.0f, 0.0f)).addChild(EntityModelPartNames.HAT, ModelPartBuilder.create(), ModelTransform.NONE);
        lv2.addChild(EntityModelPartNames.RIGHT_ARM, ModelPartBuilder.create().uv(40, 16).cuboid(-1.0f, -2.0f, -1.0f, 2.0f, 12.0f, 2.0f).uv(42, 33).cuboid(-1.55f, -2.025f, -1.5f, 3.0f, 12.0f, 3.0f), ModelTransform.origin(-5.5f, 2.0f, 0.0f));
        lv2.addChild(EntityModelPartNames.LEFT_ARM, ModelPartBuilder.create().uv(56, 16).cuboid(-1.0f, -2.0f, -1.0f, 2.0f, 12.0f, 2.0f).uv(40, 48).cuboid(-1.45f, -2.025f, -1.5f, 3.0f, 12.0f, 3.0f), ModelTransform.origin(5.5f, 2.0f, 0.0f));
        lv2.addChild(EntityModelPartNames.RIGHT_LEG, ModelPartBuilder.create().uv(0, 16).cuboid(-1.0f, 0.0f, -1.0f, 2.0f, 12.0f, 2.0f).uv(0, 49).cuboid(-1.5f, -0.0f, -1.5f, 3.0f, 12.0f, 3.0f), ModelTransform.origin(-2.0f, 12.0f, 0.0f));
        lv2.addChild(EntityModelPartNames.LEFT_LEG, ModelPartBuilder.create().uv(0, 16).cuboid(-1.0f, 0.0f, -1.0f, 2.0f, 12.0f, 2.0f).uv(4, 49).cuboid(-1.5f, 0.0f, -1.5f, 3.0f, 12.0f, 3.0f), ModelTransform.origin(2.0f, 12.0f, 0.0f));
        return TexturedModelData.of(lv, 64, 64);
    }

    @Override
    public void setAngles(S arg) {
        super.setAngles(arg);
        if (((SkeletonEntityRenderState)arg).attacking && !((SkeletonEntityRenderState)arg).holdingBow) {
            float f = ((SkeletonEntityRenderState)arg).handSwingProgress;
            float g = MathHelper.sin(f * (float)Math.PI);
            float h = MathHelper.sin((1.0f - (1.0f - f) * (1.0f - f)) * (float)Math.PI);
            this.rightArm.roll = 0.0f;
            this.leftArm.roll = 0.0f;
            this.rightArm.yaw = -(0.1f - g * 0.6f);
            this.leftArm.yaw = 0.1f - g * 0.6f;
            this.rightArm.pitch = -1.5707964f;
            this.leftArm.pitch = -1.5707964f;
            this.rightArm.pitch -= g * 1.2f - h * 0.4f;
            this.leftArm.pitch -= g * 1.2f - h * 0.4f;
            ArmPosing.swingArms(this.rightArm, this.leftArm, ((SkeletonEntityRenderState)arg).age);
        }
    }

    @Override
    public void setArmAngle(SkeletonEntityRenderState arg, Arm arg2, MatrixStack arg3) {
        this.getRootPart().applyTransform(arg3);
        float f = arg2 == Arm.RIGHT ? 1.0f : -1.0f;
        ModelPart lv = this.getArm(arg2);
        lv.originX += f;
        lv.applyTransform(arg3);
        lv.originX -= f;
    }
}

