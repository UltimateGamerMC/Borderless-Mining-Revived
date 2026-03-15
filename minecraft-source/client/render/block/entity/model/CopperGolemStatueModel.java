package net.minecraft.client.render.block.entity.model;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.util.math.Direction;

@Environment(value=EnvType.CLIENT)
public class CopperGolemStatueModel
extends Model<Direction> {
    public CopperGolemStatueModel(ModelPart root) {
        super(root, RenderLayers::entityCutoutNoCull);
    }

    @Override
    public void setAngles(Direction arg) {
        this.root.originY = 0.0f;
        this.root.yaw = arg.getOpposite().getPositiveHorizontalDegrees() * ((float)Math.PI / 180);
        this.root.roll = (float)Math.PI;
    }
}

