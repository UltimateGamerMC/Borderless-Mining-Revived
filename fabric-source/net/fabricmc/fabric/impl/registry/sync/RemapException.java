package net.fabricmc.fabric.impl.registry.sync;

import org.jspecify.annotations.Nullable;

import net.minecraft.network.chat.Component;

public class RemapException extends Exception {
	@Nullable
	private final Component text;

	public RemapException(String message) {
		super(message);
		this.text = null;
	}

	public RemapException(Component text) {
		super(text.getString());
		this.text = text;
	}

	@Nullable
	public Component getText() {
		return text;
	}
}
