package net.minecraft.client.render.entity.model;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.FelineEntityModel;
import net.minecraft.client.render.entity.state.FelineEntityRenderState;

@Environment(value=EnvType.CLIENT)
public class OcelotEntityModel
extends FelineEntityModel<FelineEntityRenderState> {
    public OcelotEntityModel(ModelPart arg) {
        super(arg);
    }
}

