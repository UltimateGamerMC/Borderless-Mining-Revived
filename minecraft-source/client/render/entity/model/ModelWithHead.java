/*
 * External method calls:
 *   Lnet/minecraft/client/model/ModelPart;applyTransform(Lnet/minecraft/client/util/math/MatrixStack;)V
 */
package net.minecraft.client.render.entity.model;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.util.math.MatrixStack;

@Environment(value=EnvType.CLIENT)
public interface ModelWithHead {
    public ModelPart getHead();

    default public void applyTransform(MatrixStack matrices) {
        this.getHead().applyTransform(matrices);
    }
}

