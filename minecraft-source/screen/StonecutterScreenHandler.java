/*
 * External method calls:
 *   Lnet/minecraft/screen/Property;create()Lnet/minecraft/screen/Property;
 *   Lnet/minecraft/recipe/display/CuttingRecipeDisplay$Grouping;empty()Lnet/minecraft/recipe/display/CuttingRecipeDisplay$Grouping;
 *   Lnet/minecraft/recipe/display/CuttingRecipeDisplay$Grouping;filter(Lnet/minecraft/item/ItemStack;)Lnet/minecraft/recipe/display/CuttingRecipeDisplay$Grouping;
 *   Lnet/minecraft/recipe/display/CuttingRecipeDisplay$Grouping;entries()Ljava/util/List;
 *   Lnet/minecraft/recipe/display/CuttingRecipeDisplay$GroupEntry;recipe()Lnet/minecraft/recipe/display/CuttingRecipeDisplay;
 *   Lnet/minecraft/recipe/display/CuttingRecipeDisplay;recipe()Ljava/util/Optional;
 *   Lnet/minecraft/item/Item;onCraftByPlayer(Lnet/minecraft/item/ItemStack;Lnet/minecraft/entity/player/PlayerEntity;)V
 *   Lnet/minecraft/screen/slot/Slot;onQuickTransfer(Lnet/minecraft/item/ItemStack;Lnet/minecraft/item/ItemStack;)V
 *   Lnet/minecraft/screen/slot/Slot;onTakeItem(Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/item/ItemStack;)V
 *   Lnet/minecraft/entity/player/PlayerEntity;dropItem(Lnet/minecraft/item/ItemStack;Z)Lnet/minecraft/entity/ItemEntity;
 *   Lnet/minecraft/screen/ScreenHandler;onClosed(Lnet/minecraft/entity/player/PlayerEntity;)V
 *   Lnet/minecraft/inventory/CraftingResultInventory;removeStack(I)Lnet/minecraft/item/ItemStack;
 *   Lnet/minecraft/screen/ScreenHandlerContext;run(Ljava/util/function/BiConsumer;)V
 *   Lnet/minecraft/recipe/StonecuttingRecipe;craft(Lnet/minecraft/recipe/input/SingleStackRecipeInput;Lnet/minecraft/registry/RegistryWrapper$WrapperLookup;)Lnet/minecraft/item/ItemStack;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/screen/StonecutterScreenHandler;addSlot(Lnet/minecraft/screen/slot/Slot;)Lnet/minecraft/screen/slot/Slot;
 *   Lnet/minecraft/screen/StonecutterScreenHandler;addPlayerSlots(Lnet/minecraft/inventory/Inventory;II)V
 *   Lnet/minecraft/screen/StonecutterScreenHandler;addProperty(Lnet/minecraft/screen/Property;)Lnet/minecraft/screen/Property;
 *   Lnet/minecraft/screen/StonecutterScreenHandler;populateResult(I)V
 *   Lnet/minecraft/screen/StonecutterScreenHandler;updateInput(Lnet/minecraft/item/ItemStack;)V
 *   Lnet/minecraft/screen/StonecutterScreenHandler;insertItem(Lnet/minecraft/item/ItemStack;IIZ)Z
 *   Lnet/minecraft/screen/StonecutterScreenHandler;dropInventory(Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/inventory/Inventory;)V
 */
package net.minecraft.screen;

import java.util.List;
import java.util.Optional;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.CraftingResultInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.recipe.StonecuttingRecipe;
import net.minecraft.recipe.display.CuttingRecipeDisplay;
import net.minecraft.recipe.input.SingleStackRecipeInput;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.screen.Property;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.Slot;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class StonecutterScreenHandler
extends ScreenHandler {
    public static final int INPUT_ID = 0;
    public static final int OUTPUT_ID = 1;
    private static final int INVENTORY_START = 2;
    private static final int INVENTORY_END = 29;
    private static final int OUTPUT_START = 29;
    private static final int OUTPUT_END = 38;
    private final ScreenHandlerContext context;
    final Property selectedRecipe = Property.create();
    private final World world;
    private CuttingRecipeDisplay.Grouping<StonecuttingRecipe> availableRecipes = CuttingRecipeDisplay.Grouping.empty();
    private ItemStack inputStack = ItemStack.EMPTY;
    long lastTakeTime;
    final Slot inputSlot;
    final Slot outputSlot;
    Runnable contentsChangedListener = () -> {};
    public final Inventory input = new SimpleInventory(1){

        @Override
        public void markDirty() {
            super.markDirty();
            StonecutterScreenHandler.this.onContentChanged(this);
            StonecutterScreenHandler.this.contentsChangedListener.run();
        }
    };
    final CraftingResultInventory output = new CraftingResultInventory();

    public StonecutterScreenHandler(int syncId, PlayerInventory playerInventory) {
        this(syncId, playerInventory, ScreenHandlerContext.EMPTY);
    }

    public StonecutterScreenHandler(int syncId, PlayerInventory playerInventory, final ScreenHandlerContext context) {
        super(ScreenHandlerType.STONECUTTER, syncId);
        this.context = context;
        this.world = playerInventory.player.getEntityWorld();
        this.inputSlot = this.addSlot(new Slot(this.input, 0, 20, 33));
        this.outputSlot = this.addSlot(new Slot(this.output, 1, 143, 33){

            @Override
            public boolean canInsert(ItemStack stack) {
                return false;
            }

            @Override
            public void onTakeItem(PlayerEntity player, ItemStack stack) {
                stack.onCraftByPlayer(player, stack.getCount());
                StonecutterScreenHandler.this.output.unlockLastRecipe(player, this.getInputStacks());
                ItemStack lv = StonecutterScreenHandler.this.inputSlot.takeStack(1);
                if (!lv.isEmpty()) {
                    StonecutterScreenHandler.this.populateResult(StonecutterScreenHandler.this.selectedRecipe.get());
                }
                context.run((world, pos) -> {
                    long l = world.getTime();
                    if (StonecutterScreenHandler.this.lastTakeTime != l) {
                        world.playSound(null, (BlockPos)pos, SoundEvents.UI_STONECUTTER_TAKE_RESULT, SoundCategory.BLOCKS, 1.0f, 1.0f);
                        StonecutterScreenHandler.this.lastTakeTime = l;
                    }
                });
                super.onTakeItem(player, stack);
            }

            private List<ItemStack> getInputStacks() {
                return List.of(StonecutterScreenHandler.this.inputSlot.getStack());
            }
        });
        this.addPlayerSlots(playerInventory, 8, 84);
        this.addProperty(this.selectedRecipe);
    }

    public int getSelectedRecipe() {
        return this.selectedRecipe.get();
    }

    public CuttingRecipeDisplay.Grouping<StonecuttingRecipe> getAvailableRecipes() {
        return this.availableRecipes;
    }

    public int getAvailableRecipeCount() {
        return this.availableRecipes.size();
    }

    public boolean canCraft() {
        return this.inputSlot.hasStack() && !this.availableRecipes.isEmpty();
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return StonecutterScreenHandler.canUse(this.context, player, Blocks.STONECUTTER);
    }

    @Override
    public boolean onButtonClick(PlayerEntity player, int id) {
        if (this.selectedRecipe.get() == id) {
            return false;
        }
        if (this.isInBounds(id)) {
            this.selectedRecipe.set(id);
            this.populateResult(id);
        }
        return true;
    }

    private boolean isInBounds(int id) {
        return id >= 0 && id < this.availableRecipes.size();
    }

    @Override
    public void onContentChanged(Inventory inventory) {
        ItemStack lv = this.inputSlot.getStack();
        if (!lv.isOf(this.inputStack.getItem())) {
            this.inputStack = lv.copy();
            this.updateInput(lv);
        }
    }

    private void updateInput(ItemStack stack) {
        this.selectedRecipe.set(-1);
        this.outputSlot.setStackNoCallbacks(ItemStack.EMPTY);
        this.availableRecipes = !stack.isEmpty() ? this.world.getRecipeManager().getStonecutterRecipes().filter(stack) : CuttingRecipeDisplay.Grouping.empty();
    }

    void populateResult(int selectedId) {
        Optional<RecipeEntry<Object>> optional;
        if (!this.availableRecipes.isEmpty() && this.isInBounds(selectedId)) {
            CuttingRecipeDisplay.GroupEntry<StonecuttingRecipe> lv = this.availableRecipes.entries().get(selectedId);
            optional = lv.recipe().recipe();
        } else {
            optional = Optional.empty();
        }
        optional.ifPresentOrElse(recipe -> {
            this.output.setLastRecipe((RecipeEntry<?>)recipe);
            this.outputSlot.setStackNoCallbacks(((StonecuttingRecipe)recipe.value()).craft(new SingleStackRecipeInput(this.input.getStack(0)), (RegistryWrapper.WrapperLookup)this.world.getRegistryManager()));
        }, () -> {
            this.outputSlot.setStackNoCallbacks(ItemStack.EMPTY);
            this.output.setLastRecipe(null);
        });
        this.sendContentUpdates();
    }

    @Override
    public ScreenHandlerType<?> getType() {
        return ScreenHandlerType.STONECUTTER;
    }

    public void setContentsChangedListener(Runnable contentsChangedListener) {
        this.contentsChangedListener = contentsChangedListener;
    }

    @Override
    public boolean canInsertIntoSlot(ItemStack stack, Slot slot) {
        return slot.inventory != this.output && super.canInsertIntoSlot(stack, slot);
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int slot) {
        ItemStack lv = ItemStack.EMPTY;
        Slot lv2 = (Slot)this.slots.get(slot);
        if (lv2 != null && lv2.hasStack()) {
            ItemStack lv3 = lv2.getStack();
            Item lv4 = lv3.getItem();
            lv = lv3.copy();
            if (slot == 1) {
                lv4.onCraftByPlayer(lv3, player);
                if (!this.insertItem(lv3, 2, 38, true)) {
                    return ItemStack.EMPTY;
                }
                lv2.onQuickTransfer(lv3, lv);
            } else if (slot == 0 ? !this.insertItem(lv3, 2, 38, false) : (this.world.getRecipeManager().getStonecutterRecipes().contains(lv3) ? !this.insertItem(lv3, 0, 1, false) : (slot >= 2 && slot < 29 ? !this.insertItem(lv3, 29, 38, false) : slot >= 29 && slot < 38 && !this.insertItem(lv3, 2, 29, false)))) {
                return ItemStack.EMPTY;
            }
            if (lv3.isEmpty()) {
                lv2.setStack(ItemStack.EMPTY);
            }
            lv2.markDirty();
            if (lv3.getCount() == lv.getCount()) {
                return ItemStack.EMPTY;
            }
            lv2.onTakeItem(player, lv3);
            if (slot == 1) {
                player.dropItem(lv3, false);
            }
            this.sendContentUpdates();
        }
        return lv;
    }

    @Override
    public void onClosed(PlayerEntity player) {
        super.onClosed(player);
        this.output.removeStack(1);
        this.context.run((world, pos) -> this.dropInventory(player, this.input));
    }
}

