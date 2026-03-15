/*
 * External method calls:
 *   Lnet/minecraft/screen/Property;create()Lnet/minecraft/screen/Property;
 *   Lnet/minecraft/screen/slot/ForgingSlotsManager;builder()Lnet/minecraft/screen/slot/ForgingSlotsManager$Builder;
 *   Lnet/minecraft/screen/slot/ForgingSlotsManager$Builder;input(IIILjava/util/function/Predicate;)Lnet/minecraft/screen/slot/ForgingSlotsManager$Builder;
 *   Lnet/minecraft/screen/slot/ForgingSlotsManager$Builder;output(III)Lnet/minecraft/screen/slot/ForgingSlotsManager$Builder;
 *   Lnet/minecraft/screen/slot/ForgingSlotsManager$Builder;build()Lnet/minecraft/screen/slot/ForgingSlotsManager;
 *   Lnet/minecraft/item/ItemStack;onCraftByPlayer(Lnet/minecraft/entity/player/PlayerEntity;I)V
 *   Lnet/minecraft/inventory/CraftingResultInventory;unlockLastRecipe(Lnet/minecraft/entity/player/PlayerEntity;Ljava/util/List;)V
 *   Lnet/minecraft/screen/ScreenHandlerContext;run(Ljava/util/function/BiConsumer;)V
 *   Lnet/minecraft/item/ItemStack;decrement(I)V
 *   Lnet/minecraft/screen/ForgingScreenHandler;onContentChanged(Lnet/minecraft/inventory/Inventory;)V
 *   Lnet/minecraft/recipe/SmithingRecipe;craft(Lnet/minecraft/recipe/input/RecipeInput;Lnet/minecraft/registry/RegistryWrapper$WrapperLookup;)Lnet/minecraft/item/ItemStack;
 *   Lnet/minecraft/world/World;syncWorldEvent(ILnet/minecraft/util/math/BlockPos;I)V
 *
 * Internal private/static methods:
 *   Lnet/minecraft/screen/SmithingScreenHandler;createForgingSlotsManager(Lnet/minecraft/recipe/RecipeManager;)Lnet/minecraft/screen/slot/ForgingSlotsManager;
 *   Lnet/minecraft/screen/SmithingScreenHandler;addProperty(Lnet/minecraft/screen/Property;)Lnet/minecraft/screen/Property;
 *   Lnet/minecraft/screen/SmithingScreenHandler;decrementStack(I)V
 *   Lnet/minecraft/screen/SmithingScreenHandler;createRecipeInput()Lnet/minecraft/recipe/input/SmithingRecipeInput;
 */
package net.minecraft.screen;

import java.util.List;
import java.util.Optional;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.recipe.RecipePropertySet;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.SmithingRecipe;
import net.minecraft.recipe.input.SmithingRecipeInput;
import net.minecraft.screen.ForgingScreenHandler;
import net.minecraft.screen.Property;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.ForgingSlotsManager;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldEvents;

public class SmithingScreenHandler
extends ForgingScreenHandler {
    public static final int TEMPLATE_ID = 0;
    public static final int EQUIPMENT_ID = 1;
    public static final int MATERIAL_ID = 2;
    public static final int OUTPUT_ID = 3;
    public static final int TEMPLATE_X = 8;
    public static final int EQUIPMENT_X = 26;
    public static final int MATERIAL_X = 44;
    private static final int OUTPUT_X = 98;
    public static final int SLOT_Y = 48;
    private final World world;
    private final RecipePropertySet basePropertySet;
    private final RecipePropertySet templatePropertySet;
    private final RecipePropertySet additionPropertySet;
    private final Property invalidRecipe = Property.create();

    public SmithingScreenHandler(int syncId, PlayerInventory playerInventory) {
        this(syncId, playerInventory, ScreenHandlerContext.EMPTY);
    }

    public SmithingScreenHandler(int syncId, PlayerInventory playerInventory, ScreenHandlerContext context) {
        this(syncId, playerInventory, context, playerInventory.player.getEntityWorld());
    }

    private SmithingScreenHandler(int syncId, PlayerInventory playerInventory, ScreenHandlerContext context, World world) {
        super(ScreenHandlerType.SMITHING, syncId, playerInventory, context, SmithingScreenHandler.createForgingSlotsManager(world.getRecipeManager()));
        this.world = world;
        this.basePropertySet = world.getRecipeManager().getPropertySet(RecipePropertySet.SMITHING_BASE);
        this.templatePropertySet = world.getRecipeManager().getPropertySet(RecipePropertySet.SMITHING_TEMPLATE);
        this.additionPropertySet = world.getRecipeManager().getPropertySet(RecipePropertySet.SMITHING_ADDITION);
        this.addProperty(this.invalidRecipe).set(0);
    }

    private static ForgingSlotsManager createForgingSlotsManager(RecipeManager recipeManager) {
        RecipePropertySet lv = recipeManager.getPropertySet(RecipePropertySet.SMITHING_BASE);
        RecipePropertySet lv2 = recipeManager.getPropertySet(RecipePropertySet.SMITHING_TEMPLATE);
        RecipePropertySet lv3 = recipeManager.getPropertySet(RecipePropertySet.SMITHING_ADDITION);
        return ForgingSlotsManager.builder().input(0, 8, 48, lv2::canUse).input(1, 26, 48, lv::canUse).input(2, 44, 48, lv3::canUse).output(3, 98, 48).build();
    }

    @Override
    protected boolean canUse(BlockState state) {
        return state.isOf(Blocks.SMITHING_TABLE);
    }

    @Override
    protected void onTakeOutput(PlayerEntity player, ItemStack stack) {
        stack.onCraftByPlayer(player, stack.getCount());
        this.output.unlockLastRecipe(player, this.getInputStacks());
        this.decrementStack(0);
        this.decrementStack(1);
        this.decrementStack(2);
        this.context.run((world, pos) -> world.syncWorldEvent(WorldEvents.SMITHING_TABLE_USED, (BlockPos)pos, 0));
    }

    private List<ItemStack> getInputStacks() {
        return List.of(this.input.getStack(0), this.input.getStack(1), this.input.getStack(2));
    }

    private SmithingRecipeInput createRecipeInput() {
        return new SmithingRecipeInput(this.input.getStack(0), this.input.getStack(1), this.input.getStack(2));
    }

    private void decrementStack(int slot) {
        ItemStack lv = this.input.getStack(slot);
        if (!lv.isEmpty()) {
            lv.decrement(1);
            this.input.setStack(slot, lv);
        }
    }

    @Override
    public void onContentChanged(Inventory inventory) {
        super.onContentChanged(inventory);
        if (this.world instanceof ServerWorld) {
            boolean bl = this.getSlot(0).hasStack() && this.getSlot(1).hasStack() && this.getSlot(2).hasStack() && !this.getSlot(this.getResultSlotIndex()).hasStack();
            this.invalidRecipe.set(bl ? 1 : 0);
        }
    }

    @Override
    public void updateResult() {
        Optional<RecipeEntry<Object>> optional;
        SmithingRecipeInput lv = this.createRecipeInput();
        World world = this.world;
        if (world instanceof ServerWorld) {
            ServerWorld lv2 = (ServerWorld)world;
            optional = lv2.getRecipeManager().getFirstMatch(RecipeType.SMITHING, lv, lv2);
        } else {
            optional = Optional.empty();
        }
        optional.ifPresentOrElse(recipe -> {
            ItemStack lv = ((SmithingRecipe)recipe.value()).craft(lv, this.world.getRegistryManager());
            this.output.setLastRecipe((RecipeEntry<?>)recipe);
            this.output.setStack(0, lv);
        }, () -> {
            this.output.setLastRecipe(null);
            this.output.setStack(0, ItemStack.EMPTY);
        });
    }

    @Override
    public boolean canInsertIntoSlot(ItemStack stack, Slot slot) {
        return slot.inventory != this.output && super.canInsertIntoSlot(stack, slot);
    }

    @Override
    public boolean isValidIngredient(ItemStack stack) {
        if (this.templatePropertySet.canUse(stack) && !this.getSlot(0).hasStack()) {
            return true;
        }
        if (this.basePropertySet.canUse(stack) && !this.getSlot(1).hasStack()) {
            return true;
        }
        return this.additionPropertySet.canUse(stack) && !this.getSlot(2).hasStack();
    }

    public boolean hasInvalidRecipe() {
        return this.invalidRecipe.get() > 0;
    }
}

