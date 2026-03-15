package net.fabricmc.fabric.mixin.attachment;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.core.RegistryAccess;
import net.minecraft.world.level.Level;

import net.fabricmc.fabric.impl.attachment.AttachmentTargetImpl;

@Mixin(Level.class)
abstract class LevelMixin implements AttachmentTargetImpl {
	@Shadow
	public abstract boolean isClientSide();

	@Shadow
	public abstract RegistryAccess registryAccess();

	@Override
	public boolean fabric_shouldTryToSync() {
		return !this.isClientSide();
	}

	@Override
	public RegistryAccess fabric_getDynamicRegistryManager() {
		return registryAccess();
	}
}
