/*
 * External method calls:
 *   Lnet/minecraft/client/model/ModelPartBuilder;create()Lnet/minecraft/client/model/ModelPartBuilder;
 *   Lnet/minecraft/client/model/ModelPartBuilder;uv(II)Lnet/minecraft/client/model/ModelPartBuilder;
 *   Lnet/minecraft/client/model/ModelPartBuilder;cuboid(FFFFFFLnet/minecraft/client/model/Dilation;)Lnet/minecraft/client/model/ModelPartBuilder;
 *   Lnet/minecraft/client/model/ModelTransform;of(FFFFFF)Lnet/minecraft/client/model/ModelTransform;
 *   Lnet/minecraft/client/model/ModelPartData;addChild(Ljava/lang/String;Lnet/minecraft/client/model/ModelPartBuilder;Lnet/minecraft/client/model/ModelTransform;)Lnet/minecraft/client/model/ModelPartData;
 *   Lnet/minecraft/client/model/ModelPartBuilder;mirrored()Lnet/minecraft/client/model/ModelPartBuilder;
 *   Lnet/minecraft/client/model/TexturedModelData;of(Lnet/minecraft/client/model/ModelData;II)Lnet/minecraft/client/model/TexturedModelData;
 *   Lnet/minecraft/client/render/entity/model/ModelTransformer;scaling(F)Lnet/minecraft/client/render/entity/model/ModelTransformer;
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
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.EntityModelPartNames;
import net.minecraft.client.render.entity.model.ModelTransformer;
import net.minecraft.client.render.entity.state.BipedEntityRenderState;

@Environment(value=EnvType.CLIENT)
public class ElytraEntityModel
extends EntityModel<BipedEntityRenderState> {
    public static final ModelTransformer BABY_TRANSFORMER = ModelTransformer.scaling(0.5f);
    private final ModelPart rightWing;
    private final ModelPart leftWing;

    public ElytraEntityModel(ModelPart arg) {
        super(arg);
        this.leftWing = arg.getChild(EntityModelPartNames.LEFT_WING);
        this.rightWing = arg.getChild(EntityModelPartNames.RIGHT_WING);
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData lv = new ModelData();
        ModelPartData lv2 = lv.getRoot();
        Dilation lv3 = new Dilation(1.0f);
        lv2.addChild(EntityModelPartNames.LEFT_WING, ModelPartBuilder.create().uv(22, 0).cuboid(-10.0f, 0.0f, 0.0f, 10.0f, 20.0f, 2.0f, lv3), ModelTransform.of(5.0f, 0.0f, 0.0f, 0.2617994f, 0.0f, -0.2617994f));
        lv2.addChild(EntityModelPartNames.RIGHT_WING, ModelPartBuilder.create().uv(22, 0).mirrored().cuboid(0.0f, 0.0f, 0.0f, 10.0f, 20.0f, 2.0f, lv3), ModelTransform.of(-5.0f, 0.0f, 0.0f, 0.2617994f, 0.0f, 0.2617994f));
        return TexturedModelData.of(lv, 64, 32);
    }

    @Override
    public void setAngles(BipedEntityRenderState arg) {
        super.setAngles(arg);
        this.leftWing.originY = arg.isInSneakingPose ? 3.0f : 0.0f;
        this.leftWing.pitch = arg.leftWingPitch;
        this.leftWing.roll = arg.leftWingRoll;
        this.leftWing.yaw = arg.leftWingYaw;
        this.rightWing.yaw = -this.leftWing.yaw;
        this.rightWing.originY = this.leftWing.originY;
        this.rightWing.pitch = this.leftWing.pitch;
        this.rightWing.roll = -this.leftWing.roll;
    }
}

