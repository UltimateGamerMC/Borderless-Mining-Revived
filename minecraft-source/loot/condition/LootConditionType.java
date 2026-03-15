package net.minecraft.loot.condition;

import com.mojang.serialization.MapCodec;
import net.minecraft.loot.condition.LootCondition;

public record LootConditionType(MapCodec<? extends LootCondition> codec) {
}

