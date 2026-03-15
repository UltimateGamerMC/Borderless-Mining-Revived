/*
 * External method calls:
 *   Lnet/minecraft/block/BlockState;with(Lnet/minecraft/state/property/Property;Ljava/lang/Comparable;)Ljava/lang/Object;
 *   Lnet/minecraft/world/World;updateNeighbors(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/Block;)V
 *
 * Internal private/static methods:
 *   Lnet/minecraft/world/DefaultRedstoneController;calculateTotalPowerAt(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;)I
 *   Lnet/minecraft/world/DefaultRedstoneController;calculateWirePowerAt(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;)I
 */
package net.minecraft.world;

import com.google.common.collect.Sets;
import java.util.HashSet;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.RedstoneWireBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.RedstoneController;
import net.minecraft.world.World;
import net.minecraft.world.block.WireOrientation;
import org.jspecify.annotations.Nullable;

public class DefaultRedstoneController
extends RedstoneController {
    public DefaultRedstoneController(RedstoneWireBlock arg) {
        super(arg);
    }

    @Override
    public void update(World world, BlockPos pos, BlockState state, @Nullable WireOrientation orientation, boolean blockAdded) {
        int i = this.calculateTotalPowerAt(world, pos);
        if (state.get(RedstoneWireBlock.POWER) != i) {
            if (world.getBlockState(pos) == state) {
                world.setBlockState(pos, (BlockState)state.with(RedstoneWireBlock.POWER, i), Block.NOTIFY_LISTENERS);
            }
            HashSet<BlockPos> set = Sets.newHashSet();
            set.add(pos);
            for (Direction lv : Direction.values()) {
                set.add(pos.offset(lv));
            }
            for (BlockPos lv2 : set) {
                world.updateNeighbors(lv2, this.wire);
            }
        }
    }

    private int calculateTotalPowerAt(World world, BlockPos pos) {
        int i = this.getStrongPowerAt(world, pos);
        if (i == 15) {
            return i;
        }
        return Math.max(i, this.calculateWirePowerAt(world, pos));
    }
}

