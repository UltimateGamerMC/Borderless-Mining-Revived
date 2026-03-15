/*
 * External method calls:
 *   Lnet/minecraft/item/ItemStack;apply(Lnet/minecraft/component/ComponentType;Ljava/lang/Object;Ljava/util/function/UnaryOperator;)Ljava/lang/Object;
 *   Lnet/minecraft/component/type/WrittenBookContentComponent;pages()Ljava/util/List;
 *   Lnet/minecraft/util/collection/ListOperation;apply(Ljava/util/List;Ljava/util/List;)Ljava/util/List;
 *   Lnet/minecraft/component/type/WrittenBookContentComponent;withPages(Ljava/util/List;)Lnet/minecraft/component/type/WrittenBookContentComponent;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/loot/function/SetWrittenBookPagesLootFunction;addConditionsField(Lcom/mojang/serialization/codecs/RecordCodecBuilder$Instance;)Lcom/mojang/datafixers/Products$P1;
 */
package net.minecraft.loot.function;

import com.google.common.annotations.VisibleForTesting;
import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.List;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.WrittenBookContentComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.function.ConditionalLootFunction;
import net.minecraft.loot.function.LootFunctionType;
import net.minecraft.loot.function.LootFunctionTypes;
import net.minecraft.text.RawFilteredPair;
import net.minecraft.text.Text;
import net.minecraft.util.collection.ListOperation;

public class SetWrittenBookPagesLootFunction
extends ConditionalLootFunction {
    public static final MapCodec<SetWrittenBookPagesLootFunction> CODEC = RecordCodecBuilder.mapCodec(instance -> SetWrittenBookPagesLootFunction.addConditionsField(instance).and(instance.group(((MapCodec)WrittenBookContentComponent.PAGES_CODEC.fieldOf("pages")).forGetter(function -> function.pages), ListOperation.UNLIMITED_SIZE_CODEC.forGetter(function -> function.operation))).apply((Applicative<SetWrittenBookPagesLootFunction, ?>)instance, SetWrittenBookPagesLootFunction::new));
    private final List<RawFilteredPair<Text>> pages;
    private final ListOperation operation;

    protected SetWrittenBookPagesLootFunction(List<LootCondition> conditions, List<RawFilteredPair<Text>> pages, ListOperation operation) {
        super(conditions);
        this.pages = pages;
        this.operation = operation;
    }

    @Override
    protected ItemStack process(ItemStack stack, LootContext context) {
        stack.apply(DataComponentTypes.WRITTEN_BOOK_CONTENT, WrittenBookContentComponent.DEFAULT, this::apply);
        return stack;
    }

    @VisibleForTesting
    public WrittenBookContentComponent apply(WrittenBookContentComponent current) {
        List<RawFilteredPair<Text>> list = this.operation.apply(current.pages(), this.pages);
        return current.withPages((List)list);
    }

    public LootFunctionType<SetWrittenBookPagesLootFunction> getType() {
        return LootFunctionTypes.SET_WRITTEN_BOOK_PAGES;
    }
}

