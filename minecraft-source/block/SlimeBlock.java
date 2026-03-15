/*
 * External method calls:
 *   Lnet/minecraft/entity/damage/DamageSources;fall()Lnet/minecraft/entity/damage/DamageSource;
 *   Lnet/minecraft/entity/Entity;handleFallDamage(DFLnet/minecraft/entity/damage/DamageSource;)Z
 *   Lnet/minecraft/block/TranslucentBlock;onEntityLand(Lnet/minecraft/world/BlockView;Lnet/minecraft/entity/Entity;)V
 *   Lnet/minecraft/block/TranslucentBlock;onSteppedOn(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;Lnet/minecraft/entity/Entity;)V
 *
 * Internal private/static methods:
 *   Lnet/minecraft/block/SlimeBlock;bounce(Lnet/minecraft/entity/Entity;)V
 *   Lnet/minecraft/block/SlimeBlock;createCodec(Ljava/util/function/Function;)Lcom/mojang/serialization/MapCodec;
 */
package net.minecraft.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.TranslucentBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class SlimeBlock
extends TranslucentBlock {
    public static final MapCodec<SlimeBlock> CODEC = SlimeBlock.createCodec(SlimeBlock::new);

    public MapCodec<SlimeBlock> getCodec() {
        return CODEC;
    }

    public SlimeBlock(AbstractBlock.Settings arg) {
        super(arg);
    }

    @Override
    public void onLandedUpon(World world, BlockState state, BlockPos pos, Entity entity, double fallDistance) {
        if (!entity.bypassesLandingEffects()) {
            entity.handleFallDamage(fallDistance, 0.0f, world.getDamageSources().fall());
        }
    }

    @Override
    public void onEntityLand(BlockView world, Entity entity) {
        if (entity.bypassesLandingEffects()) {
            super.onEntityLand(world, entity);
        } else {
            this.bounce(entity);
        }
    }

    private void bounce(Entity entity) {
        Vec3d lv = entity.getVelocity();
        if (lv.y < 0.0) {
            double d = entity instanceof LivingEntity ? 1.0 : 0.8;
            entity.setVelocity(lv.x, -lv.y * d, lv.z);
        }
    }

    @Override
    public void onSteppedOn(World world, BlockPos pos, BlockState state, Entity entity) {
        double d = Math.abs(entity.getVelocity().y);
        if (d < 0.1 && !entity.bypassesSteppingEffects()) {
            double e = 0.4 + d * 0.2;
            entity.setVelocity(entity.getVelocity().multiply(e, 1.0, e));
        }
        super.onSteppedOn(world, pos, state, entity);
    }
}

