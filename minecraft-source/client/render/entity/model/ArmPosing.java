/*
 * Internal private/static methods:
 *   Lnet/minecraft/client/render/entity/model/ArmPosing;swingArms(Lnet/minecraft/client/model/ModelPart;Lnet/minecraft/client/model/ModelPart;F)V
 *   Lnet/minecraft/client/render/entity/model/ArmPosing;swingArm(Lnet/minecraft/client/model/ModelPart;FF)V
 */
package net.minecraft.client.render.entity.model;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.state.LancerEntityRenderState;
import net.minecraft.util.Arm;
import net.minecraft.util.SwingAnimationType;
import net.minecraft.util.math.MathHelper;

@Environment(value=EnvType.CLIENT)
public class ArmPosing {
    public static void hold(ModelPart holdingArm, ModelPart otherArm, ModelPart head, boolean rightArm) {
        ModelPart lv = rightArm ? holdingArm : otherArm;
        ModelPart lv2 = rightArm ? otherArm : holdingArm;
        lv.yaw = (rightArm ? -0.3f : 0.3f) + head.yaw;
        lv2.yaw = (rightArm ? 0.6f : -0.6f) + head.yaw;
        lv.pitch = -1.5707964f + head.pitch + 0.1f;
        lv2.pitch = -1.5f + head.pitch;
    }

    public static void charge(ModelPart holdingArm, ModelPart pullingArm, float crossbowPullTime, float g, boolean rightArm) {
        ModelPart lv = rightArm ? holdingArm : pullingArm;
        ModelPart lv2 = rightArm ? pullingArm : holdingArm;
        lv.yaw = rightArm ? -0.8f : 0.8f;
        lv2.pitch = lv.pitch = -0.97079635f;
        float h = MathHelper.clamp(g, 0.0f, crossbowPullTime);
        float i = h / crossbowPullTime;
        lv2.yaw = MathHelper.lerp(i, 0.4f, 0.85f) * (float)(rightArm ? 1 : -1);
        lv2.pitch = MathHelper.lerp(i, lv2.pitch, -1.5707964f);
    }

    public static void meleeAttack(ModelPart rightArm, ModelPart leftArm, Arm mainArm, float swingProgress, float animationProgress) {
        float h = MathHelper.sin(swingProgress * (float)Math.PI);
        float i = MathHelper.sin((1.0f - (1.0f - swingProgress) * (1.0f - swingProgress)) * (float)Math.PI);
        rightArm.roll = 0.0f;
        leftArm.roll = 0.0f;
        rightArm.yaw = 0.15707964f;
        leftArm.yaw = -0.15707964f;
        if (mainArm == Arm.RIGHT) {
            rightArm.pitch = -1.8849558f + MathHelper.cos(animationProgress * 0.09f) * 0.15f;
            leftArm.pitch = -0.0f + MathHelper.cos(animationProgress * 0.19f) * 0.5f;
            rightArm.pitch += h * 2.2f - i * 0.4f;
            leftArm.pitch += h * 1.2f - i * 0.4f;
        } else {
            rightArm.pitch = -0.0f + MathHelper.cos(animationProgress * 0.19f) * 0.5f;
            leftArm.pitch = -1.8849558f + MathHelper.cos(animationProgress * 0.09f) * 0.15f;
            rightArm.pitch += h * 1.2f - i * 0.4f;
            leftArm.pitch += h * 2.2f - i * 0.4f;
        }
        ArmPosing.swingArms(rightArm, leftArm, animationProgress);
    }

    public static void swingArm(ModelPart arm, float animationProgress, float sigma) {
        arm.roll += sigma * (MathHelper.cos(animationProgress * 0.09f) * 0.05f + 0.05f);
        arm.pitch += sigma * (MathHelper.sin(animationProgress * 0.067f) * 0.05f);
    }

    public static void swingArms(ModelPart rightArm, ModelPart leftArm, float animationProgress) {
        ArmPosing.swingArm(rightArm, animationProgress, 1.0f);
        ArmPosing.swingArm(leftArm, animationProgress, -1.0f);
    }

    public static <T extends LancerEntityRenderState> void zombieArms(ModelPart leftArm, ModelPart rightArm, boolean attacking, T arg3) {
        boolean bl2;
        boolean bl = bl2 = arg3.swingAnimationType != SwingAnimationType.STAB;
        if (bl2) {
            float f = arg3.handSwingProgress;
            float g = (float)(-Math.PI) / (attacking ? 1.5f : 2.25f);
            float h = MathHelper.sin(f * (float)Math.PI);
            float i = MathHelper.sin((1.0f - (1.0f - f) * (1.0f - f)) * (float)Math.PI);
            rightArm.roll = 0.0f;
            rightArm.yaw = -(0.1f - h * 0.6f);
            rightArm.pitch = g;
            rightArm.pitch += h * 1.2f - i * 0.4f;
            leftArm.roll = 0.0f;
            leftArm.yaw = 0.1f - h * 0.6f;
            leftArm.pitch = g;
            leftArm.pitch += h * 1.2f - i * 0.4f;
        }
        ArmPosing.swingArms(rightArm, leftArm, arg3.age);
    }
}

