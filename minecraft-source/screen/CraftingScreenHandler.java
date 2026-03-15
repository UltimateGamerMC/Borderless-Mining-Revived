/*
 * External method calls:
 *   Lnet/minecraft/inventory/RecipeInputInventory;createRecipeInput()Lnet/minecraft/recipe/input/CraftingRecipeInput;
 *   Lnet/minecraft/recipe/CraftingRecipe;craft(Lnet/minecraft/recipe/input/RecipeInput;Lnet/minecraft/registry/RegistryWrapper$WrapperLookup;)Lnet/minecraft/item/ItemStack;
 *   Lnet/minecraft/server/network/ServerPlayNetworkHandler;sendPacket(Lnet/minecraft/network/packet/Packet;)V
 *   Lnet/minecraft/screen/ScreenHandlerContext;run(Ljava/util/function/BiConsumer;)V
 *   Lnet/minecraft/screen/AbstractCraftingScreenHandler;onClosed(Lnet/minecraft/entity/player/PlayerEntity;)V
 *   Lnet/minecraft/item/Item;onCraftByPlayer(Lnet/minecraft/item/ItemStack;Lnet/minecraft/entity/player/PlayerEntity;)V
 *   Lnet/minecraft/screen/slot/Slot;onQuickTransfer(Lnet/minecraft/item/ItemStack;Lnet/minecraft/item/ItemStack;)V
 *   Lnet/minecraft/screen/slot/Slot;onTakeItem(Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/item/ItemStack;)V
 *   Lnet/minecraft/entity/player/PlayerEntity;dropItem(Lnet/minecraft/item/ItemStack;Z)Lnet/minecraft/entity/ItemEntity;
 *   Lnet/minecraft/util/collection/DefaultedList;subList(II)Ljava/util/List;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/screen/CraftingScreenHandler;addResultSlot(Lnet/minecraft/entity/player/PlayerEntity;II)Lnet/minecraft/screen/slot/Slot;
 *   Lnet/minecraft/screen/CraftingScreenHandler;addInputSlots(II)V
 *   Lnet/minecraft/screen/CraftingScreenHandler;addPlayerSlots(Lnet/minecraft/inventory/Inventory;II)V
 *   Lnet/minecraft/screen/CraftingScreenHandler;updateResult(Lnet/minecraft/screen/ScreenHandler;Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/inventory/RecipeInputInventory;Lnet/minecraft/inventory/CraftingResultInventory;Lnet/minecraft/recipe/RecipeEntry;)V
 *   Lnet/minecraft/screen/CraftingScreenHandler;insertItem(Lnet/minecraft/item/ItemStack;IIZ)Z
 *   Lnet/minecraft/screen/CraftingScreenHandler;dropInventory(Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/inventory/Inventory;)V
 */
package net.minecraft.screen;

import java.util.List;
import java.util.Optional;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.CraftingResultInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.RecipeInputInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.s2c.play.ScreenHandlerSlotUpdateS2CPacket;
import net.minecraft.recipe.CraftingRecipe;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.book.RecipeBookType;
import net.minecraft.recipe.input.CraftingRecipeInput;
import net.minecraft.screen.AbstractCraftingScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import org.jspecify.annotations.Nullable;

public class CraftingScreenHandler
extends AbstractCraftingScreenHandler {
    private static final int field_52567 = 3;
    private static final int field_52568 = 3;
    public static final int RESULT_ID = 0;
    private static final int INPUT_START = 1;
    private static final int field_52569 = 9;
    private static final int INPUT_END = 10;
    private static final int INVENTORY_START = 10;
    private static final int INVENTORY_END = 37;
    private static final int HOTBAR_START = 37;
    private static final int HOTBAR_END = 46;
    private final ScreenHandlerContext context;
    private final PlayerEntity player;
    private boolean filling;

    public CraftingScreenHandler(int syncId, PlayerInventory playerInventory) {
        this(syncId, playerInventory, ScreenHandlerContext.EMPTY);
    }

    public CraftingScreenHandler(int syncId, PlayerInventory playerInventory, ScreenHandlerContext context) {
        super(ScreenHandlerType.CRAFTING, syncId, 3, 3);
        this.context = context;
        this.player = playerInventory.player;
        this.addResultSlot(this.player, 124, 35);
        this.addInputSlots(30, 17);
        this.addPlayerSlots(playerInventory, 8, 84);
    }

    protected static void updateResult(ScreenHandler handler, ServerWorld world, PlayerEntity player, RecipeInputInventory craftingInventory, CraftingResultInventory resultInventory, @Nullable RecipeEntry<CraftingRecipe> recipe) {
        CraftingRecipeInput lv = craftingInventory.createRecipeInput();
        ServerPlayerEntity lv2 = (ServerPlayerEntity)player;
        ItemStack lv3 = ItemStack.EMPTY;
        Optional<RecipeEntry<CraftingRecipe>> optional = world.getServer().getRecipeManager().getFirstMatch(RecipeType.CRAFTING, lv, (World)world, recipe);
        if (optional.isPresent()) {
            ItemStack lv6;
            RecipeEntry<CraftingRecipe> lv4 = optional.get();
            CraftingRecipe lv5 = lv4.value();
            if (resultInventory.shouldCraftRecipe(lv2, lv4) && (lv6 = lv5.craft(lv, world.getRegistryManager())).isItemEnabled(world.getEnabledFeatures())) {
                lv3 = lv6;
            }
        }
        resultInventory.setStack(0, lv3);
        handler.setReceivedStack(0, lv3);
        lv2.networkHandler.sendPacket(new ScreenHandlerSlotUpdateS2CPacket(handler.syncId, handler.nextRevision(), 0, lv3));
    }

    @Override
    public void onContentChanged(Inventory inventory) {
        if (!this.filling) {
            this.context.run((world, pos) -> {
                if (world instanceof ServerWorld) {
                    ServerWorld lv = (ServerWorld)world;
                    CraftingScreenHandler.updateResult(this, lv, this.player, this.craftingInventory, this.craftingResultInventory, null);
                }
            });
        }
    }

    @Override
    public void onInputSlotFillStart() {
        this.filling = true;
    }

    @Override
    public void onInputSlotFillFinish(ServerWorld world, RecipeEntry<CraftingRecipe> recipe) {
        this.filling = false;
        CraftingScreenHandler.updateResult(this, world, this.player, this.craftingInventory, this.craftingResultInventory, recipe);
    }

    @Override
    public void onClosed(PlayerEntity player) {
        super.onClosed(player);
        this.context.run((world, pos) -> this.dropInventory(player, this.craftingInventory));
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return CraftingScreenHandler.canUse(this.context, player, Blocks.CRAFTING_TABLE);
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int slot) {
        ItemStack lv = ItemStack.EMPTY;
        Slot lv2 = (Slot)this.slots.get(slot);
        if (lv2 != null && lv2.hasStack()) {
            ItemStack lv3 = lv2.getStack();
            lv = lv3.copy();
            if (slot == 0) {
                lv3.getItem().onCraftByPlayer(lv3, player);
                if (!this.insertItem(lv3, 10, 46, true)) {
                    return ItemStack.EMPTY;
                }
                lv2.onQuickTransfer(lv3, lv);
            } else if (slot >= 10 && slot < 46 ? !this.insertItem(lv3, 1, 10, false) && (slot < 37 ? !this.insertItem(lv3, 37, 46, false) : !this.insertItem(lv3, 10, 37, false)) : !this.insertItem(lv3, 10, 46, false)) {
                return ItemStack.EMPTY;
            }
            if (lv3.isEmpty()) {
                lv2.setStack(ItemStack.EMPTY);
            } else {
                lv2.markDirty();
            }
            if (lv3.getCount() == lv.getCount()) {
                return ItemStack.EMPTY;
            }
            lv2.onTakeItem(player, lv3);
            if (slot == 0) {
                player.dropItem(lv3, false);
            }
        }
        return lv;
    }

    @Override
    public boolean canInsertIntoSlot(ItemStack stack, Slot slot) {
        return slot.inventory != this.craftingResultInventory && super.canInsertIntoSlot(stack, slot);
    }

    @Override
    public Slot getOutputSlot() {
        return (Slot)this.slots.get(0);
    }

    @Override
    public List<Slot> getInputSlots() {
        return this.slots.subList(1, 10);
    }

    @Override
    public RecipeBookType getCategory() {
        return RecipeBookType.CRAFTING;
    }

    @Override
    protected PlayerEntity getPlayer() {
        return this.player;
    }
}

