/*
 * External method calls:
 *   Lnet/minecraft/entity/EquipmentTable;lootTable()Lnet/minecraft/registry/RegistryKey;
 *   Lnet/minecraft/entity/EquipmentTable;slotDropChances()Ljava/util/Map;
 *   Lnet/minecraft/loot/LootTable;generateLoot(Lnet/minecraft/loot/context/LootWorldContext;J)Lit/unimi/dsi/fastutil/objects/ObjectArrayList;
 *   Lnet/minecraft/entity/EquipmentSlot;split(Lnet/minecraft/item/ItemStack;)Lnet/minecraft/item/ItemStack;
 *   Lnet/minecraft/component/type/EquippableComponent;slot()Lnet/minecraft/entity/EquipmentSlot;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/entity/EquipmentHolder;equipStack(Lnet/minecraft/entity/EquipmentSlot;Lnet/minecraft/item/ItemStack;)V
 */
package net.minecraft.entity;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.EquippableComponent;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.EquipmentTable;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.context.LootWorldContext;
import net.minecraft.registry.RegistryKey;
import org.jspecify.annotations.Nullable;

public interface EquipmentHolder {
    public void equipStack(EquipmentSlot var1, ItemStack var2);

    public ItemStack getEquippedStack(EquipmentSlot var1);

    public void setEquipmentDropChance(EquipmentSlot var1, float var2);

    default public void setEquipmentFromTable(EquipmentTable equipmentTable, LootWorldContext parameters) {
        this.setEquipmentFromTable(equipmentTable.lootTable(), parameters, equipmentTable.slotDropChances());
    }

    default public void setEquipmentFromTable(RegistryKey<LootTable> lootTable, LootWorldContext parameters, Map<EquipmentSlot, Float> slotDropChances) {
        this.setEquipmentFromTable(lootTable, parameters, 0L, slotDropChances);
    }

    default public void setEquipmentFromTable(RegistryKey<LootTable> lootTable, LootWorldContext parameters, long seed, Map<EquipmentSlot, Float> slotDropChances) {
        LootTable lv = parameters.getWorld().getServer().getReloadableRegistries().getLootTable(lootTable);
        if (lv == LootTable.EMPTY) {
            return;
        }
        ObjectArrayList<ItemStack> list = lv.generateLoot(parameters, seed);
        ArrayList<EquipmentSlot> list2 = new ArrayList<EquipmentSlot>();
        for (ItemStack lv2 : list) {
            EquipmentSlot lv3 = this.getSlotForStack(lv2, list2);
            if (lv3 == null) continue;
            ItemStack lv4 = lv3.split(lv2);
            this.equipStack(lv3, lv4);
            Float float_ = slotDropChances.get(lv3);
            if (float_ != null) {
                this.setEquipmentDropChance(lv3, float_.floatValue());
            }
            list2.add(lv3);
        }
    }

    default public @Nullable EquipmentSlot getSlotForStack(ItemStack stack, List<EquipmentSlot> slotBlacklist) {
        if (stack.isEmpty()) {
            return null;
        }
        EquippableComponent lv = stack.get(DataComponentTypes.EQUIPPABLE);
        if (lv != null) {
            EquipmentSlot lv2 = lv.slot();
            if (!slotBlacklist.contains(lv2)) {
                return lv2;
            }
        } else if (!slotBlacklist.contains(EquipmentSlot.MAINHAND)) {
            return EquipmentSlot.MAINHAND;
        }
        return null;
    }
}

