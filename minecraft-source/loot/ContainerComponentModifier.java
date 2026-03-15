/*
 * External method calls:
 *   Lnet/minecraft/item/ItemStack;capCount(I)V
 *
 * Internal private/static methods:
 *   Lnet/minecraft/loot/ContainerComponentModifier;apply(Ljava/lang/Object;Ljava/util/stream/Stream;)Ljava/lang/Object;
 *   Lnet/minecraft/loot/ContainerComponentModifier;apply(Lnet/minecraft/item/ItemStack;Ljava/lang/Object;Ljava/util/stream/Stream;)V
 *   Lnet/minecraft/loot/ContainerComponentModifier;stream(Ljava/lang/Object;)Ljava/util/stream/Stream;
 *   Lnet/minecraft/loot/ContainerComponentModifier;apply(Lnet/minecraft/item/ItemStack;Ljava/util/stream/Stream;)V
 */
package net.minecraft.loot;

import java.util.function.UnaryOperator;
import java.util.stream.Stream;
import net.minecraft.component.ComponentType;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.slot.ItemStream;

public interface ContainerComponentModifier<T> {
    public ComponentType<T> getComponentType();

    public T getDefault();

    public T apply(T var1, Stream<ItemStack> var2);

    public Stream<ItemStack> stream(T var1);

    default public void apply(ItemStack stack, T component, Stream<ItemStack> contents) {
        T object2 = stack.getOrDefault(this.getComponentType(), component);
        T object3 = this.apply(object2, contents);
        stack.set(this.getComponentType(), object3);
    }

    default public void apply(ItemStack stack, Stream<ItemStack> contents) {
        this.apply(stack, this.getDefault(), contents);
    }

    default public void apply(ItemStack stack, UnaryOperator<ItemStack> contentsOperator) {
        T object = stack.get(this.getComponentType());
        if (object != null) {
            UnaryOperator unaryOperator2 = contentStack -> {
                if (contentStack.isEmpty()) {
                    return contentStack;
                }
                ItemStack lv = (ItemStack)contentsOperator.apply((ItemStack)contentStack);
                lv.capCount(lv.getMaxCount());
                return lv;
            };
            this.apply(stack, this.stream(object).map(unaryOperator2));
        }
    }

    default public ItemStream stream(ItemStack stack) {
        return () -> {
            T object = stack.get(this.getComponentType());
            if (object != null) {
                return this.stream(object).filter(s -> !s.isEmpty());
            }
            return Stream.empty();
        };
    }
}

