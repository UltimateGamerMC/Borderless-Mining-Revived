/*
 * Internal private/static methods:
 *   Lnet/minecraft/block/TintedGlassBlock;createCodec(Ljava/util/function/Function;)Lcom/mojang/serialization/MapCodec;
 */
package net.minecraft.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.TransparentBlock;

public class TintedGlassBlock
extends TransparentBlock {
    public static final MapCodec<TintedGlassBlock> CODEC = TintedGlassBlock.createCodec(TintedGlassBlock::new);

    public MapCodec<TintedGlassBlock> getCodec() {
        return CODEC;
    }

    public TintedGlassBlock(AbstractBlock.Settings arg) {
        super(arg);
    }

    @Override
    protected boolean isTransparent(BlockState state) {
        return false;
    }

    @Override
    protected int getOpacity(BlockState state) {
        return 15;
    }
}

