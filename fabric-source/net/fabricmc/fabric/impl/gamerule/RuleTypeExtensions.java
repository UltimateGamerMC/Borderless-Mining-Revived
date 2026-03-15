package net.fabricmc.fabric.impl.gamerule;

import org.jspecify.annotations.Nullable;

import net.fabricmc.fabric.impl.gamerule.rpc.FabricGameRuleType;

public interface RuleTypeExtensions {
	@Nullable
	FabricGameRuleType fabric_getType();

	void fabric_setType(FabricGameRuleType type);

	default <E extends Enum<E>> E fabric_enumCycle(E currentValue) {
		throw new UnsupportedOperationException("Non-enum rules cannot be cycled!");
	}

	default <E extends Enum<E>> Iterable<E> fabric_getSupportedEnumValues() {
		throw new UnsupportedOperationException("Non-enum rules cannot have supported enum values!");
	}

	default <E extends Enum<E>> void fabric_setSupportedEnumValues(E[] supportedValues) {
		throw new UnsupportedOperationException("Non-enum rules cannot have supported enum values!");
	}
}
