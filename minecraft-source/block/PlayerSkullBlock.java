/*
 * Internal private/static methods:
 *   Lnet/minecraft/block/PlayerSkullBlock;createCodec(Ljava/util/function/Function;)Lcom/mojang/serialization/MapCodec;
 */
package net.minecraft.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.SkullBlock;

public class PlayerSkullBlock
extends SkullBlock {
    public static final MapCodec<PlayerSkullBlock> CODEC = PlayerSkullBlock.createCodec(PlayerSkullBlock::new);

    public MapCodec<PlayerSkullBlock> getCodec() {
        return CODEC;
    }

    protected PlayerSkullBlock(AbstractBlock.Settings arg) {
        super(SkullBlock.Type.PLAYER, arg);
    }
}

