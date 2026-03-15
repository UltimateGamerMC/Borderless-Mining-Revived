/*
 * External method calls:
 *   Lnet/minecraft/world/gen/feature/SimpleBlockFeatureConfig;toPlace()Lnet/minecraft/world/gen/stateprovider/BlockStateProvider;
 *   Lnet/minecraft/block/TallPlantBlock;placeAt(Lnet/minecraft/world/WorldAccess;Lnet/minecraft/block/BlockState;Lnet/minecraft/util/math/BlockPos;I)V
 *   Lnet/minecraft/block/PaleMossCarpetBlock;placeAt(Lnet/minecraft/world/WorldAccess;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/math/random/Random;I)V
 *   Lnet/minecraft/world/StructureWorldAccess;scheduleBlockTick(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/Block;I)V
 */
package net.minecraft.world.gen.feature;

import com.mojang.serialization.Codec;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.PaleMossCarpetBlock;
import net.minecraft.block.TallPlantBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.SimpleBlockFeatureConfig;
import net.minecraft.world.gen.feature.util.FeatureContext;

public class SimpleBlockFeature
extends Feature<SimpleBlockFeatureConfig> {
    public SimpleBlockFeature(Codec<SimpleBlockFeatureConfig> codec) {
        super(codec);
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    @Override
    public boolean generate(FeatureContext<SimpleBlockFeatureConfig> context) {
        SimpleBlockFeatureConfig lv = context.getConfig();
        StructureWorldAccess lv2 = context.getWorld();
        BlockPos lv3 = context.getOrigin();
        BlockState lv4 = lv.toPlace().get(context.getRandom(), lv3);
        if (!lv4.canPlaceAt(lv2, lv3)) return false;
        if (lv4.getBlock() instanceof TallPlantBlock) {
            if (!lv2.isAir(lv3.up())) return false;
            TallPlantBlock.placeAt(lv2, lv4, lv3, Block.NOTIFY_LISTENERS);
        } else if (lv4.getBlock() instanceof PaleMossCarpetBlock) {
            PaleMossCarpetBlock.placeAt(lv2, lv3, lv2.getRandom(), Block.NOTIFY_LISTENERS);
        } else {
            lv2.setBlockState(lv3, lv4, Block.NOTIFY_LISTENERS);
        }
        if (!lv.scheduleTick()) return true;
        lv2.scheduleBlockTick(lv3, lv2.getBlockState(lv3).getBlock(), 1);
        return true;
    }
}

