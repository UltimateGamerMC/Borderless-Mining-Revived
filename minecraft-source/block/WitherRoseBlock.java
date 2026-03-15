/*
 * External method calls:
 *   Lnet/minecraft/block/ShapeContext;absent()Lnet/minecraft/block/ShapeContext;
 *   Lnet/minecraft/world/World;addParticleClient(Lnet/minecraft/particle/ParticleEffect;DDDDDD)V
 *   Lnet/minecraft/entity/damage/DamageSources;wither()Lnet/minecraft/entity/damage/DamageSource;
 *   Lnet/minecraft/entity/LivingEntity;addStatusEffect(Lnet/minecraft/entity/effect/StatusEffectInstance;)Z
 *
 * Internal private/static methods:
 *   Lnet/minecraft/block/WitherRoseBlock;createStewEffectList(Lnet/minecraft/registry/entry/RegistryEntry;F)Lnet/minecraft/component/type/SuspiciousStewEffectsComponent;
 *   Lnet/minecraft/block/WitherRoseBlock;createSettingsCodec()Lcom/mojang/serialization/codecs/RecordCodecBuilder;
 */
package net.minecraft.block;

import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FlowerBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.component.type.SuspiciousStewEffectsComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCollisionHandler;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.Difficulty;
import net.minecraft.world.World;

public class WitherRoseBlock
extends FlowerBlock {
    public static final MapCodec<WitherRoseBlock> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(STEW_EFFECT_CODEC.forGetter(FlowerBlock::getStewEffects), WitherRoseBlock.createSettingsCodec()).apply((Applicative<WitherRoseBlock, ?>)instance, WitherRoseBlock::new));

    public MapCodec<WitherRoseBlock> getCodec() {
        return CODEC;
    }

    public WitherRoseBlock(RegistryEntry<StatusEffect> arg, float f, AbstractBlock.Settings arg2) {
        this(WitherRoseBlock.createStewEffectList(arg, f), arg2);
    }

    public WitherRoseBlock(SuspiciousStewEffectsComponent arg, AbstractBlock.Settings arg2) {
        super(arg, arg2);
    }

    @Override
    protected boolean canPlantOnTop(BlockState floor, BlockView world, BlockPos pos) {
        return super.canPlantOnTop(floor, world, pos) || floor.isOf(Blocks.NETHERRACK) || floor.isOf(Blocks.SOUL_SAND) || floor.isOf(Blocks.SOUL_SOIL);
    }

    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        VoxelShape lv = this.getOutlineShape(state, world, pos, ShapeContext.absent());
        Vec3d lv2 = lv.getBoundingBox().getCenter();
        double d = (double)pos.getX() + lv2.x;
        double e = (double)pos.getZ() + lv2.z;
        for (int i = 0; i < 3; ++i) {
            if (!random.nextBoolean()) continue;
            world.addParticleClient(ParticleTypes.SMOKE, d + random.nextDouble() / 5.0, (double)pos.getY() + (0.5 - random.nextDouble()), e + random.nextDouble() / 5.0, 0.0, 0.0, 0.0);
        }
    }

    @Override
    protected void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity, EntityCollisionHandler handler, boolean bl) {
        if (world instanceof ServerWorld) {
            LivingEntity lv2;
            ServerWorld lv = (ServerWorld)world;
            if (world.getDifficulty() != Difficulty.PEACEFUL && entity instanceof LivingEntity && !(lv2 = (LivingEntity)entity).isInvulnerableTo(lv, world.getDamageSources().wither())) {
                lv2.addStatusEffect(this.getContactEffect());
            }
        }
    }

    @Override
    public StatusEffectInstance getContactEffect() {
        return new StatusEffectInstance(StatusEffects.WITHER, 40);
    }
}

