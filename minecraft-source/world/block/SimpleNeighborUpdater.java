/*
 * External method calls:
 *   Lnet/minecraft/world/block/NeighborUpdater;replaceWithStateForNeighborUpdate(Lnet/minecraft/world/WorldAccess;Lnet/minecraft/util/math/Direction;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;II)V
 *   Lnet/minecraft/world/block/NeighborUpdater;tryNeighborUpdate(Lnet/minecraft/world/World;Lnet/minecraft/block/BlockState;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/Block;Lnet/minecraft/world/block/WireOrientation;Z)V
 *
 * Internal private/static methods:
 *   Lnet/minecraft/world/block/SimpleNeighborUpdater;updateNeighbor(Lnet/minecraft/block/BlockState;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/Block;Lnet/minecraft/world/block/WireOrientation;Z)V
 */
package net.minecraft.world.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.block.NeighborUpdater;
import net.minecraft.world.block.WireOrientation;
import org.jspecify.annotations.Nullable;

public class SimpleNeighborUpdater
implements NeighborUpdater {
    private final World world;

    public SimpleNeighborUpdater(World world) {
        this.world = world;
    }

    @Override
    public void replaceWithStateForNeighborUpdate(Direction direction, BlockState neighborState, BlockPos pos, BlockPos neighborPos, @Block.SetBlockStateFlag int flags, int maxUpdateDepth) {
        NeighborUpdater.replaceWithStateForNeighborUpdate(this.world, direction, pos, neighborPos, neighborState, flags, maxUpdateDepth - 1);
    }

    @Override
    public void updateNeighbor(BlockPos pos, Block sourceBlock, @Nullable WireOrientation orientation) {
        BlockState lv = this.world.getBlockState(pos);
        this.updateNeighbor(lv, pos, sourceBlock, orientation, false);
    }

    @Override
    public void updateNeighbor(BlockState state, BlockPos pos, Block sourceBlock, @Nullable WireOrientation orientation, boolean notify) {
        NeighborUpdater.tryNeighborUpdate(this.world, state, pos, sourceBlock, orientation, notify);
    }
}

