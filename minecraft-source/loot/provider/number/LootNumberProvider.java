/*
 * Internal private/static methods:
 *   Lnet/minecraft/loot/provider/number/LootNumberProvider;nextFloat(Lnet/minecraft/loot/context/LootContext;)F
 */
package net.minecraft.loot.provider.number;

import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextAware;
import net.minecraft.loot.provider.number.LootNumberProviderType;

public interface LootNumberProvider
extends LootContextAware {
    public float nextFloat(LootContext var1);

    default public int nextInt(LootContext context) {
        return Math.round(this.nextFloat(context));
    }

    public LootNumberProviderType getType();
}

