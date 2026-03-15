/*
 * External method calls:
 *   Lnet/minecraft/client/model/ModelPartBuilder;create()Lnet/minecraft/client/model/ModelPartBuilder;
 *   Lnet/minecraft/client/model/ModelPartBuilder;uv(II)Lnet/minecraft/client/model/ModelPartBuilder;
 *   Lnet/minecraft/client/model/ModelPartBuilder;cuboid(FFFFFF)Lnet/minecraft/client/model/ModelPartBuilder;
 *   Lnet/minecraft/client/model/ModelTransform;origin(FFF)Lnet/minecraft/client/model/ModelTransform;
 *   Lnet/minecraft/client/model/ModelPartData;addChild(Ljava/lang/String;Lnet/minecraft/client/model/ModelPartBuilder;Lnet/minecraft/client/model/ModelTransform;)Lnet/minecraft/client/model/ModelPartData;
 *   Lnet/minecraft/client/model/TexturedModelData;of(Lnet/minecraft/client/model/ModelData;II)Lnet/minecraft/client/model/TexturedModelData;
 */
package net.minecraft.client.render.block.entity.model;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.ModelData;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.model.ModelPartBuilder;
import net.minecraft.client.model.ModelPartData;
import net.minecraft.client.model.ModelTransform;
import net.minecraft.client.model.TexturedModelData;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.util.math.MathHelper;

@Environment(value=EnvType.CLIENT)
public class BannerFlagBlockModel
extends Model<Float> {
    private final ModelPart flag;

    public BannerFlagBlockModel(ModelPart root) {
        super(root, RenderLayers::entitySolid);
        this.flag = root.getChild("flag");
    }

    public static TexturedModelData getTexturedModelData(boolean standing) {
        ModelData lv = new ModelData();
        ModelPartData lv2 = lv.getRoot();
        lv2.addChild("flag", ModelPartBuilder.create().uv(0, 0).cuboid(-10.0f, 0.0f, -2.0f, 20.0f, 40.0f, 1.0f), ModelTransform.origin(0.0f, standing ? -44.0f : -20.5f, standing ? 0.0f : 10.5f));
        return TexturedModelData.of(lv, 64, 64);
    }

    @Override
    public void setAngles(Float float_) {
        super.setAngles(float_);
        this.flag.pitch = (-0.0125f + 0.01f * MathHelper.cos((float)Math.PI * 2 * float_.floatValue())) * (float)Math.PI;
    }
}

