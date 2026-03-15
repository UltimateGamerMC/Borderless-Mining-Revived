package net.fabricmc.fabric.impl.attachment.sync;

import net.minecraft.network.chat.Component;

public class AttachmentSyncException extends Exception {
	private final Component text;

	public AttachmentSyncException(Component text) {
		super(text.getString());
		this.text = text;
	}

	public Component getText() {
		return text;
	}
}
