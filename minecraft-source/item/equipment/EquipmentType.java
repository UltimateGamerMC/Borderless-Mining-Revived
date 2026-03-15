/*
 * External method calls:
 *   Lnet/minecraft/util/StringIdentifiable;createBasicCodec(Ljava/util/function/Supplier;)Lcom/mojang/serialization/Codec;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/item/equipment/EquipmentType;method_48401()[Lnet/minecraft/item/equipment/EquipmentType;
 */
package net.minecraft.item.equipment;

import com.mojang.serialization.Codec;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.util.StringIdentifiable;

public enum EquipmentType implements StringIdentifiable
{
    HELMET(EquipmentSlot.HEAD, 11, "helmet"),
    CHESTPLATE(EquipmentSlot.CHEST, 16, "chestplate"),
    LEGGINGS(EquipmentSlot.LEGS, 15, "leggings"),
    BOOTS(EquipmentSlot.FEET, 13, "boots"),
    BODY(EquipmentSlot.BODY, 16, "body");

    public static final Codec<EquipmentType> CODEC;
    private final EquipmentSlot equipmentSlot;
    private final String name;
    private final int baseMaxDamage;

    private EquipmentType(EquipmentSlot equipmentSlot, int baseMaxDamage, String name) {
        this.equipmentSlot = equipmentSlot;
        this.name = name;
        this.baseMaxDamage = baseMaxDamage;
    }

    public int getMaxDamage(int multiplier) {
        return this.baseMaxDamage * multiplier;
    }

    public EquipmentSlot getEquipmentSlot() {
        return this.equipmentSlot;
    }

    public String getName() {
        return this.name;
    }

    @Override
    public String asString() {
        return this.name;
    }

    static {
        CODEC = StringIdentifiable.createBasicCodec(EquipmentType::values);
    }
}

