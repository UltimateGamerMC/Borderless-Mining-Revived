/*
 * External method calls:
 *   Lnet/minecraft/component/type/ContainerComponent;iterateNonEmpty()Ljava/lang/Iterable;
 *   Lnet/minecraft/predicate/collection/CollectionPredicate;test(Ljava/lang/Iterable;)Z
 *   Lnet/minecraft/predicate/collection/CollectionPredicate;createCodec(Lcom/mojang/serialization/Codec;)Lcom/mojang/serialization/Codec;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/predicate/item/ContainerPredicate;test(Lnet/minecraft/component/type/ContainerComponent;)Z
 */
package net.minecraft.predicate.item;

import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Optional;
import net.minecraft.component.ComponentType;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ContainerComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.predicate.collection.CollectionPredicate;
import net.minecraft.predicate.component.ComponentSubPredicate;
import net.minecraft.predicate.item.ItemPredicate;

public record ContainerPredicate(Optional<CollectionPredicate<ItemStack, ItemPredicate>> items) implements ComponentSubPredicate<ContainerComponent>
{
    public static final Codec<ContainerPredicate> CODEC = RecordCodecBuilder.create(instance -> instance.group(CollectionPredicate.createCodec(ItemPredicate.CODEC).optionalFieldOf("items").forGetter(ContainerPredicate::items)).apply((Applicative<ContainerPredicate, ?>)instance, ContainerPredicate::new));

    @Override
    public ComponentType<ContainerComponent> getComponentType() {
        return DataComponentTypes.CONTAINER;
    }

    @Override
    public boolean test(ContainerComponent arg) {
        return !this.items.isPresent() || this.items.get().test(arg.iterateNonEmpty());
    }
}

