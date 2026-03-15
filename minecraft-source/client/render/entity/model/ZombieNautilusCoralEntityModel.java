/*
 * External method calls:
 *   Lnet/minecraft/client/model/ModelPartBuilder;create()Lnet/minecraft/client/model/ModelPartBuilder;
 *   Lnet/minecraft/client/model/ModelTransform;origin(FFF)Lnet/minecraft/client/model/ModelTransform;
 *   Lnet/minecraft/client/model/ModelPartData;addChild(Ljava/lang/String;Lnet/minecraft/client/model/ModelPartBuilder;Lnet/minecraft/client/model/ModelTransform;)Lnet/minecraft/client/model/ModelPartData;
 *   Lnet/minecraft/client/model/ModelPartBuilder;uv(II)Lnet/minecraft/client/model/ModelPartBuilder;
 *   Lnet/minecraft/client/model/ModelPartBuilder;cuboid(FFFFFF)Lnet/minecraft/client/model/ModelPartBuilder;
 *   Lnet/minecraft/client/model/ModelTransform;of(FFFFFF)Lnet/minecraft/client/model/ModelTransform;
 *   Lnet/minecraft/client/model/TexturedModelData;of(Lnet/minecraft/client/model/ModelData;II)Lnet/minecraft/client/model/TexturedModelData;
 */
package net.minecraft.client.render.entity.model;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.ModelData;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.model.ModelPartBuilder;
import net.minecraft.client.model.ModelPartData;
import net.minecraft.client.model.ModelTransform;
import net.minecraft.client.model.TexturedModelData;
import net.minecraft.client.render.entity.model.EntityModelPartNames;
import net.minecraft.client.render.entity.model.NautilusEntityModel;
import net.minecraft.client.render.entity.state.NautilusEntityRenderState;

@Environment(value=EnvType.CLIENT)
public class ZombieNautilusCoralEntityModel
extends NautilusEntityModel {
    private final ModelPart corals;

    public ZombieNautilusCoralEntityModel(ModelPart arg) {
        super(arg);
        ModelPart lv = this.nautilusRoot.getChild(EntityModelPartNames.SHELL);
        this.corals = lv.getChild("corals");
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData lv = ZombieNautilusCoralEntityModel.getModelData();
        ModelPartData lv2 = lv.getRoot().getChild(EntityModelPartNames.ROOT).getChild(EntityModelPartNames.SHELL).addChild("corals", ModelPartBuilder.create(), ModelTransform.origin(8.0f, 4.5f, -8.0f));
        ModelPartData lv3 = lv2.addChild(EntityModelPartNames.YELLOW_CORAL, ModelPartBuilder.create(), ModelTransform.origin(0.0f, -11.0f, 11.0f));
        lv3.addChild(EntityModelPartNames.YELLOW_CORAL_SECOND, ModelPartBuilder.create().uv(0, 85).cuboid(-4.5f, -3.5f, 0.0f, 6.0f, 8.0f, 0.0f), ModelTransform.of(0.0f, 0.0f, 2.0f, 0.0f, -0.7854f, 0.0f));
        lv3.addChild(EntityModelPartNames.YELLOW_CORAL_FIRST, ModelPartBuilder.create().uv(0, 85).cuboid(-4.5f, -3.5f, 0.0f, 6.0f, 8.0f, 0.0f), ModelTransform.of(0.0f, 0.0f, 0.0f, 0.0f, 0.7854f, 0.0f));
        ModelPartData lv4 = lv2.addChild(EntityModelPartNames.PINK_CORAL, ModelPartBuilder.create().uv(-8, 94).cuboid(-4.5f, 4.5f, 0.0f, 6.0f, 0.0f, 8.0f), ModelTransform.origin(-12.5f, -18.0f, 11.0f));
        lv4.addChild(EntityModelPartNames.PINK_CORAL_SECOND, ModelPartBuilder.create().uv(-8, 94).cuboid(-3.0f, 0.0f, -4.0f, 6.0f, 0.0f, 8.0f), ModelTransform.of(-1.5f, 4.5f, 4.0f, 0.0f, 0.0f, 1.5708f));
        ModelPartData lv5 = lv2.addChild(EntityModelPartNames.BLUE_CORAL, ModelPartBuilder.create(), ModelTransform.origin(-14.0f, 0.0f, 5.5f));
        lv5.addChild(EntityModelPartNames.BLUE_CORAL_SECOND, ModelPartBuilder.create().uv(0, 102).cuboid(-3.5f, -5.5f, 0.0f, 5.0f, 10.0f, 0.0f), ModelTransform.of(0.0f, 0.0f, -2.0f, 0.0f, 0.7854f, 0.0f));
        lv5.addChild(EntityModelPartNames.BLUE_CORAL_FIRST, ModelPartBuilder.create().uv(0, 102).cuboid(-3.5f, -5.5f, 0.0f, 5.0f, 10.0f, 0.0f), ModelTransform.of(0.0f, 0.0f, 0.0f, 0.0f, -0.7854f, 0.0f));
        ModelPartData lv6 = lv2.addChild(EntityModelPartNames.RED_CORAL, ModelPartBuilder.create(), ModelTransform.origin(0.0f, 0.0f, 0.0f));
        lv6.addChild(EntityModelPartNames.RED_CORAL_SECOND, ModelPartBuilder.create().uv(0, 112).cuboid(-2.5f, -5.5f, 0.0f, 4.0f, 10.0f, 0.0f), ModelTransform.of(-0.5f, -1.0f, 1.5f, 0.0f, -0.829f, 0.0f));
        lv6.addChild(EntityModelPartNames.RED_CORAL_FIRST, ModelPartBuilder.create().uv(0, 112).cuboid(-4.5f, -5.5f, 0.0f, 6.0f, 10.0f, 0.0f), ModelTransform.of(0.0f, 0.0f, 0.0f, 0.0f, 0.7854f, 0.0f));
        return TexturedModelData.of(lv, 128, 128);
    }

    @Override
    public void setAngles(NautilusEntityRenderState arg) {
        super.setAngles(arg);
        this.corals.visible = arg.armorStack.isEmpty();
    }
}

