/*
 * External method calls:
 *   Lnet/minecraft/structure/pool/SinglePoolElement;createPlacementData(Lnet/minecraft/util/BlockRotation;Lnet/minecraft/util/math/BlockBox;Lnet/minecraft/structure/StructureLiquidSettings;Z)Lnet/minecraft/structure/StructurePlacementData;
 *   Lnet/minecraft/structure/StructurePlacementData;removeProcessor(Lnet/minecraft/structure/processor/StructureProcessor;)Lnet/minecraft/structure/StructurePlacementData;
 *   Lnet/minecraft/structure/StructurePlacementData;addProcessor(Lnet/minecraft/structure/processor/StructureProcessor;)Lnet/minecraft/structure/StructurePlacementData;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/structure/pool/LegacySinglePoolElement;locationGetter()Lcom/mojang/serialization/codecs/RecordCodecBuilder;
 *   Lnet/minecraft/structure/pool/LegacySinglePoolElement;processorsGetter()Lcom/mojang/serialization/codecs/RecordCodecBuilder;
 *   Lnet/minecraft/structure/pool/LegacySinglePoolElement;projectionGetter()Lcom/mojang/serialization/codecs/RecordCodecBuilder;
 *   Lnet/minecraft/structure/pool/LegacySinglePoolElement;overrideLiquidSettingsGetter()Lcom/mojang/serialization/codecs/RecordCodecBuilder;
 */
package net.minecraft.structure.pool;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Optional;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.structure.StructureLiquidSettings;
import net.minecraft.structure.StructurePlacementData;
import net.minecraft.structure.StructureTemplate;
import net.minecraft.structure.pool.SinglePoolElement;
import net.minecraft.structure.pool.StructurePool;
import net.minecraft.structure.pool.StructurePoolElementType;
import net.minecraft.structure.processor.BlockIgnoreStructureProcessor;
import net.minecraft.structure.processor.StructureProcessorList;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockBox;

public class LegacySinglePoolElement
extends SinglePoolElement {
    public static final MapCodec<LegacySinglePoolElement> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(LegacySinglePoolElement.locationGetter(), LegacySinglePoolElement.processorsGetter(), LegacySinglePoolElement.projectionGetter(), LegacySinglePoolElement.overrideLiquidSettingsGetter()).apply(instance, LegacySinglePoolElement::new));

    protected LegacySinglePoolElement(Either<Identifier, StructureTemplate> either, RegistryEntry<StructureProcessorList> arg, StructurePool.Projection arg2, Optional<StructureLiquidSettings> optional) {
        super(either, arg, arg2, optional);
    }

    @Override
    protected StructurePlacementData createPlacementData(BlockRotation rotation, BlockBox box, StructureLiquidSettings liquidSettings, boolean keepJigsaws) {
        StructurePlacementData lv = super.createPlacementData(rotation, box, liquidSettings, keepJigsaws);
        lv.removeProcessor(BlockIgnoreStructureProcessor.IGNORE_STRUCTURE_BLOCKS);
        lv.addProcessor(BlockIgnoreStructureProcessor.IGNORE_AIR_AND_STRUCTURE_BLOCKS);
        return lv;
    }

    @Override
    public StructurePoolElementType<?> getType() {
        return StructurePoolElementType.LEGACY_SINGLE_POOL_ELEMENT;
    }

    @Override
    public String toString() {
        return "LegacySingle[" + String.valueOf(this.location) + "]";
    }
}

