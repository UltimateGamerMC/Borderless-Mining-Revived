/*
 * External method calls:
 *   Lnet/minecraft/block/BlockState;with(Lnet/minecraft/state/property/Property;Ljava/lang/Comparable;)Ljava/lang/Object;
 *   Lnet/minecraft/entity/damage/DamageSources;fall()Lnet/minecraft/entity/damage/DamageSource;
 *   Lnet/minecraft/entity/Entity;handleFallDamage(DFLnet/minecraft/entity/damage/DamageSource;)Z
 *
 * Internal private/static methods:
 *   Lnet/minecraft/block/HayBlock;createCodec(Ljava/util/function/Function;)Lcom/mojang/serialization/MapCodec;
 */
package net.minecraft.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.PillarBlock;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class HayBlock
extends PillarBlock {
    public static final MapCodec<HayBlock> CODEC = HayBlock.createCodec(HayBlock::new);

    public MapCodec<HayBlock> getCodec() {
        return CODEC;
    }

    public HayBlock(AbstractBlock.Settings arg) {
        super(arg);
        this.setDefaultState((BlockState)((BlockState)this.stateManager.getDefaultState()).with(AXIS, Direction.Axis.Y));
    }

    @Override
    public void onLandedUpon(World world, BlockState state, BlockPos pos, Entity entity, double fallDistance) {
        entity.handleFallDamage(fallDistance, 0.2f, world.getDamageSources().fall());
    }
}

