package net.minecraft.loot.entry;

import com.mojang.serialization.MapCodec;
import net.minecraft.loot.entry.LootPoolEntry;

public record LootPoolEntryType(MapCodec<? extends LootPoolEntry> codec) {
}

