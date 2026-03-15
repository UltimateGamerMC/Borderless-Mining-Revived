/*
 * External method calls:
 *   Lnet/minecraft/loot/context/LootWorldContext$Builder;build(Lnet/minecraft/util/context/ContextType;)Lnet/minecraft/loot/context/LootWorldContext;
 *   Lnet/minecraft/loot/context/LootContext$Builder;build(Ljava/util/Optional;)Lnet/minecraft/loot/context/LootContext;
 *   Lnet/minecraft/advancement/criterion/AnyBlockUseCriterion$Conditions;test(Lnet/minecraft/loot/context/LootContext;)Z
 *
 * Internal private/static methods:
 *   Lnet/minecraft/advancement/criterion/AnyBlockUseCriterion;trigger(Lnet/minecraft/server/network/ServerPlayerEntity;Ljava/util/function/Predicate;)V
 */
package net.minecraft.advancement.criterion;

import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Optional;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.loot.context.LootWorldContext;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.predicate.entity.LootContextPredicate;
import net.minecraft.predicate.entity.LootContextPredicateValidator;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

public class AnyBlockUseCriterion
extends AbstractCriterion<Conditions> {
    @Override
    public Codec<Conditions> getConditionsCodec() {
        return Conditions.CODEC;
    }

    public void trigger(ServerPlayerEntity player, BlockPos pos, ItemStack stack) {
        ServerWorld lv = player.getEntityWorld();
        BlockState lv2 = lv.getBlockState(pos);
        LootWorldContext lv3 = new LootWorldContext.Builder(lv).add(LootContextParameters.ORIGIN, pos.toCenterPos()).add(LootContextParameters.THIS_ENTITY, player).add(LootContextParameters.BLOCK_STATE, lv2).add(LootContextParameters.TOOL, stack).build(LootContextTypes.ADVANCEMENT_LOCATION);
        LootContext lv4 = new LootContext.Builder(lv3).build(Optional.empty());
        this.trigger(player, conditions -> conditions.test(lv4));
    }

    public record Conditions(Optional<LootContextPredicate> player, Optional<LootContextPredicate> location) implements AbstractCriterion.Conditions
    {
        public static final Codec<Conditions> CODEC = RecordCodecBuilder.create(instance -> instance.group(EntityPredicate.LOOT_CONTEXT_PREDICATE_CODEC.optionalFieldOf("player").forGetter(Conditions::player), LootContextPredicate.CODEC.optionalFieldOf("location").forGetter(Conditions::location)).apply((Applicative<Conditions, ?>)instance, Conditions::new));

        public boolean test(LootContext location) {
            return this.location.isEmpty() || this.location.get().test(location);
        }

        @Override
        public void validate(LootContextPredicateValidator validator) {
            AbstractCriterion.Conditions.super.validate(validator);
            this.location.ifPresent(location -> validator.validate((LootContextPredicate)location, LootContextTypes.ADVANCEMENT_LOCATION, "location"));
        }
    }
}

