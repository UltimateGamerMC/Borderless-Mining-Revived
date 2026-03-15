package net.fabricmc.fabric.impl.gamerule;

import org.jspecify.annotations.Nullable;

import net.fabricmc.fabric.api.gamerule.v1.CustomGameRuleCategory;

public interface RuleCategoryExtensions {
	@Nullable
	CustomGameRuleCategory fabric_getCustomCategory();

	void fabric_setCustomCategory(CustomGameRuleCategory customCategory);
}
