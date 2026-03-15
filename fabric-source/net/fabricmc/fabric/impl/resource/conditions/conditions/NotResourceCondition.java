package net.fabricmc.fabric.impl.resource.conditions.conditions;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import org.jspecify.annotations.Nullable;

import net.minecraft.resources.RegistryOps;

import net.fabricmc.fabric.api.resource.conditions.v1.ResourceCondition;
import net.fabricmc.fabric.api.resource.conditions.v1.ResourceConditionType;
import net.fabricmc.fabric.impl.resource.conditions.DefaultResourceConditionTypes;

public record NotResourceCondition(ResourceCondition condition) implements ResourceCondition {
	public static final MapCodec<NotResourceCondition> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
			ResourceCondition.CODEC.fieldOf("value").forGetter(NotResourceCondition::condition)
	).apply(instance, NotResourceCondition::new));

	@Override
	public ResourceConditionType<?> getType() {
		return DefaultResourceConditionTypes.NOT;
	}

	@Override
	public boolean test(RegistryOps.@Nullable RegistryInfoLookup registryInfo) {
		return !this.condition().test(registryInfo);
	}
}
