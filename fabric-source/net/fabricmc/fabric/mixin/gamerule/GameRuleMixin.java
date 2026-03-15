package net.fabricmc.fabric.mixin.gamerule;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.mojang.serialization.DataResult;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import net.minecraft.world.level.gamerules.GameRule;

import net.fabricmc.fabric.api.gamerule.v1.CustomGameRuleCategory;
import net.fabricmc.fabric.impl.gamerule.RuleCategoryExtensions;
import net.fabricmc.fabric.impl.gamerule.RuleTypeExtensions;
import net.fabricmc.fabric.impl.gamerule.rpc.FabricGameRuleType;

@Mixin(GameRule.class)
public abstract class GameRuleMixin<T> implements RuleCategoryExtensions, RuleTypeExtensions {
	@Shadow
	public abstract Class<T> valueClass();

	@Unique
	@Nullable
	private CustomGameRuleCategory fabricCategory;

	@Unique
	@Nullable
	private FabricGameRuleType fabricGameRuleType;

	@Unique
	private final List<T> enumSupportedValues = new ArrayList<>();

	@Override
	public CustomGameRuleCategory fabric_getCustomCategory() {
		return this.fabricCategory;
	}

	@Override
	public void fabric_setCustomCategory(CustomGameRuleCategory customCategory) {
		this.fabricCategory = customCategory;
	}

	@Override
	public @Nullable FabricGameRuleType fabric_getType() {
		return this.fabricGameRuleType;
	}

	@Override
	public void fabric_setType(FabricGameRuleType type) {
		this.fabricGameRuleType = type;
	}

	@Override
	public <E extends Enum<E>> E fabric_enumCycle(E currentValue) {
		if (this.fabric_getType() != FabricGameRuleType.ENUM) {
			return RuleTypeExtensions.super.fabric_enumCycle(currentValue);
		}

		int index = this.enumSupportedValues.indexOf((T) currentValue);

		if (index < 0) {
			throw new IllegalArgumentException(String.format("Invalid value: %s", currentValue));
		}

		return (E) this.enumSupportedValues.get((index + 1) % this.enumSupportedValues.size());
	}

	@Override
	public <E extends Enum<E>> Iterable<E> fabric_getSupportedEnumValues() {
		if (this.fabric_getType() != FabricGameRuleType.ENUM) {
			return RuleTypeExtensions.super.fabric_getSupportedEnumValues();
		}

		return (Iterable<E>) this.enumSupportedValues;
	}

	@Override
	public <E extends Enum<E>> void fabric_setSupportedEnumValues(E[] supportedValues) {
		if (this.fabric_getType() != FabricGameRuleType.ENUM) {
			RuleTypeExtensions.super.fabric_setSupportedEnumValues(supportedValues);
			return;
		}

		this.enumSupportedValues.clear();
		Collections.addAll((List<E>) this.enumSupportedValues, supportedValues);
	}

	@WrapMethod(method = "deserialize")
	private <E extends Enum<E>> DataResult<T> deserializeEnum(String value, Operation<DataResult<T>> original) {
		if (this.fabric_getType() != FabricGameRuleType.ENUM) {
			return original.call(value);
		}

		try {
			Class<E> classType = (Class<E>) this.valueClass();
			final E deserialized = Enum.valueOf(classType, value);

			if (!this.enumSupportedValues.contains(deserialized)) {
				return DataResult.error(() -> "Failed to parse rule of value " + value + " for rule of type " + classType + " because the value is unsupported.");
			}

			return DataResult.success((T) deserialized);
		} catch (IllegalArgumentException e) {
			return DataResult.error(() -> "Failed to parse rule of value " + value + " for rule of type " + this.valueClass());
		}
	}
}
