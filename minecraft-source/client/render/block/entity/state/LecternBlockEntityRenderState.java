package net.minecraft.client.render.block.entity.state;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.block.entity.state.BlockEntityRenderState;

@Environment(value=EnvType.CLIENT)
public class LecternBlockEntityRenderState
extends BlockEntityRenderState {
    public boolean hasBook;
    public float bookRotationDegrees;
}

