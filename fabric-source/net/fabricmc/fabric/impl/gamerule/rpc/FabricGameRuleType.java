package net.fabricmc.fabric.impl.gamerule.rpc;

import net.minecraft.util.StringRepresentable;

/**
 * Extensions to {@link net.minecraft.server.dedicated.management.dispatch.GameRuleType}.
 */
public enum FabricGameRuleType implements StringRepresentable {
	DOUBLE("fabric:double"),
	ENUM("fabric:enum");

	private final String name;

	FabricGameRuleType(final String name) {
		this.name = name;
	}

	@Override
	public String getSerializedName() {
		return this.name;
	}
}
