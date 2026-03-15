/*
 * External method calls:
 *   Lnet/minecraft/entity/ai/brain/task/TaskTriggerer;task(Ljava/util/function/Function;)Lnet/minecraft/entity/ai/brain/task/SingleTickTask;
 *   Lnet/minecraft/entity/ai/brain/task/TaskTriggerer$TaskContext;queryMemoryValue(Lnet/minecraft/entity/ai/brain/MemoryModuleType;)Lnet/minecraft/entity/ai/brain/task/TaskTriggerer;
 *   Lnet/minecraft/entity/ai/brain/task/TaskTriggerer$TaskContext;queryMemoryOptional(Lnet/minecraft/entity/ai/brain/MemoryModuleType;)Lnet/minecraft/entity/ai/brain/task/TaskTriggerer;
 *   Lnet/minecraft/entity/ai/brain/task/TaskTriggerer$TaskContext;group(Lcom/mojang/datafixers/kinds/App;Lcom/mojang/datafixers/kinds/App;)Lcom/mojang/datafixers/Products$P2;
 *   Lnet/minecraft/entity/ai/brain/MemoryQueryResult;remember(Ljava/lang/Object;)V
 *   Lnet/minecraft/server/world/ServerWorld;sendEntityStatus(Lnet/minecraft/entity/Entity;B)V
 *   Lnet/minecraft/village/VillagerData;profession()Lnet/minecraft/registry/entry/RegistryEntry;
 *   Lnet/minecraft/registry/entry/RegistryEntry;matchesKey(Lnet/minecraft/registry/RegistryKey;)Z
 *   Lnet/minecraft/village/VillagerData;withProfession(Lnet/minecraft/registry/entry/RegistryEntry;)Lnet/minecraft/village/VillagerData;
 *   Lnet/minecraft/entity/passive/VillagerEntity;reinitializeBrain(Lnet/minecraft/server/world/ServerWorld;)V
 *   Lnet/minecraft/registry/DefaultedRegistry;streamEntries()Ljava/util/stream/Stream;
 *   Lnet/minecraft/village/VillagerProfession;heldWorkstation()Ljava/util/function/Predicate;
 */
package net.minecraft.entity.ai.brain.task;

import java.util.Optional;
import net.minecraft.entity.EntityStatuses;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.Task;
import net.minecraft.entity.ai.brain.task.TaskTriggerer;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.GlobalPos;
import net.minecraft.village.VillagerProfession;
import net.minecraft.world.poi.PointOfInterestType;

public class UpdateJobSiteTask {
    public static Task<VillagerEntity> create() {
        return TaskTriggerer.task(context -> context.group(context.queryMemoryValue(MemoryModuleType.POTENTIAL_JOB_SITE), context.queryMemoryOptional(MemoryModuleType.JOB_SITE)).apply(context, (potentialJobSite, jobSite) -> (world, entity, time) -> {
            GlobalPos lv = (GlobalPos)context.getValue(potentialJobSite);
            if (!lv.pos().isWithinDistance(entity.getEntityPos(), 2.0) && !entity.isNatural()) {
                return false;
            }
            potentialJobSite.forget();
            jobSite.remember(lv);
            world.sendEntityStatus(entity, EntityStatuses.ADD_VILLAGER_HAPPY_PARTICLES);
            if (!entity.getVillagerData().profession().matchesKey(VillagerProfession.NONE)) {
                return true;
            }
            MinecraftServer minecraftServer = world.getServer();
            Optional.ofNullable(minecraftServer.getWorld(lv.dimension())).flatMap(jobSiteWorld -> jobSiteWorld.getPointOfInterestStorage().getType(lv.pos())).flatMap(poiType -> Registries.VILLAGER_PROFESSION.streamEntries().filter(profession -> ((VillagerProfession)profession.value()).heldWorkstation().test((RegistryEntry<PointOfInterestType>)poiType)).findFirst()).ifPresent(profession -> {
                entity.setVillagerData(entity.getVillagerData().withProfession((RegistryEntry<VillagerProfession>)profession));
                entity.reinitializeBrain(world);
            });
            return true;
        }));
    }
}

