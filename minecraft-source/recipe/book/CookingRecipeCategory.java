/*
 * External method calls:
 *   Lnet/minecraft/util/function/ValueLists;createIndexToValueFunction(Ljava/util/function/ToIntFunction;[Ljava/lang/Object;Lnet/minecraft/util/function/ValueLists$OutOfBoundsHandling;)Ljava/util/function/IntFunction;
 *   Lnet/minecraft/util/StringIdentifiable;createCodec(Ljava/util/function/Supplier;)Lnet/minecraft/util/StringIdentifiable$EnumCodec;
 *   Lnet/minecraft/network/codec/PacketCodecs;indexed(Ljava/util/function/IntFunction;Ljava/util/function/ToIntFunction;)Lnet/minecraft/network/codec/PacketCodec;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/recipe/book/CookingRecipeCategory;method_45439()[Lnet/minecraft/recipe/book/CookingRecipeCategory;
 *   Lnet/minecraft/recipe/book/CookingRecipeCategory;values()[Lnet/minecraft/recipe/book/CookingRecipeCategory;
 */
package net.minecraft.recipe.book;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import java.util.function.IntFunction;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.function.ValueLists;

public enum CookingRecipeCategory implements StringIdentifiable
{
    FOOD(0, "food"),
    BLOCKS(1, "blocks"),
    MISC(2, "misc");

    private static final IntFunction<CookingRecipeCategory> BY_ID;
    public static final Codec<CookingRecipeCategory> CODEC;
    public static final PacketCodec<ByteBuf, CookingRecipeCategory> PACKET_CODEC;
    private final int id;
    private final String name;

    private CookingRecipeCategory(int id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public String asString() {
        return this.name;
    }

    static {
        BY_ID = ValueLists.createIndexToValueFunction(category -> category.id, CookingRecipeCategory.values(), ValueLists.OutOfBoundsHandling.ZERO);
        CODEC = StringIdentifiable.createCodec(CookingRecipeCategory::values);
        PACKET_CODEC = PacketCodecs.indexed(BY_ID, category -> category.id);
    }
}

