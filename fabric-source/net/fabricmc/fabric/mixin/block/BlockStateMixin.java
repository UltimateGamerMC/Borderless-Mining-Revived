package net.fabricmc.fabric.mixin.block;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.world.level.block.state.BlockState;

import net.fabricmc.fabric.api.block.v1.FabricBlockState;

@Mixin(BlockState.class)
public class BlockStateMixin implements FabricBlockState { }
