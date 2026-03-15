/*
 * External method calls:
 *   Lnet/minecraft/storage/WriteView;putString(Ljava/lang/String;Ljava/lang/String;)V
 *   Lnet/minecraft/storage/WriteView;putLong(Ljava/lang/String;J)V
 *   Lnet/minecraft/inventory/Inventories;writeData(Lnet/minecraft/storage/WriteView;Lnet/minecraft/util/collection/DefaultedList;)V
 *   Lnet/minecraft/storage/ReadView;read(Ljava/lang/String;Lcom/mojang/serialization/Codec;)Ljava/util/Optional;
 *   Lnet/minecraft/inventory/Inventories;readData(Lnet/minecraft/storage/ReadView;Lnet/minecraft/util/collection/DefaultedList;)V
 *   Lnet/minecraft/util/ItemScatterer;spawn(Lnet/minecraft/world/World;Lnet/minecraft/entity/Entity;Lnet/minecraft/inventory/Inventory;)V
 *   Lnet/minecraft/entity/mob/PiglinBrain;onGuardedBlockInteracted(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/player/PlayerEntity;Z)V
 *   Lnet/minecraft/entity/player/PlayerEntity;openHandledScreen(Lnet/minecraft/screen/NamedScreenHandlerFactory;)Ljava/util/OptionalInt;
 *   Lnet/minecraft/advancement/criterion/PlayerGeneratesContainerLootCriterion;trigger(Lnet/minecraft/server/network/ServerPlayerEntity;Lnet/minecraft/registry/RegistryKey;)V
 *   Lnet/minecraft/loot/context/LootWorldContext$Builder;luck(F)Lnet/minecraft/loot/context/LootWorldContext$Builder;
 *   Lnet/minecraft/loot/context/LootWorldContext$Builder;build(Lnet/minecraft/util/context/ContextType;)Lnet/minecraft/loot/context/LootWorldContext;
 *   Lnet/minecraft/loot/LootTable;supplyInventory(Lnet/minecraft/inventory/Inventory;Lnet/minecraft/loot/context/LootWorldContext;J)V
 *   Lnet/minecraft/inventory/Inventories;splitStack(Ljava/util/List;II)Lnet/minecraft/item/ItemStack;
 *   Lnet/minecraft/item/ItemStack;capCount(I)V
 *
 * Internal private/static methods:
 *   Lnet/minecraft/entity/vehicle/VehicleInventory;generateInventoryLoot(Lnet/minecraft/entity/player/PlayerEntity;)V
 */
package net.minecraft.entity.vehicle;

import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.PiglinBrain;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.loot.context.LootWorldContext;
import net.minecraft.registry.RegistryKey;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.rule.GameRules;
import org.jspecify.annotations.Nullable;

public interface VehicleInventory
extends Inventory,
NamedScreenHandlerFactory {
    public Vec3d getEntityPos();

    public Box getBoundingBox();

    public @Nullable RegistryKey<LootTable> getLootTable();

    public void setLootTable(@Nullable RegistryKey<LootTable> var1);

    public long getLootTableSeed();

    public void setLootTableSeed(long var1);

    public DefaultedList<ItemStack> getInventory();

    public void resetInventory();

    public World getEntityWorld();

    public boolean isRemoved();

    @Override
    default public boolean isEmpty() {
        return this.isInventoryEmpty();
    }

    default public void writeInventoryToData(WriteView view) {
        if (this.getLootTable() != null) {
            view.putString("LootTable", this.getLootTable().getValue().toString());
            if (this.getLootTableSeed() != 0L) {
                view.putLong("LootTableSeed", this.getLootTableSeed());
            }
        } else {
            Inventories.writeData(view, this.getInventory());
        }
    }

    default public void readInventoryFromData(ReadView view) {
        this.resetInventory();
        RegistryKey lv = view.read("LootTable", LootTable.TABLE_KEY).orElse(null);
        this.setLootTable(lv);
        this.setLootTableSeed(view.getLong("LootTableSeed", 0L));
        if (lv == null) {
            Inventories.readData(view, this.getInventory());
        }
    }

    default public void onBroken(DamageSource source, ServerWorld world, Entity vehicle) {
        if (!world.getGameRules().getValue(GameRules.ENTITY_DROPS).booleanValue()) {
            return;
        }
        ItemScatterer.spawn((World)world, vehicle, (Inventory)this);
        Entity lv = source.getSource();
        if (lv != null && lv.getType() == EntityType.PLAYER) {
            PiglinBrain.onGuardedBlockInteracted(world, (PlayerEntity)lv, true);
        }
    }

    default public ActionResult open(PlayerEntity player) {
        player.openHandledScreen(this);
        return ActionResult.SUCCESS;
    }

    default public void generateInventoryLoot(@Nullable PlayerEntity player) {
        MinecraftServer minecraftServer = this.getEntityWorld().getServer();
        if (this.getLootTable() != null && minecraftServer != null) {
            LootTable lv = minecraftServer.getReloadableRegistries().getLootTable(this.getLootTable());
            if (player != null) {
                Criteria.PLAYER_GENERATES_CONTAINER_LOOT.trigger((ServerPlayerEntity)player, this.getLootTable());
            }
            this.setLootTable(null);
            LootWorldContext.Builder lv2 = new LootWorldContext.Builder((ServerWorld)this.getEntityWorld()).add(LootContextParameters.ORIGIN, this.getEntityPos());
            if (player != null) {
                lv2.luck(player.getLuck()).add(LootContextParameters.THIS_ENTITY, player);
            }
            lv.supplyInventory(this, lv2.build(LootContextTypes.CHEST), this.getLootTableSeed());
        }
    }

    default public void clearInventory() {
        this.generateInventoryLoot(null);
        this.getInventory().clear();
    }

    default public boolean isInventoryEmpty() {
        for (ItemStack lv : this.getInventory()) {
            if (lv.isEmpty()) continue;
            return false;
        }
        return true;
    }

    default public ItemStack removeInventoryStack(int slot) {
        this.generateInventoryLoot(null);
        ItemStack lv = this.getInventory().get(slot);
        if (lv.isEmpty()) {
            return ItemStack.EMPTY;
        }
        this.getInventory().set(slot, ItemStack.EMPTY);
        return lv;
    }

    default public ItemStack getInventoryStack(int slot) {
        this.generateInventoryLoot(null);
        return this.getInventory().get(slot);
    }

    default public ItemStack removeInventoryStack(int slot, int amount) {
        this.generateInventoryLoot(null);
        return Inventories.splitStack(this.getInventory(), slot, amount);
    }

    default public void setInventoryStack(int slot, ItemStack stack) {
        this.generateInventoryLoot(null);
        this.getInventory().set(slot, stack);
        stack.capCount(this.getMaxCount(stack));
    }

    default public @Nullable StackReference getInventoryStackReference(final int slot) {
        if (slot >= 0 && slot < this.size()) {
            return new StackReference(){

                @Override
                public ItemStack get() {
                    return VehicleInventory.this.getInventoryStack(slot);
                }

                @Override
                public boolean set(ItemStack stack) {
                    VehicleInventory.this.setInventoryStack(slot, stack);
                    return true;
                }
            };
        }
        return null;
    }

    default public boolean canPlayerAccess(PlayerEntity player) {
        return !this.isRemoved() && player.canInteractWithEntityIn(this.getBoundingBox(), 4.0);
    }
}

