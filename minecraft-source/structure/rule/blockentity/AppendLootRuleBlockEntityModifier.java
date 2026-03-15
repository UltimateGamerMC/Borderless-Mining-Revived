/*
 * External method calls:
 *   Lnet/minecraft/nbt/NbtCompound;putLong(Ljava/lang/String;J)V
 */
package net.minecraft.structure.rule.blockentity;

import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.loot.LootTable;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryKey;
import net.minecraft.structure.rule.blockentity.RuleBlockEntityModifier;
import net.minecraft.structure.rule.blockentity.RuleBlockEntityModifierType;
import net.minecraft.util.math.random.Random;
import org.jspecify.annotations.Nullable;

public class AppendLootRuleBlockEntityModifier
implements RuleBlockEntityModifier {
    public static final MapCodec<AppendLootRuleBlockEntityModifier> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(((MapCodec)LootTable.TABLE_KEY.fieldOf("loot_table")).forGetter(modifier -> modifier.lootTable)).apply((Applicative<AppendLootRuleBlockEntityModifier, ?>)instance, AppendLootRuleBlockEntityModifier::new));
    private final RegistryKey<LootTable> lootTable;

    public AppendLootRuleBlockEntityModifier(RegistryKey<LootTable> lootTable) {
        this.lootTable = lootTable;
    }

    @Override
    public NbtCompound modifyBlockEntityNbt(Random random, @Nullable NbtCompound nbt) {
        NbtCompound lv = nbt == null ? new NbtCompound() : nbt.copy();
        lv.put("LootTable", LootTable.TABLE_KEY, this.lootTable);
        lv.putLong("LootTableSeed", random.nextLong());
        return lv;
    }

    @Override
    public RuleBlockEntityModifierType<?> getType() {
        return RuleBlockEntityModifierType.APPEND_LOOT;
    }
}

