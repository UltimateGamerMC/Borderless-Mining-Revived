/*
 * External method calls:
 *   Lnet/minecraft/block/entity/LockableContainerBlockEntity;removeStack(II)Lnet/minecraft/item/ItemStack;
 *   Lnet/minecraft/block/entity/LockableContainerBlockEntity;removeStack(I)Lnet/minecraft/item/ItemStack;
 *   Lnet/minecraft/block/entity/LockableContainerBlockEntity;checkUnlocked(Lnet/minecraft/entity/player/PlayerEntity;)Z
 *   Lnet/minecraft/block/entity/LockableContainerBlockEntity;handleLocked(Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/text/Text;)V
 *   Lnet/minecraft/block/entity/LockableContainerBlockEntity;readComponents(Lnet/minecraft/component/ComponentsAccess;)V
 *   Lnet/minecraft/component/type/ContainerLootComponent;lootTable()Lnet/minecraft/registry/RegistryKey;
 *   Lnet/minecraft/block/entity/LockableContainerBlockEntity;addComponents(Lnet/minecraft/component/ComponentMap$Builder;)V
 *   Lnet/minecraft/block/entity/LockableContainerBlockEntity;removeFromCopiedStackData(Lnet/minecraft/storage/WriteView;)V
 *
 * Internal private/static methods:
 *   Lnet/minecraft/block/entity/LootableContainerBlockEntity;generateLoot(Lnet/minecraft/entity/player/PlayerEntity;)V
 *   Lnet/minecraft/block/entity/LootableContainerBlockEntity;checkUnlocked(Lnet/minecraft/entity/player/PlayerEntity;)Z
 *   Lnet/minecraft/block/entity/LootableContainerBlockEntity;createScreenHandler(ILnet/minecraft/entity/player/PlayerInventory;)Lnet/minecraft/screen/ScreenHandler;
 */
package net.minecraft.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.LockableContainerBlockEntity;
import net.minecraft.component.ComponentMap;
import net.minecraft.component.ComponentsAccess;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ContainerLootComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.LootableInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootTable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.storage.WriteView;
import net.minecraft.util.math.BlockPos;
import org.jspecify.annotations.Nullable;

public abstract class LootableContainerBlockEntity
extends LockableContainerBlockEntity
implements LootableInventory {
    protected @Nullable RegistryKey<LootTable> lootTable;
    protected long lootTableSeed = 0L;

    protected LootableContainerBlockEntity(BlockEntityType<?> arg, BlockPos arg2, BlockState arg3) {
        super(arg, arg2, arg3);
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
    public boolean isEmpty() {
        this.generateLoot(null);
        return super.isEmpty();
    }

    @Override
    public ItemStack getStack(int slot) {
        this.generateLoot(null);
        return super.getStack(slot);
    }

    @Override
    public ItemStack removeStack(int slot, int amount) {
        this.generateLoot(null);
        return super.removeStack(slot, amount);
    }

    @Override
    public ItemStack removeStack(int slot) {
        this.generateLoot(null);
        return super.removeStack(slot);
    }

    @Override
    public void setStack(int slot, ItemStack stack) {
        this.generateLoot(null);
        super.setStack(slot, stack);
    }

    @Override
    public boolean checkUnlocked(PlayerEntity player) {
        return super.checkUnlocked(player) && (this.lootTable == null || !player.isSpectator());
    }

    @Override
    public @Nullable ScreenHandler createMenu(int i, PlayerInventory arg, PlayerEntity arg2) {
        if (this.checkUnlocked(arg2)) {
            this.generateLoot(arg.player);
            return this.createScreenHandler(i, arg);
        }
        LockableContainerBlockEntity.handleLocked(this.getPos().toCenterPos(), arg2, this.getDisplayName());
        return null;
    }

    @Override
    protected void readComponents(ComponentsAccess components) {
        super.readComponents(components);
        ContainerLootComponent lv = components.get(DataComponentTypes.CONTAINER_LOOT);
        if (lv != null) {
            this.lootTable = lv.lootTable();
            this.lootTableSeed = lv.seed();
        }
    }

    @Override
    protected void addComponents(ComponentMap.Builder builder) {
        super.addComponents(builder);
        if (this.lootTable != null) {
            builder.add(DataComponentTypes.CONTAINER_LOOT, new ContainerLootComponent(this.lootTable, this.lootTableSeed));
        }
    }

    @Override
    public void removeFromCopiedStackData(WriteView view) {
        super.removeFromCopiedStackData(view);
        view.remove("LootTable");
        view.remove("LootTableSeed");
    }
}

