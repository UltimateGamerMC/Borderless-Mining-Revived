/*
 * External method calls:
 *   Lnet/minecraft/util/dynamic/Codecs;rangedInclusiveFloat(FF)Lcom/mojang/serialization/Codec;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/block/MangroveLeavesBlock;createSettingsCodec()Lcom/mojang/serialization/codecs/RecordCodecBuilder;
 */
package net.minecraft.block;

import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Fertilizable;
import net.minecraft.block.PropaguleBlock;
import net.minecraft.block.TintedParticleLeavesBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.dynamic.Codecs;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;

public class MangroveLeavesBlock
extends TintedParticleLeavesBlock
implements Fertilizable {
    public static final MapCodec<MangroveLeavesBlock> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(((MapCodec)Codecs.rangedInclusiveFloat(0.0f, 1.0f).fieldOf("leaf_particle_chance")).forGetter(block -> Float.valueOf(block.leafParticleChance)), MangroveLeavesBlock.createSettingsCodec()).apply((Applicative<MangroveLeavesBlock, ?>)instance, MangroveLeavesBlock::new));

    public MapCodec<MangroveLeavesBlock> getCodec() {
        return CODEC;
    }

    public MangroveLeavesBlock(float f, AbstractBlock.Settings arg) {
        super(f, arg);
    }

    @Override
    public boolean isFertilizable(WorldView world, BlockPos pos, BlockState state) {
        return world.getBlockState(pos.down()).isAir();
    }

    @Override
    public boolean canGrow(World world, Random random, BlockPos pos, BlockState state) {
        return true;
    }

    @Override
    public void grow(ServerWorld world, Random random, BlockPos pos, BlockState state) {
        world.setBlockState(pos.down(), PropaguleBlock.getDefaultHangingState(), Block.NOTIFY_LISTENERS);
    }

    @Override
    public BlockPos getFertilizeParticlePos(BlockPos pos) {
        return pos.down();
    }
}

