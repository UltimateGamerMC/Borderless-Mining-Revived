/*
 * External method calls:
 *   Lnet/minecraft/advancement/criterion/RecipeUnlockedCriterion$Conditions;matches(Lnet/minecraft/recipe/RecipeEntry;)Z
 *
 * Internal private/static methods:
 *   Lnet/minecraft/advancement/criterion/RecipeUnlockedCriterion;trigger(Lnet/minecraft/server/network/ServerPlayerEntity;Ljava/util/function/Predicate;)V
 *   Lnet/minecraft/advancement/criterion/RecipeUnlockedCriterion;create(Lnet/minecraft/advancement/criterion/CriterionConditions;)Lnet/minecraft/advancement/AdvancementCriterion;
 */
package net.minecraft.advancement.criterion;

import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Optional;
import net.minecraft.advancement.AdvancementCriterion;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.predicate.entity.LootContextPredicate;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.network.ServerPlayerEntity;

public class RecipeUnlockedCriterion
extends AbstractCriterion<Conditions> {
    @Override
    public Codec<Conditions> getConditionsCodec() {
        return Conditions.CODEC;
    }

    @Override
    public void trigger(ServerPlayerEntity player, RecipeEntry<?> recipe) {
        ((AbstractCriterion)this).trigger(player, (T conditions) -> conditions.matches(recipe));
    }

    public static AdvancementCriterion<Conditions> create(RegistryKey<Recipe<?>> arg) {
        return Criteria.RECIPE_UNLOCKED.create(new Conditions(Optional.empty(), arg));
    }

    public record Conditions(Optional<LootContextPredicate> player, RegistryKey<Recipe<?>> recipe) implements AbstractCriterion.Conditions
    {
        public static final Codec<Conditions> CODEC = RecordCodecBuilder.create(instance -> instance.group(EntityPredicate.LOOT_CONTEXT_PREDICATE_CODEC.optionalFieldOf("player").forGetter(Conditions::player), ((MapCodec)Recipe.KEY_CODEC.fieldOf("recipe")).forGetter(Conditions::recipe)).apply((Applicative<Conditions, ?>)instance, Conditions::new));

        public boolean matches(RecipeEntry<?> recipe) {
            return this.recipe == recipe.id();
        }
    }
}

