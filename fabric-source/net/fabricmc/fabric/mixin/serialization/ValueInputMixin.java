package net.fabricmc.fabric.mixin.serialization;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.world.level.storage.ValueInput;

import net.fabricmc.fabric.api.serialization.v1.view.FabricReadView;

@Mixin(ValueInput.class)
public interface ValueInputMixin extends FabricReadView {
}
