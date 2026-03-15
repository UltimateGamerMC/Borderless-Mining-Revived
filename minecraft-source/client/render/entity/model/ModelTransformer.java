/*
 * External method calls:
 *   Lnet/minecraft/client/model/ModelData;transform(Ljava/util/function/UnaryOperator;)Lnet/minecraft/client/model/ModelData;
 *   Lnet/minecraft/client/model/ModelTransform;scaled(F)Lnet/minecraft/client/model/ModelTransform;
 *   Lnet/minecraft/client/model/ModelTransform;moveOrigin(FFF)Lnet/minecraft/client/model/ModelTransform;
 */
package net.minecraft.client.render.entity.model;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.ModelData;

@FunctionalInterface
@Environment(value=EnvType.CLIENT)
public interface ModelTransformer {
    public static final ModelTransformer NO_OP = data -> data;

    public static ModelTransformer scaling(float scale) {
        float g = 24.016f * (1.0f - scale);
        return data -> data.transform(transform -> transform.scaled(scale).moveOrigin(0.0f, g, 0.0f));
    }

    public ModelData apply(ModelData var1);
}

