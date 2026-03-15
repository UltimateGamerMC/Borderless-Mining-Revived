/*
 * External method calls:
 *   Lnet/minecraft/predicate/NumberRange$IntRange;test(I)Z
 *   Lnet/minecraft/predicate/entity/EntityPredicate;test(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/entity/Entity;)Z
 */
package net.minecraft.predicate.entity;

import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Optional;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LightningEntity;
import net.minecraft.predicate.NumberRange;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.predicate.entity.EntitySubPredicate;
import net.minecraft.predicate.entity.EntitySubPredicateTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;
import org.jspecify.annotations.Nullable;

public record LightningBoltPredicate(NumberRange.IntRange blocksSetOnFire, Optional<EntityPredicate> entityStruck) implements EntitySubPredicate
{
    public static final MapCodec<LightningBoltPredicate> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(NumberRange.IntRange.CODEC.optionalFieldOf("blocks_set_on_fire", NumberRange.IntRange.ANY).forGetter(LightningBoltPredicate::blocksSetOnFire), EntityPredicate.CODEC.optionalFieldOf("entity_struck").forGetter(LightningBoltPredicate::entityStruck)).apply((Applicative<LightningBoltPredicate, ?>)instance, LightningBoltPredicate::new));

    public static LightningBoltPredicate of(NumberRange.IntRange blocksSetOnFire) {
        return new LightningBoltPredicate(blocksSetOnFire, Optional.empty());
    }

    public MapCodec<LightningBoltPredicate> getCodec() {
        return EntitySubPredicateTypes.LIGHTNING;
    }

    @Override
    public boolean test(Entity entity, ServerWorld world, @Nullable Vec3d pos) {
        if (!(entity instanceof LightningEntity)) {
            return false;
        }
        LightningEntity lv = (LightningEntity)entity;
        return this.blocksSetOnFire.test(lv.getBlocksSetOnFire()) && (this.entityStruck.isEmpty() || lv.getStruckEntities().anyMatch(struckEntity -> this.entityStruck.get().test(world, pos, (Entity)struckEntity)));
    }
}

