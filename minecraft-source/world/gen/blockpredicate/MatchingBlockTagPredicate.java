/*
 * External method calls:
 *   Lnet/minecraft/registry/tag/TagKey;unprefixedCodec(Lnet/minecraft/registry/RegistryKey;)Lcom/mojang/serialization/Codec;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/world/gen/blockpredicate/MatchingBlockTagPredicate;registerOffsetField(Lcom/mojang/serialization/codecs/RecordCodecBuilder$Instance;)Lcom/mojang/datafixers/Products$P1;
 */
package net.minecraft.world.gen.blockpredicate;

import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.gen.blockpredicate.BlockPredicateType;
import net.minecraft.world.gen.blockpredicate.OffsetPredicate;

public class MatchingBlockTagPredicate
extends OffsetPredicate {
    final TagKey<Block> tag;
    public static final MapCodec<MatchingBlockTagPredicate> CODEC = RecordCodecBuilder.mapCodec(instance -> MatchingBlockTagPredicate.registerOffsetField(instance).and(((MapCodec)TagKey.unprefixedCodec(RegistryKeys.BLOCK).fieldOf("tag")).forGetter(predicate -> predicate.tag)).apply((Applicative<MatchingBlockTagPredicate, ?>)instance, MatchingBlockTagPredicate::new));

    protected MatchingBlockTagPredicate(Vec3i offset, TagKey<Block> tag) {
        super(offset);
        this.tag = tag;
    }

    @Override
    protected boolean test(BlockState state) {
        return state.isIn(this.tag);
    }

    @Override
    public BlockPredicateType<?> getType() {
        return BlockPredicateType.MATCHING_BLOCK_TAG;
    }
}

