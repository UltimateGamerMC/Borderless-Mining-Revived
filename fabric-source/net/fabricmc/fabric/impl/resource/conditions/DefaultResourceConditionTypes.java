package net.fabricmc.fabric.impl.resource.conditions;

import com.mojang.serialization.MapCodec;

import net.minecraft.resources.Identifier;

import net.fabricmc.fabric.api.resource.conditions.v1.ResourceCondition;
import net.fabricmc.fabric.api.resource.conditions.v1.ResourceConditionType;
import net.fabricmc.fabric.impl.resource.conditions.conditions.AllModsLoadedResourceCondition;
import net.fabricmc.fabric.impl.resource.conditions.conditions.AndResourceCondition;
import net.fabricmc.fabric.impl.resource.conditions.conditions.AnyModsLoadedResourceCondition;
import net.fabricmc.fabric.impl.resource.conditions.conditions.FeaturesEnabledResourceCondition;
import net.fabricmc.fabric.impl.resource.conditions.conditions.NotResourceCondition;
import net.fabricmc.fabric.impl.resource.conditions.conditions.OrResourceCondition;
import net.fabricmc.fabric.impl.resource.conditions.conditions.RegistryContainsResourceCondition;
import net.fabricmc.fabric.impl.resource.conditions.conditions.TagsPopulatedResourceCondition;
import net.fabricmc.fabric.impl.resource.conditions.conditions.TrueResourceCondition;

public class DefaultResourceConditionTypes {
	public static final ResourceConditionType<TrueResourceCondition> TRUE = createResourceConditionType("true", TrueResourceCondition.CODEC);
	public static final ResourceConditionType<NotResourceCondition> NOT = createResourceConditionType("not", NotResourceCondition.CODEC);
	public static final ResourceConditionType<OrResourceCondition> OR = createResourceConditionType("or", OrResourceCondition.CODEC);
	public static final ResourceConditionType<AndResourceCondition> AND = createResourceConditionType("and", AndResourceCondition.CODEC);
	public static final ResourceConditionType<AllModsLoadedResourceCondition> ALL_MODS_LOADED = createResourceConditionType("all_mods_loaded", AllModsLoadedResourceCondition.CODEC);
	public static final ResourceConditionType<AnyModsLoadedResourceCondition> ANY_MODS_LOADED = createResourceConditionType("any_mods_loaded", AnyModsLoadedResourceCondition.CODEC);
	public static final ResourceConditionType<TagsPopulatedResourceCondition> TAGS_POPULATED = createResourceConditionType("tags_populated", TagsPopulatedResourceCondition.CODEC);
	public static final ResourceConditionType<FeaturesEnabledResourceCondition> FEATURES_ENABLED = createResourceConditionType("features_enabled", FeaturesEnabledResourceCondition.CODEC);
	public static final ResourceConditionType<RegistryContainsResourceCondition> REGISTRY_CONTAINS = createResourceConditionType("registry_contains", RegistryContainsResourceCondition.CODEC);

	private static <T extends ResourceCondition> ResourceConditionType<T> createResourceConditionType(String name, MapCodec<T> codec) {
		return ResourceConditionType.create(Identifier.fromNamespaceAndPath("fabric", name), codec);
	}
}
