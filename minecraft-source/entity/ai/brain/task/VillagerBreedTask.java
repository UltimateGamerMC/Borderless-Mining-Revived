/*
 * External method calls:
 *   Lnet/minecraft/entity/ai/brain/task/TargetUtil;lookAtAndWalkTowardsEachOther(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/entity/LivingEntity;FI)V
 *   Lnet/minecraft/server/world/ServerWorld;sendEntityStatus(Lnet/minecraft/entity/Entity;B)V
 *   Lnet/minecraft/entity/passive/VillagerEntity;squaredDistanceTo(Lnet/minecraft/entity/Entity;)D
 *   Lnet/minecraft/world/poi/PointOfInterestStorage;releaseTicket(Lnet/minecraft/util/math/BlockPos;)Z
 *   Lnet/minecraft/server/debug/SubscriptionTracker;onPoiUpdated(Lnet/minecraft/util/math/BlockPos;)V
 *   Lnet/minecraft/entity/ai/brain/Brain;forget(Lnet/minecraft/entity/ai/brain/MemoryModuleType;)V
 *   Lnet/minecraft/entity/ai/pathing/EntityNavigation;findPathTo(Lnet/minecraft/util/math/BlockPos;I)Lnet/minecraft/entity/ai/pathing/Path;
 *   Lnet/minecraft/entity/passive/VillagerEntity;createChild(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/passive/PassiveEntity;)Lnet/minecraft/entity/passive/VillagerEntity;
 *   Lnet/minecraft/entity/passive/VillagerEntity;refreshPositionAndAngles(DDDFF)V
 *   Lnet/minecraft/server/world/ServerWorld;spawnEntityAndPassengers(Lnet/minecraft/entity/Entity;)V
 *   Lnet/minecraft/entity/ai/brain/Brain;remember(Lnet/minecraft/entity/ai/brain/MemoryModuleType;Ljava/lang/Object;)V
 *   Lnet/minecraft/registry/entry/RegistryEntry;matchesKey(Lnet/minecraft/registry/RegistryKey;)Z
 *
 * Internal private/static methods:
 *   Lnet/minecraft/entity/ai/brain/task/VillagerBreedTask;goHome(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/passive/VillagerEntity;Lnet/minecraft/entity/passive/VillagerEntity;)V
 *   Lnet/minecraft/entity/ai/brain/task/VillagerBreedTask;createChild(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/passive/VillagerEntity;Lnet/minecraft/entity/passive/VillagerEntity;)Ljava/util/Optional;
 *   Lnet/minecraft/entity/ai/brain/task/VillagerBreedTask;finishRunning(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/passive/VillagerEntity;J)V
 *   Lnet/minecraft/entity/ai/brain/task/VillagerBreedTask;keepRunning(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/passive/VillagerEntity;J)V
 *   Lnet/minecraft/entity/ai/brain/task/VillagerBreedTask;run(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/passive/VillagerEntity;J)V
 */
package net.minecraft.entity.ai.brain.task;

import com.google.common.collect.ImmutableMap;
import java.util.Optional;
import net.minecraft.entity.EntityStatuses;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.MemoryModuleState;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.MultiTickTask;
import net.minecraft.entity.ai.brain.task.TargetUtil;
import net.minecraft.entity.ai.pathing.Path;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.GlobalPos;
import net.minecraft.world.poi.PointOfInterestType;
import net.minecraft.world.poi.PointOfInterestTypes;

public class VillagerBreedTask
extends MultiTickTask<VillagerEntity> {
    private long breedEndTime;

    public VillagerBreedTask() {
        super(ImmutableMap.of(MemoryModuleType.BREED_TARGET, MemoryModuleState.VALUE_PRESENT, MemoryModuleType.VISIBLE_MOBS, MemoryModuleState.VALUE_PRESENT), 350, 350);
    }

    @Override
    protected boolean shouldRun(ServerWorld arg, VillagerEntity arg2) {
        return this.isReadyToBreed(arg2);
    }

    @Override
    protected boolean shouldKeepRunning(ServerWorld arg, VillagerEntity arg2, long l) {
        return l <= this.breedEndTime && this.isReadyToBreed(arg2);
    }

    @Override
    protected void run(ServerWorld arg, VillagerEntity arg2, long l) {
        PassiveEntity lv = arg2.getBrain().getOptionalRegisteredMemory(MemoryModuleType.BREED_TARGET).get();
        TargetUtil.lookAtAndWalkTowardsEachOther(arg2, lv, 0.5f, 2);
        arg.sendEntityStatus(lv, EntityStatuses.ADD_BREEDING_PARTICLES);
        arg.sendEntityStatus(arg2, EntityStatuses.ADD_BREEDING_PARTICLES);
        int i = 275 + arg2.getRandom().nextInt(50);
        this.breedEndTime = l + (long)i;
    }

    @Override
    protected void keepRunning(ServerWorld arg, VillagerEntity arg2, long l) {
        VillagerEntity lv = (VillagerEntity)arg2.getBrain().getOptionalRegisteredMemory(MemoryModuleType.BREED_TARGET).get();
        if (arg2.squaredDistanceTo(lv) > 5.0) {
            return;
        }
        TargetUtil.lookAtAndWalkTowardsEachOther(arg2, lv, 0.5f, 2);
        if (l >= this.breedEndTime) {
            arg2.eatForBreeding();
            lv.eatForBreeding();
            this.goHome(arg, arg2, lv);
        } else if (arg2.getRandom().nextInt(35) == 0) {
            arg.sendEntityStatus(lv, EntityStatuses.ADD_VILLAGER_HEART_PARTICLES);
            arg.sendEntityStatus(arg2, EntityStatuses.ADD_VILLAGER_HEART_PARTICLES);
        }
    }

    private void goHome(ServerWorld world, VillagerEntity first, VillagerEntity second) {
        Optional<BlockPos> optional = this.getReachableHome(world, first);
        if (optional.isEmpty()) {
            world.sendEntityStatus(second, EntityStatuses.ADD_VILLAGER_ANGRY_PARTICLES);
            world.sendEntityStatus(first, EntityStatuses.ADD_VILLAGER_ANGRY_PARTICLES);
        } else {
            Optional<VillagerEntity> optional2 = this.createChild(world, first, second);
            if (optional2.isPresent()) {
                this.setChildHome(world, optional2.get(), optional.get());
            } else {
                world.getPointOfInterestStorage().releaseTicket(optional.get());
                world.getSubscriptionTracker().onPoiUpdated(optional.get());
            }
        }
    }

    @Override
    protected void finishRunning(ServerWorld arg, VillagerEntity arg2, long l) {
        arg2.getBrain().forget(MemoryModuleType.BREED_TARGET);
    }

    private boolean isReadyToBreed(VillagerEntity villager) {
        Brain<VillagerEntity> lv = villager.getBrain();
        Optional<PassiveEntity> optional = lv.getOptionalRegisteredMemory(MemoryModuleType.BREED_TARGET).filter(arg -> arg.getType() == EntityType.VILLAGER);
        if (optional.isEmpty()) {
            return false;
        }
        return TargetUtil.canSee(lv, MemoryModuleType.BREED_TARGET, EntityType.VILLAGER) && villager.isReadyToBreed() && optional.get().isReadyToBreed();
    }

    private Optional<BlockPos> getReachableHome(ServerWorld world, VillagerEntity villager) {
        return world.getPointOfInterestStorage().getPosition(poiType -> poiType.matchesKey(PointOfInterestTypes.HOME), (poiType, pos) -> this.canReachHome(villager, (BlockPos)pos, (RegistryEntry<PointOfInterestType>)poiType), villager.getBlockPos(), 48);
    }

    private boolean canReachHome(VillagerEntity villager, BlockPos pos, RegistryEntry<PointOfInterestType> poiType) {
        Path lv = villager.getNavigation().findPathTo(pos, poiType.value().searchDistance());
        return lv != null && lv.reachesTarget();
    }

    private Optional<VillagerEntity> createChild(ServerWorld world, VillagerEntity parent, VillagerEntity partner) {
        VillagerEntity lv = parent.createChild(world, partner);
        if (lv == null) {
            return Optional.empty();
        }
        parent.setBreedingAge(6000);
        partner.setBreedingAge(6000);
        lv.setBreedingAge(-24000);
        lv.refreshPositionAndAngles(parent.getX(), parent.getY(), parent.getZ(), 0.0f, 0.0f);
        world.spawnEntityAndPassengers(lv);
        world.sendEntityStatus(lv, EntityStatuses.ADD_VILLAGER_HEART_PARTICLES);
        return Optional.of(lv);
    }

    private void setChildHome(ServerWorld world, VillagerEntity child, BlockPos pos) {
        GlobalPos lv = GlobalPos.create(world.getRegistryKey(), pos);
        child.getBrain().remember(MemoryModuleType.HOME, lv);
    }

    @Override
    protected /* synthetic */ void finishRunning(ServerWorld world, LivingEntity entity, long time) {
        this.finishRunning(world, (VillagerEntity)entity, time);
    }

    @Override
    protected /* synthetic */ void run(ServerWorld world, LivingEntity entity, long time) {
        this.run(world, (VillagerEntity)entity, time);
    }
}

