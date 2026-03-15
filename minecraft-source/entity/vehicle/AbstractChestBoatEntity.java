/*
 * External method calls:
 *   Lnet/minecraft/util/collection/DefaultedList;ofSize(ILjava/lang/Object;)Lnet/minecraft/util/collection/DefaultedList;
 *   Lnet/minecraft/entity/vehicle/AbstractBoatEntity;writeCustomData(Lnet/minecraft/storage/WriteView;)V
 *   Lnet/minecraft/entity/vehicle/AbstractBoatEntity;readCustomData(Lnet/minecraft/storage/ReadView;)V
 *   Lnet/minecraft/util/ItemScatterer;spawn(Lnet/minecraft/world/World;Lnet/minecraft/entity/Entity;Lnet/minecraft/inventory/Inventory;)V
 *   Lnet/minecraft/entity/vehicle/AbstractBoatEntity;interact(Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/util/Hand;)Lnet/minecraft/util/ActionResult;
 *   Lnet/minecraft/entity/mob/PiglinBrain;onGuardedBlockInteracted(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/player/PlayerEntity;Z)V
 *   Lnet/minecraft/entity/player/PlayerEntity;openHandledScreen(Lnet/minecraft/screen/NamedScreenHandlerFactory;)Ljava/util/OptionalInt;
 *   Lnet/minecraft/screen/GenericContainerScreenHandler;createGeneric9x3(ILnet/minecraft/entity/player/PlayerInventory;Lnet/minecraft/inventory/Inventory;)Lnet/minecraft/screen/GenericContainerScreenHandler;
 *   Lnet/minecraft/entity/ContainerUser;asLivingEntity()Lnet/minecraft/entity/LivingEntity;
 *   Lnet/minecraft/world/event/GameEvent$Emitter;of(Lnet/minecraft/entity/Entity;)Lnet/minecraft/world/event/GameEvent$Emitter;
 *   Lnet/minecraft/world/World;emitGameEvent(Lnet/minecraft/registry/entry/RegistryEntry;Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/world/event/GameEvent$Emitter;)V
 *
 * Internal private/static methods:
 *   Lnet/minecraft/entity/vehicle/AbstractChestBoatEntity;writeInventoryToData(Lnet/minecraft/storage/WriteView;)V
 *   Lnet/minecraft/entity/vehicle/AbstractChestBoatEntity;readInventoryFromData(Lnet/minecraft/storage/ReadView;)V
 *   Lnet/minecraft/entity/vehicle/AbstractChestBoatEntity;asItem()Lnet/minecraft/item/Item;
 *   Lnet/minecraft/entity/vehicle/AbstractChestBoatEntity;killAndDropItem(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/item/Item;)V
 *   Lnet/minecraft/entity/vehicle/AbstractChestBoatEntity;onBroken(Lnet/minecraft/entity/damage/DamageSource;Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/Entity;)V
 *   Lnet/minecraft/entity/vehicle/AbstractChestBoatEntity;open(Lnet/minecraft/entity/player/PlayerEntity;)Lnet/minecraft/util/ActionResult;
 *   Lnet/minecraft/entity/vehicle/AbstractChestBoatEntity;emitGameEvent(Lnet/minecraft/registry/entry/RegistryEntry;Lnet/minecraft/entity/Entity;)V
 *   Lnet/minecraft/entity/vehicle/AbstractChestBoatEntity;removeInventoryStack(II)Lnet/minecraft/item/ItemStack;
 *   Lnet/minecraft/entity/vehicle/AbstractChestBoatEntity;removeInventoryStack(I)Lnet/minecraft/item/ItemStack;
 *   Lnet/minecraft/entity/vehicle/AbstractChestBoatEntity;generateLoot(Lnet/minecraft/entity/player/PlayerEntity;)V
 *   Lnet/minecraft/entity/vehicle/AbstractChestBoatEntity;generateInventoryLoot(Lnet/minecraft/entity/player/PlayerEntity;)V
 */
package net.minecraft.entity.vehicle;

import java.util.function.Supplier;
import net.minecraft.entity.ContainerUser;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.RideableInventory;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.PiglinBrain;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.vehicle.AbstractBoatEntity;
import net.minecraft.entity.vehicle.VehicleInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootTable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.jspecify.annotations.Nullable;

public abstract class AbstractChestBoatEntity
extends AbstractBoatEntity
implements RideableInventory,
VehicleInventory {
    private static final int INVENTORY_SIZE = 27;
    private DefaultedList<ItemStack> inventory = DefaultedList.ofSize(27, ItemStack.EMPTY);
    private @Nullable RegistryKey<LootTable> lootTable;
    private long lootTableSeed;

    public AbstractChestBoatEntity(EntityType<? extends AbstractChestBoatEntity> arg, World arg2, Supplier<Item> supplier) {
        super(arg, arg2, supplier);
    }

    @Override
    protected float getPassengerHorizontalOffset() {
        return 0.15f;
    }

    @Override
    protected int getMaxPassengers() {
        return 1;
    }

    @Override
    protected void writeCustomData(WriteView view) {
        super.writeCustomData(view);
        this.writeInventoryToData(view);
    }

    @Override
    protected void readCustomData(ReadView view) {
        super.readCustomData(view);
        this.readInventoryFromData(view);
    }

    @Override
    public void killAndDropSelf(ServerWorld world, DamageSource damageSource) {
        this.killAndDropItem(world, this.asItem());
        this.onBroken(damageSource, world, this);
    }

    @Override
    public void remove(Entity.RemovalReason reason) {
        if (!this.getEntityWorld().isClient() && reason.shouldDestroy()) {
            ItemScatterer.spawn(this.getEntityWorld(), this, (Inventory)this);
        }
        super.remove(reason);
    }

    @Override
    public ActionResult interact(PlayerEntity player, Hand hand) {
        ActionResult lv = super.interact(player, hand);
        if (lv != ActionResult.PASS) {
            return lv;
        }
        if (!this.canAddPassenger(player) || player.shouldCancelInteraction()) {
            World world;
            ActionResult lv2 = this.open(player);
            if (lv2.isAccepted() && (world = player.getEntityWorld()) instanceof ServerWorld) {
                ServerWorld lv3 = (ServerWorld)world;
                this.emitGameEvent(GameEvent.CONTAINER_OPEN, player);
                PiglinBrain.onGuardedBlockInteracted(lv3, player, true);
            }
            return lv2;
        }
        return ActionResult.PASS;
    }

    @Override
    public void openInventory(PlayerEntity player) {
        player.openHandledScreen(this);
        World world = player.getEntityWorld();
        if (world instanceof ServerWorld) {
            ServerWorld lv = (ServerWorld)world;
            this.emitGameEvent(GameEvent.CONTAINER_OPEN, player);
            PiglinBrain.onGuardedBlockInteracted(lv, player, true);
        }
    }

    @Override
    public void clear() {
        this.clearInventory();
    }

    @Override
    public int size() {
        return 27;
    }

    @Override
    public ItemStack getStack(int slot) {
        return this.getInventoryStack(slot);
    }

    @Override
    public ItemStack removeStack(int slot, int amount) {
        return this.removeInventoryStack(slot, amount);
    }

    @Override
    public ItemStack removeStack(int slot) {
        return this.removeInventoryStack(slot);
    }

    @Override
    public void setStack(int slot, ItemStack stack) {
        this.setInventoryStack(slot, stack);
    }

    @Override
    public StackReference getStackReference(int slot) {
        return this.getInventoryStackReference(slot);
    }

    @Override
    public void markDirty() {
    }

    @Override
    public boolean canPlayerUse(PlayerEntity player) {
        return this.canPlayerAccess(player);
    }

    @Override
    public @Nullable ScreenHandler createMenu(int i, PlayerInventory arg, PlayerEntity arg2) {
        if (this.lootTable == null || !arg2.isSpectator()) {
            this.generateLoot(arg.player);
            return GenericContainerScreenHandler.createGeneric9x3(i, arg, this);
        }
        return null;
    }

    public void generateLoot(@Nullable PlayerEntity player) {
        this.generateInventoryLoot(player);
    }

    @Override
    public @Nullable RegistryKey<LootTable> getLootTable() {
        return this.lootTable;
    }

    @Override
    public void setLootTable(@Nullable RegistryKey<LootTable> lootTable) {
        this.lootTable = lootTable;
    }

    @Override
    public long getLootTableSeed() {
        return this.lootTableSeed;
    }

    @Override
    public void setLootTableSeed(long lootTableSeed) {
        this.lootTableSeed = lootTableSeed;
    }

    @Override
    public DefaultedList<ItemStack> getInventory() {
        return this.inventory;
    }

    @Override
    public void resetInventory() {
        this.inventory = DefaultedList.ofSize(this.size(), ItemStack.EMPTY);
    }

    @Override
    public void onClose(ContainerUser user) {
        this.getEntityWorld().emitGameEvent(GameEvent.CONTAINER_CLOSE, this.getEntityPos(), GameEvent.Emitter.of(user.asLivingEntity()));
    }
}

