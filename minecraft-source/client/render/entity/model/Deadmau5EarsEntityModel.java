/*
 * External method calls:
 *   Lnet/minecraft/client/model/ModelPartBuilder;create()Lnet/minecraft/client/model/ModelPartBuilder;
 *   Lnet/minecraft/client/model/ModelPartBuilder;uv(II)Lnet/minecraft/client/model/ModelPartBuilder;
 *   Lnet/minecraft/client/model/ModelPartBuilder;cuboid(FFFFFFLnet/minecraft/client/model/Dilation;)Lnet/minecraft/client/model/ModelPartBuilder;
 *   Lnet/minecraft/client/model/ModelTransform;origin(FFF)Lnet/minecraft/client/model/ModelTransform;
 *   Lnet/minecraft/client/model/ModelPartData;addChild(Ljava/lang/String;Lnet/minecraft/client/model/ModelPartBuilder;Lnet/minecraft/client/model/ModelTransform;)Lnet/minecraft/client/model/ModelPartData;
 *   Lnet/minecraft/client/model/TexturedModelData;of(Lnet/minecraft/client/model/ModelData;II)Lnet/minecraft/client/model/TexturedModelData;
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
import net.minecraft.client.render.entity.model.EntityModelPartNames;
import net.minecraft.client.render.entity.model.PlayerEntityModel;

@Environment(value=EnvType.CLIENT)
public class Deadmau5EarsEntityModel
extends PlayerEntityModel {
    public Deadmau5EarsEntityModel(ModelPart arg) {
        super(arg, false);
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData lv = PlayerEntityModel.getTexturedModelData(Dilation.NONE, false);
        ModelPartData lv2 = lv.getRoot().resetChildrenParts();
        ModelPartData lv3 = lv2.getChild(EntityModelPartNames.HEAD);
        ModelPartBuilder lv4 = ModelPartBuilder.create().uv(24, 0).cuboid(-3.0f, -6.0f, -1.0f, 6.0f, 6.0f, 1.0f, new Dilation(1.0f));
        lv3.addChild(EntityModelPartNames.LEFT_EAR, lv4, ModelTransform.origin(-6.0f, -6.0f, 0.0f));
        lv3.addChild(EntityModelPartNames.RIGHT_EAR, lv4, ModelTransform.origin(6.0f, -6.0f, 0.0f));
        return TexturedModelData.of(lv, 64, 64);
    }
}

