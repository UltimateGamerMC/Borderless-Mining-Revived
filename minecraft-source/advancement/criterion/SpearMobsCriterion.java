/*
 * External method calls:
 *   Lnet/minecraft/advancement/criterion/SpearMobsCriterion$Conditions;test(I)Z
 *
 * Internal private/static methods:
 *   Lnet/minecraft/advancement/criterion/SpearMobsCriterion;trigger(Lnet/minecraft/server/network/ServerPlayerEntity;Ljava/util/function/Predicate;)V
 */
package net.minecraft.advancement.criterion;

import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Optional;
import net.minecraft.advancement.AdvancementCriterion;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.predicate.entity.LootContextPredicate;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.dynamic.Codecs;

public class SpearMobsCriterion
extends AbstractCriterion<Conditions> {
    @Override
    public Codec<Conditions> getConditionsCodec() {
        return Conditions.CODEC;
    }

    public void trigger(ServerPlayerEntity player, int count) {
        this.trigger(player, conditions -> conditions.test(count));
    }

    public record Conditions(Optional<LootContextPredicate> player, Optional<Integer> count) implements AbstractCriterion.Conditions
    {
        public static final Codec<Conditions> CODEC = RecordCodecBuilder.create(instance -> instance.group(EntityPredicate.LOOT_CONTEXT_PREDICATE_CODEC.optionalFieldOf("player").forGetter(Conditions::player), Codecs.POSITIVE_INT.optionalFieldOf("count").forGetter(Conditions::count)).apply((Applicative<Conditions, ?>)instance, Conditions::new));

        public static AdvancementCriterion<Conditions> method_76462(int i) {
            return Criteria.SPEAR_MOBS.create(new Conditions(Optional.empty(), Optional.of(i)));
        }

        public boolean test(int count) {
            return this.count.isEmpty() || count >= this.count.get();
        }
    }
}

