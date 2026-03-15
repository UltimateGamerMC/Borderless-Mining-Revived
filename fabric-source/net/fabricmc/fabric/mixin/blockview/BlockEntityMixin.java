package net.fabricmc.fabric.mixin.blockview;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.world.level.block.entity.BlockEntity;

import net.fabricmc.fabric.api.blockview.v2.RenderDataBlockEntity;

@Mixin(BlockEntity.class)
public abstract class BlockEntityMixin implements RenderDataBlockEntity {
}
