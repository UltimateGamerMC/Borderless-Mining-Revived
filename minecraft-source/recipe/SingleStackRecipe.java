/*
 * External method calls:
 *   Lnet/minecraft/recipe/input/SingleStackRecipeInput;item()Lnet/minecraft/item/ItemStack;
 *   Lnet/minecraft/recipe/Ingredient;test(Lnet/minecraft/item/ItemStack;)Z
 *   Lnet/minecraft/recipe/IngredientPlacement;forSingleSlot(Lnet/minecraft/recipe/Ingredient;)Lnet/minecraft/recipe/IngredientPlacement;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/recipe/SingleStackRecipe;craft(Lnet/minecraft/recipe/input/SingleStackRecipeInput;Lnet/minecraft/registry/RegistryWrapper$WrapperLookup;)Lnet/minecraft/item/ItemStack;
 *   Lnet/minecraft/recipe/SingleStackRecipe;matches(Lnet/minecraft/recipe/input/SingleStackRecipeInput;Lnet/minecraft/world/World;)Z
 */
package net.minecraft.recipe;

import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.IngredientPlacement;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.input.SingleStackRecipeInput;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.world.World;
import org.jspecify.annotations.Nullable;

public abstract class SingleStackRecipe
implements Recipe<SingleStackRecipeInput> {
    private final Ingredient ingredient;
    private final ItemStack result;
    private final String group;
    private @Nullable IngredientPlacement ingredientPlacement;

    public SingleStackRecipe(String group, Ingredient ingredient, ItemStack result) {
        this.group = group;
        this.ingredient = ingredient;
        this.result = result;
    }

    @Override
    public abstract RecipeSerializer<? extends SingleStackRecipe> getSerializer();

    @Override
    public abstract RecipeType<? extends SingleStackRecipe> getType();

    @Override
    public boolean matches(SingleStackRecipeInput arg, World arg2) {
        return this.ingredient.test(arg.item());
    }

    @Override
    public String getGroup() {
        return this.group;
    }

    public Ingredient ingredient() {
        return this.ingredient;
    }

    protected ItemStack result() {
        return this.result;
    }

    @Override
    public IngredientPlacement getIngredientPlacement() {
        if (this.ingredientPlacement == null) {
            this.ingredientPlacement = IngredientPlacement.forSingleSlot(this.ingredient);
        }
        return this.ingredientPlacement;
    }

    @Override
    public ItemStack craft(SingleStackRecipeInput arg, RegistryWrapper.WrapperLookup arg2) {
        return this.result.copy();
    }

    @FunctionalInterface
    public static interface RecipeFactory<T extends SingleStackRecipe> {
        public T create(String var1, Ingredient var2, ItemStack var3);
    }

    public static class Serializer<T extends SingleStackRecipe>
    implements RecipeSerializer<T> {
        private final MapCodec<T> codec = RecordCodecBuilder.mapCodec(instance -> instance.group(Codec.STRING.optionalFieldOf("group", "").forGetter(SingleStackRecipe::getGroup), ((MapCodec)Ingredient.CODEC.fieldOf("ingredient")).forGetter(SingleStackRecipe::ingredient), ((MapCodec)ItemStack.VALIDATED_CODEC.fieldOf("result")).forGetter(SingleStackRecipe::result)).apply((Applicative<SingleStackRecipe, ?>)instance, recipeFactory::create));
        private final PacketCodec<RegistryByteBuf, T> packetCodec = PacketCodec.tuple(PacketCodecs.STRING, SingleStackRecipe::getGroup, Ingredient.PACKET_CODEC, SingleStackRecipe::ingredient, ItemStack.PACKET_CODEC, SingleStackRecipe::result, recipeFactory::create);

        protected Serializer(RecipeFactory<T> recipeFactory) {
        }

        @Override
        public MapCodec<T> codec() {
            return this.codec;
        }

        @Override
        public PacketCodec<RegistryByteBuf, T> packetCodec() {
            return this.packetCodec;
        }
    }
}

