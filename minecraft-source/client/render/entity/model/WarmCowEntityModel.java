/*
 * External method calls:
 *   Lnet/minecraft/client/model/ModelPartBuilder;create()Lnet/minecraft/client/model/ModelPartBuilder;
 *   Lnet/minecraft/client/model/ModelPartBuilder;uv(II)Lnet/minecraft/client/model/ModelPartBuilder;
 *   Lnet/minecraft/client/model/ModelPartBuilder;cuboid(FFFFFF)Lnet/minecraft/client/model/ModelPartBuilder;
 *   Lnet/minecraft/client/model/ModelPartBuilder;mirrored()Lnet/minecraft/client/model/ModelPartBuilder;
 *   Lnet/minecraft/client/model/ModelPartBuilder;mirrored(Z)Lnet/minecraft/client/model/ModelPartBuilder;
 *   Lnet/minecraft/client/model/ModelTransform;origin(FFF)Lnet/minecraft/client/model/ModelTransform;
 *   Lnet/minecraft/client/model/ModelPartData;addChild(Ljava/lang/String;Lnet/minecraft/client/model/ModelPartBuilder;Lnet/minecraft/client/model/ModelTransform;)Lnet/minecraft/client/model/ModelPartData;
 *   Lnet/minecraft/client/model/TexturedModelData;of(Lnet/minecraft/client/model/ModelData;II)Lnet/minecraft/client/model/TexturedModelData;
 */
package net.minecraft.client.render.entity.model;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.ModelData;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.model.ModelPartBuilder;
import net.minecraft.client.model.ModelTransform;
import net.minecraft.client.model.TexturedModelData;
import net.minecraft.client.render.entity.model.CowEntityModel;
import net.minecraft.client.render.entity.model.EntityModelPartNames;

@Environment(value=EnvType.CLIENT)
public class WarmCowEntityModel
extends CowEntityModel {
    public WarmCowEntityModel(ModelPart arg) {
        super(arg);
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData lv = WarmCowEntityModel.getModelData();
        lv.getRoot().addChild(EntityModelPartNames.HEAD, ModelPartBuilder.create().uv(0, 0).cuboid(-4.0f, -4.0f, -6.0f, 8.0f, 8.0f, 6.0f).uv(1, 33).cuboid(-3.0f, 1.0f, -7.0f, 6.0f, 3.0f, 1.0f).uv(27, 0).cuboid(-8.0f, -3.0f, -5.0f, 4.0f, 2.0f, 2.0f).uv(39, 0).cuboid(-8.0f, -5.0f, -5.0f, 2.0f, 2.0f, 2.0f).uv(27, 0).mirrored().cuboid(4.0f, -3.0f, -5.0f, 4.0f, 2.0f, 2.0f).mirrored(false).uv(39, 0).mirrored().cuboid(6.0f, -5.0f, -5.0f, 2.0f, 2.0f, 2.0f).mirrored(false), ModelTransform.origin(0.0f, 4.0f, -8.0f));
        return TexturedModelData.of(lv, 64, 64);
    }
}

