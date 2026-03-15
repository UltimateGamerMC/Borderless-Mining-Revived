/*
 * External method calls:
 *   Lnet/minecraft/entity/ai/brain/Brain;remember(Lnet/minecraft/entity/ai/brain/MemoryModuleType;Ljava/lang/Object;)V
 *   Lnet/minecraft/entity/ai/brain/Brain;forget(Lnet/minecraft/entity/ai/brain/MemoryModuleType;)V
 *   Lnet/minecraft/entity/ai/TargetPredicate;test(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/entity/LivingEntity;)Z
 *   Lnet/minecraft/entity/ai/TargetPredicate;createNonAttackable()Lnet/minecraft/entity/ai/TargetPredicate;
 *   Lnet/minecraft/entity/ai/TargetPredicate;ignoreVisibility()Lnet/minecraft/entity/ai/TargetPredicate;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/entity/ai/brain/sensor/TemptationsSensor;test(Lnet/minecraft/entity/mob/PathAwareEntity;Lnet/minecraft/item/ItemStack;)Z
 *   Lnet/minecraft/entity/ai/brain/sensor/TemptationsSensor;sense(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/mob/PathAwareEntity;)V
 *   Lnet/minecraft/entity/ai/brain/sensor/TemptationsSensor;test(Lnet/minecraft/entity/mob/PathAwareEntity;Lnet/minecraft/entity/player/PlayerEntity;)Z
 */
package net.minecraft.entity.ai.brain.sensor;

import com.google.common.collect.ImmutableSet;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.function.BiPredicate;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.sensor.Sensor;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.server.world.ServerWorld;

public class TemptationsSensor
extends Sensor<PathAwareEntity> {
    private static final TargetPredicate TEMPTER_PREDICATE = TargetPredicate.createNonAttackable().ignoreVisibility();
    private final BiPredicate<PathAwareEntity, ItemStack> predicate;

    public TemptationsSensor(Predicate<ItemStack> predicate) {
        this((PathAwareEntity entity, ItemStack stack) -> predicate.test((ItemStack)stack));
    }

    public static TemptationsSensor breedingItem() {
        return new TemptationsSensor((entity, stack) -> {
            if (entity instanceof AnimalEntity) {
                AnimalEntity lv = (AnimalEntity)entity;
                return lv.isBreedingItem((ItemStack)stack);
            }
            return false;
        });
    }

    private TemptationsSensor(BiPredicate<PathAwareEntity, ItemStack> predicate) {
        this.predicate = predicate;
    }

    @Override
    protected void sense(ServerWorld arg, PathAwareEntity arg2) {
        Brain<?> lv = arg2.getBrain();
        TargetPredicate lv2 = TEMPTER_PREDICATE.copy().setBaseMaxDistance((float)arg2.getAttributeValue(EntityAttributes.TEMPT_RANGE));
        List list = arg.getPlayers().stream().filter(EntityPredicates.EXCEPT_SPECTATOR).filter(player -> lv2.test(arg, arg2, (LivingEntity)player)).filter(player -> this.test(arg2, (PlayerEntity)player)).filter(playerx -> !arg2.hasPassenger((Entity)playerx)).sorted(Comparator.comparingDouble(arg2::squaredDistanceTo)).collect(Collectors.toList());
        if (!list.isEmpty()) {
            PlayerEntity lv3 = (PlayerEntity)list.get(0);
            lv.remember(MemoryModuleType.TEMPTING_PLAYER, lv3);
        } else {
            lv.forget(MemoryModuleType.TEMPTING_PLAYER);
        }
    }

    private boolean test(PathAwareEntity entity, PlayerEntity player) {
        return this.test(entity, player.getMainHandStack()) || this.test(entity, player.getOffHandStack());
    }

    private boolean test(PathAwareEntity entity, ItemStack stack) {
        return this.predicate.test(entity, stack);
    }

    @Override
    public Set<MemoryModuleType<?>> getOutputMemoryModules() {
        return ImmutableSet.of(MemoryModuleType.TEMPTING_PLAYER);
    }
}

