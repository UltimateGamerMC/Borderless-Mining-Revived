/*
 * External method calls:
 *   Lnet/minecraft/block/entity/BlockEntity;readData(Lnet/minecraft/storage/ReadView;)V
 *   Lnet/minecraft/inventory/ContainerLock;read(Lnet/minecraft/storage/ReadView;)Lnet/minecraft/inventory/ContainerLock;
 *   Lnet/minecraft/block/entity/BlockEntity;writeData(Lnet/minecraft/storage/WriteView;)V
 *   Lnet/minecraft/inventory/ContainerLock;write(Lnet/minecraft/storage/WriteView;)V
 *   Lnet/minecraft/storage/WriteView;putNullable(Ljava/lang/String;Lcom/mojang/serialization/Codec;Ljava/lang/Object;)V
 *   Lnet/minecraft/inventory/ContainerLock;checkUnlocked(Lnet/minecraft/entity/player/PlayerEntity;)Z
 *   Lnet/minecraft/text/Text;translatable(Ljava/lang/String;[Ljava/lang/Object;)Lnet/minecraft/text/MutableText;
 *   Lnet/minecraft/entity/player/PlayerEntity;sendMessage(Lnet/minecraft/text/Text;Z)V
 *   Lnet/minecraft/world/World;playSound(Lnet/minecraft/entity/Entity;DDDLnet/minecraft/sound/SoundEvent;Lnet/minecraft/sound/SoundCategory;FF)V
 *   Lnet/minecraft/inventory/Inventories;splitStack(Ljava/util/List;II)Lnet/minecraft/item/ItemStack;
 *   Lnet/minecraft/inventory/Inventories;removeStack(Ljava/util/List;I)Lnet/minecraft/item/ItemStack;
 *   Lnet/minecraft/item/ItemStack;capCount(I)V
 *   Lnet/minecraft/block/entity/BlockEntity;readComponents(Lnet/minecraft/component/ComponentsAccess;)V
 *   Lnet/minecraft/component/type/ContainerComponent;copyTo(Lnet/minecraft/util/collection/DefaultedList;)V
 *   Lnet/minecraft/block/entity/BlockEntity;addComponents(Lnet/minecraft/component/ComponentMap$Builder;)V
 *   Lnet/minecraft/component/type/ContainerComponent;fromStacks(Ljava/util/List;)Lnet/minecraft/component/type/ContainerComponent;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/block/entity/LockableContainerBlockEntity;tryParseCustomName(Lnet/minecraft/storage/ReadView;Ljava/lang/String;)Lnet/minecraft/text/Text;
 *   Lnet/minecraft/block/entity/LockableContainerBlockEntity;checkUnlocked(Lnet/minecraft/entity/player/PlayerEntity;)Z
 *   Lnet/minecraft/block/entity/LockableContainerBlockEntity;createScreenHandler(ILnet/minecraft/entity/player/PlayerInventory;)Lnet/minecraft/screen/ScreenHandler;
 *   Lnet/minecraft/block/entity/LockableContainerBlockEntity;handleLocked(Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/text/Text;)V
 */
package net.minecraft.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.component.ComponentMap;
import net.minecraft.component.ComponentsAccess;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ContainerComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.ContainerLock;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.text.Text;
import net.minecraft.text.TextCodecs;
import net.minecraft.util.Nameable;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jspecify.annotations.Nullable;

public abstract class LockableContainerBlockEntity
extends BlockEntity
implements Inventory,
NamedScreenHandlerFactory,
Nameable {
    private ContainerLock lock = ContainerLock.EMPTY;
    private @Nullable Text customName;

    protected LockableContainerBlockEntity(BlockEntityType<?> arg, BlockPos arg2, BlockState arg3) {
        super(arg, arg2, arg3);
    }

    @Override
    protected void readData(ReadView view) {
        super.readData(view);
        this.lock = ContainerLock.read(view);
        this.customName = LockableContainerBlockEntity.tryParseCustomName(view, "CustomName");
    }

    @Override
    protected void writeData(WriteView view) {
        super.writeData(view);
        this.lock.write(view);
        view.putNullable("CustomName", TextCodecs.CODEC, this.customName);
    }

    @Override
    public Text getName() {
        if (this.customName != null) {
            return this.customName;
        }
        return this.getContainerName();
    }

    @Override
    public Text getDisplayName() {
        return this.getName();
    }

    @Override
    public @Nullable Text getCustomName() {
        return this.customName;
    }

    protected abstract Text getContainerName();

    public boolean checkUnlocked(PlayerEntity player) {
        return this.lock.checkUnlocked(player);
    }

    public static void handleLocked(Vec3d containerPos, PlayerEntity player, Text name) {
        World lv = player.getEntityWorld();
        player.sendMessage(Text.translatable("container.isLocked", name), true);
        if (!lv.isClient()) {
            lv.playSound(null, containerPos.getX(), containerPos.getY(), containerPos.getZ(), SoundEvents.BLOCK_CHEST_LOCKED, SoundCategory.BLOCKS, 1.0f, 1.0f);
        }
    }

    public boolean isLocked() {
        return !this.lock.equals(ContainerLock.EMPTY);
    }

    protected abstract DefaultedList<ItemStack> getHeldStacks();

    protected abstract void setHeldStacks(DefaultedList<ItemStack> var1);

    @Override
    public boolean isEmpty() {
        for (ItemStack lv : this.getHeldStacks()) {
            if (lv.isEmpty()) continue;
            return false;
        }
        return true;
    }

    @Override
    public ItemStack getStack(int slot) {
        return this.getHeldStacks().get(slot);
    }

    @Override
    public ItemStack removeStack(int slot, int amount) {
        ItemStack lv = Inventories.splitStack(this.getHeldStacks(), slot, amount);
        if (!lv.isEmpty()) {
            this.markDirty();
        }
        return lv;
    }

    @Override
    public ItemStack removeStack(int slot) {
        return Inventories.removeStack(this.getHeldStacks(), slot);
    }

    @Override
    public void setStack(int slot, ItemStack stack) {
        this.getHeldStacks().set(slot, stack);
        stack.capCount(this.getMaxCount(stack));
        this.markDirty();
    }

    @Override
    public boolean canPlayerUse(PlayerEntity player) {
        return Inventory.canPlayerUse(this, player);
    }

    @Override
    public void clear() {
        this.getHeldStacks().clear();
    }

    @Override
    public @Nullable ScreenHandler createMenu(int i, PlayerInventory arg, PlayerEntity arg2) {
        if (this.checkUnlocked(arg2)) {
            return this.createScreenHandler(i, arg);
        }
        LockableContainerBlockEntity.handleLocked(this.getPos().toCenterPos(), arg2, this.getDisplayName());
        return null;
    }

    protected abstract ScreenHandler createScreenHandler(int var1, PlayerInventory var2);

    @Override
    protected void readComponents(ComponentsAccess components) {
        super.readComponents(components);
        this.customName = components.get(DataComponentTypes.CUSTOM_NAME);
        this.lock = components.getOrDefault(DataComponentTypes.LOCK, ContainerLock.EMPTY);
        components.getOrDefault(DataComponentTypes.CONTAINER, ContainerComponent.DEFAULT).copyTo(this.getHeldStacks());
    }

    @Override
    protected void addComponents(ComponentMap.Builder builder) {
        super.addComponents(builder);
        builder.add(DataComponentTypes.CUSTOM_NAME, this.customName);
        if (this.isLocked()) {
            builder.add(DataComponentTypes.LOCK, this.lock);
        }
        builder.add(DataComponentTypes.CONTAINER, ContainerComponent.fromStacks(this.getHeldStacks()));
    }

    @Override
    public void removeFromCopiedStackData(WriteView view) {
        view.remove("CustomName");
        view.remove("lock");
        view.remove("Items");
    }
}

