/*
 * External method calls:
 *   Lnet/minecraft/entity/spawn/SpawnContext;environmentAttributes()Lnet/minecraft/world/attribute/EnvironmentAttributeAccess;
 *   Lnet/minecraft/entity/spawn/SpawnContext;pos()Lnet/minecraft/util/math/BlockPos;
 *   Lnet/minecraft/predicate/NumberRange$DoubleRange;test(D)Z
 *
 * Internal private/static methods:
 *   Lnet/minecraft/entity/spawn/MoonBrightnessSpawnCondition;test(Lnet/minecraft/entity/spawn/SpawnContext;)Z
 */
package net.minecraft.entity.spawn;

import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.entity.spawn.SpawnCondition;
import net.minecraft.entity.spawn.SpawnContext;
import net.minecraft.predicate.NumberRange;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.MoonPhase;
import net.minecraft.world.attribute.EnvironmentAttributes;
import net.minecraft.world.dimension.DimensionType;

public record MoonBrightnessSpawnCondition(NumberRange.DoubleRange range) implements SpawnCondition
{
    public static final MapCodec<MoonBrightnessSpawnCondition> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(((MapCodec)NumberRange.DoubleRange.CODEC.fieldOf("range")).forGetter(MoonBrightnessSpawnCondition::range)).apply((Applicative<MoonBrightnessSpawnCondition, ?>)instance, MoonBrightnessSpawnCondition::new));

    @Override
    public boolean test(SpawnContext arg) {
        MoonPhase lv = arg.environmentAttributes().getAttributeValue(EnvironmentAttributes.MOON_PHASE_VISUAL, Vec3d.ofCenter(arg.pos()));
        float f = DimensionType.MOON_SIZES[lv.getIndex()];
        return this.range.test(f);
    }

    public MapCodec<MoonBrightnessSpawnCondition> getCodec() {
        return CODEC;
    }

    @Override
    public /* synthetic */ boolean test(Object context) {
        return this.test((SpawnContext)context);
    }
}

