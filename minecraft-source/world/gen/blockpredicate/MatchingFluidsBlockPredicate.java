/*
 * External method calls:
 *   Lnet/minecraft/registry/RegistryCodecs;entryList(Lnet/minecraft/registry/RegistryKey;)Lcom/mojang/serialization/Codec;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/world/gen/blockpredicate/MatchingFluidsBlockPredicate;registerOffsetField(Lcom/mojang/serialization/codecs/RecordCodecBuilder$Instance;)Lcom/mojang/datafixers/Products$P1;
 */
package net.minecraft.world.gen.blockpredicate;

import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.BlockState;
import net.minecraft.fluid.Fluid;
import net.minecraft.registry.RegistryCodecs;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.gen.blockpredicate.BlockPredicateType;
import net.minecraft.world.gen.blockpredicate.OffsetPredicate;

class MatchingFluidsBlockPredicate
extends OffsetPredicate {
    private final RegistryEntryList<Fluid> fluids;
    public static final MapCodec<MatchingFluidsBlockPredicate> CODEC = RecordCodecBuilder.mapCodec(instance -> MatchingFluidsBlockPredicate.registerOffsetField(instance).and(((MapCodec)RegistryCodecs.entryList(RegistryKeys.FLUID).fieldOf("fluids")).forGetter(predicate -> predicate.fluids)).apply((Applicative<MatchingFluidsBlockPredicate, ?>)instance, MatchingFluidsBlockPredicate::new));

    public MatchingFluidsBlockPredicate(Vec3i offset, RegistryEntryList<Fluid> fluids) {
        super(offset);
        this.fluids = fluids;
    }

    @Override
    protected boolean test(BlockState state) {
        return state.getFluidState().isIn(this.fluids);
    }

    @Override
    public BlockPredicateType<?> getType() {
        return BlockPredicateType.MATCHING_FLUIDS;
    }
}

