/*
 * External method calls:
 *   Lnet/minecraft/loot/slot/ItemStream;map(Ljava/util/function/Function;)Lnet/minecraft/loot/slot/ItemStream;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/loot/slot/ContentsSlotSource;addSlotSourceField(Lcom/mojang/serialization/codecs/RecordCodecBuilder$Instance;)Lcom/mojang/datafixers/Products$P1;
 */
package net.minecraft.loot.slot;

import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.loot.ContainerComponentModifier;
import net.minecraft.loot.ContainerComponentModifiers;
import net.minecraft.loot.slot.ItemStream;
import net.minecraft.loot.slot.SlotSource;
import net.minecraft.loot.slot.TransformSlotSource;

public class ContentsSlotSource
extends TransformSlotSource {
    public static final MapCodec<ContentsSlotSource> CODEC = RecordCodecBuilder.mapCodec(instance -> ContentsSlotSource.addSlotSourceField(instance).and(((MapCodec)ContainerComponentModifiers.MODIFIER_CODEC.fieldOf("component")).forGetter(source -> source.component)).apply((Applicative<ContentsSlotSource, ?>)instance, ContentsSlotSource::new));
    private final ContainerComponentModifier<?> component;

    private ContentsSlotSource(SlotSource slotSource, ContainerComponentModifier<?> component) {
        super(slotSource);
        this.component = component;
    }

    public MapCodec<ContentsSlotSource> getCodec() {
        return CODEC;
    }

    @Override
    protected ItemStream transform(ItemStream stream) {
        return stream.map(this.component::stream);
    }
}

