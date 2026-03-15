package net.fabricmc.fabric.impl.gamerule.rpc;

import org.jspecify.annotations.Nullable;

public interface FabricTypedRule {
	@Nullable
	FabricGameRuleType getFabricType();

	void setFabricType(FabricGameRuleType type);
}
