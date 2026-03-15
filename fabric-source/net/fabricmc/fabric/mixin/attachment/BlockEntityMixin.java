package net.fabricmc.fabric.mixin.attachment;

import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.core.BlockPos;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.impl.attachment.AttachmentTargetImpl;
import net.fabricmc.fabric.impl.attachment.AttachmentTypeImpl;
import net.fabricmc.fabric.impl.attachment.sync.AttachmentChange;
import net.fabricmc.fabric.impl.attachment.sync.AttachmentSync;
import net.fabricmc.fabric.impl.attachment.sync.AttachmentTargetInfo;

@Mixin(BlockEntity.class)
abstract class BlockEntityMixin implements AttachmentTargetImpl {
	@Shadow
	public abstract void setChanged();

	@Shadow
	@Final
	protected BlockPos worldPosition;

	@Shadow
	public abstract boolean hasLevel();

	@Shadow
	@Nullable
	protected Level level;

	@Inject(
			method = "loadWithComponents",
			at = @At("RETURN")
	)
	private void readBlockEntityAttachments(ValueInput view, CallbackInfo ci) {
		this.fabric_readAttachmentsFromNbt(view);
	}

	@Inject(
			method = "saveWithoutMetadata(Lnet/minecraft/world/level/storage/ValueOutput;)V",
			at = @At(value = "TAIL")
	)
	private void writeBlockEntityAttachments(ValueOutput view, CallbackInfo ci) {
		this.fabric_writeAttachmentsToNbt(view);
	}

	@Override
	public void fabric_markChanged(AttachmentType<?> type) {
		this.setChanged();
	}

	@Override
	public AttachmentTargetInfo<?> fabric_getSyncTargetInfo() {
		return new AttachmentTargetInfo.BlockEntityTarget(this.worldPosition);
	}

	@Override
	public void fabric_syncChange(AttachmentType<?> type, AttachmentChange change) {
		PlayerLookup.tracking((BlockEntity) (Object) this)
				.forEach(player -> {
					if (((AttachmentTypeImpl<?>) type).syncPredicate().test(this, player)) {
						AttachmentSync.trySync(change, player);
					}
				});
	}

	@Override
	public boolean fabric_shouldTryToSync() {
		// Persistent attachments are read at a time with no world
		return !this.hasLevel() || !this.level.isClientSide();
	}

	@Override
	public RegistryAccess fabric_getDynamicRegistryManager() {
		return this.level.registryAccess();
	}
}
