package net.minecraft.loot.provider.nbt;

import com.mojang.serialization.MapCodec;
import net.minecraft.loot.provider.nbt.LootNbtProvider;

public record LootNbtProviderType(MapCodec<? extends LootNbtProvider> codec) {
}

