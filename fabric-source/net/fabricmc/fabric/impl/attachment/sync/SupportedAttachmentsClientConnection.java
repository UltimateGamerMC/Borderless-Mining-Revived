package net.fabricmc.fabric.impl.attachment.sync;

import java.util.Set;

import net.minecraft.network.Connection;
import net.minecraft.resources.Identifier;

/**
 * Implemented on {@link Connection} to store which attachments the client supports.
 */
public interface SupportedAttachmentsClientConnection {
	void fabric_setSupportedAttachments(Set<Identifier> supportedAttachments);

	Set<Identifier> fabric_getSupportedAttachments();
}
