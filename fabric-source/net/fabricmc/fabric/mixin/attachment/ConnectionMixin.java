package net.fabricmc.fabric.mixin.attachment;

import java.util.HashSet;
import java.util.Set;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import net.minecraft.network.Connection;
import net.minecraft.resources.Identifier;

import net.fabricmc.fabric.impl.attachment.sync.SupportedAttachmentsClientConnection;

@Mixin(Connection.class)
public class ConnectionMixin implements SupportedAttachmentsClientConnection {
	@Unique
	private Set<Identifier> supportedAttachments = new HashSet<>();

	@Override
	public void fabric_setSupportedAttachments(Set<Identifier> supportedAttachments) {
		this.supportedAttachments = supportedAttachments;
	}

	@Override
	public Set<Identifier> fabric_getSupportedAttachments() {
		return supportedAttachments;
	}
}
