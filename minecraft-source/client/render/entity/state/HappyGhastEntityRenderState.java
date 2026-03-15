package net.minecraft.client.render.entity.state;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import net.minecraft.item.ItemStack;

@Environment(value=EnvType.CLIENT)
public class HappyGhastEntityRenderState
extends LivingEntityRenderState {
    public ItemStack harnessStack = ItemStack.EMPTY;
    public boolean hasPassengers;
    public boolean hasRopes;
}

