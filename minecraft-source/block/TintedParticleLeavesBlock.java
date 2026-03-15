/*
 * External method calls:
 *   Lnet/minecraft/particle/TintedParticleEffect;create(Lnet/minecraft/particle/ParticleType;I)Lnet/minecraft/particle/TintedParticleEffect;
 *   Lnet/minecraft/particle/ParticleUtil;spawnParticle(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/math/random/Random;Lnet/minecraft/particle/ParticleEffect;)V
 *   Lnet/minecraft/util/dynamic/Codecs;rangedInclusiveFloat(FF)Lcom/mojang/serialization/Codec;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/block/TintedParticleLeavesBlock;createSettingsCodec()Lcom/mojang/serialization/codecs/RecordCodecBuilder;
 */
package net.minecraft.block;

import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.LeavesBlock;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.particle.ParticleUtil;
import net.minecraft.particle.TintedParticleEffect;
import net.minecraft.util.dynamic.Codecs;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

public class TintedParticleLeavesBlock
extends LeavesBlock {
    public static final MapCodec<TintedParticleLeavesBlock> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(((MapCodec)Codecs.rangedInclusiveFloat(0.0f, 1.0f).fieldOf("leaf_particle_chance")).forGetter(arg -> Float.valueOf(arg.leafParticleChance)), TintedParticleLeavesBlock.createSettingsCodec()).apply((Applicative<TintedParticleLeavesBlock, ?>)instance, TintedParticleLeavesBlock::new));

    public TintedParticleLeavesBlock(float f, AbstractBlock.Settings arg) {
        super(f, arg);
    }

    @Override
    protected void spawnLeafParticle(World world, BlockPos pos, Random random) {
        TintedParticleEffect lv = TintedParticleEffect.create(ParticleTypes.TINTED_LEAVES, world.getBlockColor(pos));
        ParticleUtil.spawnParticle(world, pos, random, lv);
    }

    public MapCodec<? extends TintedParticleLeavesBlock> getCodec() {
        return CODEC;
    }
}

