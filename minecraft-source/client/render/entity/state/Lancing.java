/*
 * External method calls:
 *   Lnet/minecraft/client/render/entity/state/Lancing$class_12153;method_75397(Lnet/minecraft/component/type/KineticWeaponComponent;F)Lnet/minecraft/client/render/entity/state/Lancing$class_12153;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/client/render/entity/state/Lancing;method_75390(FFF)F
 *   Lnet/minecraft/client/render/entity/state/Lancing;method_75916(F)F
 */
package net.minecraft.client.render.entity.state;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.state.ArmedEntityRenderState;
import net.minecraft.client.render.entity.state.BipedEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.KineticWeaponComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Arm;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Easing;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;

@Environment(value=EnvType.CLIENT)
public class Lancing {
    static float method_75390(float f, float g, float h) {
        return MathHelper.clamp(MathHelper.getLerpProgress(f, g, h), 0.0f, 1.0f);
    }

    public static <T extends BipedEntityRenderState> void positionArmForSpear(ModelPart arm, ModelPart head, boolean right, ItemStack itemStack, T state) {
        int i = right ? 1 : -1;
        arm.yaw = -0.1f * (float)i + head.yaw;
        arm.pitch = -1.5707964f + head.pitch + 0.8f;
        if (state.isGliding || state.leaningPitch > 0.0f) {
            arm.pitch -= 0.9599311f;
        }
        arm.yaw = (float)Math.PI / 180 * Math.clamp(57.295776f * arm.yaw, -60.0f, 60.0f);
        arm.pitch = (float)Math.PI / 180 * Math.clamp(57.295776f * arm.pitch, -120.0f, 30.0f);
        if (state.itemUseTime <= 0.0f || state.isUsingItem && state.activeHand != (right ? Hand.MAIN_HAND : Hand.OFF_HAND)) {
            return;
        }
        KineticWeaponComponent lv = itemStack.get(DataComponentTypes.KINETIC_WEAPON);
        if (lv == null) {
            return;
        }
        class_12153 lv2 = class_12153.method_75397(lv, state.itemUseTime);
        arm.yaw += (float)(-i) * lv2.swayScaleFast() * ((float)Math.PI / 180) * lv2.swayIntensity() * 1.0f;
        arm.roll += (float)(-i) * lv2.swayScaleSlow() * ((float)Math.PI / 180) * lv2.swayIntensity() * 0.5f;
        arm.pitch += (float)Math.PI / 180 * (-40.0f * lv2.raiseProgressStart() + 30.0f * lv2.raiseProgressMiddle() + -20.0f * lv2.raiseProgressEnd() + 20.0f * lv2.lowerProgress() + 10.0f * lv2.raiseBackProgress() + 0.6f * lv2.swayScaleSlow() * lv2.swayIntensity());
    }

    public static <S extends ArmedEntityRenderState> void method_75392(S arg, MatrixStack arg2, float f, Arm arg3, ItemStack arg4) {
        KineticWeaponComponent lv = arg4.get(DataComponentTypes.KINETIC_WEAPON);
        if (lv == null || f == 0.0f) {
            return;
        }
        float g = Easing.inQuad(Lancing.method_75390(arg.handSwingProgress, 0.05f, 0.2f));
        float h = Easing.inOutExpo(Lancing.method_75390(arg.handSwingProgress, 0.4f, 1.0f));
        class_12153 lv2 = class_12153.method_75397(lv, f);
        int i = arg3 == Arm.RIGHT ? 1 : -1;
        float j = 1.0f - Easing.outBack(1.0f - lv2.raiseProgress());
        float k = 0.125f;
        float l = Lancing.method_75916(arg.timeSinceLastKineticAttack);
        arg2.translate(0.0, (double)(-l) * 0.4, (double)(-lv.forwardMovement() * (j - lv2.raiseBackProgress()) + l));
        arg2.multiply(RotationAxis.NEGATIVE_X.rotationDegrees(70.0f * (lv2.raiseProgress() - lv2.raiseBackProgress()) - 40.0f * (g - h)), 0.0f, -0.03125f, 0.125f);
        arg2.multiply(RotationAxis.POSITIVE_Y.rotationDegrees((float)(i * 90) * (lv2.raiseProgress() - lv2.swayProgress() + 3.0f * h + g)), 0.0f, 0.0f, 0.125f);
    }

    public static <T extends BipedEntityRenderState> void method_75393(BipedEntityModel<T> arg, T arg2) {
        float f = arg2.handSwingProgress;
        Arm lv = arg2.preferredArm;
        arg.rightArm.yaw -= arg.body.yaw;
        arg.leftArm.yaw -= arg.body.yaw;
        arg.leftArm.pitch -= arg.body.yaw;
        float g = Easing.inOutSine(Lancing.method_75390(f, 0.0f, 0.05f));
        float h = Easing.inQuad(Lancing.method_75390(f, 0.05f, 0.2f));
        float i = Easing.inOutExpo(Lancing.method_75390(f, 0.4f, 1.0f));
        arg.getArm((Arm)lv).pitch += (90.0f * g - 120.0f * h + 30.0f * i) * ((float)Math.PI / 180);
    }

    public static <S extends ArmedEntityRenderState> void method_75395(S arg, MatrixStack arg2) {
        if (arg.handSwingProgress <= 0.0f) {
            return;
        }
        KineticWeaponComponent lv = arg.getMainHandItemStack().get(DataComponentTypes.KINETIC_WEAPON);
        float f = lv != null ? lv.forwardMovement() : 0.0f;
        float g = 0.125f;
        float h = arg.handSwingProgress;
        float i = Easing.inQuad(Lancing.method_75390(h, 0.05f, 0.2f));
        float j = Easing.inOutExpo(Lancing.method_75390(h, 0.4f, 1.0f));
        arg2.multiply(RotationAxis.NEGATIVE_X.rotationDegrees(70.0f * (i - j)), 0.0f, -0.125f, 0.125f);
        arg2.translate(0.0f, f * (i - j), 0.0f);
    }

    private static float method_75916(float f) {
        return 0.4f * (Easing.outQuart(Lancing.method_75390(f, 1.0f, 3.0f)) - Easing.inOutSine(Lancing.method_75390(f, 3.0f, 10.0f)));
    }

    public static void method_75396(float f, MatrixStack arg, float g, Arm arg2, ItemStack arg3) {
        KineticWeaponComponent lv = arg3.get(DataComponentTypes.KINETIC_WEAPON);
        if (lv == null) {
            return;
        }
        class_12153 lv2 = class_12153.method_75397(lv, g);
        int i = arg2 == Arm.RIGHT ? 1 : -1;
        arg.translate((double)((float)i * (lv2.raiseProgress() * 0.15f + lv2.raiseProgressEnd() * -0.05f + lv2.swayProgress() * -0.1f + lv2.swayScaleSlow() * 0.005f)), (double)(lv2.raiseProgress() * -0.075f + lv2.raiseProgressMiddle() * 0.075f + lv2.swayScaleFast() * 0.01f), (double)lv2.raiseProgressStart() * 0.05 + (double)lv2.raiseProgressEnd() * -0.05 + (double)(lv2.swayScaleSlow() * 0.005f));
        arg.multiply(RotationAxis.POSITIVE_X.rotationDegrees(-65.0f * Easing.inOutBack(lv2.raiseProgress()) - 35.0f * lv2.lowerProgress() + 100.0f * lv2.raiseBackProgress() + -0.5f * lv2.swayScaleFast()), 0.0f, 0.1f, 0.0f);
        arg.multiply(RotationAxis.NEGATIVE_Y.rotationDegrees((float)i * (-90.0f * Lancing.method_75390(lv2.raiseProgress(), 0.5f, 0.55f) + 90.0f * lv2.swayProgress() + 2.0f * lv2.swayScaleSlow())), (float)i * 0.15f, 0.0f, 0.0f);
        arg.translate(0.0f, -Lancing.method_75916(f), 0.0f);
    }

    public static void method_75391(float f, MatrixStack arg, int i, Arm arg2) {
        float g = Easing.inOutSine(Lancing.method_75390(f, 0.0f, 0.05f));
        float h = Easing.outBack(Lancing.method_75390(f, 0.05f, 0.2f));
        float j = Easing.inOutExpo(Lancing.method_75390(f, 0.4f, 1.0f));
        arg.translate((float)i * 0.1f * (g - h), -0.075f * (g - j), 0.65f * (g - h));
        arg.multiply(RotationAxis.POSITIVE_X.rotationDegrees(-70.0f * (g - j)));
        arg.translate(0.0, 0.0, -0.25 * (double)(j - h));
    }

    @Environment(value=EnvType.CLIENT)
    record class_12153(float raiseProgress, float raiseProgressStart, float raiseProgressMiddle, float raiseProgressEnd, float swayProgress, float lowerProgress, float raiseBackProgress, float swayIntensity, float swayScaleSlow, float swayScaleFast) {
        public static class_12153 method_75397(KineticWeaponComponent arg, float f) {
            int i = arg.delayTicks();
            int j = arg.dismountConditions().map(KineticWeaponComponent.Condition::maxDurationTicks).orElse(0) + i;
            int k = j - 20;
            int l = arg.knockbackConditions().map(KineticWeaponComponent.Condition::maxDurationTicks).orElse(0) + i;
            int m = l - 40;
            int n = arg.damageConditions().map(KineticWeaponComponent.Condition::maxDurationTicks).orElse(0) + i;
            float g = Lancing.method_75390(f, 0.0f, i);
            float h = Lancing.method_75390(g, 0.0f, 0.5f);
            float o = Lancing.method_75390(g, 0.5f, 0.8f);
            float p = Lancing.method_75390(g, 0.8f, 1.0f);
            float q = Lancing.method_75390(f, k, m);
            float r = Easing.outCubic(Easing.inOutElastic(Lancing.method_75390(f - 20.0f, m, l)));
            float s = Lancing.method_75390(f, n - 5, n);
            float t = 2.0f * Easing.outCirc(q) - 2.0f * Easing.inCirc(s);
            float u = MathHelper.sin(f * 19.0f * ((float)Math.PI / 180)) * t;
            float v = MathHelper.sin(f * 30.0f * ((float)Math.PI / 180)) * t;
            return new class_12153(g, h, o, p, q, r, s, t, u, v);
        }
    }
}

