/*
 * External method calls:
 *   Lnet/minecraft/block/WitherSkullBlock;onPlaced(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;)V
 *
 * Internal private/static methods:
 *   Lnet/minecraft/block/WallWitherSkullBlock;createCodec(Ljava/util/function/Function;)Lcom/mojang/serialization/MapCodec;
 */
package net.minecraft.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.SkullBlock;
import net.minecraft.block.WallSkullBlock;
import net.minecraft.block.WitherSkullBlock;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jspecify.annotations.Nullable;

public class WallWitherSkullBlock
extends WallSkullBlock {
    public static final MapCodec<WallWitherSkullBlock> CODEC = WallWitherSkullBlock.createCodec(WallWitherSkullBlock::new);

    public MapCodec<WallWitherSkullBlock> getCodec() {
        return CODEC;
    }

    protected WallWitherSkullBlock(AbstractBlock.Settings arg) {
        super(SkullBlock.Type.WITHER_SKELETON, arg);
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        WitherSkullBlock.onPlaced(world, pos);
    }
}

