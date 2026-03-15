/*
 * External method calls:
 *   Lnet/minecraft/world/gen/chunk/ChunkNoiseSampler$BlockStateSampler;sample(Lnet/minecraft/world/gen/densityfunction/DensityFunction$NoisePos;)Lnet/minecraft/block/BlockState;
 */
package net.minecraft.world.gen;

import net.minecraft.block.BlockState;
import net.minecraft.world.gen.chunk.ChunkNoiseSampler;
import net.minecraft.world.gen.densityfunction.DensityFunction;
import org.jspecify.annotations.Nullable;

public record ChainedBlockSource(ChunkNoiseSampler.BlockStateSampler[] samplers) implements ChunkNoiseSampler.BlockStateSampler
{
    @Override
    public @Nullable BlockState sample(DensityFunction.NoisePos pos) {
        for (ChunkNoiseSampler.BlockStateSampler lv : this.samplers) {
            BlockState lv2 = lv.sample(pos);
            if (lv2 == null) continue;
            return lv2;
        }
        return null;
    }
}

