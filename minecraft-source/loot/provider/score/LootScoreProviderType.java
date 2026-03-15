package net.minecraft.loot.provider.score;

import com.mojang.serialization.MapCodec;
import net.minecraft.loot.provider.score.LootScoreProvider;

public record LootScoreProviderType(MapCodec<? extends LootScoreProvider> codec) {
}

