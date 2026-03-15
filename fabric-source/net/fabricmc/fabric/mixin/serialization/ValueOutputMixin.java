package net.fabricmc.fabric.mixin.serialization;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.world.level.storage.ValueOutput;

import net.fabricmc.fabric.api.serialization.v1.view.FabricWriteView;

@Mixin(ValueOutput.class)
public interface ValueOutputMixin extends FabricWriteView {
}
