package net.minecraft.loot.function;

import com.mojang.serialization.MapCodec;
import net.minecraft.loot.function.LootFunction;

public record LootFunctionType<T extends LootFunction>(MapCodec<T> codec) {
}

