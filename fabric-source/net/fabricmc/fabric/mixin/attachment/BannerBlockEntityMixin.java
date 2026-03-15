package net.fabricmc.fabric.mixin.attachment;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BannerBlockEntity;

import net.fabricmc.fabric.api.attachment.v1.AttachmentTarget;

@Mixin(BannerBlockEntity.class)
abstract class BannerBlockEntityMixin {
	@ModifyExpressionValue(method = "getUpdateTag", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/entity/BannerBlockEntity;saveWithoutMetadata(Lnet/minecraft/core/HolderLookup$Provider;)Lnet/minecraft/nbt/CompoundTag;"))
	private CompoundTag removeAttachments(CompoundTag original) {
		original.remove(AttachmentTarget.NBT_ATTACHMENT_KEY);
		return original;
	}
}
