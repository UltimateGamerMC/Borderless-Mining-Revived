package net.minecraft.client.render.entity.state;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.state.IllagerEntityRenderState;

@Environment(value=EnvType.CLIENT)
public class EvokerEntityRenderState
extends IllagerEntityRenderState {
    public boolean spellcasting;
}

