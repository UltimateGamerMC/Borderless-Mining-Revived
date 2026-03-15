package net.minecraft.client.render.entity.state;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.state.ArmedEntityRenderState;

@Environment(value=EnvType.CLIENT)
public class AllayEntityRenderState
extends ArmedEntityRenderState {
    public boolean dancing;
    public boolean spinning;
    public float spinningAnimationTicks;
    public float itemHoldAnimationTicks;
}

