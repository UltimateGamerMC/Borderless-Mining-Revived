package net.minecraft.loot.slot;

import com.mojang.serialization.MapCodec;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextAware;
import net.minecraft.loot.slot.ItemStream;

public interface SlotSource
extends LootContextAware {
    public MapCodec<? extends SlotSource> getCodec();

    public ItemStream stream(LootContext var1);
}

