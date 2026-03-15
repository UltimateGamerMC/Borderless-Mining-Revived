/*
 * External method calls:
 *   Lnet/minecraft/util/Util;allOf(Ljava/util/List;)Ljava/util/function/Predicate;
 *   Lnet/minecraft/loot/context/LootEntityValueSource;contextParam()Lnet/minecraft/util/context/ContextParameter;
 *   Lnet/minecraft/component/ComponentMap;filtered(Ljava/util/function/Predicate;)Lnet/minecraft/component/ComponentMap;
 *   Lnet/minecraft/item/ItemStack;applyComponentsFrom(Lnet/minecraft/component/ComponentMap;)V
 *   Lnet/minecraft/registry/Registry;streamEntries()Ljava/util/stream/Stream;
 *   Lnet/minecraft/loot/context/LootEntityValueSource$Builder;forEntities(Ljava/util/function/Function;)Lnet/minecraft/loot/context/LootEntityValueSource$Builder;
 *   Lnet/minecraft/loot/context/LootEntityValueSource$Builder;forBlockEntities(Ljava/util/function/Function;)Lnet/minecraft/loot/context/LootEntityValueSource$Builder;
 *   Lnet/minecraft/loot/context/LootEntityValueSource$Builder;forItemStacks(Ljava/util/function/Function;)Lnet/minecraft/loot/context/LootEntityValueSource$Builder;
 *   Lnet/minecraft/loot/context/LootEntityValueSource;createCodec(Ljava/util/function/UnaryOperator;)Lcom/mojang/serialization/Codec;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/loot/function/CopyComponentsLootFunction;addConditionsField(Lcom/mojang/serialization/codecs/RecordCodecBuilder$Instance;)Lcom/mojang/datafixers/Products$P1;
 */
package net.minecraft.loot.function;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.component.Component;
import net.minecraft.component.ComponentMap;
import net.minecraft.component.ComponentType;
import net.minecraft.component.ComponentsAccess;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootEntityValueSource;
import net.minecraft.loot.function.ConditionalLootFunction;
import net.minecraft.loot.function.LootFunction;
import net.minecraft.loot.function.LootFunctionType;
import net.minecraft.loot.function.LootFunctionTypes;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Util;
import net.minecraft.util.context.ContextParameter;

/*
 * Duplicate member names - consider using --renamedupmembers true
 */
public class CopyComponentsLootFunction
extends ConditionalLootFunction {
    private static final Codec<LootEntityValueSource<ComponentsAccess>> CODEC = LootEntityValueSource.createCodec(builder -> builder.forEntities(ComponentAccessSource::new).forBlockEntities(BlockEntityComponentsSource::new).forItemStacks(ComponentAccessSource::new));
    public static final MapCodec<CopyComponentsLootFunction> CODEC;
    private final LootEntityValueSource<ComponentsAccess> source;
    private final Optional<List<ComponentType<?>>> include;
    private final Optional<List<ComponentType<?>>> exclude;
    private final Predicate<ComponentType<?>> filter;

    CopyComponentsLootFunction(List<LootCondition> conditions, LootEntityValueSource<ComponentsAccess> source, Optional<List<ComponentType<?>>> include, Optional<List<ComponentType<?>>> exclude) {
        super(conditions);
        this.source = source;
        this.include = include.map(List::copyOf);
        this.exclude = exclude.map(List::copyOf);
        ArrayList list2 = new ArrayList(2);
        exclude.ifPresent(excludedTypes -> list2.add(type -> !excludedTypes.contains(type)));
        include.ifPresent(includedTypes -> list2.add(includedTypes::contains));
        this.filter = Util.allOf(list2);
    }

    public LootFunctionType<CopyComponentsLootFunction> getType() {
        return LootFunctionTypes.COPY_COMPONENTS;
    }

    @Override
    public Set<ContextParameter<?>> getAllowedParameters() {
        return Set.of(this.source.contextParam());
    }

    @Override
    public ItemStack process(ItemStack stack, LootContext context) {
        ComponentsAccess lv = this.source.get(context);
        if (lv != null) {
            if (lv instanceof ComponentMap) {
                ComponentMap lv2 = (ComponentMap)lv;
                stack.applyComponentsFrom(lv2.filtered(this.filter));
            } else {
                Collection collection = this.exclude.orElse(List.of());
                this.include.map(Collection::stream).orElse(Registries.DATA_COMPONENT_TYPE.streamEntries().map(RegistryEntry::value)).forEach(type -> {
                    if (collection.contains(type)) {
                        return;
                    }
                    Component lv = lv.getTyped(type);
                    if (lv != null) {
                        stack.set(lv);
                    }
                });
            }
        }
        return stack;
    }

    public static Builder entity(ContextParameter<? extends Entity> parameter) {
        return new Builder(new ComponentAccessSource<Entity>(parameter));
    }

    public static Builder blockEntity(ContextParameter<? extends BlockEntity> parameter) {
        return new Builder(new BlockEntityComponentsSource(parameter));
    }

    static {
        CODEC = RecordCodecBuilder.mapCodec(instance -> CopyComponentsLootFunction.addConditionsField(instance).and(instance.group(((MapCodec)CODEC.fieldOf("source")).forGetter(function -> function.source), ComponentType.CODEC.listOf().optionalFieldOf("include").forGetter(function -> function.include), ComponentType.CODEC.listOf().optionalFieldOf("exclude").forGetter(function -> function.exclude))).apply((Applicative<CopyComponentsLootFunction, ?>)instance, CopyComponentsLootFunction::new));
    }

    public static class Builder
    extends ConditionalLootFunction.Builder<Builder> {
        private final LootEntityValueSource<ComponentsAccess> source;
        private Optional<ImmutableList.Builder<ComponentType<?>>> include = Optional.empty();
        private Optional<ImmutableList.Builder<ComponentType<?>>> exclude = Optional.empty();

        Builder(LootEntityValueSource<ComponentsAccess> source) {
            this.source = source;
        }

        public Builder include(ComponentType<?> type) {
            if (this.include.isEmpty()) {
                this.include = Optional.of(ImmutableList.builder());
            }
            this.include.get().add((Object)type);
            return this;
        }

        public Builder exclude(ComponentType<?> type) {
            if (this.exclude.isEmpty()) {
                this.exclude = Optional.of(ImmutableList.builder());
            }
            this.exclude.get().add((Object)type);
            return this;
        }

        @Override
        protected Builder getThisBuilder() {
            return this;
        }

        @Override
        public LootFunction build() {
            return new CopyComponentsLootFunction(this.getConditions(), this.source, this.include.map(ImmutableList.Builder::build), this.exclude.map(ImmutableList.Builder::build));
        }

        @Override
        protected /* synthetic */ ConditionalLootFunction.Builder getThisBuilder() {
            return this.getThisBuilder();
        }
    }

    record ComponentAccessSource<T extends ComponentsAccess>(ContextParameter<? extends T> contextParam) implements LootEntityValueSource.ContextComponentBased<T, ComponentsAccess>
    {
        @Override
        public ComponentsAccess get(T arg) {
            return arg;
        }
    }

    record BlockEntityComponentsSource(ContextParameter<? extends BlockEntity> contextParam) implements LootEntityValueSource.ContextComponentBased<BlockEntity, ComponentsAccess>
    {
        @Override
        public ComponentsAccess get(BlockEntity arg) {
            return arg.createComponentMap();
        }
    }
}

