/*
 * External method calls:
 *   Lnet/minecraft/block/BlockState;with(Lnet/minecraft/state/property/Property;Ljava/lang/Comparable;)Ljava/lang/Object;
 *   Lnet/minecraft/block/PillarBlock;changeRotation(Lnet/minecraft/block/BlockState;Lnet/minecraft/util/BlockRotation;)Lnet/minecraft/block/BlockState;
 *
 * Internal private/static methods:
 *   Lnet/minecraft/block/RotatedInfestedBlock;createSettingsCodec()Lcom/mojang/serialization/codecs/RecordCodecBuilder;
 */
package net.minecraft.block;

import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.InfestedBlock;
import net.minecraft.block.PillarBlock;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.registry.Registries;
import net.minecraft.state.StateManager;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.Direction;

public class RotatedInfestedBlock
extends InfestedBlock {
    public static final MapCodec<RotatedInfestedBlock> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(((MapCodec)Registries.BLOCK.getCodec().fieldOf("host")).forGetter(InfestedBlock::getRegularBlock), RotatedInfestedBlock.createSettingsCodec()).apply((Applicative<RotatedInfestedBlock, ?>)instance, RotatedInfestedBlock::new));

    public MapCodec<RotatedInfestedBlock> getCodec() {
        return CODEC;
    }

    public RotatedInfestedBlock(Block arg, AbstractBlock.Settings arg2) {
        super(arg, arg2);
        this.setDefaultState((BlockState)this.getDefaultState().with(PillarBlock.AXIS, Direction.Axis.Y));
    }

    @Override
    protected BlockState rotate(BlockState state, BlockRotation rotation) {
        return PillarBlock.changeRotation(state, rotation);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(PillarBlock.AXIS);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return (BlockState)this.getDefaultState().with(PillarBlock.AXIS, ctx.getSide().getAxis());
    }
}

