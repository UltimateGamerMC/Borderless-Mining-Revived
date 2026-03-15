package net.fabricmc.fabric.mixin.attachment;

import java.util.Map;
import java.util.function.Consumer;

import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.core.RegistryAccess;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.chunk.ImposterProtoChunk;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.fabricmc.fabric.impl.attachment.AttachmentTargetImpl;
import net.fabricmc.fabric.impl.attachment.sync.AttachmentChange;
import net.fabricmc.fabric.impl.attachment.sync.AttachmentTargetInfo;

@Mixin(ImposterProtoChunk.class)
abstract class ImposterProtoChunkMixin extends AttachmentTargetsMixin {
	@Shadow
	@Final
	private LevelChunk wrapped;

	@Override
	@Nullable
	public <T> T getAttached(AttachmentType<T> type) {
		return this.wrapped.getAttached(type);
	}

	@Override
	@Nullable
	public <T> T setAttached(AttachmentType<T> type, @Nullable T value) {
		return this.wrapped.setAttached(type, value);
	}

	@Override
	public boolean hasAttached(AttachmentType<?> type) {
		return this.wrapped.hasAttached(type);
	}

	@Override
	public void fabric_writeAttachmentsToNbt(ValueOutput view) {
		((AttachmentTargetImpl) this.wrapped).fabric_writeAttachmentsToNbt(view);
	}

	@Override
	public void fabric_readAttachmentsFromNbt(ValueInput view) {
		((AttachmentTargetImpl) this.wrapped).fabric_readAttachmentsFromNbt(view);
	}

	@Override
	public boolean fabric_hasPersistentAttachments() {
		return ((AttachmentTargetImpl) this.wrapped).fabric_hasPersistentAttachments();
	}

	@Override
	public Map<AttachmentType<?>, ?> fabric_getAttachments() {
		return ((AttachmentTargetImpl) this.wrapped).fabric_getAttachments();
	}

	@Override
	public boolean fabric_shouldTryToSync() {
		return ((AttachmentTargetImpl) wrapped).fabric_shouldTryToSync();
	}

	@Override
	public void fabric_computeInitialSyncChanges(ServerPlayer player, Consumer<AttachmentChange> changeOutput) {
		((AttachmentTargetImpl) wrapped).fabric_computeInitialSyncChanges(player, changeOutput);
	}

	@Override
	public AttachmentTargetInfo<?> fabric_getSyncTargetInfo() {
		return ((AttachmentTargetImpl) wrapped).fabric_getSyncTargetInfo();
	}

	@Override
	public void fabric_syncChange(AttachmentType<?> type, AttachmentChange change) {
		((AttachmentTargetImpl) wrapped).fabric_syncChange(type, change);
	}

	@Override
	public void fabric_markChanged(AttachmentType<?> type) {
		((AttachmentTargetImpl) wrapped).fabric_markChanged(type);
	}

	@Override
	public RegistryAccess fabric_getDynamicRegistryManager() {
		return ((AttachmentTargetImpl) wrapped).fabric_getDynamicRegistryManager();
	}
}
