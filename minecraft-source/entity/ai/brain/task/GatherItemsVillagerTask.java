/*
 * External method calls:
 *   Lnet/minecraft/entity/ai/brain/task/TargetUtil;lookAtAndWalkTowardsEachOther(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/entity/LivingEntity;FI)V
 *   Lnet/minecraft/entity/passive/VillagerEntity;squaredDistanceTo(Lnet/minecraft/entity/Entity;)D
 *   Lnet/minecraft/entity/passive/VillagerEntity;talkWithVillager(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/passive/VillagerEntity;J)V
 *   Lnet/minecraft/village/VillagerData;profession()Lnet/minecraft/registry/entry/RegistryEntry;
 *   Lnet/minecraft/registry/entry/RegistryEntry;matchesKey(Lnet/minecraft/registry/RegistryKey;)Z
 *   Lnet/minecraft/inventory/SimpleInventory;count(Lnet/minecraft/item/Item;)I
 *   Lnet/minecraft/inventory/SimpleInventory;containsAny(Ljava/util/Set;)Z
 *   Lnet/minecraft/entity/ai/brain/Brain;forget(Lnet/minecraft/entity/ai/brain/MemoryModuleType;)V
 *   Lnet/minecraft/village/VillagerProfession;gatherableItems()Lcom/google/common/collect/ImmutableSet;
 *   Lnet/minecraft/item/ItemStack;decrement(I)V
 *   Lnet/minecraft/entity/ai/brain/task/TargetUtil;give(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/item/ItemStack;Lnet/minecraft/util/math/Vec3d;)V
 *
 * Internal private/static methods:
 *   Lnet/minecraft/entity/ai/brain/task/GatherItemsVillagerTask;giveHalfOfStack(Lnet/minecraft/entity/passive/VillagerEntity;Ljava/util/Set;Lnet/minecraft/entity/LivingEntity;)V
 *   Lnet/minecraft/entity/ai/brain/task/GatherItemsVillagerTask;finishRunning(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/passive/VillagerEntity;J)V
 *   Lnet/minecraft/entity/ai/brain/task/GatherItemsVillagerTask;keepRunning(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/passive/VillagerEntity;J)V
 *   Lnet/minecraft/entity/ai/brain/task/GatherItemsVillagerTask;run(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/passive/VillagerEntity;J)V
 */
package net.minecraft.entity.ai.brain.task;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import java.util.Set;
import java.util.stream.Collectors;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.MemoryModuleState;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.MultiTickTask;
import net.minecraft.entity.ai.brain.task.TargetUtil;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.village.VillagerProfession;

public class GatherItemsVillagerTask
extends MultiTickTask<VillagerEntity> {
    private Set<Item> items = ImmutableSet.of();

    public GatherItemsVillagerTask() {
        super(ImmutableMap.of(MemoryModuleType.INTERACTION_TARGET, MemoryModuleState.VALUE_PRESENT, MemoryModuleType.VISIBLE_MOBS, MemoryModuleState.VALUE_PRESENT));
    }

    @Override
    protected boolean shouldRun(ServerWorld arg, VillagerEntity arg2) {
        return TargetUtil.canSee(arg2.getBrain(), MemoryModuleType.INTERACTION_TARGET, EntityType.VILLAGER);
    }

    @Override
    protected boolean shouldKeepRunning(ServerWorld arg, VillagerEntity arg2, long l) {
        return this.shouldRun(arg, arg2);
    }

    @Override
    protected void run(ServerWorld arg, VillagerEntity arg2, long l) {
        VillagerEntity lv = (VillagerEntity)arg2.getBrain().getOptionalRegisteredMemory(MemoryModuleType.INTERACTION_TARGET).get();
        TargetUtil.lookAtAndWalkTowardsEachOther(arg2, lv, 0.5f, 2);
        this.items = GatherItemsVillagerTask.getGatherableItems(arg2, lv);
    }

    @Override
    protected void keepRunning(ServerWorld arg, VillagerEntity arg2, long l) {
        VillagerEntity lv = (VillagerEntity)arg2.getBrain().getOptionalRegisteredMemory(MemoryModuleType.INTERACTION_TARGET).get();
        if (arg2.squaredDistanceTo(lv) > 5.0) {
            return;
        }
        TargetUtil.lookAtAndWalkTowardsEachOther(arg2, lv, 0.5f, 2);
        arg2.talkWithVillager(arg, lv, l);
        boolean bl = arg2.getVillagerData().profession().matchesKey(VillagerProfession.FARMER);
        if (arg2.canShareFoodForBreeding() && (bl || lv.needsFoodForBreeding())) {
            GatherItemsVillagerTask.giveHalfOfStack(arg2, VillagerEntity.ITEM_FOOD_VALUES.keySet(), lv);
        }
        if (bl && arg2.getInventory().count(Items.WHEAT) > Items.WHEAT.getMaxCount() / 2) {
            GatherItemsVillagerTask.giveHalfOfStack(arg2, ImmutableSet.of(Items.WHEAT), lv);
        }
        if (!this.items.isEmpty() && arg2.getInventory().containsAny(this.items)) {
            GatherItemsVillagerTask.giveHalfOfStack(arg2, this.items, lv);
        }
    }

    @Override
    protected void finishRunning(ServerWorld arg, VillagerEntity arg2, long l) {
        arg2.getBrain().forget(MemoryModuleType.INTERACTION_TARGET);
    }

    private static Set<Item> getGatherableItems(VillagerEntity entity, VillagerEntity target) {
        ImmutableSet<Item> immutableSet = target.getVillagerData().profession().value().gatherableItems();
        ImmutableSet<Item> immutableSet2 = entity.getVillagerData().profession().value().gatherableItems();
        return immutableSet.stream().filter(item -> !immutableSet2.contains(item)).collect(Collectors.toSet());
    }

    private static void giveHalfOfStack(VillagerEntity villager, Set<Item> validItems, LivingEntity target) {
        SimpleInventory lv = villager.getInventory();
        ItemStack lv2 = ItemStack.EMPTY;
        for (int i = 0; i < lv.size(); ++i) {
            int j;
            Item lv4;
            ItemStack lv3 = lv.getStack(i);
            if (lv3.isEmpty() || !validItems.contains(lv4 = lv3.getItem())) continue;
            if (lv3.getCount() > lv3.getMaxCount() / 2) {
                j = lv3.getCount() / 2;
            } else {
                if (lv3.getCount() <= 24) continue;
                j = lv3.getCount() - 24;
            }
            lv3.decrement(j);
            lv2 = new ItemStack(lv4, j);
            break;
        }
        if (!lv2.isEmpty()) {
            TargetUtil.give(villager, lv2, target.getEntityPos());
        }
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

