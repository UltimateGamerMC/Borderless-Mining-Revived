package net.minecraft.client.render.entity.state;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import net.minecraft.entity.passive.SalmonEntity;

@Environment(value=EnvType.CLIENT)
public class SalmonEntityRenderState
extends LivingEntityRenderState {
    public SalmonEntity.Variant variant = SalmonEntity.Variant.MEDIUM;
}

