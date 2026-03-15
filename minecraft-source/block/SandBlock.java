/*
 * External method calls:
 *   Lnet/minecraft/block/ColoredFallingBlock;randomDisplayTick(Lnet/minecraft/block/BlockState;Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/math/random/Random;)V
 *   Lnet/minecraft/sound/AmbientDesertBlockSounds;tryPlaySandSounds(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/math/random/Random;)V
 *
 * Internal private/static methods:
 *   Lnet/minecraft/block/SandBlock;createSettingsCodec()Lcom/mojang/serialization/codecs/RecordCodecBuilder;
 */
package net.minecraft.block;

import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.ColoredFallingBlock;
import net.minecraft.sound.AmbientDesertBlockSounds;
import net.minecraft.util.ColorCode;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

public class SandBlock
extends ColoredFallingBlock {
    public static final MapCodec<SandBlock> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(((MapCodec)ColorCode.CODEC.fieldOf("falling_dust_color")).forGetter(block -> block.color), SandBlock.createSettingsCodec()).apply((Applicative<SandBlock, ?>)instance, SandBlock::new));

    public MapCodec<SandBlock> getCodec() {
        return CODEC;
    }

    public SandBlock(ColorCode arg, AbstractBlock.Settings arg2) {
        super(arg, arg2);
    }

    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        super.randomDisplayTick(state, world, pos, random);
        AmbientDesertBlockSounds.tryPlaySandSounds(world, pos, random);
    }
}

