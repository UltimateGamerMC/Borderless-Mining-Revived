/*
 * External method calls:
 *   Lnet/minecraft/component/type/BundleContentsComponent;iterate()Ljava/lang/Iterable;
 *   Lnet/minecraft/predicate/collection/CollectionPredicate;test(Ljava/lang/Iterable;)Z
 *   Lnet/minecraft/predicate/collection/CollectionPredicate;createCodec(Lcom/mojang/serialization/Codec;)Lcom/mojang/serialization/Codec;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/predicate/item/BundleContentsPredicate;test(Lnet/minecraft/component/type/BundleContentsComponent;)Z
 */
package net.minecraft.predicate.item;

import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Optional;
import net.minecraft.component.ComponentType;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.BundleContentsComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.predicate.collection.CollectionPredicate;
import net.minecraft.predicate.component.ComponentSubPredicate;
import net.minecraft.predicate.item.ItemPredicate;

public record BundleContentsPredicate(Optional<CollectionPredicate<ItemStack, ItemPredicate>> items) implements ComponentSubPredicate<BundleContentsComponent>
{
    public static final Codec<BundleContentsPredicate> CODEC = RecordCodecBuilder.create(instance -> instance.group(CollectionPredicate.createCodec(ItemPredicate.CODEC).optionalFieldOf("items").forGetter(BundleContentsPredicate::items)).apply((Applicative<BundleContentsPredicate, ?>)instance, BundleContentsPredicate::new));

    @Override
    public ComponentType<BundleContentsComponent> getComponentType() {
        return DataComponentTypes.BUNDLE_CONTENTS;
    }

    @Override
    public boolean test(BundleContentsComponent arg) {
        return !this.items.isPresent() || this.items.get().test(arg.iterate());
    }
}

