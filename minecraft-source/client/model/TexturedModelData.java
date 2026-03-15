/*
 * External method calls:
 *   Lnet/minecraft/client/render/entity/model/ModelTransformer;apply(Lnet/minecraft/client/model/ModelData;)Lnet/minecraft/client/model/ModelData;
 *   Lnet/minecraft/client/model/ModelPartData;createPart(II)Lnet/minecraft/client/model/ModelPart;
 */
package net.minecraft.client.model;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.ModelData;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.model.TextureDimensions;
import net.minecraft.client.render.entity.model.ModelTransformer;

@Environment(value=EnvType.CLIENT)
public class TexturedModelData {
    private final ModelData data;
    private final TextureDimensions dimensions;

    private TexturedModelData(ModelData data, TextureDimensions dimensions) {
        this.data = data;
        this.dimensions = dimensions;
    }

    public TexturedModelData transform(ModelTransformer transformer) {
        return new TexturedModelData(transformer.apply(this.data), this.dimensions);
    }

    public ModelPart createModel() {
        return this.data.getRoot().createPart(this.dimensions.width, this.dimensions.height);
    }

    public static TexturedModelData of(ModelData partData, int textureWidth, int textureHeight) {
        return new TexturedModelData(partData, new TextureDimensions(textureWidth, textureHeight));
    }
}

