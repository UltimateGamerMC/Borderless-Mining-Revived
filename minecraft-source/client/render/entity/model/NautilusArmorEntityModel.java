/*
 * External method calls:
 *   Lnet/minecraft/client/model/ModelPartBuilder;create()Lnet/minecraft/client/model/ModelPartBuilder;
 *   Lnet/minecraft/client/model/ModelTransform;origin(FFF)Lnet/minecraft/client/model/ModelTransform;
 *   Lnet/minecraft/client/model/ModelPartData;addChild(Ljava/lang/String;Lnet/minecraft/client/model/ModelPartBuilder;Lnet/minecraft/client/model/ModelTransform;)Lnet/minecraft/client/model/ModelPartData;
 *   Lnet/minecraft/client/model/ModelPartBuilder;uv(II)Lnet/minecraft/client/model/ModelPartBuilder;
 *   Lnet/minecraft/client/model/ModelPartBuilder;cuboid(FFFFFFLnet/minecraft/client/model/Dilation;)Lnet/minecraft/client/model/ModelPartBuilder;
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
import net.minecraft.client.render.entity.model.NautilusEntityModel;

@Environment(value=EnvType.CLIENT)
public class NautilusArmorEntityModel
extends NautilusEntityModel {
    private final ModelPart armorRoot;
    private final ModelPart shell;

    public NautilusArmorEntityModel(ModelPart arg) {
        super(arg);
        this.armorRoot = arg.getChild(EntityModelPartNames.ROOT);
        this.shell = this.armorRoot.getChild(EntityModelPartNames.SHELL);
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData lv = NautilusArmorEntityModel.getModelData();
        ModelPartData lv2 = lv.getRoot();
        ModelPartData lv3 = lv2.addChild(EntityModelPartNames.ROOT, ModelPartBuilder.create(), ModelTransform.origin(0.0f, 29.0f, -6.0f));
        ModelPartData lv4 = lv3.addChild(EntityModelPartNames.SHELL, ModelPartBuilder.create().uv(0, 0).cuboid(-7.0f, -10.0f, -7.0f, 14.0f, 10.0f, 16.0f, new Dilation(0.01f)).uv(0, 26).cuboid(-7.0f, 0.0f, -7.0f, 14.0f, 8.0f, 20.0f, new Dilation(0.01f)).uv(48, 26).cuboid(-7.0f, 0.0f, 6.0f, 14.0f, 8.0f, 0.0f, new Dilation(0.0f)), ModelTransform.origin(0.0f, -13.0f, 5.0f));
        return TexturedModelData.of(lv, 128, 128);
    }
}

