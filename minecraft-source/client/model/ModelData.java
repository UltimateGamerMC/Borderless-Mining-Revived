/*
 * External method calls:
 *   Lnet/minecraft/client/model/ModelPartData;applyTransformer(Ljava/util/function/UnaryOperator;)Lnet/minecraft/client/model/ModelPartData;
 *   Lnet/minecraft/client/render/entity/model/ModelTransformer;apply(Lnet/minecraft/client/model/ModelData;)Lnet/minecraft/client/model/ModelData;
 */
package net.minecraft.client.model;

import com.google.common.collect.ImmutableList;
import java.util.function.UnaryOperator;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.ModelPartData;
import net.minecraft.client.model.ModelTransform;
import net.minecraft.client.render.entity.model.ModelTransformer;

@Environment(value=EnvType.CLIENT)
public class ModelData {
    private final ModelPartData data;

    public ModelData() {
        this(new ModelPartData(ImmutableList.of(), ModelTransform.NONE));
    }

    private ModelData(ModelPartData data) {
        this.data = data;
    }

    public ModelPartData getRoot() {
        return this.data;
    }

    public ModelData transform(UnaryOperator<ModelTransform> transformer) {
        return new ModelData(this.data.applyTransformer(transformer));
    }

    public ModelData transform(ModelTransformer transformer) {
        return transformer.apply(this);
    }
}

