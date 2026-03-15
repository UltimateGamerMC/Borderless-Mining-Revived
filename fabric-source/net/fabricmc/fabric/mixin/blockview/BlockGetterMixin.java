package net.fabricmc.fabric.mixin.blockview;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.world.level.BlockGetter;

import net.fabricmc.fabric.api.blockview.v2.FabricBlockView;

@Mixin(BlockGetter.class)
public interface BlockGetterMixin extends FabricBlockView {
}
