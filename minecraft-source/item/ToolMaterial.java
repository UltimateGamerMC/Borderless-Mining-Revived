/*
 * External method calls:
 *   Lnet/minecraft/item/Item$Settings;maxDamage(I)Lnet/minecraft/item/Item$Settings;
 *   Lnet/minecraft/item/Item$Settings;repairable(Lnet/minecraft/registry/tag/TagKey;)Lnet/minecraft/item/Item$Settings;
 *   Lnet/minecraft/item/Item$Settings;enchantable(I)Lnet/minecraft/item/Item$Settings;
 *   Lnet/minecraft/registry/Registries;createEntryLookup(Lnet/minecraft/registry/Registry;)Lnet/minecraft/registry/RegistryEntryLookup;
 *   Lnet/minecraft/component/type/ToolComponent$Rule;ofNeverDropping(Lnet/minecraft/registry/entry/RegistryEntryList;)Lnet/minecraft/component/type/ToolComponent$Rule;
 *   Lnet/minecraft/component/type/ToolComponent$Rule;ofAlwaysDropping(Lnet/minecraft/registry/entry/RegistryEntryList;F)Lnet/minecraft/component/type/ToolComponent$Rule;
 *   Lnet/minecraft/item/Item$Settings;component(Lnet/minecraft/component/ComponentType;Ljava/lang/Object;)Lnet/minecraft/item/Item$Settings;
 *   Lnet/minecraft/item/Item$Settings;attributeModifiers(Lnet/minecraft/component/type/AttributeModifiersComponent;)Lnet/minecraft/item/Item$Settings;
 *   Lnet/minecraft/component/type/AttributeModifiersComponent;builder()Lnet/minecraft/component/type/AttributeModifiersComponent$Builder;
 *   Lnet/minecraft/component/type/AttributeModifiersComponent$Builder;build()Lnet/minecraft/component/type/AttributeModifiersComponent;
 *   Lnet/minecraft/registry/entry/RegistryEntryList;of([Lnet/minecraft/registry/entry/RegistryEntry;)Lnet/minecraft/registry/entry/RegistryEntryList$Direct;
 *   Lnet/minecraft/component/type/ToolComponent$Rule;of(Lnet/minecraft/registry/entry/RegistryEntryList;F)Lnet/minecraft/component/type/ToolComponent$Rule;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/item/ToolMaterial;applyBaseSettings(Lnet/minecraft/item/Item$Settings;)Lnet/minecraft/item/Item$Settings;
 *   Lnet/minecraft/item/ToolMaterial;createToolAttributeModifiers(FF)Lnet/minecraft/component/type/AttributeModifiersComponent;
 *   Lnet/minecraft/item/ToolMaterial;createSwordAttributeModifiers(FF)Lnet/minecraft/component/type/AttributeModifiersComponent;
 */
package net.minecraft.item;

import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.component.type.ToolComponent;
import net.minecraft.component.type.WeaponComponent;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.registry.tag.TagKey;

public record ToolMaterial(TagKey<Block> incorrectBlocksForDrops, int durability, float speed, float attackDamageBonus, int enchantmentValue, TagKey<Item> repairItems) {
    public static final ToolMaterial WOOD = new ToolMaterial(BlockTags.INCORRECT_FOR_WOODEN_TOOL, 59, 2.0f, 0.0f, 15, ItemTags.WOODEN_TOOL_MATERIALS);
    public static final ToolMaterial STONE = new ToolMaterial(BlockTags.INCORRECT_FOR_STONE_TOOL, 131, 4.0f, 1.0f, 5, ItemTags.STONE_TOOL_MATERIALS);
    public static final ToolMaterial COPPER = new ToolMaterial(BlockTags.INCORRECT_FOR_COPPER_TOOL, 190, 5.0f, 1.0f, 13, ItemTags.COPPER_TOOL_MATERIALS);
    public static final ToolMaterial IRON = new ToolMaterial(BlockTags.INCORRECT_FOR_IRON_TOOL, 250, 6.0f, 2.0f, 14, ItemTags.IRON_TOOL_MATERIALS);
    public static final ToolMaterial DIAMOND = new ToolMaterial(BlockTags.INCORRECT_FOR_DIAMOND_TOOL, 1561, 8.0f, 3.0f, 10, ItemTags.DIAMOND_TOOL_MATERIALS);
    public static final ToolMaterial GOLD = new ToolMaterial(BlockTags.INCORRECT_FOR_GOLD_TOOL, 32, 12.0f, 0.0f, 22, ItemTags.GOLD_TOOL_MATERIALS);
    public static final ToolMaterial NETHERITE = new ToolMaterial(BlockTags.INCORRECT_FOR_NETHERITE_TOOL, 2031, 9.0f, 4.0f, 15, ItemTags.NETHERITE_TOOL_MATERIALS);

    private Item.Settings applyBaseSettings(Item.Settings settings) {
        return settings.maxDamage(this.durability).repairable(this.repairItems).enchantable(this.enchantmentValue);
    }

    public Item.Settings applyToolSettings(Item.Settings settings, TagKey<Block> effectiveBlocks, float attackDamage, float attackSpeed, float disableBlockingForSeconds) {
        RegistryEntryLookup<Block> lv = Registries.createEntryLookup(Registries.BLOCK);
        return this.applyBaseSettings(settings).component(DataComponentTypes.TOOL, new ToolComponent(List.of(ToolComponent.Rule.ofNeverDropping(lv.getOrThrow(this.incorrectBlocksForDrops)), ToolComponent.Rule.ofAlwaysDropping(lv.getOrThrow(effectiveBlocks), this.speed)), 1.0f, 1, true)).attributeModifiers(this.createToolAttributeModifiers(attackDamage, attackSpeed)).component(DataComponentTypes.WEAPON, new WeaponComponent(2, disableBlockingForSeconds));
    }

    private AttributeModifiersComponent createToolAttributeModifiers(float attackDamage, float attackSpeed) {
        return AttributeModifiersComponent.builder().add(EntityAttributes.ATTACK_DAMAGE, new EntityAttributeModifier(Item.BASE_ATTACK_DAMAGE_MODIFIER_ID, attackDamage + this.attackDamageBonus, EntityAttributeModifier.Operation.ADD_VALUE), AttributeModifierSlot.MAINHAND).add(EntityAttributes.ATTACK_SPEED, new EntityAttributeModifier(Item.BASE_ATTACK_SPEED_MODIFIER_ID, attackSpeed, EntityAttributeModifier.Operation.ADD_VALUE), AttributeModifierSlot.MAINHAND).build();
    }

    public Item.Settings applySwordSettings(Item.Settings settings, float attackDamage, float attackSpeed) {
        RegistryEntryLookup<Block> lv = Registries.createEntryLookup(Registries.BLOCK);
        return this.applyBaseSettings(settings).component(DataComponentTypes.TOOL, new ToolComponent(List.of(ToolComponent.Rule.ofAlwaysDropping(RegistryEntryList.of(Blocks.COBWEB.getRegistryEntry()), 15.0f), ToolComponent.Rule.of(lv.getOrThrow(BlockTags.SWORD_INSTANTLY_MINES), Float.MAX_VALUE), ToolComponent.Rule.of(lv.getOrThrow(BlockTags.SWORD_EFFICIENT), 1.5f)), 1.0f, 2, false)).attributeModifiers(this.createSwordAttributeModifiers(attackDamage, attackSpeed)).component(DataComponentTypes.WEAPON, new WeaponComponent(1));
    }

    private AttributeModifiersComponent createSwordAttributeModifiers(float attackDamage, float attackSpeed) {
        return AttributeModifiersComponent.builder().add(EntityAttributes.ATTACK_DAMAGE, new EntityAttributeModifier(Item.BASE_ATTACK_DAMAGE_MODIFIER_ID, attackDamage + this.attackDamageBonus, EntityAttributeModifier.Operation.ADD_VALUE), AttributeModifierSlot.MAINHAND).add(EntityAttributes.ATTACK_SPEED, new EntityAttributeModifier(Item.BASE_ATTACK_SPEED_MODIFIER_ID, attackSpeed, EntityAttributeModifier.Operation.ADD_VALUE), AttributeModifierSlot.MAINHAND).build();
    }
}

