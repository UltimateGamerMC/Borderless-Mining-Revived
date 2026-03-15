/*
 * External method calls:
 *   Lnet/minecraft/util/StringIdentifiable$EnumCodec;fieldOf(Ljava/lang/String;)Lcom/mojang/serialization/MapCodec;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/block/DyedCarpetBlock;createSettingsCodec()Lcom/mojang/serialization/codecs/RecordCodecBuilder;
 */
package net.minecraft.block;

import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.CarpetBlock;
import net.minecraft.util.DyeColor;

public class DyedCarpetBlock
extends CarpetBlock {
    public static final MapCodec<DyedCarpetBlock> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(((MapCodec)DyeColor.CODEC.fieldOf("color")).forGetter(DyedCarpetBlock::getDyeColor), DyedCarpetBlock.createSettingsCodec()).apply((Applicative<DyedCarpetBlock, ?>)instance, DyedCarpetBlock::new));
    private final DyeColor dyeColor;

    public MapCodec<DyedCarpetBlock> getCodec() {
        return CODEC;
    }

    protected DyedCarpetBlock(DyeColor dyeColor, AbstractBlock.Settings settings) {
        super(settings);
        this.dyeColor = dyeColor;
    }

    public DyeColor getDyeColor() {
        return this.dyeColor;
    }
}

