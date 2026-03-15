/*
 * External method calls:
 *   Lnet/minecraft/block/BlockState;with(Lnet/minecraft/state/property/Property;Ljava/lang/Comparable;)Ljava/lang/Object;
 *   Lnet/minecraft/screen/GenericContainerScreenHandler;createGeneric9x3(ILnet/minecraft/entity/player/PlayerInventory;Lnet/minecraft/inventory/Inventory;)Lnet/minecraft/screen/GenericContainerScreenHandler;
 *   Lnet/minecraft/entity/ContainerUser;asLivingEntity()Lnet/minecraft/entity/LivingEntity;
 *   Lnet/minecraft/world/event/GameEvent$Emitter;of(Lnet/minecraft/entity/Entity;)Lnet/minecraft/world/event/GameEvent$Emitter;
 *   Lnet/minecraft/world/World;emitGameEvent(Lnet/minecraft/registry/entry/RegistryEntry;Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/world/event/GameEvent$Emitter;)V
 *   Lnet/minecraft/entity/mob/PiglinBrain;onGuardedBlockInteracted(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/player/PlayerEntity;Z)V
 *
 * Internal private/static methods:
 *   Lnet/minecraft/entity/vehicle/ChestMinecartEntity;open(Lnet/minecraft/entity/player/PlayerEntity;)Lnet/minecraft/util/ActionResult;
 *   Lnet/minecraft/entity/vehicle/ChestMinecartEntity;emitGameEvent(Lnet/minecraft/registry/entry/RegistryEntry;Lnet/minecraft/entity/Entity;)V
 */
package net.minecraft.entity.vehicle;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ChestBlock;
import net.minecraft.entity.ContainerUser;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.PiglinBrain;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.vehicle.StorageMinecartEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

public class ChestMinecartEntity
extends StorageMinecartEntity {
    public ChestMinecartEntity(EntityType<? extends ChestMinecartEntity> arg, World arg2) {
        super(arg, arg2);
    }

    @Override
    protected Item asItem() {
        return Items.CHEST_MINECART;
    }

    @Override
    public ItemStack getPickBlockStack() {
        return new ItemStack(Items.CHEST_MINECART);
    }

    @Override
    public int size() {
        return 27;
    }

    @Override
    public BlockState getDefaultContainedBlock() {
        return (BlockState)Blocks.CHEST.getDefaultState().with(ChestBlock.FACING, Direction.NORTH);
    }

    @Override
    public int getDefaultBlockOffset() {
        return 8;
    }

    @Override
    public ScreenHandler getScreenHandler(int syncId, PlayerInventory playerInventory) {
        return GenericContainerScreenHandler.createGeneric9x3(syncId, playerInventory, this);
    }

    @Override
    public void onClose(ContainerUser user) {
        this.getEntityWorld().emitGameEvent(GameEvent.CONTAINER_CLOSE, this.getEntityPos(), GameEvent.Emitter.of(user.asLivingEntity()));
    }

    @Override
    public ActionResult interact(PlayerEntity player, Hand hand) {
        World world;
        ActionResult lv = this.open(player);
        if (lv.isAccepted() && (world = player.getEntityWorld()) instanceof ServerWorld) {
            ServerWorld lv2 = (ServerWorld)world;
            this.emitGameEvent(GameEvent.CONTAINER_OPEN, player);
            PiglinBrain.onGuardedBlockInteracted(lv2, player, true);
        }
        return lv;
    }
}

