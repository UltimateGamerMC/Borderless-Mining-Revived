package net.minecraft.client.render.entity.state;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.state.SkeletonEntityRenderState;

@Environment(value=EnvType.CLIENT)
public class BoggedEntityRenderState
extends SkeletonEntityRenderState {
    public boolean sheared;
}

