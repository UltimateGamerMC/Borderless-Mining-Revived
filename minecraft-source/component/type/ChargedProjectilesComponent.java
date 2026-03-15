/*
 * External method calls:
 *   Lnet/minecraft/item/ItemStack;stacksEqual(Ljava/util/List;Ljava/util/List;)Z
 *   Lnet/minecraft/item/ItemStack;listHashCode(Ljava/util/List;)I
 *   Lnet/minecraft/item/ItemStack;areEqual(Lnet/minecraft/item/ItemStack;Lnet/minecraft/item/ItemStack;)Z
 *   Lnet/minecraft/item/ItemStack;toHoverableText()Lnet/minecraft/text/Text;
 *   Lnet/minecraft/text/Text;translatable(Ljava/lang/String;[Ljava/lang/Object;)Lnet/minecraft/text/MutableText;
 *   Lnet/minecraft/item/ItemStack;appendTooltip(Lnet/minecraft/item/Item$TooltipContext;Lnet/minecraft/component/type/TooltipDisplayComponent;Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/item/tooltip/TooltipType;Ljava/util/function/Consumer;)V
 *   Lnet/minecraft/text/Text;literal(Ljava/lang/String;)Lnet/minecraft/text/MutableText;
 *   Lnet/minecraft/text/MutableText;append(Lnet/minecraft/text/Text;)Lnet/minecraft/text/MutableText;
 *   Lnet/minecraft/text/MutableText;formatted(Lnet/minecraft/util/Formatting;)Lnet/minecraft/text/MutableText;
 *   Lnet/minecraft/network/codec/PacketCodecs;toList()Lnet/minecraft/network/codec/PacketCodec$ResultFunction;
 *   Lnet/minecraft/network/codec/PacketCodec;collect(Lnet/minecraft/network/codec/PacketCodec$ResultFunction;)Lnet/minecraft/network/codec/PacketCodec;
 *   Lnet/minecraft/network/codec/PacketCodec;xmap(Ljava/util/function/Function;Ljava/util/function/Function;)Lnet/minecraft/network/codec/PacketCodec;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/component/type/ChargedProjectilesComponent;appendProjectileTooltip(Lnet/minecraft/item/Item$TooltipContext;Ljava/util/function/Consumer;Lnet/minecraft/item/ItemStack;I)V
 */
package net.minecraft.component.type;

import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import java.util.List;
import java.util.function.Consumer;
import net.minecraft.component.ComponentsAccess;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.TooltipDisplayComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipAppender;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public final class ChargedProjectilesComponent
implements TooltipAppender {
    public static final ChargedProjectilesComponent DEFAULT = new ChargedProjectilesComponent(List.of());
    public static final Codec<ChargedProjectilesComponent> CODEC = ItemStack.CODEC.listOf().xmap(ChargedProjectilesComponent::new, arg -> arg.projectiles);
    public static final PacketCodec<RegistryByteBuf, ChargedProjectilesComponent> PACKET_CODEC = ItemStack.PACKET_CODEC.collect(PacketCodecs.toList()).xmap(ChargedProjectilesComponent::new, component -> component.projectiles);
    private final List<ItemStack> projectiles;

    private ChargedProjectilesComponent(List<ItemStack> projectiles) {
        this.projectiles = projectiles;
    }

    public static ChargedProjectilesComponent of(ItemStack projectile) {
        return new ChargedProjectilesComponent(List.of(projectile.copy()));
    }

    public static ChargedProjectilesComponent of(List<ItemStack> projectiles) {
        return new ChargedProjectilesComponent(List.copyOf(Lists.transform(projectiles, ItemStack::copy)));
    }

    public boolean contains(Item item) {
        for (ItemStack lv : this.projectiles) {
            if (!lv.isOf(item)) continue;
            return true;
        }
        return false;
    }

    public List<ItemStack> getProjectiles() {
        return Lists.transform(this.projectiles, ItemStack::copy);
    }

    public boolean isEmpty() {
        return this.projectiles.isEmpty();
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ChargedProjectilesComponent)) return false;
        ChargedProjectilesComponent lv = (ChargedProjectilesComponent)o;
        if (!ItemStack.stacksEqual(this.projectiles, lv.projectiles)) return false;
        return true;
    }

    public int hashCode() {
        return ItemStack.listHashCode(this.projectiles);
    }

    public String toString() {
        return "ChargedProjectiles[items=" + String.valueOf(this.projectiles) + "]";
    }

    @Override
    public void appendTooltip(Item.TooltipContext context, Consumer<Text> textConsumer, TooltipType type, ComponentsAccess components) {
        ItemStack lv = null;
        int i = 0;
        for (ItemStack lv2 : this.projectiles) {
            if (lv == null) {
                lv = lv2;
                i = 1;
                continue;
            }
            if (ItemStack.areEqual(lv, lv2)) {
                ++i;
                continue;
            }
            ChargedProjectilesComponent.appendProjectileTooltip(context, textConsumer, lv, i);
            lv = lv2;
            i = 1;
        }
        if (lv != null) {
            ChargedProjectilesComponent.appendProjectileTooltip(context, textConsumer, lv, i);
        }
    }

    private static void appendProjectileTooltip(Item.TooltipContext context, Consumer<Text> textConsumer, ItemStack projectile, int count) {
        if (count == 1) {
            textConsumer.accept(Text.translatable("item.minecraft.crossbow.projectile.single", projectile.toHoverableText()));
        } else {
            textConsumer.accept(Text.translatable("item.minecraft.crossbow.projectile.multiple", count, projectile.toHoverableText()));
        }
        TooltipDisplayComponent lv = projectile.getOrDefault(DataComponentTypes.TOOLTIP_DISPLAY, TooltipDisplayComponent.DEFAULT);
        projectile.appendTooltip(context, lv, null, TooltipType.BASIC, tooltip -> textConsumer.accept(Text.literal("  ").append((Text)tooltip).formatted(Formatting.GRAY)));
    }
}

