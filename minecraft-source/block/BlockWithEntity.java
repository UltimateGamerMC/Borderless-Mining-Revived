/*
 * External method calls:
 *   Lnet/minecraft/block/Block;onSyncedBlockEvent(Lnet/minecraft/block/BlockState;Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;II)Z
 *   Lnet/minecraft/block/entity/BlockEntity;onSyncedBlockEvent(II)Z
 */
package net.minecraft.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jspecify.annotations.Nullable;

public abstract class BlockWithEntity
extends Block
implements BlockEntityProvider {
    protected BlockWithEntity(AbstractBlock.Settings arg) {
        super(arg);
    }

    protected abstract MapCodec<? extends BlockWithEntity> getCodec();

    @Override
    protected boolean onSyncedBlockEvent(BlockState state, World world, BlockPos pos, int type, int data) {
        super.onSyncedBlockEvent(state, world, pos, type, data);
        BlockEntity lv = world.getBlockEntity(pos);
        if (lv == null) {
            return false;
        }
        return lv.onSyncedBlockEvent(type, data);
    }

    @Override
    protected @Nullable NamedScreenHandlerFactory createScreenHandlerFactory(BlockState state, World world, BlockPos pos) {
        BlockEntity lv = world.getBlockEntity(pos);
        return lv instanceof NamedScreenHandlerFactory ? (NamedScreenHandlerFactory)((Object)lv) : null;
    }

    protected static <E extends BlockEntity, A extends BlockEntity> @Nullable BlockEntityTicker<A> validateTicker(BlockEntityType<A> givenType, BlockEntityType<E> expectedType, BlockEntityTicker<? super E> ticker) {
        return expectedType == givenType ? ticker : null;
    }
}

