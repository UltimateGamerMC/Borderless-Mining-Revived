package net.minecraft.loot.provider.number;

import com.mojang.serialization.MapCodec;
import net.minecraft.loot.provider.number.LootNumberProvider;

public record LootNumberProviderType(MapCodec<? extends LootNumberProvider> codec) {
}

