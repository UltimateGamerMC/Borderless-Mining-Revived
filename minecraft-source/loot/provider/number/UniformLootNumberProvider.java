/*
 * External method calls:
 *   Lnet/minecraft/loot/provider/number/ConstantLootNumberProvider;create(F)Lnet/minecraft/loot/provider/number/ConstantLootNumberProvider;
 *   Lnet/minecraft/loot/provider/number/LootNumberProvider;nextInt(Lnet/minecraft/loot/context/LootContext;)I
 *   Lnet/minecraft/loot/provider/number/LootNumberProvider;nextFloat(Lnet/minecraft/loot/context/LootContext;)F
 */
package net.minecraft.loot.provider.number;

import com.google.common.collect.Sets;
import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Set;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.loot.provider.number.LootNumberProvider;
import net.minecraft.loot.provider.number.LootNumberProviderType;
import net.minecraft.loot.provider.number.LootNumberProviderTypes;
import net.minecraft.util.context.ContextParameter;
import net.minecraft.util.math.MathHelper;

public record UniformLootNumberProvider(LootNumberProvider min, LootNumberProvider max) implements LootNumberProvider
{
    public static final MapCodec<UniformLootNumberProvider> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(((MapCodec)LootNumberProviderTypes.CODEC.fieldOf("min")).forGetter(UniformLootNumberProvider::min), ((MapCodec)LootNumberProviderTypes.CODEC.fieldOf("max")).forGetter(UniformLootNumberProvider::max)).apply((Applicative<UniformLootNumberProvider, ?>)instance, UniformLootNumberProvider::new));

    @Override
    public LootNumberProviderType getType() {
        return LootNumberProviderTypes.UNIFORM;
    }

    public static UniformLootNumberProvider create(float min, float max) {
        return new UniformLootNumberProvider(ConstantLootNumberProvider.create(min), ConstantLootNumberProvider.create(max));
    }

    @Override
    public int nextInt(LootContext context) {
        return MathHelper.nextInt(context.getRandom(), this.min.nextInt(context), this.max.nextInt(context));
    }

    @Override
    public float nextFloat(LootContext context) {
        return MathHelper.nextFloat(context.getRandom(), this.min.nextFloat(context), this.max.nextFloat(context));
    }

    @Override
    public Set<ContextParameter<?>> getAllowedParameters() {
        return Sets.union(this.min.getAllowedParameters(), this.max.getAllowedParameters());
    }
}

