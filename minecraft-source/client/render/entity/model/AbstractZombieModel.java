/*
 * External method calls:
 *   Lnet/minecraft/client/render/entity/model/ArmPosing;zombieArms(Lnet/minecraft/client/model/ModelPart;Lnet/minecraft/client/model/ModelPart;ZLnet/minecraft/client/render/entity/state/LancerEntityRenderState;)V
 */
package net.minecraft.client.render.entity.model;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.ArmPosing;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.state.ZombieEntityRenderState;

@Environment(value=EnvType.CLIENT)
public abstract class AbstractZombieModel<S extends ZombieEntityRenderState>
extends BipedEntityModel<S> {
    protected AbstractZombieModel(ModelPart arg) {
        super(arg);
    }

    @Override
    public void setAngles(S arg) {
        super.setAngles(arg);
        ArmPosing.zombieArms(this.leftArm, this.rightArm, ((ZombieEntityRenderState)arg).attacking, arg);
    }
}

